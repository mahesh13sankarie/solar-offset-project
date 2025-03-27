package org.example.server.controller;

import org.example.server.dto.PanelTransactionDTO;
import org.example.server.dto.SolarPanelDTO;
import org.example.server.entity.PanelTransaction;
import org.example.server.entity.response.PanelTransactionResponse;
import org.example.server.service.panel.PanelTransactionService;
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
    //use SolarPanelDto for showing!

    @Autowired
    private PanelTransactionService panelTransactionService;

    @PostMapping("/add")
    public ResponseEntity<?> addTransaction(@RequestBody PanelTransactionDTO panelTransaction) {
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

    private List<PanelTransactionResponse> filteredResponse(List<PanelTransaction> panelTransactions) {
        return panelTransactions.stream()
                .map(panelTransaction -> new PanelTransactionResponse(
                        panelTransaction.getId(),
                        panelTransaction.getUser().getDetail(panelTransaction.getUser()),
                        SolarPanelDTO.builder()
                                .panelName(panelTransaction.getPanel().getName())
                                .installationCost(panelTransaction.getPanel().getInstallationCost())
                                .productionPerPanel(panelTransaction.getPanel().getProductionPerPanel())
                                .description(panelTransaction.getPanel().getDescription())
                                .efficiency(panelTransaction.getPanel().getEfficiency())
                                .lifespan(panelTransaction.getPanel().getLifespan())
                                .temperatureTolerance(panelTransaction.getPanel().getTemperatureTolerance())
                                .warranty(panelTransaction.getPanel().getWarranty())
                                .build()
                ))
                .collect(Collectors.toList());
    }
}
