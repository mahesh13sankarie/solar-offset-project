package org.example.server.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

/**
 * @author: astidhiyaa
 * @date: 29/04/25
 */
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Roles implements GrantedAuthority {
    @Id
    private String type;

    @Override
    public String getAuthority() {
        return type;
    }
}
