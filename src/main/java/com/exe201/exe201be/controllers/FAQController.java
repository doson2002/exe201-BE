package com.exe201.exe201be.controllers;

import com.exe201.exe201be.dtos.FAQRequestDTO;
import com.exe201.exe201be.dtos.ResponseDTO;
import com.exe201.exe201be.entities.FAQ;
import com.exe201.exe201be.exceptions.DataNotFoundException;
import com.exe201.exe201be.services.FAQService;
import com.exe201.exe201be.services.IFAQService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/faq")
@RequiredArgsConstructor
public class FAQController {
    private final IFAQService faqService;

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_PARTNER')")
    public ResponseEntity<ResponseDTO> createFAQ(
            @RequestParam("files") MultipartFile[] files,
            @RequestParam("title") String title,
            @RequestParam("article") String article) {
        try {
            // Gọi service để xử lý logic tạo bài viết
            String responseMessage = faqService.createFAQ(files, title, article);

            // Trả về JSON với status "success"
            return ResponseEntity.ok(new ResponseDTO("success"));
        } catch (Exception e) {
            // Nếu có lỗi, trả về JSON với status "error" và thông điệp lỗi
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO("error", e.getMessage()));
        }
    }

    // Endpoint để lấy tất cả FAQs với tùy chọn tìm kiếm
    @GetMapping("/get_all_faq")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_PARTNER','ROLE_CUSTOMER')")
    public ResponseEntity<?> getAllFaq(@RequestParam(defaultValue = "") String search) {
        List<FAQ> faqs = faqService.getAllFaq(search);
        return ResponseEntity.ok(faqs);
    }

    // Endpoint để lấy chi tiết FAQ theo ID
    @GetMapping("/get_faq_detail/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_PARTNER','ROLE_CUSTOMER')")
    public ResponseEntity<FAQ> getFaqDetailById(@PathVariable Long id) throws DataNotFoundException {
        FAQ faq = faqService.getFaqDetailById(id);
        return ResponseEntity.ok(faq);
    }
}
