package org.example.server.controller;

import lombok.RequiredArgsConstructor;
import org.example.server.dto.PanelCreateRequestDTO;
import org.example.server.dto.SolarPanelDTO;
import org.example.server.service.panel.PanelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/panels")
@RequiredArgsConstructor
public class PanelController {

    private final PanelService panelService;

    @GetMapping
    public List<SolarPanelDTO> getPanelByZone(@RequestParam(required = false) String code) {
        if (code == null || code.isEmpty()) {
            return panelService.getAllPanels();
        }
        return panelService.getPanelByZone(code);
    }

    // Get panel details by ID
    @GetMapping("/{id}")
    public SolarPanelDTO getPanelById(@PathVariable Long id) {
        return panelService.getPanelById(id);
    }

    // Create new panel
    @PostMapping
    public ResponseEntity<List<SolarPanelDTO>> createPanel(@RequestBody PanelCreateRequestDTO panelRequest) {
        // TODO: Validate that the user is an admin before proceeding

        // Validate that at least one country is selected
        if (panelRequest.getCountryCodes() == null || panelRequest.getCountryCodes().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        List<SolarPanelDTO> createdPanel = panelService.createPanel(panelRequest);
        return ResponseEntity.ok(createdPanel);
    }

    // Delete panel by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePanel(@PathVariable Long id) {
        return null;
    }

    // Update panel (full update)
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePanel(
            @PathVariable Long id,
            @RequestBody Object panelRequest) {
        return null;
    }

}
