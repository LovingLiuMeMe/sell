package cn.lovingliu.sell.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-09-19
 */
@Data
public class ProductInfoVO {
    @JsonProperty("id")
    private String productId;
    @JsonProperty("name")
    private String productName;
    @JsonProperty("description")
    private String productDescription;
    @JsonProperty("icon")
    private String productIcon;

    private BigDecimal productPrice;
    private Integer productStock;
    /** 商品状态: 0 正常 1下架*/
    private String productStatusMessage;

    private Integer categoryType;

    private String createTime;
    private String updateTime;
}