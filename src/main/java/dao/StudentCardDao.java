package dao;

import service.ConnectDB;

public class StudentCardDao {

	// Lấy số lượng sinh viên đang thuê sách
	public int getTotalStudentsBorrowingBooks() {
		var sql = """
				SELECT COUNT(DISTINCT Students.StudentID) AS TotalStudentsBorrowingBooks
				FROM Students
				INNER JOIN Payments ON Students.StudentID = Payments.StudentID
				INNER JOIN BorrowRecords ON Payments.PaymentID = BorrowRecords.PaymentID
				WHERE BorrowRecords.[Status] = 'Borrowed'
				  AND BorrowRecords.IsDeleted = 0
				  AND Payments.IsDeleted = 0
				  AND Students.IsDeleted = 0;
				""";
		try (var con = ConnectDB.getCon();
				var stmt = con.prepareStatement(sql);
				var rs = stmt.executeQuery()) {
			if (rs.next()) {
				return rs.getInt("TotalStudentsBorrowingBooks");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	// Lấy phần trăm số lượng sinh viên thuê sách so với tổng số sinh viên
	public float getPercentageStudentsBorrowingBooks() {
		var sql = """
				SELECT
				    (CAST(
				        (SELECT COUNT(DISTINCT Students.StudentID)
				         FROM Students
				         INNER JOIN Payments ON Students.StudentID = Payments.StudentID
				         INNER JOIN BorrowRecords ON Payments.PaymentID = BorrowRecords.PaymentID
				         WHERE BorrowRecords.[Status] = 'Borrowed'
				           AND BorrowRecords.IsDeleted = 0
				           AND Payments.IsDeleted = 0
				           AND Students.IsDeleted = 0
				        ) AS FLOAT)
				    /
				    CAST(
				        (SELECT COUNT(*)
				         FROM Students
				         WHERE IsDeleted = 0
				        ) AS FLOAT)
				    ) * 100 AS PercentageStudentsBorrowingBooks;
				""";
		try (var con = ConnectDB.getCon();
				var stmt = con.prepareStatement(sql);
				var rs = stmt.executeQuery()) {
			if (rs.next()) {
				return rs.getFloat("PercentageStudentsBorrowingBooks");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	// Lấy số lượng sinh viên thuê sách trong ngày
	public int getTotalStudentsBorrowingToday() {
		var sql = """
				SELECT COUNT(DISTINCT Students.StudentID) AS TotalStudentsBorrowingToday
				FROM Students
				INNER JOIN Payments ON Students.StudentID = Payments.StudentID
				INNER JOIN BorrowRecords ON Payments.PaymentID = BorrowRecords.PaymentID
				WHERE CONVERT(DATE, Payments.PaymentDate) = CONVERT(DATE, GETDATE())
				  AND BorrowRecords.IsDeleted = 0
				  AND Payments.IsDeleted = 0
				  AND Students.IsDeleted = 0;
				""";
		try (var con = ConnectDB.getCon();
				var stmt = con.prepareStatement(sql);
				var rs = stmt.executeQuery()) {
			if (rs.next()) {
				return rs.getInt("TotalStudentsBorrowingToday");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	// Lấy phần trăm số lượng sinh viên thuê sách trong ngày so với tổng số lượng
	// sinh viên đang thuê sách
	public float getPercentageStudentsBorrowingToday() {
		var sql = """
				SELECT
				    (CAST(
				        (SELECT COUNT(DISTINCT Students.StudentID)
				         FROM Students
				         INNER JOIN Payments ON Students.StudentID = Payments.StudentID
				         INNER JOIN BorrowRecords ON Payments.PaymentID = BorrowRecords.PaymentID
				         WHERE CONVERT(DATE, Payments.PaymentDate) = CONVERT(DATE, GETDATE())
				           AND BorrowRecords.IsDeleted = 0
				           AND Payments.IsDeleted = 0
				           AND Students.IsDeleted = 0
				        ) AS FLOAT)
				    /
				    CAST(
				        (SELECT COUNT(DISTINCT Students.StudentID)
				         FROM Students
				         INNER JOIN Payments ON Students.StudentID = Payments.StudentID
				         INNER JOIN BorrowRecords ON Payments.PaymentID = BorrowRecords.PaymentID
				         WHERE BorrowRecords.[Status] = 'Borrowed'
				           AND BorrowRecords.IsDeleted = 0
				           AND Payments.IsDeleted = 0
				           AND Students.IsDeleted = 0
				        ) AS FLOAT)
				    ) * 100 AS PercentageStudentsBorrowingToday;
				""";
		try (var con = ConnectDB.getCon();
				var stmt = con.prepareStatement(sql);
				var rs = stmt.executeQuery()) {
			if (rs.next()) {
				return rs.getFloat("PercentageStudentsBorrowingToday");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	// Lấy số lượng sinh viên trả sách trong ngày hôm nay
	public int getTotalStudentsReturningToday() {
		var sql = """
				SELECT COUNT(DISTINCT Students.StudentID) AS TotalStudentsReturningToday
				FROM Students
				INNER JOIN Payments ON Students.StudentID = Payments.StudentID
				INNER JOIN BorrowRecords ON Payments.PaymentID = BorrowRecords.PaymentID
				WHERE CONVERT(DATE, BorrowRecords.UpdatedAt) = CONVERT(DATE, GETDATE())
				  AND BorrowRecords.[Status] = 'Returned'
				  AND BorrowRecords.IsDeleted = 0
				  AND Payments.IsDeleted = 0
				  AND Students.IsDeleted = 0;

				""";
		try (var con = ConnectDB.getCon();
				var stmt = con.prepareStatement(sql);
				var rs = stmt.executeQuery()) {
			if (rs.next()) {
				return rs.getInt("TotalStudentsReturningToday");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	// Lấy phần trăm số lượng sinh viên trả sách trong ngày hôm nay so với tổng số
	// lượng sinh viên chưa trả sách
	public float getPercentageStudentsReturningToday() {
		var sql = """
				SELECT 
                (CAST(
                    (SELECT COUNT(DISTINCT Students.StudentID)
                     FROM Students
                     INNER JOIN Payments ON Students.StudentID = Payments.StudentID
                     INNER JOIN BorrowRecords ON Payments.PaymentID = BorrowRecords.PaymentID
                     WHERE CONVERT(DATE, BorrowRecords.UpdatedAt) = CONVERT(DATE, GETDATE())
                       AND BorrowRecords.[Status] = 'Returned'
                       AND BorrowRecords.IsDeleted = 0
                       AND Payments.IsDeleted = 0
                       AND Students.IsDeleted = 0
                    ) AS FLOAT)
                /
                CAST(
                    (SELECT COUNT(DISTINCT Students.StudentID)
                     FROM Students
                     INNER JOIN Payments ON Students.StudentID = Payments.StudentID
                     INNER JOIN BorrowRecords ON Payments.PaymentID = BorrowRecords.PaymentID
                     WHERE BorrowRecords.IsDeleted = 0
                       AND Payments.IsDeleted = 0
                       AND Students.IsDeleted = 0
                    ) AS FLOAT)
                ) * 100 AS PercentageStudentsReturningToday
				""";
		try (var con = ConnectDB.getCon();
				var stmt = con.prepareStatement(sql);
				var rs = stmt.executeQuery()) {
			if (rs.next()) {
				return rs.getFloat("PercentageStudentsReturningToday");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
}
