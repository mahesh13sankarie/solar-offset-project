package org.example.server.service.panel;

import org.example.server.dto.PanelTransactionDTO;
import org.example.server.entity.Panel;
import org.example.server.entity.PanelTransaction;
import org.example.server.entity.User;
import org.example.server.repository.PanelRepository;
import org.example.server.repository.PanelTransactionRepository;
import org.example.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author: astidhiyaa
 * @date: 24/03/25
 */

@Service
public class PanelTransactionServiceImpl implements PanelTransactionService {
    @Autowired
    private PanelTransactionRepository panelTransactionRepository;

    @Autowired
    private PanelRepository panelRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public PanelTransaction save(PanelTransactionDTO panelTransaction) {
        //TODO: clean code!
        Optional<Panel> panel = panelRepository.findById(panelTransaction.panelId());
        Optional<User> user = userRepository.findById(panelTransaction.userId());
        PanelTransaction newPanel = new PanelTransaction(user.get(), panel.get());
        return panelTransactionRepository.save(newPanel);
    }

    @Override
    public List<PanelTransaction> fetchAll() {
        return panelTransactionRepository.findAll();
    }

    @Override
    public List<PanelTransaction> fetchById(String id) {
        return panelTransactionRepository.findAll();
    }

}
