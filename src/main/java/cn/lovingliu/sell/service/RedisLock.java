package cn.lovingliu.sell.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @Author：LovingLiu
 * @Description: Redis分布式锁的 加锁解锁
 * @Date：Created in 2019-10-09
 */
@Component
@Slf4j
public class RedisLock {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * @Desc 加锁
     * @param key 包含productId
     * @param value 当前时间+超时时间
     * @Author LovingLiu
    */
    public boolean lock(String key,String value){
        // redis setNx 代表只在键不存在时，才对键进行设置操作。
        if(stringRedisTemplate.opsForValue().setIfAbsent(key,value)){
            return true;
        }
        /**
         * 避免在加锁->解锁的过程中出现异常,导致死锁（锁一直不释放）
         * 1.x,y 两个线程进入这里 携带的都是 key:12345 value:B
        */

        String currentValue = stringRedisTemplate.opsForValue().get(key);
        /**
         * 2.此时 currentValue = A
        */

        // 如果锁过期
        if(StringUtils.isNotBlank(currentValue)
                && Long.parseLong(currentValue) < System.currentTimeMillis()){
            // 获取上一个锁的时间 并设置新的时间
            /**
             * 3.此时 x 线程将获得oldValue = A  且 oldValue == currentValue x线程获得锁成功
             * 4.紧接着 y 线程来到这里 oldValue = B 且oldValue != currentValue y线程获得锁失败
             */
            String oldValue = stringRedisTemplate.opsForValue().getAndSet(key,value);

            if(StringUtils.isNotBlank(oldValue) && oldValue.equals(currentValue)){
                return true;
            }
        }


        return false;
    }

    /**
     * @Desc 解锁
     * @Author LovingLiu
    */
    public void unlock(String key,String value){
        try{
            String currentValue = stringRedisTemplate.opsForValue().get(key);
            if(StringUtils.isNotBlank(currentValue) && currentValue.equals(value)){
                stringRedisTemplate.opsForValue().getOperations().delete(key);
            }
        }catch (Exception e){
            log.error("【redis分布式解锁失败】,{}",e.getMessage());
        }

    }

}
