package cn.lovingliu.sell.dataobject.mapper;

import cn.lovingliu.sell.dataobject.ProductCategory;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-10-09
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class ProductCategoryMapperTest {
    @Autowired
    private ProductCategoryMapper productCategoryMapper;

    @Test
    public void insertByMap() {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setCategoryId(10);
        productCategory.setCategoryName("小孩子喜欢的");
        productCategory.setCategoryType(5);
        int changeCount = productCategoryMapper.insertByProductCategory(productCategory);
        Assert.assertEquals(changeCount,1);
    }

    @Test
    public void findByCategoryType(){
        ProductCategory productCategory = productCategoryMapper.findByCategoryType(5);
        log.info("【信息】{}",productCategory);
        Assert.assertNotNull(productCategory);
    }

    @Test
    public void findAll(){
        List<ProductCategory> productCategoryList = productCategoryMapper.findAll();
        log.info("【所有分类信息】-{}",productCategoryList);
        Assert.assertNotEquals(productCategoryList.size(),0);
    }

    @Test
    public void updateByCategoryType(){
        ProductCategory productCategory = new ProductCategory();
        productCategory.setCategoryType(5);
        productCategory.setCategoryName("情人节限定");
        int changeCount = productCategoryMapper.updateByCategoryType(productCategory);
        Assert.assertEquals(changeCount,1);
    }

    @Test
    public void deleteByCategoryIdAndType(){
        int changeCount = productCategoryMapper.deleteByCategoryIdAndType(5,10);
        Assert.assertEquals(changeCount,1);
    }
}