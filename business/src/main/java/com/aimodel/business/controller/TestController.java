package com.aimodel.business.controller;

import com.aiModel.entity.dto.ResponsePack;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okhttp3.RequestBody;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
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
            HashMap<String, String> test = new HashMap<>();
            test.put("/test","http://localhost:8001");
            test.put("/test2","http://localhost:8001");
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
    @GetMapping("/test")
    public ResponsePack test(){
        log.info("调用接口：");
        return ResponsePack.success("test");
    }
}
