package cn.lovingliu.sell.dataobject;

import cn.lovingliu.sell.enums.OrderStatusEnum;
import cn.lovingliu.sell.enums.PayStatusEnum;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author：LovingLiu
 * @Description: 订单主表
 * @Date：Created in 2019-09-19
 */
@Entity
@DynamicUpdate
@Data
public class OrderMaster {
    @Id
    private String orderId;
    private String buyerName;
    private String buyerPhone;
    private String buyerAddress;
    private String buyerOpenid;
    private BigDecimal orderAmount;
    // 订单状态 默认为新下单
    private Integer orderStatus = OrderStatusEnum.NEW.getCode();
    // 支付状态 默认为未支付
    private Integer payStatus = PayStatusEnum.WAIT.getCode();

    private Date createTime;

    private Date updateTime;
}
