package com.bazarweb.bazarweb.repository.User;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bazarweb.bazarweb.model.User.Address;

public interface AddressRepository extends JpaRepository<Address, Integer>{
    Optional<Address> findById(int id);
    List<Address> findByUserId(int id);
}
