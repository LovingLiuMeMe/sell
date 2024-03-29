package cn.lovingliu.sell.controller;

import cn.lovingliu.sell.config.ProjectUrlConfig;
import cn.lovingliu.sell.enums.ResultStatusEnum;
import cn.lovingliu.sell.exception.SellException;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLEncoder;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-09-22
 */
@Controller
@RequestMapping("/wechat/")
@Slf4j
public class WechatController {
    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private WxMpService wxOpenService;

    @Autowired
    private ProjectUrlConfig projectUrlConfig;

    /**
     * @Desc returnUrl 前端传入 获取到openid后跳转的页面 如:login->main
     * @Author LovingLiu
    */
    @GetMapping("authorize")
    public String authorize(String returnUrl){
        try{
            String willRedirectUrl = projectUrlConfig.getWechatMpAuthorize()+"sell/wechat/userinfo";
            String redirectUrl = this.wxMpService.oauth2buildAuthorizationUrl(willRedirectUrl, WxConsts.OAuth2Scope.SNSAPI_USERINFO, URLEncoder.encode(returnUrl,"UTF-8"));
            log.info("【微信网页授权获取code】{}",redirectUrl);
            // 重定向到授权页面 注意:个人公众号到以后 都是无法进行开发的
            return "redirect:"+redirectUrl;
        }catch (Exception e){
            throw new SellException(ResultStatusEnum.ENCODE_ERROR);
        }

    }

    @GetMapping("userinfo")
    public String userinfo(@RequestParam(value = "code")String code,
                           @RequestParam(value = "state")String returnUrl){
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = new WxMpOAuth2AccessToken();
        try{
            wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
        }catch (Exception e){
            log.info("【微信网页授权异常】{}",e);
            throw new SellException(ResultStatusEnum.WECHAT_MP_ERROR);
        }
        String openId= wxMpOAuth2AccessToken.getOpenId();
        return "redirect:" + returnUrl+"?openid="+openId;
    }
    /**
     * @Desc  网页授权获取二维码
     * @Author LovingLiu
    */
    @GetMapping("qrAuthorize")
    public String qrauthorize(@RequestParam("returnUrl") String returnUrl) {
        try{
            String willRedirectUrl = projectUrlConfig.getWechatOpenAuthorize()+"sell/wechat/qrUserInfo";
            String redirectUrl = wxOpenService.buildQrConnectUrl(willRedirectUrl, WxConsts.QrConnectScope.SNSAPI_LOGIN,URLEncoder.encode(returnUrl,"UTF-8"));

            return "redirect:"+redirectUrl;
        }catch (Exception e){
            log.error("【url转码错误】{}",e.getMessage());
            throw new SellException(ResultStatusEnum.ENCODE_ERROR);
        }

    }
    /**
     * @Desc  网页授权获取二维码
     * @Author LovingLiu
     */
    @GetMapping("qrUserInfo")
    public String qrUserInfo(@RequestParam(value = "code")String code,
                             @RequestParam(value = "state")String returnUrl) {
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = new WxMpOAuth2AccessToken();
        try{
            wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
        }catch (Exception e){
            log.info("【微信网页授权异常】{}",e);
            throw new SellException(ResultStatusEnum.WECHAT_MP_ERROR);
        }
        log.info("wxMpOAuth2AccessToken",wxMpOAuth2AccessToken);
        String openId= wxMpOAuth2AccessToken.getOpenId();
        return "redirect:" + returnUrl+"?openid="+openId;
    }
}
