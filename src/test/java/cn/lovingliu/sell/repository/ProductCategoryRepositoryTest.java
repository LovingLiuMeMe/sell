package cn.lovingliu.sell.repository;

import cn.lovingliu.sell.dataobject.ProductCategory;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

/**
 * @Author：LovingLiu
 * @Description: dao层中productcategory的单元测试
 * @Date：Created in 2019-09-18
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ProductCategoryRepositoryTest {
    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Test
    public void TestSave(){
        ProductCategory productCategory = new ProductCategory();
        productCategory.setCategoryName("电子设备");
        productCategory.setCategoryType(1);
        productCategoryRepository.save(productCategory);
    }

    @Test
    public void TestFindById(){
        Optional<ProductCategory> productCategory = productCategoryRepository.findById(1);
        System.out.println(productCategory);
    }

    @Test
    public void TestUpdateById(){
        Optional<ProductCategory> optional = productCategoryRepository.findById(1);
        ProductCategory productCategory = optional.orElse(null);
        // 仅仅只设置编号
        productCategory.setCategoryType(9);
        productCategoryRepository.save(productCategory);
    }
    @Test
    public void TestFindByCategoryTypeIn(){
        List<Integer> idList = Lists.newArrayList();
        idList.add(2);
        idList.add(1);
        List<ProductCategory> productCategoryList = productCategoryRepository.findByCategoryTypeIn(idList);
        log.info("productCategoryList={}",productCategoryList);
    }
}