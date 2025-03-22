package org.example.server.controller;

import lombok.RequiredArgsConstructor;
import org.example.server.dto.CountryPanelDTO;
import org.example.server.dto.PanelCreateRequestDTO;
import org.example.server.service.panel.PanelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/panels")
@RequiredArgsConstructor
public class PanelController {

    private final PanelService panelService;

    @GetMapping
    public List<CountryPanelDTO> getPanelByZone(@RequestParam(required = false) String code) {
        if (code == null || code.isEmpty()) {
            return panelService.getAllPanels();
        }
        return panelService.getPanelByZone(code);

    }

    // Get panel details by ID
    @GetMapping("/{id}")
    public ResponseEntity<CountryPanelDTO> getPanelById(@PathVariable Long id) {
        return null;
    }

    // Create new panel
    @PostMapping
    public ResponseEntity<List<CountryPanelDTO>> createPanel(@RequestBody PanelCreateRequestDTO panelRequest) {
        // TODO: Validate that the user is an admin before proceeding

        // Validate that at least one country is selected
        if (panelRequest.getCountryCodes() == null || panelRequest.getCountryCodes().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        List<CountryPanelDTO> createdPanel = panelService.createPanel(panelRequest);
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
