package com.exe201.exe201be.controllers;

import com.exe201.exe201be.dtos.FoodItemDTO;
import com.exe201.exe201be.dtos.FoodTypeDTO;
import com.exe201.exe201be.entities.FoodItem;
import com.exe201.exe201be.entities.FoodType;
import com.exe201.exe201be.responses.FoodItemResponse;
import com.exe201.exe201be.responses.FoodItemResponseList;
import com.exe201.exe201be.services.IFoodTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/food_types")
@RequiredArgsConstructor
public class FoodTypeController {

    private final IFoodTypeService foodTypeService;

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_PARTNER')")
    public ResponseEntity<?> createFoodType(
            @Valid @RequestBody FoodTypeDTO foodTypeDTO,
            BindingResult result){
        try{
            if(result.hasErrors()){
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            FoodType newFoodType = foodTypeService.createFoodType(foodTypeDTO);

            return ResponseEntity.ok(newFoodType);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/get_all_food_types")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_PARTNER','ROLE_CUSTOMER')")
    public ResponseEntity<?> getAllFoodItems(){
        List<FoodType> foodTypes = foodTypeService.getAllFoodItem();
        return ResponseEntity.ok(foodTypes);
    }
    @GetMapping("/get_food_type_by_supplier_id/{supplierId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_PARTNER','ROLE_CUSTOMER')")
    public ResponseEntity<?> getFoodTypeBySupplierId(@PathVariable Long supplierId){
        List<FoodType> foodTypes = foodTypeService.getFoodTypeBySupplierInfoId(supplierId);
        return ResponseEntity.ok(foodTypes);
    }
}
