package dao;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import entity.BorrowHistoryEntity;
import service.ConnectDB;

public class BorrowHistoryDao {

	// Lấy toàn bộ lịch sử
	public List<BorrowHistoryEntity> select() {
		List<BorrowHistoryEntity> historyList = new ArrayList<>();

		var sql = "SELECT * FROM BorrowHistory WHERE IsDeleted = 0";

		try (var conn = ConnectDB.getCon();
				var stmt = conn.prepareStatement(sql);
				var rs = stmt.executeQuery()) {

			while (rs.next()) {
				var history = new BorrowHistoryEntity();
				history.setHistoryID(rs.getInt("HistoryID"));
				history.setRecordID(rs.getInt("RecordID"));
				history.setAction(rs.getString("Action"));
				history.setActionDate(rs.getTimestamp("ActionDate").toLocalDateTime());
				history.setDeleted(rs.getBoolean("IsDeleted"));

				historyList.add(history);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return historyList;
	}

	// Thêm lịch sử mới
	public boolean insert(BorrowHistoryEntity history) {
		var sql = """
				INSERT INTO BorrowHistory (RecordID, Action, ActionDate, IsDeleted)
				VALUES (?, ?, ?, ?)
				""";

		try (var conn = ConnectDB.getCon(); var stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, history.getRecordID());
			stmt.setString(2, history.getAction());
			stmt.setTimestamp(3, Timestamp.valueOf(history.getActionDate()));
			stmt.setBoolean(4, history.isDeleted());

			return stmt.executeUpdate() > 0;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	// Cập nhật lịch sử
	public boolean update(BorrowHistoryEntity history) {
		var sql = """
				UPDATE BorrowHistory
				SET RecordID = ?, Action = ?, ActionDate = ?, IsDeleted = ?
				WHERE HistoryID = ?
				""";

		try (var conn = ConnectDB.getCon(); var stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, history.getRecordID());
			stmt.setString(2, history.getAction());
			stmt.setTimestamp(3, Timestamp.valueOf(history.getActionDate()));
			stmt.setBoolean(4, history.isDeleted());
			stmt.setInt(5, history.getHistoryID());

			return stmt.executeUpdate() > 0;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	// Xóa mềm (IsDeleted = 1)
	public boolean delete(int historyID) {
		var sql = "UPDATE BorrowHistory SET IsDeleted = 1 WHERE HistoryID = ?";

		try (var conn = ConnectDB.getCon(); var stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, historyID);

			return stmt.executeUpdate() > 0;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	// Lấy lịch sử theo ID
	public BorrowHistoryEntity selectByID(int historyID) {
		BorrowHistoryEntity history = null;

		var sql = "SELECT * FROM BorrowHistory WHERE HistoryID = ? AND IsDeleted = 0";

		try (var conn = ConnectDB.getCon(); var stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, historyID);
			try (var rs = stmt.executeQuery()) {
				if (rs.next()) {
					history = new BorrowHistoryEntity();
					history.setHistoryID(rs.getInt("HistoryID"));
					history.setRecordID(rs.getInt("RecordID"));
					history.setAction(rs.getString("Action"));
					history.setActionDate(rs.getTimestamp("ActionDate").toLocalDateTime());
					history.setDeleted(rs.getBoolean("IsDeleted"));
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return history;
	}
}
