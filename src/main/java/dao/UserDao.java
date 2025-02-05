package dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.mindrot.jbcrypt.BCrypt;

import entity.UserEntity;
import service.ConnectDB;

public class UserDao {

	// Lấy danh sách tất cả người dùng từ bảng Users
	public List<UserEntity> selectAll() {
		List<UserEntity> users = new ArrayList<>();
		var query = "SELECT * FROM Users WHERE IsDeleted = 0 ORDER BY CreatedAt DESC";
		try (var con = ConnectDB.getCon(); var ps = con.prepareStatement(query); var rs = ps.executeQuery()) {

			while (rs.next()) {
				var user = new UserEntity();
				user.setUserID(rs.getInt("UserID"));
				user.setFullName(rs.getString("FullName"));
				user.setUsername(rs.getString("Email"));
				user.setPassword(rs.getString("Password"));
				user.setEmail(rs.getString("Email"));
				user.setPhoneNumber(rs.getString("PhoneNumber"));
				user.setAddress(rs.getString("Address"));
				user.setUserRole(rs.getInt("UserRole"));
				user.setAvatar(rs.getString("Avatar"));
				user.setActive(rs.getBoolean("IsActive"));
				user.setDeleted(rs.getBoolean("IsDeleted"));
				user.setCreatedAt(rs.getTimestamp("CreatedAt").toLocalDateTime());
				user.setCreatedBy(rs.getInt("CreatedBy"));
				user.setUpdatedAt(rs.getTimestamp("UpdatedAt").toLocalDateTime());
				user.setUpdatedBy(rs.getInt("UpdatedBy"));
				users.add(user);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return users;
	}

	// Lấy thông tin một người dùng theo UserID
	public UserEntity getUserById(int userId) {
		var query = "SELECT * FROM Users WHERE UserID = ? AND IsDeleted = 0";
		try (var con = ConnectDB.getCon(); var ps = con.prepareStatement(query)) {

			ps.setInt(1, userId);
			try (var rs = ps.executeQuery()) {
				if (rs.next()) {
					var user = new UserEntity();
					user.setUserID(rs.getInt("UserID"));
					user.setFullName(rs.getString("FullName"));
					user.setUsername(rs.getString("Email"));
					user.setPassword(rs.getString("Password"));
					user.setEmail(rs.getString("Email"));
					user.setPhoneNumber(rs.getString("PhoneNumber"));
					user.setAddress(rs.getString("Address"));
					user.setUserRole(rs.getInt("UserRole"));
					user.setAvatar(rs.getString("Avatar"));
					user.setActive(rs.getBoolean("IsActive"));
					user.setDeleted(rs.getBoolean("IsDeleted"));
					user.setCreatedAt(rs.getTimestamp("CreatedAt").toLocalDateTime());
					user.setCreatedBy(rs.getInt("CreatedBy"));
					user.setUpdatedAt(rs.getTimestamp("UpdatedAt").toLocalDateTime());
					user.setUpdatedBy(rs.getInt("UpdatedBy"));
					return user;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null; // Trả về null nếu không tìm thấy người dùng
	}

	// Thêm mới một người dùng
	public void insert(UserEntity user) {
		var query = "INSERT INTO Users (FullName, Username, Password, Email, PhoneNumber, Address, UserRole, Avatar, IsActive, CreatedBy, UpdatedBy) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try (var con = ConnectDB.getCon(); var ps = con.prepareStatement(query)) {

			ps.setString(1, user.getFullName());
			ps.setString(2, user.getUsername());
			ps.setString(3, user.getPassword());
			ps.setString(4, user.getEmail());
			ps.setString(5, user.getPhoneNumber());
			ps.setString(6, user.getAddress());
			ps.setInt(7, user.getUserRole());
			ps.setString(8, user.getAvatar());
			ps.setBoolean(9, user.isActive());
			ps.setInt(10, user.getCreatedBy());
			ps.setInt(11, user.getUpdatedBy());
			ps.executeUpdate();

			System.out.println("User added successfully!");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Xóa một người dùng (xóa mềm, đặt IsDeleted = 1)
	public void delete(int userId) {
		var query = "UPDATE Users SET IsDeleted = 1 WHERE UserID = ?";
		try (var con = ConnectDB.getCon(); var ps = con.prepareStatement(query)) {

			ps.setInt(1, userId);
			ps.executeUpdate();

			System.out.println("User deleted successfully!");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Cập nhật thông tin người dùng
	public void update(UserEntity user) {
		var query = "UPDATE Users SET FullName = ?, Username = ?, Password = ?, Email = ?, PhoneNumber = ?, Address = ?, "
				+ "UserRole = ?, Avatar = ?, IsActive = ?, UpdatedAt = GETDATE(), UpdatedBy = ? WHERE UserID = ?";
		try (var con = ConnectDB.getCon(); var ps = con.prepareStatement(query)) {

			ps.setString(1, user.getFullName());
			ps.setString(2, user.getUsername());
			ps.setString(3, user.getPassword());
			ps.setString(4, user.getEmail());
			ps.setString(5, user.getPhoneNumber());
			ps.setString(6, user.getAddress());
			ps.setInt(7, user.getUserRole());
			ps.setString(8, user.getAvatar());
			ps.setBoolean(9, user.isActive());
			ps.setInt(10, user.getUpdatedBy());
			ps.setInt(11, user.getUserID());
			ps.executeUpdate();


		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// --------------------------Phần của Phú
	// ----------------------------------------------------
	public boolean UserRegister(UserEntity user) {
		var sql = "INSERT INTO dbo.Users (FullName, Username, [Password], Email, PhoneNumber, [Address], UserRole, Avatar, IsActive) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try (var con = ConnectDB.getCon(); var ps = con.prepareStatement(sql)) {

			var hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());

			ps.setString(1, user.getFullName());
			ps.setString(2, user.getEmail() != null ? user.getEmail() : "");
			ps.setString(3, hashedPassword);
			ps.setString(4, user.getEmail());
			ps.setString(5, user.getPhoneNumber() != null ? user.getPhoneNumber() : "");
			ps.setString(6, user.getAddress() != null ? user.getAddress() : "");
			ps.setInt(7, user.getUserRole());
			ps.setString(8, user.getAvatar() != null ? user.getAvatar() : "");
			ps.setBoolean(9, user.isActive());

			var affectedRows = ps.executeUpdate();
			if (affectedRows > 0) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error connecting to the database.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return false;
	}

	public boolean isUsernameExists(String username) {
		var sql = "SELECT COUNT(*) FROM Users WHERE Username = ?";
		try (var con = ConnectDB.getCon(); var ps = con.prepareStatement(sql)) {

			ps.setString(1, username);
			var rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt(1) > 0;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error checking username.", "Error", JOptionPane.ERROR_MESSAGE);
		}
		return false;
	}

	public boolean isEmailExists(String email) {
		var sql = "SELECT COUNT(*) FROM Users WHERE Email = ?";
		try (var con = ConnectDB.getCon(); var ps = con.prepareStatement(sql)) {

			ps.setString(1, email);
			var rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt(1) > 0;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error checking email.", "Error", JOptionPane.ERROR_MESSAGE);
		}
		return false;
	}

	public boolean isPhoneExists(String phone) {
		var sql = "SELECT COUNT(*) FROM Users WHERE PhoneNumber = ?";
		try (var con = ConnectDB.getCon(); var ps = con.prepareStatement(sql)) {

			ps.setString(1, phone);
			var rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt(1) > 0;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error checking email.", "Error", JOptionPane.ERROR_MESSAGE);
		}
		return false;
	}
	public boolean isPasswordExists(String password, String email) {
		var query = "SELECT password FROM Users WHERE Email = ? AND IsActive = 1";
		try (var conn = ConnectDB.getCon(); var ps = conn.prepareStatement(query)) {
			ps.setString(1, email);
			var rs = ps.executeQuery();
			if (rs.next()) {
				var hashedPassword = rs.getString("password");
				var match = BCrypt.checkpw(password, hashedPassword);
				return match;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}

	public UserEntity getUser(String email) {
		var user = new UserEntity();
		var query = "SELECT * FROM users WHERE Email = ?";
		try (var conn = ConnectDB.getCon(); var ps = conn.prepareStatement(query)) {
			ps.setString(1, email);
			var rs = ps.executeQuery();
			if (rs.next()) {
//				var hashedPassword = rs.getString("password");
				user.setUserID(rs.getInt("UserID"));
				user.setFullName(rs.getString("FullName"));
				user.setUsername(rs.getString("Email"));
				user.setEmail(rs.getString("Email"));
				user.setAvatar(rs.getString("Avatar"));
				user.setUserRole(rs.getInt("userRole"));
				user.setActive(rs.getBoolean("IsActive"));
				return user;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

	// Cập nhật thông tin người dùng
	public void updateAvatar(int userID, String avatarPath) {
		var query = "UPDATE Users SET Avatar = ? WHERE UserID = ?";
		try (var con = ConnectDB.getCon(); var ps = con.prepareStatement(query)) {

			ps.setString(1, avatarPath);
			ps.setInt(2, userID);
			ps.executeUpdate();


		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Cập nhật trạng thái duyệt tài khoản
	public boolean updateUserStatus(int userId, boolean isActive) {
	    // Truy vấn cập nhật trạng thái
	    String query = "UPDATE Users SET IsActive = ? WHERE UserID = ?";
	    try (var con = ConnectDB.getCon(); var ps = con.prepareStatement(query)) {
	        ps.setBoolean(1, isActive);
	        ps.setInt(2, userId);
	        int rowsUpdated = ps.executeUpdate();
	        return rowsUpdated > 0; // Trả về true nếu có ít nhất một dòng được cập nhật
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false; // Trả về false nếu có lỗi
	    }
	}


	public List<UserEntity> selectDeletedUser() {
		List<UserEntity> list = new ArrayList<>();
		try (var con = ConnectDB.getCon();
				var ps = con.prepareStatement("SELECT * FROM Users WHERE IsDeleted = 1 ORDER BY UserID DESC");
				var rs = ps.executeQuery()) {
			while (rs.next()) {
				var user = new UserEntity();

				user.setFullName(rs.getString("FullName"));
				user.setUsername(rs.getString("Email"));
				user.setEmail(rs.getString("Email"));
				user.setPhoneNumber(rs.getString("PhoneNumber"));
				user.setAddress(rs.getString("Address"));
				user.setUserRole(rs.getInt("UserRole")); // 1: Admin, 0: User

				user.setUserID(rs.getInt("UserID"));
				list.add(user);
//	            JOptionPane.showMessageDialog(null, user);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public void updatePassword(String email, String hashedPassword) {
		try (var con = ConnectDB.getCon();
				var ps = con.prepareStatement("UPDATE users SET password = ? WHERE email = ?")) {
			ps.setString(1, hashedPassword); // Đặt mật khẩu đã mã hóa
			ps.setString(2, email);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
