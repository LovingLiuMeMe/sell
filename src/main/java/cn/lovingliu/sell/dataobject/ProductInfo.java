package cn.lovingliu.sell.dataobject;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-09-19
 */
@Entity
@DynamicUpdate
@Data
public class ProductInfo {
    @Id
    private String productId;
    private String productName;
    private BigDecimal productPrice;
    private Integer productStock;
    private String productDescription;
    private String productIcon;
    /** 商品状态: 0 正常 1下架*/
    private Integer productStatus;
    private Integer categoryType;

    /** 排序字段需要使用*/
    private Date createTime;

    private Date updateTime;

}
