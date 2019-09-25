package cn.lovingliu.sell.util;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-09-25
 */
public class MathUtil {
    private static final Double MONEY_RANGE=0.01;
    /**
     * 比较2个金额是否相等,差值不大于0.01 认为是相等的
     * @param d1 比如 0.01
     * @param d2 比如 0.0100012456190
     * @return
     */
    public static Boolean equals(Double d1,Double d2){
        Double result=Math.abs(d1-d2);
        if(result<MONEY_RANGE){
            return true;
        }else{
            return false;
        }
    }
}
