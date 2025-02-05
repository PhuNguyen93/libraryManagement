package dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import entity.BorrowRecordsEntity;
import entity.PaymentEntity;
import service.ConnectDB;

public class PaymentDao {

	// Insert a payment record
	public void insertPayment(PaymentEntity payment) {
		var query = "EXEC sp_InsertPayment ?, ?, ?, ?, ?, ?, ?, ?, ?";
		try (var con = ConnectDB.getCon(); var ps = con.prepareStatement(query)) {
			setInsertOrUpdateParameters(ps, payment);
			ps.executeUpdate();
		} catch (SQLException e) {
			handleSQLException("Error inserting payment", e);
		}
	}

	// Update a payment record
	public void updatePayment(PaymentEntity payment) {
		var query = "EXEC sp_UpdatePayment ?, ?, ?, ?, ?, ?, ?, ?, ?";
		try (var con = ConnectDB.getCon(); var ps = con.prepareStatement(query)) {
			ps.setInt(1, payment.getPaymentID());
			setInsertOrUpdateParameters(ps, payment);
			ps.executeUpdate();
		} catch (SQLException e) {
			handleSQLException("Error updating payment", e);
		}
	}

	// Soft delete a payment record
	public void deletePayment(int paymentID) {
		var query = "EXEC sp_DeletePayment ?";
		try (var con = ConnectDB.getCon(); var ps = con.prepareStatement(query)) {
			ps.setInt(1, paymentID);
			ps.executeUpdate();
		} catch (SQLException e) {
			handleSQLException("Error deleting payment", e);
		}
	}

	// Retrieve all payments
	public List<PaymentEntity> getAllPayments() {
		List<PaymentEntity> payments = new ArrayList<>();
		var query = "EXEC sp_SelectAllPayments";

		try (var con = ConnectDB.getCon(); var ps = con.prepareStatement(query); var rs = ps.executeQuery()) {

			while (rs.next()) {
				payments.add(mapToPaymentEntity(rs));
			}
		} catch (SQLException e) {
			handleSQLException("Error retrieving all payments", e);
		}
		return payments;
	}
	public List<PaymentEntity> select() {
	    List<PaymentEntity> payments = new ArrayList<>();
	    var query = "SELECT Payments.*, Students.Avatar AS AvatarStudent, Students.StudentCode AS StudentCode, " +
	                "Students.FullName AS StudentName, Students.PhoneNumber, Students.Email, " +
	                "(SELECT ISNULL(SUM(br1.Quantity), 0) FROM BorrowRecords br1 WHERE br1.PaymentID = Payments.PaymentID) AS TotalBooksBorrowed, " +
	                "(SELECT ISNULL(SUM(br1.Quantity), 0) FROM BorrowRecords br1 WHERE br1.PaymentID = Payments.PaymentID AND br1.[Status] <> 'Borrowed') AS TotalBooksReturned, " +
	                "(SELECT ISNULL(SUM(br1.FineAmount), 0) FROM BorrowRecords br1 WHERE br1.PaymentID = Payments.PaymentID AND br1.FineAmount > 0) AS TotalFineAmount " +
	                "FROM Payments " +
	                "LEFT JOIN Students ON Payments.StudentID = Students.StudentID " +
	                "ORDER BY Payments.PaymentID DESC";
	    try (var con = ConnectDB.getCon(); var ps = con.prepareStatement(query); var rs = ps.executeQuery()) {
	        while (rs.next()) {
	            var record = mapToPaymentEntity(rs);
	            record = mapToOrder(rs, record);
	            payments.add(record);
	        }
	    } catch (SQLException e) {
	        handleSQLException("Error retrieving all payments", e);
	    }
	    return payments;
	}

	// Retrieve a payment by ID
	public PaymentEntity getPaymentByID(int paymentID) {
		PaymentEntity payment = null;
		var query = "EXEC sp_SelectPaymentByID ?";

		try (var con = ConnectDB.getCon(); var ps = con.prepareStatement(query)) {
			ps.setInt(1, paymentID);

			try (var rs = ps.executeQuery()) {
				if (rs.next()) {
					payment = mapToPaymentEntity(rs);
				}
			}
		} catch (SQLException e) {
			handleSQLException("Error retrieving payment by ID", e);
		}
		return payment;
	}

	// Get the last inserted payment ID
	public int getLastInsertedPaymentID() {
		var query = "SELECT MAX(PaymentID) AS LastPaymentID FROM Payments";
		try (var con = ConnectDB.getCon(); var ps = con.prepareStatement(query); var rs = ps.executeQuery()) {
			if (rs.next()) {
				return rs.getInt("LastPaymentID");
			}
		} catch (SQLException e) {
			handleSQLException("Error retrieving last inserted PaymentID", e);
		}
		return -1; // Default value if no record is found
	}

