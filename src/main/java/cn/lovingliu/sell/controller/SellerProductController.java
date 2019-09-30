package cn.lovingliu.sell.controller;

import cn.lovingliu.sell.convert.ProductInfoToProductInfoVO;
import cn.lovingliu.sell.dataobject.ProductInfo;
import cn.lovingliu.sell.service.ProductService;
import cn.lovingliu.sell.vo.ProductInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

/**
 * @Author：LovingLiu
 * @Description: 卖家商品列表
 * @Date：Created in 2019-09-28
 */
@Controller
@RequestMapping("/seller/product/")
public class  SellerProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("list")
    public ModelAndView list(@RequestParam(value = "pageNum",defaultValue = "1")Integer pageNum,
                             @RequestParam(value = "pageSize",defaultValue = "2 ") Integer pageSize,
                             @RequestParam(value = "sortBy",defaultValue = "createTime")String sortBy,
                             @RequestParam(value = "sortLift",defaultValue = "desc")String sortLift,
                             @RequestParam Map<String,Object> map){

        Sort sort = new Sort(sortLift.equals("desc")? Sort.Direction.DESC : Sort.Direction.ASC,sortBy);
        PageRequest pageRequest = PageRequest.of(pageNum - 1, pageSize, sort);

        Page<ProductInfo> productInfoPage = productService.findAll(pageRequest);
        List<ProductInfoVO> productInfoVOList = ProductInfoToProductInfoVO.convert(productInfoPage.getContent());

        Page<ProductInfoVO> productInfoVOPage = new PageImpl<>(productInfoVOList,pageRequest,productInfoPage.getTotalElements());
        map.put("productInfoVOPage",productInfoVOPage);
        map.put("currentPage",pageNum);
        map.put("size",pageSize);
        return new ModelAndView("product/list", map);

    }
}
