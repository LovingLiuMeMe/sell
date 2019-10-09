package cn.lovingliu.sell.controller;

import cn.lovingliu.sell.convert.ProductInfoToProductInfoVO;
import cn.lovingliu.sell.dataobject.ProductCategory;
import cn.lovingliu.sell.dataobject.ProductInfo;
import cn.lovingliu.sell.enums.ResultStatusEnum;
import cn.lovingliu.sell.exception.SellException;
import cn.lovingliu.sell.form.ProductForm;
import cn.lovingliu.sell.service.CategoryService;
import cn.lovingliu.sell.service.ProductService;
import cn.lovingliu.sell.util.KeyUtil;
import cn.lovingliu.sell.vo.ProductInfoVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @Author：LovingLiu
 * @Description: 卖家商品列表
 * @Date：Created in 2019-09-28
 */
@Controller
@RequestMapping("/seller/product/")
//@CacheConfig(cacheNames = "product")
public class  SellerProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;

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
    /**
     * @Desc 商品上架
     * @Author LovingLiu
    */
    @GetMapping("on_sale")
    public ModelAndView onSale(@RequestParam("productId") String productId,
                                @RequestParam Map<String,Object> map){
        try{
            productService.onSale(productId);
        }catch (SellException e){
            map.put("msg",e.getMessage());
            map.put("url","/sell/seller/product/list");
            return new ModelAndView("common/error",map);
        }
        map.put("msg", ResultStatusEnum.SUCCESS.getMsg());
        map.put("url","/sell/seller/product/list");
        return new ModelAndView("common/success",map);
    }
    /**
     * @Desc 商品下架
     * @Author LovingLiu
    */
    @GetMapping("off_sale")
    public ModelAndView offSale(@RequestParam("productId") String productId,
                               @RequestParam Map<String,Object> map){
        try{
            productService.offSale(productId);
        }catch (SellException e){
            map.put("msg",e.getMessage());
            map.put("url","/sell/seller/product/list");
            return new ModelAndView("common/error",map);
        }
        map.put("msg", ResultStatusEnum.SUCCESS.getMsg());
        map.put("url","/sell/seller/product/list");
        return new ModelAndView("common/success",map);
    }
    /**
     * @Desc 商品修改/商品创建 页面
     * @Author LovingLiu
    */
    @GetMapping("index")
    public ModelAndView index(@RequestParam(value = "productId",required = false) String productId,
                              @RequestParam Map<String,Object> map){
        if(StringUtils.isNotBlank(productId)){
            ProductInfo productInfo = productService.findOne(productId);
            if(productInfo != null){
                map.put("productInfoVO", ProductInfoToProductInfoVO.convert(productInfo));
            }
        }
        List<ProductCategory> productCategoryList = categoryService.findAll();

        map.put("categoryList",productCategoryList);

        return new ModelAndView("product/index",map);
    }
    /**
     * @Desc 商品保存/修改 CacheEvict发生修改时 直接删除@CachePut
     * @Author LovingLiu
    */
    @PostMapping("save")
    @CacheEvict(value = "product",key = "123")
    public ModelAndView save(@Valid ProductForm productForm,
                             BindingResult bindingResult,
                             Map<String,Object> map){
        if(bindingResult.hasErrors()){
            map.put("msg",bindingResult.getFieldError().getDefaultMessage());
            map.put("url","/sell/seller/product/index");
            return new ModelAndView("common/error",map);
        }
        /** 这种修改方式非常的严格 必须保证前端将所有的属性传递过来,无论是修改过还是未修改过的。 */
        /*ProductInfo productInfo = new ProductInfo();
        BeanUtils.copyProperties(productForm,productInfo);*/

        try{
            ProductInfo productInfo = new ProductInfo();

            if(!StringUtils.isBlank(productForm.getProductId())){
                /** 修改 */
                productInfo = productService.findOne(productForm.getProductId());
            }else{
                /** 新增商品 设置商品的唯一ID*/
                productForm.setProductId(KeyUtil.getUniqueKey());
            }
            BeanUtils.copyProperties(productForm,productInfo);
            productService.save(productInfo);
        }catch (SellException e){
            map.put("msg",e.getMessage());
            map.put("url","/sell/seller/product/index");
            return new ModelAndView("common/error",map);
        }
        map.put("msg",ResultStatusEnum.SUCCESS.getMsg());
        map.put("url","/sell/seller/product/index");
        return new ModelAndView("common/success",map);
    }

}
