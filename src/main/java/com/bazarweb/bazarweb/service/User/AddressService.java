package com.bazarweb.bazarweb.service.User;

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
        Address existingAddress = addressRepository.findById(address.getId());
        existingAddress.setCity(address.getCity());
        existingAddress.setCountry(address.getCountry());
        existingAddress.setStreet(address.getStreet());
        existingAddress.setZipCode(address.getZipCode());
        existingAddress.setUser(address.getUser());
        return addressRepository.save(existingAddress);
    }

    public void deleteAddress(int addressId){
        Address address = addressRepository.findById(addressId);
        addressRepository.delete(address);
    }

    public Address getAddressById(int addressId){
        return addressRepository.findById(addressId);
    }

    public Address findById(int id){
        return addressRepository.findById(id);
    }
    
}
