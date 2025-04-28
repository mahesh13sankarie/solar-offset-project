package org.example.server.service.Payment;

import java.util.List;

import org.example.server.dto.PaymentRequestDTO;
import org.example.server.dto.PaymentResponseDTO;
import org.example.server.exception.PaymentException;

public interface PaymentService {

	PaymentResponseDTO processPayment(PaymentRequestDTO request) throws PaymentException;

	List<PaymentResponseDTO> getUserPayments(Long userId) throws PaymentException;
}