package com.shop.ecommerce.serviceimpl;

import com.shop.ecommerce.DTO.*;
import com.shop.ecommerce.Exception.GlobalException;
import com.shop.ecommerce.entity.*;
import com.shop.ecommerce.helperclasses.GlobalMethod;
import com.shop.ecommerce.repo.*;
import com.shop.ecommerce.service.JWTService;
import com.shop.ecommerce.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

// import org.hibernate.engine.jdbc.env.internal.LobCreationLogging_.logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


@Service
@Component

public class UserServiceimpl implements UserService {
    public static final Logger logger = LoggerFactory.getLogger(UserServiceimpl.class);
    @Autowired
    UserRepo userRepo;
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


    // This method checks if the provided email has a valid format using regular expressions.
// It returns true if the email format is correct, otherwise returns false.

    public boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return Pattern.matches(emailRegex, email);
    }

    /**
     * Validates the strength of the provided password. The password must be at least 8 characters long,
     * contain at least one uppercase letter, one digit, and one special character.
     *
     * @param password The password to validate.
     * @return true if the password meets the required strength, false otherwise.
     */

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

// This method handles the user sign-up process. It checks if the provided email and username already
// exist in the database, validates the password format, and saves the new user. It also assigns
// the user a role and creates associated objects like a wallet and a cart if the user is a regular user.

    public ResponseEntity signUp(SignUpDTO signUpDTO) {
        try {

            System.out.println(signUpDTO.getEmail() + "  " + signUpDTO.getUsername() + " " + signUpDTO.getRole());
            if (signUpDTO.getRole() == null || signUpDTO.getEmail() == null ||
                    signUpDTO.getUsername() == null || signUpDTO.getPassword() == null || signUpDTO.getPhone() <= 0) {
                throw new GlobalException("role ,email,username,password and number cannot be null");
            }
            if (!isValidEmail(signUpDTO.getEmail())) {
                throw new GlobalException("Email format is not correct");
            }
            User existingEmailUser = userRepo.findByEmail(signUpDTO.getEmail());
            if (existingEmailUser != null) {
                throw new GlobalException("Email already exist");
            }
            User existingusernameUser = userRepo.findByUsername(signUpDTO.getUsername());
            if (existingusernameUser != null) {
                throw new GlobalException("Username already exist");
            }

            if (!isValidPassword(signUpDTO.getPassword())) {
                throw new GlobalException("Password is not in valid format");
            }
            User admin = roleRepo.findByRoleName("admin");
            if (admin != null && signUpDTO.getRole().equalsIgnoreCase("admin")) {
                throw new GlobalException("Admin already exists");
            }
            if (signUpDTO.getPhone() != 0) {
                if (String.valueOf(signUpDTO.getPhone()).length() != 10) {
                    throw new GlobalException("Phone number must be 10 digits long");
                }
            }
            User user = new User(signUpDTO.getFirstName(), signUpDTO.getLastName(), signUpDTO.getPassword(),
                    signUpDTO.getRole(), signUpDTO.getEmail(), signUpDTO.getPhone());
            userRepo.save(user);
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
            user.setPassword(encoder.encode(user.getPassword()));
            user.setUsername(signUpDTO.getUsername());
            userRepo.save(user);
            Role role = new Role();
            if (signUpDTO.getRole().equalsIgnoreCase("admin")) {
                role.setRolename("admin");
            } else {
                role.setRolename("user");
            }
            role.setUser(user);
            roleRepo.save(role);

            if (signUpDTO.getRole().equalsIgnoreCase("user")) {
                Wallet wallet = new Wallet();
                wallet.setUser(user);//to add user to Wallet
                wr.save(wallet);
                WalletAudit walletAudit = new WalletAudit();
                walletAudit.setWallet(wallet);
                walletAuditRepo.save(walletAudit);

                Cart cart = new Cart();
                cart.setU(user);
                cr.save(cart);
                user.setWallet(wallet);

                userRepo.save(user);

            }
            logger.info("User account created successfully");
            return new ResponseEntity<>("User account created", HttpStatus.OK);
                    } catch (GlobalException e) {
            logger.error("User account not created");
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("User account not created");
            throw new GlobalException(e.getMessage());
        }
    }

    // This method allows a logged-in user to update their profile information (first name, last name, and phone number).
