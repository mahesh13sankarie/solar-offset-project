package org.example.server.dto;

/**
 * DTO for displaying transaction history to staff users
 */
public record StaffTransactionDTO(
		String date,
		String user,
		String country,
		String panelType,
		Integer quantity,
		Integer amount,
		Double carbonOffset) {
}