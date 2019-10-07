package cn.lovingliu.sell.service.impl;

import cn.lovingliu.sell.dataobject.ProductInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-09-19
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ProductServiceImplTest {
    @Autowired
    private ProductServiceImpl productService;

    @Test
    public void findOne() {
        ProductInfo productInfo = productService.findOne("test");
        Assert.assertNotNull(productInfo);
    }

    @Test
    public void findAll() {
        Sort sort = new Sort(Sort.Direction.DESC,"productPrice");

        PageRequest pageRequest = PageRequest.of(0,10,sort);
        Page<ProductInfo> page = productService.findAll(pageRequest);

        System.out.println(page.getTotalElements());
    }

    @Test
    public void findUpAll() {
        Sort sort = new Sort(Sort.Direction.DESC,"productPrice");

        PageRequest pageRequest = PageRequest.of(0,10,sort);
        Page<ProductInfo> page = productService.findUpAll(0,pageRequest);
        System.out.println(page.getTotalElements());
    }

    @Test
    public void save() {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setProductId("test2");
        productInfo.setProductName("iphone11");
        productInfo.setProductPrice(new BigDecimal(5899.9));
        productInfo.setProductStock(100);
        productInfo.setProductDescription("准备好肾了么");
        productInfo.setProductIcon("icon.jpg");
        productInfo.setProductStatus(0);
        productInfo.setCategoryType(2);

        ProductInfo result = productService.save(productInfo);
        Assert.assertNotNull(result);
    }

    @Test
    public void onSale() {
       ProductInfo productInfo = productService.onSale("test");
       Assert.assertNotNull(productInfo);
       log.error("商品信息:{}",productInfo);
    }

    @Test
    public void offSale() {
        ProductInfo productInfo = productService.offSale("test");
        Assert.assertNotNull(productInfo);
        log.error("商品信息:{}",productInfo);
    }
}