package org.example.server.service.panel;

import org.example.server.dto.CountryPanelDTO;
import org.example.server.dto.PanelCreateRequestDTO;

import java.util.List;

public interface PanelService {

    List<CountryPanelDTO> getAllPanels();

    List<CountryPanelDTO> getPanelByZone(String zone);

    CountryPanelDTO getPanelById(Long id);

    List<CountryPanelDTO> createPanel(PanelCreateRequestDTO request);

    void deletePanel(Long id);

    CountryPanelDTO updatePanel(Long id, Object panelRequest);
}
