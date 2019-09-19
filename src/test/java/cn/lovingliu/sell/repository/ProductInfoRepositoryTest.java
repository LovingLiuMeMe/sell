package cn.lovingliu.sell.repository;

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
public class ProductInfoRepositoryTest {
    @Autowired
    private ProductInfoRepository productInfoRepository;

    @Test
    public void findByProductStatus() {
        Sort sort = new Sort(Sort.Direction.DESC,"product_price");
        PageRequest request = PageRequest.of(1,10,sort);
        Page<ProductInfo> page = productInfoRepository.findByProductStatus(0,request);
        log.info("productInfoList = {}",page);
        Assert.assertNotNull(page);
    }

    @Test
    public void testSaveOne(){
        ProductInfo productInfo = new ProductInfo();
        productInfo.setProductId("test");
        productInfo.setProductName("毛绒玩具");
        productInfo.setProductPrice(new BigDecimal(10.5));
        productInfo.setProductStock(100);
        productInfo.setProductDescription("非常好看的小熊");
        productInfo.setProductIcon("icon.jpg");
        productInfo.setProductStatus(0);
        productInfo.setCategoryType(2);

        ProductInfo result = productInfoRepository.save(productInfo);
        log.info("save result: ",result);
        //Assert.assertNotEquals(null,result);
        Assert.assertNotNull(result);
    }
}