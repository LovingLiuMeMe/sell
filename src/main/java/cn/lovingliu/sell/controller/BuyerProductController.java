package cn.lovingliu.sell.controller;

import cn.lovingliu.sell.common.ServerResponse;
import cn.lovingliu.sell.convert.ProductInfoToProductInfoVO;
import cn.lovingliu.sell.dataobject.ProductCategory;
import cn.lovingliu.sell.dataobject.ProductInfo;
import cn.lovingliu.sell.enums.ProductStatusEnum;
import cn.lovingliu.sell.service.CategoryService;
import cn.lovingliu.sell.service.ProductService;
import cn.lovingliu.sell.vo.ProductCategoryVO;
import cn.lovingliu.sell.vo.ProductInfoVO;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author：LovingLiu
 * @Description: 处理买家商品相关的请求
 * @Date：Created in 2019-09-19
 */
@RestController
@RequestMapping("/buyer/product/")
public class BuyerProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;

    @Value("${sell.imageHost}")
    private String imageHost;
    /**
     * @Desc String sellerId 并无实际用处 单纯的测试缓存效果
     * @Author LovingLiu
    */
    @GetMapping("list")
    @Cacheable(value = "product",key = "#sellerId",condition = "#sellerId != null && #sellerId.length() > 3",unless = "#result.code != 0")
    public ServerResponse<List<ProductCategoryVO>> list(@RequestParam(value = "pageNum",defaultValue = "1")Integer pageNum,
                                                  @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize,
                                                  @RequestParam(value = "sortBy",defaultValue = "productPrice")String sortBy,
                                                  @RequestParam(value = "sortLift",defaultValue = "desc")String sortLift,
                                                  @RequestParam(value = "sellerId",required = false) String sellerId){

        // 1.更具条件查询所有的商品
        Sort sort = new Sort(sortLift.equals("desc")? Sort.Direction.DESC : Sort.Direction.ASC,sortBy);

        PageRequest pageRequest = PageRequest.of(pageNum-1,pageSize,sort);
        Page<ProductInfo> page = productService.findUpAll(ProductStatusEnum.UP.getCode(),pageRequest);
        List<ProductInfo> productInfoList = page.getContent();
        // 2.查询出上述商品中的分类(一次查询:只进行一次查询 即可拿出所有信息)
        // java8 lambda表达式
        List<Integer> categorytypeList = productInfoList
                .stream()
                .map(e -> e.getCategoryType())
                .collect(Collectors.toList());

        List<ProductCategory> productCategoryList = categoryService.findByCategoryTypeInIdList(categorytypeList);
        // 3.数据封装
        List<ProductCategoryVO> productCategoryVOList = Lists.newArrayList();
        for(ProductCategory productCategory:productCategoryList){
            ProductCategoryVO productCategoryVO = new ProductCategoryVO();
            productCategoryVO.setCategoryName(productCategory.getCategoryName());
            productCategoryVO.setCategoryType(productCategory.getCategoryType());

            List<ProductInfoVO> productInfoVOList = ProductInfoToProductInfoVO.convert(productInfoList);

            productCategoryVO.setFoods(productInfoVOList);
            productCategoryVOList.add(productCategoryVO);
        }
        return ServerResponse.createBySuccess("成功",productCategoryVOList);
    }

}
