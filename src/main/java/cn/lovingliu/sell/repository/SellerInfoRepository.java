package cn.lovingliu.sell.repository;

import cn.lovingliu.sell.dataobject.SellerInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author：LovingLiu
 * @Description: 用户信息
 * @Date：Created in 2019-10-08
 */
@Repository
public interface SellerInfoRepository extends JpaRepository<SellerInfo,String> {
    SellerInfo findByOpenid(String openid);
}
