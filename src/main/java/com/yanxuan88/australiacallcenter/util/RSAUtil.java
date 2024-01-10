package com.yanxuan88.australiacallcenter.util;

import com.yanxuan88.australiacallcenter.exception.BizException;

import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public final class RSAUtil {
    /**
     * 生成密钥对
     *
     * @return KeyPair
     */
    public static KeyPair generate() {
        KeyPairGenerator generator = null;
        try {
            generator = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        generator.initialize(2048, new SecureRandom());
        return generator.generateKeyPair();
    }

    /**
     * 通过公钥获取对应的字符串
     *
     * @param publicKey 公钥
     * @return string
     */
    public static String getPublicKeyStr(PublicKey publicKey) {
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }

    /**
     * 通过私钥获取对应的字符串
     *
     * @param privateKey 私钥
     * @return string
     */
    public static String getPrivateKeyStr(PrivateKey privateKey) {
        return Base64.getEncoder().encodeToString(privateKey.getEncoded());
    }

    /**
     * 通过公钥字符串获取公钥
     *
     * @param publicKeyStr 公钥字符串
     * @return PublicKey
     */
    public static PublicKey getPublicKey(String publicKeyStr) {
        try {
            byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyStr);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
            return keyFactory.generatePublic(publicKeySpec);
        } catch (Exception e) {
            throw new BizException("获取公钥失败");
        }
    }

    /**
     * 通过私钥字符串获取私钥
     *
     * @param privateKeyStr 私钥字符串
     * @return PrivateKey
     */
    public static PrivateKey getPrivateKey(String privateKeyStr) {
        try {
            byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyStr);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            return keyFactory.generatePrivate(privateKeySpec);
        } catch (Exception e) {
            throw new BizException("获取私钥失败");
        }
    }

    public static void main(String[] args) {
        KeyPair keyPair = generate();
        System.out.println(getPublicKeyStr(keyPair.getPublic()));
        System.out.println(getPrivateKeyStr(keyPair.getPrivate()));
    }
}
