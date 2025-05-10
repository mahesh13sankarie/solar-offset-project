package org.example.server.controller;

import org.example.server.dto.PanelTransactionDTO;
import org.example.server.dto.SolarPanelDTO;
import org.example.server.dto.StaffTransactionDTO;
import org.example.server.entity.PanelTransaction;
import org.example.server.entity.Payment;
import org.example.server.entity.response.PanelTransactionResponse;
import org.example.server.service.panel.PanelTransactionService;
import org.example.server.utils.PaymentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: astidhiyaa
 * @date: 24/03/25
 */

@RequestMapping("api/v1/transaction")
@RestController
public class PanelTransactionController {
    // use SolarPanelDto for showing!

    @Autowired
    private PanelTransactionService panelTransactionService;

    @PostMapping("/add")
    public ResponseEntity<?> addTransaction(@RequestBody PanelTransactionDTO panelTransaction) {
        panelTransactionService.save(panelTransaction);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllTransaction() {
        return ResponseEntity.ok().body(filteredResponse(panelTransactionService.fetchAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTransactionByUserId(@PathVariable Long id) {
        return ResponseEntity.ok().body(filteredResponse(panelTransactionService.fetchById(id)));
    }

    @GetMapping("/staff")
    public ResponseEntity<?> getStaffTransactionHistory() {
        List<StaffTransactionDTO> transactions = panelTransactionService.getStaffTransactionHistory();
        return ResponseEntity.ok().body(transactions);
    }

    private List<PanelTransactionResponse> filteredResponse(List<PanelTransaction> panelTransactions) {
        return panelTransactions.stream()
                .map(panelTransaction -> new PanelTransactionResponse(
                                panelTransaction.getId(),
                                panelTransaction.getUser().getDetail(panelTransaction.getUser()),
                                SolarPanelDTO.builder()
                                        .panelName(panelTransaction.getPayment().getCountryPanel().getPanel().getName())
                                        .installationCost(panelTransaction.getPayment().getCountryPanel().getPanel().getInstallationCost())
                                        .productionPerPanel(panelTransaction.getPayment().getCountryPanel().getPanel().getProductionPerPanel())
                                        .description(panelTransaction.getPayment().getCountryPanel().getPanel().getDescription())
                                        .efficiency(panelTransaction.getPayment().getCountryPanel().getPanel().getEfficiency())
                                        .lifespan(panelTransaction.getPayment().getCountryPanel().getPanel().getLifespan())
                                        .temperatureTolerance(panelTransaction.getPayment().getCountryPanel().getPanel().getTemperatureTolerance())
                                        .warranty(panelTransaction.getPayment().getCountryPanel().getPanel().getWarranty())
                                        .countryCode(panelTransaction.getPayment().getCountryPanel().getCountry().getCode())
                                        .id(panelTransaction.getPayment().getCountryPanel().getPanel().getId())
                                        .build(),
                                Payment.builder()
                                        .transactionId(String.valueOf(panelTransaction.getId()))
                                        .amount(panelTransaction.getPayment().getAmount())
                                        .type(PaymentType.valueOf(panelTransaction.getPayment().getType()))
                                        .build()
                        )

                )
                .collect(Collectors.toList());
    }
}
