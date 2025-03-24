package org.example.server.repository;

import org.example.server.entity.PanelTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author: astidhiyaa
 * @date: 24/03/25
 */
public interface PanelTransactionRepository extends JpaRepository<PanelTransaction, Long> {
//    @Query("SELECT  FROM pan")
//    PanelTransaction findByUserId(Long userId);
//
//    List<PanelTransaction> fetchAll();
}
