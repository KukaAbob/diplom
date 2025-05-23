package com.bazarweb.bazarweb.service.User;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bazarweb.bazarweb.model.User.Address;
import com.bazarweb.bazarweb.repository.User.AddressRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;

    public Address addressAdd(Address address){
        return addressRepository.save(address);
    }

    public Address updateAddress(Address address){
        Address existingAddress = addressRepository.findById(address.getId())
            .orElseThrow(() -> new IllegalArgumentException("no"));
        existingAddress.setCity(address.getCity());
        existingAddress.setCountry(address.getCountry());
        existingAddress.setStreet(address.getStreet());
        existingAddress.setZipCode(address.getZipCode());
        existingAddress.setUser(address.getUser());
        return addressRepository.save(existingAddress);
    }

    public void deleteAddress(int addressId){
        Address address = addressRepository.findById(addressId)
            .orElseThrow(() -> new IllegalArgumentException("no"));
        addressRepository.delete(address);
    }

    public Address getAddressById(int addressId){
        return addressRepository.findById(addressId)
            .orElseThrow(() -> new IllegalArgumentException("no"));
    }

    public Address findById(int id){
        return addressRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("no"));
    }

    public List<Address> findByUserId(int id){
        return addressRepository.findByUserId(id);
    }
    
}
