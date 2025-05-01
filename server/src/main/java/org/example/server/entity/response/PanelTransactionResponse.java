package org.example.server.entity.response;

import org.example.server.dto.SolarPanelDTO;
import org.example.server.entity.Payment;
import org.example.server.entity.User;

/**
 * @author: astidhiyaa
 * @date: 27/03/25
 */
public record PanelTransactionResponse(Long id, User user, SolarPanelDTO solarPanel, Payment payment) {
}
