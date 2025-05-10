package org.example.server.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.example.server.dto.PanelTransactionDTO;
import org.example.server.dto.PaymentRequestDTO;
import org.example.server.dto.PaymentResponseDTO;
import org.example.server.entity.Country;
import org.example.server.entity.CountryPanel;
import org.example.server.entity.Panel;
import org.example.server.entity.Payment;
import org.example.server.entity.User;
import org.example.server.exception.PaymentException;
import org.example.server.repository.CountryPanelRepository;
import org.example.server.repository.PaymentRepository;
import org.example.server.repository.UserRepository;
import org.example.server.service.Payment.PaymentServiceImpl;
import org.example.server.service.panel.PanelTransactionServiceImpl;
import org.example.server.utils.PaymentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

/**
 * @author: astidhiyaa
 * @date: 08/05/25
 */
@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CountryPanelRepository countryPanelRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PanelTransactionServiceImpl transactionService;

    @Mock
    private PaymentIntent paymentIntent;

    @Mock
    private Charge charge;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    // This will hold our mockStatic for PaymentIntent
    private MockedStatic<PaymentIntent> mockedPaymentIntent;

    // This will hold our mockStatic for Charge
    private MockedStatic<Charge> mockedCharge;

    private Panel buildPanel() {
        return Panel.builder()
                .name("SolarMax Pro")
                .installationCost(1200.0)
                .productionPerPanel(350.0)
                .description("High efficiency panel")
                .build();
    }

    private Country buildCountry(String code) {
        return Country.builder()
                .code(code)
                .name("Country " + code)
                .population(1_000_000L)
                .build();
    }

    private User buildUser() {
        return new User("test@gmail.com", "password", "User", 1);
    }

    private CountryPanel createTestCountryPanel(Country country, Panel panel) {
        return CountryPanel
                .createCountryPanel(country, panel);
    }

    private PaymentRequestDTO createTestPaymentRequest() {
        return new PaymentRequestDTO(
                1L,
                1L,
                2,
                PaymentType.Credit,
                "card");
    }

    @BeforeEach
    void setUp() {
        // Initialize mocked statics
        mockedPaymentIntent = mockStatic(PaymentIntent.class);
        mockedCharge = mockStatic(Charge.class);

        // Mock Stripe API key (but don't use real key in tests)
        Stripe.apiKey = "test_key";
    }

    @AfterEach
    void tearDown() {
        // Close the static mocks to prevent memory leaks
        if (mockedPaymentIntent != null) {
            mockedPaymentIntent.close();
        }
        if (mockedCharge != null) {
            mockedCharge.close();
        }
    }

    @Test
    void processPayment_ShouldSuccessfullyProcessPayment() throws StripeException, PaymentException {
        // given
        User user = buildUser();
        Country country = buildCountry("US");
        Panel panel = buildPanel();
        CountryPanel countryPanel = createTestCountryPanel(country, panel);
        PaymentRequestDTO request = createTestPaymentRequest();
        String receiptUrl = "receipt.com";

        // Mock repository responses
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(countryPanelRepository.findById(anyLong())).thenReturn(Optional.of(countryPanel));

        // Mock Stripe API responses
        mockedPaymentIntent.when(() -> PaymentIntent.create(any(PaymentIntentCreateParams.class)))
                .thenReturn(paymentIntent);
        mockedCharge.when(() -> Charge.retrieve(anyString())).thenReturn(charge);

        // Mock PaymentIntent and Charge behavior
        when(paymentIntent.getStatus()).thenReturn("succeeded");
        when(paymentIntent.getId()).thenReturn("pi_test123");
        when(paymentIntent.getLatestCharge()).thenReturn("ch_test123");
        when(charge.getReceiptUrl()).thenReturn(receiptUrl);

        // Mock payment repository save
        when(paymentRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        PaymentResponseDTO result = paymentService.processPayment(request);

        // then
        assertNotNull(result);
        assertEquals("pi_test123", result.transactionId());
        assertEquals(receiptUrl, result.receiptUrl());

        verify(userRepository).findById(request.userId());
        verify(countryPanelRepository).findById(request.countryPanelId());
        verify(paymentRepository).save(any(Payment.class));
        verify(transactionService).save(any(PanelTransactionDTO.class));
    }

    @Test
    void processPayment_ShouldThrowWhenUserNotFound() {
        // Given
        PaymentRequestDTO request = createTestPaymentRequest();
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        PaymentException exception = assertThrows(PaymentException.class,
                () -> paymentService.processPayment(request));

        assertEquals("User not found with ID: " + request.userId(), exception.getMessage());
    }

    @Test
    void processPayment_ShouldThrowWhenCountryPanelNotFound() {
        // given
        User user = buildUser();
        PaymentRequestDTO request = createTestPaymentRequest();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(countryPanelRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when and then
        PaymentException exception = assertThrows(PaymentException.class,
                () -> paymentService.processPayment(request));

        assertEquals("Country panel not found with ID: " + request.countryPanelId(), exception.getMessage());
    }

    @Test
    void processPayment_ShouldThrowWhenPaymentFails() throws StripeException {
        // Given
        User user = buildUser();
        Country country = buildCountry("US");
        Panel panel = buildPanel();
        CountryPanel countryPanel = createTestCountryPanel(country, panel);
        PaymentRequestDTO request = createTestPaymentRequest();

        String expectedErrorMessage = "Card declined";
        // Use a mock to create a StripeException since it can't be directly
        // instantiated
        StripeException stripeException = mock(StripeException.class);
        when(stripeException.getMessage()).thenReturn(expectedErrorMessage);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(countryPanelRepository.findById(anyLong())).thenReturn(Optional.of(countryPanel));

        // Simulate Stripe throwing an exception
        mockedPaymentIntent.when(() -> PaymentIntent.create(any(PaymentIntentCreateParams.class)))
                .thenThrow(stripeException);

        // When & Then
        PaymentException exception = assertThrows(PaymentException.class,
                () -> paymentService.processPayment(request));

        assertEquals(expectedErrorMessage, exception.getMessage());
    }

    @Test
    void processPayment_ShouldThrowWhenPaymentNotSucceeded() throws StripeException {
        // Given
        User user = buildUser();
        Country country = buildCountry("US");
        Panel panel = buildPanel();
        CountryPanel countryPanel = createTestCountryPanel(country, panel);
        PaymentRequestDTO request = createTestPaymentRequest();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(countryPanelRepository.findById(anyLong())).thenReturn(Optional.of(countryPanel));

        // Mock PaymentIntent creation with a non-successful status
        mockedPaymentIntent.when(() -> PaymentIntent.create(any(PaymentIntentCreateParams.class)))
                .thenReturn(paymentIntent);
        when(paymentIntent.getStatus()).thenReturn("failed");

        // When & Then
        PaymentException exception = assertThrows(PaymentException.class,
                () -> paymentService.processPayment(request));

        assertEquals("Payment processing failed with status: failed", exception.getMessage());
    }

    @Test
    void getUserPayments_ShouldThrowWhenUserNotFound() {
        // Given
        Long userId = 1L;
        when(userRepository.existsById(anyLong())).thenReturn(false);

        // when and then
        PaymentException exception = assertThrows(PaymentException.class,
                () -> paymentService.getUserPayments(userId));

        assertEquals("User not found with ID: " + userId, exception.getMessage());
    }

    @Test
    void getUserPayments_ShouldThrowNotImplemented() {
        // given
        Long userId = 1L;
        when(userRepository.existsById(anyLong())).thenReturn(true);

        // when and then
        PaymentException exception = assertThrows(PaymentException.class,
                () -> paymentService.getUserPayments(userId));

        assertEquals("Unimplemented method 'getUserPayments'", exception.getMessage());
    }
}