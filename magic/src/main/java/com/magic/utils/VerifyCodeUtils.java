package com.magic.utils;

import com.magic.springboot.exception.ApiException;
import com.magic.utils.verifycode.VcgSimpleImageImpl;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.OutputStream;

public class VerifyCodeUtils {
	private static String verifyKey = "I3A6CDJTQJUV7HINGJTK5S4ZF1P1214P";
	/**
	 * 生成验证码
	 * @param width 图片的长度
	 * @param height 图片的宽度
	 * @param wordLength 验证码字母个数
	 * @param outputStream 图片输出流
	 * @return 返回验证码字符串
	 */
	public static String generate(int width, int height, int wordLength, OutputStream outputStream) throws IOException {
		return new VcgSimpleImageImpl().generateVerificationCode(width, height, outputStream, wordLength);
	}
	public static String encryptVerifyCode(String code) {
		byte[] encrypted = CryptUtils.aesEncrypt((System.currentTimeMillis() + "#" + code).getBytes(), verifyKey);
		return CryptUtils.toHexString(encrypted);
	}
	public static void checkVerifyCode(String encryptCode, String code) {
		if (!checkVerifyCodeResult(encryptCode, code)) {
			throw new ApiException(99, "验证码错误");
		}
	}
	private static boolean checkVerifyCodeResult(String encryptCode, String code) {
		if (StringUtils.isEmpty(code) || StringUtils.isEmpty(encryptCode))
			return false;
		byte[] raw = CryptUtils.aesDecrypt(CryptUtils.fromHexString(encryptCode), verifyKey);
		if (raw == null)
			return false;
		String content = new String(raw);
		String[] items = content.split("#");
		long t = Long.parseLong(items[0]);
		if (System.currentTimeMillis() - t > 120000) {
			return false;
		}
		if (code.toLowerCase().equals(items[1].toLowerCase()) == false) {
			return false;
		}
		return true;
	}
}
