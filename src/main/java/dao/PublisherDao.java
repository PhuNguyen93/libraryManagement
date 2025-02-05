package dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.swing.JOptionPane;

import entity.ComboItem;
import entity.PublisherEntity;
import service.ConnectDB;

public class PublisherDao {

	// Lấy toàn bộ dữ liệu từ bảng Publishers
	public List<PublisherEntity> select() {
		List<PublisherEntity> list = new ArrayList<>();
		try (var con = ConnectDB.getCon();
				var ps = con.prepareStatement("SELECT * FROM Publishers WHERE IsDeleted = 0 ORDER BY PublisherID DESC");
				var rs = ps.executeQuery()) {
			while (rs.next()) {
				var publisher = new PublisherEntity();
				publisher.setPublisherID(rs.getInt("PublisherID"));
				publisher.setName(rs.getString("Name"));
				publisher.setAddress(rs.getString("Address"));
				publisher.setPhoneNumber(rs.getString("PhoneNumber"));
				publisher.setEmail(rs.getString("Email"));
				publisher.setIsDeleted(rs.getBoolean("IsDeleted"));
				list.add(publisher);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	// Thêm một nhà xuất bản mới vào bảng Publishers
	public void insert(PublisherEntity publisher) {
		try (var con = ConnectDB.getCon();
				var ps = con.prepareStatement(
						"INSERT INTO Publishers (Name, Address, PhoneNumber, Email, IsDeleted) VALUES (?, ?, ?, ?, ?)");) {
			ps.setString(1, publisher.getName());
			ps.setString(2, publisher.getAddress());
			ps.setString(3, publisher.getPhoneNumber());
			ps.setString(4, publisher.getEmail());
			ps.setBoolean(5, publisher.isIsDeleted());
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Cập nhật thông tin nhà xuất bản
	public void update(PublisherEntity publisher) {
		try (var con = ConnectDB.getCon();
				var ps = con.prepareStatement(
						"UPDATE Publishers SET Name = ?, Address = ?, PhoneNumber = ?, Email = ?, IsDeleted = ? WHERE PublisherID = ?");) {
			ps.setString(1, publisher.getName());
			ps.setString(2, publisher.getAddress());
			ps.setString(3, publisher.getPhoneNumber());
			ps.setString(4, publisher.getEmail());
			ps.setBoolean(5, publisher.isIsDeleted());
			ps.setInt(6, publisher.getPublisherID());
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Xóa logic một nhà xuất bản (đặt IsDeleted = 1)
	public void delete(int publisherID, int updatedBy) {
		try (var con = ConnectDB.getCon(); var ps = con.prepareStatement("{call DeletePublisher(?, ?)}")) { // Gọi
																											// stored
																											// procedure
			ps.setInt(1, publisherID);
			ps.setInt(2, updatedBy);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Delete failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}


	// Lấy thông tin chi tiết một nhà xuất bản theo ID
	public PublisherEntity selectById(int publisherID) {
		PublisherEntity publisher = null;
		try (var con = ConnectDB.getCon();
				var ps = con.prepareStatement("SELECT * FROM Publishers WHERE PublisherID = ? AND IsDeleted = 0")) {
			ps.setInt(1, publisherID);
			try (var rs = ps.executeQuery()) {
				if (rs.next()) {
					publisher = new PublisherEntity();
					publisher.setPublisherID(rs.getInt("PublisherID"));
					publisher.setName(rs.getString("Name"));
					publisher.setAddress(rs.getString("Address"));
					publisher.setPhoneNumber(rs.getString("PhoneNumber"));
					publisher.setEmail(rs.getString("Email"));
					publisher.setIsDeleted(rs.getBoolean("IsDeleted"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return publisher;
	}

	// Lấy danh sách tên tất cả nhà xuất bản
	public List<String> getAllPublisherNames() {
		List<String> publisherNames = new ArrayList<>();
		try (var con = ConnectDB.getCon();
				var ps = con.prepareStatement("SELECT Name FROM Publishers WHERE IsDeleted = 0");
				var rs = ps.executeQuery();) {
			while (rs.next()) {
				publisherNames.add(rs.getString("Name"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return publisherNames;
	}

	public List<ComboItem> getAllPublishers() {
		List<ComboItem> publishers = new ArrayList<>();
		var sql = "SELECT PublisherID, Name FROM Publishers where IsDeleted = 0";
		try (var con = ConnectDB.getCon(); var stmt = con.prepareStatement(sql);
				var rs = stmt.executeQuery()) {
			while (rs.next()) {
				publishers.add(new ComboItem(rs.getInt("PublisherID"), rs.getString("Name")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return publishers;
	}

	public List<PublisherEntity> selectDeletedPublishers() {
		List<PublisherEntity> list = new ArrayList<>();
		try (var con = ConnectDB.getCon();
				var ps = con.prepareStatement("""
						SELECT * ,
						CASE (SELECT TOP 1 1 FROM Books b where b.PublisherID = Publishers.PublisherID )
						WHEN 1 THEN 1
						ELSE 0 
						END AS 'isSelected'
						FROM Publishers WHERE IsDeleted = 1 ORDER BY PublisherID DESC
						""");
				var rs = ps.executeQuery()) {
			while (rs.next()) {
				var publisher = new PublisherEntity();
				publisher.setPublisherID(rs.getInt("PublisherID")); // Gán giá trị PublisherID
				publisher.setName(rs.getString("Name")); // Gán giá trị Name
				publisher.setAddress(rs.getString("Address")); // Gán giá trị Address
				publisher.setPhoneNumber(rs.getString("PhoneNumber")); // Gán giá trị PhoneNumber
				publisher.setEmail(rs.getString("Email")); // Gán giá trị Email
				publisher.setIsDeleted(rs.getBoolean("IsDeleted")); // Gán trạng thái IsDeleted
				publisher.setSelected(rs.getBoolean("isSelected"));
				list.add(publisher);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	// Lấy số lượng sách liên kết với từng nhà xuất bản
	public Map<Integer, Integer> getBookCountByPublisher() {
		var bookCounts = new HashMap<Integer, Integer>();

		var sql = "SELECT PublisherID, COUNT(*) AS BookCount FROM Books WHERE IsDeleted = 0 GROUP BY PublisherID";
		try (var con = ConnectDB.getCon(); var ps = con.prepareStatement(sql); var rs = ps.executeQuery()) {

			while (rs.next()) {
				bookCounts.put(rs.getInt("PublisherID"), rs.getInt("BookCount"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return bookCounts; // Trả về danh sách số lượng sách của từng nhà xuất bản
	}
	public Optional<String> getPublisherName(int publisherID) {
	    String query = "SELECT Name FROM Publishers WHERE PublisherID = ? AND IsDeleted = 0";
	    try (var con = ConnectDB.getCon(); var ps = con.prepareStatement(query)) {
	        ps.setInt(1, publisherID);
	        try (var rs = ps.executeQuery()) {
	            if (rs.next()) {
	                return Optional.of(rs.getString("Name"));
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return Optional.empty(); // Trả về Optional rỗng nếu không tìm thấy
	}

}
