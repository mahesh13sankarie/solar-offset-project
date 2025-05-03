package org.example.server.dto;

/**
 * @author: astidhiyaa
 * @date: 02/05/25
 */
public record EnquiryRequestDto(
        String name, String email, String subject, String body
) {
}
