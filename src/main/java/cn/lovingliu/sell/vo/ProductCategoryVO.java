package cn.lovingliu.sell.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author：LovingLiu
 * @Description: 商品列表ViewObject
 * @Date：Created in 2019-09-19
 */
@Data
public class ProductCategoryVO implements Serializable {

    private static final long serialVersionUID = 8121788843481931209L;

    @JsonProperty("name")
    private String  categoryName;//类目名称
    @JsonProperty("type")
    private Integer categoryType;//类目编号
    private List<ProductInfoVO> foods;

}
