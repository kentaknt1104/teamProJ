package com.internousdev.leo.action;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.internousdev.leo.dao.CartInfoDAO;

//	カートテーブルから情報を持ち出すよう

import com.internousdev.leo.dao.ProductInfoDAO;
import com.internousdev.leo.dto.CartInfoDTO;
import com.internousdev.leo.dto.ProductInfoDTO;
import com.opensymphony.xwork2.ActionSupport;

public class AddCartAction extends ActionSupport implements SessionAware{

	private Map<String, Object> session;
	private List<CartInfoDTO> cartInfoDTOList;//	getter/setterを作りたいためここにフィールド設定

	private int productId;//	商品IDの代入用
	private int productCount;//	個数の代入用
	private int totalPrice;//	すべての商品の合計金額の代入用

	public String execute() {

//		sessionタイムアウトのチェック
		if (session.isEmpty()) {
			return "sessionTimeout";
		}

		String result = ERROR;

		// 購入個数が1～5個以外を選択された場合はエラー
		if (productCount < 1 || productCount > 5) {
			return result;
		}


		String userId = null;//	ユーザーID or 仮IDの代入用

//		ＩＤが仮か判定しユーザーID に代入（＝データ挿入時に分岐させなくてすむようにするため）
		if (session.containsKey("userId")) { 									//-->ユーザーＩＤのsession名
			userId = String.valueOf(session.get("userId"));
		} else {
			userId = String.valueOf(session.get("tempUserId"));					//-->仮ＩＤのsession名
		}


		int count = 0;															//countの値の大きさで成功か失敗か判定
		CartInfoDAO cartInfoDAO = new CartInfoDAO();

		ProductInfoDAO productInfoDAO = new ProductInfoDAO();
		ProductInfoDTO productDTO = productInfoDAO.getProductInfoByProductId(productId);
		int tempsumPrice = Math.multiplyExact(productCount, productDTO.getPrice());			    //int型のチェック用の一時的なもの


//		[int型の限界チェック] 追加前のカート合計金額と追加商品の購入金額の総額が限界を超える際,追加せずにエラー
		try {
//			現状のカート合計金額＋追加商品の購入金額
			Math.addExact(cartInfoDAO.totalPrice(userId), tempsumPrice);
		} catch(ArithmeticException e) {
			return result;
		}


//		カートテーブルにすでに値があるかないか
		if (cartInfoDAO.isExistsInfo(userId, productId)) {
//			あるとき（＝個数の上書に分岐）
			try {
//				[int型の限界チェック] 購入商品の合計金額が限界を超える際に,追加せずエラー
				int subtotal = cartInfoDAO.getPrice (userId, productId);
//				追加前の商品Aの合計金額＋追加商品Aの購入金額
				Math.addExact(subtotal, tempsumPrice);
			} catch (ArithmeticException e) {
				return result;
			}

			count = cartInfoDAO.updateCartInfo(userId, productId, productCount);
		} else {
//			無いとき（＝新規登録に分岐）

			count = cartInfoDAO.insertCartInfo(userId, productId, productCount, productDTO.getPrice());
		}

//		cartInfoDAO にデータが入ったか調べる。

//		count > 0 ==> 個数の上書 or 新規登録が行われ、（int型の許容値内なので）戻り値がint型で帰ってきている。
//		count = 0 のときはカートに追加を押しても追加されない ==> システム的なエラー or int型のオーバーフローのためエラー画面へ

		if (count > 0) {
//			daoからカートデータの取り出し、DTOListに代入してjspへ渡す
			cartInfoDTOList = cartInfoDAO.selectCartInfoList(userId);
			totalPrice = cartInfoDAO.totalPrice(userId);

			result = SUCCESS;

		}

		return result;
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

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public int getProductCount() {
		return productCount;
	}

	public void setProductCount(int productCount) {
		this.productCount = productCount;
	}

	public int getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}

}

