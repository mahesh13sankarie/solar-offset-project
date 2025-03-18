package org.example.server.mapper;

import org.example.server.entity.User;

/**
 * @author: astidhiyaa
 * @date: 18/03/25
 */
public class AuthResponseMapper {
    public BaseResponse buildLoginResponse(User user, String token) {
        return new BaseResponse(200, "", token, user);
    }

    public BaseResponse buildSuccessResponse() {
        return new BaseResponse(200, "", "", null);
    }
}

