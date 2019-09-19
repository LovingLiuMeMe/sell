package cn.lovingliu.sell.exception;

import cn.lovingliu.sell.enums.ResultStatusEnum;
import lombok.Data;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-09-19
 */
@Data
public class SellException extends RuntimeException {

    private Integer code;

    public SellException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }
    public SellException(ResultStatusEnum resultStatusEnum){
        super(resultStatusEnum.getMsg());
        this.code = resultStatusEnum.getCode();
    }
}
