package cn.lovingliu.sell.dataobject.mapper;

import cn.lovingliu.sell.dataobject.ProductCategory;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

/**
 * @Author：LovingLiu
 * @Description: 类目的mybatis
 * @Date：Created in 2019-10-09
 */
public interface ProductCategoryMapper {
    String BASE_COLUMN = " category_id,category_name,category_type,create_time,create_time ";
    /**
     * @Desc 数据库返回的结果集和实体类字段不对应，我们就需要手动指定映射关系
     * @Results各个属性的含义，
     * id为当前结果集声明唯一标识，
     * value值为结果集映射关系，
     * @Result代表一个字段的映射关系，
     * column指定数据库字段的名称，
     * property指定实体类属性的名称，
     * jdbcType数据库字段类型，
     * @Result里的id值为true表明主键，默认false；
     * 使用@ResultMap来引用映射结果集，其中value可省略。
     * @Author LovingLiu
    */
    @Select("select"+BASE_COLUMN+" from product_category")
    @Results(id="productCategoryMap", value={
            @Result(column="category_id", property="categoryId", jdbcType= JdbcType.INTEGER, id=true),
            @Result(column="category_name", property="categoryName", jdbcType=JdbcType.VARCHAR),
            @Result(column="category_type ", property="categoryType", jdbcType=JdbcType.INTEGER),
            @Result(column="create_time", property="createTime", jdbcType=JdbcType.DATE),
            @Result(column="update_time", property="updateTime", jdbcType=JdbcType.DATE)
    })
    List<ProductCategory> findAll();



    @Insert("insert into product_category(category_id,category_name,category_type,create_time,update_time) " +
            "values(#{categoryId,jdbcType=VARCHAR}," +
            "#{categoryName,jdbcType=VARCHAR}," +
            "#{categoryType,jdbcType=INTEGER}," +
            "now()," +
            "now())")
    int insertByProductCategory(ProductCategory productCategory);

    @Select("select"+BASE_COLUMN+"from product_category where category_type = #{categoryType,jdbcType=INTEGER}")
    @ResultMap(value = "productCategoryMap")
    ProductCategory findByCategoryType(Integer categoryType);

    /**
     * @Desc 注意:若涉及到多个参数,需要指定(@Param("categoryName") String categoryName,@Param("categoryType") String categoryType))
     * @Author LovingLiu
    */
    @Update("update product_category set category_name = #{categoryName,jdbcType=VARCHAR} where category_type = #{categoryType,jdbcType=INTEGER}")
    int updateByCategoryType(ProductCategory productCategory);


    @Delete("delete from product_category where category_type = #{categoryType,jdbcType=INTEGER} and category_id = #{categoryId,jdbcType=INTEGER}")
    int deleteByCategoryIdAndType(@Param("categoryType") Integer categoryType,
                                  @Param("categoryId") Integer categoryId);


}
