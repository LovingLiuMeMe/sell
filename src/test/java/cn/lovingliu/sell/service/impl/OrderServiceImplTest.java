package cn.lovingliu.sell.service.impl;

import cn.lovingliu.sell.dataobject.OrderDetail;
import cn.lovingliu.sell.dto.OrderDTO;
import cn.lovingliu.sell.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-09-20
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class OrderServiceImplTest {

    @Autowired
    private OrderService orderService;

    @Test
    public void createrOrder() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderId("1");
        orderDTO.setBuyerName("刘波");
        orderDTO.setBuyerPhone("19828404479");
        orderDTO.setBuyerAddress("四川成都");
        orderDTO.setBuyerOpenid("openid=123");
        orderDTO.setOrderAmount(new BigDecimal("123.45"));

        OrderDetail detail_1 = new OrderDetail();
        detail_1.setProductId("test");
        detail_1.setProductQuantity(12);

        OrderDetail detail_2 = new OrderDetail();
        detail_2.setProductId("test1");
        detail_2.setProductQuantity(12);
        List<OrderDetail> orderDetailList = Lists.newArrayList(detail_1,detail_2);

        orderDTO.setOrderDetailList(orderDetailList);

        OrderDTO resultOrderDTO = orderService.createrOrder(orderDTO);
        log.info("result:   {}",resultOrderDTO);
    }

    @Test
    public void findOne() {
        OrderDTO orderDTO = orderService.findOne("1569131609684277406");
        System.out.println(orderDTO);
        Assert.assertNotNull(orderDTO);
    }

    @Test
    public void findList() {
        Sort sort = new Sort(Sort.Direction.DESC,"updateTime");
        PageRequest pageRequest = PageRequest.of(0,10,sort);
        Page<OrderDTO> orderDTOPage = orderService.findList("openid=123",pageRequest);
        log.info("orderDTOPage:{}",orderDTOPage.getContent());
        Assert.assertNotNull(orderDTOPage);
    }

    @Test
    public void cancel() {
        OrderDTO orderDTO = orderService.findOne("1569131609684277406");
        OrderDTO result = orderService.cancel(orderDTO);
        log.info("cancel order result :{}",result);
        Assert.assertNotNull(result);
    }

    @Test
    public void finish() {
        OrderDTO orderDTO = orderService.findOne("1569138463406704694");
        OrderDTO result = orderService.finish(orderDTO);
        log.info("finish order result :{}",result);
        Assert.assertNotNull(result);
    }

    @Test
    public void paid() {
        OrderDTO orderDTO = orderService.findOne("1569138463406704694");
        OrderDTO result = orderService.paid(orderDTO);
        log.info("finish order result :{}",result);
        Assert.assertNotNull(result);
    }
    @Test
    public void findAll(){
        Sort sort = new Sort(Sort.Direction.DESC,"updateTime");
        PageRequest pageRequest = PageRequest.of(0,10,sort);
        Page<OrderDTO> page = orderService.findList(pageRequest);
        Assert.assertTrue("查询所有订单列表",page.getTotalElements() > 0);
    }
}