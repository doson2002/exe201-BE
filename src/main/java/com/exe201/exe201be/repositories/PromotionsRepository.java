package com.exe201.exe201be.repositories;

import com.exe201.exe201be.entities.Promotions;
import com.exe201.exe201be.entities.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PromotionsRepository extends JpaRepository<Promotions,Long> {


    @Query("SELECT p FROM Promotions p WHERE (:supplierInfoId IS NULL OR p.supplierInfo.id = :supplierInfoId) AND (:code IS NULL OR p.code LIKE %:code%)")
    Page<Promotions> searchPromotions(@Param("supplierInfoId") Long supplierInfoId,
                                      @Param("code") String code,
                                      Pageable pageable);
}
