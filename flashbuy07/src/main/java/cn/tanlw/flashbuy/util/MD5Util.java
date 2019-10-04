package cn.tanlw.flashbuy.util;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {
	
	public static String md5(String src) {
		return DigestUtils.md5Hex(src);
	}
	
	private static final String salt = "1234$#@!";
	
	public static String inputPassToFormPass(String inputPass) {
		String str = ""+salt.charAt(0)+salt.charAt(2) + inputPass +salt.charAt(5) + salt.charAt(4);
		System.out.println("inputPass 2:" + str);
		return md5(str);
	}
	
	public static String formPassToDBPass(String formPass, String salt) {
		String str = ""+salt.charAt(0)+salt.charAt(2) + formPass +salt.charAt(5) + salt.charAt(4);
		return md5(str);
	}
	
	public static String inputPassToDbPass(String inputPass, String saltDB) {
		String formPass = inputPassToFormPass(inputPass);
		String dbPass = formPassToDBPass(formPass, saltDB);
		return dbPass;
	}
	
	public static void main(String[] args) {
		System.out.println(inputPassToFormPass("123456"));//7da175eb9aa32e56a884eb2161111e20
		System.out.println(formPassToDBPass(inputPassToFormPass("123456"), "QWERfdsa"));
		System.out.println(inputPassToDbPass("123456", "QWERfdsa"));//1ce5dd59898565d64eaff30baaaa792a
	}
	
}
