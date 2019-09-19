package cn.lovingliu.sell.service.impl;

import cn.lovingliu.sell.dataobject.OrderDetail;
import cn.lovingliu.sell.dataobject.OrderMaster;
import cn.lovingliu.sell.dataobject.ProductInfo;
import cn.lovingliu.sell.dto.OrderDTO;
import cn.lovingliu.sell.enums.ResultStatusEnum;
import cn.lovingliu.sell.exception.SellException;
import cn.lovingliu.sell.repository.OrderDetailRepository;
import cn.lovingliu.sell.repository.OrderMasterRepository;
import cn.lovingliu.sell.repository.ProductInfoRepository;
import cn.lovingliu.sell.service.OrderService;
import cn.lovingliu.sell.util.BigDecimalUtil;
import cn.lovingliu.sell.util.KeyUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.awt.print.Pageable;
import java.math.BigDecimal;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-09-19
 */
public class OrderServiceImpl implements OrderService {
    @Autowired
    private ProductInfoRepository productInfoRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private OrderMasterRepository orderMasterRepository;

    @Override
    public OrderDTO createrOrder(OrderDTO orderDTO) {
        BigDecimal orderAllAmount = new BigDecimal("0");
        String orderId = KeyUtil.getUniqueKey();
        // 1.查询商品详情
        for(OrderDetail orderDetail:orderDTO.getOrderDetailList()){
            ProductInfo productInfo = productInfoRepository.findById(orderDetail.getProductId()).orElse(null);
            if(productInfo==null){
                throw new SellException(ResultStatusEnum.PRODUCT_NOT_EXIT);
            }
            // 计算该订单的所有总价
            orderAllAmount = BigDecimalUtil.add(
                    orderAllAmount.doubleValue(),
                    BigDecimalUtil.mul(
                            productInfo.getProductPrice().doubleValue(),
                            orderDetail.getProductQuantity().doubleValue()
                    ).doubleValue()
            );
            // 订单详情入库
            /**
             * @Desc 出于安全和隐私 前端只会发 [{productId: "1423113435324",productQuantity: 2},...] 过来,其他数据需要后端在service层组装
             * 订单Id 自身主键
             * @Author LovingLiu
            */
            orderDetail.setDetailId(KeyUtil.getUniqueKey());
            orderDetail.setOrderId(orderId);
//            orderDetail.setProductPrice(productInfo.getProductPrice());
//            orderDetail.setProductName(productInfo.getProductName());
//            orderDetail.setProductIcon(productInfo.getProductIcon());
            BeanUtils.copyProperties(productInfo,orderDetail);
            orderDetailRepository.save(orderDetail);

        }
        // 3.写入订单数据库
        OrderMaster orderMaster = new OrderMaster();
        orderMaster.setOrderId(orderId);
        BeanUtils.copyProperties(orderDTO, orderMaster);
        orderMasterRepository.save(orderMaster);
        // 5.扣库存


        return null;
    }

    @Override
    public OrderDTO findOne(String orderId) {
        return null;
    }

    @Override
    public Page<OrderDTO> findList(String buyerOpenid, Pageable pageable) {
        return null;
    }

    @Override
    public OrderDTO cancel(OrderDTO orderDTO) {
        return null;
    }

    @Override
    public OrderDTO finish(OrderDTO orderDTO) {
        return null;
    }

    @Override
    public OrderDTO paid(OrderDTO orderDTO) {
        return null;
    }
}
