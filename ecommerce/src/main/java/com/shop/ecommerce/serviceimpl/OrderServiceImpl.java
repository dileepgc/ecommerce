package com.shop.ecommerce.serviceimpl;

import com.shop.ecommerce.DTO.FetchOrderDTO;
import com.shop.ecommerce.DTO.GlobalMethodDTO;
import com.shop.ecommerce.DTO.PlaceOrderDTO;
import com.shop.ecommerce.Exception.GlobalException;
import com.shop.ecommerce.entity.*;
import com.shop.ecommerce.helperclasses.GlobalMethod;
import com.shop.ecommerce.repo.*;
//import com.shop.ecommerce.repo.OrderedAuditRepo;
import com.shop.ecommerce.service.JWTService;
import com.shop.ecommerce.service.OrderService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    JWTService jwtService;
    @Autowired
    OrderRepo orderRepo;
    @Autowired
    Product product;
    @Autowired
    TransactionRepo transactionRepo;
    @Autowired
    OrderStatusRepo orderStatusRepo;
    @Autowired
    UserRepo userRepo;
    @Autowired
    CartItemRepo cartItemRepo;
    @Autowired
    CartRepo cartRepo;
    @Autowired
    PromocodeRepo promocodeRepo;
    @Autowired
    WalletRepo walletRepo;
    @Autowired
    OrderPromocode orderPromocode;
    @Autowired
    OrderPromocodeRepo orderPromocodeRepo;
    @Autowired
    Transaction transaction;
//    @Autowired
//    Order order;
    //    @Autowired
//    Payment payment;
    @Autowired
    PaymentRepo paymentRepo;
    @Autowired
    ProductRepo productRepo;
    @Autowired
    OrderedItemsRepo orderedItemsRepo;
    @Autowired
    OrderedAudit orderedAudit;
    @Autowired
    OrderStatus orderStatus;
    @Autowired
    OrderedAuditRepo orderedAuditRepo;
    @Autowired
    WalletAudit walletAudit;
    @Autowired
    WalletAuditRepo walletAuditRepo;
    @Autowired
    AddressRepo addressRepo;
//    @Autowired
//    Address address;
    @Autowired
    GlobalMethod globalMethod;


    //    @Autowired
//    OrderedAuditRepo orderedAuditRepo;
    @Autowired
    OrderAuditRepo orderAuditRepo;

    // This method allows an admin to change the status of an order.
