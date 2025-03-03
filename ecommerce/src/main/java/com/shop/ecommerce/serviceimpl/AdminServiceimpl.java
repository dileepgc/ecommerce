package com.shop.ecommerce.serviceimpl;

import com.shop.ecommerce.DTO.AddCategoryDTO;
import com.shop.ecommerce.DTO.AddProductDTO;
import com.shop.ecommerce.DTO.AdminLoginDTO;
import com.shop.ecommerce.Exception.GlobalException;
import com.shop.ecommerce.entity.*;
import com.shop.ecommerce.repo.*;
import com.shop.ecommerce.service.AdminService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Component
@Service
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


    public boolean login(AdminLoginDTO adminLoginDTO, HttpSession session) {
        User admin1=(User) session.getAttribute("admin");
        if(admin1!=null)
        {
            throw new GlobalException("admin already logged in");
        }
         String adminEmail = adminLoginDTO.getEmail();
         String adminPassword=adminLoginDTO.getPassword();
         String rolename=adminLoginDTO.getRole();
        System.out.println(rolename);
         if(!(rolename.equalsIgnoreCase("admin")))
         {
             throw new GlobalException("only Admin should login");
         }
         User admin=roleRepo.findByRoleName(rolename);
         if(admin==null)
         {
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
    public String adminLogout(HttpSession session)
    {
        Object admin=session.getAttribute("admin");

        if(admin!=null)
        {
            System.out.println(session.getAttribute("admin"));
            session.invalidate();
            return "Admin logged out";
        }
        else {
            return "Admin Doesn't exist or not logged in";
        }
    }
    public String topupWallet(int id,double amount,HttpSession session) {
            User user=userRepo.findById(id).orElse(null);
            Wallet wallet = walletRepo.findByUserId(id);
            if (user == null) {
                return "User does not exist to this id";
            }
            wallet.setBalance(wallet.getBalance() + amount);
            WalletAudit walletAudit1=new WalletAudit();
            walletAudit1.setWallet(wallet);
            walletAudit1.setDiffamount("+"+amount);
            walletAuditRepo.save(walletAudit1);
            walletRepo.save(wallet);
            return "Wallet Updated Successfully";

    }
    public String createCategory(AddCategoryDTO addCategoryDTO, HttpSession session)
    {
        if (session.getAttribute("admin") == null) {
            return "Admin Should Login First";
        }
        Category category=new Category(addCategoryDTO.getCate_name());
        categoryRepo.save(category);
        return "Category has been created";
    }

    public String removeCategory(int id, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "Admin Should Login First";
        }
        Category category=categoryRepo.findById(id).orElse(null);
        if(category==null)
        {
            return "Category of this id does not exist";
        }
        category.setIs_deleted(true);
        return category.getCate_name()+"Category Deleted";

    }

    public String newProduct(int id, AddProductDTO addProductDTO, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "Admin Should Login First";
        }
        Category category=categoryRepo.findById(id).orElse(null);
        if(category==null)
        {
            return "Category does not exist";
        }
        Product product=new Product();
        product.setStock(addProductDTO.getStock());
        product.setName(addProductDTO.getProd_name());
        product.setPrice(addProductDTO.getPrice());
        product.setDescription(addProductDTO.getDescription());
        product.setCategory(category);
        productRepo.save(product);

        List<Product> productList=category.getProducts();
        if(productList==null)
        {
            productList = new ArrayList<>();
        }
        productList.add(product);
        category.setP(productList);
        categoryRepo.save(category);
        System.out.println(product.getCategory().getCate_name());
        return "Product have been added";

    }

    public String deleteProduct(int id, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "Admin Should Login First";
        }
        Product product=productRepo.findById(id).orElse(null);
        if(product==null)
        {
            return "product does not exist";
        }
        product.setIs_deleted(true);
        return "Product deleted";
    }

    public Object getAllCategories() {
        return categoryRepo.findAll();
    }

    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    public Object updatePrice(int id,double price, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "Admin Should Login First";
        }
        Product product= productRepo.findById(id).orElse(null);
        if(product==null)
        {
            return "Product does not exist";
        }
        product.setPrice(price);
        productRepo.save(product);
        return "Price Updated successfully";
    }


        public Object getWalletAudit(int userId,HttpSession session) {
            if (session.getAttribute("admin") == null) {
                return "Admin Should Login First";
            }
            Wallet wallet = walletRepository.findByUserId(userId);
            if(wallet==null)
            {
                return "wallet doesnot have any audit";
            }
           return wallet.getWalletAuditsList();
    }

    public Object adNewStock(int newstock, int id, HttpSession session1) {
        Product product=productRepo.findById(id).orElse(null);
        if(product==null)
        {
            return "product does not exist";
        }
        product.setStock(product.getStock()+newstock);
        productRepo.save(product);
        return "Stock updated successfully";
    }
}
