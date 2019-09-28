package cn.lovingliu.sell.vo;

import cn.lovingliu.sell.dataobject.OrderDetail;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-09-26
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderVO {
    private String orderId;
    private String buyerName;
    private String buyerPhone;
    private String buyerAddress;
    private String buyerOpenid;
    private BigDecimal orderAmount;
    // 订单状态 默认为新下单
    private String orderStatusMessage;
    private String payStatusMessage;
    private String createTime;

    private List<OrderDetail> orderDetailList;
}
