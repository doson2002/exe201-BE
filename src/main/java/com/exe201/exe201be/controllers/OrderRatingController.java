package com.exe201.exe201be.controllers;

import com.exe201.exe201be.dtos.OrderRatingDTO;
import com.exe201.exe201be.entities.FoodType;
import com.exe201.exe201be.entities.OrderRating;
import com.exe201.exe201be.entities.SupplierInfo;
import com.exe201.exe201be.exceptions.DataNotFoundException;
import com.exe201.exe201be.responses.OrderRatingResponse;
import com.exe201.exe201be.responses.SupplierInfoResponse;
import com.exe201.exe201be.responses.SupplierRatingResponse;
import com.exe201.exe201be.services.IOrderRatingService;
import com.exe201.exe201be.services.OrderRatingService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.prefix}/order_ratings")
@RequiredArgsConstructor
public class OrderRatingController {
    private final IOrderRatingService orderRatingService;

    @PostMapping("/add_rating")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_CUSTOMER')")
    public ResponseEntity<OrderRating> addRating(@RequestBody OrderRatingDTO orderRatingDTO) throws DataNotFoundException {
        OrderRating savedRating = orderRatingService.addRating(orderRatingDTO);
        return ResponseEntity.ok(savedRating);
    }

    @GetMapping("/get_rating_by_supplier_id/{supplierId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_PARTNER','ROLE_CUSTOMER')")
    public ResponseEntity<?> getRatingBySupplierId(@PathVariable Long supplierId){
        List<OrderRating> orderRatingList = orderRatingService.getRatingBySupplierId(supplierId);
        List<OrderRatingResponse> orderRatingResponseList = orderRatingList.stream()
                .map(OrderRatingResponse::fromSupplierInfo)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orderRatingResponseList);
    }
    @GetMapping("/get_rating_detail/{supplierId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_PARTNER','ROLE_CUSTOMER')")
    public ResponseEntity<SupplierRatingResponse> getSupplierRatings(@PathVariable Long supplierId) throws DataNotFoundException {
        // Gọi service để lấy thông tin về đánh giá
        SupplierRatingResponse response = orderRatingService.getSupplierRatings(supplierId);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/get_all_messages_by_stars/{supplierId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_PARTNER','ROLE_CUSTOMER')")
    public ResponseEntity<Map<Integer, List<String>>> getAllMessagesByStars(
            @PathVariable Long supplierId) throws DataNotFoundException {
        // Gọi service để lấy danh sách các response_message phân loại theo số sao
        Map<Integer, List<String>> responseMessagesByStars = orderRatingService.getAllResponseMessagesByStars(supplierId);

        // Trả về response
        return ResponseEntity.ok(responseMessagesByStars);
    }
}
