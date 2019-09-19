package cn.lovingliu.sell.repository;

import cn.lovingliu.sell.dataobject.OrderMaster;
import cn.lovingliu.sell.enums.OrderStatusEnum;
import cn.lovingliu.sell.enums.PayStatusEnum;
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
 * @Date：Created in 2019-09-19
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderMasterRepositoryTest {
    @Autowired
    private OrderMasterRepository orderMasterRepository;
    @Test
    public void saveTest(){
        OrderMaster orderMaster = new OrderMaster();
        orderMaster.setOrderId("test1");
        orderMaster.setBuyerName("刘波1");
        orderMaster.setBuyerOpenid("1233");
        orderMaster.setBuyerPhone("19828404479");
        orderMaster.setOrderAmount(new BigDecimal(101));
        orderMaster.setOrderStatus(OrderStatusEnum.NEW.getCode());
        orderMaster.setPayStatus(PayStatusEnum.WAIT.getCode());
        orderMaster.setBuyerAddress("四川成都");
        OrderMaster result = orderMasterRepository.save(orderMaster);
        Assert.assertNotNull(result);
        System.out.println(result);
    }

    @Test
    public void findByBuyerOpenid() {
        /**
         * @Desc 在创建sort 对象时,排序属性一定要在声明的Entity中
         * @Author LovingLiu
        */
        Sort sort = new Sort(Sort.Direction.DESC,"updateTime");
        PageRequest request = PageRequest.of(0,10,sort);
        Page<OrderMaster> page = orderMasterRepository.findByBuyerOpenid("123",request);
        List<OrderMaster> orderMasterList = page.getContent();
        Assert.assertNotEquals(0,orderMasterList.size());
        System.out.println(orderMasterList);
    }
}