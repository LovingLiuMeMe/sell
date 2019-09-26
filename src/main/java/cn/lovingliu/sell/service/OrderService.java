package cn.lovingliu.sell.service;

import cn.lovingliu.sell.dto.OrderDTO;
import cn.lovingliu.sell.vo.OrderVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-09-19
 */
public interface OrderService {
    // 创建订单
    OrderDTO createrOrder(OrderDTO orderDTO);
    // 查询单个订单
    OrderDTO findOne(String orderId);
    // 查询订单列表
    Page<OrderDTO> findList(String buyerOpenid, Pageable pageable);
    // 查询订单列表(卖家端)
    Page<OrderVO> findList(Pageable pageable) ;
    // 取消订单
    OrderDTO cancel(OrderDTO orderDTO);
    // 完成订单
    OrderDTO finish(OrderDTO orderDTO);
    // 支付订单
    OrderDTO paid(OrderDTO orderDTO);

}
