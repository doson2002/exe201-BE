package com.exe201.exe201be.services;

import com.exe201.exe201be.dtos.SupplierInfoDTO;
import com.exe201.exe201be.entities.SupplierInfo;
import com.exe201.exe201be.entities.Users;
import com.exe201.exe201be.exceptions.DataNotFoundException;
import com.exe201.exe201be.repositories.SupplierInfoRepository;
import com.exe201.exe201be.repositories.UserRepository;
import com.exe201.exe201be.responses.SupplierInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SupplierInfoService implements ISupplierInfoService {
    private final SupplierInfoRepository supplierInfoRepository;
    private final UserRepository userRepository;

    public SupplierInfo createSupplierInfo(SupplierInfoDTO supplierInfoDTO) throws DataNotFoundException {
        Users existingUser = userRepository.findById(supplierInfoDTO.getUserId())
                .orElseThrow(()-> new DataNotFoundException("Cannot find user id: "+ supplierInfoDTO.getUserId()));
        SupplierInfo newSupplierInfo = SupplierInfo
                .builder()
                .restaurantName(supplierInfoDTO.getRestaurantName())
                .description(supplierInfoDTO.getDescription())
                .slogan(supplierInfoDTO.getSlogan())
                .imgUrl(supplierInfoDTO.getImgUrl())
                .user(existingUser)
                .address(supplierInfoDTO.getAddress())
                .build();
        existingUser.setFirstLogin(false);
        return supplierInfoRepository.save(newSupplierInfo);
    }

    @Override
    public SupplierInfo updateSupplier(Long id, SupplierInfoDTO supplierInfoDTO) throws DataNotFoundException {
        SupplierInfo existingSupplier = supplierInfoRepository.findById(id)
                .orElseThrow(()->new DataNotFoundException("Supplier infomation cannot find with id"+ id));
        if(existingSupplier!=null){

            Users existingUser = userRepository.findById(supplierInfoDTO.getUserId())
                    .orElseThrow(()-> new DataNotFoundException("Cannot find type with id "+supplierInfoDTO.getUserId()));
            existingSupplier.setRestaurantName(supplierInfoDTO.getRestaurantName());
            existingSupplier.setDescription(supplierInfoDTO.getDescription());
            existingSupplier.setAddress(supplierInfoDTO.getAddress());
            existingSupplier.setSlogan(supplierInfoDTO.getSlogan());
            existingSupplier.setImgUrl(supplierInfoDTO.getImgUrl());
            existingSupplier.setUser(existingUser);
            return supplierInfoRepository.save(existingSupplier);
        }
        return null;
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

}
