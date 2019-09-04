package com.yanglf.payment.utils;

import sun.misc.BASE64Encoder;

import java.security.MessageDigest;

/**
 * @author yanglf
 */
public class SecurityUtil {

    /**
     * 加密（MD5、Base64）
     *
     * @param clearText
     * @return
     */
    public static String encryptionWithMd5Base64(String clearText) {
        String cipherText;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            BASE64Encoder base64en = new BASE64Encoder();
            cipherText = base64en.encode(md5.digest(clearText.getBytes("utf-8")));
        } catch (Exception e) {
            throw new RuntimeException("加密失败[clearText: " + clearText + "]");
        }
        return cipherText;
    }

    /**
     * 加密(MD5)
     *
     * @param clearText
     * @return
     */
    public static String encryptionWithMd5(String clearText) {
        String cipherText;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            cipherText = byteArrayToHexString(md5.digest(clearText
                    .getBytes("utf-8")));
        } catch (Exception e) {
            throw new RuntimeException("加密失败[clearText: " + clearText + "]");
        }
        return cipherText;
    }

    /**
     * 字节数组转换为字符串
     *
     * @param b
     * @return
     */
    private static String byteArrayToHexString(byte b[]) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    /**
     * 字节转字符串
     *
     * @param b
     * @return
     */
    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n += 256;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return HEXDIGITS[d1] + HEXDIGITS[d2];
    }

    private static final String HEXDIGITS[] = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

}
