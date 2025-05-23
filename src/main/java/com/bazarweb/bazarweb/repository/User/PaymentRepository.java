package com.bazarweb.bazarweb.repository.User;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bazarweb.bazarweb.model.User.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Integer>{
    Optional<Payment> findById(int id);
    List<Payment> findByUserId(int id);
}
