package e_commerce.khilat.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;


import e_commerce.khilat.entity.ProductImage;
import e_commerce.khilat.service.ProductImageService;

@SuppressWarnings("unused")
@RestController
@RequestMapping("/api/product-images")
@CrossOrigin
public class ProductImageController {

    @Autowired
    private ProductImageService productImageService;

    @PostMapping(
        value = "/upload",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ProductImage> uploadImage(
            @RequestParam Long productId,
            @RequestParam MultipartFile image) {

        return ResponseEntity.ok(
                productImageService.uploadProductImage(productId, image)
        );
    }

    @GetMapping("/{productId}")
    public ResponseEntity<List<ProductImage>> getProductImages(
            @PathVariable Long productId) {

        return ResponseEntity.ok(
                productImageService.getImagesByProduct(productId)
        );
    }
}
