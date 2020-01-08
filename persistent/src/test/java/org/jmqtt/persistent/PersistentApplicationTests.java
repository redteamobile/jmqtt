package org.jmqtt.persistent;

import org.jmqtt.persistent.service.PresentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class PersistentApplicationTests {

    @Autowired
    private PresentService presentService;
    @Test
    public void test(){
        presentService.connect("89023022000010000000000001555428");
    }



}
