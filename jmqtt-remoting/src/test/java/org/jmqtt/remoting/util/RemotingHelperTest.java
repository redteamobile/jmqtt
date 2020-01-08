package org.jmqtt.remoting.util;

import org.jmqtt.persistent.asyncTask.AsyncTask;
import org.jmqtt.persistent.utils.SpringUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;


@SpringBootTest
@RunWith(SpringRunner.class)
public class RemotingHelperTest {


    @Test
    public void getLocalAddr() {
        String currentIp = RemotingHelper.getLocalAddr();
        assert currentIp != null;
    }

    @Test
    public void test(){
    }
}