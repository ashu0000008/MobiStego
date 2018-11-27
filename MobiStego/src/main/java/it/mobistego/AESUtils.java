package it.mobistego;

import android.text.TextUtils;
import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.spec.AlgorithmParameterSpec;

/**
 * <pre>
 * company:第伍区科技
 * author : whoislcj
 * time   : 2018/5/9
 * desc   : AES加解密算法
 * version: 1.0
 * </pre>
 */

public class AESUtils {
    private static final String HEX = "0123456789ABCDEF";
    private static final String CBC_PKCS7_PADDING = "AES/CBC/PKCS7PADDING";//AES是加密方式 CBC是工作模式 PKCS5Padding是填充模式
    private static final String AES = "AES";//AES 加密
    private static final int KEY_SIZE = 32;


    //二进制转字符
    public static String toHex(byte[] buf) {
        if (buf == null)
            return "";
        StringBuffer result = new StringBuffer(2 * buf.length);
        for (int i = 0; i < buf.length; i++) {
            appendHex(result, buf[i]);
        }
        return result.toString();
    }

    private static void appendHex(StringBuffer sb, byte b) {
        sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
    }


    private static SecretKey deriveKeyInsecurely(String password) {
        byte[] passwordBytes = password.getBytes(StandardCharsets.US_ASCII);
        return new SecretKeySpec(InsecureSHA1PRNGKeyDerivator.deriveInsecureKey(passwordBytes, KEY_SIZE), AES);
    }

    /*
     * 加密
     */
    public static String encrypt(String password, String cleartext) {
        if (TextUtils.isEmpty(cleartext)) {
            return cleartext;
        }
        try {
            byte[] result = encrypt(password, cleartext.getBytes());
            return Base64.encodeToString(result, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /*
     * 加密
     */
    private static byte[] encrypt(String password, byte[] clear) throws Exception {
        SecretKey secretKey = deriveKeyInsecurely(password);
        Cipher cipher = Cipher.getInstance(CBC_PKCS7_PADDING);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, makeIv());
        byte[] result = cipher.doFinal(clear);

        return result;
    }

    /*
     * 解密
     */
    public static String decrypt(String password, String encrypted) {
        if (TextUtils.isEmpty(encrypted)) {
            return encrypted;
        }
        try {
            byte[] enc = Base64.decode(encrypted, Base64.DEFAULT);
            byte[] result = decrypt(password, enc);
            return new String(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
     * 解密
     */
    private static byte[] decrypt(String password, byte[] encrypted) throws Exception {
        SecretKey secretKey = deriveKeyInsecurely(password);
        Cipher cipher = Cipher.getInstance(CBC_PKCS7_PADDING);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, makeIv());
        return cipher.doFinal(encrypted);
    }

    private static AlgorithmParameterSpec makeIv() {
        try {
            return new IvParameterSpec("4e5Wa71fYoT7MFEX".getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
