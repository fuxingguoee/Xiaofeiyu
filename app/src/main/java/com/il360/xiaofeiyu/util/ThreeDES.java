package com.il360.xiaofeiyu.util;

import java.net.URLEncoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;

public class ThreeDES {

	public static final String IV = "1111119-";
	public static final String KEY = "xfycbbcaomei665122";

	/**
	 * DESCBC加密
	 *
	 * @param src 数据源
	 * @param key 密钥，长度必须是8的倍数
	 * @return 返回加密后的数据
	 * @throws Exception
	 */
	public static String encryptDESCBC(String src) throws Exception {
		// --生成key,同时制定是des还是DESede,两者的key长度要求不同
		 DESKeySpec desKeySpec = new DESKeySpec(KEY.getBytes("UTF-8"));
		 SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		 SecretKey secretKey = keyFactory.generateSecret(desKeySpec);

		// --加密向量
		 IvParameterSpec iv = new IvParameterSpec(IV.getBytes("UTF-8"));

		// --通过Chipher执行加密得到的是一个byte的数组,Cipher.getInstance("DES")就是采用ECB模式,cipher.init(Cipher.ENCRYPT_MODE,
		// secretKey)就可以了.
		 Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		 cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
		 byte[] b = cipher.doFinal(src.getBytes("UTF-8"));
		return Base64.encode(b);
	}

	/**
	 * DESCBC解密
	 *
	 * @param src 数据源
	 * @param key 密钥，长度必须是8的倍数
	 * @return 返回解密后的原始数据
	 * @throws Exception
	 */
	public static String decryptDESCBC( String src,  String key) throws Exception {
		// --通过base64,将字符串转成byte数组
		final BASE64Decoder decoder = new BASE64Decoder();
		final byte[] bytesrc = decoder.decodeBuffer(src);

		// --解密的key
		final DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
		final SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		final SecretKey secretKey = keyFactory.generateSecret(desKeySpec);

		// --向量
		final IvParameterSpec iv = new IvParameterSpec(IV.getBytes("UTF-8"));

		// --Chipher对象解密Cipher.getInstance("DES")就是采用ECB模式,cipher.init(Cipher.DECRYPT_MODE,
		// secretKey)就可以了.
		final Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
		final byte[] retByte = cipher.doFinal(bytesrc);

		return new String(retByte);

	}

	// 3DESECB加密,key必须是长度大于等于 3*8 = 24 位哈
	public static String encryptThreeDESECB(final String password, final String key) throws Exception {
		final DESedeKeySpec dks = new DESedeKeySpec(key.getBytes("UTF-8"));
		final SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
		final SecretKey securekey = keyFactory.generateSecret(dks);

		final Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, securekey);

		String src = URLEncoder.encode(password, "UTF-8");

		final byte[] b = cipher.doFinal(src.getBytes());

		final BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(b).replaceAll("\r", "").replaceAll("\n", "");

	}

	// 3DESECB解密,key必须是长度大于等于 3*8 = 24 位哈
	public static String decryptThreeDESECB(final String src, final String key) throws Exception {
		// --通过base64,将字符串转成byte数组
		final BASE64Decoder decoder = new BASE64Decoder();
		final byte[] bytesrc = decoder.decodeBuffer(src);
		// --解密的key
		final DESedeKeySpec dks = new DESedeKeySpec(key.getBytes("UTF-8"));
		final SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
		final SecretKey securekey = keyFactory.generateSecret(dks);

		// --Chipher对象解密
		final Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, securekey);
		final byte[] retByte = cipher.doFinal(bytesrc);

		return new String(retByte);
	}
}
