package cn.lovingliu.sell.form;

import lombok.Data;

/**
 * @Author：LovingLiu
 * @Description: 类目的表单验证
 * @Date：Created in 2019-10-07
 */
@Data
public class CategoryForm {
    private Integer categoryId;
    private String categoryName;
    private Integer categoryType;
}
