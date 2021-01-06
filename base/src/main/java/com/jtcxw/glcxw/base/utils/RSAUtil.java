package com.jtcxw.glcxw.base.utils;

import android.util.Base64;

import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class RSAUtil {

    public static final String aesKey = "123456";

    public static String pKey = "";
//            "\n" +
//            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDKyMXO8kSEGKgWJq2IGd6O8lQk\n" +
//            "DHjI13Etm9Bt2dnQ+ilah/DMVMlcl6DXAJBTFF6VdUxgduklmXxik+Nb4U8foEVn\n" +
//            "1sRQZY+s0OnXEXW6tpyWHv/JVZQVeObgbWNsIAG5DHU75TbUC163P/g2NLxU+4+F\n" +
//            "7nfVrgcTRSqmjEgYaQIDAQAB\n";
    /**RSA算法*/
    public static final String RSA = "RSA";
    /**加密方式，android的*/
//  public static final String TRANSFORMATION = "RSA/None/NoPadding";
    /**加密方式，标准jdk的*/
    public static final String TRANSFORMATION = "RSA/ECB/PKCS1Padding";

    /** 使用公钥加密 */
    public static String encrypt(byte[] data) throws Exception {

        // 得到公钥对象
        byte[] keyBytes = Base64.decode(pKey, Base64.DEFAULT);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey pubKey = keyFactory.generatePublic(keySpec);
        // 加密数据
        Cipher cp = Cipher.getInstance(TRANSFORMATION);
        cp.init(Cipher.ENCRYPT_MODE, pubKey);
        String enCrypt = Base64.encodeToString(cp.doFinal(data), Base64.DEFAULT);
        return enCrypt;
    }

    /** 使用私钥解密 */
    public static byte[] decryptByPrivateKey(byte[] encrypted, byte[] privateKey) throws Exception {
        // 得到私钥对象
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKey);
        KeyFactory kf = KeyFactory.getInstance(RSA);
        PrivateKey keyPrivate = kf.generatePrivate(keySpec);
        // 解密数据
        Cipher cp = Cipher.getInstance(TRANSFORMATION);
        cp.init(Cipher.DECRYPT_MODE, keyPrivate);
        byte[] arr = cp.doFinal(encrypted);
        return arr;
    }

    public static Key getKey(String strKey) {
        try {
            if (strKey == null) {
                strKey = "";
            }
            KeyGenerator _generator = KeyGenerator.getInstance("AES");
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(strKey.getBytes());
            _generator.init(128, secureRandom);
            return _generator.generateKey();
        } catch (Exception e) {
            throw new RuntimeException(" 密钥出现异常 ");
        }
    }

        public static String CIPHER_ALGORITHM = "AES/CBC/PKCS7Padding";

    public static String aesEncrypt(String data) throws Exception {
        byte[] iv = { 0x31, 0x31, 0x31, 0x31, 0x31, 0x31, 0x31, 0x31, 0x31, 0x31, 0x31, 0x31, 0x31, 0x31, 0x31, 0x31 };

        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        byte[] realKey = getSecretKey(aesKey);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(realKey,"AES"),ivParameterSpec);
        byte[] bt = cipher.doFinal(data.getBytes("UTF-8"));
        String strS = Base64.encodeToString(bt, Base64.DEFAULT);
        return strS;
    }

    public static String aesDecrypt(String data) throws Exception {
        byte[] decodeData = Base64.decode(data, Base64.DEFAULT);
        byte[] iv = { 0x31, 0x31, 0x31, 0x31, 0x31, 0x31, 0x31, 0x31, 0x31, 0x31, 0x31, 0x31, 0x31, 0x31, 0x31, 0x31 };

        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        byte[] realKey = getSecretKey(aesKey);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(realKey,"AES"),ivParameterSpec);
        byte[] bt = cipher.doFinal(decodeData);
        return new String(bt, Charset.forName("UTF-8"));
    }

    /**
     * 对密钥key进行处理：如密钥长度不够位数的则 以指定paddingChar 进行填充；
     * 此处用空格字符填充，也可以 0 填充，具体可根据实际项目需求做变更
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] getSecretKey(String key) throws Exception{
        final byte paddingChar = 0x0;

        byte[] realKey = new byte[16];
        byte[] byteKey = key.getBytes("UTF-8");
        for (int i =0;i<realKey.length;i++){
            if (i<byteKey.length){
                realKey[i] = byteKey[i];
            }else {
                realKey[i] = paddingChar;
            }
        }

        return realKey;
    }

    }
