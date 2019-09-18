package cn.lovingliu.sell.repository;

import cn.lovingliu.sell.dataobject.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author：LovingLiu
 * @Description: dao层
 * @Date：Created in 2019-09-18
 */
public interface ProductCategoryRepository extends JpaRepository<ProductCategory,Integer> {
    List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList);
}
