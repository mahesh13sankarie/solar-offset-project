package org.example.server.controller;

import org.example.server.dto.PanelTransactionDTO;
import org.example.server.service.panel.PanelTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author: astidhiyaa
 * @date: 24/03/25
 */

@RequestMapping("api/v1/transaction")
@RestController
public class PanelTransactionController {
    //TODO: need to check which panel to store!

    @Autowired
    private PanelTransactionService panelTransactionService;

    @PostMapping("/add")
    public ResponseEntity<?> addTransaction(@RequestBody PanelTransactionDTO panelTransaction) {
        return ResponseEntity.ok().body(
                panelTransactionService.save(panelTransaction)
        );
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllTransaction() {
        return ResponseEntity.ok().body(
                panelTransactionService.fetchAll()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTransactionByUserId(@PathVariable String id) {
        return ResponseEntity.ok().build();
    }
}
