package com.exe201.exe201be.repositories;

import com.exe201.exe201be.entities.FoodType;
import com.exe201.exe201be.entities.SupplierType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierTypeRepository extends JpaRepository<SupplierType, Long> {

}
