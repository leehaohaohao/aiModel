package com.aiModel.gateway.fault.retry;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.rholder.retry.*;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * 固定时间间隔-重试策略
 *
 * @author lihao
 * &#064;date  2024/9/24--18:41
 * @since 1.0
 */
@Slf4j
public class FixedIntervalRetryStrategy implements RetryStrategy{
    @Override
    public JsonNode doRetry(Callable<JsonNode> callable) throws Exception {
        Retryer<JsonNode> retryer = RetryerBuilder.<JsonNode>newBuilder()
                .retryIfExceptionOfType(Exception.class)
                .withWaitStrategy(WaitStrategies.fixedWait(3L, TimeUnit.SECONDS))
                .withStopStrategy(StopStrategies.stopAfterAttempt(3))
                .withRetryListener(new RetryListener() {
                    @Override
                    public <V> void onRetry(Attempt<V> attempt) {
                        //TODO 可以加一个异常判断
                        log.info("重试次数：{}", attempt.getAttemptNumber());
                    }
                })
                .build();
        return retryer.call(callable);
    }
}
