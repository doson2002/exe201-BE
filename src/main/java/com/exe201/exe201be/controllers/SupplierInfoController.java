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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
            SupplierInfo updateSupplier = supplierInfoService.updateSupplier(id, supplierInfoDTO);
            return ResponseEntity.ok(updateSupplier);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
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


    @GetMapping("/get_all_suppliers")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_PARTNER')")
    public ResponseEntity<SupplierInfoResponseList> getAllProducts(
            @RequestParam(defaultValue = "")String keyword,
            @RequestParam("page") int page, @RequestParam("limit")int limit){
        PageRequest pageRequest = PageRequest.of(page, limit);
        Page<SupplierInfoResponse> supplierInfoResponsePage = supplierInfoService.getAllSuppliers(keyword, pageRequest);
        int totalPages = supplierInfoResponsePage.getTotalPages();
        List<SupplierInfoResponse> supplierResponseList = supplierInfoResponsePage.getContent();
        return ResponseEntity.ok(SupplierInfoResponseList.builder()
                .suppliers(supplierResponseList)
                .totalPages(totalPages)
                .build());
    }
    @GetMapping("/get_supplier_by_id/{supplierId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_PARTNER')")
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



    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_PARTNER','ROLE_CUSTOMER')")
    @GetMapping("/get_supplier_by_supplier_type_id/{supplierTypeId}")
    public ResponseEntity<?> getSupplierBySupplierTypeId(@PathVariable Long supplierTypeId) {
        List<SupplierInfo> supplierInfoList = supplierInfoService.getSupplierInfoBySupplierTypeId(supplierTypeId);
        List<SupplierInfoResponse> supplierInfoResponses = supplierInfoList.stream()
                .map(SupplierInfoResponse::fromSupplierInfo)
                .collect(Collectors.toList());
        return ResponseEntity.ok(supplierInfoResponses);
    }
}
