package com.aiModel.gateway.fault.retry;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.concurrent.Callable;

/**
 * 不重试-重试策略
 *
 * @author lihao
 * &#064;date  2024/9/24--18:40
 * @since 1.0
 */
public class NoRetryStrategy implements RetryStrategy{
    /**
     * 重试
     * @param callable
     * @return
     */
    @Override
    public JsonNode doRetry(Callable<JsonNode> callable) throws Exception {
        return callable.call();
    }
}
