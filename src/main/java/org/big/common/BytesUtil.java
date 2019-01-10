package org.big.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Optional;

import org.springframework.util.StringUtils;

public class BytesUtil {

	public static String objectToBytesString(Object obj) {
		byte[] bytes = null;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream sOut;
		try {
			sOut = new ObjectOutputStream(out);
			sOut.writeObject(obj);
			sOut.flush();
			bytes = out.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return BytesUtil.toHexString(Optional.ofNullable(bytes).get());
	}

	public static Object StringToObject(String hexString) {
		byte[] bytes = toByteArray(hexString);
		Object object = null;
		ByteArrayInputStream in = new ByteArrayInputStream(bytes);
		ObjectInputStream sIn;
		try {
			sIn = new ObjectInputStream(in);
			object = sIn.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Optional.ofNullable(object).get();

	}

	
	public static byte[] toByteArray(String hexString) {
		if (StringUtils.isEmpty(hexString))
			throw new IllegalArgumentException("this hexString must not be empty");

		hexString = hexString.toLowerCase();
		final byte[] byteArray = new byte[hexString.length() / 2];
		int k = 0;
		for (int i = 0; i < byteArray.length; i++) {// 因为是16进制，最多只会占用4位，转换成字节需要两个16进制的字符，高位在先
			byte high = (byte) (Character.digit(hexString.charAt(k), 16) & 0xff);
			byte low = (byte) (Character.digit(hexString.charAt(k + 1), 16) & 0xff);
			byteArray[i] = (byte) (high << 4 | low);
			k += 2;
		}
		return byteArray;
	}

	/**
	 * 
	 * @Description 字节数组转成16进制表示格式的字符串
	 * @param byteArray 需要转换的字节数组
	 * @return 16进制表示格式的字符串
	 * @author ZXY
	 */
	public static String toHexString(byte[] byteArray) {
		if (byteArray == null || byteArray.length < 1)
			throw new IllegalArgumentException("this byteArray must not be null or empty");

		final StringBuilder hexString = new StringBuilder();
		for (int i = 0; i < byteArray.length; i++) {
			if ((byteArray[i] & 0xff) < 0x10)// 0~F前面不零
				hexString.append("0");
			hexString.append(Integer.toHexString(0xFF & byteArray[i]));
		}
		return hexString.toString().toLowerCase();
	}
}