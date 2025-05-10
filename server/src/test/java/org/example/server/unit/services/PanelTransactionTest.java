package org.example.server.unit.services;

import org.example.server.dto.PanelTransactionDTO;
import org.example.server.dto.StaffTransactionDTO;
import org.example.server.entity.*;
import org.example.server.exception.PanelTransactionException;
import org.example.server.repository.CountryPanelRepository;
import org.example.server.repository.PanelTransactionRepository;
import org.example.server.repository.PaymentRepository;
import org.example.server.repository.UserRepository;
import org.example.server.service.panel.PanelTransactionServiceImpl;
import org.example.server.utils.PaymentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * @author: astidhiyaa
 * @date: 08/05/25
 */
@ExtendWith(MockitoExtension.class)
public class PanelTransactionTest {
    @InjectMocks
    private PanelTransactionServiceImpl panelTransactionService;

    @Mock
    private PanelTransactionRepository panelTransactionRepository;

    @Mock
    private CountryPanelRepository panelRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Test
    void test_save_success() {
        // given
        CountryPanel countryPanel = buildCountryPanel(buildCountry("GB"), buildPanel());
        User user = buildUser();
        Payment payment = buildPayment();
        PanelTransaction panelTransaction = buildPanelTransaction(user, countryPanel, payment);
        PanelTransactionDTO panelTransactionDTO = buildPanelTransactionDTO();

        modifyId(user, 1L);
        modifyId(payment, 1L);
        modifyId(payment.getUser(), 1L);

        when(panelRepository.findById(anyLong())).thenReturn(Optional.of(countryPanel));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(paymentRepository.findByUserId(anyLong())).thenReturn(List.of(payment));
        when(panelTransactionRepository.save(any(PanelTransaction.class))).thenReturn(panelTransaction);

        // when
        PanelTransaction result = panelTransactionService.save(panelTransactionDTO);

        // then
        assertNotNull(result);
        assertEquals(user.getId(), result.getUser().getId());
        assertEquals(countryPanel.getId(), result.getPanel().getId());
        assertEquals(payment.getId(), result.getPayment().getId());

        verify(panelRepository).findById(panelTransactionDTO.panelId());
        verify(userRepository).findById(panelTransactionDTO.userId());
        verify(paymentRepository).findByUserId(panelTransactionDTO.userId());
        verify(panelTransactionRepository).save(any(PanelTransaction.class));
    }

