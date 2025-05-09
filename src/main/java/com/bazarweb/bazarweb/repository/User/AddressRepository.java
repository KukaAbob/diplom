package com.bazarweb.bazarweb.repository.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bazarweb.bazarweb.model.User.Address;

public interface AddressRepository extends JpaRepository<Address, Integer>{
    Address findById(int id);
    List<Address> findByUserId(int id);
}
