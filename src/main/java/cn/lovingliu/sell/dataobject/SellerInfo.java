package cn.lovingliu.sell.dataobject;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * @Author：LovingLiu
 * @Description: 用户信息的实体类
 * @Date：Created in 2019-10-08
 */
@Data
@DynamicUpdate
@Entity
public class SellerInfo {
    @Id
    private String sellerId;
    private String username;
    private String password;

    private String openid;

    private Date createTime;
    private Date update_time;
}
