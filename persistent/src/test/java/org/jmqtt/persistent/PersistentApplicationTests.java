package org.jmqtt.persistent;

import org.jmqtt.persistent.service.PresenceService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class PersistentApplicationTests {

    @Autowired
    private PresenceService presenceService;
    @Test
    public void test(){
        presenceService.connect("89023022000010000000000001555428");
    }



}
