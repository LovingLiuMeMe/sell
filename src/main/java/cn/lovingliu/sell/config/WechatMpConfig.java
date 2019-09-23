package cn.lovingliu.sell.config;

import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-09-22
 */
@Configuration
@EnableConfigurationProperties(WechatAccountConfig.class)
public class WechatMpConfig {
    @Autowired
    private WechatAccountConfig wechatAccountConfig;

    @Bean
    public WxMpService wxMpService() {

        WxMpService service = new WxMpServiceImpl();
        WxMpDefaultConfigImpl configStorage = new WxMpDefaultConfigImpl();

        configStorage.setAppId(wechatAccountConfig.getAppId());
        configStorage.setSecret(wechatAccountConfig.getSecret());
        configStorage.setToken(wechatAccountConfig.getToken());
        configStorage.setAesKey(wechatAccountConfig.getAesKey());


        service.setWxMpConfigStorage(configStorage);
        return service;
    }
}
