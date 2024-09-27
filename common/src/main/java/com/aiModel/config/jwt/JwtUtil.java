package com.aiModel.config.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecureDigestAlgorithm;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

/**
 * jwt
 *
 * @author lihao
 * &#064;date  2024/9/26--15:11
 * @since 1.0
 */
public class JwtUtil {
    /**
     * 生成jwt
     * @param secretKey 秘钥
     * @param expire 过期时间
     * @param content 内容
     * @return
     */
    public static String createJwt(String secretKey, long expire , Map<String,Object> content){
        SecureDigestAlgorithm<SecretKey,SecretKey> algorithm = Jwts.SIG.HS256;
        long expireTime = System.currentTimeMillis() + expire;
        Date exp = new Date(expireTime);
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());
        String compact = Jwts.builder()
                .setClaims(content)
                .setExpiration(exp)
                .signWith(key,algorithm)
                .compact();
        return compact;
    }
    /**
     * 解析jwt
     * @param secretKey 秘钥
     * @param token token
     * @return
     */
    public static Map<String,Object> parseJwt(String secretKey, String token) {
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());
        Map<String,Object> claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims;
    }
}