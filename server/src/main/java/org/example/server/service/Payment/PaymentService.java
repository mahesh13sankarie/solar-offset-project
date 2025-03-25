package org.example.server.service.Payment;

import java.util.List;

import org.example.server.dto.PaymentRequestDTO;
import org.example.server.dto.PaymentResponseDTO;

public interface PaymentService {

	PaymentResponseDTO processPayment(PaymentRequestDTO request);

	List<PaymentResponseDTO> getUserPayments(Long userId);
}