package org.example.server.service.panel;

import org.example.server.dto.CountryPanelDTO;
import org.example.server.dto.PanelCreateRequest;

import java.util.List;

public interface PanelService {

    List<CountryPanelDTO> getAllPanels();

    List<CountryPanelDTO> getPanelByZone(String zone);

    CountryPanelDTO getPanelById(Long id);

    List<CountryPanelDTO> createPanel(PanelCreateRequest request);

    void deletePanel(Long id);

    CountryPanelDTO updatePanel(Long id, Object panelRequest);
}
