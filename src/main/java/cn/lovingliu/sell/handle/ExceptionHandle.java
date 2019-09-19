package cn.lovingliu.sell.handle;

import cn.lovingliu.sell.common.ServerResponse;
import cn.lovingliu.sell.exception.SellException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-09-19
 */
@ControllerAdvice
public class ExceptionHandle {

    @ExceptionHandler(value = SellException.class)
    @ResponseBody
    public ServerResponse resolveException(Exception e){
        if(e instanceof SellException){
            SellException sellException = (SellException) e;
            return ServerResponse.createByErrorCodeMessage(sellException.getCode(),sellException.getMessage());
        }
        return ServerResponse.createByErrorMessage(e.getMessage());
    }
}
