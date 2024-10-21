package com.aiModel.gateway.fault.retry;

/**
 * classname
 *
 * @author lihao
 * &#064;date  2024/9/24--19:05
 * @since 1.0
 */
public interface RetryStrategyKeys {
    /**
     * 不重试
     */
    String NO_RETRY = "no";
    /**
     * 固定时间间隔
     */
    String FIXED_INTERVAL = "fixedInterval";
}
