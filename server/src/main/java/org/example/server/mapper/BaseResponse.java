package org.example.server.mapper;

public record BaseResponse(
        int status, String message, String token, Object data
) {
}


