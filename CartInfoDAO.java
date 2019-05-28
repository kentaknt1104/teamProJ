package com.internousdev.leo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.internousdev.leo.dto.CartInfoDTO;
import com.internousdev.leo.util.DBConnector;

public class CartInfoDAO {

	public List<CartInfoDTO> selectCartInfoList (String userId) {
//		ユーザー欄の商品すべてのデータを表示
//		商品テーブル カートテーブルを結合後DTOに代入
		DBConnector db = new DBConnector();
		Connection con = db.getConnection();
		List<CartInfoDTO> cartInfoDTOList = new ArrayList<CartInfoDTO>();

		String sql = "SELECT "
				+ "ci.id, "
				+ "ci.user_id, "
				+ "ci.product_id, "
				+ "ci.product_count, "
				+ "pi.price, "
				+ "pi.product_name, "
				+ "pi.product_name_kana, "
				+ "pi.image_file_path, "
				+ "pi.image_file_name, "
				+ "pi.release_date, "
				+ "pi.release_company, "
				+ "(ci.price * ci.product_count) as sum_price, "
				+ "pi.status, "
				+ "ci.regist_date, "
				+ "ci.update_date "
				+ "FROM cart_info ci "
				+ "LEFT JOIN product_info pi "
				+ "ON ci.product_id = pi.product_id "
				+ "WHERE user_id = ? "
				+ "ORDER BY update_date DESC, regist_date DESC";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, userId);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				CartInfoDTO cartDTO = new CartInfoDTO();

				cartDTO.setId(rs.getInt("id"));
				cartDTO.setUserId(rs.getString("user_id"));
				cartDTO.setProductId(rs.getInt("product_id"));
				cartDTO.setProductCount(rs.getInt("product_count"));
				cartDTO.setPrice(rs.getInt("price"));
				cartDTO.setProductName(rs.getString("product_name"));
				cartDTO.setProductNameKana(rs.getString("product_name_kana"));
				cartDTO.setImageFilePath(rs.getString("image_file_path"));
				cartDTO.setImageFileName(rs.getString("image_file_name"));
				cartDTO.setReleaseDate(rs.getDate("release_date"));
				cartDTO.setReleaseCompany(rs.getString("release_company"));
				cartDTO.setItemSumPrice(rs.getInt("sum_price"));
				cartDTO.setStatus(rs.getString("status"));

				cartInfoDTOList.add(cartDTO);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return cartInfoDTOList;
	}

	public int totalPrice (String userId) throws ArithmeticException{
//		カートの全商品合計の金額を表示
		DBConnector db = new DBConnector();
		Connection con = db.getConnection();

		int totalPrice = 0;

//		個数＊価格を合計し、検索IDの欄のもののみを取り出す。
		String sql = "SELECT SUM(product_count * price) AS total_price "
				+ "FROM cart_info WHERE user_id = ? "
				+ "GROUP BY user_id";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, userId);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				totalPrice = rs.getInt("total_price");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return totalPrice;
	}

	public int getPrice (String userId, int productId) throws ArithmeticException{
//		カートの全商品合計の金額を表示
		DBConnector db = new DBConnector();
		Connection con = db.getConnection();

		int price = 0;

//		検索IDの欄のもののみを取り出す。
		String sql = "product_count * price As subtotal"
				+ "FROM cart_info WHERE user_id = ? AND product_id = ?";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, userId);
			ps.setInt(2, productId);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				price = rs.getInt("subtotal");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return price;
	}

	public int insertCartInfo (String userId, int productId, int productCount, int price) {
//		新規登録の際に使用。カートテーブルに情報の格納を行う。
		DBConnector db = new DBConnector();
		Connection con = db.getConnection();
		int count = 0;

//		引数で与えられたものと、now関数を用いる。
		String sql = "INSERT "
				+ "INTO cart_info (user_id, product_id, product_count, price, regist_date, update_date) "
				+ "VALUES (?, ?, ?, ?, now(), now())";
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, userId);
			ps.setInt(2, productId);
			ps.setInt(3, productCount);
			ps.setInt(4, price);

			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
			con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return count;
	}

	public int updateCartInfo (String userId, int productId, int productCount) {
//		上書登録の際に使用。個数のカラム、更新日を上書する。
		DBConnector db = new DBConnector();
		Connection con = db.getConnection();
		int count = 0;

		String sql = "UPDATE cart_info "
				+ "SET product_count = (product_count + ?), update_date = now() "
				+ "WHERE user_id = ? AND product_id = ?";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, productCount);
			ps.setString(2, userId);
			ps.setInt(3, productId);

			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return count;
	}

	public int deleteCartInfo (String productId, String userId) {
//		カートにある商品のうち、チェックのついた商品を削除する。
		DBConnector db = new DBConnector();
		Connection con = db.getConnection();
		int delete = 0;

//		引数の商品IDでチェックのあるなしを判定。
		String sql = "DELETE FROM cart_info WHERE user_id = ? AND product_id = ?";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, userId);
			ps.setString(2, productId);

			delete = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
			con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return delete;
	}

	public int deleteAllCartInfo (String userId) {
//		カートにある商品の全件削除
		DBConnector db = new DBConnector();
		Connection con = db.getConnection();
		int delete = 0;

		String sql = "DELETE FROM cart_info WHERE user_id = ?";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, userId);

			delete = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return delete;
	}

	public boolean isExistsInfo (String userId, int productId) {
//		cartのなかに情報が入っているか確認するメソッド
//		個数上書・新規登録の判断の際に使う
		DBConnector db = new DBConnector();
		Connection con = db.getConnection();
		boolean result = false;

		String sql = "SELECT COUNT(id) AS count FROM cart_info WHERE user_id = ? AND product_id = ?";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, userId);
			ps.setInt(2, productId);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				if (rs.getInt("count") > 0) {
					result = true;
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
			con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public int updateCartInfoAfterLogined (String userId, String tempUserId, int productId) {
//		tempId を UserIdに変換してカートテーブルに代入する
		DBConnector db = new DBConnector();
		Connection con = db.getConnection();
		int count = 0;

//		tempユーザーIDをユーザーIDにして、登録日の変更も行う。
		String sql = "UPDATE cart_info "
				+ "SET user_id = ?, update_date = now() "
				+ "WHERE user_id = ? AND product_id = ?";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1,  userId);
			ps.setString(2, tempUserId);
			ps.setInt(3, productId);

			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return count;
	}

}
