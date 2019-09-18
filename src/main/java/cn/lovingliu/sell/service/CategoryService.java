package cn.lovingliu.sell.service;

import cn.lovingliu.sell.dataobject.ProductCategory;

import java.util.List;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-09-19
 */
public interface CategoryService {
    ProductCategory findOne(Integer productCategoryId);
    List<ProductCategory> findAll();
    List<ProductCategory> findByCategoryTypeInIdList(List<Integer> categoryIdList);
    ProductCategory save(ProductCategory productCategory);
}
