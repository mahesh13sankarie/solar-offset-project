package org.example.server.controller;

import lombok.RequiredArgsConstructor;
import org.example.server.dto.PanelResponseDTO;
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
    public List<PanelResponseDTO> getPanelByZone(@RequestParam(required = false) String code) {
        if (code == null || code.isEmpty()) {
            return panelService.getAllPanels();
        }
        return panelService.getPanelByZone(code);

    }


    // Get panel details by ID
    @GetMapping("/{id}")
    public ResponseEntity<PanelResponseDTO> getPanelById(@PathVariable Long id) {
        return null;
    }

    // Create new panel
    @PostMapping
    public ResponseEntity<?> createPanel(@RequestBody Object panelRequest) {
        return null;
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
