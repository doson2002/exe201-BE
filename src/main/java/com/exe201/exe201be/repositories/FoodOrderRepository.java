package com.exe201.exe201be.repositories;

import com.exe201.exe201be.entities.FoodOrder;
import com.exe201.exe201be.entities.FoodOrderItem;
import com.exe201.exe201be.responses.FoodItemReportResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.annotation.Nullable;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface FoodOrderRepository extends JpaRepository<FoodOrder, Long> {

    List<FoodOrder> findByUser_Id(Long userId);
    // Query để chỉ so sánh ngày
    @Query("SELECT o FROM FoodOrder o WHERE DATE(o.orderTime) = :orderDate AND o.supplierInfo.id = :supplierInfoId")
    List<FoodOrder> findByOrderTimeAndSupplierInfo_Id(@Param("orderDate") Date orderDate, @Param("supplierInfoId") Long supplierInfoId);

    @Query("SELECT o FROM FoodOrder o WHERE DATE(o.orderTime) = :orderDate")
    List<FoodOrder> findByOrderTime(@Param("orderDate") Date orderDate);



    // Query cho khoảng thời gian nhưng chỉ so sánh theo ngày
    @Query("SELECT o FROM FoodOrder o WHERE DATE(o.orderTime) BETWEEN :startDate AND :endDate AND o.supplierInfo.id = :supplierInfoId")
    List<FoodOrder> findByOrderTimeBetweenAndSupplierInfo_Id(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("supplierInfoId") Long supplierInfoId);


    @Query("SELECT o FROM FoodOrder o WHERE DATE(o.orderTime) BETWEEN :startDate AND :endDate")
    List<FoodOrder> findByOrderTimeBetween(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT f.supplierInfo AS supplierInfo, COUNT(f) AS totalOrders " +
            "FROM FoodOrder f " +
            "GROUP BY f.supplierInfo " +
            "ORDER BY COUNT(f) DESC")
    Page<Object[]> findTopSuppliersByOrderCount(Pageable pageable);


    @Query("SELECT f FROM FoodOrder f WHERE f.user.id = :userId " +
            "AND (:status IS NULL OR f.status = :status) " +
            "AND DATE(f.orderTime) BETWEEN :startDate AND :endDate")

    Page<FoodOrder> findByUserIdAndStatusAndDateRange(@Param("userId") Long userId,
                                                      @Param("status") String status,
                                                      @Nullable @Param("startDate") Date startDate,
                                                      @Nullable @Param("endDate") Date endDate,
                                                      Pageable pageable);


    @Query("SELECT fo.supplierInfo.id, COUNT(fo), fo.supplierInfo.restaurantName, fo.supplierInfo.imgUrl " +
            "FROM FoodOrder fo " +
            "GROUP BY fo.supplierInfo.id, fo.supplierInfo.restaurantName, fo.supplierInfo.imgUrl")
    List<Object[]> countOrdersBySupplier();

    @Query("SELECT COUNT(fo) FROM FoodOrder fo")
    Long countTotalOrders();


    @Query("SELECT foi.foodItem.id AS id, " +
            "foi.foodItem.foodName AS foodName, " +
            "foi.foodItem.imgUrl AS imgUrl, " +
            "SUM(foi.quantity) AS quantity " +
            "FROM FoodOrder fo " +
            "JOIN fo.foodOrderItems foi " +
            "WHERE DATE(fo.orderTime) BETWEEN :startDate AND :endDate " +
            "AND fo.supplierInfo.id = :supplierInfoId " + // Thêm điều kiện lọc theo supplierInfoId
            "GROUP BY foi.foodItem.id, foi.foodItem.foodName, foi.foodItem.imgUrl " +
            "ORDER BY SUM(foi.quantity) DESC")
    List<Object[]> findTopSellingFoodItems(@Param("startDate") Date startDate,
                                           @Param("endDate") Date endDate,
                                           @Param("supplierInfoId") Long supplierInfoId);
}
