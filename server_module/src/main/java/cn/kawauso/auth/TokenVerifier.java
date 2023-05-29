package cn.kawauso.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;

/**
 * {@link TokenVerifier}是一个基于jwt的封装工具类，提供了token分发、token校验和信息提取的能力
 *
 * @author RealDragonking
 */
@Service
public final class TokenVerifier {

    private final String secretKey;
    private final long timeout;

    public TokenVerifier(@Value("${security.secret-key}") String secretKey,
                         @Value("${security.token-timeout}") long timeout) {
        this.secretKey = secretKey;
        this.timeout = timeout;
    }

    /**
     * 使用给定的头部信息和本地密钥，签发一份token
     *
     * @param headers 头部键值对信息
     * @return token
     */
    public String generateToken(Map<String, Object> headers) {
        Instant timeoutInstant = Instant.now().plusMillis(timeout);
        return JWT.create()
                .withHeader(headers)
                .withExpiresAt(timeoutInstant)
                .sign(Algorithm.HMAC256(secretKey));
    }

    /**
     * 对给定的token进行校验，从中提取数据
     *
     * @param token 用于权限鉴定的token
     * @return {@link DecodedJWT}
     */
    public DecodedJWT verifyToken(String token) {
        return JWT.require(Algorithm.HMAC256(secretKey)).build().verify(token);
    }

}
