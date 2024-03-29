package cn.lovingliu.sell.enums;

import lombok.Getter;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-09-19
 */
@Getter
public enum PayStatusEnum implements CodeEnum {
    WAIT(0,"未支付"),
    SUCCESS(1,"支付成功");

    private Integer code;
    private String desc;

    PayStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
