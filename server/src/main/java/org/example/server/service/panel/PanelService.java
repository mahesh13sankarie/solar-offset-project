package org.example.server.service.panel;

import org.example.server.dto.countryPanelDTO;

import java.util.List;

public interface PanelService {

    List<countryPanelDTO> getAllPanels();
    List<countryPanelDTO> getPanelByZone(String zone);
}
