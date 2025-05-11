package org.example.server.unit.controller;

import org.example.server.controller.PanelController;
import org.example.server.dto.PanelCreateRequestDTO;
import org.example.server.dto.SolarPanelDTO;
import org.example.server.service.panel.PanelService;
import org.example.server.utils.ApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class PanelControllerTest {

    private PanelController panelController;
    private PanelService panelService;

    @BeforeEach
    void setUp() {
        // Mock the service layer
        panelService = mock(PanelService.class);

        // Create controller with mocked dependency
        panelController = new PanelController(panelService);
    }

    @Test
    @DisplayName("HTTP 200 OK: Returns all panels when no country code is provided")
    void getPanelByZone_WithNoCode_ReturnsAllPanels() {
        // Arrange
        SolarPanelDTO panel1 = SolarPanelDTO.builder()
                .id(1L)
                .panelName("SunPower Maxeon 7DC 445W")
                .installationCost(400.00)
                .productionPerPanel(445.00)
                .description("Top-tier efficiency, extremely durable (40-year warranty)")
                .efficiency("22.8")
                .warranty("40 years")
                .countryCode("GB")
                .build();

        SolarPanelDTO panel2 = SolarPanelDTO.builder()
                .id(2L)
                .panelName("Project Solar Evo Max Super Series 480W")
                .installationCost(375.00)
                .productionPerPanel(480.00)
                .description("Great customer satisfaction (94.4%), lifetime warranty")
                .efficiency("22.3")
                .warranty("Lifetime")
                .countryCode("GB")
                .build();

        List<SolarPanelDTO> panels = Arrays.asList(panel1, panel2);

        when(panelService.getAllPanels()).thenReturn(panels);

        // Act
        ApiResponse<ApiResponse.CustomBody<List<SolarPanelDTO>>> response = panelController.getPanelByZone(null);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().getSuccess());
        assertNotNull(response.getBody().getResponse());
        assertEquals(2, response.getBody().getResponse().size());
        assertEquals("SunPower Maxeon 7DC 445W", response.getBody().getResponse().get(0).getPanelName());
        assertEquals("GB", response.getBody().getResponse().get(0).getCountryCode());
        assertEquals("Project Solar Evo Max Super Series 480W", response.getBody().getResponse().get(1).getPanelName());
    }

    @Test
    @DisplayName("HTTP 500 INTERNAL_SERVER_ERROR: Returns error when there's an exception fetching all panels")
    void getPanelByZone_WhenExceptionOccurs_ReturnsErrorResponse() {
        // Arrange
        when(panelService.getAllPanels()).thenThrow(new RuntimeException("Database error"));

        // Act
        ApiResponse<ApiResponse.CustomBody<List<SolarPanelDTO>>> response = panelController.getPanelByZone(null);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertFalse(response.getBody().getSuccess());
        assertNull(response.getBody().getResponse());
        assertNotNull(response.getBody().getError());
        assertEquals("PANEL_ERROR", response.getBody().getError().getCode());
        assertEquals("Failed to retrieve panels", response.getBody().getError().getMessage());
    }

    @Test
    @DisplayName("HTTP 200 OK: Returns panels for specific country code")
    void getPanelByZone_WithCode_ReturnsPanelsForCountry() {
        // Arrange
        String countryCode = "FR";

        SolarPanelDTO panel = SolarPanelDTO.builder()
                .id(3L)
                .panelName("AIKO ABC Neostar 3N54 495W")
                .installationCost(390.00)
                .productionPerPanel(495.00)
                .description("Highest production capacity on this list, advanced cell technology")
                .efficiency("23.2")
                .warranty("30 years")
                .countryCode(countryCode)
                .build();

        List<SolarPanelDTO> panels = Collections.singletonList(panel);

        when(panelService.getPanelByZone(countryCode)).thenReturn(panels);

        // Act
        ApiResponse<ApiResponse.CustomBody<List<SolarPanelDTO>>> response = panelController.getPanelByZone(countryCode);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().getSuccess());
        assertNotNull(response.getBody().getResponse());
        assertEquals(1, response.getBody().getResponse().size());
        assertEquals("AIKO ABC Neostar 3N54 495W", response.getBody().getResponse().get(0).getPanelName());
        assertEquals(countryCode, response.getBody().getResponse().get(0).getCountryCode());
    }

    @Test
    @DisplayName("HTTP 500 INTERNAL_SERVER_ERROR: Returns error when there's an exception fetching panels by zone")
    void getPanelByZone_WithCodeWhenExceptionOccurs_ReturnsErrorResponse() {
        // Arrange
        String countryCode = "FR";
        when(panelService.getPanelByZone(countryCode)).thenThrow(new RuntimeException("Database error"));

        // Act
        ApiResponse<ApiResponse.CustomBody<List<SolarPanelDTO>>> response = panelController.getPanelByZone(countryCode);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertFalse(response.getBody().getSuccess());
        assertNull(response.getBody().getResponse());
        assertNotNull(response.getBody().getError());
        assertEquals("PANEL_ERROR", response.getBody().getError().getCode());
        assertEquals("Failed to retrieve panels", response.getBody().getError().getMessage());
    }

    @Test
    @DisplayName("HTTP 200 OK: Returns panel details by ID")
    void getPanelById_ReturnsPanelDetails() {
        // Arrange
        Long panelId = 1L;

        SolarPanelDTO panel = SolarPanelDTO.builder()
                .id(panelId)
                .panelName("SunPower Maxeon 7DC 445W")
                .installationCost(400.00)
                .productionPerPanel(445.00)
                .description("Top-tier efficiency, extremely durable (40-year warranty)")
                .efficiency("22.8")
                .warranty("40 years")
                .countryCode("GB")
                .build();

        when(panelService.getPanelById(panelId)).thenReturn(panel);

        // Act
        ApiResponse<ApiResponse.CustomBody<SolarPanelDTO>> response = panelController.getPanelById(panelId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().getSuccess());
        assertNotNull(response.getBody().getResponse());
        assertEquals("SunPower Maxeon 7DC 445W", response.getBody().getResponse().getPanelName());
        assertEquals(panelId, response.getBody().getResponse().getId());
    }

    @Test
    @DisplayName("HTTP 404 NOT_FOUND: Returns error when panel with ID is not found")
    void getPanelById_WhenExceptionOccurs_ReturnsErrorResponse() {
        // Arrange
        Long panelId = 999L;
        when(panelService.getPanelById(panelId)).thenThrow(new RuntimeException("Panel not found"));

        // Act
        ApiResponse<ApiResponse.CustomBody<SolarPanelDTO>> response = panelController.getPanelById(panelId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertFalse(response.getBody().getSuccess());
        assertNull(response.getBody().getResponse());
        assertNotNull(response.getBody().getError());
        assertEquals("PANEL_NOT_FOUND", response.getBody().getError().getCode());
        assertEquals("Panel not found with id: " + panelId, response.getBody().getError().getMessage());
    }

    @Test
    @DisplayName("HTTP 200 OK: Successfully creates new panel")
    void createPanel_ReturnsCreatedPanels() {
        // Arrange
        PanelCreateRequestDTO requestDTO = new PanelCreateRequestDTO();
        requestDTO.setName("New Solar Panel");
        requestDTO.setInstallationCost(350.00);
        requestDTO.setProductionPerPanel(420.00);
        requestDTO.setDescription("New high-efficiency panel");
        requestDTO.setCountryCodes(Arrays.asList("GB", "TH"));

        SolarPanelDTO panel1 = SolarPanelDTO.builder()
                .id(10L)
                .panelName("New Solar Panel")
                .installationCost(350.00)
                .productionPerPanel(420.00)
                .description("New high-efficiency panel")
                .countryCode("GB")
                .build();

        SolarPanelDTO panel2 = SolarPanelDTO.builder()
                .id(11L)
                .panelName("New Solar Panel")
                .installationCost(350.00)
                .productionPerPanel(420.00)
                .description("New high-efficiency panel")
                .countryCode("TH")
                .build();

        List<SolarPanelDTO> createdPanels = Arrays.asList(panel1, panel2);

        when(panelService.createPanel(any(PanelCreateRequestDTO.class))).thenReturn(createdPanels);

        // Act
        ApiResponse<ApiResponse.CustomBody<List<SolarPanelDTO>>> response = panelController.createPanel(requestDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().getSuccess());
        assertNotNull(response.getBody().getResponse());
        assertEquals(2, response.getBody().getResponse().size());
        assertEquals("New Solar Panel", response.getBody().getResponse().get(0).getPanelName());
        assertEquals("GB", response.getBody().getResponse().get(0).getCountryCode());
        assertEquals("TH", response.getBody().getResponse().get(1).getCountryCode());
    }

    @Test
    @DisplayName("HTTP 500 INTERNAL_SERVER_ERROR: Returns error when panel creation fails")
    void createPanel_WhenExceptionOccurs_ReturnsErrorResponse() {
        // Arrange
        PanelCreateRequestDTO requestDTO = new PanelCreateRequestDTO();
        requestDTO.setName("New Solar Panel");
        requestDTO.setInstallationCost(350.00);
        requestDTO.setProductionPerPanel(420.00);
        requestDTO.setDescription("New high-efficiency panel");
        requestDTO.setCountryCodes(Arrays.asList("GB", "TH"));

        when(panelService.createPanel(any(PanelCreateRequestDTO.class)))
                .thenThrow(new RuntimeException("Failed to create panel"));

        // Act
        ApiResponse<ApiResponse.CustomBody<List<SolarPanelDTO>>> response = panelController.createPanel(requestDTO);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertFalse(response.getBody().getSuccess());
        assertNull(response.getBody().getResponse());
        assertNotNull(response.getBody().getError());
        assertEquals("PANEL_CREATE_ERROR", response.getBody().getError().getCode());
        assertEquals("Failed to create panel", response.getBody().getError().getMessage());
    }

    @Test
    @DisplayName("HTTP 200 OK: Successfully updates panel")
    void updatePanel_ReturnsSuccess() {
        // Arrange
        Long panelId = 1L;
        PanelCreateRequestDTO requestDTO = new PanelCreateRequestDTO();
        requestDTO.setName("Updated Solar Panel");
        requestDTO.setInstallationCost(380.00);
        requestDTO.setProductionPerPanel(450.00);
        requestDTO.setDescription("Updated high-efficiency panel");
        requestDTO.setCountryCodes(Arrays.asList("FR", "CA"));

        // Act
        ApiResponse<ApiResponse.CustomBody<Object>> response = panelController.updatePanel(panelId, requestDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().getSuccess());
        assertNull(response.getBody().getError());

        // Verify the service was called with the correct parameters
        verify(panelService).updatePanel(eq(panelId), any(PanelCreateRequestDTO.class));
    }

    @Test
    @DisplayName("HTTP 500 INTERNAL_SERVER_ERROR: Returns error when panel update fails")
    void updatePanel_WhenExceptionOccurs_ReturnsErrorResponse() {
        // Arrange
        Long panelId = 1L;
        PanelCreateRequestDTO requestDTO = new PanelCreateRequestDTO();
        requestDTO.setName("Updated Solar Panel");
        requestDTO.setInstallationCost(380.00);
        requestDTO.setProductionPerPanel(450.00);
        requestDTO.setDescription("Updated high-efficiency panel");
        requestDTO.setCountryCodes(Arrays.asList("FR", "CA"));

        doThrow(new RuntimeException("Failed to update panel"))
                .when(panelService).updatePanel(eq(panelId), any(PanelCreateRequestDTO.class));

        // Act
        ApiResponse<ApiResponse.CustomBody<Object>> response = panelController.updatePanel(panelId, requestDTO);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertFalse(response.getBody().getSuccess());
        assertNotNull(response.getBody().getError());
        assertEquals("PANEL_UPDATE_ERROR", response.getBody().getError().getCode());
        assertEquals("Failed to update panel with id: " + panelId, response.getBody().getError().getMessage());
    }

    @Test
    @DisplayName("HTTP 200 OK: Successfully deletes panel")
    void deletePanel_ReturnsSuccess() {
        // Arrange
        Long panelId = 1L;

        // Act
        ApiResponse<ApiResponse.CustomBody<Object>> response = panelController.deletePanel(panelId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().getSuccess());
        assertNull(response.getBody().getError());

        // Verify the service was called with the correct parameter
        verify(panelService).deletePanel(panelId);
    }

    @Test
    @DisplayName("HTTP 500 INTERNAL_SERVER_ERROR: Returns error when panel deletion fails")
    void deletePanel_WhenExceptionOccurs_ReturnsErrorResponse() {
        // Arrange
        Long panelId = 1L;
        doThrow(new RuntimeException("Failed to delete panel"))
                .when(panelService).deletePanel(panelId);

        // Act
        ApiResponse<ApiResponse.CustomBody<Object>> response = panelController.deletePanel(panelId);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertFalse(response.getBody().getSuccess());
        assertNotNull(response.getBody().getError());
        assertEquals("PANEL_DELETE_ERROR", response.getBody().getError().getCode());
        assertEquals("Failed to delete panel with id: " + panelId, response.getBody().getError().getMessage());
    }

    @Test
    @DisplayName("HTTP 400 BAD REQUEST: Returns error when country codes are empty")
    void createPanel_WithEmptyCountryCodes_ReturnsBadRequest() {
        // Arrange
        PanelCreateRequestDTO requestDTO = new PanelCreateRequestDTO();
        requestDTO.setName("New Solar Panel");
        requestDTO.setInstallationCost(350.00);
        requestDTO.setProductionPerPanel(420.00);
        requestDTO.setDescription("New high-efficiency panel");
        requestDTO.setCountryCodes(Collections.emptyList());

        // Act
        ApiResponse<ApiResponse.CustomBody<List<SolarPanelDTO>>> response = panelController.createPanel(requestDTO);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse(response.getBody().getSuccess());
        assertNull(response.getBody().getResponse());
        assertNotNull(response.getBody().getError());
        assertEquals("INVALID_REQUEST", response.getBody().getError().getCode());
        assertEquals("Country codes must not be empty", response.getBody().getError().getMessage());
    }

    @Test
    @DisplayName("HTTP 200 OK: Returns all panels when empty country code is provided")
    void getPanelByZone_WithEmptyCode_ReturnsAllPanels() {
        // Arrange
        String emptyCode = "";
        SolarPanelDTO panel1 = SolarPanelDTO.builder()
                .id(1L)
                .panelName("SunPower Maxeon 7DC 445W")
                .installationCost(400.00)
                .productionPerPanel(445.00)
                .description("Top-tier efficiency, extremely durable (40-year warranty)")
                .efficiency("22.8")
                .warranty("40 years")
                .countryCode("GB")
                .build();

        SolarPanelDTO panel2 = SolarPanelDTO.builder()
                .id(2L)
                .panelName("Project Solar Evo Max Super Series 480W")
                .installationCost(375.00)
                .productionPerPanel(480.00)
                .description("Great customer satisfaction (94.4%), lifetime warranty")
                .efficiency("22.3")
                .warranty("Lifetime")
                .countryCode("GB")
                .build();

        List<SolarPanelDTO> panels = Arrays.asList(panel1, panel2);

        when(panelService.getAllPanels()).thenReturn(panels);

        // Act
        ApiResponse<ApiResponse.CustomBody<List<SolarPanelDTO>>> response = panelController.getPanelByZone(emptyCode);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().getSuccess());
        assertNotNull(response.getBody().getResponse());
        assertEquals(2, response.getBody().getResponse().size());
        assertEquals("SunPower Maxeon 7DC 445W", response.getBody().getResponse().get(0).getPanelName());
        assertEquals("GB", response.getBody().getResponse().get(0).getCountryCode());
    }

    @Test
    @DisplayName("HTTP 400 BAD REQUEST: Returns error when country codes are null")
    void createPanel_WithNullCountryCodes_ReturnsBadRequest() {
        // Arrange
        PanelCreateRequestDTO requestDTO = new PanelCreateRequestDTO();
        requestDTO.setName("New Solar Panel");
        requestDTO.setInstallationCost(350.00);
        requestDTO.setProductionPerPanel(420.00);
        requestDTO.setDescription("New high-efficiency panel");
        requestDTO.setCountryCodes(null);

        // Act
        ApiResponse<ApiResponse.CustomBody<List<SolarPanelDTO>>> response = panelController.createPanel(requestDTO);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse(response.getBody().getSuccess());
        assertNull(response.getBody().getResponse());
        assertNotNull(response.getBody().getError());
        assertEquals("INVALID_REQUEST", response.getBody().getError().getCode());
        assertEquals("Country codes must not be empty", response.getBody().getError().getMessage());
    }
}