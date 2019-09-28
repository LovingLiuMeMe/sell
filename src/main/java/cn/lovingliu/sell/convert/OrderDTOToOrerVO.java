package cn.lovingliu.sell.convert;

import cn.lovingliu.sell.dto.OrderDTO;
import cn.lovingliu.sell.enums.OrderStatusEnum;
import cn.lovingliu.sell.enums.PayStatusEnum;
import cn.lovingliu.sell.util.CodeEnumUtil;
import cn.lovingliu.sell.util.DateTimeUtil;
import cn.lovingliu.sell.vo.OrderVO;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * @Author：LovingLiu
 * @Description: 利用了范型 达到了优化
 * @Date：Created in 2019-09-26
 */
public class OrderDTOToOrerVO {
    public static OrderVO convert(OrderDTO orderDTO){
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(orderDTO, orderVO);
        orderVO.setOrderStatusMessage(CodeEnumUtil.getByCode(orderDTO.getOrderStatus(),OrderStatusEnum.class).getDesc());
        orderVO.setPayStatusMessage(CodeEnumUtil.getByCode(orderDTO.getPayStatus(),PayStatusEnum.class).getDesc());
        orderVO.setCreateTime(DateTimeUtil.dateToStr(orderDTO.getCreateTime()));
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
