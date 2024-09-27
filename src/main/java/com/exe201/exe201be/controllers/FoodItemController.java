package com.exe201.exe201be.controllers;

import com.exe201.exe201be.dtos.FoodItemDTO;
import com.exe201.exe201be.dtos.SupplierInfoDTO;
import com.exe201.exe201be.dtos.UpdateTimeRequestDTO;
import com.exe201.exe201be.entities.FoodItem;
import com.exe201.exe201be.entities.SupplierInfo;
import com.exe201.exe201be.exceptions.DataNotFoundException;
import com.exe201.exe201be.responses.*;
import com.exe201.exe201be.services.IFoodItemService;
import com.exe201.exe201be.services.ISupplierInfoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
@RequestMapping("${api.prefix}/food_item")
@RequiredArgsConstructor
public class FoodItemController {
    private final IFoodItemService foodItemService;

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_PARTNER')")
    public ResponseEntity<?> createFoodItem(
            @Valid @RequestBody FoodItemDTO foodItemDTO,
            BindingResult result){
        try{
            if(result.hasErrors()){
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            FoodItem newFoodItem = foodItemService.createFoodItem(foodItemDTO);

            return ResponseEntity.ok(newFoodItem);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_PARTNER')")
    public ResponseEntity<?> updateFoodItem(@PathVariable Long id,
                                            @Valid @RequestBody FoodItemDTO foodItemDTO){
        try{
            FoodItem updatedFoodItem = foodItemService.updateFoodItem(id, foodItemDTO);
            UpdateFoodItemResponse response = UpdateFoodItemResponse.fromUpdateFoodItem(updatedFoodItem);
            return ResponseEntity.ok(response);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
//    @GetMapping("/get_all_food_items")
//    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_PARTNER')")
//    public ResponseEntity<FoodItemResponseList> getAllFoodItems(
//            @RequestParam(defaultValue = "")String keyword,
//            @RequestParam("page") int page, @RequestParam("limit")int limit){
//        PageRequest pageRequest = PageRequest.of(page, limit);
//        Page<FoodItemResponse> foodItemResponseListPage = foodItemService.getAllFoodItem(keyword, pageRequest);
//        int totalPages = foodItemResponseListPage.getTotalPages();
//        List<FoodItemResponse> foodItemResponseList = foodItemResponseListPage.getContent();
//        return ResponseEntity.ok(FoodItemResponseList.builder()
//                .foodItems(foodItemResponseList)
//                .totalPages(totalPages)
//                .build());
//    }

    @PutMapping("/update_offered_status/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_PARTNER')")
    public ResponseEntity<?> updateOfferedStatus(
            @PathVariable Long id,
             @RequestParam int isOffered) {
        try {
            // Gọi service để cập nhật thời gian mở/đóng cửa
            foodItemService.updateOfferedStatus(id, isOffered);

            // Tạo đối tượng phản hồi (Response) với các thông tin chi tiết
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Cập nhật offered status thành công với ID: " + id);
            response.put("data", isOffered); // Trả về dữ liệu đã cập nhật

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

    @GetMapping("/get_all_food_items")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_PARTNER','ROLE_CUSTOMER')")
    public ResponseEntity<FoodItemResponseList> getAllFoodItems(
            @RequestParam(defaultValue = "")String keyword){
        List<FoodItemResponse> foodItemResponseList = foodItemService.getAllFoodItem(keyword);
        return ResponseEntity.ok(FoodItemResponseList.builder()
                .foodItems(foodItemResponseList)
                .build());
    }
    @GetMapping("/get_food_items_grouped_by_supplierId")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_PARTNER','ROLE_CUSTOMER')")
    public ResponseEntity<?> getAllFoodItemsGroupedBySupplierId(
            @RequestParam(defaultValue = "") String keyword) {

        // Gọi service để lấy danh sách nhóm theo Supplier
        List<SupplierWithFoodItemsResponse> supplierWithFoodItemsList = foodItemService.getAllFoodItemGroupedBySupplier(keyword);

        return ResponseEntity.ok(supplierWithFoodItemsList);
    }

    @GetMapping("/get_food_items_by_offered_status/{supplierId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_PARTNER','ROLE_CUSTOMER')")
    public ResponseEntity<?> getAllFoodItemsByOfferedStatus(
            @Valid @PathVariable Long supplierId,
            @RequestParam int isOffered) {

        List<FoodItemOfferedResponse> foodItemOfferedResponseList = foodItemService.getAllFoodItemOffered(supplierId, isOffered);

        return ResponseEntity.ok(foodItemOfferedResponseList);
    }

    @GetMapping("/get_food_item_by_id/{foodItemId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_PARTNER')")
    public ResponseEntity<?> getFoodItem(@Valid @PathVariable Long foodItemId) throws DataNotFoundException {
        FoodItem foodItem = foodItemService.getFoodItem(foodItemId);
        return ResponseEntity.ok(FoodItemResponse.fromFoodItem(foodItem));
    }
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_PARTNER','ROLE_CUSTOMER')")
    @GetMapping("/get_food_item_by_supplier/{supplierId}")
    public ResponseEntity<?> getFoodItemBySupplierId(@PathVariable Long supplierId) {
        List<FoodItem> foodItemList = foodItemService.getFoodItemBySupplierId(supplierId);
        List<FoodItemResponse> foodItemResponseList = foodItemList.stream()
                .map(FoodItemResponse::fromFoodItem)
                .collect(Collectors.toList());
        return ResponseEntity.ok(foodItemResponseList);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_PARTNER','ROLE_CUSTOMER')")
    @GetMapping("/get_food_item_by_food_type/{foodTypeId}")
    public ResponseEntity<?> getFoodItemByFoodTypeId(@PathVariable Long foodTypeId) {
        List<FoodItem> foodItemList = foodItemService.getFoodItemByFoodTypeId(foodTypeId);
        List<FoodItemResponse> foodItemResponseList = foodItemList.stream()
                .map(FoodItemResponse::fromFoodItem)
                .collect(Collectors.toList());
        return ResponseEntity.ok(foodItemResponseList);
    }

    @GetMapping("/get_all_food_item_names")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_PARTNER','ROLE_CUSTOMER')")
    public ResponseEntity<List<String>> getAllFoodItemNames(@RequestParam String keyword) {
        List<String> foodItemNames = foodItemService.getAllFoodItemNames(keyword);
        return ResponseEntity.ok(foodItemNames);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_PARTNER')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteFoodItem(@PathVariable("id") Long foodItemId) {
        try {
            // Gọi Service để xóa FoodItem
            foodItemService.deleteFoodItem(foodItemId);
            // Trả về phản hồi thành công
            return ResponseEntity.ok("Xóa món ăn thành công với ID: " + foodItemId);
        } catch (Exception e) {
            // Xử lý lỗi khi xóa không thành công
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi xóa món ăn: " + e.getMessage());
        }
    }

}
