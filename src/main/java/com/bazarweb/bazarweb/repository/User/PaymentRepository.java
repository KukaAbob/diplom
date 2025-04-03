package com.bazarweb.bazarweb.repository.User;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bazarweb.bazarweb.model.User.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Integer>{
    Payment findById(int id);
}
