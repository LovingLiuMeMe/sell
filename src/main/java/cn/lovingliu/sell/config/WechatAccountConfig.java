package cn.lovingliu.sell.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * @Author：LovingLiu
 * @Description: 微信配置类
 * @Date：Created in 2019-09-23
 */
@Data
@ConfigurationProperties(prefix = "wx.mp")
public class WechatAccountConfig {
    /**
     * 设置微信公众号的appid
     */
    private String appId;

    /**
     * 设置微信公众号的app secret
     */
    private String secret;

    /**
     * 设置微信公众号的token
     */
    private String token;

    /**
     * 设置微信公众号的EncodingAESKey
     */
    private String aesKey;
}
