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
    private String id;
    @JsonProperty("name")
    private String productName;
    private BigDecimal price;
    private String description;
    private String icon;
}
