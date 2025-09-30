package tech.zeta.mavericks.digital_insurance_management_system.serviceTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import tech.zeta.mavericks.digital_insurance_management_system.service.SupabaseStorageService;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class SupabaseStorageServiceTest {

    private SupabaseStorageService service;

    @BeforeEach
    void setUp() throws Exception {
        service = new SupabaseStorageService();
        // Inject simple dummy values for @Value fields via reflection
        setPrivateField("supabaseUrl", "https://dummy.supabase.co");
        setPrivateField("supabaseAnonKey", "anon-key");
        setPrivateField("bucketName", "claim-proofs");
    }

    private void setPrivateField(String name, Object value) throws Exception {
        Field f = SupabaseStorageService.class.getDeclaredField(name);
        f.setAccessible(true);
        f.set(service, value);
    }

    @Test
    @DisplayName("isValidFileType should accept PDF")
    void isValidFileType_acceptsPdf() {
        MockMultipartFile file = new MockMultipartFile("document", "file.pdf", "application/pdf", new byte[]{1});
        assertTrue(service.isValidFileType(file));
    }

    @Test
    @DisplayName("isValidFileType should reject TXT")
    void isValidFileType_rejectsTxt() {
        MockMultipartFile file = new MockMultipartFile("document", "file.txt", "text/plain", new byte[]{1});
        assertFalse(service.isValidFileType(file));
    }

    @Test
    @DisplayName("isValidFileSize should accept file < 5MB")
    void isValidFileSize_acceptsUnderLimit() {
        byte[] data = new byte[1024]; // 1 KB
        MockMultipartFile file = new MockMultipartFile("document", "file.pdf", "application/pdf", data);
        assertTrue(service.isValidFileSize(file));
    }

    @Test
    @DisplayName("isValidFileSize should reject file > 5MB")
    void isValidFileSize_rejectsOverLimit() {
        byte[] data = new byte[5 * 1024 * 1024 + 10]; // Just over 5MB
        MockMultipartFile file = new MockMultipartFile("document", "file.pdf", "application/pdf", data);
        assertFalse(service.isValidFileSize(file));
    }

    @Test
    @DisplayName("uploadFile should throw for invalid file type before HTTP call")
    void uploadFile_throwsOnInvalidType() {
        MockMultipartFile file = new MockMultipartFile("document", "file.txt", "text/plain", new byte[]{1});
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.uploadFile(file, 1L));
        assertTrue(ex.getMessage().contains("Invalid file type"));
    }

    @Test
    @DisplayName("uploadFile should throw for oversized file before HTTP call")
    void uploadFile_throwsOnOversize() {
        byte[] data = new byte[5 * 1024 * 1024 + 100];
        MockMultipartFile file = new MockMultipartFile("document", "file.pdf", "application/pdf", data);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.uploadFile(file, 2L));
        assertTrue(ex.getMessage().contains("File size exceeds"));
    }
}