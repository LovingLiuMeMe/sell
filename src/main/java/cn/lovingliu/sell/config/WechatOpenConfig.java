package cn.lovingliu.sell.config;

import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @Author：LovingLiu
 * @Description: 微信开发平台配置(实现登陆授权)
 * @Date：Created in 2019-10-08
 */
@Component
@EnableConfigurationProperties(WechatAccountConfig.class)
public class WechatOpenConfig {
    @Autowired
    private WechatAccountConfig wechatAccountConfig;

    @Bean
    public WxMpService wxOpenService() {
        WxMpService wxOpenService = new WxMpServiceImpl();
        wxOpenService.setWxMpConfigStorage(wxOpenDefaultConfig());
        return wxOpenService;
    }

    @Bean
    public WxMpDefaultConfigImpl wxOpenDefaultConfig(){
        WxMpDefaultConfigImpl configStorage = new WxMpDefaultConfigImpl();

        configStorage.setAppId(wechatAccountConfig.getOpenAppid());
        configStorage.setAesKey(wechatAccountConfig.getOpenSecret());
        return configStorage;
    }
}
