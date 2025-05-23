package com.bazarweb.bazarweb.service.User;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.bazarweb.bazarweb.model.User.Payment;
import com.bazarweb.bazarweb.repository.User.PaymentRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    
    public Payment paymentCreate(Payment payment){
        validatePayment(payment);
        return paymentRepository.save(payment);
    }

    public void paymentDelete(int paymentId){
        Payment payment = paymentRepository.findById(paymentId)
            .orElseThrow(() -> new IllegalArgumentException("no"));
        paymentRepository.delete(payment);
    }

    private void validatePayment(Payment payment) {
        // Проверка на null
        if (payment == null) {
            throw new IllegalArgumentException("Payment cannot be null");
        }
        
        // Проверка номера карты
        if (payment.getCardNumber() == null || payment.getCardNumber().isEmpty()) {
            throw new IllegalArgumentException("Card number cannot be empty");
        }
        
        // Проверка формата номера карты (должен содержать только цифры)
        if (!payment.getCardNumber().matches("\\d+")) {
            throw new IllegalArgumentException("Card number must contain only digits");
        }
        
        // Проверка длины номера карты (обычно 16 цифр)
        if (payment.getCardNumber().length() != 16) {
            throw new IllegalArgumentException("Card number must be 16 digits");
        }
        
        // Проверка срока действия
        if (payment.getExpiryDate() == null) {
            throw new IllegalArgumentException("Expiry date cannot be empty");
        }
        
        // Проверка, что срок действия не истек
        if (payment.getExpiryDate().before(new Date())) {
            throw new IllegalArgumentException("Card has expired");
        }
        
        // Проверка CVV/CVC
        if (payment.getCvvCode() == null || payment.getCvvCode().isEmpty()) {
            throw new IllegalArgumentException("CVV cannot be empty");
        }
        
        // Проверка формата CVV (должен содержать только цифры, обычно 3 или 4 цифры)
        if (!payment.getCvvCode().matches("\\d+") || (payment.getCvvCode().length() != 3 && payment.getCvvCode().length() != 4)) {
            throw new IllegalArgumentException("CVV must be 3 or 4 digits");
        }
    }

    public Payment findById(int id){
        return paymentRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("no"));
    }

    public List<Payment> findByUserId(int id){
        return paymentRepository.findByUserId(id);
    }
}
