package cn.lovingliu.sell.config;

import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-09-22
 */
@Component
public class Configuration {
    @Autowired
    private WxMpConfig wxMpConfig;
    @Bean
    public WxMpService wxMpService() {
        // 代码里 getConfigs()处报错的同学，请注意仔细阅读项目说明，你的IDE需要引入lombok插件！！！！
        WxMpService service = new WxMpServiceImpl();
        service.setWxMpConfigStorage(wxMpConfigStorage());
        return service;

    }
    @Bean
    public WxMpConfigStorage wxMpConfigStorage(){
        WxMpDefaultConfigImpl wxMpConfigStorage = new WxMpDefaultConfigImpl();
        wxMpConfigStorage.setAesKey(wxMpConfig.getAesKey());
        wxMpConfigStorage.setSecret(wxMpConfig.getSecret());
        return wxMpConfigStorage;
    }
}
