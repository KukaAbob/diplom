package com.bazarweb.bazarweb.service.Product;

import java.util.ArrayList;
import java.util.List;


import org.springframework.stereotype.Service;

import com.bazarweb.bazarweb.dto.ProductDTO;
import com.bazarweb.bazarweb.dto.ProductVariantDTO;
import com.bazarweb.bazarweb.model.Catalog.Category;
import com.bazarweb.bazarweb.model.Product.Color;
import com.bazarweb.bazarweb.model.Product.Product;
import com.bazarweb.bazarweb.model.Product.ProductVariant;
import com.bazarweb.bazarweb.model.Product.Size;
import com.bazarweb.bazarweb.repository.Catalog.CategoryRepository;
import com.bazarweb.bazarweb.repository.Product.ColorRepository;
import com.bazarweb.bazarweb.repository.Product.ProductRepository;
import com.bazarweb.bazarweb.repository.Product.SizeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
    
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ColorRepository colorRepository;
    private final SizeRepository sizeRepository;
    
    public Product save(Product product){
        return productRepository.save(product);
    }

    public Product productCreate(ProductDTO productDTO) {
        // Логируем входные данные
        System.out.println("Creating product with data: " + productDTO);
        
        // Создаем основной продукт
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setCollection(productDTO.getCollection());
        product.setGender(productDTO.getGender());
        product.setProductStatus(productDTO.getProductStatus());
        
        // Проверяем и загружаем категорию
        if (productDTO.getCategory() != null) {
            if (productDTO.getCategory().getId() != 0) {
                Category category = categoryRepository.findById(productDTO.getCategory().getId())
                    .orElseThrow(() -> new RuntimeException("Category not found with id: " + productDTO.getCategory().getId()));
                product.setCategory(category);
            } else {
                product.setCategory(productDTO.getCategory());
            }
        }
        
        // Валидация обязательных полей
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new RuntimeException("Product name is required");
        }
        if (product.getDescription() == null || product.getDescription().trim().isEmpty()) {
            throw new RuntimeException("Product description is required");
        }
        if (product.getPrice() == null) {
            throw new RuntimeException("Product price is required");
        }
        if (product.getGender() == null) {
            throw new RuntimeException("Product gender is required");
        }
        if (product.getProductStatus() == null) {
            throw new RuntimeException("Product status is required");
        }
        if (product.getCategory() == null) {
            throw new RuntimeException("Product category is required");
        }
        
        // Логируем данные продукта перед сохранением
        System.out.println("Product before save: " + product);
        
        // Сначала сохраняем продукт без вариантов
        Product savedProduct = productRepository.save(product);
        
        // Создаем и сохраняем варианты
        if (productDTO.getVariants() != null && !productDTO.getVariants().isEmpty()) {
            List<ProductVariant> variants = createProductVariants(productDTO.getVariants(), savedProduct);
            savedProduct.setVariants(variants);
        }
        
        return savedProduct;
    }
    
    private List<ProductVariant> createProductVariants(List<ProductVariantDTO> variantDTOs, Product product) {
        List<ProductVariant> variants = new ArrayList<>();
        
        for (ProductVariantDTO variantDTO : variantDTOs) {
            ProductVariant variant = new ProductVariant();
            variant.setProduct(product);
            variant.setStock(variantDTO.getStock());
            variant.setPrice(variantDTO.getPrice());
            
            // Находим или создаем Color
            Color color = findOrCreateColor(variantDTO.getColor());
            variant.setColor(color);
            
            // Находим или создаем Size
            Size size = findOrCreateSize(variantDTO.getSize());
            variant.setSize(size);
            
            variants.add(variant);
        }
        
        return variants;
    }
    
    private Color findOrCreateColor(String colorName) {
        return colorRepository.findByName(colorName)
            .orElseGet(() -> {
                Color newColor = Color.builder()
                    .name(colorName)
                    .build();
                return colorRepository.save(newColor);
            });
    }
    
    private Size findOrCreateSize(String sizeName) {
        return sizeRepository.findByName(sizeName)
            .orElseGet(() -> {
                Size newSize = Size.builder()
                    .name(sizeName)
                    .build();
                return sizeRepository.save(newSize);
            });
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

    public void deleteProducts(){
        productRepository.deleteAll();
    }
}
