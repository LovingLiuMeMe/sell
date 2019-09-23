package cn.lovingliu.sell.enums;

import lombok.Getter;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-09-19
 */
@Getter
public enum ResultStatusEnum {
    PARAMS_ERROR(1,"参数错误"),
    CART_EMPTY(2,"购物车为空"),
    WECHAT_PARAMS_ERROR(3,"微信参数配置错误"),
    WECHAT_MP_ERROR(4, "微信公众账号异常"),
  PRODUCT_NOT_EXIT(10,"商品不存在"),
  PRODUCT_STOCK_ERROR(11,"库存不足，无法下单"),
  ORDER_NOT_EXIT(12,"订单不存在"),
  ORDER_DETAIL_NOT_EXIT(13,"订单详情不存在"),
  ORDER_CANNOT_CANCEL(14,"订单不可取消"),
  ORDER_UPDATE_FAIL(15,"订单更新失败"),
    ORDER_STATUS_ERROR(16,"订单状态错误"),
    ORDER_PAY_STATUS_ERROR(17,"订单支付状态错误"),
    ORDER_OWNER_ERROR(18,"该订单不属于当前用户");
  private Integer code;
  private String msg;

    ResultStatusEnum(Integer code,String msg){
        this.code = code;
        this.msg = msg;
    }

}
