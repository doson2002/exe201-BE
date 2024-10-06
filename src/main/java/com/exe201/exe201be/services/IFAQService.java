package com.exe201.exe201be.services;

import com.exe201.exe201be.entities.FAQ;
import com.exe201.exe201be.exceptions.DataNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IFAQService {
    String createFAQ(MultipartFile[] files, String title, String article);
    List<FAQ> getAllFaq(String search);
    FAQ getFaqDetailById(Long id) throws DataNotFoundException;
}
