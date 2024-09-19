package com.exe201.exe201be.controllers;

import com.exe201.exe201be.entities.FoodItem;
import com.exe201.exe201be.entities.FoodType;
import com.exe201.exe201be.responses.FoodItemResponse;
import com.exe201.exe201be.responses.FoodItemResponseList;
import com.exe201.exe201be.services.IFoodTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/food_types")
@RequiredArgsConstructor
public class FoodTypeController {

    private final IFoodTypeService foodTypeService;
    @GetMapping("/get_all_food_types")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_PARTNER')")
    public ResponseEntity<?> getAllFoodItems(){
        List<FoodType> foodTypes = foodTypeService.getAllFoodItem();
        return ResponseEntity.ok(foodTypes);
    }
}
