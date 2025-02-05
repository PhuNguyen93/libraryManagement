package dao;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import entity.StudentEntity;
import service.ConnectDB;

public class StudentDao {

	// Lấy toàn bộ dữ liệu từ bảng Students
	public List<StudentEntity> select() {
		List<StudentEntity> list = new ArrayList<>();
		try (var con = ConnectDB.getCon();
				var ps = con.prepareStatement("""
						SELECT * ,
						CASE (SELECT TOP 1 1 FROM Payments P WHERE P.StudentID= Students.StudentID)
						WHEN 1 THEN 1
						ELSE 0 
						END AS 'isPayment'
						FROM Students 
						WHERE IsDeleted = 0 ORDER BY StudentID DESC
						""");
				var rs = ps.executeQuery();) {
			while (rs.next()) {
				var student = new StudentEntity();
				student.setStudentID(rs.getInt("StudentID"));
				student.setStudentCode(rs.getString("StudentCode")); // Lấy mã sinh viên
				student.setAvatar(rs.getString("Avatar"));
				student.setFullName(rs.getString("FullName"));
				student.setDateOfBirth(rs.getDate("DateOfBirth").toLocalDate());
				student.setGender(rs.getString("Gender"));
				student.setEmail(rs.getString("Email"));
				student.setPhoneNumber(rs.getString("PhoneNumber"));
				student.setAddress(rs.getString("Address"));
				student.setEnrollmentYear(rs.getInt("GraduationYear"));
				student.setSchoolName(rs.getString("SchoolName"));
				student.setUserID(rs.getInt("UserID"));
				student.setTotalBooksRented(rs.getInt("TotalBooksRented")); // Lấy số lượng sách đã thuê
				student.setLateReturnsCount(rs.getInt("LateReturnsCount")); // Lấy số lần trả sách trễ
				student.setDamagedBooksCount(rs.getInt("DamagedBooksCount")); // Lấy số lần sách hư hại
				student.setTotalOrders(rs.getInt("TotalOrders")); // Lấy tổng số đơn
				student.setDeleted(rs.getBoolean("IsDeleted"));
				student.setPayment(rs.getBoolean("isPayment"));
				list.add(student);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<StudentEntity> selectValidCard() {
		List<StudentEntity> list = new ArrayList<>();
		try (var con = ConnectDB.getCon();
				var ps = con.prepareStatement(
						"SELECT * FROM Students WHERE GraduationYear >= YEAR(GETDATE()) and IsDeleted = 0 ORDER BY StudentID DESC");
				var rs = ps.executeQuery();) {
			while (rs.next()) {
				var student = new StudentEntity();
				student.setStudentID(rs.getInt("StudentID"));
				student.setStudentCode(rs.getString("StudentCode")); // Lấy mã sinh viên
				student.setAvatar(rs.getString("Avatar"));
				student.setFullName(rs.getString("FullName"));
				student.setDateOfBirth(rs.getDate("DateOfBirth").toLocalDate());
				student.setGender(rs.getString("Gender"));
				student.setEmail(rs.getString("Email"));
				student.setPhoneNumber(rs.getString("PhoneNumber"));
				student.setAddress(rs.getString("Address"));
				student.setEnrollmentYear(rs.getInt("GraduationYear"));
				student.setSchoolName(rs.getString("SchoolName"));
				student.setUserID(rs.getInt("UserID"));
				student.setTotalBooksRented(rs.getInt("TotalBooksRented")); // Lấy số lượng sách đã thuê
				student.setLateReturnsCount(rs.getInt("LateReturnsCount")); // Lấy số lần trả sách trễ
				student.setDamagedBooksCount(rs.getInt("DamagedBooksCount")); // Lấy số lần sách hư hại
				student.setTotalOrders(rs.getInt("TotalOrders")); // Lấy tổng số đơn
				student.setDeleted(rs.getBoolean("IsDeleted"));

				list.add(student);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	// Thêm một sinh viên mới vào bảng Students
	public void insert(StudentEntity student) {
		try (var con = ConnectDB.getCon();
				var ps = con.prepareStatement(
						"INSERT INTO Students (StudentCode, Avatar, FullName, DateOfBirth, Gender, Email, PhoneNumber, Address, GraduationYear, SchoolName, UserID, TotalBooksRented, LateReturnsCount, DamagedBooksCount, TotalOrders, IsDeleted) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");) {
			ps.setString(1, student.getStudentCode()); // Thêm mã sinh viên
			ps.setString(2, student.getAvatar());
			ps.setString(3, student.getFullName());
			ps.setDate(4, java.sql.Date.valueOf(student.getDateOfBirth()));
			ps.setString(5, student.getGender());
			ps.setString(6, student.getEmail());
			ps.setString(7, student.getPhoneNumber());
			ps.setString(8, student.getAddress());
			ps.setInt(9, student.getEnrollmentYear());
			ps.setString(10, student.getSchoolName());
			ps.setInt(11, student.getUserID());
			ps.setInt(12, student.getTotalBooksRented()); // Thêm số lượng sách đã thuê
			ps.setInt(13, student.getLateReturnsCount()); // Thêm số lần trả sách trễ
			ps.setInt(14, student.getDamagedBooksCount()); // Thêm số lần sách hư hại
			ps.setInt(15, student.getTotalOrders()); // Thêm tổng số đơn
			ps.setBoolean(16, student.isDeleted());
			ps.executeUpdate();
			JOptionPane.showMessageDialog(null, "Insert success");
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Insert failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void delete(int studentID) {
	    try (var con = ConnectDB.getCon();
	         var ps = con.prepareCall("{call deleteStu(?)}")) { // Chỉ truyền 1 tham số
	        ps.setInt(1, studentID); // Truyền StudentID
	        ps.executeUpdate(); // Thực thi stored procedure
	        JOptionPane.showMessageDialog(null, "Student marked as deleted successfully!");
	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new RuntimeException("Failed to mark student as deleted: " + e.getMessage());
	    }
	}


	public String getUserNameById(int userId) {
		var userName = "Unknown"; // Mặc định nếu không tìm thấy
		try (var con = ConnectDB.getCon();
				var ps = con.prepareStatement("SELECT UserName FROM Users WHERE UserID = ?")) {
			ps.setInt(1, userId);
			try (var rs = ps.executeQuery()) {
				if (rs.next()) {
					userName = rs.getString("UserName");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userName;
	}

	public StudentEntity getStudentById(int studentID) {
		var student = new StudentEntity(); // Mặc định nếu không tìm thấy
		try (var con = ConnectDB.getCon();
				var ps = con.prepareStatement("SELECT * FROM Students WHERE StudentID = ?")) {
			ps.setInt(1, studentID);
			try (var rs = ps.executeQuery()) {
				if (rs.next()) {
					student.setFullName(rs.getString("FullName"));
					student.setStudentCode(rs.getString("StudentCode"));
					student.setPhoneNumber(rs.getString("PhoneNumber"));
					student.setEmail(rs.getString("Email"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return student;
	}

	public StudentEntity selectByStudentCd(String studentcd) {
		StudentEntity student = null;
		try (var con = ConnectDB.getCon();
				var ps = con.prepareStatement("SELECT * FROM Students WHERE StudentCode = ?")) {
			ps.setString(1, studentcd);
			try (var rs = ps.executeQuery()) {
				if (rs.next()) {
					student = new StudentEntity();
					student.setStudentID(rs.getInt("StudentID"));
					student.setStudentCode(rs.getString("StudentCode"));
					student.setAvatar(rs.getString("Avatar"));
					student.setFullName(rs.getString("FullName"));
					student.setDateOfBirth(
							rs.getDate("DateOfBirth") != null ? rs.getDate("DateOfBirth").toLocalDate() : null);
					student.setGender(rs.getString("Gender"));
					student.setEmail(rs.getString("Email"));
					student.setPhoneNumber(rs.getString("PhoneNumber"));
					student.setAddress(rs.getString("Address"));
					student.setEnrollmentYear(rs.getInt("GraduationYear"));
					student.setSchoolName(rs.getString("SchoolName"));
					student.setUserID(rs.getInt("UserID"));
					student.setTotalBooksRented(rs.getInt("TotalBooksRented"));
					student.setLateReturnsCount(rs.getInt("LateReturnsCount"));
					student.setDamagedBooksCount(rs.getInt("DamagedBooksCount"));
					student.setTotalOrders(rs.getInt("TotalOrders"));
					student.setDeleted(rs.getBoolean("IsDeleted"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return student;
	}

	public boolean updateStudentStatistics(StudentEntity student, int userid) {
		// Câu lệnh SQL để cập nhật thông tin thống kê
		var sql = """
				    UPDATE Students
				    SET
				        TotalBooksRented = ?,
				        LateReturnsCount = ?,
				        DamagedBooksCount = ?,
				        TotalOrders = ?,
				        UpdatedAt = GETDATE(),
				        UpdatedBy = ?
				    WHERE StudentID = ? AND IsDeleted = 0
				""";

		try (var conn = ConnectDB.getCon(); var stmt = conn.prepareStatement(sql)) {

			// Gán giá trị cho các tham số
			stmt.setInt(1, student.getTotalBooksRented());
			stmt.setInt(2, student.getLateReturnsCount());
			stmt.setInt(3, student.getDamagedBooksCount());
			stmt.setInt(4, student.getTotalOrders());
			stmt.setInt(5, userid); // ID người cập nhật
			stmt.setInt(6, student.getStudentID()); // ID của sinh viên

			// Thực thi cập nhật
			var rowsAffected = stmt.executeUpdate();

			// Trả về true nếu cập nhật thành công
			return rowsAffected > 0;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// Phương thức update sử dụng stored procedure updateStudent
	public void update(StudentEntity student, int updatedBy) {
		try (var con = ConnectDB.getCon();
				var ps = con.prepareStatement("{call updateStudent(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}")) {

			ps.setInt(1, student.getStudentID());
			ps.setString(2, student.getFullName());
			ps.setDate(3, student.getDateOfBirth() != null ? Date.valueOf(student.getDateOfBirth()) : null);
			ps.setString(4, student.getGender());
			ps.setString(5, student.getEmail());
			ps.setString(6, student.getPhoneNumber());
			ps.setString(7, student.getAddress());
			ps.setString(8, student.getAvatar());
			ps.setInt(9, student.getEnrollmentYear());
			ps.setString(10, student.getSchoolName());
			ps.setInt(11, updatedBy);
			ps.setInt(12, student.getTotalBooksRented());
			ps.setInt(13, student.getLateReturnsCount());
			ps.setInt(14, student.getDamagedBooksCount());
			ps.setInt(15, student.getTotalOrders());
			ps.setString(16, student.getStudentCode());

			ps.executeUpdate();
			JOptionPane.showMessageDialog(null, "Update success!");
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Update failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	public boolean isStudentCodeExists(String studentCode) {
		var query = "SELECT COUNT(*) FROM Students WHERE UPPER(StudentCode) = ? AND IsDeleted = 0";
		try (var con = ConnectDB.getCon(); var ps = con.prepareStatement(query)) {
			ps.setString(1, studentCode);
			try (var rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1) > 0; // Nếu COUNT > 0, nghĩa là StudentCode đã tồn tại
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false; // Mặc định trả về false nếu có lỗi
	}

	public boolean isEmailExists(String email) {
		var query = "SELECT COUNT(*) FROM Students WHERE LOWER(Email) = ? AND IsDeleted = 0";
		try (var con = ConnectDB.getCon(); var ps = con.prepareStatement(query)) {
			ps.setString(1, email); // Chuyển email sang dạng chữ thường để kiểm tra không phân biệt hoa/thường
			try (var rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1) > 0; // Trả về true nếu COUNT > 0
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false; // Mặc định trả về false nếu có lỗi
	}

	public List<StudentEntity> selectDeletedStudents() {
	    List<StudentEntity> list = new ArrayList<>();
	    try (var con = ConnectDB.getCon();
	         var ps = con.prepareStatement("SELECT * FROM Students WHERE IsDeleted = 1 ORDER BY StudentID DESC");
	         var rs = ps.executeQuery()) {
	        while (rs.next()) {
	            var student = new StudentEntity();
	            student.setAvatar(rs.getString("Avatar")); // Gán giá trị Avatar
	            student.setFullName(rs.getString("FullName")); // Gán giá trị FullName
	            student.setDateOfBirth(rs.getDate("DateOfBirth") != null ? rs.getDate("DateOfBirth").toLocalDate() : null); // Xử lý NULL
	            student.setGender(rs.getString("Gender")); // Gán giá trị Gender
	            student.setEmail(rs.getString("Email")); // Gán giá trị Email
	            student.setPhoneNumber(rs.getString("PhoneNumber")); // Gán giá trị PhoneNumber
	            student.setAddress(rs.getString("Address")); // Gán giá trị Address
	            student.setSchoolName(rs.getString("SchoolName")); // Gán giá trị SchoolName
	            student.setStudentID(rs.getInt("StudentID")); // Gán giá trị StudentID
	            list.add(student);
	        }
	    } catch (Exception e) {
	        System.err.println("Error fetching deleted students: " + e.getMessage());
	        e.printStackTrace();
	    }
	    return list;
	}


	public int countStudentsByUserId(int userId) {
		var count = 0;
		var query = "SELECT COUNT(*) FROM Students WHERE UserID = ? AND IsDeleted = 0";
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
	public boolean isPhoneNumberExists(String phoneNumber, int studentID) {
	    String query = "SELECT COUNT(*) FROM Students WHERE PhoneNumber = ? AND StudentID != ? AND IsDeleted = 0";
	    try (var con = ConnectDB.getCon(); 
	         var ps = con.prepareStatement(query)) { // Đổi "preparedStatement" thành "ps"
	        ps.setString(1, phoneNumber); // Sử dụng ps thay vì preparedStatement
	        ps.setInt(2, studentID);

	        try (var resultSet = ps.executeQuery()) { // Đổi "preparedStatement" thành "ps"
	            if (resultSet.next()) {
	                return resultSet.getInt(1) > 0; // Trả về true nếu tìm thấy số điện thoại trùng
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return false; // Trả về false nếu không có lỗi hoặc không tìm thấy trùng lặp
	}

}
