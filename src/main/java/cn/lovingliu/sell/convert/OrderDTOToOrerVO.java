package cn.lovingliu.sell.convert;

import cn.lovingliu.sell.dto.OrderDTO;
import cn.lovingliu.sell.enums.OrderStatusEnum;
import cn.lovingliu.sell.enums.PayStatusEnum;
import cn.lovingliu.sell.vo.OrderVO;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-09-26
 */
public class OrderDTOToOrerVO {
    public static OrderVO convert(OrderDTO orderDTO){
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(orderDTO, orderVO);
        orderVO.setPayStatusMessage(OrderStatusEnum.getOrderStatusEnum(orderDTO.getOrderStatus()).getDesc());
        orderVO.setPayStatusMessage(PayStatusEnum.getPayStatusEnum(orderDTO.getPayStatus()).getDesc());
        return orderVO;
    }

    public static List<OrderVO> convert(List<OrderDTO> list){
        List<OrderVO> orderVOList = Lists.newArrayList();
        for(OrderDTO orderDTO : list){
            OrderVO orderVO = convert(orderDTO);
            orderVOList.add(orderVO);
        }
        return orderVOList;
    }
}
