package com.exe201.exe201be.services;

import com.exe201.exe201be.entities.ImageBanner;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageBannerService {
    List<ImageBanner> getAllImageBanner();
    String createBanner(MultipartFile file) throws Exception;
}
