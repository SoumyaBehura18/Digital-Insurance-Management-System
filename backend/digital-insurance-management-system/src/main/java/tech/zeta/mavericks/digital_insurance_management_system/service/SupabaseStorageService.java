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
 * Service for handling file uploads to Supabase Storage.
 *
 * This service provides secure file upload functionality for claim supporting documents.
 * It handles file validation, unique filename generation, and direct upload to Supabase
 * storage buckets. The service supports multiple file formats including images, PDFs,
 * and Word documents with size limitations for security and performance.
 *
 * Features:
 * - File type validation (JPG, PNG, PDF, DOC, DOCX)
 * - File size validation (5MB limit)
 * - Unique filename generation to prevent conflicts
 * - Direct HTTP API integration with Supabase Storage
 * - Public URL generation for uploaded files
 * - Organized file structure by claim ID
 */
@Service
public class SupabaseStorageService {

    /** Base URL for Supabase instance - configured via application properties */
    @Value("${supabase.url:https://exhhnhdfkmwxluwhvyvq.supabase.co}")
    private String supabaseUrl;
    
    /** Anonymous key for Supabase authentication - configured via application properties */
    @Value("${supabase.anon.key:your-anon-key}")
    private String supabaseAnonKey;
    
    /** Name of the Supabase storage bucket for claim documents */
    @Value("${supabase.bucket.name:claim-proofs}")
    private String bucketName;

    /** HTTP client for making requests to Supabase Storage API */
    private final HttpClient httpClient = HttpClient.newHttpClient();

    /**
     * Uploads a file to Supabase Storage and returns the public URL.
     *
     * This method performs complete file upload workflow:
     * 1. Validates file type and size
     * 2. Generates unique filename with claim ID prefix
     * 3. Creates organized folder structure in bucket
     * 4. Uploads file via HTTP POST to Supabase Storage API
     * 5. Returns public URL for accessing the uploaded file
     *
     * The uploaded file is stored with path: claims/{claimId}/{uniqueFilename}
     *
     * @param file The multipart file to upload (must be valid type and size)
     * @param claimId The claim ID for organizing files in folders
     * @return The public URL of the uploaded file for accessing via web
     * @throws IOException If file upload fails or network issues occur
     * @throws InterruptedException If the upload request is interrupted
     * @throws IllegalArgumentException If file type or size validation fails
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
     * Generates a public URL for accessing an uploaded file.
     *
     * Constructs the public URL using Supabase's public object endpoint.
     * The generated URL allows direct access to the file without authentication.
     *
     * @param filePath The file path within the storage bucket
     * @return The complete public URL for accessing the file
     */
    private String getPublicUrl(String filePath) {
        return supabaseUrl + "/storage/v1/object/public/" + bucketName + "/" + filePath;
    }
    
    /**
     * Validates if the uploaded file type is allowed for claim documents.
     *
     * Checks the MIME type against a whitelist of supported formats:
     * - Images: JPEG, JPG, PNG
     * - Documents: PDF, DOC, DOCX
     *
     * @param file The multipart file to validate
     * @return true if file type is allowed, false otherwise
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
     * Validates if the file size is within acceptable limits.
     *
     * Enforces a maximum file size of 5MB to ensure reasonable upload times
     * and prevent abuse of storage resources.
     *
     * @param file The multipart file to check
     * @return true if file size is within the 5MB limit, false otherwise
     */
    public boolean isValidFileSize(MultipartFile file) {
        long maxSize = 5 * 1024 * 1024; // 5MB
        return file.getSize() <= maxSize;
    }
}
