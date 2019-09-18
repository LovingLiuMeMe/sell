package cn.lovingliu.sell;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author：LovingLiu
 * @Description:使用@Slf4j 可以省略
 * @Date：Created in 2019-09-18
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class SellTest {
    //private static final Logger logger = LoggerFactory.getLogger(SellTest.class);

    @Test
    public void test1(){
        log.info("username:{},password:{}",123,456);// 只会在info文件中存在
        log.error("danger-----------------so much");
    }

}
