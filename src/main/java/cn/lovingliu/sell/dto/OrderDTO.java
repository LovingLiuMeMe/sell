package cn.lovingliu.sell.dto;

import cn.lovingliu.sell.dataobject.OrderDetail;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Author：LovingLiu
 * @Description: 数据传输对象 在各个层之间传输
 * @Date：Created in 2019-09-19
 */
@Data
public class OrderDTO {
    private String orderId;
    private String buyerName;
    private String buyerPhone;
    private String buyerAddress;
    private String buyerOpenid;
    private BigDecimal orderAmount;
    // 订单状态 默认为新下单
    private Integer orderStatus;
    // 支付状态 默认为未支付
    private Integer payStatus;

    private Date createTime;

    private Date updateTime;

    private List<OrderDetail> orderDetailList;


}
