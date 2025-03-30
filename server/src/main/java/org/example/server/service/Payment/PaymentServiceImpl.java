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

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

	private final UserRepository userRepository;

	private final CountryPanelRepository countryPanelRepository;

	private final PaymentRepository paymentRepository;

	@Override
	@Transactional
	public PaymentResponseDTO processPayment(PaymentRequestDTO request) throws StripeException {
		// Find the user and countryPanel
		User user = userRepository.findById(request.userId())
				.orElseThrow(() -> new IllegalArgumentException("User not found"));

		CountryPanel countryPanel = countryPanelRepository.findById(request.countryPanelId())
				.orElseThrow(() -> new IllegalArgumentException("Country Panel not found"));

		// Amount : installation cost * amount
		BigDecimal totalCostInPounds = request.amount()
				.multiply(BigDecimal.valueOf(countryPanel.getPanel().getInstallationCost()));
		long amountInSmallestUnit = totalCostInPounds.movePointRight(2).longValueExact();

		try {
			// Create a PaymentIntent with the order amount and currency
			PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
					.setAmount(amountInSmallestUnit)
					.setCurrency("gbp") // Set currency to GBP
					.setAutomaticPaymentMethods(
							PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
									.setEnabled(true)
									.build())
					.build();

			PaymentIntent paymentIntent = PaymentIntent.create(params);

			// Generate a receipt URL - normally this would come from Stripe
			// For now we'll use a simple placeholder
			String receiptUrl = "https://dashboard.stripe.com/payments/" + paymentIntent.getId();

			// Save the payment record
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
		} catch (StripeException e) {
			// Handle payment failure
			return new PaymentResponseDTO(
					null,
					null,
					null,
					false,
					e.getMessage());
		}
	}

	@Override
	public List<PaymentResponseDTO> getUserPayments(Long userId) {
		// Implementation to be added
		throw new UnsupportedOperationException("Unimplemented method 'getUserPayments'");
	}
}
