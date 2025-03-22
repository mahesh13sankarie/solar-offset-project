package org.example.server.service.panel;

import org.example.server.dto.PanelResponseDTO;

import java.util.List;

public interface PanelService {

    List<PanelResponseDTO> getAllPanels();
    List<PanelResponseDTO> getPanelByZone(String zone);
}
