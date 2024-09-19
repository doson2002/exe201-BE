package com.exe201.exe201be.repositories;

import com.exe201.exe201be.entities.Delivery;
import com.exe201.exe201be.entities.FoodItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    @Query("SELECT d FROM Delivery d WHERE " +
            "(:keyword IS NULL OR :keyword = '' OR d.driverName ILIKE %:keyword% OR d.phone ILIKE %:keyword%)")
    Page<Delivery> searchDelivery(@Param("keyword") String keyword, Pageable pageable);

}
