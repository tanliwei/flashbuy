package cn.tanlw.flashbuy.rabbitmq;


import cn.tanlw.flashbuy.domain.FlashbuyUser;

public class FlashbuyMessage {
	private FlashbuyUser user;
	private long goodsId;
	public FlashbuyUser getUser() {
		return user;
	}
	public void setUser(FlashbuyUser user) {
		this.user = user;
	}
	public long getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(long goodsId) {
		this.goodsId = goodsId;
	}
}
