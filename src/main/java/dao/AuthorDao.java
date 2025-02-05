package dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.swing.JOptionPane;

import entity.AuthorEntity;
import entity.ComboItem;
import service.ConnectDB;

public class AuthorDao {

	// Lấy toàn bộ dữ liệu từ bảng Authors
	public List<AuthorEntity> select() {
		List<AuthorEntity> list = new ArrayList<>();
		try (var con = ConnectDB.getCon();
				var ps = con.prepareStatement("SELECT * FROM Authors WHERE IsDeleted = 0 ORDER BY AuthorID DESC");
				var rs = ps.executeQuery();) {
			while (rs.next()) {
				var author = new AuthorEntity();
				author.setIdAuthorID(rs.getInt("AuthorID"));
				author.setFullName(rs.getString("FullName"));
				author.setNationality(rs.getString("Nationality"));
				author.setDateOfBirth(rs.getDate("DateOfBirth").toLocalDate());
				author.setIsDeleted(rs.getBoolean("IsDeleted"));
				list.add(author);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	// Thêm một tác giả mới vào bảng Authors
	public void insert(AuthorEntity author) {
		try (var con = ConnectDB.getCon();
				var ps = con.prepareStatement(
						"INSERT INTO Authors (FullName, Nationality, DateOfBirth, IsDeleted) VALUES (?, ?, ?, ?)");) {
			ps.setString(1, author.getFullName());
			ps.setString(2, author.getNationality());
			ps.setDate(3, java.sql.Date.valueOf(author.getDateOfBirth()));
			ps.setBoolean(4, author.isIsDeleted());
			ps.executeUpdate();
			JOptionPane.showMessageDialog(null, "Insert success");
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Insert failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	// Xóa một tác giả (sử dụng stored procedure hoặc query trực tiếp)
	public void delete(int authorID, int updatedBy) {
		try (var con = ConnectDB.getCon(); var ps = con.prepareStatement("{call DeleteAuthor(?, ?)}")) { // Truyền thêm
																											// @UpdatedBy
			ps.setInt(1, authorID);
			ps.setInt(2, updatedBy);
			var rowsAffected = ps.executeUpdate();
			if (rowsAffected > 0) {
				JOptionPane.showMessageDialog(null, "Delete success!");
			} else {
				JOptionPane.showMessageDialog(null, "AuthorID not found!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Delete failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}


	// Cập nhật thông tin tác giả
	public void update(AuthorEntity author) {
		try (var con = ConnectDB.getCon();
				var ps = con.prepareStatement(
						"UPDATE Authors SET FullName = ?, Nationality = ?, DateOfBirth = ?, IsDeleted = ? WHERE AuthorID = ?");) {
			ps.setString(1, author.getFullName());
			ps.setString(2, author.getNationality());
			ps.setDate(3, java.sql.Date.valueOf(author.getDateOfBirth()));
			ps.setBoolean(4, author.isIsDeleted());
			ps.setInt(5, author.getIdAuthorID());
			ps.executeUpdate();
			JOptionPane.showMessageDialog(null, "Update success!");
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Update failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	// Lấy danh sách tất cả tên tác giả
	public List<String> getAllAuthorNames() {
		List<String> authorNames = new ArrayList<>();
		try (var con = ConnectDB.getCon();
				var ps = con.prepareStatement("SELECT FullName FROM Authors WHERE IsDeleted = 0");
				var rs = ps.executeQuery();) {
			while (rs.next()) {
				authorNames.add(rs.getString("FullName"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return authorNames;
	}

	// Tìm kiếm một tác giả theo ID
	public AuthorEntity selectById(int authorID) {
		AuthorEntity author = null;
		try (var con = ConnectDB.getCon(); var ps = con.prepareStatement("SELECT * FROM Authors WHERE AuthorID = ?")) {
			ps.setInt(1, authorID);
			try (var rs = ps.executeQuery()) {
				if (rs.next()) {
					author = new AuthorEntity();
					author.setIdAuthorID(rs.getInt("AuthorID"));
					author.setFullName(rs.getString("FullName"));
					author.setNationality(rs.getString("Nationality"));
					author.setDateOfBirth(rs.getDate("DateOfBirth").toLocalDate());
					author.setIsDeleted(rs.getBoolean("IsDeleted"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return author;
	}

	public List<ComboItem> getAllAuthors() {
		List<ComboItem> authors = new ArrayList<>();
		var sql = "SELECT AuthorID, FullName FROM Authors where IsDeleted = 0";
		try (var conn = ConnectDB.getCon();
				var stmt = conn.prepareStatement(sql);
				var rs = stmt.executeQuery()) {
			while (rs.next()) {
				authors.add(new ComboItem(rs.getInt("AuthorID"), rs.getString("FullName")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return authors;
	}

	public List<AuthorEntity> selectDeletedAuthors() {
		List<AuthorEntity> list = new ArrayList<>();
		try (var con = ConnectDB.getCon();
				var ps = con.prepareStatement("""
						SELECT * ,
						CASE (SELECT TOP 1 1 FROM Books b where b.AuthorID = Authors.AuthorID )
						WHEN 1 THEN 1
						ELSE 0 
						END AS 'isSelected'
						FROM Authors 
						WHERE IsDeleted = 1 ORDER BY AuthorID DESC
						""");
				var rs = ps.executeQuery()) {
			while (rs.next()) {
				var pro = new AuthorEntity();
				pro.setFullName(rs.getString("FullName"));
				pro.setNationality(rs.getString("Nationality"));
				pro.setDateOfBirth(rs.getDate("DateOfBirth").toLocalDate());
				pro.setIsDeleted(rs.getBoolean("IsDeleted")); // Lưu trạng thái IsDeleted
				pro.setIdAuthorID(rs.getInt("AuthorID"));
				pro.setSelected(rs.getBoolean("isSelected"));
				list.add(pro);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public Map<Integer, Integer> getBookCountByAuthor() {
		Map<Integer, Integer> bookCounts = new HashMap<>();
		var query = "SELECT AuthorID, COUNT(*) AS BookCount FROM Books WHERE IsDeleted = 0 GROUP BY AuthorID";
		try (var con = ConnectDB.getCon(); var ps = con.prepareStatement(query); var rs = ps.executeQuery()) {
			while (rs.next()) {
				bookCounts.put(rs.getInt("AuthorID"), rs.getInt("BookCount"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bookCounts;
	}
	public Optional<String> getAuthorName(int authorID) {
	    String query = "SELECT FullName FROM Authors WHERE AuthorID = ? AND IsDeleted = 0";
	    try (var con = ConnectDB.getCon(); var ps = con.prepareStatement(query)) {
	        ps.setInt(1, authorID);
	        try (var rs = ps.executeQuery()) {
	            if (rs.next()) {
	                return Optional.of(rs.getString("FullName"));
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return Optional.empty(); // Trả về Optional rỗng nếu không tìm thấy
	}


}