package com.exe201.exe201be.services;

import com.exe201.exe201be.entities.ImageBanner;
import com.exe201.exe201be.entities.SupplierType;
import com.exe201.exe201be.repositories.ImageBannerRepository;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.firebase.cloud.StorageClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImageBannerService implements IImageBannerService{
    private final ImageBannerRepository imageBannerRepository;
    private final String bucketName = "exe201-c1658.appspot.com"; // Đặt tên bucket của

    public List<ImageBanner> getAllImageBanner(int bannerType) {
        return imageBannerRepository.findByBannerType(bannerType).stream()
                .filter(ImageBanner::getIsActived) // Lọc các banner có isActived = true
                .collect(Collectors.toList());
    }

    @Override
    public String createBanner(MultipartFile file) throws Exception {
        // Upload file to Firebase
        String imageUrl = uploadImageToFirebase(file);

        // Save image URL to database
        ImageBanner imageBanner = ImageBanner.builder()
                .imageUrl(imageUrl)
                .isActived(true)
                .build();
        imageBannerRepository.save(imageBanner);

        return imageUrl; // Trả về URL ảnh
    }

    public String uploadImageToFirebase(MultipartFile file) throws IOException {
        // Lấy bucket
        Bucket bucket = StorageClient.getInstance().bucket(); // đã config trong FirebaseConfig
        // Kiểm tra xem bucket có tồn tại không
        if (bucket == null) {
            throw new RuntimeException("Bucket not found: " + bucketName);
        }
        // Tạo đường dẫn cho file upload
        String fileName = "images/" + System.currentTimeMillis() + "_" + file.getOriginalFilename();

        // Upload file
        Blob blob = bucket.create(fileName, file.getInputStream(), file.getContentType());

        // Trả về URL của file đã upload
        String downloadUrl = "https://firebasestorage.googleapis.com/v0/b/" + bucket.getName() +
                "/o/" + URLEncoder.encode(blob.getName(), StandardCharsets.UTF_8) + "?alt=media";

        return downloadUrl;
    }
}
