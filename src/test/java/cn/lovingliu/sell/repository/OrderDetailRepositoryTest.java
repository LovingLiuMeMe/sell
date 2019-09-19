package cn.lovingliu.sell.repository;

import cn.lovingliu.sell.dataobject.OrderDetail;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-09-19
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderDetailRepositoryTest {
    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Test
    public void save(){
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setDetailId("test");
        orderDetail.setOrderId("test");
        orderDetail.setProductIcon("icon.png");
        orderDetail.setProductId("test");
        orderDetail.setProductName("毛绒玩具");
        orderDetail.setProductPrice(new BigDecimal(112.3));
        orderDetail.setProductQuantity(12);
        orderDetailRepository.save(orderDetail);
    }
    @Test
    public void findByOrderId() {
        List<OrderDetail> orderDetailList = orderDetailRepository.findByOrderId("test");
        System.out.println(orderDetailList);
        Assert.assertNotEquals(0,orderDetailList.size());
    }
}