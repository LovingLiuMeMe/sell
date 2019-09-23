package cn.lovingliu.sell.service.impl;

import cn.lovingliu.sell.dto.OrderDTO;
import cn.lovingliu.sell.enums.ResultStatusEnum;
import cn.lovingliu.sell.exception.SellException;
import cn.lovingliu.sell.service.OrderService;
import cn.lovingliu.sell.service.PayService;
import cn.lovingliu.sell.util.JsonUtils;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.model.RefundRequest;
import com.lly835.bestpay.model.RefundResponse;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-09-23
 */
@Service
@Slf4j
public class PayServiceImpl implements PayService{

    private static final String ORDER_NAME="微信点餐订单";

    @Autowired
    private BestPayServiceImpl bestPayService;

    @Autowired
    private OrderService orderService;

    @Override
    public PayResponse create(OrderDTO orderDTO) {

        PayRequest payRequest=new PayRequest();

        payRequest.setOpenid(orderDTO.getBuyerOpenid());
        payRequest.setOrderAmount(orderDTO.getOrderAmount().doubleValue());
        payRequest.setOrderId(orderDTO.getOrderId());
        payRequest.setOrderName(ORDER_NAME);
        payRequest.setPayTypeEnum(BestPayTypeEnum.WXPAY_H5);
        log.info("【微信支付】发起支付，request={}", JsonUtils.toJson(payRequest));

        PayResponse payResponse=bestPayService.pay(payRequest);
        log.info("【微信支付】发起支付，response={}",JsonUtils.toJson(payResponse));
        return payResponse;
    }


    @Override
    public PayResponse notify(String notifyData) {
        //1.验证签名
        //2.支付状态
        //3. 支付金额
        //4. 支付人（下单人==支付人）
        PayResponse payResponse=bestPayService.asyncNotify(notifyData);//可以完成1、2两步
        log.info("【微信支付 异步通知】，payResponse={}",JsonUtils.toJson(payResponse));

        //查询订单
        OrderDTO orderDTO=orderService.findOne(payResponse.getOrderId());

        //判断订单是否存在
        if(orderDTO==null){
            log.error("【微信支付】 异步通知，订单不存在，orderId={}",payResponse.getOrderId());
            throw new SellException(ResultStatusEnum.ORDER_NOT_EXIT);
        }
        //判断金额是否一致(0.10   0.1)
//        if(!MathUtil.equals(payResponse.getOrderAmount(),orderDTO.getOrderAmount().doubleValue())){
//            log.error("【微信支付】 异步通知，订单金额不一致，orderId={},微信通知金额={}，系统金额={}",
//                    payResponse.getOrderId(),
//                    payResponse.getOrderAmount(),
//                    orderDTO.getOrderAmount());
//            throw new SellException(ResultEnum.WXPAY_NOTIFY_MONEY_VERIFY);
//        }
        //修改订单的支付状态
        orderService.paid(orderDTO);

        return payResponse;
    }

    /**
     * 退款
     * @param orderDTO
     */
    @Override
    public RefundResponse refund(OrderDTO orderDTO) {
        RefundRequest refundRequest=new RefundRequest();
        refundRequest.setOrderId(orderDTO.getOrderId());
        refundRequest.setOrderAmount(orderDTO.getOrderAmount().doubleValue());
        refundRequest.setPayTypeEnum(BestPayTypeEnum.WXPAY_H5);
        log.info("【微信退款】 request={}", JsonUtils.toJson(refundRequest));

        RefundResponse refundResponse=bestPayService.refund(refundRequest);
        log.info("【微信退款】 response={}",JsonUtils.toJson(refundResponse));

        return refundResponse;
    }
}
