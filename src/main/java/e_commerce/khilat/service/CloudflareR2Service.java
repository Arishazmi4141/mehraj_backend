package e_commerce.khilat.service;

import jakarta.annotation.PostConstruct;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.util.UUID;

@Service
public class CloudflareR2Service {

    private static final Logger LOGGER = LoggerFactory.getLogger(CloudflareR2Service.class);

    @Value("${cloudflare.r2.access-key-id}")
    private String accessKeyId;

    @Value("${cloudflare.r2.secret-access-key}")
    private String secretAccessKey;

    @Value("${cloudflare.r2.endpoint}")
    private String endpoint;

    @Value("${cloudflare.r2.bucket-name}")
    private String bucketName;

    // 🔥 FIX: ADD THIS (IMPORTANT)
    @Value("${cloudflare.r2.public-url}")
    private String publicUrl;

    private S3Client s3Client;

    @PostConstruct
    public void init() {
        this.s3Client = S3Client.builder()
                .endpointOverride(URI.create(endpoint))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKeyId, secretAccessKey)
                ))
                .region(Region.of("auto"))
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(true)
                        .build())
                .build();

        LOGGER.debug("Cloudflare R2 S3Client initialized successfully");
    }

    public String uploadImage(MultipartFile file) throws Exception {

        LOGGER.debug("Uploading image to Cloudflare R2: {}", file.getOriginalFilename());

        validateFileSize(file);

        byte[] compressedImage = compressImage(file);

        String extension = getFileExtension(file.getOriginalFilename());
        if (extension == null) {
            extension = "jpg";
        }

        String uniqueKey = "products/" + UUID.randomUUID() + "." + extension;

        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(uniqueKey)
                .contentType(file.getContentType())
                .build();

        try {
            s3Client.putObject(putRequest, RequestBody.fromBytes(compressedImage));
            LOGGER.debug("Upload SUCCESS to R2: {}", uniqueKey);

        } catch (Exception e) {
            LOGGER.error("R2 upload FAILED", e);
            throw new RuntimeException("R2 upload failed", e);
        }

        // 🔥 FIX: NOW THIS WILL WORK
        LOGGER.error("PUBLIC_URL=[{}]", publicUrl);
        LOGGER.error("PUBLIC_URL_LENGTH={}", publicUrl.length());

        String imageUrl = publicUrl.trim() + "/" + uniqueKey;

        LOGGER.error("FINAL_URL=[{}]", imageUrl);

        return imageUrl;
    }

    private void validateFileSize(MultipartFile file) {
        long fileSize = file.getSize();
        if (fileSize > 32 * 1024 * 1024) {
            throw new RuntimeException("File size exceeds 32MB limit");
        }
    }

    private byte[] compressImage(MultipartFile file) throws Exception {

        BufferedImage originalImage = ImageIO.read(file.getInputStream());

        String extension = getFileExtension(file.getOriginalFilename());
        if (extension == null) {
            extension = "jpg";
        }

        BufferedImage resizedImage = Thumbnails.of(originalImage)
                .size(1024, 1024)
                .outputFormat(extension)
                .asBufferedImage();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(resizedImage, extension, baos);

        return baos.toByteArray();
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return null;
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    }
}