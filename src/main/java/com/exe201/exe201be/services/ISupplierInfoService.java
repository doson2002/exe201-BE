package com.exe201.exe201be.services;

import com.exe201.exe201be.dtos.SupplierInfoDTO;
import com.exe201.exe201be.entities.SupplierInfo;
import com.exe201.exe201be.exceptions.DataNotFoundException;
import com.exe201.exe201be.responses.SupplierInfoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public interface ISupplierInfoService {
    SupplierInfo createSupplierInfo(SupplierInfoDTO supplierInfoDTO) throws DataNotFoundException;
    void updateSupplier(Long id, SupplierInfoDTO supplierInfoDTO) throws DataNotFoundException;

    SupplierInfo getSupplierInfo(Long id) throws DataNotFoundException;

    Page<SupplierInfoResponse> getAllSuppliers(String keyword, PageRequest pageRequest);

    SupplierInfo getSupplierByUserId(Long userId);

    List<SupplierInfo> getSupplierInfoBySupplierTypeId(Long supplierTypeId);
    void updateTimeOfRestaurant(Long id, LocalTime openTime, LocalTime closeTime) throws DataNotFoundException;

    Page<Map<String, Object>> getTopSuppliers(Pageable pageable);
}
