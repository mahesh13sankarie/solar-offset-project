package org.example.server.dto;

import java.math.BigDecimal;

public record PaymentRequestDTO(
		Long userId,
		Long countryPanelId,
		BigDecimal amount,
		String type) {
}