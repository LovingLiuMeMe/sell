package cn.lovingliu.sell.config;

import lombok.Data;
import org.springframework.context.annotation.Configuration;

/**
 * @Author：LovingLiu
 * @Description: 微信配置类
 * @Date：Created in 2019-09-23
 */
@Configuration("mx.mp")
@Data
public class WxMpConfig {
    private String appId;
    private String secret;
    private String aesKey;
}
