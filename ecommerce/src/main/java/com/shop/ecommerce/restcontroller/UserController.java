package com.shop.ecommerce.restcontroller;

import com.shop.ecommerce.DTO.ProdToCart;
import com.shop.ecommerce.DTO.SignUpDTO;
import com.shop.ecommerce.DTO.UserLoginDTO;
import com.shop.ecommerce.entity.User;

import com.shop.ecommerce.service.AdminService;
import com.shop.ecommerce.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    AdminService as;

    @PostMapping("/signup")
    public ResponseEntity<Object> signUp(@RequestBody SignUpDTO signUpDTO) {
        try {
            if (userService.isEmailPresent(signUpDTO.getEmail())) {
                return new ResponseEntity<>("Email already exists", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(userService.signUp(signUpDTO), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(""+e.getMessage(),HttpStatus.BAD_REQUEST);
        }

    }
    @GetMapping("/myprofile")
    public ResponseEntity<Object> myProfile(HttpSession session)
    {

            return userService.myProfile(session);


    }

    @PatchMapping("/update/{id}")
    public ResponseEntity updateUser(@RequestBody User u, @PathVariable("id") int id) {
        return new ResponseEntity(userService.updateUser(u, id), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity userLogin(@RequestBody UserLoginDTO userLoginDTO, HttpSession session) {
        return new ResponseEntity(userService.loginUser(userLoginDTO, session), HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity userLogout(HttpSession session) {
        return new ResponseEntity(userService.userLogout(session), HttpStatus.OK);
    }

    @GetMapping("/getallproducts")
    public ResponseEntity getProducts(HttpSession session) {

        return new ResponseEntity(as.getAllProducts(), HttpStatus.OK);
    }

    @GetMapping("/getallcategories")
    public ResponseEntity getcategories(HttpSession session) {
        return new ResponseEntity(as.getAllCategories(), HttpStatus.OK);
    }
    @GetMapping("/viewbalance")
    public ResponseEntity viewBalance( HttpSession session)
    {
        try{
            return new ResponseEntity(userService.viewwallet(session),HttpStatus.OK) ;
        } catch (RuntimeException e) {
            return new ResponseEntity<>(""+e.getMessage(),HttpStatus.OK);
        }
    }

    @PostMapping("/addproducttocart")
    public ResponseEntity addproduct( @RequestBody ProdToCart pdetails, HttpSession session)
    {
        return new ResponseEntity(userService.addproductToCart(pdetails,session),HttpStatus.OK);
    }
    @PostMapping("/do_order")
        public <address> ResponseEntity doOrder(@RequestParam String address, HttpSession session)
    {
        return new ResponseEntity(userService.doOrder(address,session),HttpStatus.OK);
    }
    @GetMapping("/productdetails")
    public ResponseEntity getProdDetails(@RequestParam int id,HttpSession session)
    {
        return as.getProdDetails(id,session);
    }
    @GetMapping("/prodincart")
    public  ResponseEntity getProdInCart(HttpSession session)
    {
        return userService.getProdinCart(session);
    }
    @GetMapping("/fetchorders")
    public ResponseEntity getOrders(HttpSession session)
    {
       return  userService.getOrders(session);
    }
    @PostMapping("/cancelorder")
    public ResponseEntity cancelOrder(@RequestParam int orderId,HttpSession session)
    {
        return userService.cancelOrder(orderId,session);
    }

}
