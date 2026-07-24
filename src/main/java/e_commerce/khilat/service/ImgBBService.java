package e_commerce.khilat.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;


@Service
public class ImgBBService {
	
	private static final Logger LOGGER =
            LoggerFactory.getLogger(ImgBBService.class);

    private final String apiKey = "46929eb0d1c8c7724d8cc6c373d0ca19"; // Replace with your API key
    
    
    public String uploadImage(MultipartFile file) throws Exception {

        LOGGER.debug("Uploading image to ImgBB: {}", file.getOriginalFilename());

        validateFileSize(file);

        byte[] compressedImage = compressImage(file);

        String boundary = generateBoundary();

        byte[] requestBody = buildMultipartBody(file, compressedImage, boundary);

        HttpRequest request = buildHttpRequest(requestBody, boundary);

        return uploadWithRetry(request);
    }
    
    private void validateFileSize(MultipartFile file) {

        long fileSize = file.getSize();

        LOGGER.debug("File size in bytes: {}", fileSize);

        if (fileSize > 32 * 1024 * 1024) {
            throw new RuntimeException("File size exceeds ImgBB limit of 32MB");
        }
    }
    
    private byte[] compressImage(MultipartFile file) throws Exception {

        BufferedImage originalImage = ImageIO.read(file.getInputStream());

        String originalFileName = file.getOriginalFilename();

        String extension = getFileExtension(originalFileName);

        if (extension == null) {
            extension = "jpg";
        }

        BufferedImage resizedImage = Thumbnails.of(originalImage)
                .size(1024, 1024)
                .outputFormat(extension)
                .asBufferedImage();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        ImageIO.write(resizedImage, extension, baos);

        byte[] fileBytes = baos.toByteArray();

        LOGGER.debug("Compressed image bytes length: {}", fileBytes.length);

        return fileBytes;
    }
    
    private String generateBoundary() {
        return "Boundary-" + System.currentTimeMillis();
    }
    
    private byte[] buildMultipartBody(
            MultipartFile file,
            byte[] fileBytes,
            String boundary
    ) throws Exception {

        String fileNameEncoded = URLEncoder.encode(
                file.getOriginalFilename(),
                "UTF-8"
        );

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        String partHeader =
                "--" + boundary + "\r\n" +
                "Content-Disposition: form-data; name=\"image\"; filename=\"" + fileNameEncoded + "\"\r\n" +
                "Content-Type: " + file.getContentType() + "\r\n\r\n";

        outputStream.write(partHeader.getBytes());

        outputStream.write(fileBytes);

        outputStream.write(
                ("\r\n--" + boundary + "--\r\n").getBytes()
        );

        LOGGER.debug(
                "Multipart body prepared, total length: {}",
                outputStream.size()
        );

        return outputStream.toByteArray();
    }
    
    
    private HttpRequest buildHttpRequest(
            byte[] requestBody,
            String boundary
    ) {

        return HttpRequest.newBuilder()
                .uri(URI.create(
                        "https://api.imgbb.com/1/upload?key=" + apiKey
                ))
                .header(
                        "Content-Type",
                        "multipart/form-data; boundary=" + boundary
                )
                .POST(HttpRequest.BodyPublishers.ofByteArray(requestBody))
                .build();
    }
    
    private String uploadWithRetry(HttpRequest request) throws Exception {

        HttpClient client = HttpClient.newHttpClient();

        int maxRetries = 3;

        int attempt = 0;

        while (attempt < maxRetries) {

            attempt++;

            try {

                LOGGER.debug(
                        "Attempt {}: Sending request to ImgBB API...",
                        attempt
                );

                HttpResponse<String> response = client.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );

                LOGGER.debug(
                        "Attempt {}: Received response with status code {}",
                        attempt,
                        response.statusCode()
                );

                LOGGER.debug(
                        "Attempt {}: Raw response body: {}",
                        attempt,
                        response.body()
                );

                String imageUrl = extractImageUrl(response.body());

                if (imageUrl != null && !imageUrl.isEmpty()) {

                    LOGGER.debug(
                            "Image successfully uploaded to ImgBB: {}",
                            imageUrl
                    );

                    return imageUrl;
                }

                LOGGER.warn(
                        "Attempt {} failed: {}",
                        attempt,
                        response.body()
                );

            } catch (Exception ex) {

                LOGGER.warn(
                        "Attempt {} exception: {}",
                        attempt,
                        ex.getMessage()
                );
            }

            Thread.sleep(1000);
        }

        throw new RuntimeException(
                "Failed to upload image after 3 attempts"
        );
    }
    
    private String extractImageUrl(String responseBody) throws Exception {

        ObjectMapper mapper = new ObjectMapper();

        JsonNode root = mapper.readTree(responseBody);

        boolean success = root.path("success").asBoolean(false);

        if (!success) {
            return null;
        }

        return root.path("data")
                .path("url")
                .asText(null);
    }
    
    private String getFileExtension(String fileName) {

        if (fileName == null || !fileName.contains(".")) {
            return null;
        }

        return fileName.substring(fileName.lastIndexOf(".") + 1)
                .toLowerCase();
    }

}
