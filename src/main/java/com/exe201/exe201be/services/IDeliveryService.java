package com.exe201.exe201be.services;

import com.exe201.exe201be.dtos.DeliveryDTO;
import com.exe201.exe201be.entities.Delivery;
import com.exe201.exe201be.exceptions.DataNotFoundException;
import com.exe201.exe201be.responses.DeliveryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface IDeliveryService {
    Delivery createDelivery(DeliveryDTO deliveryDTO) throws DataNotFoundException;

    Delivery updateDelivery(Long id, DeliveryDTO deliveryDTO) throws DataNotFoundException;

    Delivery getDelivery(Long id) throws DataNotFoundException;

    Page<DeliveryResponse> getAllDelivery(String keyword, PageRequest pageRequest);
}
