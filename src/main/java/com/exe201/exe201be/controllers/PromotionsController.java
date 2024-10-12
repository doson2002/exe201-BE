package com.exe201.exe201be.controllers;

import com.exe201.exe201be.dtos.PromotionsDTO;
import com.exe201.exe201be.entities.Promotions;
import com.exe201.exe201be.exceptions.DataNotFoundException;
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
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_PARTNER')")
    public ResponseEntity<?> getAllPromotions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @PathVariable Long supplierInfoId,
            @RequestParam(defaultValue = "") String code) {

        Page<Promotions> promotionsPage = promotionService.getAllPromotions(page, size, supplierInfoId, code);
        return ResponseEntity.ok(promotionsPage);
    }
}
