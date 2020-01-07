package org.jmqtt.persistent;

import org.jmqtt.persistent.utils.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@Rollback(false)
@RunWith(SpringRunner.class)
public class PersistentApplicationTests {

    @Test
    public void test() throws Exception{
        TestUtils testUtils = new TestUtils();
        testUtils.pressureUtil(1);
        Thread.currentThread().wait();
    }



}
