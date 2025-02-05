package dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entity.BorrowRecordsEntity;
import entity.UserSession;
import service.ConnectDB;

public class BorrowRecordsDao {
	private final Connection connection;
	private UserSession userSession;
	public BorrowRecordsDao(Connection connection) {
		this.connection = connection;
		userSession = UserSession.getInstance();
	}

	// Fetch all borrow records that are not deleted
	public List<BorrowRecordsEntity> select() {
		List<BorrowRecordsEntity> records = new ArrayList<>();
		var query = "SELECT * FROM BorrowRecords WHERE IsDeleted = 0";

		try (var statement = connection.prepareStatement(query); var resultSet = statement.executeQuery()) {

			while (resultSet.next()) {
				var record = mapToEntity(resultSet);
				records.add(record);
			}
		} catch (SQLException e) {
			System.err.println("Error retrieving borrow records: " + e.getMessage());
			e.printStackTrace();
		}
		return records;
	}

	public List<BorrowRecordsEntity> selectBrrow() {
		List<BorrowRecordsEntity> records = new ArrayList<>();
		var query = """
				    SELECT br.*, p.StudentID, s.StudentCode, s.FullName, s.PhoneNumber, s.Email, u.UserName, u.Email as U_EMAIL,
				    b.Title AS BookName, b.Image as BookImage, b.ISBN
				    FROM BorrowRecords br
				    JOIN Payments p ON br.PaymentID = p.PaymentID
				    JOIN Users u ON br.UserID = u.UserID
				    JOIN Books b ON br.BookID = b.BookID
				    JOIN Students s ON s.StudentID = p.StudentID
				    WHERE br.IsDeleted = 0 AND br.Status = 'Borrowed' and b.IsDeleted = 0
				    ORDER BY br.UpdatedAt DESC
				""";

		try (var statement = connection.prepareStatement(query); var resultSet = statement.executeQuery()) {

			while (resultSet.next()) {
				var record = mapToEntity(resultSet);
				record = mapToOtherEntity(resultSet, record);
				records.add(record);
			}
		} catch (SQLException e) {
			System.err.println("Error retrieving borrow records: " + e.getMessage());
			e.printStackTrace();
		}
		return records;
	}

	
	public List<Map<String, Object>> selectBrrowByPaymentID(int paymentId) {
	    List<Map<String, Object>> records = new ArrayList<>();
		var query = """
				    SELECT br.*, p.StudentID, s.StudentCode, s.FullName, s.PhoneNumber, s.Email, u.UserName, u.Email as U_EMAIL,
				    b.Title AS BookName, b.Image as BookImage, b.ISBN, T1.FullName as AuthorName, T2.Name as PublidherName, 
				    b.Category, b.DepositPercentage, b.FineMultiplier, b.Price,
				    ((br.Quantity * br.BorrowPrice) + (br.Quantity * b.Price * b.DepositPercentage / 100)) AS Total
				    FROM BorrowRecords br
				    JOIN Payments p ON br.PaymentID = p.PaymentID
				    JOIN Users u ON br.UserID = u.UserID
				    JOIN Books b ON br.BookID = b.BookID
				    JOIN Students s ON s.StudentID = p.StudentID
					OUTER APPLY (SELECT T1.FullName 
								 FROM Authors T1 
								 WHERE b.AuthorID = T1.AuthorID) T1
					OUTER APPLY (SELECT T2.Name 
								 FROM Publishers T2 
								 WHERE b.PublisherID = T2.PublisherID) T2
				    WHERE br.IsDeleted = 0 AND b.IsDeleted = 0 and p.PaymentID = ?
				    ORDER BY br.RecordID
				""";
		try (var statement = connection.prepareStatement(query)) {
	        statement.setInt(1, paymentId);
	        try (var resultSet = statement.executeQuery()) {

			while (resultSet.next()) {
                Map<String, Object> record = new HashMap<>();
                record.put("RecordID", resultSet.getInt("RecordID"));
                record.put("PaymentID", resultSet.getInt("PaymentID"));
                record.put("StudentID", resultSet.getInt("StudentID"));
                record.put("StudentCode", resultSet.getString("StudentCode"));
                
                record.put("UserName", resultSet.getString("UserName"));
                record.put("UserEmail", resultSet.getString("U_EMAIL"));
                record.put("BookName", resultSet.getString("BookName"));
                record.put("BookImage", resultSet.getString("BookImage"));
                record.put("ISBN", resultSet.getString("ISBN"));
                record.put("Quantity", resultSet.getInt("Quantity"));
                record.put("BorrowDate", resultSet.getDate("BorrowDate"));
                record.put("DueReturnDate", resultSet.getDate("DueReturnDate"));
                record.put("Status", resultSet.getString("Status"));
                record.put("FineAmount", resultSet.getBigDecimal("FineAmount"));
                record.put("BorrowPrice", resultSet.getBigDecimal("BorrowPrice"));
                record.put("NumberOfDays", resultSet.getInt("NumberOfDays"));
                record.put("UpdatedAt", resultSet.getTimestamp("UpdatedAt"));
                
                
                record.put("StudentName", resultSet.getString("FullName"));
                record.put("BookName", resultSet.getString("BookName"));
                
                record.put("StudentPhoneNum", resultSet.getString("PhoneNumber"));
                record.put("StudentEmail", resultSet.getString("Email"));
                record.put("AuthorName", resultSet.getString("AuthorName"));
                record.put("PublisherName", resultSet.getString("PublidherName"));
                record.put("Category", resultSet.getString("Category"));
                record.put("DepositPercentage", resultSet.getBigDecimal("DepositPercentage"));
                record.put("FineMultiplier", resultSet.getBigDecimal("FineMultiplier"));
                record.put("Price", resultSet.getBigDecimal("Price"));                
                record.put("Total", resultSet.getBigDecimal("Total"));
                

				records.add(record);
				}
			}
        } catch (SQLException e) {
            System.err.println("Error retrieving borrow records: " + e.getMessage());
            e.printStackTrace();
        }
		return records;
	}
	public boolean selectBrrowIsRetal(String studentcode, String isbn) {
		List<BorrowRecordsEntity> records = new ArrayList<>();
		var query = """
				    SELECT 1
				    FROM BorrowRecords br
				    JOIN Payments p ON br.PaymentID = p.PaymentID
				    JOIN Students s ON s.StudentID = p.StudentID
				    JOIN Books b ON b.BookID = br.BookID
				    WHERE br.IsDeleted = 0 AND br.Status = 'Borrowed' and StudentCode = ? and b.ISBN = ?
				    ORDER BY br.DueReturnDate ASC
				""";

		try (var statement = connection.prepareStatement(query)) {
//			setStatementParameters(statement, record);
			statement.setString(1, studentcode);
			statement.setString(2, isbn);
			var resultSet = statement.executeQuery();
			if (resultSet.next()) {
				return true;
			}
		} catch (SQLException e) {
			System.err.println("Error retrieving borrow records: " + e.getMessage());
			e.printStackTrace();
		}
		return false;
	}

