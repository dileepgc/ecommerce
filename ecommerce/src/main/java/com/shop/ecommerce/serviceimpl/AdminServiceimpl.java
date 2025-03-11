package com.shop.ecommerce.serviceimpl;

import com.shop.ecommerce.DTO.AddCategoryDTO;
import com.shop.ecommerce.DTO.AddProductDTO;
import com.shop.ecommerce.DTO.AdminLoginDTO;
import com.shop.ecommerce.DTO.PromocodeDTO;
import com.shop.ecommerce.Exception.GlobalException;
import com.shop.ecommerce.entity.*;
import com.shop.ecommerce.repo.*;
import com.shop.ecommerce.service.AdminService;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Component
@Service
@Transactional
public class AdminServiceimpl implements AdminService {

    @Autowired
    WalletRepo walletRepo;
    @Autowired
    CategoryRepo categoryRepo;
    @Autowired
    ProductRepo productRepo;
    @Autowired
    WalletAuditRepo walletAuditRepo;
    @Autowired
    UserRepo userRepo;
    @Autowired
    WalletRepo walletRepository;
    @Autowired
    AdminLoginDTO adminLoginDTO;
    @Autowired
    RoleRepo roleRepo;
    @Autowired
    OrderRepo orderRepo;
    @Autowired
    OrderStatusRepo orderStatusRepo;
    @Autowired
    OrderedAudit orderedAudit;
    @Autowired
    OrderAuditRepo orderAuditRepo;
    @Autowired
    Promocode promocode;
    @Autowired
    PromocodeRepo promocodeRepo;
    @Autowired
    OrderPromocode orderPromocode;
    @Autowired
    OrderPromocodeRepo orderPromocodeRepo;


    public boolean login(AdminLoginDTO adminLoginDTO, HttpSession session) {
        User admin1 = (User) session.getAttribute("admin");
        if (admin1 != null) {
            throw new GlobalException("admin already logged in");
        }
        String adminEmail = adminLoginDTO.getEmail();
        String adminPassword = adminLoginDTO.getPassword();
        User admin = userRepo.findByEmail(adminEmail);  //        String rolename = adminLoginDTO.getRole();
        System.out.println(admin.getEmail());
        if (!(admin.getRole().equalsIgnoreCase("admin"))) {
            throw new GlobalException("only Admin should login");
        }

        if (admin == null) {
            throw new GlobalException("Admin not found found with this email");
        }

        System.out.println(admin);


        if (admin != null && admin.getPassword().equals(adminPassword) && admin.getEmail().equals(adminEmail)) {
            session.setAttribute("admin", admin);
            System.out.println(session.getAttribute("admin"));
            return true;
        } else {
            return false;
        }
    }

    public String adminLogout(HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "Admin Should Login First";
        }
        Object admin = session.getAttribute("admin");

