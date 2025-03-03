package com.shop.ecommerce.service;

import com.shop.ecommerce.DTO.AddCategoryDTO;
import com.shop.ecommerce.DTO.AddProductDTO;
import com.shop.ecommerce.DTO.AdminLoginDTO;
import com.shop.ecommerce.entity.Category;
import com.shop.ecommerce.entity.Product;
import jakarta.servlet.http.HttpSession;

import java.util.List;

public interface AdminService {

    public boolean login(AdminLoginDTO a, HttpSession session);
    public String adminLogout(HttpSession session);
    public String topupWallet(int id,double amount,HttpSession session);
    public String createCategory(AddCategoryDTO addCategoryDTO, HttpSession session);
    public String removeCategory(int id, HttpSession session);
    public String newProduct(int id, AddProductDTO addProductDTO, HttpSession session);
    public String deleteProduct(int id, HttpSession session);
    public Object getAllCategories();
    public List<Product> getAllProducts();
    public Object updatePrice(int id,double price, HttpSession session);
    public Object getWalletAudit(int userId,HttpSession session);
    public Object adNewStock(int newstock, int id, HttpSession session1);

}