    @Test
    void test_save_panel_notfound() {
        // given
        PanelTransactionDTO panelTransactionDTO = buildPanelTransactionDTO();
        when(panelRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when, then
        assertThrows(PanelTransactionException.class, () -> panelTransactionService.save(panelTransactionDTO));

        verify(panelRepository).findById(panelTransactionDTO.panelId());
        verify(userRepository, never()).findById(anyLong());
        verify(paymentRepository, never()).findByUserId(anyLong());
        verify(panelTransactionRepository, never()).save(any(PanelTransaction.class));
    }

    @Test
    void test_save_user_notfound() {
        // given
        CountryPanel countryPanel = buildCountryPanel(buildCountry("GB"), buildPanel());
        PanelTransactionDTO panelTransactionDTO = buildPanelTransactionDTO();
        when(panelRepository.findById(anyLong())).thenReturn(Optional.of(countryPanel));
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when and then
        assertThrows(PanelTransactionException.class, () -> panelTransactionService.save(panelTransactionDTO));

        verify(panelRepository).findById(panelTransactionDTO.panelId());
        verify(userRepository).findById(panelTransactionDTO.userId());
        verify(paymentRepository, never()).findByUserId(anyLong());
        verify(panelTransactionRepository, never()).save(any(PanelTransaction.class));
    }

    @Test
    void test_save_payment_notfound() {
        // given
        CountryPanel countryPanel = buildCountryPanel(buildCountry("GB"), buildPanel());
        User user = buildUser();
        PanelTransactionDTO panelTransactionDTO = buildPanelTransactionDTO();

        when(panelRepository.findById(anyLong())).thenReturn(Optional.of(countryPanel));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(paymentRepository.findByUserId(anyLong())).thenReturn(List.of());

        // when and then
        assertThrows(PanelTransactionException.class, () -> panelTransactionService.save(panelTransactionDTO));

        verify(panelRepository).findById(panelTransactionDTO.panelId());
        verify(userRepository).findById(panelTransactionDTO.userId());
        verify(paymentRepository).findByUserId(panelTransactionDTO.userId());
        verify(panelTransactionRepository, never()).save(any(PanelTransaction.class));
    }


    @Test
    void test_fetch_all_success() {
        // given
        CountryPanel countryPanel = buildCountryPanel(buildCountry("GB"), buildPanel());
        User user = buildUser();
        Payment payment = buildPayment();
        PanelTransaction panelTransaction = buildPanelTransaction(user, countryPanel, payment);
        List<PanelTransaction> transactions = List.of(panelTransaction);

        when(panelTransactionRepository.findAll()).thenReturn(transactions);

        // when
        List<PanelTransaction> result = panelTransactionService.fetchAll();

        // then
        assertNotNull(result);
        assertEquals(1, result.size());

        verify(panelTransactionRepository).findAll();
    }

    @Test
    void test_fetchById_success() {
        // given
        Long userId = 1L;
        CountryPanel countryPanel = buildCountryPanel(buildCountry("GB"), buildPanel());
        User user = buildUser();
        Payment payment = buildPayment();
        PanelTransaction panelTransaction = buildPanelTransaction(user, countryPanel, payment);
        List<PanelTransaction> transactions = List.of(panelTransaction);

        when(panelTransactionRepository.findByUserId(userId)).thenReturn(transactions);

        // when
        List<PanelTransaction> result = panelTransactionService.fetchById(userId);

        // then
        assertNotNull(result);
        assertEquals(1, result.size());

        verify(panelTransactionRepository).findByUserId(userId);
    }

    @Test
    void test_getStaffTransactionHistory_success() {
        // given
        Payment payment = buildPayment();
        List<Payment> payments = List.of(payment);
        when(paymentRepository.findAll()).thenReturn(payments);

        // when
        List<StaffTransactionDTO> result = panelTransactionService.getStaffTransactionHistory();

        // then
        assertNotNull(result);
        assertEquals(1, result.size());

        StaffTransactionDTO dto = result.get(0);

        assertEquals(LocalDate.now().toString(), dto.date());
        assertEquals("test@gmail.com", dto.user());
        assertEquals("United Kingdom", dto.country());
        assertEquals("SolarMax Pro", dto.panelType());

        verify(paymentRepository).findAll();
    }


    private User buildUser() {
        User user = new User("test@gmail.com", "password", "User", 1);
        return user;
    }

    private CountryPanel buildCountryPanel(Country country, Panel panel) {
        return CountryPanel.createCountryPanel(country, panel);
    }

    private Panel buildPanel() {
        return Panel.builder()
                .name("SolarMax Pro")
                .installationCost(1200.0)
                .productionPerPanel(350.0)
                .description("High efficiency panel")
                .efficiency("22.5")
                .lifespan("25")
                .temperatureTolerance("-40")
                .warranty("10")
                .id(1L)
                .build();
    }

    private Country buildCountry(String countryCode) {
        return Country.builder()
                .code(countryCode)
                .name("United Kingdom")
                .population(1000_000_000L)
                .build();
    }

    private Payment buildPayment() {
        return Payment.builder()
                .transactionId("1")
                .type(PaymentType.Credit)
                .amount(100)
                .receiptUrl("http://example.com")
                .user(buildUser())
                .countryPanel(buildCountryPanel(buildCountry("GB"), buildPanel()))
                .build();
    }

    private PanelTransaction buildPanelTransaction(User user, CountryPanel countryPanel, Payment payment) {
        return new PanelTransaction(user, countryPanel, payment);
    }

    private PanelTransactionDTO buildPanelTransactionDTO() {
        return new PanelTransactionDTO(1L, 1L, 1L);
    }

    public static void modifyId(Object entity, Long id) {
        try {
            Field field = entity.getClass().getDeclaredField("id");
            field.setAccessible(true);
            field.set(entity, id);
        } catch (Exception e) {
            throw new RuntimeException("Test setup failed", e);
        }
    }
}
