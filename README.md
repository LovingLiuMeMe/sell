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

### 5.JPA分页数据的二次封装
```java
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
```
### 6.java Date类型转换成 long类型
1.low写法 创建VO对象循环遍历 并封装
```java
Page<OrderDTO> orderDTOPage = orderService.findList(openId,pageRequest);
List<OrderDTO> sortOrderDTOList = orderDTOPage.getContent();
for(OrderDTO orderDTO:sortOrderDTOList){
    orderVODTO.setCreateTime(orderDTO.getCreateTime().getTime() / 100);
    orderVODTO.setUpdateTime(orderDTO.getUpdateTime().getTime() / 100);
}
```
2.利用@RestController的序列化为json的特点
1.创建序列化工具类 并实现`JsonSerializer`接口
```java
package cn.lovingliu.sell.util.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Date;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-09-22
 */
public class DateToLongSerializer extends JsonSerializer<Date> {
    @Override
    public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeNumber(value.getTime() / 1000);
    }
}
```
2.在要序列化的类的具体属性上添加`@JsonSerialize`
```java
package cn.lovingliu.sell.dto;

import cn.lovingliu.sell.dataobject.OrderDetail;
import cn.lovingliu.sell.util.serializer.DateToLongSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Author：LovingLiu
 * @Description: 数据传输对象 在各个层之间传输
 * @Date：Created in 2019-09-19
 */
@Data
public class OrderDTO {
    private String orderId;
    private String buyerName;
    private String buyerPhone;
    private String buyerAddress;
    private String buyerOpenid;
    private BigDecimal orderAmount;
    // 订单状态 默认为新下单
    private Integer orderStatus;
    // 支付状态 默认为未支付
    private Integer payStatus;

    @JsonSerialize(using = DateToLongSerializer.class)
    private Date createTime;

    @JsonSerialize(using = DateToLongSerializer.class)
    private Date updateTime;

    private List<OrderDetail> orderDetailList;


}
```
### 7.设置序列化(是否必须返回字段)
1.当属性有值时 才进行序列化  
**全局**  
application-prod.yml
```xml
spring:
  jackson:
    default-property-inclusion: non_null
```
**单例**  
在具体类上应用`@JsonInclude(JsonInclude.Include.NON_NULL)`

2.当值为null时,希望String类型,值为null时 返回"",List类型值为null时，返回[]
移除所有关于 `@JsonInclude(JsonInclude.Include.NON_NULL)` 设置
然后设置类的初始值
```java
    private String buyerName = "";
    private List<OrderDetail> orderDetailList = new ArrayList<>();
    
```
### 8.Spring 访问URL
https://developers.weixin.qq.com/doc/offiaccount/OA_Web_Apps/Wechat_webpage_authorization.html

### 9.@EnableConfigurationProperties 和 @AllArgsConstructor
1.@AllArgsConstructor  
@AllArgsContructor： 会生成一个包含所有变量，同时如果变量使用了NotNull annotation ， 会进行是否为空的校验.  
2.@EnableConfigurationProperties  
`@EnableConfigurationProperties`注解的作用是：使使用 `@ConfigurationProperties` 注解的类生效。  
如果一个配置类只配置`@ConfigurationProperties`注解，而没有使用`@Component`，那么在IOC容器中是获取不到properties 配置文件转化的bean。说白了 `@EnableConfigurationProperties` 相当于把使用 `@ConfigurationProperties` 的类进行了一次注入。  
当`@EnableConfigurationProperties`注解应用到你的`@Configuration`时， 任何被`@ConfigurationProperties`注解的beans将自动被`Environment`属性配置。  
**不使用 `@EnableConfigurationProperties` 进行注册，使用 `@Component` 注册**

### 10.@Configuration
`@Configuration` 用于定义配置类，可替换XML配置文件，被注解的类内部包含一个或多个`@Bean`注解方法。  
1.1、`@Configuration`配置spring并启动spring容器  
`@Configuration`标注在类上，相当于把该类作为spring的xml配置文件中的`<beans>`，作用为：配置spring容器(应用上下文)  

1.2、`@Configuration`启动容器+`@Bean`注册Bean，`@Bean`下管理bean的生命周期  
`@Bean`标注在方法上(返回某个实例的方法)，等价于spring的xml配置文件中的`<bean>`，作用为：注册bean对象  

