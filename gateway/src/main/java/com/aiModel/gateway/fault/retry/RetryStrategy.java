package com.aiModel.gateway.fault.retry;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.concurrent.Callable;


/**
 * 重试策略
 *
 * @author lihao
 * &#064;date  2024/9/24--18:38
 * @since 1.0
 */
public interface RetryStrategy {
    JsonNode doRetry(Callable<JsonNode> callable) throws Exception;
}
