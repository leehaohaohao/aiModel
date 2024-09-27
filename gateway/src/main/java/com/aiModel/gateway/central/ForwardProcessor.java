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
        String uri = request.getRequestURI();
        log.info("要求转发的uri:{}",uri);
        if(release(uri)){
            return true;
        }
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
        //转发请求
        JsonNode jsonNode = forwardService.forward(uri,heads,content.toString(),params);
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write(mapper.writeValueAsString(jsonNode));
        return false;
    }
    private boolean release(String uri){
        if ("/register".equals(uri)) {
            return true;
        }
        return false;
    }
}
