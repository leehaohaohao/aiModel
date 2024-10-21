package com.aiModel.gateway.core;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

/**
 * okHttpClient连接池
 *
 * @author lihao
 * &#064;date  2024/9/27--12:56
 * @since 1.0
 */
public class OkClientPool {
    private static final int MAX_IDLE_CONNECTIONS = 5;
    private static final long KEEP_ALIVE_DURATION = 5L;

    private static final OkHttpClient CLIENT;

    static {
        ConnectionPool connectionPool = new ConnectionPool(MAX_IDLE_CONNECTIONS, KEEP_ALIVE_DURATION, TimeUnit.MINUTES);
        CLIENT = new OkHttpClient.Builder()
                .connectionPool(connectionPool)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
    }

    public static OkHttpClient getClient() {
        return CLIENT;
    }
}
