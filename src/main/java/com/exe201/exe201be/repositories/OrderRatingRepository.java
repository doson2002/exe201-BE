package com.exe201.exe201be.repositories;

import com.exe201.exe201be.entities.OrderRating;
import com.exe201.exe201be.entities.Role;
import com.exe201.exe201be.entities.SupplierInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRatingRepository extends JpaRepository<OrderRating,Long> {
    List<OrderRating> findBySupplierInfo_Id(Long supplierInfoId);

    int countByRatingStarAndSupplierInfo(int ratingStar, SupplierInfo supplierInfo);
    List<OrderRating> findBySupplierInfoId(Long supplierId);;

}
