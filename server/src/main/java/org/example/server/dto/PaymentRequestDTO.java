package org.example.server.dto;

import java.math.BigDecimal;

import org.example.server.utils.PaymentType;

/**
 * Payment Request DTO
 * Following Stripe's recommended approach, this DTO uses only paymentMethodId.
 * The client should collect card information using Stripe.js and generate a
 * PaymentMethod ID.
 */
public record PaymentRequestDTO(
		Long userId,
		Long countryPanelId,
		BigDecimal amount,
		PaymentType paymentType,
		String paymentMethodId) {
}