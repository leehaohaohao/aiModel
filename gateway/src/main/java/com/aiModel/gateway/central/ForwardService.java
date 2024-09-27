package com.aiModel.gateway.central;

import com.aiModel.entity.constants.ExceptionConstants;
import com.aiModel.entity.dto.ResponsePack;
import com.aiModel.entity.map.CacheMap;
import com.aiModel.gateway.config.GateWayConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;

/**
 * 转发服务
 *
 * @author lihao
 * &#064;date  2024/9/26--16:50
 * @since 1.0
 */
public class ForwardService {
    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    @SuppressWarnings("unchecked")
    public ResponsePack forward(String path,Map<String, String> headers,
                                String body,Map<String,String> params){
        try{
            HashSet<String> python = (HashSet<String>) CacheMap.pythonMap.get("python");
            if(python ==null){
                python = new HashSet<>();
            }
            if(!python.contains(path)){
                return ResponsePack.fail(ExceptionConstants.SERVICE_NOT_FOUND);
            }
            // 拼接url
            StringBuilder targetUrl = new StringBuilder(GateWayConfig.getPythonUrl() + path);
            if(!params.isEmpty()){
                targetUrl.append("?");
                params.forEach((k,v)->targetUrl.append(k).append("=").append(v).append("&"));
                targetUrl.setLength(targetUrl.length()-1);
            }
            // 构建请求头
            Headers.Builder headersBuilder = new Headers.Builder();
            headers.forEach(headersBuilder::add);
            // 构建请求体
            RequestBody requestBody = RequestBody.create(body, MediaType.parse("application/json"));
            Request request = new Request.Builder()
                    .url(targetUrl.toString())
                    .headers(headersBuilder.build())
                    .post(requestBody)
                    .build();
            try(Response response = client.newCall(request).execute()){
                if(response.isSuccessful()){
                    if (response.body() != null) {
                        return mapper.convertValue(response.body(), ResponsePack.class);
                    }
                    return ResponsePack.success(null);
                }else {
                    return ResponsePack.fail(response.code(),response.message());
                }
            }
        }catch (IOException e) {
            return ResponsePack.fail(ExceptionConstants.SERVER_ERROR);
        }
    }
}
