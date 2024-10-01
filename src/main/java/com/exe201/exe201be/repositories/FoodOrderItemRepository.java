package com.exe201.exe201be.repositories;

import com.exe201.exe201be.entities.FoodItem;
import com.exe201.exe201be.entities.FoodOrderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodOrderItemRepository extends JpaRepository<FoodOrderItem, Long> {

    List<FoodOrderItem> findByFoodOrderId(Long orderId);

    @Query("SELECT foi.foodItem, SUM(foi.quantity) AS totalQuantity " +
            "FROM FoodOrderItem foi " +
            "GROUP BY foi.foodItem " +
            "ORDER BY totalQuantity DESC")
    Page<Object[]> findTopOrderedFoodItems(Pageable pageable);
}
