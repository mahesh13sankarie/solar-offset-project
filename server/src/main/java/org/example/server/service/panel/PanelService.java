package org.example.server.service.panel;

import org.example.server.dto.PanelCreateRequestDTO;
import org.example.server.dto.SolarPanelDTO;

import java.util.List;

public interface PanelService {

    List<SolarPanelDTO> getAllPanels();

    List<SolarPanelDTO> getPanelByZone(String zone);

    SolarPanelDTO getPanelById(Long id);

    List<SolarPanelDTO> createPanel(PanelCreateRequestDTO request);

    void deletePanel(Long id);

    SolarPanelDTO updatePanel(Long id, Object panelRequest);
}
