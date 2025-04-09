package com.bazarweb.bazarweb.controller.Product;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.bazarweb.bazarweb.model.Product.Image;
import com.bazarweb.bazarweb.service.Product.ImageService;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(
        @RequestParam("file") MultipartFile file, 
        @RequestParam("productId") Integer productId
    ) {
        try {
            Image savedImage = imageService.saveImage(file, productId);
            return ResponseEntity.ok("Изображение загружено. ID: " + savedImage.getId());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Ошибка загрузки: " + e.getMessage());
        }
    }

    @GetMapping("/{productId}")
    public ResponseEntity<byte[]> getProductImage(@PathVariable Integer productId) {
        Image image = imageService.getImage(productId);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(image.getContentType()));
        headers.setContentLength(image.getFileSize());
        headers.set(HttpHeaders.CONTENT_DISPOSITION, 
            "inline; filename=\"" + image.getName() + "\"");
        
        return new ResponseEntity<>(image.getFileContent(), headers, HttpStatus.OK);
    }

    // Если у продукта несколько изображений
    @GetMapping("/all/{productId}")
    public ResponseEntity<List<byte[]>> getAllProductImages(@PathVariable Integer productId) {
        List<Image> images = imageService.getAllImagesByProductId(productId);
        
        List<byte[]> imageContents = images.stream()
            .map(Image::getFileContent)
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(imageContents);
    }
}