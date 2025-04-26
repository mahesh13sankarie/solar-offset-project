package org.example.server.dto;

import org.example.server.utils.PaymentType;

import jakarta.validation.constraints.NotNull;

/**
 * Payment Request DTO
 * Following Stripe's recommended approach, this DTO uses only paymentMethodId.
 * The client should collect card information using Stripe.js and generate a
 * PaymentMethod ID.
 */
public record PaymentRequestDTO(
		@NotNull(message = "User ID must not be null")
		Long userId,
		@NotNull(message = "Country Panel ID must not be null")
		Long countryPanelId,

		@NotNull(message = "Amount must not be null")
		Integer quantity,

		@NotNull(message = "Payment type must not be null")
		PaymentType paymentType,

		@NotNull(message = "Payment method ID must not be null")
		String paymentMethodId) {
}