        if (admin != null) {
            System.out.println(session.getAttribute("admin"));
            session.invalidate();
            return "Admin logged out";
        } else {
            return "Admin Doesn't exist or not logged in";
        }
    }

    public String topupWallet(int id, double amount, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "Admin Should Login First";
        }
        User user = userRepo.findById(id).orElse(null);
        Wallet wallet = walletRepo.findByUserId(id);
        if (user == null) {
            return "User does not exist to this id";
        }
        wallet.setBalance(wallet.getBalance() + amount);
        WalletAudit walletAudit1 = new WalletAudit();
        walletAudit1.setWallet(wallet);
        walletAudit1.setDiffamount("+" + amount);
        walletAuditRepo.save(walletAudit1);
        walletRepo.save(wallet);
        return "Wallet Updated Successfully";

    }

    public String createCategory(AddCategoryDTO addCategoryDTO, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "Admin Should Login First";
        }
        Category category = new Category(addCategoryDTO.getCate_name());
        categoryRepo.save(category);
        return "Category has been created";
    }

    public String removeCategory(int id, HttpSession session) {
//        if (session.getAttribute("admin") == null) {
//            return "Admin Should Login First";
//        }
        Category category = categoryRepo.findById(id).orElse(null);
        if (category == null) {
            return "Category of this id does not exist";
        }
        category.setIs_deleted(true);
        List<Product> productList = productRepo.findByCategory(category);
        for (Product product : productList) {
            product.setIs_deleted(true);
        }
        categoryRepo.save(category);
        return category.getCate_name() + "Category Deleted";

    }

    @Transactional
    public String newProduct(int id, AddProductDTO addProductDTO, HttpSession session) {
        // if (session.getAttribute("admin") == null) {
        //     return "Admin Should Login First";
        // }

        Category category = categoryRepo.findById(id).orElse(null);
        if (category == null || category.isIs_deleted()) {
            return "Category does not exist or deleted";
        }

        Product product = new Product();
        product.setStock(addProductDTO.getStock());
        product.setName(addProductDTO.getProd_name());
        product.setPrice(addProductDTO.getPrice());
        product.setImage(addProductDTO.getImageURL());
        product.setDescription(addProductDTO.getDescription());
        product.setCategory(category); // Set category for the product

        productRepo.save(product);

        // Add the new product to the category's list of products
        List<Product> productList = category.getProducts();
        if (productList == null) {
            productList = new ArrayList<>();
        }
        productList.add(product);
        category.setP(productList);

        System.out.println(product.getCategory().getCate_name());
        return "Product has been added successfully";
    }


    public ResponseEntity deleteProduct(int id, HttpSession session) {
        try
        {

        if (session.getAttribute("admin") == null) {
            throw new GlobalException("Admin Should Login First") ;
        }
        Product product = productRepo.findById(id).orElse(null);
        if (product == null) {
            throw new GlobalException("product does not exist") ;
        }
        product.setIs_deleted(true);
        return new ResponseEntity("Product Deleted",HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity(""+e.getMessage(),HttpStatus.OK);
        }


    }

    public Object getAllCategories() {
        try
        {
        List<Category> categories = categoryRepo.findCategories();
        return categories;
        } catch (RuntimeException e) {
            return new ResponseEntity(""+e.getMessage(),HttpStatus.OK);
        }
    }

    public List<Product> getAllProducts() {
        List<Product> products = productRepo.findProducts();
        return products;
    }

    public Object updatePrice(int id, double price, HttpSession session) {
        try
        {
            if (session.getAttribute("admin") == null) {
                throw new GlobalException("Admin Should Login First") ;
            }
        Product product = productRepo.findById(id).orElse(null);
        if (product == null) {
            throw new GlobalException("Product does not exist") ;
        }
        product.setPrice(price);
        productRepo.save(product);
        return "Price Updated successfully";
        } catch (RuntimeException e) {
            return new ResponseEntity(e.getMessage(),HttpStatus.OK);
        }
    }


    public Object getWalletAudit(int userId, HttpSession session) {
        try
        {
            if (session.getAttribute("admin") == null) {
                throw new GlobalException("Admin Should Login First") ;
            }
        Wallet wallet = walletRepository.findByUserId(userId);
        if (wallet == null) {
            return "wallet doesnot have any audit";
        }
        return wallet.getWalletAuditsList();
        } catch (RuntimeException e) {
            return new ResponseEntity(""+e.getMessage(),HttpStatus.OK);
        }
    }

    public Object adNewStock(int newstock, int id, HttpSession session) {
        try{
            if (session.getAttribute("admin") == null) {
                throw new GlobalException("Admin Should Login First") ;
            }
        Product product = productRepo.findById(id).orElse(null);
        if (product == null) {
            return "product does not exist";
        }
        product.setStock(product.getStock() + newstock);
        productRepo.save(product);
        return "Stock updated successfully";
        } catch (RuntimeException e) {
            return new ResponseEntity(""+e.getMessage(),HttpStatus.OK);
        }
    }

    public Object allUsers(HttpSession session) {
        try {
            if (session.getAttribute("admin") == null) {
                return "Admin Should Login First";
            }
            List<User> users = userRepo.findByRole();
            return users;
        }

     catch(
    RuntimeException e)

    {
        return new ResponseEntity("" + e.getMessage(), HttpStatus.OK);
    }
}

    public Object deleteUser(int id, HttpSession session) {
        try
        {
            if (session.getAttribute("admin") == null) {
                throw new GlobalException("Admin Should Login First") ;
            }
        User user=userRepo.findById(id).orElse(null);
        if(user==null)
        {
            throw new GlobalException("User not found for this id");
        }
        else{
            user.setIs_deleted(true);
            userRepo.save(user);
            return user.getFirstName()+" User is deleted";
        }
        } catch (RuntimeException e) {
            return new ResponseEntity(""+e.getMessage(),HttpStatus.OK);
        }
    }

    public ResponseEntity getProdDetails(int id, HttpSession session) {
        try
        {
            Product product=productRepo.findById(id).orElse(null);
            if(product==null)
            {
                throw new GlobalException("product not found");
            }
            return new ResponseEntity(product,HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }

    }
   public ResponseEntity getProdByCate(int cateId, HttpSession session)
    {
        try
        {
            List<Product> products=productRepo.findByCategoryId(cateId);
            if(products.isEmpty())
            {
                throw new GlobalException("There are no products in this category");
            }
            return new ResponseEntity(products,HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity(e.getMessage(),HttpStatus.UNAUTHORIZED);
        }
    }

    public ResponseEntity changeStatus(int orderid,String status, HttpSession session)
    {
        try
        {
            if (session.getAttribute("admin") == null) {
                throw new GlobalException("Admin Should Login First") ;
            }

            Order order=orderRepo.findById(orderid).orElse(null);
            OrderStatus orderStatus=orderStatusRepo.findByOrder(order);
            OrderedAudit orderedAudit1=orderAuditRepo.findByOrder(order);
            OrderedAudit orderedAudit=new OrderedAudit();
            orderedAudit.setPrevious_audit(orderedAudit1.getPresent_status());
            orderedAudit.setOrder(order);
            orderedAudit.setPresent_status(status);
            orderAuditRepo.save(orderedAudit);

            orderStatus.setStatus(status);
            order.setOrderedStatus(status);
            orderStatusRepo.save(orderStatus);
            orderRepo.save(order);

            return new ResponseEntity<>("Status changed successfully",HttpStatus.OK);
        } catch (GlobalException e) {
            return new ResponseEntity(e.getMessage(),HttpStatus.UNAUTHORIZED);
        }
    }
    @Scheduled(cron = "0 0/30 * * * *")
    // This runs every minute
    public void changePromoStatus() {
        List<Promocode> activePromocodes = promocodeRepo.findAll();
        for (Promocode promocode : activePromocodes) {
            promocode.setExpiredStatus(true);
            promocodeRepo.save(promocode);
        }
    }
    @Scheduled(cron = "0 0/30 * * * *")
    // This runs every 01 minute
    public void changeOrderPromoStatus() {
        List<OrderPromocode> orderPromocodes = orderPromocodeRepo.findAll();
        for (OrderPromocode orderPromocode1 : orderPromocodes) {
            orderPromocode1.setExpireStatus(true);
            orderPromocodeRepo.save(orderPromocode1);
        }
    }

    @Transactional
    public ResponseEntity newPromocode(PromocodeDTO promocodeDTO, HttpSession session) {
        try {
            Promocode promocode=new Promocode();
            promocode.setCode(promocodeDTO.getCode());
            promocode.setDiscount(promocodeDTO.getDiscount());
            Product product = productRepo.findById(promocodeDTO.getProductId()).orElse(null);
            if(product==null)
            {
                throw new GlobalException("Product not found");
            }
            promocode.setProduct(product);
            promocodeRepo.save(promocode);


            return new ResponseEntity<>("Promocode created successfully",HttpStatus.OK);
        }
        catch (GlobalException e) {
            return new ResponseEntity(e.getMessage(),HttpStatus.UNAUTHORIZED);
        }
    }

    public  ResponseEntity orderPromocode(String code, int discount, HttpSession session) {
        try {
            OrderPromocode orderPromocode=new OrderPromocode();
            orderPromocode.setCode(code);
            orderPromocode.setDiscount(discount);
            orderPromocodeRepo.save(orderPromocode);
            return new ResponseEntity<>("Ordered Promocode created succesfully",HttpStatus.OK);
        } catch (GlobalException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }


}