package com.aiModel.entity.enums;

import lombok.Getter;

/**
 * 响应码枚举
 *
 * @author lihao
 * &#064;date  2024/9/24--20:35
 * @since 1.0
 */
@Getter
public enum CodeEnum {
    SUCCESS(200, "成功"),
    FAIL(400, "失败"),
    ERROR(500, "错误"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "未找到"),
    METHOD_NOT_ALLOWED(405, "方法不允许"),
    REQUEST_TIMEOUT(408, "请求超时"),
    TOO_MANY_REQUESTS(429, "请求过于频繁"),
    INTERNAL_SERVER_ERROR(500, "服务器内部错误");
    private final int code;
    private final String msg;
    CodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
