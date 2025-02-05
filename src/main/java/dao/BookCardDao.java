package dao;

import service.ConnectDB;

public class BookCardDao {

	// Lấy tổng số sách đang được thuê
	public int getTotalBooksBorrowed() {
		var totalBooksBorrowed = 0;
		var sql = "SELECT SUM(Quantity) AS TotalBooksBorrowed FROM BorrowRecords WHERE [Status] = 'Borrowed' AND IsDeleted = 0";
		try (var con = ConnectDB.getCon(); var stmt = con.prepareStatement(sql); var rs = stmt.executeQuery()) {
			if (rs.next()) {
				totalBooksBorrowed = rs.getInt("TotalBooksBorrowed");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return totalBooksBorrowed;
	}

	// Lấy phần trăm sách đang được thuê so với tổng số lượng sách
	public float getPercentageBooksBorrowed() {
		var percentageBooksBorrowed = 0F;
		var sql = """
				    SELECT (CAST((SELECT SUM(Quantity)
				                  FROM BorrowRecords
				                  WHERE [Status] = 'Borrowed' AND IsDeleted = 0) AS FLOAT)
				            / CAST((SELECT SUM(StockQuantity)
				                    FROM Books
				                    WHERE IsDeleted = 0) AS FLOAT)) * 100 AS PercentageBooksBorrowed
				""";
		try (var con = ConnectDB.getCon(); var stmt = con.prepareStatement(sql); var rs = stmt.executeQuery()) {
			if (rs.next()) {
				percentageBooksBorrowed = rs.getFloat("PercentageBooksBorrowed");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return percentageBooksBorrowed;
	}

	// Lấy tổng số sách được thuê trong ngày hôm nay
	public int getTotalBooksBorrowedToday() {
		var totalBooksBorrowedToday = 0;
		var sql = """
				    SELECT SUM(Quantity) AS TotalBooksBorrowedToday
				    FROM BorrowRecords
				    WHERE CONVERT(DATE, BorrowDate) = CONVERT(DATE, GETDATE()) AND IsDeleted = 0
				""";
		try (var con = ConnectDB.getCon(); var stmt = con.prepareStatement(sql); var rs = stmt.executeQuery()) {
			if (rs.next()) {
				totalBooksBorrowedToday = rs.getInt("TotalBooksBorrowedToday");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return totalBooksBorrowedToday;
	}

	// Lấy phần trăm sách được thuê hôm nay so với tổng số sách đã thuê
	public float getPercentageBooksBorrowedToday() {
		var percentageBooksBorrowedToday = 0F;
		var sql = """
				    SELECT (CAST((SELECT SUM(Quantity)
				                  FROM BorrowRecords
				                  WHERE CONVERT(DATE, BorrowDate) = CONVERT(DATE, GETDATE()) AND IsDeleted = 0) AS FLOAT)
				            / CAST((SELECT SUM(Quantity)
				                    FROM BorrowRecords
				                    WHERE [Status] = 'Borrowed' AND IsDeleted = 0) AS FLOAT)) * 100 AS PercentageBooksBorrowedToday
				""";
		try (var con = ConnectDB.getCon(); var stmt = con.prepareStatement(sql); var rs = stmt.executeQuery()) {
			if (rs.next()) {
				percentageBooksBorrowedToday = rs.getFloat("PercentageBooksBorrowedToday");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return percentageBooksBorrowedToday;
	}

	// Lấy tổng số sách đã trả hôm nay
	public int getTotalBooksReturnedToday() {
		var totalBooksReturnedToday = 0;
		var sql = """
				   SELECT SUM(Quantity) AS TotalBooksReturnedToday
					FROM BorrowRecords
					WHERE [Status] IN ('Returned', 'Overdue')
					  AND CONVERT(DATE, UpdatedAt) = CAST(GETDATE() AS DATE)
					  AND IsDeleted = 0;

				""";
		try (var con = ConnectDB.getCon(); var stmt = con.prepareStatement(sql); var rs = stmt.executeQuery()) {
			if (rs.next()) {
				totalBooksReturnedToday = rs.getInt("TotalBooksReturnedToday");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return totalBooksReturnedToday;
	}

	// Lấy phần trăm sách đã trả hôm nay so với tổng số sách đang được thuê
	public float getPercentageBooksReturnedToday() {
		var percentageBooksReturnedToday = 0F;
		var sql = """
				    SELECT (CAST((SELECT SUM(Quantity)
				                  FROM BorrowRecords
				                  WHERE [Status] in ('Returned','Overdue') AND CONVERT(DATE, UpdatedAt) = CONVERT(DATE, GETDATE()) AND IsDeleted = 0) AS FLOAT)
				            / CAST((SELECT SUM(Quantity)
				                    FROM BorrowRecords
				                    WHERE [Status] = 'Borrowed' AND IsDeleted = 0) AS FLOAT)) * 100 AS PercentageBooksReturnedToday
				""";
		try (var con = ConnectDB.getCon(); var stmt = con.prepareStatement(sql); var rs = stmt.executeQuery()) {
			if (rs.next()) {
				percentageBooksReturnedToday = rs.getFloat("PercentageBooksReturnedToday");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return percentageBooksReturnedToday;
	}
}
