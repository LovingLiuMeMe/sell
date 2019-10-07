package cn.lovingliu.sell.controller;

import cn.lovingliu.sell.dataobject.ProductCategory;
import cn.lovingliu.sell.form.CategoryForm;
import cn.lovingliu.sell.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @Description: 卖家商品目录的Controller
 * @Date：Created in 2019-10-07
 */
@Controller
@RequestMapping("/seller/category/")
public class SellerCategoryController {
    @Autowired
    private CategoryService categoryService;
    @GetMapping("list")
    public ModelAndView list(@RequestParam Map<String,Object> map){
        List<ProductCategory> categoryList = categoryService.findAll();
        map.put("categoryList",categoryList);
        return new ModelAndView("category/list",map);
    }
    @GetMapping("index")
    public ModelAndView index(@RequestParam(value = "categoryId",required = false) Integer categoryId,
                              @RequestParam Map<String,Object> map){
        if(categoryId != null){
            ProductCategory productCategory = categoryService.findOne(categoryId);
            map.put("category",productCategory);
        }
        return new ModelAndView("category/index",map);
    }
    @PostMapping("save")
    public ModelAndView save(@Valid CategoryForm categoryForm,
                             BindingResult bindingResult,
                             Map<String,Object> map){
        if(bindingResult.hasErrors()){
            map.put("msg",bindingResult.getFieldError().getDefaultMessage());
            map.put("url","/sell/seller/product/index");
            return new ModelAndView("common/error",map);
        }

        ProductCategory productCategory = new ProductCategory();
        if(categoryForm.getCategoryId() != null){
           productCategory = categoryService.findOne(categoryForm.getCategoryId());
        }

        BeanUtils.copyProperties(categoryForm,productCategory);
        categoryService.save(productCategory);

        return new ModelAndView("category/index",map);
    }
}
