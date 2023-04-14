package com.platform.utils;


import io.jsonwebtoken.*;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

/**
 * JWT工具类
 * 生成JWT
 * 解析JWT
 */
public class JwtUtil {

    //有效期为
    public static final Long JWT_TTL = 240 * 60 * 60 * 1000L; //60 * 60 * 1000 一个小时
    //设置秘钥明文
    public static final String JWT_KEY = "sangeng";

    public static String getUUID(){
        String token = UUID.randomUUID().toString().replaceAll("-","");
        return token;
    }

    /**
     * 生成jwt
     * @param subject token中要存放的数据(json格式）
     * @return
     */
    public static String createJWT(String subject){
        JwtBuilder builder = getJwtBuilder(subject, null, getUUID()); //设置过期时间
        return builder.compact();
    }

    /**
     * 生成jwt
     * @param subject token中要存放的数据（json格式）
     * @param ttlMillis token超时时间
     * @return
     */
    public static String createJWT(String subject, Long ttlMillis){
        JwtBuilder builder = getJwtBuilder(subject, ttlMillis, getUUID()); //设置过期时间
        return builder.compact();
    }


    private static JwtBuilder getJwtBuilder(String subject, Long ttlMills, String uuid){
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        SecretKey secretKey = generalKey();
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        if(ttlMills == null){
            ttlMills=JwtUtil.JWT_TTL;
        }
        long expMills = nowMillis + ttlMills;
        Date expDate = new Date(expMills);
        return Jwts.builder()
                .setId(uuid)             //唯一的ID
                .setSubject(subject)     //主题   可以是JSON数据
                .setIssuer("sg")         //签发者
                .setIssuedAt(now)        //签发时间
                .signWith(signatureAlgorithm, secretKey)   //使用HS256对称加密算法签名，第二个参数为秘钥
                .setExpiration(expDate);
    }

    /**
     * 创建token
     * @param id
     * @param subject
     * @param ttlMillis
     * @return
     */
    public static String createJWT(String id,String subject,Long ttlMillis){
        JwtBuilder builder = getJwtBuilder(subject, ttlMillis, id); //设置过期时间
        return builder.compact();
    }

    public static void main(String[] args) throws Exception{
        String jwt = createJWT("1540618175123570690");
        System.out.println(jwt);

    }

    /**
     * 生成加密后的秘钥 secretKey
     * @return
     */
    public static SecretKey generalKey(){
        byte[] encodeKey = Base64.getDecoder().decode(JwtUtil.JWT_KEY);
        SecretKey key = new SecretKeySpec(encodeKey, 0 ,encodeKey.length,"AES");
        return key;
    }

    /**
     * 解析
     * @param jwt
     * @return
     * @throws Exception
     */
    public static Claims parseJWT(String jwt) throws Exception{
        SecretKey secretKey = generalKey();
        Claims claims = Jwts.parser()
                    .setSigningKey(secretKey) // 设置标识名
                    .parseClaimsJws(jwt)  //解析token
                    .getBody();

        return claims;
    }

}
