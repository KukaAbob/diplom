package com.bazarweb.bazarweb.service.Product;

import java.util.List;


import org.springframework.stereotype.Service;

import com.bazarweb.bazarweb.model.Product.Product;
import com.bazarweb.bazarweb.repository.Product.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
    
    private final ProductRepository productRepository;
    
    public Product save(Product product){
        return productRepository.save(product);
    }

    public Product productCreate(Product product){
        return save(product);
    }

    public Product getProductById(int id){
            return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public Product getByName(String name){
        return productRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    public Product updateProduct(int id, Product updatedProduct) {
        var product = getProductById(id);
        product.setName(updatedProduct.getName());
        product.setPrice(updatedProduct.getPrice());
        product.setCategory(updatedProduct.getCategory());
        return productRepository.save(product);
    }

    public void deleteProduct(int id) {
        productRepository.deleteById(id);
    }
}
