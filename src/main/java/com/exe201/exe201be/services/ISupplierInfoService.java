package com.exe201.exe201be.services;

import com.exe201.exe201be.dtos.SupplierInfoDTO;
import com.exe201.exe201be.entities.SupplierInfo;
import com.exe201.exe201be.exceptions.DataNotFoundException;
import com.exe201.exe201be.responses.SupplierInfoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface ISupplierInfoService {
    SupplierInfo createSupplierInfo(SupplierInfoDTO supplierInfoDTO) throws DataNotFoundException;
    SupplierInfo updateSupplier(Long id, SupplierInfoDTO supplierInfoDTO) throws DataNotFoundException;

    SupplierInfo getSupplierInfo(Long id) throws DataNotFoundException;

    Page<SupplierInfoResponse> getAllSuppliers(String keyword, PageRequest pageRequest);

    SupplierInfo getSupplierByUserId(Long userId);
}
