package com.exe201.exe201be.controllers;

import com.exe201.exe201be.entities.FoodType;
import com.exe201.exe201be.entities.SupplierType;
import com.exe201.exe201be.services.IFoodTypeService;
import com.exe201.exe201be.services.ISupplierTypeService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/supplier_types")
@SecurityRequirement(name = "bearer-key")
@RequiredArgsConstructor
public class SupplierTypeController {

    private final ISupplierTypeService supplierTypeService;
    @GetMapping("/get_all_supplier_types")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_PARTNER','ROLE_CUSTOMER')")
    public ResponseEntity<?> getAllSupplier() {
        List<SupplierType> supplierTypes = supplierTypeService.getAllSupplierType();
        return ResponseEntity.ok(supplierTypes);
    }
}
