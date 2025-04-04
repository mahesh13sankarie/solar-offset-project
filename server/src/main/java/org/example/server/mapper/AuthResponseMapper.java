package org.example.server.mapper;


/**
 * @author: astidhiyaa
 * @date: 18/03/25
 */
public class AuthResponseMapper {
    public BaseResponse buildLoginResponse(Object user, String token) {
        return new BaseResponse(200, "", token, user);
    }

    public BaseResponse buildSuccessResponse() {
        return new BaseResponse(200, "", "", null);
    }

    public BaseResponse buildCustomResponse(Object data) {
        return new BaseResponse(200, "", "", data);
    }

    public BaseResponse buildCustomMessage(String message) {
        return new BaseResponse(200, message, "", null);
    }
}

