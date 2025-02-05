package entity;

public class UserSession {
	private static UserSession instance; // Instance duy nhất của class

	private String username; // Lưu username của user
	private int userId; // Lưu userId của user
	private String avatar; // Lưu đường dẫn đến avatar của user
	private int userRole;
	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getUserRole() {
		return userRole;
	}

	public void setUserRole(int userRole) {
		this.userRole = userRole;
	}

	// Constructor private để ngăn việc tạo instance từ bên ngoài
	private UserSession() {
	}

	// Phương thức để lấy instance duy nhất của UserSession
	public static UserSession getInstance() {
		if (instance == null) {
			instance = new UserSession();
		}
		return instance;
	}

	// Getters và Setters cho thông tin user
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	// Reset session (nếu cần logout user)
	public void clearSession() {
		username = null;
		userId = 0;
		avatar = null;
		email = null;
	}
}