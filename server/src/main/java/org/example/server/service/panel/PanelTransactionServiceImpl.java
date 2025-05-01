package org.example.server.service.panel;

import org.example.server.dto.PanelTransactionDTO;
import org.example.server.dto.StaffTransactionDTO;
import org.example.server.entity.*;
import org.example.server.repository.CountryPanelRepository;
import org.example.server.repository.PanelTransactionRepository;
import org.example.server.repository.PaymentRepository;
import org.example.server.repository.UserRepository;
import org.example.server.exception.PanelTransactionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author: astidhiyaa
 * @date: 24/03/25
 */

@Service
public class PanelTransactionServiceImpl implements PanelTransactionService {
    @Autowired
    private PanelTransactionRepository panelTransactionRepository;

    @Autowired
    private CountryPanelRepository panelRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public PanelTransaction save(PanelTransactionDTO panelTransaction) {
        CountryPanel panel = panelRepository.findById(panelTransaction.panelId())
                .orElseThrow(() -> PanelTransactionException.panelNotFound(panelTransaction.panelId()));

        User user = userRepository.findById(panelTransaction.userId())
                .orElseThrow(() -> PanelTransactionException.userNotFound(panelTransaction.userId()));

        List<Payment> payments = paymentRepository.findByUserId(panelTransaction.userId());
        Payment payment = payments.stream()
                .filter(p -> Objects.equals(p.getUser().getId(), user.getId()))
                .filter(p -> Objects.equals(p.getId(), panelTransaction.paymentId()))
                .findFirst()
                .orElseThrow(
                        () -> PanelTransactionException.panelNotFound(panelTransaction.panelId())
                );
        PanelTransaction newTransaction = new PanelTransaction(user, panel, payment);
        return panelTransactionRepository.save(newTransaction);
    }

    @Override
    public List<PanelTransaction> fetchAll() {
        return panelTransactionRepository.findAll();
    }

    @Override
    public List<PanelTransaction> fetchById(Long id) {
        return panelTransactionRepository.findByUserId(id);
    }

    @Override
    public List<StaffTransactionDTO> getStaffTransactionHistory() {
        List<Payment> payments = paymentRepository.findAll();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return payments.stream()
                .map(payment -> {
                    // Get user details
                    User user = payment.getUser();
                    String userEmail = user.getEmail();

                    // Get panel and country details through CountryPanel
                    Panel panel = payment.getCountryPanel().getPanel();
                    String panelType = panel.getName();
                    String country = payment.getCountryPanel().getCountry().getName();

                    // Get payment date
                    LocalDateTime date = payment.getCreatedAt();

                    // Get number of panels purchased
                    Integer panelQuantity = payment.getAmount();

                    // Calculate total price (quantity * installation cost)
                    Integer totalPrice = panelQuantity * panel.getInstallationCost().intValue();

                    // Calculate carbon offset based on panel efficiency and quantity
                    // This is a simple estimation based on panel production
                    double productionPerPanel = panel.getProductionPerPanel();
                    double carbonOffsetValue = panelQuantity * productionPerPanel * 0.7; // Assuming 0.7 tons CO2 offset
                                                                                         // per
                    // kW

                    return new StaffTransactionDTO(
                            date.format(dateFormatter),
                            userEmail,
                            country,
                            panelType,
                            panelQuantity,
                            totalPrice,
                            carbonOffsetValue);
                })
                .collect(Collectors.toList());
    }
}
