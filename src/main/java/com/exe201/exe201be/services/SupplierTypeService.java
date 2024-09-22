package com.exe201.exe201be.services;

import com.exe201.exe201be.entities.FoodType;
import com.exe201.exe201be.entities.SupplierType;
import com.exe201.exe201be.repositories.FoodTypeRepository;
import com.exe201.exe201be.repositories.SupplierTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SupplierTypeService implements ISupplierTypeService{

    private final SupplierTypeRepository supplierTypeRepository;
    public List<SupplierType> getAllSupplierType() {
        return supplierTypeRepository.findAll();
    }
}
