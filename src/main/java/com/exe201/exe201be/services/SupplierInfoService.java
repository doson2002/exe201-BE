package com.exe201.exe201be.services;

import com.exe201.exe201be.dtos.SupplierInfoDTO;
import com.exe201.exe201be.entities.*;
import com.exe201.exe201be.exceptions.DataNotFoundException;
import com.exe201.exe201be.repositories.SupplierInfoRepository;
import com.exe201.exe201be.repositories.SupplierTypeRepository;
import com.exe201.exe201be.repositories.UserRepository;
import com.exe201.exe201be.responses.SupplierInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupplierInfoService implements ISupplierInfoService {
    private final SupplierInfoRepository supplierInfoRepository;
    private final UserRepository userRepository;
    private final SupplierTypeRepository supplierTypeRepository;


    public SupplierInfo createSupplierInfo(SupplierInfoDTO supplierInfoDTO) throws DataNotFoundException {
        Users existingUser = userRepository.findById(supplierInfoDTO.getUserId())
                .orElseThrow(()-> new DataNotFoundException("Cannot find user id: "+ supplierInfoDTO.getUserId()));
        SupplierType existingSupplierType = supplierTypeRepository.findById(supplierInfoDTO.getSupplierTypeId())
                .orElseThrow(()-> new DataNotFoundException("Cannot find user id: "+ supplierInfoDTO.getSupplierTypeId()));

        SupplierInfo newSupplierInfo = new SupplierInfo();

        if (!supplierInfoDTO.getRestaurantName().isEmpty()&&supplierInfoDTO.getRestaurantName()!=null){
            newSupplierInfo.setRestaurantName(supplierInfoDTO.getRestaurantName());
        }
        if (!supplierInfoDTO.getDescription().isEmpty()){
            newSupplierInfo.setDescription(supplierInfoDTO.getDescription());
        }
        if (!supplierInfoDTO.getAddress().isEmpty()){
            newSupplierInfo.setAddress(supplierInfoDTO.getAddress());
        }
        if (!supplierInfoDTO.getImgUrl().isEmpty()){
            newSupplierInfo.setImgUrl(supplierInfoDTO.getImgUrl());
        }
        if (existingUser!=null){
            newSupplierInfo.setUser(existingUser);
        }
        if(existingSupplierType!=null){
            newSupplierInfo.setSupplierType(existingSupplierType);
        }
        newSupplierInfo.setTotalReviewCount(0);
        newSupplierInfo.setTotalReviewCount(0);
        existingUser.setFirstLogin(false);
        return supplierInfoRepository.save(newSupplierInfo);
    }

    @Override
    public void updateSupplier(Long id, SupplierInfoDTO supplierInfoDTO) throws DataNotFoundException {
        SupplierInfo existingSupplier = supplierInfoRepository.findById(id)
                .orElseThrow(()->new DataNotFoundException("Supplier infomation cannot find with id"+ id));


        if(existingSupplier!=null){

            if(supplierInfoDTO.getUserId()!=null){
                Users existingUser = userRepository.findById(supplierInfoDTO.getUserId())
                        .orElseThrow(()-> new DataNotFoundException("Cannot find user with id "+supplierInfoDTO.getUserId()));
                existingSupplier.setUser(existingUser);
            }

            if (supplierInfoDTO.getRestaurantName() !=null){
                existingSupplier.setRestaurantName(supplierInfoDTO.getRestaurantName());
            }
            if (supplierInfoDTO.getDescription() != null){
                existingSupplier.setDescription(supplierInfoDTO.getDescription());
            }
            if (supplierInfoDTO.getAddress() !=null){
                existingSupplier.setAddress(supplierInfoDTO.getAddress());
            }
            if (supplierInfoDTO.getImgUrl()!=null){
                existingSupplier.setImgUrl(supplierInfoDTO.getImgUrl());
            }
            if(supplierInfoDTO.getSupplierTypeId()!=null){
                SupplierType existingSupplierType = supplierTypeRepository.findById(supplierInfoDTO.getSupplierTypeId())
                        .orElseThrow(()->new DataNotFoundException("Supplier type cannot find with id"+ supplierInfoDTO.getSupplierTypeId()));
                existingSupplier.setSupplierType(existingSupplierType);
            }



             supplierInfoRepository.save(existingSupplier);
        }
    }

    @Override
    public void updateTimeOfRestaurant(Long id, LocalTime openTime, LocalTime closeTime) throws DataNotFoundException {
        SupplierInfo existingSupplier = supplierInfoRepository.findById(id)
                .orElseThrow(()->new DataNotFoundException("Supplier infomation cannot find with id"+ id));
        if(existingSupplier!=null){
            existingSupplier.setOpenTime(openTime);
            existingSupplier.setCloseTime(closeTime);
            supplierInfoRepository.save(existingSupplier);
        }
    }

    public SupplierInfo getSupplierInfo(Long id) throws DataNotFoundException {
        return supplierInfoRepository.findById(id).orElseThrow(()->new DataNotFoundException("Supplier not found with id:" + id));
    }


    public Page<SupplierInfoResponse> getAllSuppliers(String keyword, PageRequest pageRequest) {
        Page<SupplierInfo> supplierPage = supplierInfoRepository.searchSupplierInfo(keyword, pageRequest);
        return supplierPage.map(SupplierInfoResponse::fromSupplierInfo);
    }

    public SupplierInfo getSupplierByUserId(Long userId) {
        return supplierInfoRepository.findByUser_id(userId);
    }

    public List<SupplierInfo> getSupplierInfoBySupplierTypeId(Long supplierTypeId) {
        // Tìm danh sách FoodItemType liên kết với foodTypeId
        List<SupplierInfo> supplierInfoList = supplierInfoRepository.findAllBySupplierType_Id(supplierTypeId);
        return supplierInfoList;
    }

}
