package edit;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import org.mindrot.jbcrypt.BCrypt;

import dao.UserDao;
import entity.UserSession;

public class ChangePasswordDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	private JPasswordField oldPasswordField;
	private JPasswordField newPasswordField;
	private JPasswordField confirmPasswordField;
	private JButton btnChangePassword;
	private UserDao userDao;

	public ChangePasswordDialog() {
		setTitle("Change Password");
		setModal(true);
		setSize(400, 300);
		setLocationRelativeTo(null);
		setLayout(null);

		var lblOldPassword = new JLabel("Old Password:");
		lblOldPassword.setBounds(50, 50, 120, 25);
		add(lblOldPassword);

		oldPasswordField = new JPasswordField();
		oldPasswordField.setBounds(180, 50, 150, 25);
		add(oldPasswordField);

		var lblNewPassword = new JLabel("New Password:");
		lblNewPassword.setBounds(50, 100, 120, 25);
		add(lblNewPassword);

		newPasswordField = new JPasswordField();
		newPasswordField.setBounds(180, 100, 150, 25);
		add(newPasswordField);

		var lblConfirmPassword = new JLabel("Confirm Password:");
		lblConfirmPassword.setBounds(50, 150, 120, 25);
		add(lblConfirmPassword);

		confirmPasswordField = new JPasswordField();
		confirmPasswordField.setBounds(180, 150, 150, 25);
		add(confirmPasswordField);

		btnChangePassword = new JButton("Change Password");
		btnChangePassword.setBounds(100, 200, 180, 30);
		add(btnChangePassword);

		btnChangePassword.addActionListener(e -> handleChangePassword());

		userDao = new UserDao();
	}
	private void handleChangePassword() {
	    var userSession = UserSession.getInstance();
	    var oldPassword = new String(oldPasswordField.getPassword()).trim();
	    var newPassword = new String(newPasswordField.getPassword()).trim();
	    var confirmPassword = new String(confirmPasswordField.getPassword()).trim();

	    // 1. Kiểm tra các trường không được để trống
	    if (oldPassword.isEmpty()) {
	        JOptionPane.showMessageDialog(this, "Old Password cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
	        oldPasswordField.requestFocus();
	        return;
	    }
	    if (newPassword.isEmpty()) {
	        JOptionPane.showMessageDialog(this, "New Password cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
	        newPasswordField.requestFocus();
	        return;
	    }
	    if (confirmPassword.isEmpty()) {
	        JOptionPane.showMessageDialog(this, "Confirm Password cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
	        confirmPasswordField.requestFocus();
	        return;
	    }

	    // 2. Kiểm tra mật khẩu mới tuân theo quy tắc
	    if (newPassword.length() < 6 ||
	        !newPassword.matches(".*[A-Z].*") || // Chứa ít nhất một chữ in hoa
	        !newPassword.matches(".*[a-z].*") || // Chứa ít nhất một chữ thường
	        !newPassword.matches(".*\\d.*") ||   // Chứa ít nhất một số
	        !newPassword.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) { // Chứa ít nhất một ký tự đặc biệt
	        JOptionPane.showMessageDialog(this, 
	            "New Password must be at least 6 characters long and include an uppercase letter, a lowercase letter, a number, and a special character.",
	            "Error", JOptionPane.ERROR_MESSAGE);
	        newPasswordField.requestFocus();
	        return;
	    }

	    // 3. Kiểm tra xác nhận mật khẩu (Confirm Password)
	    if (!newPassword.equals(confirmPassword)) {
	        JOptionPane.showMessageDialog(this, "New passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
	        confirmPasswordField.requestFocus();
	        return;
	    }

	    // 4. Kiểm tra mật khẩu cũ chính xác
	    if (!userDao.isPasswordExists(oldPassword, userSession.getEmail())) {
	        JOptionPane.showMessageDialog(this, "Old password is incorrect!", "Error", JOptionPane.ERROR_MESSAGE);
	        oldPasswordField.requestFocus();
	        return;
	    }

	    // 5. Hash mật khẩu mới và cập nhật vào cơ sở dữ liệu
	    var hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
	    userDao.updatePassword(userSession.getEmail(), hashedPassword);

	    // 6. Hiển thị thông báo thành công và đóng dialog
	    JOptionPane.showMessageDialog(this, "Password changed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
	    dispose();
	}

}