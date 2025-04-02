package org.example.server.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.example.server.utils.PaymentType;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "payments")
public class Payment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "country_panel_id")
	private CountryPanel countryPanel;

	@Column(nullable = false)
	private BigDecimal amount;

	@Column(nullable = false)
	private String type;

	@Column(name = "transaction_id")
	private String transactionId;

	@Column
	private String receipUrl;

	@Column
	private LocalDateTime createdAt;

	@Column
	private LocalDateTime updatedAt;

	@Builder
	public Payment(User user, CountryPanel countryPanel, BigDecimal amount, PaymentType type, String transactionId,
			String status, String receipUrl) {
		this.user = user;
		this.countryPanel = countryPanel;
		this.amount = amount;

		// https://www.linkedin.com/pulse/why-should-you-avoid-using-enums-mysql-eyad-mohammed-osama/
		this.type = type.toString();
		this.transactionId = transactionId;
		this.receipUrl = receipUrl;
		this.updatedAt = LocalDateTime.now();
		this.createdAt = LocalDateTime.now();
	}

}