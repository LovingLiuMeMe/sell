package cn.lovingliu.sell.dataobject;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author：LovingLiu
 * @Description: 类目表映射过来的对象,@Table 防止表名不和驼峰的类名一致时
 * @Date：Created in 2019-09-18
 */
@Table(name = "product_category")
@Entity
@DynamicUpdate
@Data
public class ProductCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer categoryId;
    private String categoryName;
    private Integer categoryType;

    private Date createTime;
    private Date updateTime;
}
