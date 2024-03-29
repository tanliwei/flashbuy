package cn.tanlw.flashbuy.vo;


import cn.tanlw.flashbuy.domain.Goods;

import java.util.Date;

public class GoodsVo extends Goods {
	private Double flashbuyPrice;
	private Integer stockCount;
	private Date startDate;
	private Date endDate;
	public Integer getStockCount() {
		return stockCount;
	}
	public void setStockCount(Integer stockCount) {
		this.stockCount = stockCount;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Double getFlashbuyPrice() {
		return flashbuyPrice;
	}
	public void setFlashbuyPrice(Double flashbuyPrice) {
		this.flashbuyPrice = flashbuyPrice;
	}
}
