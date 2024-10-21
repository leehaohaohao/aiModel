package com.aiModel.gateway.config;

import com.aiModel.gateway.core.ForwardProcessor;
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
public class GateWayConfigurer implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ForwardProcessor()).addPathPatterns("/**");
    }

}
