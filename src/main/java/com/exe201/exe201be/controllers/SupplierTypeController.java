package com.exe201.exe201be.controllers;

import com.exe201.exe201be.dtos.FoodTypeDTO;
import com.exe201.exe201be.dtos.SupplierTypeDTO;
import com.exe201.exe201be.entities.FoodType;
import com.exe201.exe201be.entities.SupplierType;
import com.exe201.exe201be.services.IFoodTypeService;
import com.exe201.exe201be.services.ISupplierTypeService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/supplier_types")
@SecurityRequirement(name = "bearer-key")
@RequiredArgsConstructor
public class SupplierTypeController {

    private final ISupplierTypeService supplierTypeService;

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_PARTNER')")
    public ResponseEntity<?> createSupplierType(
            @Valid @RequestBody SupplierTypeDTO supplierTypeDTO,
            BindingResult result){
        try{
            if(result.hasErrors()){
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            SupplierType newSupplierType = supplierTypeService.createFoodType(supplierTypeDTO);

            return ResponseEntity.ok(newSupplierType);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/get_all_supplier_types")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_PARTNER','ROLE_CUSTOMER')")
    public ResponseEntity<?> getAllSupplier() {
        List<SupplierType> supplierTypes = supplierTypeService.getAllSupplierType();
        return ResponseEntity.ok(supplierTypes);
    }
}