	// Map ResultSet to PaymentEntity
	private PaymentEntity mapToPaymentEntity(java.sql.ResultSet rs) throws SQLException {
		var payment = new PaymentEntity();
		payment.setPaymentID(rs.getInt("PaymentID"));
		payment.setUserID(rs.getInt("UserID"));
		payment.setAmount(rs.getDouble("Amount"));
		payment.setPaymentDate(rs.getDate("PaymentDate"));
		payment.setPaymentMethod(rs.getString("PaymentMethod"));
		payment.setDescription(rs.getString("Description"));
		payment.setAmountGiven(rs.getDouble("AmountGiven"));
		payment.setTotalOrderAmount(rs.getDouble("TotalOrderAmount"));
		payment.setChangeAmount(rs.getDouble("ChangeAmount"));
		payment.setDeleted(rs.getBoolean("IsDeleted"));
	    return payment;
	}
	private PaymentEntity mapToOrder(java.sql.ResultSet rs, PaymentEntity payment) throws SQLException {
		payment.setStudentAvatar(rs.getString("AvatarStudent"));
		payment.setStudentCode(rs.getString("StudentCode"));		
		payment.setStudentName(rs.getString("StudentName"));
		payment.setTotalBooksBorrowed(rs.getDouble("TotalBooksBorrowed"));
		payment.setTotalBooksReturned(rs.getDouble("TotalBooksReturned"));
		payment.setTotalFineAmount(rs.getDouble("TotalFineAmount"));
		// Các cột liên quan đến sinh viên:
	    payment.setPhoneNumber(rs.getString("PhoneNumber")); 
	    payment.setEmail(rs.getString("Email"));		

		return payment;
	}
	// Set parameters for insert or update queries
	private void setInsertOrUpdateParameters(java.sql.PreparedStatement ps, PaymentEntity payment) throws SQLException {
		ps.setInt(1, payment.getUserID());
		ps.setDouble(2, payment.getAmount());
		ps.setString(3, payment.getPaymentMethod());
		ps.setString(4, payment.getDescription());
		ps.setDouble(5, payment.getAmountGiven());
		ps.setDouble(6, payment.getTotalOrderAmount());
		ps.setDouble(7, payment.getChangeAmount());
		ps.setBoolean(8, payment.isDeleted());
		ps.setInt(9, payment.getStudentID());
	}

	// Handle SQL exceptions with error messages
	private void handleSQLException(String message, SQLException e) {
		e.printStackTrace();
		JOptionPane.showMessageDialog(null, message + ": " + e.getMessage(), "Database Error",
				JOptionPane.ERROR_MESSAGE);
	}
	public List<BorrowRecordsEntity> getOrderDetails(int paymentID) {
	    List<BorrowRecordsEntity> orderDetails = new ArrayList<>();
	    String query = "SELECT \r\n"
	    		+ "    Books.Title AS BookTitle, \r\n"
	    		+ "    br.Quantity, \r\n"
	    		+ "    CASE \r\n"
	    		+ "        WHEN br.Status = 'Returned' AND br.FineAmount > 0 THEN 'Damage'\r\n"
	    		+ "        WHEN br.Status = 'Overdue' THEN 'Overdue'\r\n"
	    		+ "        WHEN br.Status = 'Borrowed' THEN 'Borrowed'\r\n"
	    		+ "        ELSE 'Returned'\r\n"
	    		+ "    END AS Status,\r\n"
	    		+ "    br.DueReturnDate,\r\n"
	    		+ "    ((br.Quantity * br.BorrowPrice) + (br.Quantity * Books.Price * Books.DepositPercentage / 100)) AS Total \r\n"
	    		+ "FROM BorrowRecords br \r\n"
	    		+ "LEFT JOIN Books ON br.BookID = Books.BookID \r\n"
	    		+ "WHERE br.PaymentID = ?\r\n";
	    try (var con = ConnectDB.getCon(); var ps = con.prepareStatement(query)) {
	        ps.setInt(1, paymentID);
	        try (var rs = ps.executeQuery()) {
	            while (rs.next()) {
	                var detail = new BorrowRecordsEntity();
	                detail.setBookName(rs.getString("BookTitle"));
	                detail.setQuantity(rs.getInt("Quantity"));
	                detail.setStatus(rs.getString("Status"));    
	                detail.setDueReturnDate(rs.getDate("DueReturnDate"));
	                detail.setTotal(rs.getDouble("Total"));

	                orderDetails.add(detail);
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        JOptionPane.showMessageDialog(null, "Error loading order details: " + e.getMessage(),
	                "Database Error", JOptionPane.ERROR_MESSAGE);
	    }
	    return orderDetails;
	}
	public List<PaymentEntity> selectAll() {
	    List<PaymentEntity> payments = new ArrayList<>();
	    String query = "SELECT PaymentID, UserID, Amount, PaymentDate, PaymentMethod, Description, AmountGiven, " +
	                   "TotalOrderAmount, ChangeAmount, StudentID FROM Payments ";  // Lọc theo điều kiện nếu cần

	    try (var con = ConnectDB.getCon(); var ps = con.prepareStatement(query); var rs = ps.executeQuery()) {
	        while (rs.next()) {
	        
	            payments.add(mapToPaymentEntity1(rs));  // Chuyển đổi kết quả truy vấn thành đối tượng PaymentEntity
	        }
	    } catch (SQLException e) {
	        handleSQLException("Error retrieving all payments", e);
	    }
	    return payments;
	}
	private PaymentEntity mapToPaymentEntity1(java.sql.ResultSet rs) throws SQLException {
		var payment = new PaymentEntity();
		payment.setPaymentID(rs.getInt("PaymentID"));
		payment.setUserID(rs.getInt("UserID"));
		payment.setAmount(rs.getDouble("Amount"));
		payment.setPaymentDate(rs.getDate("PaymentDate"));
		payment.setPaymentMethod(rs.getString("PaymentMethod"));
		payment.setDescription(rs.getString("Description"));
		payment.setAmountGiven(rs.getDouble("AmountGiven"));
		payment.setTotalOrderAmount(rs.getDouble("TotalOrderAmount"));
		payment.setChangeAmount(rs.getDouble("ChangeAmount"));
		
	    return payment;
	}

}
