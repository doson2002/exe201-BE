package com.exe201.exe201be.services;

import com.exe201.exe201be.dtos.FoodTypeDTO;
import com.exe201.exe201be.dtos.SupplierTypeDTO;
import com.exe201.exe201be.entities.FoodType;
import com.exe201.exe201be.entities.SupplierInfo;
import com.exe201.exe201be.entities.SupplierType;
import com.exe201.exe201be.exceptions.DataNotFoundException;
import com.exe201.exe201be.repositories.FoodTypeRepository;
import com.exe201.exe201be.repositories.SupplierTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SupplierTypeService implements ISupplierTypeService{

    private final SupplierTypeRepository supplierTypeRepository;

    public SupplierType createFoodType(SupplierTypeDTO supplierTypeDTO) {
        SupplierType newSupplierType = new SupplierType();
        newSupplierType.setTypeName(supplierTypeDTO.getTypeName());
        newSupplierType.setImgUrl(supplierTypeDTO.getImgUrl());
        return supplierTypeRepository.save(newSupplierType);
    }
    public List<SupplierType> getAllSupplierType() {
        return supplierTypeRepository.findAll();
    }
}