// It verifies the user's identity using the authorization header and updates the user's details in the database.
@Transactional
    public ResponseEntity updateUser(User user, String authorizationHeader) {
        try {
            String token = authorizationHeader.replace("Bearer ", "");
            String username = jwtService.extractUserName(token);

            User existingUser = userRepo.findByUsername(username);
            if (existingUser == null) {
                return new ResponseEntity<>("User not found ", HttpStatus.OK);
            } else {
                if (user.getFirstName() != null) existingUser.setFirstName(user.getFirstName());
                if (user.getLastName() != null) existingUser.setLastName(user.getLastName());
                
                if (user.getPhone() != 0) {
                    if (String.valueOf(user.getPhone()).length() != 10) {
                        throw new GlobalException("Phone number must be 10 digits long");
                    }
                    existingUser.setPhone(user.getPhone());
                }
                userRepo.save(existingUser);
                logger.info("User updated successfully");
                return new ResponseEntity<>("Updated Successfully", HttpStatus.OK);
            }
            } catch (GlobalException e) {
            logger.error("User not updated");
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("User not updated");
            throw new GlobalException(e.getMessage());
        }
    }

    public ResponseEntity allusers( String authorizationHeader) {
        try {
            String token = authorizationHeader.replace("Bearer ", "");
            String username = jwtService.extractUserName(token);
            String role = jwtService.extractRole(token);
            System.out.println(username);
            if (role.equalsIgnoreCase("user"))
                return new ResponseEntity<>("Access denied", HttpStatus.UNAUTHORIZED);


            List<User> userList = userRepo.findByRoleUser();
            logger.info("All users retrieved successfully");
            return new ResponseEntity<>(userList, HttpStatus.OK);

        } catch (GlobalException e) {
            logger.error("All users not retrieved");
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // This method authenticates the user based on the provided email and password.
// If valid, it generates and returns a JWT token. If authentication fails, it returns an error message.

    /**
     *
     * @param userLoginDTO
     * @return
     */
    public ResponseEntity loginUser(UserLoginDTO userLoginDTO) {
        try {
            Map<String, String> map = new HashMap<String, String>();
            User user = userRepo.findByEmail(userLoginDTO.getEmail());
            System.out.println(user);

            if (user == null) {
                throw new GlobalException("User Does not exist");
            }

            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

            if (encoder.matches(userLoginDTO.getPassword(), user.getPassword())) {
                if(user.getRole().equalsIgnoreCase("admin"))
                {
                    map.put("Message", " Admin Login successful");
                    map.put("role","admin");
                }
                else {
                    map.put("Message", " User Login successful");
                    map.put("role","user");
                }
                Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
                if (authentication.isAuthenticated()) {
                    map.put("token", jwtService.generateToken(user.getUsername(), user.getRole()));
                    logger.info("User logged in successfully");
                    return new ResponseEntity<>(map, HttpStatus.OK);
                }
            }
            return new ResponseEntity<>("login failed due to wrong password", HttpStatus.UNAUTHORIZED);
            } catch (GlobalException e) {
            logger.error("Login failed due to wrong password");
            return new ResponseEntity(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }




    // This method retrieves the profile details of the currently authenticated user, including their first name,
// last name, username, email, phone number, and wallet balance. The user must be logged in and provide a valid JWT token.
    public ResponseEntity<Object> myProfile(String authorizationHeader) {
        try {
            GlobalMethodDTO globalMethodDTO = globalMethod.adminAccess(authorizationHeader);
            String username = globalMethodDTO.getUsername();


            User user = userRepo.findByUsername(username);
            Map map = new HashMap<>();
            map.put("id",user.getId());
            map.put("First Name", user.getFirstName());
            map.put("Last Name", user.getLastName());
            map.put("Username", user.getUsername());
            map.put("Email", user.getEmail());
            map.put("Phone number", user.getPhone());
            map.put("role",user.getRole());
            Wallet wallet = walletRepo.findByUser(user);
            if (wallet == null) {
                map.put("wallet Balance", "no wallet");
            } else
                map.put("wallet Balance", wallet.getBalance());
            logger.info("User profile retrieved successfully");

            return new ResponseEntity<>(map, HttpStatus.OK);
            } catch (GlobalException e) {
            logger.error("User profile not retrieved");
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }
}