	public boolean selectBrrowIsRetal(int id) {
		List<BorrowRecordsEntity> records = new ArrayList<>();
		var query = """
				    SELECT *
				    FROM BorrowRecords br
				    WHERE br.IsDeleted = 0 AND br.Status = 'Borrowed' and RecordID = ?
				    ORDER BY br.DueReturnDate ASC
				""";

		try (var statement = connection.prepareStatement(query)) {
//			setStatementParameters(statement, record);
			statement.setInt(1, id);
			var resultSet = statement.executeQuery();
			if (resultSet.next()) {
				return true;
			}
		} catch (SQLException e) {
			System.err.println("Error retrieving borrow records: " + e.getMessage());
			e.printStackTrace();
		}
		return false;
	}
	// Insert a new borrow record
	public boolean insert(BorrowRecordsEntity record) {
		var query = "INSERT INTO BorrowRecords (UserID, BookID, Quantity, NumberOfDays, BorrowDate, DueReturnDate, Status, FineAmount, BorrowPrice, IsDeleted, CreatedBy, UpdatedBy, PaymentID) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try (var statement = connection.prepareStatement(query)) {
//			setStatementParameters(statement, record);
			statement.setInt(1, record.getUserID());
			statement.setInt(2, record.getBookID());
			statement.setInt(3, record.getQuantity());
			statement.setInt(4, record.getNumberOfDays());
			statement.setDate(5, new java.sql.Date(record.getBorrowDate().getTime()));
			statement.setDate(6,
					record.getDueReturnDate() != null ? new java.sql.Date(record.getDueReturnDate().getTime()) : null);
			statement.setString(7, record.getStatus());
			statement.setDouble(8, record.getFineAmount());
			statement.setDouble(9, record.getBorrowPrice());
			statement.setBoolean(10, record.isDeleted());
			statement.setInt(11, record.getCreatedBy());
			statement.setInt(12, record.getUpdatedBy());
			statement.setInt(13, record.getPaymentID());
			var rowsInserted = statement.executeUpdate();
			if (rowsInserted > 0) {
				System.out.println("Insert successful: " + rowsInserted + " rows affected.");
				return true;
			}
			System.err.println("Insert failed: No rows affected.");
		} catch (SQLException e) {
			System.err.println("Error inserting BorrowRecordsEntity: " + e.getMessage());
			e.printStackTrace();
		}
		return false;
	}

	// Update an existing borrow record
	public boolean update(BorrowRecordsEntity record) {
		var query = "UPDATE BorrowRecords SET UserID = ?, BookID = ?, Quantity = ?, NumberOfDays = ?, BorrowDate = ?, DueReturnDate = ?, Status = ?, FineAmount = ?, BorrowPrice = ?, IsDeleted = ?, UpdatedBy = ?, UpdatedAt = GETDATE() WHERE RecordID = ?";

		try (var statement = connection.prepareStatement(query)) {
			setStatementParameters(statement, record);
			statement.setInt(12, record.getRecordID());

			return statement.executeUpdate() > 0;
		} catch (SQLException e) {
			System.err.println("Error updating BorrowRecordsEntity: " + e.getMessage());
			e.printStackTrace();
		}
		return false;
	}

	// Soft delete a borrow record
	public boolean delete(int recordID) {
		var query = "UPDATE BorrowRecords SET IsDeleted = 1 WHERE RecordID = ?";

		try (var statement = connection.prepareStatement(query)) {
			statement.setInt(1, recordID);
			return statement.executeUpdate() > 0;
		} catch (SQLException e) {
			System.err.println("Error deleting BorrowRecordsEntity: " + e.getMessage());
			e.printStackTrace();
		}
		return false;
	}

	// Retrieve the number of days for a specific borrow record
	public int getNumberOfDays(int recordID) {
		var query = "SELECT NumberOfDays FROM BorrowRecords WHERE RecordID = ?";

		try (var statement = connection.prepareStatement(query)) {
			statement.setInt(1, recordID);

			try (var resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					return resultSet.getInt("NumberOfDays");
				}
			}
		} catch (SQLException e) {
			System.err.println("Error retrieving number of days for RecordID " + recordID + ": " + e.getMessage());
			e.printStackTrace();
		}
		return 0;
	}

	// Update the number of days for a specific borrow record
	public boolean updateNumberOfDays(int recordID, int numberOfDays) {
		var query = "UPDATE BorrowRecords SET NumberOfDays = ? WHERE RecordID = ?";

		try (var statement = connection.prepareStatement(query)) {
			statement.setInt(1, numberOfDays);
			statement.setInt(2, recordID);

			return statement.executeUpdate() > 0;
		} catch (SQLException e) {
			System.err.println("Error updating number of days for RecordID " + recordID + ": " + e.getMessage());
			e.printStackTrace();
		}
		return false;
	}

	// Update the number of days for a specific borrow record
	public boolean updateStatus(int recordID, String status) {
		var query = "UPDATE BorrowRecords SET Status = ?,  UpdatedBy = ?, UpdatedAt = GETDATE() WHERE RecordID = ?";

		try (var statement = connection.prepareStatement(query)) {
			statement.setString(1, status);
			statement.setInt(2, userSession.getUserId());
			statement.setInt(3, recordID);
			return statement.executeUpdate() > 0;
		} catch (SQLException e) {
			System.err.println("Error updating status for RecordID " + recordID + ": " + e.getMessage());
			e.printStackTrace();
		}
		return false;
	}

	// Update the number of days for a specific borrow record
	public boolean updateFineAmount(int recordID, BigDecimal amount) {
		var query = "UPDATE BorrowRecords SET FineAmount = ? WHERE RecordID = ?";

		try (var statement = connection.prepareStatement(query)) {
			statement.setBigDecimal(1, amount);
			statement.setInt(2, recordID);

			return statement.executeUpdate() > 0;
		} catch (SQLException e) {
			System.err.println("Error updating status for RecordID " + recordID + ": " + e.getMessage());
			e.printStackTrace();
		}
		return false;
	}
	public int getMaxRecordID() {
		var maxRecordID = -1; // Giá trị mặc định nếu không có bản ghi nào

		var sql = "SELECT MAX(RecordID) AS MaxRecordID FROM BorrowRecords";

		try (var conn = ConnectDB.getCon();
				var stmt = conn.prepareStatement(sql);
				var rs = stmt.executeQuery()) {

			if (rs.next()) {
				maxRecordID = rs.getInt("MaxRecordID");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return maxRecordID;
	}
	// Map ResultSet to BorrowRecordsEntity
	private BorrowRecordsEntity mapToEntity(java.sql.ResultSet resultSet) throws SQLException {
		var record = new BorrowRecordsEntity();
		record.setRecordID(resultSet.getInt("RecordID"));
		record.setUserID(resultSet.getInt("UserID"));
		record.setBookID(resultSet.getInt("BookID"));
		record.setQuantity(resultSet.getInt("Quantity"));
		record.setNumberOfDays(resultSet.getInt("NumberOfDays"));
		record.setBorrowDate(resultSet.getDate("BorrowDate"));
		record.setDueReturnDate(resultSet.getDate("DueReturnDate"));
		record.setStatus(resultSet.getString("Status"));
		record.setFineAmount(resultSet.getDouble("FineAmount"));
		record.setBorrowPrice(resultSet.getDouble("BorrowPrice"));
		record.setDeleted(resultSet.getBoolean("IsDeleted"));
		record.setCreatedAt(resultSet.getDate("CreatedAt"));
		record.setCreatedBy(resultSet.getInt("CreatedBy"));
		record.setUpdatedAt(resultSet.getDate("UpdatedAt"));
		record.setUpdatedBy(resultSet.getInt("UpdatedBy"));
		return record;
	}

	private BorrowRecordsEntity mapToOtherEntity(java.sql.ResultSet resultSet, BorrowRecordsEntity record)
			throws SQLException {
		record.setUserName(resultSet.getString("UserName"));
		record.setStudentID(resultSet.getInt("StudentID"));
		record.setStudentCode(resultSet.getString("StudentCode"));
		record.setStudentName(resultSet.getString("FullName"));
		record.setBookName(resultSet.getString("BookName"));
		record.setIsbn(resultSet.getString("ISBN"));
		record.setUserEmail(resultSet.getString("U_EMAIL"));
		record.setStudentPhoneNum(resultSet.getString("PhoneNumber"));
		record.setStudentEmail(resultSet.getString("Email"));
		record.setBookImage(resultSet.getString("BookImage"));
		return record;
	}

	// Set PreparedStatement parameters for BorrowRecordsEntity
	private void setStatementParameters(java.sql.PreparedStatement statement, BorrowRecordsEntity record)
			throws SQLException {
		statement.setInt(1, record.getUserID());
		statement.setInt(2, record.getBookID());
		statement.setInt(3, record.getQuantity());
		statement.setInt(4, record.getNumberOfDays());
		statement.setDate(5, new java.sql.Date(record.getBorrowDate().getTime()));
		statement.setDate(6,
				record.getDueReturnDate() != null ? new java.sql.Date(record.getDueReturnDate().getTime()) : null);
		statement.setString(7, record.getStatus());
		statement.setDouble(8, record.getFineAmount());
		statement.setDouble(9, record.getBorrowPrice());
		statement.setBoolean(10, record.isDeleted());
		statement.setInt(11, record.getCreatedBy());
		statement.setInt(12, record.getUpdatedBy());
	}
}
