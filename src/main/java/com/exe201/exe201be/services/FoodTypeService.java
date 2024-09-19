package com.exe201.exe201be.services;

import com.exe201.exe201be.entities.FoodItem;
import com.exe201.exe201be.entities.FoodType;
import com.exe201.exe201be.repositories.FoodTypeRepository;
import com.exe201.exe201be.responses.FoodItemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FoodTypeService implements IFoodTypeService{
    private final FoodTypeRepository foodTypeRepository;
    public List<FoodType> getAllFoodItem() {
        return foodTypeRepository.findAll();
    }
}
