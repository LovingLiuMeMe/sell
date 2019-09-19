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
