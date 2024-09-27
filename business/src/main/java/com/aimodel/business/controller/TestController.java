package com.aimodel.business.controller;

import com.aiModel.entity.dto.ResponsePack;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * 测试controller
 *
 * @author lihao
 * &#064;date  2024/9/26--20:26
 * @since 1.0
 */
@RestController
@Slf4j
public class TestController {
    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    @PostConstruct
    private void init(){
        try {
            Set<String> test = new HashSet<>();
            test.add("/test");
            test.add("/test2");
            RequestBody requestBody = RequestBody.create(mapper.writeValueAsString(test), MediaType.parse("application/json"));
            log.info("请求体为：{}", requestBody);
            Request request = new Request.Builder()
                    .url("http://localhost:8000/register")
                    .post(requestBody)
                    .build();
            try(Response response = client.newCall(request).execute()){
                if(response.isSuccessful()){
                    log.info("注册成功,注册服务名为：{}", test);
                }else{
                    log.error("注册失败,注册服务名为：{}", test);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @PostMapping("/test")
    public ResponsePack test(){
        //TODO 修改bug
        log.info("调用接口：");
        return ResponsePack.success("test");
    }
}
