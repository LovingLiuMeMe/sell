package cn.lovingliu.sell.enums;

import lombok.Getter;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-09-19
 */
@Getter
public enum ResultStatusEnum {
  PRODUCT_NOT_EXIT(10,"商品不存在");
  private Integer code;
  private String msg;

    ResultStatusEnum(Integer code,String msg){
        this.code = code;
        this.msg = msg;
    }

}
