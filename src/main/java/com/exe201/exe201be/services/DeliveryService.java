package com.exe201.exe201be.services;

import com.exe201.exe201be.dtos.DeliveryDTO;
import com.exe201.exe201be.dtos.FoodItemDTO;
import com.exe201.exe201be.entities.Delivery;
import com.exe201.exe201be.entities.FoodItem;
import com.exe201.exe201be.entities.SupplierInfo;
import com.exe201.exe201be.exceptions.DataNotFoundException;
import com.exe201.exe201be.repositories.DeliveryRepository;
import com.exe201.exe201be.responses.DeliveryResponse;
import com.exe201.exe201be.responses.FoodItemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryService implements IDeliveryService{

    private final DeliveryRepository deliveryRepository;
    public Delivery createDelivery(DeliveryDTO deliveryDTO) throws DataNotFoundException {
        Delivery newDelivery = Delivery
                .builder()
                .driverName(deliveryDTO.getDriverName())
                .vehicleType(deliveryDTO.getVehicleType())
                .phone(deliveryDTO.getPhone())
                .address(deliveryDTO.getAddress())
                .isActive(true)
                .build();
        return deliveryRepository.save(newDelivery);
    }

    @Override
    public Delivery updateDelivery(Long id, DeliveryDTO deliveryDTO) throws DataNotFoundException {
        Delivery existingDelivery = deliveryRepository.findById(id)
                .orElseThrow(()->new DataNotFoundException("delivery cannot find with id"+ id));
        if(existingDelivery!=null){
            existingDelivery.setDriverName(deliveryDTO.getDriverName());
            existingDelivery.setVehicleType(deliveryDTO.getVehicleType());
            existingDelivery.setAddress(deliveryDTO.getAddress());
            existingDelivery.setPhone(deliveryDTO.getPhone());
            existingDelivery.setIsActive(deliveryDTO.getIsActive());
            return deliveryRepository.save(existingDelivery);
        }
        return null;
    }

    public Delivery getDelivery(Long id) throws DataNotFoundException {
        return deliveryRepository.findById(id).orElseThrow(()->new DataNotFoundException("Delivery not found with id:" + id));
    }


    public Page<DeliveryResponse> getAllDelivery(String keyword, PageRequest pageRequest) {
        Page<Delivery> deliveryPage = deliveryRepository.searchDelivery(keyword, pageRequest);
        return deliveryPage.map(DeliveryResponse::fromDelivery);
    }

}
