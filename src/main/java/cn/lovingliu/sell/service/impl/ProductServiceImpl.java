package cn.lovingliu.sell.service.impl;

import cn.lovingliu.sell.dataobject.ProductInfo;
import cn.lovingliu.sell.dto.CartDTO;
import cn.lovingliu.sell.enums.ProductStatusEnum;
import cn.lovingliu.sell.enums.ResultStatusEnum;
import cn.lovingliu.sell.exception.SellException;
import cn.lovingliu.sell.repository.ProductInfoRepository;
import cn.lovingliu.sell.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Override
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.REPEATABLE_READ)
    public void increaseStock(List<CartDTO> cartDTOList) {
        for (CartDTO cartDTO:cartDTOList){
            ProductInfo productInfo = productInfoRepository.findById(cartDTO.getProductId()).orElse(null);
            if(productInfo==null){
                throw new SellException(ResultStatusEnum.ORDER_DETAIL_NOT_EXIT);
            }
            Integer resultStock = cartDTO.getProductQuantity() + productInfo.getProductStock();
            if(resultStock < 0){
                throw new SellException(ResultStatusEnum.PRODUCT_STOCK_ERROR);
            }
            productInfo.setProductStock(resultStock);
            productInfoRepository.save(productInfo);
        }
    }
    /**
     * @Desc 实现库存减少
     * @Author LovingLiu
    */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.REPEATABLE_READ)
    public void decreaseStock(List<CartDTO> cartDTOList) {
        for (CartDTO cartDTO: cartDTOList) {
           ProductInfo productInfo = productInfoRepository.findById(cartDTO.getProductId()).orElse(null);
           if (productInfo == null){
               throw new SellException(ResultStatusEnum.PRODUCT_NOT_EXIT);
           }
           Integer resultStock = productInfo.getProductStock() - cartDTO.getProductQuantity();
           /**
            * @Desc 超卖现象
            * 假设此时有两个线程同时进入这里，库存10件，且都买9件，此时就会出现超卖。卖出的数量超过库存
            * @Author LovingLiu
           */
           if(resultStock < 0){
               throw new SellException(ResultStatusEnum.PRODUCT_STOCK_ERROR);
           }
           productInfo.setProductStock(resultStock);
           productInfoRepository.save(productInfo);
        }
    }

    /**
     * @Desc seller实现商品的上架
     * @Author LovingLiu
    */
    public ProductInfo onSale(String productId){
        ProductInfo productInfo = productInfoRepository.findById(productId).orElse(null);
        if(productId == null){
            throw new SellException(ResultStatusEnum.PRODUCT_NOT_EXIT);
        }
        if(productInfo.getProductStatus() == ProductStatusEnum.UP.getCode()){
            throw new SellException(ResultStatusEnum.PRODUCT_STATUS_ERROR);
        }
        productInfo.setProductStatus(ProductStatusEnum.UP.getCode());
        return productInfoRepository.save(productInfo);
    }
    /**
     * @Desc seller实现商品的下架
     * @Author LovingLiu
    */
    public ProductInfo offSale(String productId){
        ProductInfo productInfo = productInfoRepository.findById(productId).orElse(null);
        if(productId == null){
            throw new SellException(ResultStatusEnum.PRODUCT_NOT_EXIT);
        }
        if(productInfo.getProductStatus() == ProductStatusEnum.DOWN.getCode()){
            throw new SellException(ResultStatusEnum.PRODUCT_STATUS_ERROR);
        }
        productInfo.setProductStatus(ProductStatusEnum.DOWN.getCode());
        return productInfoRepository.save(productInfo);
    }
}
