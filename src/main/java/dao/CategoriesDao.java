package dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.swing.JOptionPane;

import entity.CategoriesEntity;
import entity.ComboItem;
import service.ConnectDB;

public class CategoriesDao {

	// Lấy toàn bộ dữ liệu từ bảng Categories
	public List<CategoriesEntity> select() {
		List<CategoriesEntity> list = new ArrayList<>();
		try (var con = ConnectDB.getCon();
				var ps = con.prepareStatement("SELECT * FROM Categories WHERE IsDeleted = 0 ORDER BY CategoryID DESC");
				var rs = ps.executeQuery()) {
			while (rs.next()) {
				var category = new CategoriesEntity();
				category.setCategoryID(rs.getInt("CategoryID"));
				category.setCategoryName(rs.getString("CategoryName"));
				category.setDescription(rs.getString("Description"));
				category.setIsDeleted(rs.getBoolean("IsDeleted"));
				list.add(category);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	// Thêm một danh mục mới vào bảng Categories
	public void insert(CategoriesEntity category) {
		try (var con = ConnectDB.getCon();
				var ps = con.prepareStatement(
						"INSERT INTO Categories (CategoryName, Description, IsDeleted) VALUES (?, ?, ?)");) {
			ps.setString(1, category.getCategoryName());
			ps.setString(2, category.getDescription());
			ps.setBoolean(3, category.isIsDeleted());
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Cập nhật thông tin danh mục
	public void update(CategoriesEntity category) {
		try (var con = ConnectDB.getCon();
				var ps = con.prepareStatement(
						"UPDATE Categories SET CategoryName = ?, Description = ?, IsDeleted = ? WHERE CategoryID = ?");) {
			ps.setString(1, category.getCategoryName());
			ps.setString(2, category.getDescription());
			ps.setBoolean(3, category.isIsDeleted());
			ps.setInt(4, category.getCategoryID());
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Xóa logic một danh mục (đặt IsDeleted = 1)
	public void delete(int categoryID, int updatedBy) {
		try (var con = ConnectDB.getCon(); var ps = con.prepareStatement("{call DeleteCategory(?, ?)}")) { // Gọi stored
																											// procedure
																											// DeleteCategory
			ps.setInt(1, categoryID);
			ps.setInt(2, updatedBy);
			var rowsAffected = ps.executeUpdate();
			if (rowsAffected > 0) {
				JOptionPane.showMessageDialog(null, "Category deleted successfully!");
			} else {
				JOptionPane.showMessageDialog(null, "CategoryID not found!", "Warning", JOptionPane.WARNING_MESSAGE);
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Delete failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}


	// Lấy thông tin chi tiết một danh mục theo ID
	public CategoriesEntity selectById(int categoryID) {
		CategoriesEntity category = null;
		try (var con = ConnectDB.getCon();
				var ps = con.prepareStatement("SELECT * FROM Categories WHERE CategoryID = ? AND IsDeleted = 0")) {
			ps.setInt(1, categoryID);
			try (var rs = ps.executeQuery()) {
				if (rs.next()) {
					category = new CategoriesEntity();
					category.setCategoryID(rs.getInt("CategoryID"));
					category.setCategoryName(rs.getString("CategoryName"));
					category.setDescription(rs.getString("Description"));
					category.setIsDeleted(rs.getBoolean("IsDeleted"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return category;
	}

	// Lấy danh sách tên tất cả danh mục
	public List<String> getAllCategoryNames() {
		List<String> categoryNames = new ArrayList<>();
		try (var con = ConnectDB.getCon();
				var ps = con.prepareStatement("SELECT CategoryName FROM Categories WHERE IsDeleted = 0");
				var rs = ps.executeQuery()) {
			while (rs.next()) {
				categoryNames.add(rs.getString("CategoryName"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return categoryNames;
	}

	public List<ComboItem> getAllCategories() {
		List<ComboItem> categories = new ArrayList<>();

		var sql = "SELECT CategoryID, CategoryName FROM Categories where IsDeleted = 0"; // Truy vấn để lấy dữ liệu
		try (var con = ConnectDB.getCon(); // Mở kết nối
				var stmt = con.prepareStatement(sql);
				var rs = stmt.executeQuery()) {

			while (rs.next()) {
				var id = rs.getInt("CategoryID");
				var name = rs.getString("CategoryName");
				categories.add(new ComboItem(id, name)); // Thêm vào danh sách
			}

		} catch (Exception e) {
			e.printStackTrace(); // Log lỗi nếu xảy ra
		}

		return categories; // Trả về danh sách các danh mục
	}

	public List<CategoriesEntity> selectDeletedCategories() {
		List<CategoriesEntity> list = new ArrayList<>();
		try (var con = ConnectDB.getCon();
				var ps = con.prepareStatement("""
						SELECT * ,
						CASE (SELECT TOP 1 1 FROM BookCategories b where b.CategoryID = Categories.CategoryID )
						WHEN 1 THEN 1
						ELSE 0 
						END AS 'isSelected'
						FROM Categories WHERE IsDeleted = 1 ORDER BY CategoryID DESC
						""");
				var rs = ps.executeQuery()) {
			while (rs.next()) {
				var category = new CategoriesEntity();
				category.setCategoryID(rs.getInt("CategoryID"));
				category.setCategoryName(rs.getString("CategoryName"));
				category.setDescription(rs.getString("Description"));
				category.setIsDeleted(rs.getBoolean("IsDeleted")); // Lưu trạng thái IsDeleted
				category.setSelected(rs.getBoolean("isSelected"));
				list.add(category);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	// Lấy số lượng sách liên kết với từng danh mục
	public Map<Integer, Integer> getBookCountByCategory() {
		var bookCounts = new HashMap<Integer, Integer>();

		var sql = "SELECT CategoryID, COUNT(*) AS BookCount FROM BookCategories GROUP BY CategoryID";
		try (var con = ConnectDB.getCon(); var ps = con.prepareStatement(sql); var rs = ps.executeQuery()) {

			while (rs.next()) {
				bookCounts.put(rs.getInt("CategoryID"), rs.getInt("BookCount"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return bookCounts; // Trả về danh sách số lượng sách của từng danh mục
	}
	public Optional<String> getCategoryName(int categoryID) {
	    String query = "SELECT CategoryName FROM Categories WHERE CategoryID = ? AND IsDeleted = 0";
	    try (var con = ConnectDB.getCon(); var ps = con.prepareStatement(query)) {
	        ps.setInt(1, categoryID);
	        try (var rs = ps.executeQuery()) {
	            if (rs.next()) {
	                return Optional.of(rs.getString("CategoryName"));
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return Optional.empty(); // Trả về Optional rỗng nếu không tìm thấy
	}

	public void deleteBookCategory(int bookid) {
		// Chuẩn bị cập nhật BookCategories
		try (var con = ConnectDB.getCon()) {
		    // Xóa các danh mục cũ liên kết với BookID hiện tại
		    try (var psDelete = con.prepareStatement("DELETE FROM BookCategories WHERE BookID = ?")) {
		        psDelete.setInt(1, bookid);
		        psDelete.executeUpdate();
		    }

		   
		} catch (Exception e) {
		    e.printStackTrace();
		    JOptionPane.showMessageDialog(null, "Failed to update book categories: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}
