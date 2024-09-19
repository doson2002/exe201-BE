package com.exe201.exe201be.repositories;

import com.exe201.exe201be.entities.FoodItem;
import com.exe201.exe201be.entities.FoodOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodOrderItemRepository extends JpaRepository<FoodOrderItem, Long> {

    List<FoodOrderItem> findByFoodOrderId(Long orderId);
}
