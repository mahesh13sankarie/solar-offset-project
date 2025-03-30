package org.example.server.dto;

import java.math.BigDecimal;

import org.example.server.utils.PaymentType;

public record PaymentRequestDTO(
		Long userId,
		Long countryPanelId,
		BigDecimal amount,
		PaymentType type) {
}