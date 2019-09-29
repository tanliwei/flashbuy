package cn.tanlw.flashbuy.redis;

/**
 * @Creator Tan Liwei
 * @Date 2019/9/25 20:46
 */
public class FlashbuyKey extends BasePrefix {
    public FlashbuyKey(String prefix) {
        super(prefix);
    }

    public static FlashbuyKey goodsFlashbuyOver = new FlashbuyKey("goodsFlashbuyOver");
}
