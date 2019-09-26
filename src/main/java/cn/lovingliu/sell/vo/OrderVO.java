package cn.lovingliu.sell.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;

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
}
