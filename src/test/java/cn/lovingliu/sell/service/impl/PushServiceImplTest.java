package cn.lovingliu.sell.service.impl;

import cn.lovingliu.sell.dto.OrderDTO;
import cn.lovingliu.sell.service.OrderService;
import cn.lovingliu.sell.service.PushService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-10-08
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PushServiceImplTest {
    @Autowired
    private PushService pushService;
    @Autowired
    private OrderService orderService;

    @Test
    public void orderStatus() {
        OrderDTO orderDTO = orderService.findOne("1569146190935149862");
        pushService.orderStatus(orderDTO);
    }
}