## 一.买家类目
### 1.为什么dataobject中不填充`create_time`和`update_time`字段呢  
数据库定义
```sql
`create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间,当发生跟新时 自动填入当前时间',
```
ProductCategory.java
```java
@Table(name = "product_category")
@Entity
public class ProductCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer categoryId;
    private String categoryName;
    private Integer categoryType;
    //...get/set
    }
```
ProductCategoryRepositoryTest.java
```java
@Test
public void TestUpdateById(){
    Optional<ProductCategory> optional = productCategoryRepository.findById(1);
    ProductCategory productCategory = optional.orElse(null);
    // 仅仅只设置编号
    productCategory.setCategoryType(3);
    productCategoryRepository.save(productCategory);
}
```
若`ProductCategory.java`中定义了`createTime`,`update_time`时,`productCategoryRepository.findById(1)`会将对应的时间全部注入。  
直接save 在只更改了`setCategoryType(3)`的情况下`save()` 会将所有的值更新,但是`update_time`存放的任然是oldValue,所以值并不会发生变化  

*如何实现在即定义了update_time且只发生了category_type更改下的情况下保证数据的动态更新呢?*  
`@DynamicUpdate`是hibernate里面的注解，注解加上之后就不会为字段值不变的字段生成sql语句，这样sql的长度就减少了提高了传输效率和执行效率.  
**在做修改的时候，千万不要以为这两个注解不会为字段值为null的字段生成sql，如果前端传进来一个实体对象，部分字段没有传，这时候如果使用xxxRepository.save(entity) 方法，他会把null的字段设置为空，而不是不生成sql**

### 2.@Data 可不写get/set/toString方法
### 3.@Transient 忽略数据库字段（替代VO）
### 4.类的静态属性注入问题
```java
package cn.lovingliu.sell.convert;

import cn.lovingliu.sell.dataobject.OrderDetail;
import cn.lovingliu.sell.dataobject.OrderMaster;
import cn.lovingliu.sell.dto.OrderDTO;
import cn.lovingliu.sell.enums.ResultStatusEnum;
import cn.lovingliu.sell.exception.SellException;
import cn.lovingliu.sell.repository.OrderDetailRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-09-21
 */
@Component
public class OrderMasterToOrderDTO {
    @Autowired
    private static OrderDetailRepository orderDetailRepository;
    
    public static OrderDTO convert(OrderMaster orderMaster){
        if(orderMaster == null){
            throw new SellException(ResultStatusEnum.ORDER_NOT_EXIT);
        }
        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(orderMaster,orderDTO);
        List<OrderDetail> orderDetailList = orderDetailRepository.findByOrderId(orderMaster.getOrderId());
        if(CollectionUtils.isEmpty(orderDetailList)){
            throw new SellException(ResultStatusEnum.ORDER_DETAIL_NOT_EXIT);
        }
        orderDTO.setOrderDetailList(orderDetailList);
        return orderDTO;
    }

    public static List<OrderDTO> convert(List<OrderMaster> orderMasterList){
        return orderMasterList.stream().map(e ->
            convert(e)
        ).collect(Collectors.toList());
    }
}
```
当调用时会抛出`空指针异常`。原因就是 你试图@Autowired一个静态变量。  
静态变量/类变量不是对象的属性,而是一个类的属性,spring则是基于对象层面上的依赖注入.  
解决办法1: 增加set方法 并`@Autowired`
```java
    @Autowired
    public void setOrderDetailRepository(OrderDetailRepository orderDetailRepository){
        OrderMasterToOrderDTO.orderDetailRepository = orderDetailRepository;
    }
```
解决办法2: new OrderDetailRepository()  
其实看一下整个类,其实就是工具类，所有的方法都为静态方法，所以该类也可以说是静态类。基本可以全局共享，不必创建它的实例对象，所以并不用交给Spring管理(IOC).  

**但是 OrderDetailRepository 并不能实例化(new OrderDetailRepository())** 所以选择方法一