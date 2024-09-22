package com.exe201.exe201be.repositories;

import com.exe201.exe201be.dtos.FoodTypeDTO;
import com.exe201.exe201be.entities.FoodOrderItem;
import com.exe201.exe201be.entities.FoodType;
import com.exe201.exe201be.exceptions.DataNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodTypeRepository extends JpaRepository<FoodType, Long> {
    List<FoodType> findBySupplierInfoId(Long supplierInfoId);

}
