package org.example.server.service.Payment;

import java.util.List;

import org.example.server.dto.PaymentRequestDTO;
import org.example.server.dto.PaymentResponseDTO;

import com.stripe.exception.StripeException;

public interface PaymentService {

	PaymentResponseDTO processPayment(PaymentRequestDTO request) throws StripeException;

	List<PaymentResponseDTO> getUserPayments(Long userId) throws StripeException;
}