package cn.tanlw.flashbuy.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidatorUtil {
	
	private static final Pattern PHONE_PATTERN = Pattern.compile("1\\d{10}");
	
	public static boolean isPhone(String src) {
		if(StringUtils.isEmpty(src)) {
			return false;
		}
		Matcher m = PHONE_PATTERN.matcher(src);
		return m.matches();
	}
	
//	public static void main(String[] args) {
//			System.out.println(isPhone("18912341234"));
//			System.out.println(isPhone("1891234123"));
//	}
}
