package org.example.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.server.dto.PanelCreateRequestDTO;
import org.example.server.dto.SolarPanelDTO;
import org.example.server.service.panel.PanelService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PanelController.class)
@Import(TestSecurityConfig.class)
class PanelControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private PanelService panelService;

	@Test
	@WithMockUser
	@DisplayName("HTTP 200 OK: Returns all panels when no country code is provided")
	void getPanelByZone_WithNoCode_ReturnsAllPanels() throws Exception {
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

		// Act & Assert
		mockMvc.perform(get("/api/v1/panels")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				// These tests assume ApiResponse is already integrated, but controller
				// currently uses ResponseEntity directly
				.andExpect(jsonPath("$[0].panelName").value("SunPower Maxeon 7DC 445W"))
				.andExpect(jsonPath("$[0].countryCode").value("GB"))
				.andExpect(jsonPath("$[1].panelName").value("Project Solar Evo Max Super Series 480W"));
	}

	@Test
	@WithMockUser
	@DisplayName("HTTP 200 OK: Returns panels for specific country code")
	void getPanelByZone_WithCode_ReturnsPanelsForCountry() throws Exception {
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

		// Act & Assert
		mockMvc.perform(get("/api/v1/panels")
				.param("code", countryCode)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].panelName").value("AIKO ABC Neostar 3N54 495W"))
				.andExpect(jsonPath("$[0].countryCode").value(countryCode));
	}

	@Test
	@WithMockUser
	@DisplayName("HTTP 200 OK: Returns panel details by ID")
	void getPanelById_ReturnsPanelDetails() throws Exception {
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

		// Act & Assert
		mockMvc.perform(get("/api/v1/panels/{id}", panelId)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.panelName").value("SunPower Maxeon 7DC 445W"))
				.andExpect(jsonPath("$.id").value(panelId));
	}

	@Test
	@WithMockUser
	@DisplayName("HTTP 200 OK: Successfully creates new panel")
	void createPanel_ReturnsCreatedPanels() throws Exception {
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

		// Act & Assert
		mockMvc.perform(post("/api/v1/panels")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestDTO)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].panelName").value("New Solar Panel"))
				.andExpect(jsonPath("$[0].countryCode").value("GB"))
				.andExpect(jsonPath("$[1].countryCode").value("TH"));
	}

	@Test
	@WithMockUser
	@DisplayName("HTTP 200 OK: Successfully updates panel")
	void updatePanel_ReturnsSuccess() throws Exception {
		// Arrange
		Long panelId = 1L;
		PanelCreateRequestDTO requestDTO = new PanelCreateRequestDTO();
		requestDTO.setName("Updated Solar Panel");
		requestDTO.setInstallationCost(380.00);
		requestDTO.setProductionPerPanel(450.00);
		requestDTO.setDescription("Updated high-efficiency panel");
		requestDTO.setCountryCodes(Arrays.asList("FR", "CA"));

		// Act & Assert
		mockMvc.perform(put("/api/v1/panels/{id}", panelId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestDTO)))
				.andExpect(status().isOk());
	}
}