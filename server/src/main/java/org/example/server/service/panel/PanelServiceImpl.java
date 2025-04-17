package org.example.server.service.panel;

import lombok.RequiredArgsConstructor;
import org.example.server.dto.PanelCreateRequestDTO;
import org.example.server.dto.SolarPanelDTO;
import org.example.server.entity.Country;
import org.example.server.entity.CountryPanel;
import org.example.server.entity.Panel;
import org.example.server.repository.CountryPanelRepository;
import org.example.server.repository.CountryRepository;
import org.example.server.repository.PanelRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PanelServiceImpl implements PanelService {

    private final CountryPanelRepository countryPanelRepository;
    private final PanelRepository panelRepository;
    private final CountryRepository countryRepository;

    @Override
    public List<SolarPanelDTO> getAllPanels() {
        List<CountryPanel> countryPanels = countryPanelRepository.findAll();
        return countryPanels.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<SolarPanelDTO> getPanelByZone(String zone) {
        List<CountryPanel> countryPanels = countryPanelRepository.findByCountryCode(zone);
        return countryPanels.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public SolarPanelDTO getPanelById(Long id) {
        return countryPanelRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new IllegalArgumentException("Panel not found with ID: " + id));
    }

    @Override
    @Transactional
    public List<SolarPanelDTO> createPanel(PanelCreateRequestDTO request) {
        // Create and save the new panel
        Panel panel = Panel.builder()
                .name(request.getName())
                .installationCost(request.getInstallationCost())
                .productionPerPanel(request.getProductionPerPanel())
                .description(request.getDescription())
                .build();

        Panel savedPanel = panelRepository.save(panel);

        // Retrieve the countries and create CountryPanel entries
        List<CountryPanel> countryPanels = new ArrayList<>();

        // Find all countries by their codes
        List<Country> countries = countryRepository.findByCodeIn(request.getCountryCodes());

        // Validate that all country codes were found
        if (countries.size() != request.getCountryCodes().size()) {
            throw new IllegalArgumentException("One or more country codes are invalid");
        }

        for (Country country : countries) {
            CountryPanel countryPanel = CountryPanel.createCountryPanel(country, savedPanel);
            countryPanels.add(countryPanelRepository.save(countryPanel));
        }

        // For simplicity, return the first countryPanel as a DTO
        return countryPanels.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

    }

    private SolarPanelDTO mapToDTO(CountryPanel countryPanel) {
        Panel panel = countryPanel.getPanel();
        Country country = countryPanel.getCountry();

        return SolarPanelDTO.builder()
                .id(countryPanel.getId())
                .panelName(panel.getName())
                .installationCost(panel.getInstallationCost())
                .productionPerPanel(panel.getProductionPerPanel())
                .description(panel.getDescription())
                .countryCode(country.getCode())
                .build();
    }

    @Override
    public void deletePanel(Long id) {
        // Implementation will be added later
    }

    @Override
    public SolarPanelDTO updatePanel(Long id, Object panelRequest) {
        return null;
    }
}
