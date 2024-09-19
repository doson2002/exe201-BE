package com.exe201.exe201be.repositories;

import com.exe201.exe201be.entities.FoodOrder;
import com.exe201.exe201be.entities.FoodOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodOrderRepository extends JpaRepository<FoodOrder, Long> {

    List<FoodOrder> findByUser_Id(Long userId);

}
