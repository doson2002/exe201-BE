package com.exe201.exe201be.controllers;

import com.exe201.exe201be.dtos.PromotionsDTO;
import com.exe201.exe201be.entities.Promotions;
import com.exe201.exe201be.exceptions.DataNotFoundException;
import com.exe201.exe201be.responses.PromotionResponse;
import com.exe201.exe201be.services.IPromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/promotions")
public class PromotionsController {

    private final IPromotionService promotionService;

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_PARTNER')")
    public ResponseEntity<?> createPromotions(@RequestBody PromotionsDTO promotionsDTO) throws DataNotFoundException {
        try{
            Promotions promotion = promotionService.createPromotion(promotionsDTO);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("data", promotion);
            return ResponseEntity.ok(response);
        } catch (DataNotFoundException e) {
            // Trả về lỗi nếu order không tồn tại
            Map<String, String> response = new HashMap<>();
            response.put("status", "failed");
            response.put("errorMessage", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            // Trả về lỗi chung nếu có lỗi khác
            Map<String, String> response = new HashMap<>();
            response.put("status", "failed");
            response.put("errorMessage", "An error occurred while deleting the order");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }




    @GetMapping("/get_all_promotions/{supplierInfoId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_PARTNER','ROLE_CUSTOMER')")
    public ResponseEntity<?> getAllPromotions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @PathVariable Long supplierInfoId,
            @RequestParam(defaultValue = "") String code) {

        // Lấy danh sách promotions từ service
        Page<Promotions> promotionsPage = promotionService.getAllPromotions(page, size, supplierInfoId, code);

        // Map từ Promotions sang PromotionResponse
        Page<PromotionResponse> promotionResponsesPage = promotionsPage.map(PromotionResponse::fromPromotion);

        return ResponseEntity.ok(promotionResponsesPage);
    }

    @PutMapping("/update_status/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_PARTNER')")
    public ResponseEntity<?> updateOfferedStatus(
            @PathVariable Long id,
            @RequestParam boolean status) {
        try {
            // Gọi service để cập nhật thời gian mở/đóng cửa
            promotionService.updateStatus(id, status);

            // Tạo đối tượng phản hồi (Response) với các thông tin chi tiết
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Cập nhật status thành công với ID: " + id);
            response.put("data", status); // Trả về dữ liệu đã cập nhật

            return ResponseEntity.ok(response);
        } catch (DataNotFoundException e) {
            // Tạo đối tượng phản hồi lỗi khi không tìm thấy thông tin
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Lỗi: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            // Tạo đối tượng phản hồi lỗi cho các lỗi khác
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Lỗi khi thay đổi trạng thái đề xuất món ăn: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/apply_promotion")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_PARTNER','ROLE_CUSTOMER')")
    public ResponseEntity<?> applyPromotion(
            @RequestParam Long foodOrderId,
            @RequestParam Long promotionId) {

        try {
            // Gọi phương thức applyPromotion trong service
             promotionService.applyPromotion(foodOrderId, promotionId);

            // Tạo đối tượng phản hồi (Response) với các thông tin chi tiết
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Áp dụng khuyến mãi thành công");


            return ResponseEntity.ok(response);
        } catch (DataNotFoundException e) {
            // Tạo đối tượng phản hồi lỗi khi không tìm thấy thông tin
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Lỗi: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            // Tạo đối tượng phản hồi lỗi cho các lỗi khác
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Lỗi khi áp dụng khuyến mãi: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
