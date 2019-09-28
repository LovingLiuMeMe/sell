package cn.lovingliu.sell.util;

import cn.lovingliu.sell.enums.CodeEnum;

/**
 * @Author：LovingLiu
 * @Description: Enum的工具类 在OrderDTO->OrderVO 使用
 * @Date：Created in 2019-09-27
 */
public class CodeEnumUtil {
    public static  <T extends CodeEnum> T getByCode(Integer code,Class<T> enumClass){
        for(T each: enumClass.getEnumConstants()){
            if(code.equals(each.getCode())){
                return each;
            }
        }
        return null;
    }
}
