package com.bazarweb.bazarweb.service.Product;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.bazarweb.bazarweb.model.Product.Image;
import com.bazarweb.bazarweb.model.Product.Product;
import com.bazarweb.bazarweb.repository.Product.ImageRepository;
import com.bazarweb.bazarweb.repository.Product.ProductRepository;

import jakarta.persistence.EntityNotFoundException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class ImageService {
    private ImageRepository imageRepository;
    private ProductRepository productRepository;

    public ImageService(ImageRepository imageRepository, ProductRepository productRepository){
        this.imageRepository = imageRepository;
        this.productRepository = productRepository;
    }

    public Image saveImage(MultipartFile file, Integer productId) throws IOException {

        Product product = productRepository.findById(productId)
        .orElseThrow(() -> new EntityNotFoundException("Продукт не найден"));

        // Проверка размера и типа файла
        if (file.getSize() > 5 * 1024 * 1024) { // Макс. 5 МБ
            throw new IllegalArgumentException("Файл слишком большой");
        }

        String[] allowedTypes = {"image/jpeg", "image/png", "image/gif"};
        boolean isAllowedType = Arrays.stream(allowedTypes)
            .anyMatch(type -> type.equals(file.getContentType()));

        if (!isAllowedType) {
            throw new IllegalArgumentException("Неподдерживаемый тип файла");
        }

        Image image = new Image();
        image.setProduct(product);
        image.setName(file.getOriginalFilename());
        image.setFileContent(file.getBytes());
        image.setContentType(file.getContentType());
        image.setFileSize((int) file.getSize());

        return imageRepository.save(image);
    }

    public Image getImage(int id) {
        return imageRepository.findByProductId(id)
            .orElseThrow(() -> new EntityNotFoundException("Изображение не найдено"));
    }

    public List<Image> getAllImagesByProductId(Integer productId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new EntityNotFoundException("Продукт не найден"));
        
        return imageRepository.findAllByProduct(product);
    }
}