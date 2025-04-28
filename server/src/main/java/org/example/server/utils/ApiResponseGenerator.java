package org.example.server.utils;

import org.springframework.http.HttpStatus;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ApiResponseGenerator {

	public static ApiResponse<ApiResponse.CustomBody<Object>> success(final HttpStatus status) {
		return new ApiResponse<>(new ApiResponse.CustomBody<>(true, null, null), status);
	}

	public static <T> ApiResponse<ApiResponse.CustomBody<T>> success(final HttpStatus status, T data) {
		return new ApiResponse<>(new ApiResponse.CustomBody<>(true, data, null), status);
	}

	public static ApiResponse<ApiResponse.CustomBody<Object>> fail(String code, String message,
			final HttpStatus status) {
		return new ApiResponse<>(new ApiResponse.CustomBody<>(false, null, new Error(code, message, status.toString())),
				status);
	}

}