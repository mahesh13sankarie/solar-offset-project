package org.example.server.service.Payment;

import java.math.BigDecimal;
import java.util.List;

import org.example.server.dto.PaymentRequestDTO;
import org.example.server.dto.PaymentResponseDTO;
import org.example.server.entity.CountryPanel;
import org.example.server.entity.Payment;
import org.example.server.entity.User;
import org.example.server.repository.CountryPanelRepository;
import org.example.server.repository.PaymentRepository;
import org.example.server.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

import lombok.RequiredArgsConstructor;

/**
 * PaymentService Implementation
 * 
 * Stripe recommended payment processing flow:
 * 1. Client-side: Collect card information using Stripe.js and generate a
 * PaymentMethod ID
 * 2. Server-side: Process only the PaymentMethod ID received from the client
 * 3. Card information is processed only on the client-side and never sent to
 * the server
 * 
 * Reference documentation:
 * - https://docs.stripe.com/payments/accept-a-payment-synchronously
 */
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

	private final UserRepository userRepository;

	private final CountryPanelRepository countryPanelRepository;

	private final PaymentRepository paymentRepository;

	/**
	 * Process a payment.
	 * 
	 * Following Stripe's recommended approach, this method uses the PaymentMethod
	 * ID directly
	 * without a separate authentication process.
	 * 
	 * How to test in the backend:
	 * 1. You can use Stripe's test PaymentMethod IDs:
	 * - pm_card_visa: Test successful Visa card payment
	 * - pm_card_visa_chargeDeclined: Test payment decline
	 * - pm_card_authenticationRequired: Test authentication required
	 * 
	 * Test example:
	 * curl -X POST http://localhost:8000/api/payments \
	 * -H "Content-Type: application/json" \
	 * -d '{"userId": 1, "countryPanelId": 1, "amount": 100, "type": "STRIPE",
	 * "paymentMethodId": "pm_card_visa"}'
	 * 
	 * @param request Payment request information
	 * @return Payment response
	 * @throws StripeException Stripe API exception
	 */
	@Override
	@Transactional
	public PaymentResponseDTO processPayment(PaymentRequestDTO request) throws StripeException {
		// Find the user and countryPanel
		User user = userRepository.findById(request.userId())
				.orElseThrow(() -> new IllegalArgumentException("User not found"));

		CountryPanel countryPanel = countryPanelRepository.findById(request.countryPanelId())
				.orElseThrow(() -> new IllegalArgumentException("Country Panel not found"));

		// Validate payment method ID
		if (request.paymentMethodId() == null || request.paymentMethodId().isEmpty()) {
			return new PaymentResponseDTO(
					null,
					null,
					null,
					false,
					"Missing or invalid payment method ID. A valid PaymentMethod ID must be provided.");
		}

		// Amount : installation cost * amount
		BigDecimal totalCostInPounds = request.amount()
				.multiply(BigDecimal.valueOf(countryPanel.getPanel().getInstallationCost()));
		long amountInSmallestUnit = totalCostInPounds.movePointRight(2).longValueExact();

		try {
			// Create a PaymentIntent with the order amount, currency and payment method
			PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
					.setAmount(amountInSmallestUnit)
					.setCurrency("gbp")
					.setPaymentMethod(request.paymentMethodId())
					.setConfirm(true)
					.build();

			PaymentIntent paymentIntent = PaymentIntent.create(params);

			if ("succeeded".equals(paymentIntent.getStatus())) {
				String receiptUrl = "https://dashboard.stripe.com/payments/" + paymentIntent.getId();

				// Save payment record
				Payment payment = Payment.builder()
						.user(user)
						.countryPanel(countryPanel)
						.amount(request.amount())
						.type(request.type())
						.transactionId(paymentIntent.getId())
						.receipUrl(receiptUrl)
						.build();

				Payment savedPayment = paymentRepository.save(payment);

				// Return success response
				return new PaymentResponseDTO(
						savedPayment.getId(),
						paymentIntent.getId(),
						receiptUrl,
						true,
						null);
			} else {
				return new PaymentResponseDTO(
						null,
						paymentIntent.getId(),
						null,
						false,
						"Payment failed: " + paymentIntent.getStatus());
			}
		} catch (StripeException e) {
			// Handle payment failure
			return new PaymentResponseDTO(
					null,
					null,
					null,
					false,
					"Error processing payment: " + e.getMessage());
		}
	}

	@Override
	public List<PaymentResponseDTO> getUserPayments(Long userId) {
		// Implementation to be added
		throw new UnsupportedOperationException("Unimplemented method 'getUserPayments'");
	}

}
