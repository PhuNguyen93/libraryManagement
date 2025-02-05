package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import service.ConnectDB;

public class ChartDao {

	/**
	 * Lấy số lượng sách mượn theo tuần trong một tháng cụ thể
	 *
	 * @param year  Năm cần thống kê (ví dụ: 2025)
	 * @param month Tháng cần thống kê (ví dụ: 1 cho Tháng 1)
	 * @return Map với key là "W[WeekInMonth]" và value là số lượng mượn sách
	 */
	public Map<String, Integer> getWeeklyLoanCountInMonth(int year, int month) {
		Map<String, Integer> loanCounts = new LinkedHashMap<>();
		var sql = """
				SET DATEFIRST 1; -- Đặt Monday là ngày đầu tiên trong tuần
				SELECT
				'W' + CAST(CEILING((DAY(BorrowDate) + DATEPART(WEEKDAY, DATEADD(DAY, 1 - DAY(BorrowDate), BorrowDate)) - 1) / 7.0) AS VARCHAR) AS WeekInMonth,
				COUNT(RecordID) AS LoanCount
				FROM BorrowRecords
				WHERE YEAR(BorrowDate) = ?
				  AND MONTH(BorrowDate) = ?
				  AND IsDeleted = 0
				GROUP BY CEILING((DAY(BorrowDate) + DATEPART(WEEKDAY, DATEADD(DAY, 1 - DAY(BorrowDate), BorrowDate)) - 1) / 7.0)
				ORDER BY CEILING((DAY(BorrowDate) + DATEPART(WEEKDAY, DATEADD(DAY, 1 - DAY(BorrowDate), BorrowDate)) - 1) / 7.0);
				""";
		try (Connection con = ConnectDB.getCon(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, year);
			ps.setInt(2, month);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					var week = rs.getString("WeekInMonth"); // "W1", "W2", etc.
					var count = rs.getInt("LoanCount");
					loanCounts.put(week, count);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return loanCounts;
	}

	/**
	 * Lấy tổng doanh thu theo tuần trong một tháng cụ thể
	 *
	 * @param year  Năm cần thống kê (ví dụ: 2025)
	 * @param month Tháng cần thống kê (ví dụ: 1 cho Tháng 1)
	 * @return Map với key là "W[WeekNumber]" và value là tổng doanh thu
	 */
	public Map<String, Double> getWeeklyRevenue(int year, int month) {
		Map<String, Double> weeklyRevenue = new LinkedHashMap<>();
		var sql = """
				SET DATEFIRST 1; -- Đặt Monday là ngày đầu tiên trong tuần
				SELECT
				'W' + CAST(CEILING((DAY(PaymentDate) + DATEPART(WEEKDAY, DATEADD(DAY, 1 - DAY(PaymentDate), PaymentDate)) - 1) / 7.0) AS VARCHAR) AS WeekNumber,
				SUM(Amount) AS WeeklyRevenue
				FROM Payments
				WHERE YEAR(PaymentDate) = ?
				  AND MONTH(PaymentDate) = ?
				  AND IsDeleted = 0
				GROUP BY CEILING((DAY(PaymentDate) + DATEPART(WEEKDAY, DATEADD(DAY, 1 - DAY(PaymentDate), PaymentDate)) - 1) / 7.0)
				ORDER BY CEILING((DAY(PaymentDate) + DATEPART(WEEKDAY, DATEADD(DAY, 1 - DAY(PaymentDate), PaymentDate)) - 1) / 7.0);
				""";

		try (Connection con = ConnectDB.getCon(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, year);
			ps.setInt(2, month);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					var week = rs.getString("WeekNumber"); // "W1", "W2", etc.
					var revenue = rs.getDouble("WeeklyRevenue");
					weeklyRevenue.put(week, revenue);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return weeklyRevenue;
	}

	/**
	 * Lấy số lượng sách theo tên thể loại để vẽ Biểu đồ tròn
	 *
	 * @return Map với key là tên thể loại và value là số lượng sách
	 */
	public Map<String, Integer> getBookCountByCategoryName() {
		Map<String, Integer> bookCounts = new LinkedHashMap<>();
		var sql = """
				SELECT
				C.CategoryName AS Category,
				COUNT(B.BookID) AS BookCount
				FROM Categories C
				LEFT JOIN BookCategories BC ON C.CategoryID = BC.CategoryID
				LEFT JOIN Books B ON BC.BookID = B.BookID
				WHERE C.IsDeleted = 0 AND (B.IsDeleted = 0 OR B.IsDeleted IS NULL)
				GROUP BY C.CategoryName
				ORDER BY BookCount DESC;
				""";
		try (Connection con = ConnectDB.getCon();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				var category = rs.getString("Category");
				var count = rs.getInt("BookCount");
				bookCounts.put(category, count);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bookCounts;
	}

//	public Map<String, Integer> getWeeklyDamagedBooks(int year, int month) {
//		Map<String, Integer> data = new LinkedHashMap<>();
//		var sql = """
//				SELECT DATEPART(WEEK, DamageDate) AS WeekNumber, COUNT(*) AS DamageCount \
//				FROM DamagedBooks \
//				WHERE YEAR(DamageDate) = ? AND MONTH(DamageDate) = ? AND IsDeleted = 0 \
//				GROUP BY DATEPART(WEEK, DamageDate) \
//				ORDER BY WeekNumber ASC""";
//
//		try (var con = ConnectDB.getCon(); var ps = con.prepareStatement(sql)) {
//
//			ps.setInt(1, year);
//			ps.setInt(2, month);
//			var rs = ps.executeQuery();
//
//			while (rs.next()) {
//				var week = "W" + rs.getInt("WeekNumber");
//				var count = rs.getInt("DamageCount");
//				data.put(week, count);
//			}
//
//			rs.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return data;
//	}

}