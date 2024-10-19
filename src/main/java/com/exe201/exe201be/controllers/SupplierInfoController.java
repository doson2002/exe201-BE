package com.exe201.exe201be.controllers;

import com.exe201.exe201be.dtos.SupplierInfoDTO;
import com.exe201.exe201be.dtos.UpdateTimeRequestDTO;
import com.exe201.exe201be.entities.FoodItem;
import com.exe201.exe201be.entities.SupplierInfo;
import com.exe201.exe201be.exceptions.DataNotFoundException;
import com.exe201.exe201be.responses.FoodItemResponse;
import com.exe201.exe201be.responses.SupplierInfoResponse;
import com.exe201.exe201be.responses.SupplierInfoResponseList;
import com.exe201.exe201be.services.ISupplierInfoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.prefix}/supplier_info")
@SecurityRequirement(name = "bearer-key")
@RequiredArgsConstructor
public class SupplierInfoController {
    private final ISupplierInfoService supplierInfoService;

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_PARTNER')")
    public ResponseEntity<?> createSupplierInfo(
            @Valid @RequestBody SupplierInfoDTO supplierInfoDTO,
            BindingResult result){
        try{
            if(result.hasErrors()){
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            SupplierInfo newSupplier = supplierInfoService.createSupplierInfo(supplierInfoDTO);

            return ResponseEntity.ok(newSupplier);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_PARTNER')")
    public ResponseEntity<?> updateSupplier(@PathVariable Long id,
                                           @Valid @RequestBody SupplierInfoDTO supplierInfoDTO){
        try{
            supplierInfoService.updateSupplier(id, supplierInfoDTO);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Cập nhật SupplierInfo thành công với ID: " + id);
            response.put("data", supplierInfoDTO); // Trả về dữ liệu đã cập nhật
            return ResponseEntity.ok(response);
        }catch(DataNotFoundException e){
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Lỗi: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }catch (Exception e){
            // Tạo đối tượng phản hồi lỗi cho các lỗi khác
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Lỗi khi cập nhật SupplierInfo: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PutMapping("/update_time/{supplierId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_PARTNER')")
    public ResponseEntity<?> updateTimeOfRestaurant(
            @PathVariable Long supplierId,
            @Valid @RequestBody UpdateTimeRequestDTO request) {
        try {
            // Gọi service để cập nhật thời gian mở/đóng cửa
            supplierInfoService.updateTimeOfRestaurant(supplierId, request.getOpenTime(), request.getCloseTime());

            // Tạo đối tượng phản hồi (Response) với các thông tin chi tiết
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Cập nhật thời gian mở/đóng cửa thành công cho nhà hàng với ID: " + supplierId);
            response.put("data", request); // Trả về dữ liệu đã cập nhật

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
            errorResponse.put("message", "Lỗi khi cập nhật thời gian: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // Phương thức cập nhật tọa độ cho nhà cung cấp
    @PutMapping("/update_location/{supplierId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_PARTNER')")
    public ResponseEntity<Map<String, String>> updateSupplierLocation(
            @PathVariable Long supplierId,
            @RequestParam double latitude,
            @RequestParam double longitude) {
        try {
            supplierInfoService.updateLocation(supplierId, latitude, longitude);

            // Trả về JSON với status thành công
            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Supplier location updated successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Trả về lỗi nếu có
            Map<String, String> response = new HashMap<>();
            response.put("status", "failed");
            response.put("errorMessage", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/get_all_suppliers")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_PARTNER','ROLE_CUSTOMER')")
    public ResponseEntity<SupplierInfoResponseList> getAllProducts(
            @RequestParam(defaultValue = "")String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,  // Đặt sortBy mặc định là "id"
            @RequestParam(defaultValue = "asc") String sortOrder) { // Tham số sortOrder vẫn giữ nguyên)
        Sort sort = sortOrder.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        PageRequest pageRequest = PageRequest.of(page, size,sort);
        Page<SupplierInfoResponse> supplierInfoResponsePage = supplierInfoService.getAllSuppliers(keyword, pageRequest);
        int totalPages = supplierInfoResponsePage.getTotalPages();
        List<SupplierInfoResponse> supplierResponseList = supplierInfoResponsePage.getContent();
        return ResponseEntity.ok(SupplierInfoResponseList.builder()
                .suppliers(supplierResponseList)
                .totalPages(totalPages)
                .build());
    }
    @GetMapping("/get_supplier_by_id/{supplierId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_PARTNER','ROLE_CUSTOMER')")
    public ResponseEntity<?> getProduct(@Valid @PathVariable Long supplierId) throws DataNotFoundException {
        SupplierInfo supplier = supplierInfoService.getSupplierInfo(supplierId);
        return ResponseEntity.ok(SupplierInfoResponse.fromSupplierInfo(supplier));
    }

    @GetMapping("/get_supplier_by_user_id/{userId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_PARTNER','ROLE_CUSTOMER')")
    public ResponseEntity<?> getSupplierByUserId(@Valid @PathVariable Long userId) throws DataNotFoundException {
        SupplierInfo supplier = supplierInfoService.getSupplierByUserId(userId);
        return ResponseEntity.ok(SupplierInfoResponse.fromSupplierInfo(supplier));
    }

    @GetMapping("/top")
    public ResponseEntity<Page<Map<String, Object>>> getTopSuppliers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Map<String, Object>> result = supplierInfoService.getTopSuppliers(pageable);
        return ResponseEntity.ok(result);
    }



    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_PARTNER','ROLE_CUSTOMER')")
    @GetMapping("/get_supplier_by_supplier_type_id/{supplierTypeId}")
    public ResponseEntity<?> getSupplierBySupplierTypeId(@PathVariable Long supplierTypeId) {
        List<SupplierInfo> supplierInfoList = supplierInfoService.getSupplierInfoBySupplierTypeId(supplierTypeId);
        List<SupplierInfoResponse> supplierInfoResponses = supplierInfoList.stream()
                .map(SupplierInfoResponse::fromSupplierInfo)
                .collect(Collectors.toList());
        return ResponseEntity.ok(supplierInfoResponses);
    }

    @PutMapping("/block/{supplierId}/{status}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> blockOrEnable(
            @Valid @PathVariable long supplierId,
            @Valid @PathVariable int status
    ) {
        try {
            supplierInfoService.blockOrEnable(supplierId, status);

            // Trả về JSON với status thành công
            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            String message = status > 0 ? "Successfully enabled the user." : "Successfully blocked the user.";
            response.put("message", message);
            return ResponseEntity.ok(response);
        } catch (DataNotFoundException e) {
            // Trả về lỗi nếu không tìm thấy người dùng
            Map<String, String> response = new HashMap<>();
            response.put("status", "failed");
            response.put("errorMessage", "User not found.");
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            // Trả về lỗi nếu có
            Map<String, String> response = new HashMap<>();
            response.put("status", "failed");
            response.put("errorMessage", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
