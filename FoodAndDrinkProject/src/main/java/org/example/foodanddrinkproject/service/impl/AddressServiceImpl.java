package org.example.foodanddrinkproject.service.impl;

import org.example.foodanddrinkproject.dto.AddressDto;
import org.example.foodanddrinkproject.exception.BadRequestException;
import org.example.foodanddrinkproject.exception.ResourceNotFoundException;
import org.example.foodanddrinkproject.entity.Address;
import org.example.foodanddrinkproject.entity.User;
import org.example.foodanddrinkproject.repository.AddressRepository;
import org.example.foodanddrinkproject.repository.UserRepository;
import org.example.foodanddrinkproject.service.AddressService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public AddressServiceImpl(AddressRepository addressRepository, UserRepository userRepository) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<AddressDto> getAllAddresses(Long userId) {
        return addressRepository.findByUserId(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AddressDto addAddress(Long userId, AddressDto addressDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Address address = new Address();
        address.setUser(user);

        List<Address> existingAddresses = addressRepository.findByUserId(userId);
        if (existingAddresses.isEmpty()) {
            address.setDefault(true);
        } else {
            if (addressDto.isDefault()) {
                unsetOtherDefaults(existingAddresses);
            }
            address.setDefault(addressDto.isDefault());
        }

        updateEntityFromDto(address, addressDto);
        Address savedAddress = addressRepository.save(address);
        return convertToDto(savedAddress);
    }

    @Override
    @Transactional
    public AddressDto updateAddress(Long userId, Long addressId, AddressDto addressDto) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "id", addressId));
        // Security Check: Does this address belong to the user?
        if (!address.getUser().getId().equals(userId)) {
            throw new BadRequestException("You do not have permission to update this address.");
        }
        // Logic: If setting this as default, unset others
        if (addressDto.isDefault() && !address.isDefault()) {
            List<Address> allAddresses = addressRepository.findByUserId(userId);
            unsetOtherDefaults(allAddresses);
        }

        address.setDefault(addressDto.isDefault());
        updateEntityFromDto(address, addressDto);

        Address savedAddress = addressRepository.save(address);
        return convertToDto(savedAddress);
    }

    @Override
    @Transactional
    public void deleteAddress(Long userId, Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "id", addressId));

        if (!address.getUser().getId().equals(userId)) {
            throw new BadRequestException("You do not have permission to delete this address.");
        }
        // Logic: If deleting default, find another one to promote
        if (address.isDefault()) {
            List<Address> allAddresses = addressRepository.findByUserId(userId);
            Address newDefault = allAddresses.stream()
                    .filter(a -> !a.getId().equals(addressId))
                    .findFirst() // Picks the first one found (usually oldest or newest depending on Sort)
                    .orElse(null);
            if (newDefault != null) {
                newDefault.setDefault(true);
                addressRepository.save(newDefault);
            }
        }
        addressRepository.delete(address);
    }

    // --- Helpers ---

    private void unsetOtherDefaults(List<Address> addresses) {
        for (Address addr : addresses) {
            if (addr.isDefault()) {
                addr.setDefault(false);
                addressRepository.save(addr);
            }
        }
    }

    private void updateEntityFromDto(Address address, AddressDto dto) {
        address.setRecipientName(dto.getRecipientName());
        address.setPhoneNumber(dto.getPhoneNumber());
        address.setStreet(dto.getStreet());
        address.setCity(dto.getCity());
        address.setState(dto.getState());
        address.setZipCode(dto.getZipCode());
        address.setCountry(dto.getCountry());
    }

    private AddressDto convertToDto(Address address) {
        AddressDto dto = new AddressDto();
        dto.setId(address.getId());
        dto.setRecipientName(address.getRecipientName());
        dto.setPhoneNumber(address.getPhoneNumber());
        dto.setStreet(address.getStreet());
        dto.setCity(address.getCity());
        dto.setState(address.getState());
        dto.setZipCode(address.getZipCode());
        dto.setCountry(address.getCountry());
        dto.setDefault(address.isDefault());
        return dto;
    }
}