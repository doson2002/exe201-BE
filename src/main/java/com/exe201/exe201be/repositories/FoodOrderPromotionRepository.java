package com.exe201.exe201be.repositories;

import com.exe201.exe201be.entities.FoodOrder;
import com.exe201.exe201be.entities.FoodOrderPromotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodOrderPromotionRepository extends JpaRepository<FoodOrderPromotion, Long> {

}
