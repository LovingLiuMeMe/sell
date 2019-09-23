package cn.lovingliu.sell.service.impl;

import cn.lovingliu.sell.dto.OrderDTO;
import cn.lovingliu.sell.service.PayService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-09-24
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class PayServiceImplTest {

    @Autowired
    private PayService payService;

    @Test
    public void create() {
        OrderDTO orderDTO = new OrderDTO();
        payService.create(orderDTO);
    }

    @Test
    public void notify1() {
    }

    @Test
    public void refund() {
    }
}