package com.miko.util;

import com.alibaba.fastjson2.JSONObject;


import java.io.*;
import java.nio.charset.StandardCharsets;

import java.util.HashMap;

public class JsonUtil {
    public static HashMap parseJson(String path) throws IOException {
        BufferedReader reader;
        StringBuilder str = new StringBuilder();
        InputStream fileInputStream = ClassLoader.getSystemClassLoader().getSystemResourceAsStream(path);
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
        reader = new BufferedReader(inputStreamReader);
        // 读取文件信息
        String tempString;
        while ((tempString = reader.readLine()) != null) {
            str.append(tempString);
        }
        reader.close();
        // 转换为HashMap对象
        return JSONObject.parseObject(str.toString(), HashMap.class);
//        return (HashMap<String, Object>) JSON.parse(str.toString());
    }
}
