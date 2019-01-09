package org.big;

import org.big.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BioIdeApplicationTests.class)         //开启Web上下文
public class BioIdeApplicationTests {

	
	@Autowired 
	private UserService userService;
	@Test
	public void test(){
		System.out.println("test");
//		ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
//		opsForValue.set("redisKey","cluster test");
//		System.out.println(opsForValue.get("redisKey"));
	}
}