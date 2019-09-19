package cn.lovingliu.sell.service;

import cn.lovingliu.sell.dataobject.ProductInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-09-19
 */
public interface ProductService {
    ProductInfo findOne(String product_id);
    /**
     * @Desc 管理端查询所有商品
     * @Author LovingLiu
    */
    Page<ProductInfo> findAll(Pageable pageable);
    /**
     * @Desc 客户端仅仅只查询上架的商品
     * @Author LovingLiu
    */
    Page<ProductInfo> findUpAll(Integer productStatus,Pageable pageable);

    ProductInfo save(ProductInfo productInfo);

    //加减库存

}
