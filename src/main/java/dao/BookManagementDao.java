package dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import entity.BookManagementEntity;
import service.ConnectDB;

public class BookManagementDao {

	// Lấy toàn bộ dữ liệu từ bảng Books
	public List<BookManagementEntity> select() {
		List<BookManagementEntity> list = new ArrayList<>();
		try (var con = ConnectDB.getCon(); var ps = con.prepareStatement("""
				SELECT * ,
				CASE (SELECT TOP 1 1 FROM BorrowRecords br WHERE br.BookID = Books.BookID)
				WHEN 1 then 1
				ELSE 0
				END AS 'isRental'
				FROM Books WHERE IsDeleted = 0 ORDER BY BookID DESC
				"""); var rs = ps.executeQuery()) {
			while (rs.next()) {
				var book = new BookManagementEntity();
				book.setBookID(rs.getInt("BookID"));
				book.setTitle(rs.getString("Title"));
				book.setAuthorID(rs.getInt("AuthorID"));
				book.setPublisherID(rs.getInt("PublisherID"));
				book.setIsbn(rs.getString("ISBN"));
				book.setCategory(rs.getString("Category"));
				book.setQuantity(rs.getInt("Quantity"));
				book.setStockQuantity(rs.getInt("StockQuantity"));
				book.setPrice(rs.getBigDecimal("Price"));
				book.setRentalPrice(rs.getBigDecimal("RentalPrice"));
				book.setLanguage(rs.getString("Language"));
				book.setImage(rs.getString("Image"));
				book.setDeleted(rs.getBoolean("IsDeleted"));
				book.setDepositPercentage(rs.getBigDecimal("DepositPercentage"));
				book.setFineMultiplier(rs.getBigDecimal("FineMultiplier"));
//				book.setStockIn(rs.getBoolean("StockIn"));
				book.setRental(rs.getBoolean("isRental"));
				list.add(book);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<BookManagementEntity> selectInStock() {
		List<BookManagementEntity> list = new ArrayList<>();
		try (var con = ConnectDB.getCon();
				var ps = con.prepareStatement(
						"SELECT * FROM Books WHERE IsDeleted = 0 and Quantity > 0 ORDER BY BookID DESC");
				var rs = ps.executeQuery()) {
			while (rs.next()) {
				var book = new BookManagementEntity();
				book.setBookID(rs.getInt("BookID"));
				book.setTitle(rs.getString("Title"));
				book.setAuthorID(rs.getInt("AuthorID"));
				book.setPublisherID(rs.getInt("PublisherID"));
				book.setIsbn(rs.getString("ISBN"));
				book.setCategory(rs.getString("Category"));
				book.setQuantity(rs.getInt("Quantity"));
				book.setStockQuantity(rs.getInt("StockQuantity"));
				book.setPrice(rs.getBigDecimal("Price"));
				book.setRentalPrice(rs.getBigDecimal("RentalPrice"));
				book.setLanguage(rs.getString("Language"));
				book.setImage(rs.getString("Image"));
				book.setDeleted(rs.getBoolean("IsDeleted"));
				book.setDepositPercentage(rs.getBigDecimal("DepositPercentage"));
				book.setFineMultiplier(rs.getBigDecimal("FineMultiplier"));

				list.add(book);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	// Thêm một sách mới vào bảng Books
	public void insert(BookManagementEntity book) {
		try (var con = ConnectDB.getCon();
				var ps = con.prepareStatement(
						"INSERT INTO Books (Title, AuthorID, PublisherID, ISBN, Category, Quantity, StockQuantity, Price, RentalPrice, Language, Image, IsDeleted, CreatedAt, CreatedBy, UpdatedAt, UpdatedBy, DepositPercentage, FineMultiplier) "
								+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, GETDATE(), ?, GETDATE(), ?, ?, ?)")) {
			ps.setString(1, book.getTitle());
			ps.setInt(2, book.getAuthorID());
			ps.setInt(3, book.getPublisherID());
			ps.setString(4, book.getIsbn());
			ps.setString(5, book.getCategory());
			ps.setInt(6, book.getQuantity());
			ps.setInt(7, book.getStockQuantity());
			ps.setBigDecimal(8, book.getPrice());
			ps.setBigDecimal(9, book.getRentalPrice());
			ps.setString(10, book.getLanguage());
			ps.setString(11, book.getImage());
			ps.setBoolean(12, book.isDeleted());
			ps.setInt(13, book.getCreatedBy());
			ps.setInt(14, book.getUpdatedBy());
			ps.setBigDecimal(15, book.getDepositPercentage());
			ps.setBigDecimal(16, book.getFineMultiplier());
			ps.executeUpdate();
			JOptionPane.showMessageDialog(null, "Insert success");
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Insert failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	// Xóa một sách dựa trên BookID
	public void delete(int bookID, int updatedBy) throws SQLException {
		var sql = "{CALL DeleteBook(?, ?)}"; // Gọi stored procedure với 2 tham số
		try (var con = ConnectDB.getCon(); var stmt = con.prepareCall(sql)) {
			stmt.setInt(1, bookID); // Tham số @BookID
			stmt.setInt(2, updatedBy); // Tham số @UpdatedBy
			stmt.executeUpdate();
		}
	}

	// Cập nhật sách
	public void update(BookManagementEntity book) {
		try (var con = ConnectDB.getCon();
				var ps = con.prepareStatement(
						"UPDATE Books SET Title = ?, AuthorID = ?, PublisherID = ?, ISBN = ?, Category = ?, Quantity = ?, StockQuantity = ?, Price = ?, RentalPrice = ?, Language = ?, Image = ?, UpdatedAt = GETDATE(), UpdatedBy = ?, DepositPercentage = ?, FineMultiplier = ? "
								+ "WHERE BookID = ? AND IsDeleted = 0")) {
			ps.setString(1, book.getTitle());
			ps.setInt(2, book.getAuthorID());
			ps.setInt(3, book.getPublisherID());
			ps.setString(4, book.getIsbn());
			ps.setString(5, book.getCategory());
			ps.setInt(6, book.getQuantity());
			ps.setInt(7, book.getStockQuantity());
			ps.setBigDecimal(8, book.getPrice());
			ps.setBigDecimal(9, book.getRentalPrice());
			ps.setString(10, book.getLanguage());
			ps.setString(11, book.getImage());
			ps.setInt(12, book.getUpdatedBy());
			ps.setBigDecimal(13, book.getDepositPercentage());
			ps.setBigDecimal(14, book.getFineMultiplier());
			ps.setInt(15, book.getBookID());

			ps.executeUpdate();
			JOptionPane.showMessageDialog(null, "Update success!");
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Update failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	// Kiểm tra trùng ISBN
	public boolean isIsbnExists(String isbn) {
		var query = "SELECT COUNT(*) FROM Books WHERE LOWER(ISBN) = ? AND IsDeleted = 0";
		try (var con = ConnectDB.getCon(); var ps = con.prepareStatement(query)) {
			ps.setString(1, isbn.toLowerCase());
			try (var rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1) > 0; // Trả về true nếu COUNT > 0
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false; // Trả về false nếu có lỗi
	}

	// Lấy sách theo BookID
	public BookManagementEntity selectByBookID(int bookID) {
		BookManagementEntity book = null;
		var query = "SELECT * FROM Books WHERE BookID = ? AND IsDeleted = 0";
		try (var con = ConnectDB.getCon(); var ps = con.prepareStatement(query)) {
			ps.setInt(1, bookID);
			try (var rs = ps.executeQuery()) {
				if (rs.next()) {
					book = new BookManagementEntity();
					book.setBookID(rs.getInt("BookID"));
					book.setTitle(rs.getString("Title"));
					book.setAuthorID(rs.getInt("AuthorID"));
					book.setPublisherID(rs.getInt("PublisherID"));
					book.setIsbn(rs.getString("ISBN"));
					book.setCategory(rs.getString("Category"));
					book.setQuantity(rs.getInt("Quantity"));
					book.setStockQuantity(rs.getInt("StockQuantity"));
					book.setPrice(rs.getBigDecimal("Price"));
					book.setRentalPrice(rs.getBigDecimal("RentalPrice"));
					book.setLanguage(rs.getString("Language"));
					book.setImage(rs.getString("Image"));
					book.setDeleted(rs.getBoolean("IsDeleted"));
					book.setFineMultiplier(rs.getBigDecimal("FineMultiplier"));

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return book;
	}

	public String getAuthorNameById(int authorId) {
		var authorName = "Unknown"; // Giá trị mặc định nếu không tìm thấy
		var query = "SELECT AuthorName FROM Authors WHERE AuthorID = ?";
		try (var con = ConnectDB.getCon(); var ps = con.prepareStatement(query)) {
			ps.setInt(1, authorId);
			try (var rs = ps.executeQuery()) {
				if (rs.next()) {
					authorName = rs.getString("AuthorName");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return authorName;
	}

	public int getLastInsertedBookId() {
		var sql = "SELECT MAX(BookID) AS LastID FROM Books"; // Giả sử BookID tự tăng
		try (var con = ConnectDB.getCon(); var stmt = con.prepareStatement(sql); var rs = stmt.executeQuery()) {

			if (rs.next()) {
				return rs.getInt("LastID");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1; // Trả về -1 nếu có lỗi
	}

	public void insertBookCategory(int bookId, int categoryId) {
		var sql = "INSERT INTO BookCategories (BookID, CategoryID) VALUES (?, ?)";

		try (var con = ConnectDB.getCon(); var stmt = con.prepareStatement(sql)) {

			stmt.setInt(1, bookId);
			stmt.setInt(2, categoryId);
			stmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public BookManagementEntity getBookWithDetails(String isbn) {
		BookManagementEntity book = null;

		var sql = """
				    SELECT top 1 b.*,
				           a.FullName AS AuthorName,
				           p.Name AS PublisherName
				    FROM Books b
				    LEFT JOIN Authors a ON b.AuthorID = a.AuthorID
				    LEFT JOIN Publishers p ON b.PublisherID = p.PublisherID
				    WHERE b.ISBN = ?;
				""";

		try (var conn = ConnectDB.getCon(); var stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, isbn);
			try (var rs = stmt.executeQuery()) {
				if (rs.next()) {
					book = new BookManagementEntity();
					book.setBookID(rs.getInt("BookID"));
					book.setIsbn(rs.getString("ISBN"));
					book.setTitle(rs.getString("Title"));
					book.setAuthorID(rs.getInt("AuthorID"));
					book.setPublisherID(rs.getInt("PublisherID"));
					book.setCategory(rs.getString("Category"));
					book.setQuantity(rs.getInt("Quantity"));
					book.setStockQuantity(rs.getInt("StockQuantity"));
					book.setPrice(rs.getBigDecimal("Price"));
					book.setRentalPrice(rs.getBigDecimal("RentalPrice"));
					book.setLanguage(rs.getString("Language"));
					book.setImage(rs.getString("Image"));
					book.setDepositPercentage(rs.getBigDecimal("DepositPercentage"));
					book.setFineMultiplier(rs.getBigDecimal("FineMultiplier"));

					// Lấy tên tác giả và nhà xuất bản từ truy vấn
					book.setAuthorName(rs.getString("AuthorName"));
					book.setPublisherName(rs.getString("PublisherName"));

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return book;
	}

	public BookManagementEntity getBookWithDetails(int bookid) {
		BookManagementEntity book = null;

		var sql = """
				    SELECT top 1 b.*,
				           a.FullName AS AuthorName,
				           p.Name AS PublisherName
				    FROM Books b
				    LEFT JOIN Authors a ON b.AuthorID = a.AuthorID
				    LEFT JOIN Publishers p ON b.PublisherID = p.PublisherID
				    WHERE b.BookID = ?;
				""";

		try (var conn = ConnectDB.getCon(); var stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, bookid);
			try (var rs = stmt.executeQuery()) {
				if (rs.next()) {
					book = new BookManagementEntity();
					book.setBookID(rs.getInt("BookID"));
					book.setIsbn(rs.getString("ISBN"));
					book.setTitle(rs.getString("Title"));
					book.setAuthorID(rs.getInt("AuthorID"));
					book.setPublisherID(rs.getInt("PublisherID"));
					book.setCategory(rs.getString("Category"));
					book.setQuantity(rs.getInt("Quantity"));
					book.setStockQuantity(rs.getInt("StockQuantity"));
					book.setPrice(rs.getBigDecimal("Price"));
					book.setRentalPrice(rs.getBigDecimal("RentalPrice"));
					book.setLanguage(rs.getString("Language"));
					book.setImage(rs.getString("Image"));
					book.setDepositPercentage(rs.getBigDecimal("DepositPercentage"));
					book.setFineMultiplier(rs.getBigDecimal("FineMultiplier"));

					// Lấy tên tác giả và nhà xuất bản từ truy vấn
					book.setAuthorName(rs.getString("AuthorName"));
					book.setPublisherName(rs.getString("PublisherName"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return book;
	}

	public BookManagementEntity selectByIsbn(String isbn) {
		try (var conn = ConnectDB.getCon(); var stmt = conn.prepareStatement("SELECT * FROM Books WHERE isbn = ?")) {
			stmt.setString(1, isbn);

			try (var rs = stmt.executeQuery()) {
				if (rs.next()) {
					var book = new BookManagementEntity();
					book.setBookID(rs.getInt("BookID"));
					book.setIsbn(rs.getString("ISBN"));
					// Gán các trường khác từ cơ sở dữ liệu...
					return book;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public boolean updateBookQuantity(String isbn, int newQuantity, int newStockQuantity) {
		var sql = "UPDATE books SET quantity = ?, StockQuantity = ?, StockIn = 0  WHERE isbn = ?"; // Điều chỉnh tên
																									// bảng và cột nếu
																									// cần

		try (var conn = ConnectDB.getCon(); // Giả sử bạn đã có phương thức để lấy kết nối
				var statement = conn.prepareStatement(sql)) {

			statement.setInt(1, newQuantity); // Gán giá trị số lượng mới
			statement.setInt(2, newStockQuantity); // Gán giá trị số lượng mới
			statement.setString(3, isbn); // Gán giá trị ISBN
			var rowsAffected = statement.executeUpdate(); // Thực thi lệnh UPDATE

			return rowsAffected > 0; // Trả về true nếu cập nhật thành công
		} catch (Exception e) {
			e.printStackTrace();
			return false; // Trả về false nếu có lỗi
		}
	}

	public boolean updateStockIn(String isbn, Boolean isStockin) {
		var sql = "UPDATE books SET StockIn = ?  WHERE isbn = ?"; // Điều chỉnh tên bảng và cột nếu cần

		try (var conn = ConnectDB.getCon(); // Giả sử bạn đã có phương thức để lấy kết nối
				var statement = conn.prepareStatement(sql)) {

			statement.setBoolean(1, isStockin); // Gán giá trị số lượng mới
			statement.setString(2, isbn); // Gán giá trị ISBN
			var rowsAffected = statement.executeUpdate(); // Thực thi lệnh UPDATE

			return rowsAffected > 0; // Trả về true nếu cập nhật thành công
		} catch (Exception e) {
			e.printStackTrace();
			return false; // Trả về false nếu có lỗi
		}
	}

	public boolean updateBookStockRetal(int bookid, int newStockQuantity, int newQuantity) {
		var sql = "UPDATE books SET StockQuantity = ?, quantity = ? WHERE BookID = ?";
		try (var conn = ConnectDB.getCon(); // Giả sử bạn đã có phương thức để lấy kết nối
				var statement = conn.prepareStatement(sql)) {

			statement.setInt(1, newStockQuantity); // Gán giá trị số lượng mới
			statement.setInt(2, newQuantity); // Gán giá trị số lượng mới
			statement.setInt(3, bookid); // Gán giá trị ISBN
			var rowsAffected = statement.executeUpdate(); // Thực thi lệnh UPDATE

			return rowsAffected > 0; // Trả về true nếu cập nhật thành công
		} catch (Exception e) {
			e.printStackTrace();
			return false; // Trả về false nếu có lỗi
		}
	}

	public List<BookManagementEntity> selectDeleteBook() {
		List<BookManagementEntity> list = new ArrayList<>();
		try (var con = ConnectDB.getCon();
				var ps = con.prepareStatement("SELECT * FROM Books WHERE IsDeleted = 1 ORDER BY BookID DESC");
				var rs = ps.executeQuery()) {
			while (rs.next()) {
				var book = new BookManagementEntity();
				book.setBookID(rs.getInt("BookID"));
				book.setImage(rs.getString("Image"));
				book.setTitle(rs.getString("Title"));
				book.setAuthorID(rs.getInt("AuthorID"));
				book.setPublisherID(rs.getInt("PublisherID"));
				book.setIsbn(rs.getString("ISBN"));
				book.setQuantity(rs.getInt("Quantity"));
				book.setStockQuantity(rs.getInt("StockQuantity"));
				book.setPrice(rs.getBigDecimal("Price"));
				book.setLanguage(rs.getString("Language"));
				book.setDepositPercentage(rs.getBigDecimal("DepositPercentage"));
				book.setFineMultiplier(rs.getBigDecimal("FineMultiplier"));

				list.add(book);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public int countBooksByUserId(int userId) {
		var count = 0;
		var query = "SELECT COUNT(*) FROM Books WHERE CreatedBy = ? AND IsDeleted = 0";
		try (var con = ConnectDB.getCon(); var ps = con.prepareStatement(query)) {
			ps.setInt(1, userId);
			try (var rs = ps.executeQuery()) {
				if (rs.next()) {
					count = rs.getInt(1);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

}
