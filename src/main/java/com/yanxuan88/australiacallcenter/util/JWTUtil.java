package com.yanxuan88.australiacallcenter.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.yanxuan88.australiacallcenter.common.Constant;
import com.yanxuan88.australiacallcenter.exception.BizException;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Collections;
import java.util.Map;

public final class JWTUtil {
    private static final String TOKEN_PREFIX = "Bearer ";

    private static final String PUBLIC_KEY_STR = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwfYAT44vmaP9vGw9gDD8fuyrkYP4xaSfttRGr0Cz8EMoZZs3U8wyuoIc7EjX0zuUQH0tcf/jsZLaxaQwAxe7AVXpliTStEr0d7/Kf5TANKJMZbwvsc6N0yoCxblh5kAo29HXGgmDo9KSHcKByc4YUFLNuXCv82dCY0U9S4EcB9In4EsZTWC4BbuhbHWRjW00bw5aXDQ3NQjsPtrRecMRHB+8zAMUqF1dj14HICDNiTxQaZnFfsoQwvqS4uUZvMzRIILhAIl+XGNI30W+EgFblc1j4n6b8xK2fI4rQ+OArazA2Bj3MQqC+sn3SsMYxXG/o9KKOXv90eUPBpfvzk1XmQIDAQAB";
    private static final String PRIVATE_KEY_STR = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDB9gBPji+Zo/28bD2AMPx+7KuRg/jFpJ+21EavQLPwQyhlmzdTzDK6ghzsSNfTO5RAfS1x/+OxktrFpDADF7sBVemWJNK0SvR3v8p/lMA0okxlvC+xzo3TKgLFuWHmQCjb0dcaCYOj0pIdwoHJzhhQUs25cK/zZ0JjRT1LgRwH0ifgSxlNYLgFu6FsdZGNbTRvDlpcNDc1COw+2tF5wxEcH7zMAxSoXV2PXgcgIM2JPFBpmcV+yhDC+pLi5Rm8zNEgguEAiX5cY0jfRb4SAVuVzWPifpvzErZ8jitD44CtrMDYGPcxCoL6yfdKwxjFcb+j0oo5e/3R5Q8Gl+/OTVeZAgMBAAECggEAJ3MS7htjyBKILZYwTg3olSIq5R5b70sD5wo21DdLQc9O/Jd8wdDy8mc7Xb2VFBP2m8U6BIlAtOHDGFbEIyTk9qkUKOMVbfEe1upN0/QbwbjO5BRJ+RajVvMg/Dkv1cZv6QG9h8CuoxfVezMP9c0yuceVqyVlzBJQ+OLgzCYW3un+5ZBfJbmcxsQxGjjgzJVaLH1K41Knm3AsR+QmQ7cobveQRy/dzbbYdffaomDhpa9dfhcRx64lWdBtVJTkiB3MDB+J9nV1WcU5fF2lXGxAAJovXKPpFe+QCceK1ycuSgU6d04CJukwVLaBtEgUFncJi7CKITOXDXVgAkUhRrB+OQKBgQD3xrPB9storeHiKghENP21RQ7nR6scz5+Iah9qITqPBXe7YTvoqIlU+3L93A48ZCxeYg3JZcYA9lmPOqQlLZuSAVwWXVzdBcZPNWrUxCydhNxxr35fxX+dEUB9Y0Oc0mrQ1t+vtPB8MsgCVyEXdldvu4X4lDqiUNLEPRGXSInbvQKBgQDIZgsPWqPlgP1KGQyNzMeY3TSJM+gub3DxcRD7ApSV5/ymL6HABjr9rqffXVZLdM2p7FdMAdEoKYyutgeHU+iApJIU4Frj8E3OH3KwL3vyeKW86rc4j4x5ULsl/Co1l2AUskBKQvL3qKhiRgnNyhLGFZCboyrTETXVJBhpmYVbDQKBgQDq1bbVZTUQd40ZL8E59Adp4JPpfDGfUuDfB8XSbr/zgbkjLVr8fY/7rX4lQIV1k3PB2Kk8cmshylrDt06PthQ2Y3eF8uLq0GhN9lDUa0Hl4WCVl0DtQGBc21cGACi632tBmAXVoGY1MGQEY9QHSrBAQ7kMCmEwKe4487BSc/vE+QKBgFU5SFTUSEVPEOjVNEiNoQi6ciAzSfeNfyu+2OjW1RfXW9PVB+XhAG9oOo48ZHmch+ZO02VIlImO+fxG/3am0Yb79gAY1a6AQKZ7K0wQNZcNhfaMnzTXn3bsRVrHHbBC2yKSF9M9G5UFTe7VOFAjMs641xHJV2oBtLIbMqKeHl91AoGASAxwY4HCyTHp8Wfasxmt7Fxdb66MvdOLRWsOMHkgqK/+c0jFq1aNwdy7QBwrmxji0NcJmr9MHYwzjjwMvcboWTX4F8NDbZlsbIMI1Dgth1SuI49LjJqshb5X3/kxiuc/Sp3tBfXc3DF2/QYvkMLb6Q0v8WH7w4l+TlRtBbGciJo=";

    public static DecodedJWT decodedJWT(String token) {
        if (token == null || token.trim().length() <= 0) {
            throw new RuntimeException("token为空，无法校验");
        }

        if (token.startsWith(TOKEN_PREFIX)) {
            token = token.substring(TOKEN_PREFIX.length());
        }

        RSAPublicKey publicKey = (RSAPublicKey) RSAUtil.getPublicKey(PUBLIC_KEY_STR);
        Algorithm algorithm = Algorithm.RSA256(publicKey, null);
        JWTVerifier verifier = JWT.require(algorithm).build(); //Reusable verifier instance
        return verifier.verify(token);
    }

    public static String generateToken(Map<String, String> payload) {
        JWTCreator.Builder builder = JWT.create();
        payload.forEach((k, v) -> builder.withClaim(k, v));
        RSAPrivateKey privateKey = (RSAPrivateKey) RSAUtil.getPrivateKey(PRIVATE_KEY_STR);
        return TOKEN_PREFIX + builder.sign(Algorithm.RSA256(null, privateKey));
    }

    public static void main(String[] args) {
//        System.out.println(generateToken(Collections.singletonMap("aa", "aa")));
        DecodedJWT decodedJWT = decodedJWT("Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhYSI6ImFhIn0.qxP8_SsbHvXX9Lh6Jt_BWT6Pmz7oHXwrTpdhozNQB5D0rmT6m2M1hRQoIBs5QroGb46JH-evFpDBlhy4wUnvATHLC-3DaU8qyA5T5sl0XZVPWn7b5Z6uRWwz1nMvVSRULTVEuI1vktB6eYRO501MoGZg23kYhnSESue5PS8zva1RIeaQ7Nc-ztxWNUqLddg7q7S0GesjeIytFa35hPRVGlr4bUd49ALlF6LhvORtPP_oRrS9JyfzNNRbuUfCAhmXPDPs3Ty9EHnDLCkV-d8W15z1_6XRGURZOLYojr1WKSH9EK-NPSWMLNvVChbK8YTSyl1T4bqqcARSE7ic2Ezgng");
        System.out.println(decodedJWT.getClaim("aa").asString());
        System.out.println(decodedJWT.getPayload());
        System.out.println(decodedJWT.getClaims());
    }
}
