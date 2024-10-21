package com.aiModel.gateway.fault.retry;


/**
 * 重试策略工厂
 *
 * @author lihao
 * &#064;date  2024/9/24--19:02
 * @since 1.0
 */
public class RetryStrategyFactory {
    private static FixedIntervalRetryStrategy fixedIntervalRetryStrategy;
    public static RetryStrategy getRetryStrategy(){
        if(fixedIntervalRetryStrategy == null){
            synchronized (RetryStrategyFactory.class){
                if(fixedIntervalRetryStrategy == null){
                    fixedIntervalRetryStrategy = new FixedIntervalRetryStrategy();
                }
            }
        }
        return fixedIntervalRetryStrategy;
    }
}
