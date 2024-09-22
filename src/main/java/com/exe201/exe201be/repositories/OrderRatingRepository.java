package com.exe201.exe201be.repositories;

import com.exe201.exe201be.entities.OrderRating;
import com.exe201.exe201be.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRatingRepository extends JpaRepository<OrderRating,Long> {
    List<OrderRating> findAllByFoodOrder_SupplierInfo_Id(Long supplierInfoId);
}
