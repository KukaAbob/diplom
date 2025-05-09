package com.bazarweb.bazarweb.repository.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bazarweb.bazarweb.model.User.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Integer>{
    Payment findById(int id);
    List<Payment> findByUserId(int id);
}
