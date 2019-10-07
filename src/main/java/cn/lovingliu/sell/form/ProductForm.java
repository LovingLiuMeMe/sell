package cn.lovingliu.sell.form;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author：LovingLiu
 * @Description: seller端 保存/修改
 * @Date：Created in 2019-09-30
 */
@Data
public class ProductForm {
    private String productId;
    private String productName;
    private String productDescription;
    private String productIcon;
    /**
     * String -> BigDecimal controller 报错
     * 怀疑是因为 精度丢失之类的情况发生,所有使用String
    */
    private BigDecimal productPrice;

    private Integer productStock;
    private Integer categoryType;
}
