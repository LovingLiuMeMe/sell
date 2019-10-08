package cn.lovingliu.sell.service.impl;

import cn.lovingliu.sell.convert.OrderDTOToOrerVO;
import cn.lovingliu.sell.dto.OrderDTO;
import cn.lovingliu.sell.service.PushService;
import cn.lovingliu.sell.vo.OrderVO;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-10-08
 */
@Service
@Slf4j
public class PushServiceImpl implements PushService {
    @Autowired
    private WxMpService wxMpService;

    @Override
    public void orderStatus(OrderDTO orderDTO) {
        try {
            OrderVO orderVO = OrderDTOToOrerVO.convert(orderDTO);

            WxMpTemplateMessage templateMessage = new WxMpTemplateMessage();
            templateMessage.setTemplateId("");// 设置模版ID
            templateMessage.setToUser(orderDTO.getBuyerOpenid());// 推送给谁:openId 注意:此处的openId相对于 关注该公众账号的openId
            // 设置通知的详情
            List<WxMpTemplateData> data = Lists.newArrayList(new WxMpTemplateData(),
                    new WxMpTemplateData("first","亲，记得收货"),
                    new WxMpTemplateData("keyword1","微信点餐"),
                    new WxMpTemplateData("keyword2","15712363915"),
                    new WxMpTemplateData("keyword3",orderVO.getOrderId()),
                    new WxMpTemplateData("keyword4", orderVO.getOrderStatusMessage()),
                    new WxMpTemplateData("keyword5","￥"+orderVO.getOrderAmount()),
                    new WxMpTemplateData("remark","欢迎再次光临"));
            templateMessage.setData(data);
            wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
        }catch (Exception e){
            log.error("【微信模版消息发送异常】,{}",e.getMessage());
        }

    }
}
