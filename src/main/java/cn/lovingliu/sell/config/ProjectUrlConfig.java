package cn.lovingliu.sell.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author：LovingLiu
 * @Description: 内网穿透的网址
 * @Date：Created in 2019-10-08
 */
@Data
@ConfigurationProperties(prefix = "project-url")
@Component
public class ProjectUrlConfig {
    /**
     * @Desc 微信公众平台授权的URl
     * @Author LovingLiu
    */
    public String wechatMpAuthorize;
    /**
     * @Desc 微信开发平台授权的URl
     * @Author LovingLiu
     */
    public String wechatOpenAuthorize;

    /**
     * @Desc 项目的URL
     * @Author LovingLiu
    */
    public String sell;
}
