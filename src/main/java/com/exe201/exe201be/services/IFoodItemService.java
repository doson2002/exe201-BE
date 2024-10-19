package com.exe201.exe201be.services;

import com.exe201.exe201be.dtos.FoodItemDTO;
import com.exe201.exe201be.dtos.FoodItemOrderDTO;
import com.exe201.exe201be.entities.FoodItem;
import com.exe201.exe201be.exceptions.DataNotFoundException;
import com.exe201.exe201be.responses.FoodItemOfferedResponse;
import com.exe201.exe201be.responses.FoodItemResponse;
import com.exe201.exe201be.responses.FoodItemSoldTopResponse;
import com.exe201.exe201be.responses.SupplierWithFoodItemsResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IFoodItemService {

    FoodItem createFoodItem(FoodItemDTO foodItemDTO) throws DataNotFoundException;
    FoodItem updateFoodItem(Long id, FoodItemDTO foodItemDTO) throws DataNotFoundException;

    FoodItem getFoodItem(Long id) throws DataNotFoundException;
    Page<FoodItemResponse> getAllFoodItem(String keyword, Pageable pageable);

    void deleteFoodItem(Long foodItemId);
    List<FoodItem> getFoodItemBySupplierId(Long supplierId);

    List<FoodItem> getFoodItemByFoodTypeId(Long foodTypeId, String keyword);
    Page<SupplierWithFoodItemsResponse> getAllFoodItemGroupedBySupplier(String keyword, Pageable pageable);

    List<String> getAllFoodItemNames(String keyword);

    void updateOfferedStatus(Long id, int isOffered) throws DataNotFoundException;
    List<FoodItemOfferedResponse> getAllFoodItemOffered(Long supplierId, int isOffered);

    Page<FoodItemOrderDTO> getTopSoldFoodItems(Pageable pageable);

    Page<FoodItemSoldTopResponse> getTopFoodItems(int n, int page, int size);
}
