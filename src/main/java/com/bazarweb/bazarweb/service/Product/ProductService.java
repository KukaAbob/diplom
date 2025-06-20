package com.bazarweb.bazarweb.service.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.bazarweb.bazarweb.dto.ProductDTO;
import com.bazarweb.bazarweb.dto.ProductVariantDTO;
import com.bazarweb.bazarweb.dto.Requests.admin.ProductCreateRequest;
import com.bazarweb.bazarweb.dto.Requests.admin.ProductUpdateRequest;
import com.bazarweb.bazarweb.dto.Requests.admin.ProductVariantUpdateRequest;
import com.bazarweb.bazarweb.enums.Gender;
import com.bazarweb.bazarweb.enums.ProductStatus;
import com.bazarweb.bazarweb.model.Catalog.Category;
import com.bazarweb.bazarweb.model.Product.Color;
import com.bazarweb.bazarweb.model.Product.Product;
import com.bazarweb.bazarweb.model.Product.ProductVariant;
import com.bazarweb.bazarweb.model.Product.Size;
import com.bazarweb.bazarweb.repository.Catalog.CategoryRepository;
import com.bazarweb.bazarweb.repository.Product.ColorRepository;
import com.bazarweb.bazarweb.repository.Product.ProductRepository;
import com.bazarweb.bazarweb.repository.Product.ProductVariantRepository;
import com.bazarweb.bazarweb.repository.Product.SizeRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
    
    private final ProductRepository productRepository;
    private final ProductVariantRepository productVariantRepository;
    private final CategoryRepository categoryRepository;
    private final ColorRepository colorRepository;
    private final SizeRepository sizeRepository;
    
    public Product save(Product product){
        return productRepository.save(product);
    }

    public Product createFromRequest(ProductCreateRequest request) {
        // Найти категорию
        Category category = categoryRepository.findById(request.getCategoryId())
            .orElseThrow(() -> new EntityNotFoundException("Категория с ID " + request.getCategoryId() + " не найдена"));
        
        // Создать продукт
        Product product = new Product();
        product.setName(request.getName().trim());
        product.setDescription(request.getDescription().trim());
        product.setPrice(request.getPrice());
        product.setCollection(request.getCollection() != null ? request.getCollection().trim() : null);
        product.setCategory(category);
        product.setProductStatus(request.getProductStatus());
        product.setGender(request.getGender());
        
        return productRepository.save(product);
    }

    public Product productCreate(Product incomingProduct) {
    // Логируем входные данные
    System.out.println("Creating product with data: " + incomingProduct);
    
    // Создаем новый продукт (НЕ перезаписываем параметр!)
    Product product = new Product();
    product.setName(incomingProduct.getName());
    product.setDescription(incomingProduct.getDescription());
    product.setPrice(incomingProduct.getPrice());
    product.setCollection(incomingProduct.getCollection());
    product.setGender(incomingProduct.getGender());
    product.setProductStatus(incomingProduct.getProductStatus());
    
    // Проверяем и загружаем категорию
    if (incomingProduct.getCategory() != null) {
        if (incomingProduct.getCategory().getId() != 0) {
            Category category = categoryRepository.findById(incomingProduct.getCategory().getId())
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + incomingProduct.getCategory().getId()));
            product.setCategory(category);
        } else {
            product.setCategory(incomingProduct.getCategory());
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
    
    // Создаем и сохраняем варианты (если они есть в entity)
    if (incomingProduct.getVariants() != null && !incomingProduct.getVariants().isEmpty()) {
        List<ProductVariant> variants = createProductVariantsFromEntity(incomingProduct.getVariants(), savedProduct);
        savedProduct.setVariants(variants);
        // Пересохраняем продукт с вариантами
        savedProduct = productRepository.save(savedProduct);
    }
    
    return savedProduct;
}

// Метод для создания вариантов из Entity (если Product содержит List<ProductVariant>)
private List<ProductVariant> createProductVariantsFromEntity(List<ProductVariant> incomingVariants, Product product) {
    List<ProductVariant> variants = new ArrayList<>();
    
    for (ProductVariant incomingVariant : incomingVariants) {
        ProductVariant variant = new ProductVariant();
        variant.setProduct(product);
        variant.setStock(incomingVariant.getStock());
        variant.setPrice(incomingVariant.getPrice());
        
        // Если color и size приходят как строки
        if (incomingVariant.getColor() != null) {
            Color color = findOrCreateColor(incomingVariant.getColor().getName());
            variant.setColor(color);
        }
        
        if (incomingVariant.getSize() != null) {
            Size size = findOrCreateSize(incomingVariant.getSize().getName());
            variant.setSize(size);
        }
        
        // Сохраняем вариант
        variants.add(productVariantRepository.save(variant));
    }
    
    return variants;
}

private Color findOrCreateColor(String colorName) {
    if (colorName == null || colorName.trim().isEmpty()) {
        throw new RuntimeException("Color name cannot be empty");
    }
    
    return colorRepository.findByName(colorName.trim())
        .orElseGet(() -> {
            Color newColor = Color.builder()
                .name(colorName.trim())
                .build();
            return colorRepository.save(newColor);
        });
}

private Size findOrCreateSize(String sizeName) {
    if (sizeName == null || sizeName.trim().isEmpty()) {
        throw new RuntimeException("Size name cannot be empty");
    }
    
    return sizeRepository.findByName(sizeName.trim())
        .orElseGet(() -> {
            Size newSize = Size.builder()
                .name(sizeName.trim())
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

public ProductDTO updateProduct(Integer id, ProductDTO productDTO) {
    // Find existing product
    Product existingProduct = productRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

    System.out.println("Existing product found: " + existingProduct.getName());
    System.out.println("Updating with data: " + productDTO);

    // Update basic fields if they're not null
    if (productDTO.getName() != null) {
        existingProduct.setName(productDTO.getName());
    }
    if (productDTO.getDescription() != null) {
        existingProduct.setDescription(productDTO.getDescription());
    }
    if (productDTO.getPrice() != null) {
        existingProduct.setPrice(productDTO.getPrice());
    }
    if (productDTO.getCollection() != null) {
        existingProduct.setCollection(productDTO.getCollection());
    }
    if (productDTO.getProductStatus() != null) {
        existingProduct.setProductStatus(productDTO.getProductStatus());
    }
    if (productDTO.getGender() != null) {
        existingProduct.setGender(productDTO.getGender());
    }

    // Update category if provided
    if (productDTO.getCategory() != null && productDTO.getCategory().getId() != null) {
        Category category = categoryRepository.findById(productDTO.getCategory().getId())
            .orElseThrow(() -> new RuntimeException("Category not found: " + productDTO.getCategory().getId()));
        existingProduct.setCategory(category);
    }

    // Update variants if provided
    if (productDTO.getVariants() != null && !productDTO.getVariants().isEmpty()) {
        // Remove old variants
        productVariantRepository.deleteByProductId(id);
        
        // Create new variants
        List<ProductVariant> updatedVariants = productDTO.getVariants().stream()
            .map(variantDTO -> {
                ProductVariant variant = variantDTO.toEntity(colorRepository, sizeRepository);
                variant.setProduct(existingProduct);
                return productVariantRepository.save(variant);
            })
            .collect(Collectors.toList());
        
        existingProduct.setVariants(updatedVariants);
    }

    // Save and return updated product
    Product savedProduct = productRepository.save(existingProduct);
    return ProductDTO.fromEntity(savedProduct);
}

    public void deleteProduct(int id) {
        productRepository.deleteById(id);
    }

    public void deleteProducts(){
        productRepository.deleteAll();
    }
}
