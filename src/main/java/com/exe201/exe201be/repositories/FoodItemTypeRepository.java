package com.exe201.exe201be.repositories;

import com.exe201.exe201be.entities.FoodItem;
import com.exe201.exe201be.entities.FoodItemType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodItemTypeRepository extends JpaRepository<FoodItemType, Long> {
    void deleteByFoodItem(FoodItem foodItem);  // Xóa tất cả các bản ghi liên quan đến FoodItem
    List<FoodItemType> findByFoodTypeId(Long foodTypeId);
}