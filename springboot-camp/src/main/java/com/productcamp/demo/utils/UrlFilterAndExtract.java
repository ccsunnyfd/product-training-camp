package com.productcamp.demo.utils;

import com.productcamp.demo.config.AliyunOSSConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * UrlFilterAndExtract
 *
 * @version 1.0
 */
public class UrlFilterAndExtract {
    private static String ossUrl = AliyunOSSConfig.OSS_URL;
//    private static String bucketName = AliyunOSSConfig.BUCKET_NAME;

    /**
     * 过滤url数组，筛选出oss的url对象名称路径
     *
     * @param array 需要处理的url数组
     * @return 返回符合条件的url对象名称，不包含url前缀，但包含全部对象名称路径，不包含bucketName
     */
    public static List<String> filterOssUrlArray(List<String> array) {
        String ossUrlReg = ossUrl + "(.*?)/(.*)";
        // 创建 Pattern 对象
        Pattern r = Pattern.compile(ossUrlReg, Pattern.CASE_INSENSITIVE);
        List<String> result = new ArrayList<>();
        for (String s : array) {
            Matcher m = r.matcher(s);
            if (m.find()) {
                result.add(m.group(2));
            }
        }
        return result;
    }

    /**
     * 从文本字符串中提取出url,返回url数组
     * @param val 文本字符串，例如html
     * @return 返回url数组
     */
    public static List<String> extractUrlFromString(String val) {
        String urlReg = "<img\\b.*?(?:>|/>)";
        String srcReg = "\\bsrc\\b\\s*=\\s*[\'\"]?([^\'\"]*)[\'\"]?";
        Pattern r = Pattern.compile(urlReg, Pattern.CASE_INSENSITIVE);
        Pattern urlR = Pattern.compile(srcReg, Pattern.CASE_INSENSITIVE);
        Matcher m = r.matcher(val);
        List<String> result = new ArrayList<>();
        while (m.find()) {
            String imgString = m.group();
            Matcher urlM = urlR.matcher(imgString);
            if (urlM.find()) {
                result.add(urlM.group(1));
            }
        }
        return result;
    }
}
