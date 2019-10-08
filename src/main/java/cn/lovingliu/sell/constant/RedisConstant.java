package cn.lovingliu.sell.constant;

/**
 * @Author：LovingLiu
 * @Description: Redis常量
 * @Date：Created in 2019-10-08
 */
public interface RedisConstant {
    Integer EXPIRE = 7200;// 两个小时
    String TOKEN_PREFIX = "token_%s";
}
