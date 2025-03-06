package com.shop.ecommerce.service;

import com.shop.ecommerce.DTO.AddCategoryDTO;
import com.shop.ecommerce.DTO.AddProductDTO;
import com.shop.ecommerce.DTO.AdminLoginDTO;
import com.shop.ecommerce.DTO.PromocodeDTO;
import com.shop.ecommerce.entity.Category;
import com.shop.ecommerce.entity.Product;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public interface AdminService {

    public boolean login(AdminLoginDTO a, HttpSession session);
    public String adminLogout(HttpSession session);
    public String topupWallet(int id,double amount,HttpSession session);
    public String createCategory(AddCategoryDTO addCategoryDTO, HttpSession session);
    public String removeCategory(int id, HttpSession session);
    public String newProduct(int id, AddProductDTO addProductDTO, HttpSession session);
    public ResponseEntity deleteProduct(int id, HttpSession session);
    public Object getAllCategories();
    public List<Product> getAllProducts();
    public Object updatePrice(int id,double price, HttpSession session);
    public Object getWalletAudit(int userId,HttpSession session);
    public Object adNewStock(int newstock, int id, HttpSession session1);
    public Object allUsers(HttpSession session);
    public Object deleteUser(int id, HttpSession session);

    ResponseEntity getProdDetails(int id, HttpSession session);

    ResponseEntity getProdByCate(int cateId, HttpSession session);


    ResponseEntity changeStatus(int orderid,String status, HttpSession session);

    public ResponseEntity newPromocode(PromocodeDTO promocodeDTO, HttpSession session);

    ResponseEntity orderPromocode(String code, int discount, HttpSession session);
}
