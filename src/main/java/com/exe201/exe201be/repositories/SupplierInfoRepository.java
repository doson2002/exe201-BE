package com.exe201.exe201be.repositories;

import com.exe201.exe201be.entities.SupplierInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierInfoRepository extends JpaRepository<SupplierInfo, Long> {

    @Query("SELECT s FROM SupplierInfo s WHERE " +
            "(:keyword IS NULL OR :keyword = '' OR s.restaurantName ILIKE %:keyword%)")
    Page<SupplierInfo> searchSupplierInfo(@Param("keyword") String keyword, Pageable pageable);

    SupplierInfo findByUser_id(Long userId);

    List<SupplierInfo> findAllBySupplierType_Id(Long supplierTypeId);
}
