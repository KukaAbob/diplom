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

// Альтернативный метод, если варианты приходят как строки в JSON
private List<ProductVariant> createProductVariantsFromStrings(Map<String, Object> variantData, Product product) {
    List<ProductVariant> variants = new ArrayList<>();
    
    // Предполагаем, что варианты приходят в формате:
    // "variants": [{"color": "Red", "size": "M", "stock": 10, "price": 100.0}]
    if (variantData.containsKey("variants")) {
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> variantsList = (List<Map<String, Object>>) variantData.get("variants");
        
        for (Map<String, Object> variantMap : variantsList) {
            ProductVariant variant = new ProductVariant();
            variant.setProduct(product);
            
            if (variantMap.containsKey("stock")) {
                variant.setStock(((Number) variantMap.get("stock")).intValue());
            }
            
            if (variantMap.containsKey("price")) {
                variant.setPrice(new BigDecimal(variantMap.get("price").toString()));
            }
            
            if (variantMap.containsKey("color")) {
                Color color = findOrCreateColor(variantMap.get("color").toString());
                variant.setColor(color);
            }
            
            if (variantMap.containsKey("size")) {
                Size size = findOrCreateSize(variantMap.get("size").toString());
                variant.setSize(size);
            }
            
            variants.add(productVariantRepository.save(variant));
        }
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

public ProductDTO updateProduct(Integer id, ProductUpdateRequest request) {
        System.out.println("=== НАЧАЛО ОБНОВЛЕНИЯ В СЕРВИСЕ ===");
        
        // 1. Находим существующий продукт
        Product existingProduct = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Продукт с ID " + id + " не найден"));
        
        System.out.println("Найден продукт: " + existingProduct.getName());
        
        // 2. Обновляем основные поля
        updateBasicFields(existingProduct, request);
        
        // 3. Обновляем категорию
        updateCategory(existingProduct, request);
        
        // 4. Сохраняем основные изменения
        Product savedProduct = productRepository.save(existingProduct);
        System.out.println("Основные поля сохранены");
        
        // 5. Обновляем варианты (если переданы)
        if (request.getVariants() != null) {
            updateVariants(savedProduct, request.getVariants());
        }
        
        // 6. Перезагружаем продукт с обновленными данными
        Product finalProduct = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Ошибка перезагрузки продукта"));
        
        System.out.println("=== ОБНОВЛЕНИЕ ЗАВЕРШЕНО ===");
        return ProductDTO.fromEntity(finalProduct);
    }
    
    private void updateBasicFields(Product product, ProductUpdateRequest request) {
        if (request.getName() != null && !request.getName().trim().isEmpty()) {
            product.setName(request.getName().trim());
        }
        
        if (request.getDescription() != null) {
            product.setDescription(request.getDescription().trim());
        }
        
        if (request.getPrice() != null && request.getPrice().compareTo(BigDecimal.ZERO) > 0) {
            product.setPrice(request.getPrice());
        }
        
        if (request.getCollection() != null) {
            product.setCollection(request.getCollection().trim());
        }
        
        if (request.getGender() != null && !request.getGender().trim().isEmpty()) {
            try {
                product.setGender(Gender.valueOf(request.getGender().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Неверное значение пола: " + request.getGender());
            }
        }
        
        if (request.getProductStatus() != null && !request.getProductStatus().trim().isEmpty()) {
            try {
                product.setProductStatus(ProductStatus.valueOf(request.getProductStatus().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Неверный статус продукта: " + request.getProductStatus());
            }
        }
    }
    
    private void updateCategory(Product product, ProductUpdateRequest request) {
        if (request.getCategoryId() != null && request.getCategoryId() > 0) {
            Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Категория с ID " + request.getCategoryId() + " не найдена"));
            product.setCategory(category);
            System.out.println("Категория обновлена: " + category.getName());
        }
    }
    
    private void updateVariants(Product product, List<ProductVariantUpdateRequest> variantRequests) {
        System.out.println("Обновляем варианты, количество: " + variantRequests.size());
        
        // Получаем существующие варианты
        List<ProductVariant> existingVariants = productVariantRepository.findByProductId(product.getId());
        
        // Собираем ID вариантов, которые нужно оставить
        Set<Integer> keepVariantIds = variantRequests.stream()
            .filter(req -> req.getId() != null && req.getId() > 0)
            .map(ProductVariantUpdateRequest::getId)
            .collect(Collectors.toSet());
        
        // Удаляем варианты, которых нет в запросе
        existingVariants.stream()
            .filter(variant -> !keepVariantIds.contains(variant.getId()))
            .forEach(variant -> {
                System.out.println("Удаляем вариант ID: " + variant.getId());
                productVariantRepository.delete(variant);
            });
        
        // Обновляем или создаем варианты
        for (ProductVariantUpdateRequest variantRequest : variantRequests) {
            updateOrCreateVariant(product, variantRequest);
        }
        
        System.out.println("Варианты обновлены");
    }
    
    private void updateOrCreateVariant(Product product, ProductVariantUpdateRequest request) {
        ProductVariant variant;
        
        if (request.getId() != null && request.getId() > 0) {
            // Обновляем существующий вариант
            variant = productVariantRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Вариант с ID " + request.getId() + " не найден"));
            System.out.println("Обновляем существующий вариант ID: " + variant.getId());
        } else {
            // Создаем новый вариант
            variant = new ProductVariant();
            variant.setProduct(product);
            System.out.println("Создаем новый вариант");
        }
        
        // Обновляем цвет
        if (request.getColorId() != null && request.getColorId() > 0) {
            Color color = colorRepository.findById(request.getColorId())
                .orElseThrow(() -> new RuntimeException("Цвет с ID " + request.getColorId() + " не найден"));
            variant.setColor(color);
        }
        
        // Обновляем размер
        if (request.getSizeId() != null && request.getSizeId() > 0) {
            Size size = sizeRepository.findById(request.getSizeId())
                .orElseThrow(() -> new RuntimeException("Размер с ID " + request.getSizeId() + " не найден"));
            variant.setSize(size);
        }
        
        // Обновляем остальные поля
        if (request.getStock() != null) {
            variant.setStock(request.getStock());
        }
        if (request.getPrice() != null) {
            variant.setPrice(request.getPrice());
        }
        
        productVariantRepository.save(variant);
        System.out.println("Вариант сохранен");
    }

    public void deleteProduct(int id) {
        productRepository.deleteById(id);
    }

    public void deleteProducts(){
        productRepository.deleteAll();
    }
}
