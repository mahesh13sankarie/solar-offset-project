package org.example.server.controller;

import java.util.List;

import org.example.server.dto.PanelCreateRequestDTO;
import org.example.server.dto.SolarPanelDTO;
import org.example.server.service.panel.PanelService;
import org.example.server.utils.ApiResponse;
import org.example.server.utils.ApiResponseGenerator;
import org.example.server.utils.Error;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/panels")
@RequiredArgsConstructor
public class PanelController {

    private final PanelService panelService;

    @GetMapping
    public ApiResponse<ApiResponse.CustomBody<List<SolarPanelDTO>>> getPanelByZone(
            @RequestParam(required = false) String code) {
        try {
            List<SolarPanelDTO> panels = (code == null || code.isEmpty())
                    ? panelService.getAllPanels()
                    : panelService.getPanelByZone(code);
            return ApiResponseGenerator.success(HttpStatus.OK, panels);
        } catch (Exception e) {
            return failForPanelList("PANEL_ERROR", "Failed to retrieve panels", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get panel details by ID
    @GetMapping("/{id}")
    public ApiResponse<ApiResponse.CustomBody<SolarPanelDTO>> getPanelById(@PathVariable Long id) {
        try {
            SolarPanelDTO panel = panelService.getPanelById(id);
            return ApiResponseGenerator.success(HttpStatus.OK, panel);
        } catch (Exception e) {
            return failForPanel("PANEL_NOT_FOUND", "Panel not found with id: " + id, HttpStatus.NOT_FOUND);
        }
    }

    // Create new panel
    @PostMapping
    public ApiResponse<ApiResponse.CustomBody<List<SolarPanelDTO>>> createPanel(
            @RequestBody PanelCreateRequestDTO panelRequest) {
        try {
            if (panelRequest.getCountryCodes() == null || panelRequest.getCountryCodes().isEmpty()) {
                return failForPanelList("INVALID_REQUEST", "Country codes must not be empty", HttpStatus.BAD_REQUEST);
            }
            List<SolarPanelDTO> createdPanels = panelService.createPanel(panelRequest);
            return ApiResponseGenerator.success(HttpStatus.OK, createdPanels);
        } catch (Exception e) {
            return failForPanelList("PANEL_CREATE_ERROR", "Failed to create panel", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Delete panel by ID
    @DeleteMapping("/{id}")
    public ApiResponse<ApiResponse.CustomBody<Object>> deletePanel(@PathVariable Long id) {
        try {
            panelService.deletePanel(id);
            return ApiResponseGenerator.success(HttpStatus.OK);
        } catch (Exception e) {
            return ApiResponseGenerator.fail("PANEL_DELETE_ERROR", "Failed to delete panel with id: " + id,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Update panel (full update)
    @PutMapping("/{id}")
    public ApiResponse<ApiResponse.CustomBody<Object>> updatePanel(@PathVariable Long id,
            @RequestBody PanelCreateRequestDTO panelRequest) {
        try {
            panelService.updatePanel(id, panelRequest);
            return ApiResponseGenerator.success(HttpStatus.OK);
        } catch (Exception e) {
            return ApiResponseGenerator.fail("PANEL_UPDATE_ERROR", "Failed to update panel with id: " + id,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Helper method for creating typed error responses for panel list
    private ApiResponse<ApiResponse.CustomBody<List<SolarPanelDTO>>> failForPanelList(String code, String message,
            HttpStatus status) {
        return new ApiResponse<>(
                new ApiResponse.CustomBody<>(false, null, new Error(code, message, status.toString())),
                status);
    }

    // Helper method for creating typed error responses for single panel
    private ApiResponse<ApiResponse.CustomBody<SolarPanelDTO>> failForPanel(String code, String message,
            HttpStatus status) {
        return new ApiResponse<>(
                new ApiResponse.CustomBody<>(false, null, new Error(code, message, status.toString())),
                status);
    }
}
