package com.internousdev.leo.action;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.internousdev.leo.dao.CartInfoDAO;
import com.internousdev.leo.dto.CartInfoDTO;
import com.opensymphony.xwork2.ActionSupport;

public class CartAction extends ActionSupport implements SessionAware {

	private Map<String, Object> session;
	private List<CartInfoDTO> cartInfoDTOList;

	private String userId = null;
	private int totalPrice = 0;

	public String execute() {

//		セッションタイムアウトのチェック
		if (session.isEmpty()) {
			return "sessionTimeout";
		}

//		どちらのIDかのチェック
		if (session.containsKey("userId")) {
			userId = String.valueOf(session.get("userId"));
		} else {
			userId = String.valueOf(session.get("tempUserId"));
		}

//		カートの情報があればDTOにいれてjspに渡す。
//		なければ無しのほうの分岐にはいる。
		CartInfoDAO cartInfoDAO = new CartInfoDAO();
		cartInfoDTOList = cartInfoDAO.selectCartInfoList(userId);
		totalPrice = cartInfoDAO.totalPrice(userId);

		return SUCCESS;
	}

	public Map<String, Object> getSession() {
		return session;
	}

	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public List<CartInfoDTO> getCartInfoDTOList() {
		return cartInfoDTOList;
	}

	public void setCartInfoDTOList(List<CartInfoDTO> cartInfoDTOList) {
		this.cartInfoDTOList = cartInfoDTOList;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}
}
