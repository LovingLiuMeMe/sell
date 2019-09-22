package cn.lovingliu.sell.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-09-22
 */
@RestController
@RequestMapping("/wechat/")
@Slf4j
public class WechatController {
    @Autowired
    private WxMpService wxService;

    @GetMapping("authorize")
    public void authorize(){
        this.wxService.oauth2buildAuthorizationUrl(WxConsts.OAuth2Scope.SNSAPI_USERINFO);
    }

    @GetMapping("userinfo")
    public void userinfo(){

    }
}