// It accepts the order ID, new status, and an authorization header for admin access.
// The status change is logged in the order audit, and the order's status is updated in the database.

    @Transactional
    public ResponseEntity changeStatus(int orderid, String status, String authorizationHeader) {
        try {
            GlobalMethodDTO globalMethodDTO = globalMethod.adminAccess(authorizationHeader);
            if (!globalMethodDTO.isAccess())
                return new ResponseEntity<>("Access Denied", HttpStatus.UNAUTHORIZED);

            Order order = orderRepo.findById(orderid).orElse(null);
            OrderStatus orderStatus = orderStatusRepo.findByOrder(order);
            List<OrderedAudit> orderedAudits = orderAuditRepo.findAllByOrder(order);
            OrderedAudit orderedAudit1 = orderedAudits.get(orderedAudits.size() - 1);
            OrderedAudit orderedAudit = new OrderedAudit();
            orderedAudit.setPrevious_audit(orderedAudit1.getPresent_status());
            orderedAudit.setOrder(order);
            orderedAudit.setPresent_status(status);
            orderAuditRepo.save(orderedAudit);

            orderStatus.setStatus(status);
            order.setOrderedStatus(status);
            orderStatusRepo.save(orderStatus);
            orderRepo.save(order);

            return new ResponseEntity<>("Status changed successfully", HttpStatus.OK);
        } catch (GlobalException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    // This method handles the order placement for a user.
    // It processes the cart items, applies applicable promocodes, checks the user's wallet balance,
    // and updates the order, payment, and wallet accordingly.
    // The status of the order is set as 'placed', and related order details are saved in the database.
    @Transactional
    public ResponseEntity doOrder(PlaceOrderDTO placeOrderDTO, String authorizationHeader) {
        try {
            GlobalMethodDTO globalMethodDTO = globalMethod.adminAccess(authorizationHeader);
            String username = globalMethodDTO.getUsername();

            if (globalMethodDTO.isAccess())
                return new ResponseEntity<>("Access Denied", HttpStatus.UNAUTHORIZED);

            User user = userRepo.findByUsername(username);
            String result = "";

            Cart cart = cartRepo.findByU(user);
            if (cart == null || cart.getCartItem().isEmpty()) {
                throw new GlobalException("Cart is empty or not found for user");
            }

            // Total amount before applying promocode
            double totalAmount = cart.getTotal_amount();
            double amountBeforeDiscount = totalAmount;
            List<CartItem> cartItems = cartItemRepo.findByCart(cart);

            // Applying product promocode
            if (placeOrderDTO.getProdPromo() != null && !placeOrderDTO.getProdPromo().isEmpty()) {
                Promocode promocode = promocodeRepo.findByCode(placeOrderDTO.getProdPromo());
                if (promocode == null) {
                    throw new GlobalException("Product promocode does not exist");
                }
                if (promocode.isExpiredStatus()) {
                    throw new GlobalException("Product promocode got expired");
                }

                Product product = promocode.getProduct();
                CartItem cartItem = cartItemRepo.findByProduct(product);

                if (cartItems.contains(cartItem)) {
                    int quantity = cartItem.getQuantity();
                    double priceBeforePromocode = quantity * product.getPrice();
                    double disc = (product.getPrice() * ((double) promocode.getDiscount() / 100));
                    double discountedPricePerPiece = product.getPrice() - disc;
                    double discountedPrice = quantity * discountedPricePerPiece;
                    totalAmount = totalAmount - priceBeforePromocode + discountedPrice;
                    result = "Product promo code applied of code: " + promocode.getCode();
                }
            }

            // Applying order promocode
            if (placeOrderDTO.getOrderPromo() != null && !placeOrderDTO.getOrderPromo().isEmpty()) {
                OrderPromocode orderPromocode = orderPromocodeRepo.findByCode(placeOrderDTO.getOrderPromo());
                if (orderPromocode == null) {
                    throw new GlobalException("Order promocode does not exist");
                }
                if (orderPromocode.isExpireStatus()) {
                    throw new GlobalException("Order promo code got expired");
                }

                if (totalAmount > 1000) {
                    double discount = (double) orderPromocode.getDiscount() / 100;
                    double discountedPrice = totalAmount * discount;
                    totalAmount -= discountedPrice;
                    result += " Order promo code applied of code " + orderPromocode.getCode();
                }
            }

            // Check wallet balance
            Wallet wallet = walletRepo.findByUser(user);
            if (totalAmount > wallet.getBalance()) {
                transaction.setStatus("Failed");
                transactionRepo.save(transaction);
                throw new GlobalException("Insufficient balance");
            }

            // Creating the Order
            Order order = new Order();
            order.setTotalAmount(totalAmount);
            order.setName(user.getFirstName() + " " + user.getLastName());
            order.setUser(user);
            order.setOrderedStatus("Placed");
            order.setAmountBeforeDiscount(amountBeforeDiscount);

            // Save the order and create associated items
            for (CartItem cartItem : cartItems) {
                OrderedItems orderedItems = new OrderedItems();
                orderedItems.setOrder(order);
                orderedItems.setName(cartItem.getProduct().getName());
                orderedItems.setPrice(cartItem.getProduct().getPrice());
                orderedItems.setQuantity(cartItem.getQuantity());
                orderedItems.setProduct(cartItem.getProduct());

                Product product = cartItem.getProduct();
                if (cartItem.getQuantity() > product.getStock()) {
                    throw new GlobalException("Stock is not available");
                }
                product.setStock(product.getStock() - cartItem.getQuantity());
                productRepo.save(product);

                orderedItemsRepo.save(orderedItems);
            }

            // Creating a new Payment entity
            Payment payment = new Payment();
            payment.setAmount(totalAmount);

            payment.setOrder(order);

            // Save the payment
            paymentRepo.save(payment);

            // Set the Address for the Order
            Address address = new Address();
            address.setOrder(order);
            address.setAddress(placeOrderDTO.getAddress());
            address.setUser(user);
            addressRepo.save(address);
            order.setAddress(address);

            // Create transaction for the order
            transaction.setOrder(order);
            transaction.setStatus("Success");
            transaction.setAmount(totalAmount);
            transactionRepo.save(transaction);

            // Wallet deduction
            double remainingBalance = wallet.getBalance() - totalAmount;
            wallet.setBalance(remainingBalance);
            walletRepo.save(wallet);

            // Wallet audit entry
            walletAudit.setDiffamount("-" + totalAmount);
            walletAudit.setWallet(wallet);
            walletAuditRepo.save(walletAudit);

            // Clear cart and update it
            cart.getCartItem().clear();
            cart.setQuantity(0);
            cart.setTotal_amount(0);
            cartRepo.save(cart);

            result = result + "Order placed successfully";

            // Save the order in the order repository
            orderRepo.save(order);

            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (GlobalException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // This method retrieves all orders placed by a user based on their username from the authorization header.
    // It returns the list of orders associated with the logged-in user.
    public ResponseEntity getOrders(String authorizationHeader) {
        try {
            GlobalMethodDTO globalMethodDTO = globalMethod.adminAccess(authorizationHeader);
            String username = globalMethodDTO.getUsername();
            if (globalMethodDTO.isAccess())
                return new ResponseEntity<>("Access Denied", HttpStatus.UNAUTHORIZED);


            User user = userRepo.findByUsername(username);

            List<Order> orders = orderRepo.findByUser(user);

            List<FetchOrderDTO> fetchOrderDTOList=new ArrayList<FetchOrderDTO>();
            for(Order order:orders)
            {
                FetchOrderDTO fetchOrderDTO=new FetchOrderDTO();
                fetchOrderDTO.setId(order.getId());
                fetchOrderDTO.setOrderedStatus(order.getOrderedStatus());
                fetchOrderDTO.setName(order.getName());
                fetchOrderDTO.setAmountBeforeDiscount(order.getAmountBeforeDiscount());
                System.out.println("++++++++++++++++++++++++++++++++++++++");
                fetchOrderDTO.setTotalAmount(order.getTotalAmount());
//                System.out.println(order.getAddress().getAddress());
//                fetchOrderDTO.setAddress(order.getAddress().getAddress());
                fetchOrderDTOList.add(fetchOrderDTO);
            }

            return new ResponseEntity(fetchOrderDTOList, HttpStatus.OK);
        } catch (GlobalException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // This method allows a user to cancel their order, provided certain conditions are met.
    // If the order has not been completed or cancelled, it will be marked as 'Cancelled', and the user is refunded.
    // The order's status and stock are also updated accordingly.
    @Transactional
    public ResponseEntity cancelOrder(int orderId, String authorizationHeader) {
        try {
            GlobalMethodDTO globalMethodDTO = globalMethod.adminAccess(authorizationHeader);
            String username = globalMethodDTO.getUsername();
            if (globalMethodDTO.isAccess())
                return new ResponseEntity<>("Access Denied", HttpStatus.UNAUTHORIZED);

            User user = userRepo.findByUsername(username);


            Order order = orderRepo.findById(orderId).orElse(null);
            String status = order.getOrderedStatus();
            System.out.println(order);
            System.out.println(status);

            if (order == null) {
                throw new GlobalException("Order not found");
            } else if (order.getUser().getId() != user.getId()) {
                throw new GlobalException("This order does not belongs to logged in user");
            }
            System.out.println(order.getOrderedStatus());
            if (status.trim().equalsIgnoreCase("completed")) {
                throw new GlobalException("Order delivery completed You cannot cancel");
            }

            if (status.trim().equalsIgnoreCase("cancelled")) {
                throw new GlobalException("Order is already cancelled");
            }

            if (order.getAmountBeforeDiscount() != order.getTotalAmount()) {
                throw new GlobalException("Promo code applied orders cannot be cancelled");
            }
            Wallet wallet = walletRepo.findByUserId(user.getId());
            wallet.setBalance(wallet.getBalance() + order.getTotalAmount());
            WalletAudit walletAudit1 = new WalletAudit();
            walletAudit1.setDiffamount("+" + order.getTotalAmount());
            walletAudit1.setWallet(wallet);
            walletRepo.save(wallet);
            walletAuditRepo.save(walletAudit1);


            order.setOrderedStatus("Cancelled");
            OrderStatus orderStatus1 = orderStatusRepo.findByOrder(order);
            orderStatus1.setStatus("Cancelled");


            List<OrderedAudit> orderedAuditList = orderedAuditRepo.findAllByOrderId(order.getId());
            OrderedAudit orderedAudit1 = orderedAuditList.get(orderedAuditList.size() - 1);

            System.out.println(orderedAudit1);
            OrderedAudit orderedAudit2 = new OrderedAudit();
            orderedAudit2.setPresent_status("Cancelled");
            orderedAudit2.setPrevious_audit(orderedAudit1.getPresent_status());
            orderedAudit2.setOrder(order);


            List<OrderedItems> orderedItemsList = orderedItemsRepo.findAllByOrder(order);

            for (OrderedItems orderedItems1 : orderedItemsList) {
                int productId = orderedItems1.getProduct().getId();
                Product product = productRepo.findById(productId).orElse(null);
                product.setStock(product.getStock() + orderedItems1.getQuantity());
                productRepo.save(product);
            }
            orderedAuditRepo.save(orderedAudit2);
            orderStatusRepo.save(orderStatus1);

            orderRepo.save(order);


            return new ResponseEntity("order cancelled successfully", HttpStatus.OK);
        } catch (GlobalException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    //NOt used this method
    public ResponseEntity deleteUser(int id, String authorizationHeader) {
        try {
            String token = authorizationHeader.replace("Bearer ", "");
            String role = jwtService.extractRole(token);
            if (!role.equalsIgnoreCase("admin"))
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

            User user = userRepo.findById(id).orElse(null);
            if (user == null) {
                throw new GlobalException("User not found for this id");
            } else {
                user.setIs_deleted(true);
                userRepo.save(user);
                return new ResponseEntity<>(" User is deleted", HttpStatus.OK);
            }
        } catch (RuntimeException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.OK);
        }
    }


}
