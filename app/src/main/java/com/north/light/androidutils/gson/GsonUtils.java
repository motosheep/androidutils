package com.north.light.androidutils.gson;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzt
 * time 2020/9/30
 * 描述：Gson工具类
 */
public class GsonUtils implements Serializable {

    /**
     * 生成json list
     */
    public static <T> List<T> getJsonList(String jsonString, Class<T> cls) {
        List<T> list = new ArrayList<T>();
        try {
            Gson gson = new Gson();
            JsonArray arry = new JsonParser().parse(jsonString).getAsJsonArray();
            for (JsonElement jsonElement : arry) {
                list.add(gson.fromJson(jsonElement, cls));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * obj生成字符串
     */
    public static String getJsonStr(Object obj) {
        try {
            return new Gson().toJson(obj);
        } catch (Exception e) {
            return "";
        }
    }


    /**
     * 字符串生产class
     */
    public static <T> T getClassByStr(String str, Class<T> t) throws Exception {
        return new Gson().fromJson(str, t);

    }
}
