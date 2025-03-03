package com.shop.ecommerce.serviceimpl;

import com.shop.ecommerce.DTO.SignUpDTO;
import com.shop.ecommerce.Exception.GlobalException;
import com.shop.ecommerce.entity.*;
import com.shop.ecommerce.repo.*;
import com.shop.ecommerce.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;


@Service
@Component
@Transactional
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
        CartItemRepo cartItemRepo;
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

        public String signUp(SignUpDTO signUpDTO)
        {
            User admin=roleRepo.findByRoleName("admin");
            if(admin !=null && signUpDTO.getRole().equalsIgnoreCase("admin"))
            {
                throw new GlobalException("Admin already exists");
            }
            User user =new User(signUpDTO.getFirstName(),signUpDTO.getLastName(),signUpDTO.getPassword(),signUpDTO.getRole(),
                    signUpDTO.getEmail());
            userRepo.save(user);
            Wallet wallet =new Wallet();
            Role role=new Role();
            if(signUpDTO.getRole().equalsIgnoreCase("admin"))
            {
                role.setRolename("admin");
            }else {
                role.setRolename("User");
            }
            role.setUser(user);
            roleRepo.save(role);

            wallet.setUser(user);//to add user to Wallet
            wr.save(wallet);
            WalletAudit walletAudit = new WalletAudit();
            walletAudit.setWallet(wallet);
            walletAuditRepo.save(walletAudit);

            Cart cart =new Cart();
            cart.setU(user);          //to add user to cart
            cr.save(cart);
          user.setWallet(wallet);

            ur.save(user);

            return "User account created";
        }



    public String updateUser(User user, int id)
        {

            User existingUser = ur.findById(id).orElse(null);
            if(existingUser ==null){
                return "User not found ";
            }
            else {
                if(user.getFirstName()!=null)
                    existingUser.setName(user.getFirstName());
                if(user.getLastName()!=null)
                    existingUser.setName(user.getLastName());



                ur.save(existingUser);
                return "Updated Successfully";
            }
        }

        public String loginUser(User u, HttpSession session)
        {
            User u1=ur.findByEmail(u.getEmail());
            if (session.getAttribute("user") != null) {
                return "User Already logged in";
            }
            session.setAttribute("user",u);

            System.out.println(session.getAttribute("user"));
            if(u1==null)
            {
                return  "Email Does not exist";
            }


                if(u1.getPassword().trim().equals(u.getPassword().trim()))
                {
                    session.setAttribute("user",u);
                    return "Login Successfully";
                }

            return "Incorrect password ";
        }
        public String userLogout(HttpSession session)
        {
            if(session.getAttribute("user")!=null)
            {
                System.out.println(session.getAttribute("user"));
                session.invalidate();

                return "User Logged out";
            }
            return "user not logged in";
        }

    public boolean isEmailPresent(String email) {
           User u= ur.findByEmail(email);
           if(u==null)
           {
               return false;
           }
           else {
               return true;
           }
    }

    public Object addproduct(int userid,AddProduct pdetails,HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "user must log in";
        }

        int pid=pdetails.getProductId();
        int quantity=pdetails.getQuantity();
        Product product=productRepo.findById(pid).orElse(null);
        if(product==null)
        {
            return "product does not exist ";
        }
        if(product.getStock()<quantity)
        {
            return "stock is not available";
        }
        CartItem cartItem=cartItemRepo.findByProduct(product);
        if(cartItem==null){
             CartItem cartItem1=new CartItem();
             cartItem=cartItem1;

            cartItemRepo.save(cartItem);

        }
        if(cartItem.getProduct()!=product)
        {
            cartItem.setProduct(product);
            cartItemRepo.save(cartItem);

        }

        cartItem.setQuantity(cartItem.getQuantity()+quantity);
        User user = ur.findById(userid).orElse(null);  // Get User object by ID
        Cart cart = cartRepo.findByU(user);

        cart.setQuantity(cart.getQuantity()+quantity);
        double totalamount=  (product.getPrice()*quantity);
        cart.setTotal_amount(cart.getTotal_amount()+totalamount);
        product.setStock(product.getStock()-quantity);
         cartItemRepo.save(cartItem);
         cartItem.setCart(cart);
         cartRepo.save(cart);
         productRepo.save(product);
         return "Product added successfully";
    }
    @Transactional
    public Object doOrder(int id, HttpSession session) {

        if (session.getAttribute("user") == null) {
            return "User must log in";
        }


        User user = userRepo.findById(id).orElse(null);
        if (user == null) {
            return "User does not exist";
        }


        Cart cart = cartRepo.findByU(user);
        if (cart == null) {
            return "Cart not found for user";
        }
        cartRepo.save(cart);

        int cartId = cart.getId();
        List<CartItem> cartItems = cartItemRepo.findByCartId(cartId);
        if(cartItems.isEmpty())
        {
            return "Cart is empty";
        }

        order.setTotalAmount(cart.getTotal_amount());
        orderRepo.save(order);

        List<OrderedAudit> orderedAudits = new ArrayList<>();
        OrderedAudit orderedAudit = new OrderedAudit();
        orderedAuditRepo.save(orderedAudit);
        orderedAudit.setAction("created");
        orderedAudit.setOrder(order);
        orderedAuditRepo.save(orderedAudit);
        orderedAudits.add(orderedAudit);
        order.setOrderedAudit(orderedAudits);
        order.setUser(user);
        cartRepo.save(cart);


        List<OrderedItems> orderedItemsList = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            OrderedItems orderedItems = new OrderedItems();
            orderedItems.setOrder(order);
            orderedItems.setQuantity(cartItem.getQuantity());
            orderedItems.setProduct(cartItem.getProduct());
            orderedItemsRepo.save(orderedItems);
            orderedItemsList.add(orderedItems);
        }

        order.setOrderedItems(orderedItemsList);
        order.setOrderedStatus("Pending");

        orderRepo.save(order);

        List<CartItem> cartItems1=cartItemRepo.findAllByCartId(cartId);
        for(CartItem cartItem:cartItems1)
        {
            cartItem.setIs_deleted(true);
            cartItemRepo.save(cartItem);
        }


        Cart cart1= cartRepo.findById(cartId).orElse(null);
        cart1.setIs_deleted(true);
        cartRepo.save(cart1);

        return "Order placed successfully";
    }
}
