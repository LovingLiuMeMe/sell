package cn.lovingliu.sell.exception;

import cn.lovingliu.sell.enums.ResultStatusEnum;
import lombok.Data;

/**
 * @Author：LovingLiu
 * @Description: 模拟实现请求银行接口错误时 接口返回错误码设置
 * @Date：Created in 2019-10-09
 */
@Data
public class ResponseBankException extends RuntimeException {
    private Integer code;

    public ResponseBankException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }
    public ResponseBankException(ResultStatusEnum resultStatusEnum){
        super(resultStatusEnum.getMsg());
        this.code = resultStatusEnum.getCode();
    }
}
