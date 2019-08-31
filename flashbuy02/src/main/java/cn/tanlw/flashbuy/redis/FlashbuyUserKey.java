package cn.tanlw.flashbuy.redis;

public class FlashbuyUserKey extends BasePrefix {
    public static final int TOKEN_EXPIRE = 3600*24 * 2;
    private FlashbuyUserKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }
    public static FlashbuyUserKey TOKEN = new FlashbuyUserKey(TOKEN_EXPIRE, "tk");
}
