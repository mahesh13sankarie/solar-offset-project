package org.example.server.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.example.server.dto.PanelTransactionDTO;
import org.example.server.dto.SolarPanelDTO;
import org.example.server.dto.StaffTransactionDTO;
import org.example.server.entity.PanelTransaction;
import org.example.server.entity.Payment;
import org.example.server.entity.response.PanelTransactionResponse;
import org.example.server.service.panel.PanelTransactionService;
import org.example.server.utils.ApiResponse;
import org.example.server.utils.ApiResponseGenerator;
import org.example.server.utils.Error;
import org.example.server.utils.PaymentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ApiResponse<ApiResponse.CustomBody<Object>> addTransaction(
            @RequestBody PanelTransactionDTO panelTransaction) {
        try {
            validateTransactionRequest(panelTransaction);
            panelTransactionService.save(panelTransaction);
            return ApiResponseGenerator.success(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ApiResponseGenerator.fail("VALIDATION_ERROR", e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ApiResponseGenerator.fail("SERVER_ERROR", "An unexpected error occurred",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    public ApiResponse<ApiResponse.CustomBody<List<PanelTransactionResponse>>> getAllTransaction() {
        try {
            List<PanelTransactionResponse> transactions = filteredResponse(panelTransactionService.fetchAll());
            return ApiResponseGenerator.success(HttpStatus.OK, transactions);
        } catch (Exception e) {
            return failForTransactionList("SERVER_ERROR", "Failed to fetch transactions",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ApiResponse<ApiResponse.CustomBody<List<PanelTransactionResponse>>> getTransactionByUserId(
            @PathVariable Long id) {
        try {
            List<PanelTransactionResponse> transactions = filteredResponse(panelTransactionService.fetchById(id));
            return ApiResponseGenerator.success(HttpStatus.OK, transactions);
        } catch (Exception e) {
            return failForTransactionList("SERVER_ERROR", "Failed to fetch transactions for user: " + id,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/staff")
    public ApiResponse<ApiResponse.CustomBody<List<StaffTransactionDTO>>> getStaffTransactionHistory() {
        try {
            List<StaffTransactionDTO> transactions = panelTransactionService.getStaffTransactionHistory();
            return ApiResponseGenerator.success(HttpStatus.OK, transactions);
        } catch (Exception e) {
            return failForStaffTransactionList("SERVER_ERROR", "Failed to fetch staff transaction history",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private List<PanelTransactionResponse> filteredResponse(List<PanelTransaction> panelTransactions) {
        return panelTransactions.stream()
                .map(panelTransaction -> new PanelTransactionResponse(
                        panelTransaction.getId(),
                        panelTransaction.getUser().getDetail(panelTransaction.getUser()),
                        SolarPanelDTO.builder()
                                .panelName(panelTransaction.getPayment().getCountryPanel().getPanel().getName())
                                .installationCost(panelTransaction.getPayment().getCountryPanel().getPanel()
                                        .getInstallationCost())
                                .productionPerPanel(panelTransaction.getPayment().getCountryPanel().getPanel()
                                        .getProductionPerPanel())
                                .description(
                                        panelTransaction.getPayment().getCountryPanel().getPanel().getDescription())
                                .efficiency(panelTransaction.getPayment().getCountryPanel().getPanel().getEfficiency())
                                .lifespan(panelTransaction.getPayment().getCountryPanel().getPanel().getLifespan())
                                .temperatureTolerance(panelTransaction.getPayment().getCountryPanel().getPanel()
                                        .getTemperatureTolerance())
                                .warranty(panelTransaction.getPayment().getCountryPanel().getPanel().getWarranty())
                                .countryCode(panelTransaction.getPayment().getCountryPanel().getCountry().getCode())
                                .id(panelTransaction.getPayment().getCountryPanel().getPanel().getId())
                                .build(),
                        Payment.builder()
                                .transactionId(String.valueOf(panelTransaction.getId()))
                                .amount(panelTransaction.getPayment().getAmount())
                                .type(PaymentType.valueOf(panelTransaction.getPayment().getType()))
                                .build()))
                .collect(Collectors.toList());
    }

    private void validateTransactionRequest(PanelTransactionDTO request) {
        if (request.userId() == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }
        if (request.panelId() == null) {
            throw new IllegalArgumentException("Panel ID must not be null");
        }
        if (request.paymentId() == null) {
            throw new IllegalArgumentException("Payment ID must not be null");
        }
    }

    // Helper method for creating typed error responses for transaction lists
    private ApiResponse<ApiResponse.CustomBody<List<PanelTransactionResponse>>> failForTransactionList(
            String code, String message, HttpStatus status) {
        return new ApiResponse<>(
                new ApiResponse.CustomBody<>(false, null, new Error(code, message, status.toString())),
                status);
    }

    // Helper method for creating typed error responses for staff transaction lists
    private ApiResponse<ApiResponse.CustomBody<List<StaffTransactionDTO>>> failForStaffTransactionList(
            String code, String message, HttpStatus status) {
        return new ApiResponse<>(
                new ApiResponse.CustomBody<>(false, null, new Error(code, message, status.toString())),
                status);
    }
}
