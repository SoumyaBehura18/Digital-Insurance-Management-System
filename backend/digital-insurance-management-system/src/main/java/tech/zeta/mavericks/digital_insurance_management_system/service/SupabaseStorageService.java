package tech.zeta.mavericks.digital_insurance_management_system.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;

/**
 * Service for handling file uploads to Supabase Storage
 * This service provides clean and simple file upload functionality
 */
@Service
public class SupabaseStorageService {

    // Supabase configuration
    @Value("${supabase.url:https://exhhnhdfkmwxluwhvyvq.supabase.co}")
    private String supabaseUrl;
    
    @Value("${supabase.anon.key:your-anon-key}")
    private String supabaseAnonKey;
    
    @Value("${supabase.bucket.name:claim-proofs}")
    private String bucketName;

    private final HttpClient httpClient = HttpClient.newHttpClient();

    /**
     * Upload a file to Supabase Storage and return the public URL
     * 
     * @param file The multipart file to upload
     * @param claimId The claim ID for organizing files
     * @return The public URL of the uploaded file
     */
    public String uploadFile(MultipartFile file, Long claimId) throws IOException, InterruptedException {
        System.out.println("SupabaseStorageService: Starting file upload");
        System.out.println("File name: " + file.getOriginalFilename());
        System.out.println("File size: " + file.getSize());
        System.out.println("Content type: " + file.getContentType());
        
        // Validate file
        if (!isValidFileType(file)) {
            System.err.println("Invalid file type: " + file.getContentType());
            throw new IllegalArgumentException("Invalid file type. Allowed types: JPG, PNG, PDF, DOC, DOCX");
        }
        
        if (!isValidFileSize(file)) {
            System.err.println("File size too large: " + file.getSize());
            throw new IllegalArgumentException("File size exceeds 5MB limit");
        }
        
        System.out.println("File validation passed");
        
        // Generate unique filename to avoid conflicts
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf('.'));
        String uniqueFilename = "claim-" + claimId + "-" + UUID.randomUUID() + fileExtension;
        
        // Create file path in bucket
        String filePath = "claims/" + claimId + "/" + uniqueFilename;
        
        // Build the upload URL
        String uploadUrl = supabaseUrl + "/storage/v1/object/" + bucketName + "/" + filePath;
        System.out.println("Upload URL: " + uploadUrl);
        
        // Create the request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uploadUrl))
                .POST(HttpRequest.BodyPublishers.ofByteArray(file.getBytes()))
                .header("Authorization", "Bearer " + supabaseAnonKey)
                .header("Content-Type", file.getContentType())
                .build();
        
        System.out.println("Sending request to Supabase...");
        
        // Execute the request
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        System.out.println("Supabase response status: " + response.statusCode());
        System.out.println("Supabase response body: " + response.body());
        
        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            String publicUrl = getPublicUrl(filePath);
            System.out.println("File uploaded successfully, public URL: " + publicUrl);
            return publicUrl;
        } else {
            throw new IOException("Failed to upload file to Supabase. Status: " + response.statusCode() + ", Body: " + response.body());
        }
    }
    
    /**
     * Generate public URL for accessing the uploaded file
     * 
     * @param filePath The file path in the bucket
     * @return The public URL
     */
    private String getPublicUrl(String filePath) {
        return supabaseUrl + "/storage/v1/object/public/" + bucketName + "/" + filePath;
    }
    
    /**
     * Validate if the file type is allowed
     * 
     * @param file The file to validate
     * @return true if file type is allowed
     */
    public boolean isValidFileType(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null) return false;
        
        return contentType.equals("image/jpeg") ||
               contentType.equals("image/jpg") ||
               contentType.equals("image/png") ||
               contentType.equals("application/pdf") ||
               contentType.equals("application/msword") ||
               contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
    }
    
    /**
     * Check if file size is within limits (5MB)
     * 
     * @param file The file to check
     * @return true if file size is acceptable
     */
    public boolean isValidFileSize(MultipartFile file) {
        long maxSize = 5 * 1024 * 1024; // 5MB
        return file.getSize() <= maxSize;
    }
}
