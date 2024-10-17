package com.aiModel.gateway.central;

import cn.hutool.core.util.StrUtil;
import com.aiModel.entity.constants.ExceptionConstants;
import com.aiModel.entity.dto.ResponsePack;
import com.aiModel.entity.enums.MethodEnum;
import com.aiModel.entity.map.CacheMap;
import com.aiModel.gateway.config.GateWayConfig;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.util.HashMap;
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
                            String body, Map<String,String> params,
                            String method,String contentType){
        try{
            OkHttpClient client = OkClientPool.getClient();
            HashMap<String,String> python = CacheMap.pythonMap;
            if(python == null){
                python = new HashMap<>();
                CacheMap.pythonMap = python;
            }
            if(!python.containsKey(path)){
                return buildJsonNode(ResponsePack.fail(ExceptionConstants.SERVER_ERROR));
            }
            Request request = buildRequest(python.get(path)+path, headers, body, params, method, contentType);
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

    /**
     * 构建请求头
     * @param headers 请求头
     * @return
     */
    private Headers buildHeaders(Map<String, String> headers){
        Headers.Builder headersBuilder = new Headers.Builder();
        headers.forEach(headersBuilder::add);
        return headersBuilder.build();
    }

    /**
     * 构建目标url
     * @param path 路径
     * @param params 路径参数
     * @return
     */
    private String buildTargetUrl(String path, Map<String,String> params){
        StringBuilder targetUrl = new StringBuilder(path);
        if(!params.isEmpty()){
            targetUrl.append("?");
            params.forEach((k,v)->targetUrl.append(k).append("=").append(v).append("&"));
            targetUrl.setLength(targetUrl.length()-1);
        }
        return targetUrl.toString();
    }

    /**
     * 构建请求
     * @param path 路径
     * @param headers 请求头
     * @param body 请求体
     * @param params 路径参数
     * @param method 请求方法
     * @param contentType 请求体类型
     * @return
     */
    private Request buildRequest(String path , Map<String, String> headers,
                                 String body , Map<String,String> params,
                                 String method , String contentType){
        // 拼接url
        String targetUrl = buildTargetUrl(path,params);
        log.debug("rpc==转发请求url=={}==方法：{}",targetUrl,method);
        // 构建请求头
        Headers headersBuilder = buildHeaders(headers);
        log.debug("rpc==转发请求头=={}",headersBuilder);
        // 构建请求体
        RequestBody requestBody = buildRequestBody(body,contentType);
        log.debug("rpc==转发请求体=={}",requestBody);
        // 构建请求
        return switch (method.toUpperCase()) {
            case "GET" -> new Request.Builder()
                    .url(targetUrl)
                    .headers(headersBuilder)
                    .get()
                    .build();
            case "DELETE" -> new Request.Builder()
                    .url(targetUrl)
                    .headers(headersBuilder)
                    .delete()
                    .build();
            case "OPTIONS" -> new Request.Builder()
                    .url(targetUrl)
                    .headers(headersBuilder)
                    .method("OPTIONS", requestBody)
                    .build();
            case "HEAD" -> new Request.Builder()
                    .url(targetUrl)
                    .headers(headersBuilder)
                    .head()
                    .build();
            case "PATCH" -> new Request.Builder()
                    .url(targetUrl)
                    .headers(headersBuilder)
                    .patch(requestBody)
                    .build();
            case "PUT" -> new Request.Builder()
                    .url(targetUrl)
                    .headers(headersBuilder)
                    .put(requestBody)
                    .build();
            case "POST" -> new Request.Builder()
                    .url(targetUrl)
                    .headers(headersBuilder)
                    .post(requestBody)
                    .build();
            default -> throw new RuntimeException("不支持的请求方法");
        };
    }

    /**
     * 构建请求体
     * @param body 请求体
     * @param contentType 请求体类型
     * @return
     */
    private RequestBody buildRequestBody(String body,String contentType){
        if(StrUtil.isBlank(body)){
            body="";
        }
        if(StrUtil.isBlank(contentType)){
            contentType = "application/json";
        }
        return RequestBody.create(body, MediaType.parse(contentType));
    }

    /**
     * 构建响应体
     * @param responsePack  响应体
     * @return
     */
    private JsonNode buildJsonNode(ResponsePack responsePack){
        return MAPPER.convertValue(responsePack, JsonNode.class);
    }
}
