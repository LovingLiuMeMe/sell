package cn.lovingliu.sell.service.impl;

import cn.lovingliu.sell.dataobject.SellerInfo;
import cn.lovingliu.sell.repository.SellerInfoRepository;
import cn.lovingliu.sell.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author：LovingLiu
 * @Description: 用户信息service
 * @Date：Created in 2019-10-08
 */
@Service
public class SellerServiceImpl implements SellerService {
    @Autowired
    private SellerInfoRepository sellerInfoRepository;

    public SellerInfo findSellerInfoByOpenid(String openid){
        return sellerInfoRepository.findByOpenid(openid);
    }
}
