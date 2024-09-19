package com.exe201.exe201be.controllers;

import com.exe201.exe201be.dtos.DeliveryDTO;
import com.exe201.exe201be.entities.Delivery;
import com.exe201.exe201be.exceptions.DataNotFoundException;
import com.exe201.exe201be.responses.DeliveryResponse;
import com.exe201.exe201be.responses.DeliveryResponseList;
import com.exe201.exe201be.services.IDeliveryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("${api.prefix}/delivery")
@RequiredArgsConstructor
public class DeliveryController {
    private final IDeliveryService deliveryService;

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MANAGER')")
    public ResponseEntity<?> createDelivery(
            @Valid @RequestBody DeliveryDTO deliveryDTO,
            BindingResult result){
        try{
            if(result.hasErrors()){
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            Delivery newDelivery = deliveryService.createDelivery(deliveryDTO);

            return ResponseEntity.ok(newDelivery);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MANAGER')")
    public ResponseEntity<?> updateDelivery(@PathVariable Long id,
                                            @Valid @RequestBody DeliveryDTO deliveryDTO){
        try{
            Delivery updateDelivery = deliveryService.updateDelivery(id, deliveryDTO);
            return ResponseEntity.ok(updateDelivery);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/get_all_deliveries")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MANAGER','ROLE_STAFF')")
    public ResponseEntity<DeliveryResponseList> getAllDeliveries(
            @RequestParam(defaultValue = "")String keyword,
            @RequestParam("page") int page, @RequestParam("limit")int limit){
        PageRequest pageRequest = PageRequest.of(page, limit);
        Page<DeliveryResponse> deliveryResponsePage = deliveryService.getAllDelivery(keyword, pageRequest);
        int totalPages = deliveryResponsePage.getTotalPages();
        List<DeliveryResponse> deliveryResponseList = deliveryResponsePage.getContent();
        return ResponseEntity.ok(DeliveryResponseList.builder()
                .deliveries(deliveryResponseList)
                .totalPages(totalPages)
                .build());
    }
    @GetMapping("/get_delivery_by_id/{deliveryId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MANAGER','ROLE_STAFF')")
    public ResponseEntity<?> getDelivery(@Valid @PathVariable Long deliveryId) throws DataNotFoundException {
        Delivery delivery = deliveryService.getDelivery(deliveryId);
        return ResponseEntity.ok(DeliveryResponse.fromDelivery(delivery));
    }
}
