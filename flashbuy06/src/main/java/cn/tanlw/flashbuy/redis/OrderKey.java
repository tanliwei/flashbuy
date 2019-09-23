package cn.tanlw.flashbuy.redis;

public class OrderKey extends BasePrefix {

	public OrderKey(String prefix) {
		super(prefix);
	}
	public static OrderKey getFlashbuyOrderByUidGid = new OrderKey("oug");
}
