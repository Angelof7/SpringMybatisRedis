package com.netease.vcloud.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.testng.log4testng.Logger;

public class SerializeUtil {
	private static Logger logger = Logger.getLogger(SerializeUtil.class);

	public static byte[] serialize(Object object) {
		if (object == null) {
			return null;
		}

		ObjectOutputStream oos = null;
		ByteArrayOutputStream baos = null;
		try {
			// 序列化
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			byte[] bytes = baos.toByteArray();
			oos.close();
			baos.close();
			return bytes;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Object unserialize(byte[] bytes) {
		if (bytes == null) {
			return null;
		}
		ByteArrayInputStream bais = null;
		try {
			// 反序列化
			bais = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(bais);
			Object obj = ois.readObject();
			logger.info("反序列化：" + obj.toString());
			ois.close();
			bais.close();
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
