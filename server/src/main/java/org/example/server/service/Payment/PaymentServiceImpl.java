package org.example.server.service.Payment;

import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.RequiredArgsConstructor;
import org.example.server.dto.PanelTransactionDTO;
import org.example.server.dto.PaymentRequestDTO;
import org.example.server.dto.PaymentResponseDTO;
import org.example.server.entity.CountryPanel;
import org.example.server.entity.Payment;
import org.example.server.entity.User;
import org.example.server.exception.PaymentException;
import org.example.server.repository.CountryPanelRepository;
import org.example.server.repository.PaymentRepository;
import org.example.server.repository.UserRepository;
import org.example.server.service.panel.PanelTransactionServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;


@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

	private final UserRepository userRepository;

	private final CountryPanelRepository countryPanelRepository;

	private final PaymentRepository paymentRepository;
	private final PanelTransactionServiceImpl transactionService;

	@Override
	@Transactional
	public PaymentResponseDTO processPayment(PaymentRequestDTO request) throws PaymentException {
		// Find the user and countryPanel
		User user = userRepository.findById(request.userId())
				.orElseThrow(() -> PaymentException.userNotFound(request.userId()));

		CountryPanel countryPanel = countryPanelRepository.findById(request.countryPanelId())
				.orElseThrow(() -> PaymentException.countryPanelNotFound(request.countryPanelId()));

		// Amount : installation cost * quantity
		BigDecimal totalCostInPounds = BigDecimal
				.valueOf(request.quantity() * countryPanel.getPanel().getInstallationCost());
		long amountInSmallestUnit = totalCostInPounds.movePointRight(2).longValueExact();

		try {
			// Create a PaymentIntent with the order quantity, currency and payment method
			PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
					.setAmount(amountInSmallestUnit)
					.setCurrency("gbp")
					.setPaymentMethod(request.paymentMethodId())
					.setReceiptEmail(user.getEmail())
					.setConfirm(true)
					.build();

			PaymentIntent paymentIntent = PaymentIntent.create(params);

			if ("succeeded".equals(paymentIntent.getStatus())) {
				Charge charge = Charge.retrieve(paymentIntent.getLatestCharge());
				String actualReceiptUrl = charge.getReceiptUrl();

				// Save payment record using the actual receipt URL
				Payment payment = Payment.builder()
						.user(user)
						.countryPanel(countryPanel)
						.amount(request.quantity())
						.type(request.paymentType())
						.transactionId(paymentIntent.getId())
						.receiptUrl(actualReceiptUrl)
						.build();
				Payment savedPayment = paymentRepository.save(payment);

				PanelTransactionDTO panelTransactionDTO = new PanelTransactionDTO(
						user.getId(),
						countryPanel.getPanel().getId());
				transactionService.save(panelTransactionDTO);

				return new PaymentResponseDTO(
						savedPayment.getId(),
						paymentIntent.getId(),
						actualReceiptUrl

				);
			} else {
				throw new PaymentException(
						PaymentException.PAYMENT_PROCESSING_ERROR,
						"Payment processing failed with status: " + paymentIntent.getStatus(),
						HttpStatus.BAD_REQUEST);
			}
		} catch (StripeException e) {
			// Handle payment failure
			throw PaymentException.fromStripeException(e);
		}
	}

	@Override
	public List<PaymentResponseDTO> getUserPayments(Long userId) throws PaymentException {
		// Check if user exists
		if (!userRepository.existsById(userId)) {
			throw PaymentException.userNotFound(userId);
		}

		// Implementation to be added
		throw new PaymentException(
				"SERVER-ERROR",
				"Unimplemented method 'getUserPayments'",
				HttpStatus.NOT_IMPLEMENTED);
	}
}
