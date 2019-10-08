package cn.lovingliu.sell.controller;

import cn.lovingliu.sell.config.ProjectUrlConfig;
import cn.lovingliu.sell.constant.CookieConstant;
import cn.lovingliu.sell.constant.RedisConstant;
import cn.lovingliu.sell.dataobject.SellerInfo;
import cn.lovingliu.sell.enums.ResultStatusEnum;
import cn.lovingliu.sell.service.SellerService;
import cn.lovingliu.sell.util.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Author：LovingLiu
 * @Description: 卖家用户相关操作
 * @Date：Created in 2019-10-08
 */
@Controller
@RequestMapping("/seller/")
public class SellerUserController {
    @Autowired
    private SellerService sellerService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ProjectUrlConfig projectUrlConfig;

    @GetMapping("login")
    public ModelAndView login(@RequestParam("openId") String openId,
                              HttpServletResponse response,
                              @RequestParam Map<String,Object> map){
        //1.数据匹配
        SellerInfo loginInfo = sellerService.findSellerInfoByOpenid(openId);
        if (loginInfo == null){
            map.put("msg", ResultStatusEnum.LOGIN_FAIL.getMsg());
            map.put("url","/sell/seller/order/list");
            return new ModelAndView("common/error",map);
        }
        //2.设置token至redis
        String token = UUID.randomUUID().toString();
        Integer expire = RedisConstant.EXPIRE;

        stringRedisTemplate.opsForValue().set(String.format(RedisConstant.TOKEN_PREFIX,token),openId,expire,TimeUnit.SECONDS);
        //3.设置token至cookie
        CookieUtil.set(response, CookieConstant.TOKEN,token,CookieConstant.EXPIRE);

        return new ModelAndView("redirect:"+projectUrlConfig.getSell()+"/sell/seller/order/list");
    }
    @GetMapping("logout")
    public ModelAndView logout(HttpServletRequest httpServletRequest,
                       HttpServletResponse httpServletResponse,
                       Map<String,Object> map){
        // 1.从cookie查询
        Cookie cookie = CookieUtil.get(httpServletRequest,CookieConstant.TOKEN);
        if (cookie == null){
            map.put("msg", ResultStatusEnum.LOGOUT_FAIL.getMsg());
            map.put("url","/sell/seller/order/list");
            return new ModelAndView("common/error",map);
        }
        // 2.清除redis
        stringRedisTemplate.opsForValue().getOperations().delete(String.format(RedisConstant.TOKEN_PREFIX,cookie.getValue()));

        // 3.清除cookie
        CookieUtil.set(httpServletResponse,CookieConstant.TOKEN,null,0);

        map.put("msg", ResultStatusEnum.LOGOUT_SUCCESS.getMsg());
        map.put("url","/sell/seller/order/list");
        return new ModelAndView("common/success",map);
    }
}
