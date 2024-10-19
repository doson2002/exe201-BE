package com.exe201.exe201be.services;

import com.exe201.exe201be.dtos.SupplierTypeDTO;
import com.exe201.exe201be.entities.SupplierType;

import java.util.List;

public interface ISupplierTypeService {
    SupplierType createFoodType(SupplierTypeDTO supplierTypeDTO);
    List<SupplierType> getAllSupplierType();
}
