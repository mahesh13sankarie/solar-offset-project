package org.example.server.service.panel;

import java.util.List;

import org.example.server.dto.countryPanelDTO;
import org.example.server.entity.CountryPanel;
import org.example.server.repository.CountryPanelRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PanelServiceImpl implements PanelService {

    private final CountryPanelRepository countryPanelRepository;

    @Override
    public List<countryPanelDTO> getAllPanels() {

        List<CountryPanel> countryPanels = countryPanelRepository.findAll();

        return countryPanels.stream()
                .map(countryPanel -> new countryPanelDTO(
                        countryPanel.getPanel().getName(),
                        countryPanel.getPanel().getInstallationCost(),
                        countryPanel.getCountry().getCode(),
                        countryPanel.getPanel().getDescription()))
                .toList();
    }

    @Override
    public List<countryPanelDTO> getPanelByZone(String code) {
        return countryPanelRepository.findByCountryCode(code).stream()
                .map(countryPanel -> new countryPanelDTO(
                        countryPanel.getPanel().getName(),
                        countryPanel.getPanel().getInstallationCost(),
                        countryPanel.getCountry().getCode(),
                        countryPanel.getPanel().getDescription()))
                .toList();
    }
}
