package com.thoseyears.curriculum.util;

import com.alibaba.fastjson.JSON;
import  com.thoseyears.curriculum.entity.*;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import net.sf.json.JSONObject;
import org.apache.tomcat.util.codec.binary.Base64;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {

    static Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static Map<String,String> userTokens = new HashMap<>();

    /**
     * 注册本地TOEKNN
     *
     * @return
     */
    public static void addToken(String Token){
        try{
            Claims claims = JwtUtil.parseJWT(Token);
            //获取token的用户信息
            JSONObject js =  JSONObject.fromObject(claims.getSubject());
            userTokens.put(js.getString("userid"),Token);
        }catch (Exception e){
            System.out.println("添加验证出错");
            e.printStackTrace();
        }
    }
    /**
     * 验证TOKEN
     *
     * @return
     */
    public static boolean checkToken(String Token){
        boolean flag = false;
        //获取当前时间
        long nowMillis = System.currentTimeMillis();//生成JWT的时间
        Date now = new Date(nowMillis);
        try{
            Claims claims = JwtUtil.parseJWT(Token);
            Date expTime = claims.getExpiration();//获取TOKEN过期时间 -1与1
            int timeFlag = expTime.compareTo(now);
            if(timeFlag==1&&userTokens.containsValue(Token))
            {
                //获取token的用户信息
                JSONObject js =  JSONObject.fromObject(claims.getSubject());
                flag = true;
            }
        }catch (Exception e){
            System.out.println("验证TOKEN出错");
            e.printStackTrace();
        }
        return flag;
    }
    /**
     * 由字符串生成加密key
     *
     * @return
     */
    public static SecretKey generalKey() {
        String stringKey = Constant.JWT_SECRET;//本地配置文件中加密的密文7786df7fc3a34e26a61c034d5ec8245d
        byte[] encodedKey = Base64.decodeBase64(stringKey);//本地的密码解码[B@152f6e2
        System.out.println(encodedKey);//[B@152f6e2
        System.out.println(Base64.encodeBase64URLSafeString(encodedKey));//7786df7fc3a34e26a61c034d5ec8245d
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");// 根据给定的字节数组使用AES加密算法构造一个密钥，使用 encodedKey中的始于且包含 0 到前 leng 个字节这是当然是所有。（后面的文章中马上回推出讲解Java加密和解密的一些算法）
        return key;
    }

    /**
     * 创建jwt
     *
     * @param id
     * @param subject
     * @param ttlMillis 过期的时间长度
     * @return
     * @throws Exception
     */
    public static String createJWT(String id, String subject, long ttlMillis) throws Exception {
        long nowMillis = System.currentTimeMillis();//生成JWT的时间
        Date now = new Date(nowMillis);
        //下面就是在为payload添加各种标准声明和私有声明了
        JwtBuilder builder = Jwts.builder() //这里其实就是new一个JwtBuilder，设置jwt的body
                .setId(id)                    //设置jti(JWT ID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
                .setIssuedAt(now)            //iat: jwt的签发时间
                .setSubject(subject)        //sub(Subject)：代表这个JWT的主体，即它的所有人，这个是一个json格式的字符串，可以存放什么userid，roldid之类的，作为什么用户的唯一标志。
                .signWith(key);     //设置签名使用的签名算法和签名使用的秘钥
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);        //设置过期时间
        }
        return builder.compact();
    }

    /**
     * 解密jwt
     *
     * @param jwt
     * @return
     * @throws Exception
     */
    public static Claims parseJWT(String jwt) throws Exception {
        jwt = jwt.replace("Bearer ", "");
        return Jwts.parser().setSigningKey(key).parseClaimsJws(jwt).getBody();
    }


    public static CheckResult validateJWT(String jwtStr) {
        CheckResult checkResult = new CheckResult();
        Claims claims = null;
        try {
            claims = parseJWT(jwtStr);
            checkResult.setSuccess(true);
            checkResult.setClaims(claims);
        } catch (ExpiredJwtException e) {
            checkResult.setErrCode(Constant.JWT_ERRCODE_EXPIRE);
            checkResult.setSuccess(false);
        } catch (SignatureException e) {
            checkResult.setErrCode(Constant.JWT_ERRCODE_FAIL);
            checkResult.setSuccess(false);
        } catch (Exception e) {
            checkResult.setErrCode(Constant.JWT_ERRCODE_FAIL);
            checkResult.setSuccess(false);
        }
        return checkResult;
    }


    /**
     * 生成subject信息
     *
     * @param user
     * @return
     */
    public static String generalSubject(User user) {
        return JSON.toJSONString(user);
    }

}
