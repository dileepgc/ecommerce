package com.shop.ecommerce.serviceimpl;

import com.shop.ecommerce.DTO.AddProductDTO;
import com.shop.ecommerce.DTO.CartProductDTO;
import com.shop.ecommerce.DTO.GlobalMethodDTO;
import com.shop.ecommerce.DTO.ProdToCart;
import com.shop.ecommerce.Exception.GlobalException;
import com.shop.ecommerce.entity.*;
import com.shop.ecommerce.helperclasses.GlobalMethod;
import com.shop.ecommerce.repo.*;
import com.shop.ecommerce.service.CartService;
import com.shop.ecommerce.service.JWTService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    public static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);
    @Autowired
    UserRepo userRepo;
    @Autowired
    Cart cart;
    //    @Autowired
//    User user;
    @Autowired
    AuthenticationManager authenticationManager;
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
    @Autowired
    JWTService jwtService;
    @Autowired
    GlobalMethod globalMethod;

    // Adds a product to the user's cart, ensuring that the product exists and is not restricted by admin access.
// It updates the cart's quantity and total amount based on the product price and quantity.

    public ResponseEntity addproductToCart(ProdToCart pdetails, String authorizationHeader) {
        try {
            GlobalMethodDTO globalMethodDTO=globalMethod.adminAccess(authorizationHeader);
            String username=globalMethodDTO.getUsername();
            System.out.println(globalMethodDTO.getRole());
            System.out.println(globalMethodDTO.getUsername());
            if(globalMethodDTO.isAccess()) {
                return new ResponseEntity<>("Access Denied", HttpStatus.UNAUTHORIZED);
            }
            User user1 = userRepo.findByUsername(username);
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
            User user = userRepo.findById(user_id).orElse(null);  // Get User object by ID
            Cart cart = cartRepo.findByU(user);

            cart.setQuantity(cart.getQuantity() + quantity);
            double totalamount = (product.getPrice() * quantity);
            cart.setTotal_amount(cart.getTotal_amount() + totalamount);

            cartItemRepo.save(cartItem);
            cartItem.setCart(cart);
            cartRepo.save(cart);
            logger.info("Product added successfully");
            return new ResponseEntity<>("Product added successfully", HttpStatus.OK);


        } catch ( RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // Retrieves the list of products currently in the user's cart, formatted with product details for display.
// The response will include product names, descriptions, prices, and quantities from the user's cart.
    public ResponseEntity getProdinCart(String authorizationHeader) {
        try {
            GlobalMethodDTO globalMethodDTO=globalMethod.adminAccess(authorizationHeader);
            String username=globalMethodDTO.getUsername();
            if(globalMethodDTO.isAccess())
                return new ResponseEntity<>("Access Denied",HttpStatus.UNAUTHORIZED);

            User user = userRepo.findByUsername(username);

            Cart cart = cartRepo.findByU(user);
            List<CartItem> cartItems = cartItemRepo.findByCart(cart);
            List<CartProductDTO> productlist = new ArrayList<>();
            for (CartItem cartItem : cartItems) {
                CartProductDTO productDTO = new CartProductDTO();
                productDTO.setId(cartItem.getProduct().getId());
                productDTO.setProd_name(cartItem.getProduct().getName());
                productDTO.setPrice(cartItem.getProduct().getPrice());
                productDTO.setQuantity(cartItem.getQuantity());
                productDTO.setDescription(cartItem.getProduct().getDescription());
                productDTO.setImage(cartItem.getProduct().getImage());
                productlist.add(productDTO);
            }
            logger.info("Products retrieved successfully");
            return new ResponseEntity(productlist, HttpStatus.OK);
        } catch (GlobalException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }

    }

    // Removes a specified quantity of a product from the user's cart, adjusting the cart's total amount.
// If the product quantity to remove exceeds what's in the cart, an exception is thrown.
    public ResponseEntity deleteProdFromCart(int id, int quantity, String authorizationHeader) {
        try {
            GlobalMethodDTO globalMethodDTO=globalMethod.adminAccess(authorizationHeader);
            String username=globalMethodDTO.getUsername();
            if(globalMethodDTO.isAccess())
                return new ResponseEntity<>("Access Denied",HttpStatus.UNAUTHORIZED);

            User user = userRepo.findByUsername(username);

            Cart cart = cartRepo.findByU(user);
            Product product = productRepo.findById(id).orElse(null);
            if (product == null) {
                throw new GlobalException("product of this Id does not exist");
            }
            List<CartItem> cartItemList = cart.getCartItem();
            CartItem cartItem = cartItemRepo.findByProduct(product);
            if (cartItem == null) {
                throw new GlobalException("Your Cart does not contains this product");
            }
            if (cartItem.getQuantity() < quantity) {
                throw new GlobalException("Quantity is more then cart item quantity");
            }

            cart.getCartItem().remove(product);
            cart.setQuantity(cart.getQuantity() - quantity);
            cart.setTotal_amount(cart.getTotal_amount() - (product.getPrice() * quantity));
            cartRepo.save(cart);

            cartItem.setQuantity(cartItem.getQuantity() - quantity);
            cartItemRepo.save(cartItem);

            product.setStock(product.getStock() + quantity);
            productRepo.save(product);
            System.out.println(cart.getCartItem());
            logger.info("Product removed successfully from cart");
            return new ResponseEntity("Product removed successfully from cart", HttpStatus.OK);

        } catch (GlobalException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
