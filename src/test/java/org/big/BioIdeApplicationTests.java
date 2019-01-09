package org.big;

import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes = BioIdeApplication.class)         //开启Web上下文
@WebAppConfiguration
public class BioIdeApplicationTests {

	public void test() {
		System.out.println("======TEST=======");
	}
}