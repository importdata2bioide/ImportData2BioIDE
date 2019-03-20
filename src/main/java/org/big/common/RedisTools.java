package org.big.common;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component("redisTools")
public class RedisTools {
	
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	//DEFAULT_EXPIRE 过期时长，单位：秒
    public static final long DEFAULT_EXPIRE = 60 * 10;//10min过期

	public void setDataToRedis(Object[] args,String shortString, Object obj) {
		if(obj == null) {
			return;
		}
		String redisKey = getRedisKey(args, shortString);
		String value = BytesUtil.objectToBytesString(obj);//将对象转换为二进制字节串
		redisTemplate.opsForValue().set(redisKey, value);
	}

	public Object getDataFromRedis(Object[] args,String shortString) {
		String value = redisTemplate.opsForValue().get(getRedisKey(args, shortString));
		if(StringUtils.isEmpty(value)) {
			return null;
		}
		//反序列化
		Object object = null;
		try {
			object = BytesUtil.StringToObject(value);//将二进制字节串转换为对象
		} catch (Exception e) {
			e.printStackTrace();
		}
		return object;
		
	}
	
	/**
	 * 
	 * @Description 根据传入参数生成key
	 * @param args
	 * @param shortString
	 * @return
	 * @author ZXY
	 */
	private String  getRedisKey(Object[] args,String shortString) {
		StringBuffer key = new StringBuffer();
		key.append(shortString);
		key.append("_");
		if(args != null && args.length >0) {
			for (Object arg : args) {
				key.append(arg);
			}
		}
		return key.toString();
	}

}
//zhouxinyue`s note
//RedisTemplate：如果没特殊情况，切勿定义成RedisTemplate<Object, Object>，否则根据里氏替换原则，使用的时候会造成类型错误 。
//redisTemplate.opsForValue();//操作字符串
//redisTemplate.opsForHash();//操作hash
//redisTemplate.opsForList();//操作list
//redisTemplate.opsForSet();//操作set
//redisTemplate.opsForZSet();//操作有序set
