package org.example.server.service.panel;

import lombok.RequiredArgsConstructor;
import org.example.server.dto.PanelResponseDTO;
import org.example.server.entity.Panel;
import org.example.server.repository.PanelRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PanelServiceImpl implements PanelService {

    private final PanelRepository panelRepository;

    @Override
    public List<PanelResponseDTO> getAllPanels() {
        List<Panel> panels = panelRepository.findAll();

        return panels.stream()
                .map(panel -> new PanelResponseDTO(
                        panel.getName(),
                        panel.getInstallationCost(),
                        panel.getCountry().getCode()))
                .toList();
    }

    @Override
    public List<PanelResponseDTO> getPanelByZone(String code) {
        return panelRepository.findByCountryCode(code);
    }
}
