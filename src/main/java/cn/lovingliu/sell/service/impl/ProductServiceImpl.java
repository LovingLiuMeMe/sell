package cn.lovingliu.sell.service.impl;

import cn.lovingliu.sell.dataobject.ProductInfo;
import cn.lovingliu.sell.enums.ProductStatusEnum;
import cn.lovingliu.sell.repository.ProductInfoRepository;
import cn.lovingliu.sell.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * @Author：LovingLiu
 * @Description: 商品详情
 * @Date：Created in 2019-09-19
 */
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductInfoRepository productInfoRepository;

    @Override
    public ProductInfo findOne(String product_id) {
        return productInfoRepository.findById(product_id).orElse(null);
    }

    @Override
    public Page<ProductInfo> findAll(Pageable pageable) {
        Page page = productInfoRepository.findAll(pageable);
        return page;
    }

    @Override
    public Page<ProductInfo> findUpAll(Integer productStatus, Pageable pageable) {
        Page page = productInfoRepository.findByProductStatus(ProductStatusEnum.UP.getCode(),pageable);
        return page;
    }

    @Override
    public ProductInfo save(ProductInfo productInfo) {
        return productInfoRepository.save(productInfo);
    }
}
