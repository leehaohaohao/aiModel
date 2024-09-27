package com.aiModel.gateway.config;

import com.aiModel.gateway.central.ForwardProcessor;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 网关配置
 *
 * @author lihao
 * @date 2024/9/26--16:26
 * @since 1.0
 */
@Configuration

public class GateWayConfig implements WebMvcConfigurer {
    @Value("${python.host}")
    private String pythonHost;
    @Value("${python.port}")
    private String pythonPort;

    private static String staticPythonHost;
    private static String staticPythonPort;

    @PostConstruct
    public void init() {
        staticPythonHost = this.pythonHost;
        staticPythonPort = this.pythonPort;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ForwardProcessor()).addPathPatterns("/**");
    }

    public static String getPythonUrl() {
        return "http://" + staticPythonHost + ":" + staticPythonPort;
    }
}
