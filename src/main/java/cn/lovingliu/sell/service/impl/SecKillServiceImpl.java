package cn.lovingliu.sell.service.impl;

import cn.lovingliu.sell.exception.SellException;
import cn.lovingliu.sell.service.RedisLock;
import cn.lovingliu.sell.service.SecKillService;
import cn.lovingliu.sell.util.KeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-10-09
 */
@Service
public class SecKillServiceImpl implements SecKillService {
    private static final int TIMEOUT = 10 * 1000; //超时时间 10s

    @Autowired
    private RedisLock redisLock;

    /**
     * 3个map的数据接口 分别模拟的是3张表
     * 国庆活动，皮蛋粥特价，限量100000份
     */
    static Map<String,Integer> products;
    static Map<String,Integer> stock;
    static Map<String,String> orders;
    static
    {
        /**
         * 模拟多个表，商品信息表，库存表，秒杀成功订单表
         */
        products = new HashMap<>();
        stock = new HashMap<>();
        orders = new HashMap<>();
        products.put("123456", 100000); // 商品ID 参加商品的总库存
        stock.put("123456", 100000); // 商品ID 商品的库存
    }

    private String queryMap(String productId)
    {
        return "国庆活动，皮蛋粥特价，限量份"
                + products.get(productId)
                +" 还剩：" + stock.get(productId)+" 份"
                +" 该商品成功下单用户数目："
                +  orders.size() +" 人" ;
    }

    @Override
    public String querySecKillProductInfo(String productId)
    {
        return this.queryMap(productId);
    }

    @Override
    public void orderProductMockDiffUser(String productId)
    {
        // 加锁
        long time = System.currentTimeMillis() + TIMEOUT;
        if(!redisLock.lock(productId,String.valueOf(time))){
            throw new SellException(101,"抢购失败,请稍后再试一试");
        }

        //1.查询该商品库存，为0则活动结束。
        int stockNum = stock.get(productId);

        if(stockNum == 0) {
            throw new SellException(100,"活动结束");
        }else {
            //2.库存足够 减库存
            stockNum =stockNum-1;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            stock.put(productId,stockNum);

            //3.订单信息入库
            orders.put(KeyUtil.getUniqueKey(),productId);
        }


        // 解锁
        redisLock.unlock(productId,String.valueOf(time));
    }
}
