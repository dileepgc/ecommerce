package com.shop.ecommerce.restcontroller;

import com.shop.ecommerce.DTO.SignUpDTO;
import com.shop.ecommerce.entity.AddProduct;
import com.shop.ecommerce.entity.User;

import com.shop.ecommerce.serviceimpl.AdminServiceimpl;
import com.shop.ecommerce.serviceimpl.UserServiceimpl;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserServiceimpl us;
    @Autowired
    AdminServiceimpl as;

    @PostMapping("/signup")
    public ResponseEntity<Object> signUp(@RequestBody SignUpDTO signUpDTO) {
        try {
            if (us.isEmailPresent(signUpDTO.getEmail())) {
                return new ResponseEntity<>("Email already exists", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(us.signUp(signUpDTO), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(""+e.getMessage(),HttpStatus.BAD_REQUEST);
        }

    }

    @PatchMapping("/update/{id}")
    public ResponseEntity updateUser(@RequestBody User u, @PathVariable("id") int id) {
        return new ResponseEntity(us.updateUser(u, id), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity userLogin(@RequestBody User u, HttpSession session) {
        return new ResponseEntity(us.loginUser(u, session), HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity userLogout(HttpSession session) {
        return new ResponseEntity(us.userLogout(session), HttpStatus.OK);
    }

    @PostMapping("/getallproducts")
    public ResponseEntity getProducts(HttpSession session) {

        return new ResponseEntity(as.getAllProducts(), HttpStatus.OK);
    }

    @PostMapping("/getallcategories")
    public ResponseEntity getcategories(HttpSession session) {
        return new ResponseEntity(as.getAllCategories(), HttpStatus.OK);
    }

    @PostMapping("/addproduct/{id}")
    public ResponseEntity addproduct(@PathVariable int id, @RequestBody AddProduct pdetails, HttpSession session)
    {
        return new ResponseEntity(us.addproduct(id,pdetails,session),HttpStatus.OK);
    }
    @PostMapping("/order/{id}")
        public ResponseEntity doOrder(@PathVariable int id, HttpSession session)
    {
        return new ResponseEntity(us.doOrder(id,session),HttpStatus.OK);
    }
}
