package e_commerce.khilat.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import e_commerce.khilat.controller.ProductController;
import e_commerce.khilat.entity.Product;
import e_commerce.khilat.entity.ProductImage;
import e_commerce.khilat.repository.ProductImageRepo;
import e_commerce.khilat.repository.ProductRepo;

@Service
public class ProductImageService {
	
	private static final Logger LOGGER =
            LoggerFactory.getLogger(ProductImageService.class);

    private static final String UPLOAD_DIR = "uploads/products/";

    @Autowired
    private ProductImageRepo productImageRepo;

    @Autowired
    private ProductRepo productRepo;
    
    @Autowired
    private CloudflareR2Service cloudflareR2Service;

    public List<ProductImage> getImagesByProduct(Long productId) {
        return productImageRepo.findByProduct_Id(productId);
    }

    public ProductImage uploadProductImage(Long productId, MultipartFile file) {
    	
    	LOGGER.debug("process started of saving img");

        if (file == null || file.isEmpty()) {
            throw new RuntimeException("Image is required");
        }

        try {
            // 1️⃣ Fetch product
            Product product = productRepo.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            
            LOGGER.debug("product with id: {} rcvd" , product.toString());

            // 2️⃣ Upload image to ImgBB
            String imageUrl = cloudflareR2Service.uploadImage(file); // Returns the public URL

            // 3️⃣ Save ProductImage
            ProductImage image = new ProductImage();
            image.setProduct(product);
            image.setImageUrl(imageUrl);
            

            return productImageRepo.save(image);  // ✅ Store ImgBB URL

        } catch (Exception e) {
            throw new RuntimeException("Image upload failed", e);
        }
    }
    
    public void deleteProductImage(Long imageId) {
        ProductImage image = productImageRepo.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Image not found"));

        // ⚠️ Abhi sirf DB delete
        productImageRepo.delete(image);
    }


}
