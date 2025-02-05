package entity;

import java.time.LocalDateTime;

public class UserEntity {
	private int userID; // ID của người dùng
	private String fullName; // Họ và tên
	private String username; // Tên đăng nhập
	private String password; // Mật khẩu
	private String email; // Email
	private String phoneNumber; // Số điện thoại
	private String address; // Địa chỉ
	private int userRole; // Vai trò (1: Nhân viên, 2: Admin)
	private String avatar; // Đường dẫn ảnh đại diện
	private boolean isActive; // Trạng thái hoạt động
	private boolean isDeleted; // Trạng thái xóa
	private LocalDateTime createdAt; // Thời gian tạo
	private int createdBy; // Người tạo
	private LocalDateTime updatedAt; // Thời gian cập nhật
	private int updatedBy; // Người cập nhật

	// Constructor mặc định
	public UserEntity() {
		super();
	}

	// Constructor đầy đủ tham số
	public UserEntity(int userID, String fullName, String username, String password, String email, String phoneNumber,
			String address, int userRole, String avatar, boolean isActive, boolean isDeleted, LocalDateTime createdAt,
			int createdBy, LocalDateTime updatedAt, int updatedBy) {
		super();
		this.userID = userID;
		this.fullName = fullName;
		this.username = username;
		this.password = password;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.userRole = userRole;
		this.avatar = avatar;
		this.isActive = isActive;
		this.isDeleted = isDeleted;
		this.createdAt = createdAt;
		this.createdBy = createdBy;
		this.updatedAt = updatedAt;
		this.updatedBy = updatedBy;
	}

	// Getter và Setter
	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getUserRole() {
		return userRole;
	}

	public void setUserRole(int userRole) {
		this.userRole = userRole;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public int getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}

	// Phương thức toString
	@Override
	public String toString() {
		return "UserEntity{" + "userID=" + userID + ", fullName='" + fullName + '\'' + ", username='" + username + '\''
				+ ", password='" + password + '\'' + ", email='" + email + '\'' + ", phoneNumber='" + phoneNumber + '\''
				+ ", address='" + address + '\'' + ", userRole=" + userRole + ", avatar='" + avatar + '\''
				+ ", isActive=" + isActive + ", isDeleted=" + isDeleted + ", createdAt=" + createdAt + ", createdBy="
				+ createdBy + ", updatedAt=" + updatedAt + ", updatedBy=" + updatedBy + '}';
	}
}
