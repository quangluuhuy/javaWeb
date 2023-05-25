///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
package com.student.northwind.controller;

import com.student.northwind.model.domain.Products;
import com.student.northwind.repository.ProductRepo;
import java.util.List;
import javax.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author luuqu
 */
@RestController
@RequestMapping("/api")
public class ProductRestController {
     @Autowired
    private ProductRepo productRepo;
    @GetMapping("/products")
    public List<Products> getAllProduct(){
        return productRepo.findAll();
    }
    @PostMapping("/products")
    public Products createProduct(@RequestBody Products requestProduct){
        return productRepo.save(requestProduct);
    }
    @PutMapping("/products/{id}")
    public Products updateProduct(@PathVariable int productID,@RequestBody Products requestProduct){
        Products product = productRepo.findById(productID).orElseThrow(() -> new EntityNotFoundException("Khong tim thay ban ghi"));
       product.setProductName(requestProduct.getProductName());
        product.setSupplierID(requestProduct.getSupplierID());
        product.setCategoryID(requestProduct.getCategoryID());
        product.setQuantityPerUnit(requestProduct.getQuantityPerUnit());
        product.setUnitPrice(requestProduct.getUnitPrice());
        product.setUnitsOnOrder(requestProduct.getUnitsOnOrder());
        product.setReorderLevel(requestProduct.getReorderLevel());
        product.setDiscontinued(requestProduct.getDiscontinued());
        return  productRepo.save(product);   
    }
    @DeleteMapping("/products/{id}")
    public void deleteProduct(@PathVariable int productID){        
        productRepo.deleteById(productID);
    }
    
}
