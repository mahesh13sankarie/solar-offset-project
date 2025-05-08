package org.example.server.services;

import org.example.server.dto.PanelCreateRequestDTO;
import org.example.server.dto.SolarPanelDTO;
import org.example.server.entity.Country;
import org.example.server.entity.CountryPanel;
import org.example.server.entity.Panel;
import org.example.server.repository.CountryPanelRepository;
import org.example.server.repository.CountryRepository;
import org.example.server.repository.PanelRepository;
import org.example.server.service.panel.PanelServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * @author: astidhiyaa
 * @date: 08/05/25
 */
@ExtendWith(MockitoExtension.class)
public class PanelServiceTest {
        @InjectMocks
        private PanelServiceImpl panelService;

        @Mock
        private CountryPanelRepository countryPanelRepository;

        @Mock
        private PanelRepository panelRepository;

        @Mock
        private CountryRepository countryRepository;

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

        private CountryPanel buildCountryPanel(Country country, Panel panel) {
            return CountryPanel.createCountryPanel(country, panel);
        }

        private PanelCreateRequestDTO buildPanelCreateRequest() {
            PanelCreateRequestDTO request = new PanelCreateRequestDTO();
            request.setName("SolarMax Pro");
            request.setInstallationCost(1200.0);
            request.setProductionPerPanel(350.0);
            request.setDescription("High efficiency panel");
            request.setCountryCodes(Arrays.asList("US", "DE"));
            return request;
        }

        @Test
        void test_getAllPanels_success() {
            // given
            Country country1 = buildCountry("US");
            Country country2 = buildCountry("DE");
            Panel panel1 = buildPanel();
            Panel panel2 = buildPanel();

            CountryPanel countryPanel1 = buildCountryPanel(country1, panel1);
            CountryPanel countryPanel2 = buildCountryPanel(country2, panel2);
            List<CountryPanel> countryPanels = Arrays.asList(countryPanel1, countryPanel2);

            when(countryPanelRepository.findAll()).thenReturn(countryPanels);

            // when
            List<SolarPanelDTO> result = panelService.getAllPanels();

            // then
            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals(panel1.getName(), result.get(0).getPanelName());
            assertEquals(country1.getCode(), result.get(0).getCountryCode());
            assertEquals(panel2.getName(), result.get(1).getPanelName());
            assertEquals(country2.getCode(), result.get(1).getCountryCode());

            verify(countryPanelRepository).findAll();
        }

        @Test
        void test_getPanelByZone_success() {
            // given
            String zone = "US";
            Country country = buildCountry(zone);
            Panel panel = buildPanel();
            CountryPanel countryPanel = buildCountryPanel(country, panel);

            when(countryPanelRepository.findByCountryCode(zone)).thenReturn(List.of(countryPanel));

            // when
            List<SolarPanelDTO> result = panelService.getPanelByZone(zone);

            // then
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(panel.getName(), result.get(0).getPanelName());
            assertEquals(country.getCode(), result.get(0).getCountryCode());

            verify(countryPanelRepository).findByCountryCode(zone);
        }

        @Test
        void test_getPanelById_success() {
            // given
            Long panelId = 1L;
            Country country = buildCountry("US");
            Panel panel = buildPanel();
            CountryPanel countryPanel = buildCountryPanel(country, panel);

            when(countryPanelRepository.findById(panelId)).thenReturn(Optional.of(countryPanel));

            // when
            SolarPanelDTO result = panelService.getPanelById(panelId);

            // then
            assertNotNull(result);
            assertEquals(panel.getName(), result.getPanelName());
            assertEquals(country.getCode(), result.getCountryCode());
            assertEquals(panel.getInstallationCost(), result.getInstallationCost());
            assertEquals(panel.getProductionPerPanel(), result.getProductionPerPanel());
            assertEquals(panel.getDescription(), result.getDescription());

            verify(countryPanelRepository).findById(panelId);
        }

        @Test
        void test_getPanelById_notFound() {
            // given
            Long panelId = 99L;

            when(countryPanelRepository.findById(panelId)).thenReturn(Optional.empty());

            // when, then
            assertThrows(IllegalArgumentException.class, () -> panelService.getPanelById(panelId));
            verify(countryPanelRepository).findById(panelId);
        }

        @Test
        void test_createPanel_success() {
            // given
            PanelCreateRequestDTO request = buildPanelCreateRequest();

            Panel panel = buildPanel();
            Panel savedPanel = buildPanel();

            Country country1 = buildCountry("US");
            Country country2 = buildCountry("DE");
            List<Country> countries = Arrays.asList(country1, country2);

            CountryPanel countryPanel1 = buildCountryPanel(country1, savedPanel);
            CountryPanel countryPanel2 = buildCountryPanel(country2, savedPanel);

            when(panelRepository.save(any(Panel.class))).thenReturn(savedPanel);
            when(countryRepository.findByCodeIn(anyList())).thenReturn(countries);
            when(countryPanelRepository.save(any(CountryPanel.class)))
                    .thenReturn(countryPanel1)
                    .thenReturn(countryPanel2);

            // when
            List<SolarPanelDTO> result = panelService.createPanel(request);

            // then
            assertNotNull(result);
            assertEquals(2, result.size());

            assertEquals(savedPanel.getName(), result.get(0).getPanelName());
            assertEquals(country1.getCode(), result.get(0).getCountryCode());

            assertEquals(savedPanel.getName(), result.get(1).getPanelName());
            assertEquals(country2.getCode(), result.get(1).getCountryCode());

            verify(panelRepository).save(any(Panel.class));
            verify(countryRepository).findByCodeIn(eq(request.getCountryCodes()));
            verify(countryPanelRepository, times(2)).save(any(CountryPanel.class));
        }

        @Test
        void test_createPanel_invalidCountryCodes() {
            // given
            PanelCreateRequestDTO request = buildPanelCreateRequest();
            Panel savedPanel = buildPanel();

            Country country = buildCountry("US");
            // Only one country found instead of two requested
            List<Country> countries = List.of(country);

            when(panelRepository.save(any(Panel.class))).thenReturn(savedPanel);
            when(countryRepository.findByCodeIn(anyList())).thenReturn(countries);

            // when, then
            assertThrows(IllegalArgumentException.class, () -> panelService.createPanel(request));

            verify(panelRepository).save(any(Panel.class));
            verify(countryRepository).findByCodeIn(eq(request.getCountryCodes()));
            verify(countryPanelRepository, never()).save(any(CountryPanel.class));
        }

        @Test
        void test_deletePanel_not_implemented() {

            // when
            panelService.deletePanel(1L);

            // then
            // No verification needed as the method is empty
        }

        @Test
        void test_updatePanel_not_implemented() {

            // when
            SolarPanelDTO result = panelService.updatePanel(1L, new Object());

            // then
            assertNull(result);
        }
}