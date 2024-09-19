package com.exe201.exe201be.controllers;

import com.exe201.exe201be.dtos.FoodItemDTO;
import com.exe201.exe201be.dtos.SupplierInfoDTO;
import com.exe201.exe201be.entities.FoodItem;
import com.exe201.exe201be.entities.SupplierInfo;
import com.exe201.exe201be.exceptions.DataNotFoundException;
import com.exe201.exe201be.responses.FoodItemResponse;
import com.exe201.exe201be.responses.FoodItemResponseList;
import com.exe201.exe201be.responses.SupplierInfoResponse;
import com.exe201.exe201be.responses.SupplierInfoResponseList;
import com.exe201.exe201be.services.IFoodItemService;
import com.exe201.exe201be.services.ISupplierInfoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
            FoodItem updateFoodItem = foodItemService.updateFoodItem(id, foodItemDTO);
            return ResponseEntity.ok(updateFoodItem);
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

    @GetMapping("/get_all_food_items")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_PARTNER')")
    public ResponseEntity<FoodItemResponseList> getAllFoodItems(
            @RequestParam(defaultValue = "")String keyword){
        List<FoodItemResponse> foodItemResponseList = foodItemService.getAllFoodItem(keyword);
        return ResponseEntity.ok(FoodItemResponseList.builder()
                .foodItems(foodItemResponseList)
                .build());
    }
    @GetMapping("/get_food_item_by_id/{foodItemId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_PARTNER')")
    public ResponseEntity<?> getFoodItem(@Valid @PathVariable Long foodItemId) throws DataNotFoundException {
        FoodItem foodItem = foodItemService.getFoodItem(foodItemId);
        return ResponseEntity.ok(FoodItemResponse.fromFoodItem(foodItem));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_PARTNER')")
    @GetMapping("/get_food_item_by_supplier/{supplierId}")
    public ResponseEntity<?> getFoodItemBySupplierId(@PathVariable Long supplierId) {
        List<FoodItem> foodItemList = foodItemService.getFoodItemBySupplierId(supplierId);
        List<FoodItemResponse> foodItemResponseList = foodItemList.stream()
                .map(FoodItemResponse::fromFoodItem)
                .collect(Collectors.toList());
        return ResponseEntity.ok(foodItemResponseList);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_PARTNER')")
    @GetMapping("/get_food_item_by_food_type/{foodTypeId}")
    public ResponseEntity<?> getFoodItemByFoodTypeId(@PathVariable Long foodTypeId) {
        List<FoodItem> foodItemList = foodItemService.getFoodItemByFoodTypeId(foodTypeId);
        List<FoodItemResponse> foodItemResponseList = foodItemList.stream()
                .map(FoodItemResponse::fromFoodItem)
                .collect(Collectors.toList());
        return ResponseEntity.ok(foodItemResponseList);
    }

}
