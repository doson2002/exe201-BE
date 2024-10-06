package com.exe201.exe201be.services;

import com.exe201.exe201be.entities.FAQ;
import com.exe201.exe201be.entities.FoodType;
import com.exe201.exe201be.exceptions.DataNotFoundException;
import com.exe201.exe201be.repositories.FAQRepository;
import com.exe201.exe201be.services.FireBase.FireBaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FAQService implements IFAQService {
    private final FAQRepository faqRepository;
    private final FireBaseService fireBaseService;

    public String createFAQ(MultipartFile[] files, String title, String article) {
        try {
            // Biến để lưu URL của các file đã upload
            List<String> uploadedImageUrls = new ArrayList<>();

            // Upload từng file lên Firebase Storage
            for (MultipartFile file : files) {
                String imageUrl = fireBaseService.uploadFAQImageToFirebase(file);
                uploadedImageUrls.add(imageUrl); // Lưu URL vào danh sách
            }

            // Thay thế các placeholder trong nội dung bài viết
            for (int i = 0; i < uploadedImageUrls.size(); i++) {
                String placeholder = "{{file" + (i + 1) + "}}"; // Tạo placeholder như {{file1}}, {{file2}}, ...
                article = article.replace(placeholder, "<img src=\"" + uploadedImageUrls.get(i) + "\" alt=\"Image\">");
            }

            // Lưu bài viết vào database
            FAQ newFaq = new FAQ();
            newFaq.setTitle(title);
            newFaq.setArticle(article);
            faqRepository.save(newFaq);

            return "Bài viết đã được tạo thành công!";
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tạo bài viết: " + e.getMessage());
        }
    }

    // Hàm lấy tất cả FAQs với tùy chọn tìm kiếm
    public List<FAQ> getAllFaq(String search) {
        if (search == null || search.isEmpty()) {
            return faqRepository.findAll(); // Lấy tất cả nếu không có từ khóa tìm kiếm
        } else {
            return faqRepository.searchByTitleOrArticle(search);
        }
    }
    // Hàm lấy chi tiết FAQ theo ID
    public FAQ getFaqDetailById(Long id) throws DataNotFoundException {
        return faqRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("FAQ not found with id " + id));
    }
}
