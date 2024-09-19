package com.exe201.exe201be.repositories;

import com.exe201.exe201be.entities.FoodOrderItem;
import com.exe201.exe201be.entities.FoodType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodTypeRepository extends JpaRepository<FoodType, Long> {
}
