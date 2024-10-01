package com.exe201.exe201be.repositories;

import com.exe201.exe201be.entities.FoodItem;
import com.exe201.exe201be.entities.ForgotPassword;
import com.exe201.exe201be.entities.SupplierInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodItemRepository extends JpaRepository<FoodItem, Long> {
    @Query("SELECT f FROM FoodItem f WHERE " +
            "(:keyword IS NULL OR :keyword = '' OR f.foodName ILIKE %:keyword%)")
    List<FoodItem> searchFoodItem(@Param("keyword") String keyword);

    @Query("SELECT f FROM FoodItem f WHERE " +
            "(:keyword IS NULL OR :keyword = '' OR f.foodName ILIKE %:keyword%)")
    Page<FoodItem> searchFoodItemPageable(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT f FROM FoodItem f ORDER BY f.quantitySold DESC")
    Page<FoodItem> findTopSoldFoodItems(Pageable pageable);

    List<FoodItem> findBySupplierInfo_Id(Long supplierId);

    @Query("SELECT f.foodName FROM FoodItem f WHERE LOWER(f.foodName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<String> findAllFoodItemNames(@Param("keyword") String keyword);

    @Query(value = "SELECT f FROM FoodItem f ORDER BY f.quantitySold DESC")
    List<FoodItem> findTopSellingProducts(@Param("limit") int limit);

    List<FoodItem> findBySupplierInfo_IdAndIsOffered(Long supplierId, int isOffered);
}
