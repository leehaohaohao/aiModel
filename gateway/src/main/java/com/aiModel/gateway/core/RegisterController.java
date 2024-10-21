package com.aiModel.gateway.core;

import com.aiModel.entity.dto.ResponsePack;
import com.aiModel.entity.map.CacheMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 注册控制器
 *
 * @author lihao
 * &#064;date  2024/9/26--19:23
 * @since 1.0
 */
@RestController
@Slf4j
public class RegisterController {
    /**
     * 注册服务
     * @param serviceInfo 如正常请求地址121.11.11.11:8080/api/test 则键名：/api/test 对应值：121.11.11.11:8080
     * @return
     */
    @RequestMapping("/register")
    public ResponsePack register(@RequestBody HashMap<String,String> serviceInfo)
    {
        log.info("接收到注册信息:{}",serviceInfo);
        HashMap<String,String> python = CacheMap.pythonMap;
        if(python == null){
            python = new HashMap<>();
            CacheMap.pythonMap = python;
        }
        python.putAll(serviceInfo);

        return ResponsePack.success(null);
    }
}
