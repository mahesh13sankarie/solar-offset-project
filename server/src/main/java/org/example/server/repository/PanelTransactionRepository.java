package org.example.server.repository;

import org.example.server.entity.PanelTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author: astidhiyaa
 * @date: 24/03/25
 */
public interface PanelTransactionRepository extends JpaRepository<PanelTransaction, Long> {
    @Query(value = "select pt from PanelTransaction pt where pt.user.id = :userId")
    List<PanelTransaction> findByUserId(Long userId);
}
