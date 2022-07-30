package com.pepsiwyl.utils;

import java.util.Random;

/**
 * @author by pepsi-wyl
 * @date 2022-04-16 14:17
 */

// 盐 随机
public class SaltUtils {

    // 字符字典
    private final static String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~!@#$%^&*()_+-";

    /**
     * 生成 随机盐
     *
     * @param length 生成的长度
     * @return 随机盐
     */
    public static String getSalt(int length) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < length; i++) buffer.append(str.charAt(new Random().nextInt(str.length())));
        return buffer.toString();
    }

    public static void main(String[] args) {
        System.out.println(getSalt(ConstUtils.SaltNumber));
    }

}