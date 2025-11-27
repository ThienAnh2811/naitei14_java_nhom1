package org.example.foodanddrinkproject.service;

import org.example.foodanddrinkproject.dto.AddressDto;
import java.util.List;

public interface AddressService {
    List<AddressDto> getAllAddresses(Long userId);
    AddressDto addAddress(Long userId, AddressDto addressDto);
    AddressDto updateAddress(Long userId, Long addressId, AddressDto addressDto);
    void deleteAddress(Long userId, Long addressId);
}