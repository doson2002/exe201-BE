package com.exe201.exe201be.services;

import com.exe201.exe201be.dtos.FoodItemDTO;
import com.exe201.exe201be.entities.FoodItem;
import com.exe201.exe201be.exceptions.DataNotFoundException;
import com.exe201.exe201be.responses.FoodItemResponse;
import com.exe201.exe201be.responses.SupplierWithFoodItemsResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface IFoodItemService {

    FoodItem createFoodItem(FoodItemDTO foodItemDTO) throws DataNotFoundException;
    FoodItem updateFoodItem(Long id, FoodItemDTO foodItemDTO) throws DataNotFoundException;

    FoodItem getFoodItem(Long id) throws DataNotFoundException;
    List<FoodItemResponse> getAllFoodItem(String keyword);

    void deleteFoodItem(Long foodItemId);
    List<FoodItem> getFoodItemBySupplierId(Long supplierId);

    List<FoodItem> getFoodItemByFoodTypeId(Long foodTypeId);
    List<SupplierWithFoodItemsResponse> getAllFoodItemGroupedBySupplier(String keyword);

    List<String> getAllFoodItemNames(String keyword);
}
