package com.exe201.exe201be.services;

import com.exe201.exe201be.dtos.FoodTypeDTO;
import com.exe201.exe201be.dtos.SupplierInfoDTO;
import com.exe201.exe201be.entities.*;
import com.exe201.exe201be.exceptions.DataNotFoundException;
import com.exe201.exe201be.repositories.FoodTypeRepository;
import com.exe201.exe201be.repositories.SupplierInfoRepository;
import com.exe201.exe201be.responses.FoodItemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FoodTypeService implements IFoodTypeService{
    private final FoodTypeRepository foodTypeRepository;
    private final SupplierInfoRepository supplierInfoRepository;
    public List<FoodType> getAllFoodItem() {
        return foodTypeRepository.findAll();
    }

    public List<FoodType> getFoodTypeBySupplierInfoId(Long supplierInfoId) {
        return foodTypeRepository.findBySupplierInfoId(supplierInfoId);
    }

    public FoodType createFoodType(FoodTypeDTO foodTypeDTO) throws DataNotFoundException {
        SupplierInfo existingSupplier = supplierInfoRepository.findById(foodTypeDTO.getSupplierInfoId())
                .orElseThrow(()-> new DataNotFoundException("Cannot find Supplier with id: "+ foodTypeDTO.getSupplierInfoId()));
        FoodType newFoodType = new FoodType();
        newFoodType.setTypeName(foodTypeDTO.getTypeName());
        newFoodType.setI(foodTypeDTO.getI());
        newFoodType.setSupplierInfo(existingSupplier);

        return foodTypeRepository.save(newFoodType);
    }
}
