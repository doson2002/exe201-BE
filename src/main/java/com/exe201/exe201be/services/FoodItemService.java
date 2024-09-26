package com.exe201.exe201be.services;

import com.exe201.exe201be.dtos.FoodItemDTO;
import com.exe201.exe201be.dtos.SupplierInfoDTO;
import com.exe201.exe201be.entities.*;
import com.exe201.exe201be.exceptions.DataNotFoundException;
import com.exe201.exe201be.repositories.*;
import com.exe201.exe201be.responses.FoodItemResponse;
import com.exe201.exe201be.responses.FoodItemResponseWithSupplier;
import com.exe201.exe201be.responses.SupplierInfoResponse;
import com.exe201.exe201be.responses.SupplierWithFoodItemsResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FoodItemService implements IFoodItemService{
    private final FoodItemRepository foodItemRepository;
    private final SupplierInfoRepository supplierInfoRepository;
    private final UserRepository userRepository;
    private final FoodTypeRepository foodTypeRepository;
    private final FoodItemTypeRepository foodItemTypeRepository;
    public FoodItem createFoodItem(FoodItemDTO foodItemDTO) throws DataNotFoundException {
        SupplierInfo existingSupplier = supplierInfoRepository.findById(foodItemDTO.getSupplierId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find supplier id" + foodItemDTO.getSupplierId()));

        FoodItem newFoodItem = FoodItem
                .builder()
                .foodName(foodItemDTO.getFoodName())
                .price(foodItemDTO.getPrice())
                .quantitySold(foodItemDTO.getQuantitySold())
                .supplierInfo(existingSupplier)
                .description(foodItemDTO.getDescription())
                .imgUrl(foodItemDTO.getImageUrl())
                .status(foodItemDTO.getStatus())
                .readyTime(foodItemDTO.getReadyTime())
                .category(foodItemDTO.getCategory())
                .build();

        // Lưu FoodItem trước để tạo ID
        FoodItem savedFoodItem = foodItemRepository.save(newFoodItem);
        // Xử lý danh sách foodTypeIds để liên kết với FoodType
        if (foodItemDTO.getFoodTypeIds() != null && !foodItemDTO.getFoodTypeIds().isEmpty()) {
            for (Long foodTypeId : foodItemDTO.getFoodTypeIds()) {
                FoodType foodType = foodTypeRepository.findById(foodTypeId)
                        .orElseThrow(() -> new DataNotFoundException("Cannot find food type id " + foodTypeId));

                // Tạo bản ghi FoodItemType
                FoodItemType foodItemType = FoodItemType.builder()
                        .foodItem(savedFoodItem)
                        .foodType(foodType)
                        .build();

                // Lưu FoodItemType
                foodItemTypeRepository.save(foodItemType);
            }

        }
        return savedFoodItem;
    }

    @Transactional
    public void deleteExistingFoodItemTypes(FoodItem foodItem) {
        foodItemTypeRepository.deleteByFoodItem(foodItem);
    }

    @Transactional
    public void addNewFoodItemTypes(FoodItem foodItem, List<Long> foodTypeIds) throws DataNotFoundException {
        for (Long foodTypeId : foodTypeIds) {
            FoodType foodType = foodTypeRepository.findById(foodTypeId)
                    .orElseThrow(() -> new DataNotFoundException("Cannot find food type id " + foodTypeId));

            FoodItemType foodItemType = FoodItemType.builder()
                    .foodItem(foodItem)
                    .foodType(foodType)
                    .build();

            foodItemTypeRepository.save(foodItemType);
        }
    }

    @Transactional
    @Override
    // Trong hàm updateFoodItem, tách biệt hai quá trình trên
    public FoodItem updateFoodItem(Long id, FoodItemDTO foodItemDTO) throws DataNotFoundException {
        FoodItem existingFoodItem = foodItemRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Food item cannot find with id" + id));

        existingFoodItem.setFoodName(foodItemDTO.getFoodName());
        existingFoodItem.setPrice(foodItemDTO.getPrice());
        existingFoodItem.setQuantitySold(foodItemDTO.getQuantitySold());
        existingFoodItem.setImgUrl(foodItemDTO.getImageUrl());
        existingFoodItem.setDescription(foodItemDTO.getDescription());
        existingFoodItem.setStatus(foodItemDTO.getStatus());
//        existingFoodItem.setReadyTime(foodItemDTO.getReadyTime());
        existingFoodItem.setModifiedDate(new Date());
        existingFoodItem.setCategory(foodItemDTO.getCategory());

        // Xóa các bản ghi FoodItemType hiện tại liên quan đến FoodItem
        deleteExistingFoodItemTypes(existingFoodItem);

        // Thêm các liên kết mới dựa trên foodTypeIds từ DTO
        addNewFoodItemTypes(existingFoodItem, foodItemDTO.getFoodTypeIds());

        return foodItemRepository.save(existingFoodItem);
    }



    public FoodItem getFoodItem(Long id) throws DataNotFoundException {
        return foodItemRepository.findById(id).orElseThrow(()->new DataNotFoundException("Food item not found with id:" + id));
    }


    public List<FoodItemResponse> getAllFoodItem(String keyword) {
        List<FoodItem> foodItemList = foodItemRepository.searchFoodItem(keyword);
        // Sử dụng Stream API để map từ FoodItem sang FoodItemResponse
        List<FoodItemResponse> foodItemResponseList = foodItemList.stream()
                .map(FoodItemResponse::fromFoodItem) // Map từng FoodItem sang FoodItemResponse
                .collect(Collectors.toList());

        return foodItemResponseList;
    }
    public List<FoodItem> getFoodItemBySupplierId(Long supplierId){
        return foodItemRepository.findBySupplierInfo_Id(supplierId);
    }


    public List<FoodItem> getFoodItemByFoodTypeId(Long foodTypeId) {
        // Tìm danh sách FoodItemType liên kết với foodTypeId
        List<FoodItemType> foodItemTypes = foodItemTypeRepository.findByFoodTypeId(foodTypeId);

        // Lấy danh sách FoodItem từ FoodItemType
        List<FoodItem> foodItems = foodItemTypes.stream()
                .map(FoodItemType::getFoodItem)
                .collect(Collectors.toList());

        return foodItems;
    }

    public List<SupplierWithFoodItemsResponse> getAllFoodItemGroupedBySupplier(String keyword) {
        List<FoodItem> foodItemList = foodItemRepository.searchFoodItem(keyword);

        // Sử dụng Stream API để chuyển từ FoodItem sang FoodItemResponse
        List<FoodItemResponseWithSupplier> foodItemResponseList = foodItemList.stream()
                .map(FoodItemResponseWithSupplier::fromFoodItem)
                .collect(Collectors.toList());

        // Nhóm FoodItemResponse theo SupplierInfo
        Map<SupplierInfo, List<FoodItemResponseWithSupplier>> groupedBySupplier = foodItemResponseList.stream()
                .collect(Collectors.groupingBy(FoodItemResponseWithSupplier::getSupplierInfo));

        // Chuyển đổi từ Map sang List<SupplierWithFoodItems>
        List<SupplierWithFoodItemsResponse> supplierWithFoodItemsList = new ArrayList<>();
        groupedBySupplier.forEach((supplier, foodItems) -> {
            supplierWithFoodItemsList.add(new SupplierWithFoodItemsResponse(supplier, foodItems));
        });

        return supplierWithFoodItemsList;
    }

    public List<String> getAllFoodItemNames(String keyword) {
        return foodItemRepository.findAllFoodItemNames(keyword);
    }

    @Override
    @Transactional
    public void deleteFoodItem(Long foodItemId) {
        foodItemRepository.deleteById(foodItemId);
    }

}
