package com.exe201.exe201be.services;

import com.exe201.exe201be.dtos.FoodItemDTO;
import com.exe201.exe201be.dtos.FoodItemOrderDTO;
import com.exe201.exe201be.dtos.SupplierDTO;
import com.exe201.exe201be.dtos.SupplierInfoDTO;
import com.exe201.exe201be.entities.*;
import com.exe201.exe201be.exceptions.DataNotFoundException;
import com.exe201.exe201be.repositories.*;
import com.exe201.exe201be.responses.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
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
                .quantitySold(0)
                .inventoryQuantity(foodItemDTO.getInventoryQuantity())
                .supplierInfo(existingSupplier)
                .description(foodItemDTO.getDescription())
                .imgUrl(foodItemDTO.getImageUrl())
                .isOffered(0)
                .status(foodItemDTO.getStatus())
                .readyTime(new Date())
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
        if(foodItemDTO.getImageUrl()!=null && !foodItemDTO.getImageUrl().equals("")) {
            existingFoodItem.setImgUrl(foodItemDTO.getImageUrl());

        }
        existingFoodItem.setInventoryQuantity(foodItemDTO.getInventoryQuantity());
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

    public void updateOfferedStatus(Long id, int isOffered) throws DataNotFoundException {
        FoodItem existingFoodItem = foodItemRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Food item cannot find with id" + id));
        if(existingFoodItem!=null){
            existingFoodItem.setIsOffered(isOffered);
            foodItemRepository.save(existingFoodItem);
        }
    }



    public FoodItem getFoodItem(Long id) throws DataNotFoundException {
        return foodItemRepository.findById(id).orElseThrow(()->new DataNotFoundException("Food item not found with id:" + id));
    }


    public Page<FoodItemResponse> getAllFoodItem(String keyword, Pageable pageable) {
        // Lấy dữ liệu phân trang từ repository
        Page<FoodItem> foodItemPage = foodItemRepository.searchFoodItemPageable(keyword, pageable);

        // Sử dụng Stream API để map từ FoodItem sang FoodItemResponse
        List<FoodItemResponse> foodItemResponseList = foodItemPage.stream()
                .map(FoodItemResponse::fromFoodItem) // Map từng FoodItem sang FoodItemResponse
                .collect(Collectors.toList());

        // Trả về Page với danh sách FoodItemResponse và các thông tin phân trang
        return new PageImpl<>(foodItemResponseList, pageable, foodItemPage.getTotalElements());
    }


    public List<FoodItemOfferedResponse> getAllFoodItemOffered(Long supplierId, int isOffered) {
        List<FoodItem> foodItemList = foodItemRepository.findBySupplierInfo_IdAndIsOffered(supplierId, isOffered);
        List<FoodItemOfferedResponse> foodItemOfferedResponseList = foodItemList.stream()
                .map(FoodItemOfferedResponse::fromFoodItem) // Map từng FoodItem sang FoodItemResponse
                .collect(Collectors.toList());
        return foodItemOfferedResponseList;
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


    public Page<SupplierWithFoodItemsResponse> getAllFoodItemGroupedBySupplier(String keyword, Pageable pageable) {
        // Lấy tất cả FoodItems liên quan đến keyword, không phân trang ở đây
        List<FoodItem> foodItemList = foodItemRepository.searchFoodItem(keyword);

        // Sử dụng Stream API để chuyển từ FoodItem sang FoodItemResponse
        List<FoodItemResponseWithSupplier> foodItemResponseList = foodItemList.stream()
                .map(FoodItemResponseWithSupplier::fromFoodItem)
                .collect(Collectors.toList());

        // Nhóm FoodItemResponse theo SupplierInfo
        Map<SupplierInfo, List<FoodItemResponseWithSupplier>> groupedBySupplier = foodItemResponseList.stream()
                .collect(Collectors.groupingBy(FoodItemResponseWithSupplier::getSupplierInfo));

        // Chuyển đổi từ Map sang List<SupplierWithFoodItemsResponse>
        List<SupplierWithFoodItemsResponse> supplierWithFoodItemsList = new ArrayList<>();
        groupedBySupplier.forEach((supplier, foodItems) -> {
            supplierWithFoodItemsList.add(new SupplierWithFoodItemsResponse(supplier, foodItems));
        });

        // Tính toán vị trí phân trang trên danh sách Supplier
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), supplierWithFoodItemsList.size());

        // Kiểm tra nếu vị trí start vượt quá danh sách, tránh lỗi phân trang
        if (start > supplierWithFoodItemsList.size()) {
            return new PageImpl<>(new ArrayList<>(), pageable, supplierWithFoodItemsList.size());
        }

        // Tạo một Page mới với SupplierWithFoodItemsResponse được phân trang
        List<SupplierWithFoodItemsResponse> pagedSupplierList = supplierWithFoodItemsList.subList(start, end);
        return new PageImpl<>(pagedSupplierList, pageable, supplierWithFoodItemsList.size());
    }

    public Page<FoodItemOrderDTO> getTopSoldFoodItems(Pageable pageable) {
        Page<FoodItem> foodItems = foodItemRepository.findTopSoldFoodItems(pageable);
        return foodItems.map(this::convertToDTO);
    }

    public Page<FoodItemSoldTopResponse> getTopFoodItems(int n, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "quantitySold"));
        Page<FoodItem> foodItemsPage = foodItemRepository.findTopSoldFoodItems(pageable);

        return foodItemsPage.map(this::convertToResponse);
    }

    // Chuyển đổi từ FoodItem sang FoodItemResponse và tính percentageSold
    private FoodItemSoldTopResponse convertToResponse(FoodItem foodItem) {
        FoodItemSoldTopResponse response = new FoodItemSoldTopResponse();
        response.setId(foodItem.getId());
        response.setFoodName(foodItem.getFoodName());
        response.setQuantitySold(foodItem.getQuantitySold());
        response.setInventoryQuantity(foodItem.getInventoryQuantity());
        response.setPercentageSold((foodItem.getQuantitySold() / (double) foodItem.getInventoryQuantity()) * 100);
        return response;
    }

    private FoodItemOrderDTO convertToDTO(FoodItem foodItem) {
        FoodItemOrderDTO dto = new FoodItemOrderDTO();
        dto.setFoodId(foodItem.getId());
        dto.setName(foodItem.getFoodName());
        dto.setQuantitySold(foodItem.getQuantitySold()); // Sử dụng QuantitySold

        // Lấy thông tin supplier và chuyển đổi sang SupplierDTO
        SupplierInfo supplier = foodItem.getSupplierInfo();
        if (supplier != null) {
            SupplierDTO supplierDTO = new SupplierDTO();
            supplierDTO.setId(supplier.getId());
            supplierDTO.setAddress(supplier.getAddress());
            supplierDTO.setDescription(supplier.getDescription());
            supplierDTO.setRestaurantName(supplier.getRestaurantName());
            supplierDTO.setImgUrl(supplier.getImgUrl());
            supplierDTO.setOpenTime(supplier.getOpenTime());
            supplierDTO.setCloseTime(supplier.getCloseTime());
            supplierDTO.setTotalReviewCount(supplier.getTotalReviewCount());
            supplierDTO.setTotalStarRating(supplier.getTotalStarRating());
            // Thêm các thuộc tính khác nếu cần
            dto.setSupplierInfo(supplierDTO); // Đặt supplierInfo
        }

        return dto;
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
