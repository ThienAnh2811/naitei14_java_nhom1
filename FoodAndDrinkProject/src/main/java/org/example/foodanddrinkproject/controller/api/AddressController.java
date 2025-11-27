package org.example.foodanddrinkproject.controller.api;

import org.example.foodanddrinkproject.dto.AddressDto;
import org.example.foodanddrinkproject.dto.ApiResponse;
import org.example.foodanddrinkproject.security.CurrentUser;
import org.example.foodanddrinkproject.security.UserPrincipal;
import org.example.foodanddrinkproject.service.AddressService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/addresses")
@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping
    public ResponseEntity<List<AddressDto>> getMyAddresses(@CurrentUser UserPrincipal userPrincipal) {
        return ResponseEntity.ok(addressService.getAllAddresses(userPrincipal.getId()));
    }

    @PostMapping
    public ResponseEntity<AddressDto> addAddress(@CurrentUser UserPrincipal userPrincipal,
                                                 @Valid @RequestBody AddressDto addressDto) {
        AddressDto newAddress = addressService.addAddress(userPrincipal.getId(), addressDto);
        return ResponseEntity.ok(newAddress);
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<AddressDto> updateAddress(@CurrentUser UserPrincipal userPrincipal,
                                                    @PathVariable Long addressId,
                                                    @Valid @RequestBody AddressDto addressDto) {
        AddressDto updatedAddress = addressService.updateAddress(userPrincipal.getId(), addressId, addressDto);
        return ResponseEntity.ok(updatedAddress);
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<ApiResponse> deleteAddress(@CurrentUser UserPrincipal userPrincipal,
                                                     @PathVariable Long addressId) {
        addressService.deleteAddress(userPrincipal.getId(), addressId);
        return ResponseEntity.ok(new ApiResponse(true, "Address deleted successfully"));
    }
}
