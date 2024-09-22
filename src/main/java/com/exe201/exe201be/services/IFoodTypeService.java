package com.exe201.exe201be.services;

import com.exe201.exe201be.dtos.FoodTypeDTO;
import com.exe201.exe201be.entities.FoodType;
import com.exe201.exe201be.exceptions.DataNotFoundException;

import java.util.List;

public interface IFoodTypeService {

    FoodType createFoodType(FoodTypeDTO foodTypeDTO) throws DataNotFoundException;
    List<FoodType> getAllFoodItem();
    List<FoodType> getFoodTypeBySupplierInfoId(Long supplierInfoId);
}
