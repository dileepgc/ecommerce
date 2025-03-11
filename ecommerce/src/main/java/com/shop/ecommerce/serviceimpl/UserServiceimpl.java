package com.shop.ecommerce.serviceimpl;

import com.shop.ecommerce.DTO.ProdToCart;
import com.shop.ecommerce.DTO.SignUpDTO;
import com.shop.ecommerce.DTO.UserLoginDTO;
import com.shop.ecommerce.Exception.GlobalException;
import com.shop.ecommerce.entity.*;
import com.shop.ecommerce.repo.*;
import com.shop.ecommerce.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;


@Service
@Component

public class UserServiceimpl implements UserService {
    @Autowired
    UserRepo userRepo;
    @Autowired
    UserRepo ur;
    @Autowired
    CartRepo cr;
    @Autowired
    WalletRepo wr;
    @Autowired
    WalletAuditRepo walletAuditRepo;
    @Autowired
    ProductRepo productRepo;

    @Autowired
    CartRepo cartRepo;
    @Autowired
    WalletRepo walletRepo;
    @Autowired
    RoleRepo roleRepo;
    @Autowired
    OrderedItemsRepo orderedItemsRepo;
    @Autowired
    OrderedAuditRepo orderedAuditRepo;
    @Autowired
    OrderRepo orderRepo;
    @Autowired
    Order order;
    @Autowired
    OrderStatusRepo orderStatusRepo;
    @Autowired
    TransactionRepo transactionRepo;
    @Autowired
    Transaction transaction;
    @Autowired
    Payment payment;
    @Autowired
    OrderedAudit orderedAudit;
    @Autowired
    OrderStatus orderStatus;
    @Autowired
    PaymentRepo paymentRepo;
    @Autowired
    WalletAudit walletAudit;
    @Autowired
    OrderedItems orderedItems;
//    @Autowired
//    CartItem cartItem;

    @Autowired
    CartItemRepo cartItemRepo;


    @Autowired
    PromocodeRepo promocodeRepo;
    @Autowired
    OrderPromocodeRepo orderPromocodeRepo;


