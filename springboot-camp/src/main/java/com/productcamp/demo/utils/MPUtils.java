package com.productcamp.demo.utils;

import com.alibaba.fastjson.JSONObject;
import com.productcamp.demo.config.WXAppConfig;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.codehaus.xfire.util.Base64;
import org.springframework.web.client.RestTemplate;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidParameterSpecException;
import java.util.Arrays;

/**
 * MPUtils
 *
 * @version 1.0
 */
public class MPUtils {

    private static String APPID = WXAppConfig.APP_ID;
    private static String APPSECRET = WXAppConfig.APP_SECRET;

    public static JSONObject decryptByAES(String encryptedData, String sessionkey, String iv) {
        // 被加密的数据
        byte[] dataByte = Base64.decode(encryptedData);
        // 加密秘钥
        byte[] keyByte = Base64.decode(sessionkey);
        // 偏移量
        byte[] ivByte = Base64.decode(iv);
        try {
            // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
            int base = 16;
            if (keyByte.length % base != 0) {
                int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
                byte[] temp = new byte[groups * base];
                Arrays.fill(temp, (byte) 0);
                System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
                keyByte = temp;
            }
            // 初始化
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivByte));
            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
            byte[] resultByte = cipher.doFinal(dataByte);
            if (null != resultByte && resultByte.length > 0) {
                String jsonString = new String(resultByte, "UTF-8");
                JSONObject jsonObject = JSONObject.parseObject(jsonString);
                return jsonObject;
            }
        } catch (NoSuchAlgorithmException | InvalidParameterSpecException | IllegalBlockSizeException | InvalidKeyException | InvalidAlgorithmParameterException | UnsupportedEncodingException | NoSuchPaddingException | BadPaddingException | NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String encryptSha1(String data) {
        if (data == null) {
            return null;
        }
        String result = "";
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            sha.update(data.getBytes("utf-8"));
            result = byteToHex(sha.digest());

        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();

        }
        return result;
    }

    public static String getSessionKey(String code) {
        String appid = APPID;
        String appSecret = APPSECRET;
        String url = "https://api.weixin.qq.com/sns/jscode2session?" + "appid=" + appid + "&" + "secret=" + appSecret + "&" +
                "js_code=" + code + "&" + "grant_type=authorization_code";
        RestTemplate rest = new RestTemplate();
        String jsonString = rest.getForObject(url, String.class);
        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        String session_key = jsonObject.get("session_key").toString();
        return session_key;
    }

    /**
     * byte数组转hex
     *
     * @param bytes
     * @return
     */
    public static String byteToHex(byte[] bytes) {
        String strHex = "";
        StringBuilder sb = new StringBuilder("");
        for (int n = 0; n < bytes.length; n++) {
            strHex = Integer.toHexString(bytes[n] & 0xFF);
            sb.append((strHex.length() == 1) ? "0" + strHex : strHex); // 每个字节由两个字符表示，位数不够，高位补0
        }
        return sb.toString().trim();
    }
}
