package cn.lovingliu.sell.service;

import cn.lovingliu.sell.dto.OrderDTO;

/**
 * @Author：LovingLiu
 * @Description: 买家的Service 主要是避免在controller进行繁琐的业务逻辑操作 解决越权问题
 * @Date：Created in 2019-09-22
 */
public interface BuyerService {
    OrderDTO findOrderOne(String openid,String orderId);
    OrderDTO cancelOrder(String openid,String orderId);
}