    public boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return Pattern.matches(emailRegex, email);
    }

    public static boolean isValidPassword(String password) {
        if (password.length() < 8) {
            return false;
        }
        boolean hasUppercase = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;

        // Loop through each character of the password and check conditions
        for (int i = 0; i < password.length(); i++) {
            char ch = password.charAt(i);

            if (Character.isUpperCase(ch)) {
                hasUppercase = true;
            } else if (Character.isDigit(ch)) {
                hasDigit = true;
            } else if (isSpecialCharacter(ch)) {
                hasSpecialChar = true;
            }
        }
        return hasUppercase && hasDigit && hasSpecialChar;
    }

    private static boolean isSpecialCharacter(char ch) {
        String specialCharacters = "!@#$%^&*()-_=+[{]}\\|;:'\",<.>/?";
        return specialCharacters.indexOf(ch) >= 0;
    }


    public String signUp(SignUpDTO signUpDTO) {
        System.out.println(signUpDTO.getPassword());
        User admin = roleRepo.findByRoleName("admin");
        System.out.println(signUpDTO.getEmail());
        if (!isValidEmail(signUpDTO.getEmail())) {
            throw new GlobalException("Email format is not correct");
        }
//        if (!isValidPassword(signUpDTO.getPassword())) {
//            throw new GlobalException("Password is not in valid format");
//        }
        if (admin != null && signUpDTO.getRole().equalsIgnoreCase("admin")) {
            throw new GlobalException("Admin already exists");
        }
        User user = new User(signUpDTO.getFirstName(), signUpDTO.getLastName(), signUpDTO.getPassword(), signUpDTO.getRole(), signUpDTO.getEmail());
        userRepo.save(user);
        Wallet wallet = new Wallet();
        Role role = new Role();
        if (signUpDTO.getRole().equalsIgnoreCase("admin")) {
            role.setRolename("admin");
        } else {
            role.setRolename("User");
        }
        role.setUser(user);
        roleRepo.save(role);

        wallet.setUser(user);//to add user to Wallet
        wr.save(wallet);
        WalletAudit walletAudit = new WalletAudit();
        walletAudit.setWallet(wallet);
        walletAuditRepo.save(walletAudit);

        Cart cart = new Cart();
        cart.setU(user);          //to add user to cart
        cr.save(cart);
        user.setWallet(wallet);

        ur.save(user);

        return "User account created";
    }


    public String updateUser(User user, int id) {
        User existingUser = ur.findById(id).orElse(null);
        if (existingUser == null) {
            return "User not found ";
        } else {
            if (user.getFirstName() != null) existingUser.setName(user.getFirstName());
            if (user.getLastName() != null) existingUser.setName(user.getLastName());
            ur.save(existingUser);
            return "Updated Successfully";
        }
    }

    public String loginUser(UserLoginDTO userLoginDTO, HttpSession session) {
        User u1 = ur.findByEmail(userLoginDTO.getEmail());

        if (session.getAttribute("user") != null) {
            return "User Already logged in";
        }


        if (u1 == null) {
            return "Email Does not exist";
        }


        if (u1.getPassword().trim().equals(userLoginDTO.getPassword().trim())) {
            session.setAttribute("user", u1);
            return "Login Successfully";
        }

        return "Incorrect password ";
    }

    public String userLogout(HttpSession session) {
        if (session.getAttribute("user") != null) {
            System.out.println(session.getAttribute("user"));
            session.invalidate();

            return "User Logged out";
        }
        return "user not logged in";
    }

    public boolean isEmailPresent(String email) {
        User u = ur.findByEmail(email);
        if (u == null) {
            return false;
        } else {
            return true;
        }
    }

    public ResponseEntity getProdinCart(HttpSession session) {
        try {
            if (session.getAttribute("user") == null) {
                throw new GlobalException("user must log in");
            }
            User user = (User) session.getAttribute("user");
            Cart cart = cartRepo.findByU(user);
            List<CartItem> cartItems = cartItemRepo.findByCart(cart);


            return new ResponseEntity(cartItems, HttpStatus.OK);
        } catch (GlobalException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.OK);
        }

    }

    public ResponseEntity addproductToCart(ProdToCart pdetails, HttpSession session) {
        try {

            if (session.getAttribute("user") == null) {
                return new ResponseEntity<>("user must log in", HttpStatus.OK);
            }
            User user1 = (User) session.getAttribute("user");
            int user_id = user1.getId();

            int pid = pdetails.getProd_id();
            int quantity = pdetails.getQuantity();
            Product product = productRepo.findById(pid).orElse(null);
            if (product == null) {
                return new ResponseEntity<>("product does not exist n", HttpStatus.OK);


            }

            CartItem cartItem = cartItemRepo.findByProduct(product);
            System.out.println(cartItem);
            if (cartItem == null) {
                CartItem cartItem1 = new CartItem();
                cartItem = cartItem1;

                cartItemRepo.save(cartItem);

            }

            cartItem.setProduct(product);
            cartItemRepo.save(cartItem);


            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            User user = ur.findById(user_id).orElse(null);  // Get User object by ID
            Cart cart = cartRepo.findByU(user);

            cart.setQuantity(cart.getQuantity() + quantity);
            double totalamount = (product.getPrice() * quantity);
            cart.setTotal_amount(cart.getTotal_amount() + totalamount);

            cartItemRepo.save(cartItem);
            cartItem.setCart(cart);
            cartRepo.save(cart);

            return new ResponseEntity<>("Product added successfully", HttpStatus.OK);


        } catch (GlobalException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
//
//    public static void main(String[] args) {
//        UserServiceimpl impl = new UserServiceimpl();
//        HttpSession session = new HttpSession() {
//            @Override
//            public long getCreationTime() {
//                return 0;
//            }
//
//            @Override
//            public String getId() {
//                return "";
//            }
//
//            @Override
//            public long getLastAccessedTime() {
//                return 0;
//            }
//
//            @Override
//            public ServletContext getServletContext() {
//                return null;
//            }
//
//            @Override
//            public void setMaxInactiveInterval(int i) {
//
//            }
//
//            @Override
//            public int getMaxInactiveInterval() {
//                return 0;
//            }
//
//            @Override
//            public Object getAttribute(String s) {
//                return null;
//            }
//
//            @Override
//            public Enumeration<String> getAttributeNames() {
//                return null;
//            }
//
//            @Override
//            public void setAttribute(String s, Object o) {
//
//            }
//
//            @Override
//            public void removeAttribute(String s) {
//
//            }
//
//            @Override
//            public void invalidate() {
//
//            }
//
//            @Override
//            public boolean isNew() {
//                return false;
//            }
//        };
//        impl.doOrder("virima",session);
//    }

    @Transactional
    public Object doOrder(String address, HttpSession session) {
        try {
//
            if (session.getAttribute("user") == null) {
                throw new GlobalException("User must login first");
            }
            String result = "";

            User sessionAttribute = (User) session.getAttribute("user");
            User user = userRepo.findById(sessionAttribute.getId()).orElse(null);

            Cart cart = cartRepo.findByU(user);
            if (cart == null) {
                return "Cart not found for user";
            }





            //total amount before applying promocode
            double totalAmount = cart.getTotal_amount();
            double amountBeforeDiscount=totalAmount;
            System.out.println("total amount before discount-------->" + totalAmount);
            List<CartItem> cartItems = cartItemRepo.findByCart(cart);

            Promocode promocode=promocodeRepo.findById(1).orElse(null);
            if(promocode!=null) {
                Product product1 = promocode.getProduct();

                CartItem cartItem1 = cartItemRepo.findByProduct(product1);

                if (cartItems.contains(cartItem1) && promocode.isExpiredStatus() == false) {
                    System.out.println("=************************************************888");
                    int quantity = cartItemRepo.findByProduct(product1).getQuantity();
                    double priceBeforePromocode = quantity * product1.getPrice();
                    System.out.println("product full quan price before discount" + priceBeforePromocode);
                    double disc = (product1.getPrice() * ((double) promocode.getDiscount() / 100));
                    System.out.println("disc------>" + disc);//5
                    double discountedPricePerPiece = product1.getPrice() - disc;
                    System.out.println(discountedPricePerPiece);//95
                    double discountedPrice = quantity * discountedPricePerPiece;
                    System.out.println(discountedPrice);//1140
                    totalAmount = totalAmount - priceBeforePromocode;//0
                    totalAmount = totalAmount + discountedPrice;//1140
                    result = result + "product promo code applied of code: " + promocode.getCode();
                }
            }


            OrderPromocode orderPromocode=orderPromocodeRepo.findById(1).orElse(null);
            if(orderPromocode!=null) {

                if (totalAmount > 1000 && orderPromocode.isExpireStatus() == false) {
                    double discount = (double) orderPromocode.getDiscount() / 100;
                    double discountedPrice = totalAmount * discount;

                    totalAmount = totalAmount - (discountedPrice);//1083
                    result += " Order promo code applied of code" + orderPromocode.getCode();
                }
            }
            if (cartItems.isEmpty()) {
                return "Cart is empty";
            }

            Wallet wallet = walletRepo.findByUser(user);

            if (totalAmount > wallet.getBalance()) {
                transaction.setStatus("Failed");
                transactionRepo.save(transaction);
                throw new GlobalException("Insufficient balance");
            }

            //adding items list
            for (CartItem cartItem : cartItems) {
                OrderedItems orderedItems = new OrderedItems();
                orderedItems.setOrder(order);
                orderedItems.setName(cartItem.getProduct().getName());
                orderedItems.setPrice(cartItem.getProduct().getPrice());
                orderedItems.setQuantity(cartItem.getQuantity());
                orderedItems.setProduct(cartItem.getProduct());
                Product product = cartItem.getProduct();
                if(cartItem.getQuantity()>product.getStock())
                {
                    throw new GlobalException("Stock is not available");
                }
                product.setStock(product.getStock() - cartItem.getQuantity());
                productRepo.save(product);

                orderedItemsRepo.save(orderedItems);
            }

            paymentRepo.save(payment);
            order.setTotalAmount(totalAmount);
            order.setName(user.getFirstName() + user.getLastName());
            order.setUser(user);
            order.setOrderedStatus("order placed successfully");
            order.setPayment(payment);
            order.setAddress(address);
            order.setAmountBeforeDiscount(amountBeforeDiscount);
            orderRepo.save(order);

            orderedAudit.setOrder(order);
            orderStatus.setOrder(order);
            orderStatus.setStatus(" placed");
            orderStatusRepo.save(orderStatus);

            transaction.setOrder(order);
            transaction.setStatus("Success");
            transaction.setAmount(totalAmount);
            transactionRepo.save(transaction);


            orderedAudit.setPresent_status(orderStatus.getStatus());
            orderedAuditRepo.save(orderedAudit);

            payment.setOrder(order);
            payment.setTransaction(transaction);
            paymentRepo.save(payment);
            //updating balance in wallet
            double remaining = (wallet.getBalance() - totalAmount);
            wallet.setBalance(remaining);
            walletRepo.save(wallet);
            //updating in walletaudit
            walletAudit.setDiffamount("-" + totalAmount);
            walletAudit.setWallet(wallet);
            walletAuditRepo.save(walletAudit);


            for (CartItem cartItem : cartItems) {
                cartItemRepo.delete(cartItem);
            }

            cart.setQuantity(0);
            cart.setTotal_amount(0);
            cartRepo.save(cart);
            result = result + "Order placed successfully";
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (GlobalException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    public String viewwallet(HttpSession session) {

        if (session.getAttribute("user") == null) {
            throw new GlobalException("User must login first");
        }
        User user = (User) session.getAttribute("user");
        Wallet wallet = walletRepo.findByUserId(user.getId());
        return wallet.getUser().getFirstName() + " wallet balance is" + wallet.getBalance();
    }

    public ResponseEntity<Object> myProfile(HttpSession session) {
        if (session.getAttribute("user") == null) {
            throw new GlobalException("User must login first");
        }
        User user = (User) session.getAttribute("user");
        System.out.println(user);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    public ResponseEntity getOrders(HttpSession session) {
        try {
            if (session.getAttribute("user") == null) {
                throw new GlobalException("User must login first");
            }
            User user = (User) session.getAttribute("user");

            List<Order> orders = orderRepo.findByUser(user);
            return new ResponseEntity(orders, HttpStatus.OK);
        } catch (GlobalException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Transactional
     public ResponseEntity cancelOrder(int orderId,HttpSession session)
    {
        try {
            if (session.getAttribute("user") == null) {
                throw new GlobalException("User must login first");
            }
            User user = (User) session.getAttribute("user");

            Order order= orderRepo.findById(orderId).orElse(null);
            if(order==null )
            {
                throw new GlobalException("Order not found");
            }
            if(order.getUser().getId()!=user.getId() )
            {
                throw new GlobalException("This order does not belongs to logged in user");
            }
            if(order.getOrderedStatus().equalsIgnoreCase("completed"))
            {
                throw new GlobalException("Order delivery completed You cannot cancel");
            }
            if(order.getAmountBeforeDiscount()!=order.getTotalAmount())
            {
                throw new GlobalException("Promo code applied orders cannot be cancelled");
            }
            Wallet wallet=walletRepo.findByUserId(user.getId());
            wallet.setBalance(wallet.getBalance()+order.getTotalAmount());
            WalletAudit walletAudit1=new WalletAudit();
            walletAudit1.setDiffamount("+"+order.getTotalAmount());
            walletAudit1.setWallet(wallet);
            walletRepo.save(wallet);
            walletAuditRepo.save(walletAudit1);


            order.setOrderedStatus("Cancelled");
            OrderStatus orderStatus1=orderStatusRepo.findByOrder(order);
            orderStatus1.setStatus("Cancelled");


            List<OrderedAudit> orderedAuditList=orderedAuditRepo.findAllByOrderId(order.getId());
            OrderedAudit orderedAudit1=orderedAuditList.get(orderedAuditList.size() - 1);

            System.out.println(orderedAudit1);
            OrderedAudit orderedAudit2=new OrderedAudit();
            orderedAudit2.setPresent_status("Cancelled");
            orderedAudit2.setPrevious_audit(orderedAudit2.getPresent_status());
            orderedAudit2.setOrder(order);


            List<OrderedItems> orderedItemsList=orderedItemsRepo.findAllByOrder(order);

            for (OrderedItems orderedItems1:orderedItemsList)
            {
                int productId=orderedItems1.getProduct().getId();
                Product product=productRepo.findById(productId).orElse(null);
                product.setStock(product.getStock()+orderedItems1.getQuantity());
                productRepo.save(product);
            }
            orderedAuditRepo.save(orderedAudit2);
            orderStatusRepo.save(orderStatus1);

            orderRepo.save(order);
            

            return new ResponseEntity("order cancelled successfully",HttpStatus.OK);
        } catch (GlobalException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
