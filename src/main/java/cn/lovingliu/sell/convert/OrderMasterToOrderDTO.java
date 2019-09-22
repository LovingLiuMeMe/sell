package cn.lovingliu.sell.convert;

import cn.lovingliu.sell.dataobject.OrderDetail;
import cn.lovingliu.sell.dataobject.OrderMaster;
import cn.lovingliu.sell.dto.OrderDTO;
import cn.lovingliu.sell.enums.ResultStatusEnum;
import cn.lovingliu.sell.exception.SellException;
import cn.lovingliu.sell.repository.OrderDetailRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-09-21
 */
@Component
public class OrderMasterToOrderDTO {
    private static OrderDetailRepository orderDetailRepository;
    @Autowired
    public void setOrderDetailRepository(OrderDetailRepository orderDetailRepository){
        OrderMasterToOrderDTO.orderDetailRepository = orderDetailRepository;
    }

    public static OrderDTO convert(OrderMaster orderMaster){
        if(orderMaster == null){
            throw new SellException(ResultStatusEnum.ORDER_NOT_EXIT);
        }
        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(orderMaster,orderDTO);
        List<OrderDetail> orderDetailList = orderDetailRepository.findByOrderId(orderMaster.getOrderId());
        if(CollectionUtils.isEmpty(orderDetailList)){
            throw new SellException(ResultStatusEnum.ORDER_DETAIL_NOT_EXIT);
        }
        orderDTO.setOrderDetailList(orderDetailList);
        return orderDTO;
    }

    public static List<OrderDTO> convert(List<OrderMaster> orderMasterList){
        return orderMasterList.stream().map(e ->
            convert(e)
        ).collect(Collectors.toList());
    }
}
