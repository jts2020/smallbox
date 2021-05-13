package test.com.jts.demo.busi;

import com.jts.smallbox.SmallBoxApplication;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SmallBoxApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BusiControllerTest {

    @Resource
    private TestRestTemplate restTemplate;

    @Test
    public void testbusiIndex() {
        String result = restTemplate.getForObject("/busi/index?param=1", String.class);
        Assert.assertTrue(result.contains("busiIndex_1"));
    }

}
