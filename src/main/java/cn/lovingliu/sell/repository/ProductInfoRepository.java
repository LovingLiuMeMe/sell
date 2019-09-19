package cn.lovingliu.sell.repository;

import cn.lovingliu.sell.dataobject.ProductInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-09-19
 */
@Repository
public interface ProductInfoRepository extends JpaRepository<ProductInfo,String> {
    /**
     * @Desc 查询对应状态的商品列表
     * @Author LovingLiu
     */
    Page<ProductInfo> findByProductStatus(Integer productStatus, Pageable pageable);

}
