package dao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import entity.DamageEntity;
import service.ConnectDB;

public class DamageDao {

	public boolean insertDamage(DamageEntity damage) {
		var sql = """
				    INSERT INTO DamagedBooks (BookID, ReportedBy, DamageDate, DamageDescription, DamageSeverity, [Status], RepairCost, CreatedBy, UpdatedBy)
				    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
				""";

		try (var conn = ConnectDB.getCon(); var stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, damage.getBookID());
			stmt.setInt(2, damage.getReportedBy());
			stmt.setTimestamp(3, new Timestamp(damage.getDamageDate().getTime()));
			stmt.setString(4, damage.getDamageDescription());
			stmt.setString(5, damage.getDamageSeverity());
			stmt.setString(6, damage.getStatus());
			stmt.setBigDecimal(7, damage.getRepairCost());
			stmt.setInt(8, damage.getCreatedBy());
			stmt.setInt(9, damage.getUpdatedBy());
			return stmt.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean updateDamage(DamageEntity damage) {
		var sql = """
				    UPDATE DamagedBooks
				    SET BookID = ?, ReportedBy = ?, DamageDate = ?, DamageDescription = ?, DamageSeverity = ?, [Status] = ?,
				        RepairCost = ?, UpdatedAt = GETDATE(), UpdatedBy = ?
				    WHERE DamageID = ? AND IsDeleted = 0
				""";

		try (var conn = ConnectDB.getCon(); var stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, damage.getBookID());
			stmt.setInt(2, damage.getReportedBy());
			stmt.setTimestamp(3, new Timestamp(damage.getDamageDate().getTime()));
			stmt.setString(4, damage.getDamageDescription());
			stmt.setString(5, damage.getDamageSeverity());
			stmt.setString(6, damage.getStatus());
			stmt.setBigDecimal(7, damage.getRepairCost());
			stmt.setInt(8, damage.getUpdatedBy());
			stmt.setInt(9, damage.getDamageID());
			return stmt.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean deleteDamage(int damageID) {
		var sql = "UPDATE DamagedBooks SET IsDeleted = 1, UpdatedAt = GETDATE() WHERE DamageID = ?";

		try (var conn = ConnectDB.getCon(); var stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, damageID);
			return stmt.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public DamageEntity getDamageByID(int damageID) {
		var sql = """
				    SELECT * FROM DamagedBooks WHERE DamageID = ? AND IsDeleted = 0
				""";

		try (var conn = ConnectDB.getCon(); var stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, damageID);
			try (var rs = stmt.executeQuery()) {
				if (rs.next()) {
					return mapResultSetToEntity(rs);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<DamageEntity> getAllDamages() {
		List<DamageEntity> damages = new ArrayList<>();
		var sql = "SELECT * FROM DamagedBooks WHERE IsDeleted = 0 ORDER BY DamageDate DESC";
//		JOptionPane.showMessageDialog(null, 1);
		try (var conn = ConnectDB.getCon(); var stmt = conn.prepareStatement(sql); var rs = stmt.executeQuery()) {
			while (rs.next()) {
				damages.add(mapResultSetToEntity(rs));
//				JOptionPane.showMessageDialog(null, 2);

			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return damages;
	}

	private DamageEntity mapResultSetToEntity(ResultSet rs) throws SQLException {
		var damage = new DamageEntity();
		damage.setDamageID(rs.getInt("DamageID"));
		damage.setBookID(rs.getInt("BookID"));
		damage.setReportedBy(rs.getInt("ReportedBy"));
		damage.setDamageDate(rs.getTimestamp("DamageDate"));
		damage.setDamageDescription(rs.getString("DamageDescription"));
		damage.setDamageSeverity(rs.getString("DamageSeverity"));
		damage.setStatus(rs.getString("Status"));
		damage.setRepairCost(rs.getBigDecimal("RepairCost"));
		damage.setIsDeleted(rs.getBoolean("IsDeleted"));
		damage.setCreatedAt(rs.getTimestamp("CreatedAt"));
		damage.setCreatedBy(rs.getInt("CreatedBy"));
		damage.setUpdatedAt(rs.getTimestamp("UpdatedAt"));
		damage.setUpdatedBy(rs.getInt("UpdatedBy"));
		return damage;
	}
	
	public boolean updateDamageStatus(int damageID, String status) {
	    String sql = "UPDATE DamagedBooks SET Status = ? WHERE DamageID = ?";
	    try (var conn = ConnectDB.getCon(); var stmt = conn.prepareStatement(sql)) {
	        stmt.setString(1, status);
	        stmt.setInt(2, damageID);
	        return stmt.executeUpdate() > 0; // Trả về true nếu cập nhật thành công
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false; // Trả về false nếu có lỗi
	    }
	}
	
	 public static void updateRepairCost(int damageID, BigDecimal repairCost) {
	        String query = "UPDATE DamagedBooks SET RepairCost = ?, UpdatedAt = GETDATE() WHERE DamageID = ?";

	        try (var conn = ConnectDB.getCon();
	             var ps = conn.prepareStatement(query)) {

	            ps.setBigDecimal(1, repairCost);
	            ps.setInt(2, damageID);

	            int rowsAffected = ps.executeUpdate();
//	            if (rowsAffected > 0) {
//	                System.out.println("Repair cost updated successfully for DamageID: " + damageID);
//	            } else {
//	                System.out.println("No rows updated. Check if DamageID exists: " + damageID);
//	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	            throw new RuntimeException("Failed to update repair cost for DamageID: " + damageID, e);
	        }
	    }

	    public static void updateStatus(int damageID, String status) {
	        String query = "UPDATE DamagedBooks SET [Status] = ?, UpdatedAt = GETDATE() WHERE DamageID = ?";

	        try (var conn = ConnectDB.getCon();
		             var ps = conn.prepareStatement(query)) {

	            ps.setString(1, status);
	            ps.setInt(2, damageID);

	            int rowsAffected = ps.executeUpdate();
//	            if (rowsAffected > 0) {
//	                System.out.println("Status updated successfully for DamageID: " + damageID);
//	            } else {
//	                System.out.println("No rows updated. Check if DamageID exists: " + damageID);
//	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	            throw new RuntimeException("Failed to update status for DamageID: " + damageID, e);
	        }
	    }
	    public static List<DamageEntity> searchDamagedBooks(String searchQuery) {
	        String query = "SELECT d.DamageID, d.BookID, b.Image, b.Title, u.FullName AS ReportedBy, d.DamageDate, d.DamageDescription, d.DamageSeverity, d.Status, d.RepairCost " +
	                       "FROM DamagedBooks d " +
	                       "JOIN Books b ON d.BookID = b.BookID " +
	                       "JOIN Users u ON d.ReportedBy = u.UserID " +
	                       "WHERE (b.Title LIKE ? OR d.DamageDescription LIKE ? OR u.FullName LIKE ?) AND d.IsDeleted = 0 ORDER BY d.DamageDate DESC";

	        List<DamageEntity> damages = new ArrayList<>();

	        try (var conn = ConnectDB.getCon();
		             var ps = conn.prepareStatement(query)) {

	            String searchPattern = "%" + searchQuery + "%";
	            ps.setString(1, searchPattern);
	            ps.setString(2, searchPattern);
	            ps.setString(3, searchPattern);

	            try (ResultSet rs = ps.executeQuery()) {
	                while (rs.next()) {
	                    DamageEntity damage = new DamageEntity();
	                    damage.setDamageID(rs.getInt("DamageID"));
	                    damage.setBookID(rs.getInt("BookID")); // Assuming BookID is same as DamageID for simplicity
	                    damage.setBookName(rs.getString("Title"));
	                    damage.setBookImage(rs.getString("Image")); // Assuming BookID is same as DamageID for simplicity
	                    damage.setReportedName(rs.getString("ReportedBy"));
	                    damage.setDamageDate(rs.getDate("DamageDate"));
	                    damage.setDamageDescription(rs.getString("DamageDescription"));
	                    damage.setDamageSeverity(rs.getString("DamageSeverity"));
	                    damage.setStatus(rs.getString("Status"));
	                    damage.setRepairCost(rs.getBigDecimal("RepairCost"));
	                    
	                    damages.add(damage);
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	            throw new RuntimeException("Failed to search damaged books", e);
	        }

	        return damages;
	    }
	    
	    public static void updateBookQuantity(int bookID, int increment) {
	        String query = "UPDATE Books SET Quantity = Quantity + ?, StockQuantity = StockQuantity + ?, UpdatedAt = GETDATE() WHERE BookID = ?";

	        try (var conn = ConnectDB.getCon();
		             var ps = conn.prepareStatement(query)) {

	            ps.setInt(1, increment);
	            ps.setInt(2, increment);
	            ps.setInt(3, bookID);

	            int rowsAffected = ps.executeUpdate();
//	            if (rowsAffected > 0) {
//	                System.out.println("Book quantity updated successfully for BookID: " + bookID);
//	            } else {
//	                System.out.println("No rows updated. Check if BookID exists: " + bookID);
//	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	            throw new RuntimeException("Failed to update book quantity for BookID: " + bookID, e);
	        }
	    }
}