**@Bean下管理bean的生命周期**  
可以使用基于 Java 的配置来管理 bean 的生命周期。`@Bean` 支持两种属性，即 `initMethod` 和`destroyMethod`，这些属性可用于定义生命周期方法。在实例化 `bean` 或即将销毁它时，容器便可调用生命周期方法。  

```java
@Configuration
public class TestConfiguration {
    public TestConfiguration() {
        System.out.println("TestConfiguration容器启动初始化。。。");
    }

    //@Bean注解注册bean,同时可以指定初始化和销毁方法
    @Bean(name="testBean",initMethod="start",destroyMethod="cleanUp")
    @Scope("prototype")
    public TestBean testBean() {
        return new TestBean();
    }
}
```
1.3、@Configuration启动容器+@Component注册Bean  
**总结**  
`@Configuation`等价于`<Beans></Beans>`  
`@Bean`等价于`<Bean></Bean>`  
`@ComponentScan`等价于`<context:component-scan base-package="com.dxz.demo"/>`  
`@Component` 等价于`<Bean></Bean>`  
**`@Bean` VS `@Component`**
两个注解的结果是相同的，bean都会被添加到Spring上下文中。
`@Component` 标注的是类,允许通过自动扫描发现。`@Bean`需要在配置类`@Configuation`中使用。
`@Component`类使用的方法或字段时不会使用CGLIB增强。而在`@Configuration`类中使用方法或字段时则使用CGLIB创造协作对象
假设我们需要将一些第三方的库组件装配到应用中或者 我们有一个在多个应用程序中共享的模块，它包含一些服务。并非所有应用都需要它们。

如果在这些服务类上使用`@Component`并在应用程序中使用组件扫描，我们最终可能会检测到超过必要的bean。导致应用程序无法启动
但是我们可以使用 `@Bean`来加载。

### 11.在Controller中使用Map来接收不确定参数
```java
@GetMapping("list")
public ModelAndView list(@RequestParam(value = "openid",required = true)String openId,
                         @RequestParam(value = "pageNum",defaultValue = "1")Integer pageNum,
                         @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize,
                         @RequestParam(value = "sortBy",defaultValue = "updateTime")String sortBy,
                         @RequestParam(value = "sortLift",defaultValue = "desc")String sortLift,
                         @RequestParam Map<String,Object> map)
```
同时`map`前边要加上`@RequestParam`，参数才能自动进入到`map`中

### 12.@RequestBody里的大文章
`@RequestBody`主要用来接收前端传递给后端的`json字符串`**中**的数据的(`请求体中`的数据的)；**GET方式**无请求体，所以使用`@RequestBody`接收数据时，前端不能使用GET方式提交数据，而是用POST方式进行提交。  
注：一个请求，只有一个`RequestBody`；一个请求，可以有多个`RequestParam`。

注：当同时使用`@RequestParam（）`和`@RequestBody`时，`@RequestParam（）`指定的参数可以是普通元素、数组、集合、对象等等  
(即:当，`@RequestBody` 与`@RequestParam()`可以同时使用时，原SpringMVC接收参数的机制不变，只不过`RequestBody` 接收的是请求体里面的数据；而`RequestParam`接收的是`key-value`里面的参数，所以它会被切面进行处理从而可以用普通元素、数组、集合、对象等接收)。  
即：如果参数时放在请求体中，传入后台的话，那么后台要用`@RequestBody`才能接收到  
如果不是放在请求体中的话，那么后台接收前台传过来的参数时，要用`@RequestParam`来接收，或则形参前什么也不写也能接收。

注：如果参数前写了`@RequestParam(xxx)`，那么前端必须有对应的xxx名字才行(不管其是否有值，当然可以通过设置该注解的required属性来调节是否必须传)，如果没有xxx名的话，那么请求会出错，报400。

注：如果参数前不写`@RequestParam(xxx)`的话，那么就前端可以有可以没有对应的xxx名字才行，如果有xxx名的话，那么就会自动匹配；没有的话，请求也能正确发送。

***如果后端参数是一个对象（domain实体类）***，且该参数前是以`@RequestBody`修饰的，那么前端传递json参数时，必须满足以下要求：

