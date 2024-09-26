package com.exe201.exe201be.repositories;

import com.exe201.exe201be.entities.FoodOrder;
import com.exe201.exe201be.entities.FoodOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface FoodOrderRepository extends JpaRepository<FoodOrder, Long> {

    List<FoodOrder> findByUser_Id(Long userId);
    // Query để chỉ so sánh ngày
    @Query("SELECT o FROM FoodOrder o WHERE DATE(o.orderTime) = :orderDate AND o.supplierInfo.id = :supplierInfoId")
    List<FoodOrder> findByOrderTimeAndSupplierInfo_Id(@Param("orderDate") Date orderDate, @Param("supplierInfoId") Long supplierInfoId);

    // Query cho khoảng thời gian nhưng chỉ so sánh theo ngày
    @Query("SELECT o FROM FoodOrder o WHERE DATE(o.orderTime) BETWEEN :startDate AND :endDate AND o.supplierInfo.id = :supplierInfoId")
    List<FoodOrder> findByOrderTimeBetweenAndSupplierInfo_Id(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("supplierInfoId") Long supplierInfoId);


}
