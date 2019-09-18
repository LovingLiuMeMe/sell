package cn.lovingliu.sell.service.impl;

import cn.lovingliu.sell.dataobject.ProductCategory;
import cn.lovingliu.sell.repository.ProductCategoryRepository;
import cn.lovingliu.sell.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-09-19
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Override
    public ProductCategory findOne(Integer productCategoryId) {
        return productCategoryRepository.findById(productCategoryId).orElse(null);
    }

    @Override
    public List<ProductCategory> findAll() {
        return productCategoryRepository.findAll();
    }

    @Override
    public List<ProductCategory> findByCategoryTypeInIdList(List<Integer> categoryIdList) {
        return productCategoryRepository.findByCategoryTypeIn(categoryIdList);
    }

    @Override
    public ProductCategory save(ProductCategory productCategory) {
        return productCategoryRepository.save(productCategory);
    }
}
