package com.aiModel.gateway.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * gateway配置
 *
 * @author lihao
 * &#064;date  2024/9/27--13:06
 * @since 1.0
 */
@Getter
@Component
public class GateWayConfig {
    @Value("${python.host}")
    private String pythonHost;
    @Value("${python.port}")
    private String pythonPort;
    @Value("${rpc.connect-timeout}")
    private Long rpcConnectTimeout;
    @Value("${rpc.read-timeout}")
    private Long rpcReadTimeout;
    @Value("${rpc.write-timeout}")
    private Long rpcWriteTimeout;
    @Value("${rpc.max-idle-connections}")
    private Integer maxIdleConnections;
    @Value("${rpc.keep-alive-duration}")
    private Integer keepAliveDuration;
    private static String staticPythonHost;
    private static String staticPythonPort;
    @PostConstruct
    public void init() {
        staticPythonHost = this.pythonHost;
        staticPythonPort = this.pythonPort;
    }
    public static String getPythonUrl() {
        return "http://" + staticPythonHost + ":" + staticPythonPort;
    }
}
