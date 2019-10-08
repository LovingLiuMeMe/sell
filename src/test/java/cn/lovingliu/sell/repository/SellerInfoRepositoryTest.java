package cn.lovingliu.sell.repository;

import cn.lovingliu.sell.dataobject.SellerInfo;
import cn.lovingliu.sell.util.KeyUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-10-08
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SellerInfoRepositoryTest {
    @Autowired
    private SellerInfoRepository sellerInfoRepository;

    @Test
    public void save(){
        SellerInfo sellerInfo = new SellerInfo();
        sellerInfo.setSellerId(KeyUtil.getUniqueKey());
        sellerInfo.setUsername("lovingliu");
        sellerInfo.setPassword("lovingliu");
        sellerInfo.setOpenid("abc");

        SellerInfo result = sellerInfoRepository.save(sellerInfo);
        Assert.assertNotNull(result);
    }
    @Test
    public void findByOpenid() {
        SellerInfo sellerInfo = sellerInfoRepository.findByOpenid("abc");
        Assert.assertNotNull(sellerInfo);
    }
}