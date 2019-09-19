package cn.lovingliu.sell.enums;

/**
 * @Author：LovingLiu
 * @Description: 商品状态
 * @Date：Created in 2019-09-19
 */
public enum ProductStatusEnum {
    UP(0,"上架"),
    DOWN(1,"下架");

    private Integer code;
    private String desc;

    ProductStatusEnum(Integer code,String desc){
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
