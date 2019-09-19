package cn.lovingliu.sell.util;

import java.util.Random;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-09-20
 */
public class KeyUtil {
    /**
     * @Desc 生成唯一的主键
     * 时间+随机数
     * @Author LovingLiu
    */
    public static synchronized String getUniqueKey(){
        Random random = new Random();
        Integer a = random.nextInt(900000)+10000;// 生成6位随机数
        return  System.currentTimeMillis() + String.valueOf(a);
    }
}
