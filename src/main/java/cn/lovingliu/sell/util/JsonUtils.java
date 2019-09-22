package cn.lovingliu.sell.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-09-22
 */
public class JsonUtils {
    public static String toJson(Object obj) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        return gson.toJson(obj);
    }
}
