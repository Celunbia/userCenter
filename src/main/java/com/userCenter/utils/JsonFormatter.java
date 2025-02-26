package com.userCenter.utils;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * @version 1.0
 * @Author 李嘉烨
 * @Date 2025/2/17 20:21
 * @注释
 */
public class JsonFormatter {
    private static volatile ObjectMapper instance;

    private JsonFormatter() {}

    public static ObjectMapper getInstance() {
        if (instance == null) {
            synchronized (JsonFormatter.class) {
                if (instance == null) {
                    instance = new ObjectMapper();
                    instance.enable(SerializationFeature.INDENT_OUTPUT);
                    // JSON 格式化输出
                    instance.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

                    // 忽略未知字段
                }
            }
        }
        return instance;
    }
}
