package org.example.server.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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

	@Builder
	public Payment(User user, CountryPanel countryPanel, BigDecimal amount, String type, String transactionId,
			String status, String receipUrl) {
		this.user = user;
		this.countryPanel = countryPanel;
		this.amount = amount;
		this.type = type;
		this.transactionId = transactionId;
		this.receipUrl = receipUrl;
		this.updatedAt = LocalDateTime.now();
		this.createdAt = LocalDateTime.now();
	}

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
