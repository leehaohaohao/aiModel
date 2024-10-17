package com.aiModel.gateway.central;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 转发处理器
 *
 * @author lihao
 * &#064;date  2024/9/26--16:23
 * @since 1.0
 */
@Slf4j
public class ForwardProcessor implements HandlerInterceptor {
    private final ForwardService forwardService = new ForwardService();
    private final ObjectMapper mapper = new ObjectMapper();
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        long start = System.currentTimeMillis();
        String uri = request.getRequestURI();
        log.debug("rpc==要求转发的uri=={}",uri);
        if(release(uri)){
            return true;
        }
        //TODO 限流
        Map<String,String> heads = new HashMap<>();
        Map<String,String> params = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        //获取请求头
        while (headerNames.hasMoreElements())
        {
            String key = headerNames.nextElement();
            String value = request.getHeader(key);
            heads.put(key,value);
        }
        //获取param参数
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements())
        {
            String key = paramNames.nextElement();
            String value = request.getParameter(key);
            params.put(key,value);
        }
        // 获取请求体
        StringBuilder content = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream(), Charset.defaultCharset()))) {
            String line;
            while ((line = in.readLine()) != null) {
                content.append(line);
            }
        }
        // 获取请求方式
        String method = request.getMethod();
        // 获取请求类型
        String contentType = request.getContentType();
        //转发请求
        //TODO 容错、重试、降级
        JsonNode jsonNode = forwardService.forward(uri,heads,content.toString(),params,method,contentType);
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write(mapper.writeValueAsString(jsonNode));
        long end = System.currentTimeMillis();
        log.debug("转发请求耗时=={}ms",end-start);
        return false;
    }
    private boolean release(String uri){
        if ("/register".equals(uri)) {
            return true;
        }
        return false;
    }
}
