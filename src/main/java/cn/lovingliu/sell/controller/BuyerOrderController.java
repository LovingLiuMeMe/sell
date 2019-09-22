package cn.lovingliu.sell.controller;

import cn.lovingliu.sell.common.ServerResponse;
import cn.lovingliu.sell.convert.OrderFormToOrderDTO;
import cn.lovingliu.sell.dto.OrderDTO;
import cn.lovingliu.sell.enums.ResultStatusEnum;
import cn.lovingliu.sell.exception.SellException;
import cn.lovingliu.sell.form.OrderForm;
import cn.lovingliu.sell.service.BuyerService;
import cn.lovingliu.sell.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author：LovingLiu
 * @Description: 买家端下单
 * @Date：Created in 2019-09-22
 */
@RestController
@RequestMapping("/buyer/order/")
@Slf4j
public class BuyerOrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private BuyerService buyerService;
    /**
     * @Desc 获得单个订单
     * @Author LovingLiu
    */
    @GetMapping("detail")
    public ServerResponse<OrderDTO> detail(String openid,String orderId){
        OrderDTO orderDTO = buyerService.findOrderOne(openid, orderId);
        return ServerResponse.createBySuccess(orderDTO);
    }
    /**
     * @Desc 获得我的所有订单列表
     * @Author LovingLiu
    */
    @GetMapping("list")
    public ServerResponse<Page<OrderDTO>> list(@RequestParam(value = "openid",required = true)String openId,
                                               @RequestParam(value = "pageNum",defaultValue = "1")Integer pageNum,
                                               @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize,
                                               @RequestParam(value = "sortBy",defaultValue = "updateTime")String sortBy,
                                               @RequestParam(value = "sortLift",defaultValue = "desc")String sortLift){
        Sort sort = new Sort(sortLift.equals("desc")? Sort.Direction.DESC : Sort.Direction.ASC,sortBy);

        PageRequest pageRequest = PageRequest.of(pageNum-1,pageSize,sort);
        Page<OrderDTO> orderDTOPage = orderService.findList(openId,pageRequest);

        return ServerResponse.createBySuccess(orderDTOPage);
    }
    /**
     * @Desc 创建订单
     * @Author LovingLiu
    */
    @PostMapping("create")
    public ServerResponse<Map<String,String>> create(@Valid OrderForm orderForm, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            log.error("参数不正确,参数不正确{}",orderForm);
            throw new SellException(ResultStatusEnum.PARAMS_ERROR.getCode(),bindingResult.getFieldError().getDefaultMessage());
        }
        OrderDTO orderDTO = OrderFormToOrderDTO.convert(orderForm);
        if(CollectionUtils.isEmpty(orderDTO.getOrderDetailList())){
            log.error("【创建订单购物车不能为空】");
            throw new SellException(ResultStatusEnum.CART_EMPTY);
        }
        OrderDTO resultDto = orderService.createrOrder(orderDTO);
        Map<String,String> resultMap = new HashMap<>();
        resultMap.put("orderId",resultDto.getOrderId());
        return ServerResponse.createBySuccess(resultMap);
    }

    /**
     * @Desc 取消订单
     * @Author LovingLiu
    */
    @PostMapping("cancel")
    public ServerResponse<OrderDTO> cancel(String openid,String orderId){
        buyerService.cancelOrder(openid, orderId);
        return ServerResponse.createByErrorMessage("成功");
    }
    /**
     * @Desc 支付订单
     * @Author LovingLiu
    */

    /**
     * @Desc 申请退款
     * @Author LovingLiu
    */








}
