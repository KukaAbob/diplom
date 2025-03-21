package com.bazarweb.bazarweb.service.Product;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bazarweb.bazarweb.model.Product.ProductVariant;
import com.bazarweb.bazarweb.repository.Product.ProductVariantRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductVariantService {
    private final ProductVariantRepository productVariantRepository;

    // Получить все варианты товара по ID продукта
    public List<ProductVariant> getVariantsByProductId(int productId) {
        return productVariantRepository.findByProductId(productId);
    }

    // Добавить новый вариант товара
    public ProductVariant addProductVariant(ProductVariant variant) {
        return productVariantRepository.save(variant);
    }

    // Удалить вариант товара
    public void deleteProductVariant(int variantId) {
        productVariantRepository.deleteById(variantId);
    }
}
