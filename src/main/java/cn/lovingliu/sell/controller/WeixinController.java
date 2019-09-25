package cn.lovingliu.sell.controller;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.util.crypto.SHA1;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/**
 * @Author：LovingLiu
 * @Description: 临时使用 微信验证服务器所需要的
 * @Date：Created in 2019-09-22
 */
@RestController
@RequestMapping("/weixin")
@Slf4j
public class WeixinController {

    private String appid = "wxd0491840b258a176";
    private String secret = "8920134334693cdb35e3281d598088f6";
    private String token = "lovingliu";

    @GetMapping("/auth")
    // 用户同意授权后跳转 并携带参数code
    public String auth(String signature,String nonce,String timestamp,String echostr){
        System.out.println(appid+"-"+secret+"-"+token);

        String [] str = {token, timestamp, nonce};
        Arrays.sort(str);
        String bigStr = str[0] + str[1] + str[2];
        // SHA1加密
        String digest = SHA1.gen(bigStr).toLowerCase();

        if(digest.equals(signature)){
            return echostr;
        }
        return echostr;
    }
}
