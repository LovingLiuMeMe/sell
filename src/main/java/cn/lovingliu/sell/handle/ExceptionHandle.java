package cn.lovingliu.sell.handle;

import cn.lovingliu.sell.common.ServerResponse;
import cn.lovingliu.sell.config.ProjectUrlConfig;
import cn.lovingliu.sell.exception.SellAuthorizeException;
import cn.lovingliu.sell.exception.SellException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Author：LovingLiu
 * @Description:统一异常处理
 * @Date：Created in 2019-09-19
 */
@ControllerAdvice
@Slf4j
public class ExceptionHandle {
    @Autowired
    private ProjectUrlConfig projectUrlConfig;
    /**
     * @Desc 拦截其他非登陆异常情况 如:商品查询抛出的异常
     * @Author LovingLiu
    */
    @ExceptionHandler(value = SellException.class)
    @ResponseBody
    public ServerResponse resolveException(Exception e){
        if(e instanceof SellException){
            SellException sellException = (SellException) e;
            return ServerResponse.createByErrorCodeMessage(sellException.getCode(),sellException.getMessage());
        }
        return ServerResponse.createByErrorMessage(e.getMessage());
    }

    /**
     * @Desc 拦截登陆异常情况
     * @Author LovingLiu
    */
    @ExceptionHandler(value = SellAuthorizeException.class)
    public ModelAndView resolveAuthorizeException(Exception e){
        log.error("【统一异常捕获】登陆信息报错-{}",e.getMessage());
        return new ModelAndView("redirect:"
                .concat(projectUrlConfig.getWechatOpenAuthorize())
                .concat("/sell/wechat/qrAuthorize")
                .concat("?returnUrl=")
                .concat(projectUrlConfig.getSell())
                .concat("/sell/seller/login"));
    }

}
