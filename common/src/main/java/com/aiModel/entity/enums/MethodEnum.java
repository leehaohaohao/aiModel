package com.aiModel.entity.enums;

import lombok.Getter;

/**
 * 方法枚举
 *
 * @author lihao
 * &#064;date  2024/9/26--19:45
 * @since 1.0
 */
@Getter
public enum MethodEnum {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE"),
    OPTIONS("OPTIONS"),
    HEAD("HEAD"),
    PATCH("PATCH");

    private final String method;

    MethodEnum(String method) {
        this.method = method;
    }
}
