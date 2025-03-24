package org.example.server.service.panel;

import org.example.server.dto.PanelTransactionDTO;
import org.example.server.entity.PanelTransaction;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author: astidhiyaa
 * @date: 24/03/25
 */
public interface PanelTransactionService {
    PanelTransaction save(PanelTransactionDTO panelTransaction);
    List<PanelTransaction> fetchAll();

    @Query("select trx from PanelTransaction trx join trx.user usr where usr.id == :id")
    List<PanelTransaction> fetchById(String id);
}
