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
    public ResponseEntity<List<SolarPanelDTO>> getPanelByZone(@RequestParam(required = false) String code) {
        List<SolarPanelDTO> panels = (code == null || code.isEmpty())
                ? panelService.getAllPanels()
                : panelService.getPanelByZone(code);
        return ResponseEntity.ok(panels);
    }

    // Get panel details by ID
    @GetMapping("/{id}")
    public ResponseEntity<SolarPanelDTO> getPanelById(@PathVariable Long id) {
        SolarPanelDTO panel = panelService.getPanelById(id);
        return ResponseEntity.ok(panel);
    }

    // Create new panel
    @PostMapping
    public ResponseEntity<List<SolarPanelDTO>> createPanel(@RequestBody PanelCreateRequestDTO panelRequest) {
        // TODO: Validate that the user is an admin before proceeding
        if (panelRequest.getCountryCodes() == null || panelRequest.getCountryCodes().isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }
        List<SolarPanelDTO> createdPanels = panelService.createPanel(panelRequest);
        return ResponseEntity.ok(createdPanels);
    }

    // Delete panel by ID
    public ResponseEntity<?> deletePanel(@PathVariable Long id) {
        panelService.deletePanel(id);
        return ResponseEntity.ok().build();
    }

    // Update panel (full update)
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePanel(@PathVariable Long id, @RequestBody PanelCreateRequestDTO panelRequest) {
        panelService.updatePanel(id, panelRequest);
        return ResponseEntity.ok().build();
    }
}
