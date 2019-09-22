package cn.lovingliu.sell.dto;

import lombok.Data;

/**
 * @Author：LovingLiu
 * @Description: 购物车DTO 加减库存
 * @Date：Created in 2019-09-20
 */
@Data
public class CartDTO {
    private String productId;
    private Integer productQuantity;

    //lambda表达式使用
    public CartDTO(String productId, Integer productQuantity) {
        this.productId = productId;
        this.productQuantity = productQuantity;
    }
}
