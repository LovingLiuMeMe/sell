package cn.lovingliu.sell.exception;

import cn.lovingliu.sell.enums.ResultStatusEnum;
import lombok.Data;

/**
 * @Author：LovingLiu
 * @Description: 登陆权限的问题
 * @Date：Created in 2019-10-08
 */
@Data
public class SellAuthorizeException extends RuntimeException {
    private Integer code;

    public SellAuthorizeException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }
    public SellAuthorizeException(ResultStatusEnum resultStatusEnum){
        super(resultStatusEnum.getMsg());
        this.code = resultStatusEnum.getCode();
    }
}
