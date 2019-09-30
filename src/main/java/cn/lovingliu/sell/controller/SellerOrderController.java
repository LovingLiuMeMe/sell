package cn.lovingliu.sell.controller;

import cn.lovingliu.sell.convert.OrderDTOToOrerVO;
import cn.lovingliu.sell.dto.OrderDTO;
import cn.lovingliu.sell.enums.ResultStatusEnum;
import cn.lovingliu.sell.exception.SellException;
import cn.lovingliu.sell.service.OrderService;
import cn.lovingliu.sell.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * @Author：LovingLiu
 * @Description: 卖家端的controller
 * @Date：Created in 2019-09-26
 */
@Controller
@RequestMapping("/seller/order/")
@Slf4j
public class SellerOrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("list")
    public ModelAndView list(@RequestParam(value = "pageNum",defaultValue = "1")Integer pageNum,
                             @RequestParam(value = "pageSize",defaultValue = "2 ") Integer pageSize,
                             @RequestParam(value = "sortBy",defaultValue = "updateTime")String sortBy,
                             @RequestParam(value = "sortLift",defaultValue = "desc")String sortLift,
                             @RequestParam Map<String,Object> map){
        Sort sort = new Sort(sortLift.equals("desc")? Sort.Direction.DESC : Sort.Direction.ASC,sortBy);
        PageRequest pageRequest = PageRequest.of(pageNum - 1, pageSize, sort);

        Page<OrderVO> orderVOPage = orderService.findList(pageRequest);
        map.put("orderVOPage",orderVOPage);
        map.put("currentPage",pageNum);
        map.put("size",pageSize);
        return new ModelAndView("order/list", map);
    }
    /**
     * @Desc 取消订单
     * @Author LovingLiu
    */
    @GetMapping("cancel")
    public ModelAndView cancel(@RequestParam("orderId") String orderId,
                               @RequestParam Map<String,Object> map){
        try {
            OrderDTO orderDTO = orderService.findOne(orderId);
            orderService.cancel(orderDTO);
        }catch (SellException e){
            map.put("msg", e.getMessage());// 返回的具体的消息
            map.put("url", "/sell/seller/order/list");// 跳转的url
            return new ModelAndView("common/error",map);
        }
        map.put("msg", ResultStatusEnum.SUCCESS.getMsg());// 返回的具体的消息
        map.put("url", "/sell/seller/order/list");// 跳转的url
        return new ModelAndView("common/success",map);
    }
    /**
     * @Desc 查看商品详情
     * @Author LovingLiu
    */
    @GetMapping("detail")
    public ModelAndView detail(@RequestParam("orderId") String orderId,@RequestParam Map<String,Object> map){
        OrderVO orderVO;
        try{
            OrderDTO orderDTO = orderService.findOne(orderId);
            orderVO = OrderDTOToOrerVO.convert(orderDTO);
        }catch (Exception e){
            log.error("订单查询失败");
            map.put("msg",e.getMessage());
            map.put("url", "/sell/seller/order/list");// 跳转的url
            return new ModelAndView("common/error",map);

        }
        map.put("orderVO",orderVO);
        return new ModelAndView("order/detail",map);
    }
    @GetMapping("finish")
    public ModelAndView finish(@RequestParam("orderId") String orderId,
                               @RequestParam Map<String,Object> map){
        try{
            OrderDTO orderDTO = orderService.findOne(orderId);
            orderService.finish(orderDTO);
        }catch (Exception e){
            log.error("订单取消失败");
            map.put("msg",e.getMessage());
            map.put("url", "/sell/seller/order/list");// 跳转的url
            return new ModelAndView("common/error",map);
        }
        map.put("msg", ResultStatusEnum.SUCCESS.getMsg());// 返回的具体的消息
        map.put("url", "/sell/seller/order/list");// 跳转的url
        return new ModelAndView("common/success",map);
    }

}