1.后端`@RequestBody`注解对应的类在将HTTP的输入流(含请求体)装配到目标类(即：`@RequestBody`后面的类)时，会根据`json`字符串中的`key`来匹配对应实体类的属性，如果匹配一致且`json`中的该`key`对应的值符合(或可转换为)实体类的对应属性的类型要求时,会调用实体类的`setter`方法将值赋给该属性。

2.`json`字符串中，如果`value`为`""`的话，后端对应属性如果是`String`类型的，那么接受到的就是`""`，如果是后端属性的类型是`Integer`、`Double`等类型，那么接收到的就是`null`。

3.`json`字符串中，如果`value`为`null`的话，后端对应收到的就是`null`。

4.如果某个参数没有`value`的话，在传`json`字符串给后端时，要么干脆就不把该字段写到`json`字符串中；要么写`value`时， 必须有值，`null`  或`""`都行。千万不能有类似`"stature"`:，这样的写法，如:
```json
{
"stature": ,
"age": null,
"name": ""
}
```
### 13.修改商品时的小技巧
1.传统 前端将商品的所有属性传递到后端 后端保存
```java
 public ModelAndView save(@Valid ProductForm productForm,
                             BindingResult bindingResult,
                             Map<String,Object> map){
    /** 省略其他操作 **/
    ProductInfo productInfo = new ProductInfo();
    BeanUtils.copyProperties(productForm,productInfo);
    productService.save(productInfo);
}
```
2.前端只用传递 必要属性(id) 和 发生更改的属性即可
```java
    ProductInfo productInfo = productService.findOne(productForm.getProductId());
    BeanUtils.copyProperties(productForm,productInfo);
    productService.save(productInfo);
```
3.前端type='number'
```java
package cn.lovingliu.sell.form;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author：LovingLiu
 * @Description: seller端 保存/修改
 * @Date：Created in 2019-09-30
 */
@Data
public class ProductForm {
    private String productId;
    private String productName;
    private String productDescription;
    private String productIcon;
    /**
     * number(前端) -> BigDecimal controller 报错
     * 怀疑是因为 精度丢失之类的情况发生,所有使用String
    */
    //private BigDecimal productPrice;
    private String productPrice

    private Integer productStock;
    private Integer categoryType;
}

```

### 14.springBoot webSocket
1.引入依赖
```xml
<!-- websocket依赖 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-websocket</artifactId>
</dependency>
```
2.配置类编写
```java
package cn.lovingliu.sell.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @Author：LovingLiu
 * @Description: websocket的配置
 * @Date：Created in 2019-10-08
 */
@Component
public class WebsocketConfig {
    @Bean
    public ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter();
    }
}
```
3.编写特殊的controller
```java
package cn.lovingliu.sell.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @Author：LovingLiu
 * @Description: websocket 区别于一般的Controller
 * @Date：Created in 2019-10-08
 */
@Component
@ServerEndpoint("/webSoket")
@Slf4j
public class WebSocket {
    private Session session;

    private static CopyOnWriteArraySet<WebSocket> webSocketSet = new CopyOnWriteArraySet<>();
    @OnOpen
    public void onOpen(Session session){
        this.session = session;
        webSocketSet.add(this);
        log.info("【websocket消息】有新的连接");
    }
}
```
### 15.SpringBoot 缓存的使用
当我们在调用一个缓存方法时会把该方法参数和返回结果作为一个键值对存放在缓存中，等到下次利用同样的参数来调用该方法时将不再执行该方法，而是直接从缓存中获取结果进行返回。  
所以在使用Spring Cache的时候我们要保证我们缓存的方法对于相同的方法参数要有相同的返回结果。

Spring为我们提供了几个注解来支持Spring Cache。  
其核心主要是`@Cacheable`和`@CacheEvict`。使用`@Cacheable`标记的方法在执行后`Spring Cache`将缓存其返回结果，而使用`@CacheEvict`标记的方法会在方法执行前或者执行后移除`Spring Cache`中的某些元素。

1.@Cacheable  
`@Cacheable`可以标记在一个方法上，也可以标记在一个类上。  
当标记在一个方法上时表示该方法是支持缓存的，当标记在一个类上时则表示该类所有的方法都是支持缓存的。  
对于一个支持缓存的方法，Spring会在其被调用后将其返回值缓存起来，以保证下次利用同样的参数来执行该方法时可以直接从缓存中获取结果，而不需要再次执行该方法。
属性:  

