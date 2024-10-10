package com.exe201.exe201be.controllers;

import com.exe201.exe201be.entities.ImageBanner;
import com.exe201.exe201be.entities.SupplierType;
import com.exe201.exe201be.services.IImageBannerService;
import com.exe201.exe201be.services.ImageBannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/image_banner")
@RequiredArgsConstructor
public class ImageBannerController {
    private final IImageBannerService imageBannerService;
    @GetMapping("/get_all_image_banners")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_PARTNER','ROLE_CUSTOMER')")
    public ResponseEntity<?> getAllSupplier(@RequestParam(defaultValue = "1") int bannerType) {
        List<ImageBanner> imageBanners = imageBannerService.getAllImageBanner(bannerType);
        return ResponseEntity.ok(imageBanners);
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_PARTNER','ROLE_CUSTOMER')")
    public ResponseEntity<?> createBanner(@RequestParam("files") MultipartFile[] files) {
        StringBuilder responseMessage = new StringBuilder();

        try {
            for (MultipartFile file : files) {
                String imageUrl = imageBannerService.createBanner(file);
                responseMessage.append("Banner created successfully: ").append(imageUrl).append("\n");
            }
            return ResponseEntity.ok(responseMessage.toString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
}
