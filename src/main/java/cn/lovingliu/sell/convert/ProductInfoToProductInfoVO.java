package cn.lovingliu.sell.convert;

import cn.lovingliu.sell.dataobject.ProductInfo;
import cn.lovingliu.sell.enums.ProductStatusEnum;
import cn.lovingliu.sell.util.CodeEnumUtil;
import cn.lovingliu.sell.util.DateTimeUtil;
import cn.lovingliu.sell.vo.ProductInfoVO;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-09-28
 */
public class ProductInfoToProductInfoVO {

    public static ProductInfoVO convert(ProductInfo productInfo){
        ProductInfoVO productInfoVO = new ProductInfoVO();
        BeanUtils.copyProperties(productInfo, productInfoVO);


        productInfoVO.setCreateTime(DateTimeUtil.dateToStr(productInfo.getCreateTime()));
        productInfoVO.setUpdateTime(DateTimeUtil.dateToStr(productInfo.getUpdateTime()));
        productInfoVO.setProductStatusMessage(CodeEnumUtil.getByCode(productInfo.getProductStatus(), ProductStatusEnum.class).getDesc());

        return productInfoVO;
    }
    public static List<ProductInfoVO> convert(List<ProductInfo> productInfoList){
        List<ProductInfoVO> list = Lists.newArrayList();
        for(ProductInfo productInfo: productInfoList){
            list.add(convert(productInfo));
        }
        return list;
    }
}
