package cn.lovingliu.sell.aspect;

import cn.lovingliu.sell.constant.CookieConstant;
import cn.lovingliu.sell.constant.RedisConstant;
import cn.lovingliu.sell.enums.ResultStatusEnum;
import cn.lovingliu.sell.exception.SellAuthorizeException;
import cn.lovingliu.sell.util.CookieUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author：LovingLiu
 * @Description: AOP 登陆拦截
 * @Date：Created in 2019-10-08
 */
@Aspect
@Component
@Slf4j
public class SellerAuthorize {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Pointcut("execution(public * cn.lovingliu.sell.controller.Seller*.*(..))" +
    "&& !execution(public * cn.lovingliu.sell.controller.SellerUserController.*(..))")
    public void verify(){
    }
    @Before("verify()")
    public void doVerify(){
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        // 1.获取cookie
        Cookie cookie = CookieUtil.get(request, CookieConstant.TOKEN);
        if(cookie == null){
            log.error("【登陆校验】Cookie中查不到token");
            throw new SellAuthorizeException(ResultStatusEnum.AUTHORIZE_FAIL);
        }
        // 2.redis查询
        String tokenValue = stringRedisTemplate.opsForValue().get(String.format(RedisConstant.TOKEN_PREFIX,cookie.getValue()));
        if(StringUtils.isBlank(tokenValue)){
            log.error("【登陆校验】Redis中查不到token");
            throw new SellAuthorizeException(ResultStatusEnum.AUTHORIZE_FAIL);
        }
    }
}
