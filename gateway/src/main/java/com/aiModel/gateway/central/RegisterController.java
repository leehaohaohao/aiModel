package com.aiModel.gateway.central;

import cn.hutool.core.lang.hash.Hash;
import com.aiModel.entity.dto.ResponsePack;
import com.aiModel.entity.map.CacheMap;
import jakarta.annotation.PostConstruct;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
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
     * @param serviceInfo 如正常请求地址121.11.11.11:8080/api/test 则需/api/test
     * @return
     */
    @RequestMapping("/register")
    public ResponsePack register(@RequestBody Set<String> serviceInfo)
    {
        log.info("接收到注册信息:{}",serviceInfo);
        HashSet<String> python =(HashSet<String>)CacheMap.pythonMap.get("python");
        if(python==null){
            python = new HashSet<>();
            CacheMap.pythonMap.put("python",python);
        }
        python.addAll(serviceInfo);

        return ResponsePack.success(null);
    }
}
