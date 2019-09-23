package cn.tanlw.flashbuy.vo;


import cn.tanlw.flashbuy.domain.FlashbuyUser;

public class GoodsDetailVo {
	private int flashbuyStatus = 0;
	private int remainSeconds = 0;
	private GoodsVo goods ;
	private FlashbuyUser user;
	public int getFlashbuyStatus() {
		return flashbuyStatus;
	}
	public void setFlashbuyStatus(int flashbuyStatus) {
		this.flashbuyStatus = flashbuyStatus;
	}
	public int getRemainSeconds() {
		return remainSeconds;
	}
	public void setRemainSeconds(int remainSeconds) {
		this.remainSeconds = remainSeconds;
	}
	public GoodsVo getGoods() {
		return goods;
	}
	public void setGoods(GoodsVo goods) {
		this.goods = goods;
	}
	public FlashbuyUser getUser() {
		return user;
	}
	public void setUser(FlashbuyUser user) {
		this.user = user;
	}
}