|  value   | 缓存的名称，在 spring 配置文件中定义，必须指定至少一个  |例如:@Cacheable(value=”mycache”) @Cacheable(value={”cache1”,”cache2”} |
|  ----  | ----  | ----  |
| key  | 缓存的 key，可以为空，如果指定要按照 SpEL 表达式编写，如果不指定，则缺省按照方法的所有参数进行组合 | @Cacheable(value=”testcache”,key=”#userName”) |
| condition  | 缓存的条件，可以为空，使用 SpEL 编写，返回 true 或者 false，只有为 true 才进行缓存 | 单元格 |

1.1.1  value属性指定Cache名称  
       value属性是必须指定的，其表示当前方法的返回值是会被缓存在哪个Cache上的，对应Cache的名称。其可以是一个Cache也可以是多个Cache，当需要指定多个Cache时其是一个数组。
```java
@Cacheable("cache1")//Cache是发生在cache1上的
   public User find(Integer id) {
      returnnull;
   }
  @Cacheable({"cache1", "cache2"})//Cache是发生在cache1和cache2上的
   public User find(Integer id) {
      returnnull;
   }
```
1.1.2  使用key属性自定义key  
key属性是用来指定Spring缓存方法的返回结果时对应的key的。  
该属性支持SpringEL表达式。当我们没有指定该属性时，Spring将使用默认策略生成key。  
我们这里先来看看自定义策略，至于默认策略会在后文单独介绍。
自定义策略是指我们可以通过Spring的EL表达式来指定我们的key。这里的EL表达式可以使用方法参数及它们对应的属性。使用方法参数时我们可以直接使用“#参数名”或者“#p参数index”。下面是几个使用参数作为key的示例。
```java
/**
* key 是指传入时的参数
*
*/
   @Cacheable(value="users", key="#id")
   public User find(Integer id) {
      returnnull;
   }
// 表示第一个参数
  @Cacheable(value="users", key="#p0")
   public User find(Integer id) {
      returnnull;
   }
// 表示User中的id值
   @Cacheable(value="users", key="#user.id")
   public User find(User user) {
      returnnull;
   }
 // 表示第一个参数里的id属性值
   @Cacheable(value="users", key="#p0.id")
   public User find(User user) {
      returnnull;
   }
```
1.1.3  condition属性指定发生的条件  
有的时候我们可能并不希望缓存一个方法所有的返回结果。  
通过condition属性可以实现这一功能。  
condition属性默认为空，表示将缓存所有的调用情形。  
其值是通过SpringEL表达式来指定的，当为true时表示进行缓存处理；当为false时表示不进行缓存处理，即每次调用该方法时该方法都会执行一次。如下示例表示只有当user的id为偶数时才会进行缓存。
```java
    // 根据条件判断是否缓存
    @Cacheable(value={"users"}, key="#user.id", condition="#user.id%2==0")
   public User find(User user) {
      System.out.println("find user by user " + user);
      return user;
   }
```
1.2 @CachePut  
在支持Spring Cache的环境下，对于使用`@Cacheable`标注的方法，Spring在每次执行前都会检查Cache中是否存在相同key的缓存元素，如果存在就不再执行该方法，而是直接从缓存中获取结果进行返回，否则才会执行并将返回结果存入指定的缓存中。
`@CachePut`也可以声明一个方法支持缓存功能。与`@Cacheable`不同的是使用`@CachePut`标注的方法在执行前不会去检查缓存中是否存在之前执行过的结果，而是每次都会执行该方法，并将执行结果以键值对的形式存入指定的缓存中。
```java
 //@CachePut也可以标注在类上和方法上。使用@CachePut时我们可以指定的属性跟@Cacheable是一样的。
   @CachePut("users")//每次都会执行方法，并将结果存入指定的缓存中
   public User find(Integer id) {
      return null;
   }
```
1.3 @CacheEvict
`@CacheEvict`是用来标注在需要清除缓存元素的方法或类上的。当标记在一个类上时表示其中所有的方法的执行都会触发缓存的清除操作。  
`@CacheEvict`可以指定的属性有value、key、condition、allEntries和beforeInvocation。其中value、key和condition的语义与`@Cacheable`对应的属性类似。  
即value表示清除操作是发生在哪些Cache上的（对应Cache的名称）；key表示需要清除的是哪个key，如未指定则会使用默认策略生成的key；condition表示清除操作发生的条件。