package cn.lovingliu.sell.service;

import cn.lovingliu.sell.dataobject.SellerInfo;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-10-08
 */
public interface SellerService {
    SellerInfo findSellerInfoByOpenid(String openid);
}
