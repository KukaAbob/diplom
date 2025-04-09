package com.bazarweb.bazarweb.controller.User;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bazarweb.bazarweb.dto.AddressDto;
import com.bazarweb.bazarweb.model.User.Address;
import com.bazarweb.bazarweb.service.User.AddressService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/address")
@RequiredArgsConstructor
public class AddressController {
    
    private final AddressService addressService;

    @GetMapping("/{id}")
    public ResponseEntity<AddressDto> getAddress(@PathVariable int id) {
        Address address = addressService.findById(id);
        return ResponseEntity.ok(convertToDto(address));
    }
    
    @PostMapping("/create")
    public ResponseEntity<AddressDto> createPayment(@RequestBody Address address){
        Address savedAddress = addressService.addressAdd(address);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDto(savedAddress));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressDto> updateAddress(@PathVariable int id, @RequestBody Address address){
        address.setId(id);
        Address updatedAddress = addressService.updateAddress(address);
        return ResponseEntity.ok(convertToDto(updatedAddress));
    }

    @DeleteMapping("/{id}")
    public void deleteAddress(@PathVariable int id){
        addressService.deleteAddress(id);
    }

        private AddressDto convertToDto(Address address) {
        AddressDto dto = new AddressDto();
        dto.setId(address.getId());
        dto.setCountry(address.getCountry());
        dto.setCity(address.getCity());
        dto.setStreet(address.getStreet());
        dto.setZipCode(address.getZipCode());
        
        if (address.getUser() != null) {
            dto.setUserId(address.getUser().getId());
        }
        
        return dto;
    }
}
