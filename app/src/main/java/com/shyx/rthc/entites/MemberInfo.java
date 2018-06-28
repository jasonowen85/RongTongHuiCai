package com.shyx.rthc.entites;

/**
 * 账号基本信息
 * @author weiyunchao
 *
 */
public class MemberInfo {
	
	private String name;//用户名
	private String photo;//头像图标路径
	private double totalIncome;//总收益
	private double totalAssets;//总资产
	private double balance;//可用余额
	private boolean hasUserUnreadMsg;//是否有未读消息

	public boolean isHasUserUnreadMsg() {
		return hasUserUnreadMsg;
	}

	public void setHasUserUnreadMsg(boolean hasUserUnreadMsg) {
		this.hasUserUnreadMsg = hasUserUnreadMsg;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public double getTotalIncome() {
		return totalIncome;
	}

	public void setTotalIncome(double totalIncome) {
		this.totalIncome = totalIncome;
	}

	public double getTotalAssets() {
		return totalAssets;
	}

	public void setTotalAssets(double totalAssets) {
		this.totalAssets = totalAssets;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}
}
