package com.exe201.exe201be.services.FireBase;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class FireBaseService {

    private final String bucketName = "exe201-c1658.appspot.com";

    public String uploadFAQImageToFirebase(MultipartFile file) throws IOException {
        // Lấy bucket
        Bucket bucket = StorageClient.getInstance().bucket(); // đã config trong FirebaseConfig
        // Kiểm tra xem bucket có tồn tại không
        if (bucket == null) {
            throw new RuntimeException("Bucket not found: " + bucketName);
        }
        // Tạo đường dẫn cho file upload
        String fileName = "images/FAQ/" + System.currentTimeMillis() + "_" + file.getOriginalFilename();

        // Upload file
        Blob blob = bucket.create(fileName, file.getInputStream(), file.getContentType());

        // Trả về URL của file đã upload
        String downloadUrl = "https://firebasestorage.googleapis.com/v0/b/" + bucket.getName() +
                "/o/" + URLEncoder.encode(blob.getName(), StandardCharsets.UTF_8) + "?alt=media";

        return downloadUrl;
    }
}
