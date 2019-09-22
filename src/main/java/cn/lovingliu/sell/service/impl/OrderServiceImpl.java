package cn.lovingliu.sell.service.impl;

import cn.lovingliu.sell.convert.OrderMasterToOrderDTO;
import cn.lovingliu.sell.dataobject.OrderDetail;
import cn.lovingliu.sell.dataobject.OrderMaster;
import cn.lovingliu.sell.dataobject.ProductInfo;
import cn.lovingliu.sell.dto.CartDTO;
import cn.lovingliu.sell.dto.OrderDTO;
import cn.lovingliu.sell.enums.OrderStatusEnum;
import cn.lovingliu.sell.enums.PayStatusEnum;
import cn.lovingliu.sell.enums.ResultStatusEnum;
import cn.lovingliu.sell.exception.SellException;
import cn.lovingliu.sell.repository.OrderDetailRepository;
import cn.lovingliu.sell.repository.OrderMasterRepository;
import cn.lovingliu.sell.repository.ProductInfoRepository;
import cn.lovingliu.sell.service.OrderService;
import cn.lovingliu.sell.service.ProductService;
import cn.lovingliu.sell.util.BigDecimalUtil;
import cn.lovingliu.sell.util.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-09-19
 */
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Autowired
    private ProductInfoRepository productInfoRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private OrderMasterRepository orderMasterRepository;
    @Autowired
    private ProductService productService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.REPEATABLE_READ)
    public OrderDTO createrOrder(OrderDTO orderDTO) {
        BigDecimal orderAllAmount = new BigDecimal("0");

        String orderId = KeyUtil.getUniqueKey();
        for(OrderDetail orderDetail:orderDTO.getOrderDetailList()){
            // 1.查询商品详情
            ProductInfo productInfo = productInfoRepository.findById(orderDetail.getProductId()).orElse(null);
            if(productInfo==null){
                throw new SellException(ResultStatusEnum.PRODUCT_NOT_EXIT);
            }
            // 2.计算该订单的所有总价
            orderAllAmount = BigDecimalUtil.add(
                    orderAllAmount.doubleValue(),
                    BigDecimalUtil.mul(
                            productInfo.getProductPrice().doubleValue(),
                            orderDetail.getProductQuantity().doubleValue()
                    ).doubleValue()
            );
            // 3.订单详情入库
            /**
             * 出于安全和隐私 前端只会发 [{productId: "1423113435324",productQuantity: 2},...] 过来
             * 其他数据需要后端在service层组装
             * 订单Id 自身主键 都必须生成
            */
            BeanUtils.copyProperties(productInfo, orderDetail);
            orderDetail.setDetailId(KeyUtil.getUniqueKey());
            orderDetail.setOrderId(orderId);

            //orderDetail.setProductPrice(productInfo.getProductPrice());
            //orderDetail.setProductName(productInfo.getProductName());
            //orderDetail.setProductIcon(productInfo.getProductIcon());
            orderDetailRepository.save(orderDetail);

        }
        // 4.订单入库
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO, orderMaster);
        orderMaster.setOrderId(orderId);
        orderMaster.setOrderStatus(OrderStatusEnum.NEW.getCode());
        orderMaster.setPayStatus(PayStatusEnum.WAIT.getCode());
        orderMasterRepository.save(orderMaster);
        // 5.扣库存 (就算多个商品也只会调用一次)
        // lambda表达式
        List<CartDTO> cartDTOList  = orderDTO.getOrderDetailList().stream().map(
                e -> new CartDTO(e.getProductId(),e.getProductQuantity())

        ).collect(Collectors.toList());
        productService.decreaseStock(cartDTOList);


        return orderDTO;
    }

    @Override
    public OrderDTO findOne(String orderId) {
        OrderMaster orderMaster = orderMasterRepository.findById(orderId).orElse(null);
        return OrderMasterToOrderDTO.convert(orderMaster);
    }

    @Override
    public Page<OrderDTO> findList(String buyerOpenid, Pageable pageable) {
        Page<OrderMaster> orderMasterPage = orderMasterRepository.findByBuyerOpenid(buyerOpenid,pageable);
        // 此处不需要抛异常
        List<OrderDTO> orderDTOList = OrderMasterToOrderDTO.convert(orderMasterPage.getContent());
        /**
         * @Desc 将Pageable进行二次封装
         * @Author LovingLiu
        */
        Page<OrderDTO> orderDTOPage = new PageImpl<>(orderDTOList,pageable,orderMasterPage.getTotalElements());
        return orderDTOPage;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.REPEATABLE_READ)
    public OrderDTO cancel(OrderDTO orderDTO) {
        // 获取订单详情
        OrderMaster orderMaster = orderMasterRepository.findById(orderDTO.getOrderId()).orElse(null);
        if (orderMaster == null){
            throw new SellException(ResultStatusEnum.ORDER_NOT_EXIT);
        }
        // 判断订单状态
        if(orderMaster.getOrderStatus().equals(OrderStatusEnum.FINISGHED)
                ||orderMaster.getOrderStatus().equals(OrderStatusEnum.CANCEL)
        ){
            throw new SellException(ResultStatusEnum.ORDER_CANNOT_CANCEL);
        }
        // 取消订单(修改订单状态)
        OrderMaster targetMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO,targetMaster);
        targetMaster.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
        OrderMaster resultMaster = orderMasterRepository.save(targetMaster);
        if(!resultMaster.getOrderStatus().equals(OrderStatusEnum.CANCEL.getCode())){
            throw new SellException(ResultStatusEnum.ORDER_UPDATE_FAIL);
        }
        // 增加库存
        if(CollectionUtils.isEmpty(orderDTO.getOrderDetailList())){
            log.error("取消订单:{} 中,无购买订单详情",orderDTO.getOrderId());
            throw new SellException(ResultStatusEnum.ORDER_DETAIL_NOT_EXIT);
        }
        List<CartDTO> cartDTOList  = orderDTO.getOrderDetailList().stream().map(
                e -> new CartDTO(e.getProductId(),e.getProductQuantity())
        ).collect(Collectors.toList());
        productService.increaseStock(cartDTOList);

        // 如果已支付要退款
        if(orderDTO.getPayStatus().equals(PayStatusEnum.SUCCESS.getCode())){
            //TODO
        }
        return orderDTO;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.REPEATABLE_READ)
    public OrderDTO finish(OrderDTO orderDTO) {
        // 查看详细订单
        OrderMaster orderMaster = orderMasterRepository.findById(orderDTO.getOrderId()).orElse(null);
        if(orderMaster == null){
            throw new SellException(ResultStatusEnum.ORDER_NOT_EXIT);
        }
        // 判断订单状态
        if(orderMaster.getOrderStatus()!= OrderStatusEnum.NEW.getCode()){
            throw new SellException(ResultStatusEnum.ORDER_STATUS_ERROR);
        }
        // 修改状态
        orderMaster.setOrderStatus(OrderStatusEnum.FINISGHED.getCode());
        OrderMaster resultMaster = orderMasterRepository.save(orderMaster);
        if(resultMaster == null){
            throw new SellException(ResultStatusEnum.ORDER_UPDATE_FAIL);
        }
        orderDTO.setOrderStatus(OrderStatusEnum.FINISGHED.getCode());
        return orderDTO;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.REPEATABLE_READ)
    public OrderDTO paid(OrderDTO orderDTO) {
        // 查看详细订单
        OrderMaster orderMaster = orderMasterRepository.findById(orderDTO.getOrderId()).orElse(null);
        if(orderMaster == null){
            throw new SellException(ResultStatusEnum.ORDER_NOT_EXIT);
        }
        // 判断订单状态
        if(orderMaster.getOrderStatus()!= OrderStatusEnum.NEW.getCode()){
            throw new SellException(ResultStatusEnum.ORDER_STATUS_ERROR);
        }
        // 判断支付状态
        if(orderMaster.getPayStatus() != PayStatusEnum.WAIT.getCode()){
            throw new SellException(ResultStatusEnum.ORDER_PAY_STATUS_ERROR);
        }
        //修改支付状态
        orderDTO.setPayStatus(PayStatusEnum.SUCCESS.getCode());
        OrderMaster payMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO,payMaster);
        OrderMaster resultMaster = orderMasterRepository.save(payMaster);
        if(resultMaster.getPayStatus() != PayStatusEnum.SUCCESS.getCode()){
            throw new SellException(ResultStatusEnum.ORDER_PAY_STATUS_ERROR);
        }
        return orderDTO;
    }
}
