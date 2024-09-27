package com.aiModel.gateway.central;

import com.aiModel.entity.constants.ExceptionConstants;
import com.aiModel.entity.dto.ResponsePack;
import com.aiModel.entity.map.CacheMap;
import com.aiModel.gateway.config.GateWayConfig;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

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
@Slf4j
public class ForwardService {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    static {
        MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }
    @SuppressWarnings("unchecked")
    public JsonNode forward(String path, Map<String, String> headers,
                            String body, Map<String,String> params){
        try{
            OkHttpClient client = OkClientPool.getClient();
            HashSet<String> python = (HashSet<String>) CacheMap.pythonMap.get("python");
            if(python ==null){
                python = new HashSet<>();
            }
            if(!python.contains(path)){
                return buildJsonNode(ResponsePack.fail(ExceptionConstants.SERVER_ERROR));
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
                        String responseBody = response.body().string();
                        if (responseBody.isEmpty()) {
                            return buildJsonNode(ResponsePack.success(null));
                        }
                        return MAPPER.readTree(responseBody);
                    }
                    return buildJsonNode(ResponsePack.success(null));
                }
                return buildJsonNode(ResponsePack.fail(null,response.code(),response.message()));
            }
        }catch (IOException e) {
            log.error("转发服务异常",e);
            return buildJsonNode(ResponsePack.fail(ExceptionConstants.SERVER_ERROR));
        }
    }
    private JsonNode buildJsonNode(ResponsePack responsePack){
        return MAPPER.convertValue(responsePack, JsonNode.class);
    }
}
