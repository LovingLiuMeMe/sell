package cn.lovingliu.sell.service;

import cn.lovingliu.sell.dto.OrderDTO;

/**
 * @Author：LovingLiu
 * @Description: 微信模版推送
 * @Date：Created in 2019-10-08
 */
public interface PushService {
    /**
     * @Desc 订单状态变更通知
     * @Author LovingLiu
    */
    void orderStatus(OrderDTO orderDTO);
}
