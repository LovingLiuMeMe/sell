package cn.lovingliu.sell.controller;

import cn.lovingliu.sell.service.OrderService;
import cn.lovingliu.sell.vo.OrderVO;
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
public class SellerOrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("list")
    public ModelAndView list(@RequestParam(value = "openid",required = true)String openId,
                             @RequestParam(value = "pageNum",defaultValue = "1")Integer pageNum,
                             @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize,
                             @RequestParam(value = "sortBy",defaultValue = "updateTime")String sortBy,
                             @RequestParam(value = "sortLift",defaultValue = "desc")String sortLift,
                             @RequestParam Map<String,Object> map){
        Sort sort = new Sort(sortLift.equals("desc")? Sort.Direction.DESC : Sort.Direction.ASC,sortBy);
        PageRequest pageRequest = PageRequest.of(pageNum - 1, pageSize, sort);
        Page<OrderVO> orderVOPage = orderService.findList(pageRequest);
        map.put("orderVOPage", orderVOPage);

        return new ModelAndView("order/list", map);
    }
}
