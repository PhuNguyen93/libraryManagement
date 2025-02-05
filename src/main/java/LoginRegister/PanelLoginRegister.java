package LoginRegister;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import component.Menu;
import dao.UserDao;
import entity.UserEntity;
import entity.UserSession;
import gui.Main;
import net.miginfocom.swing.MigLayout;

public class PanelLoginRegister extends JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private JPanel register;
	private JTextField txtFullName; // Biến lớp cho trường Full Name
	private JTextField txtUsername; // Biến lớp cho trường Username
	private JTextField txtEmail; // Biến lớp cho trường Email
	private JPasswordField txtPassword; // Biến lớp cho trường Password
	private JTextField txtLoginEmail; // Trường Email trong Login
	private JPasswordField txtLoginPassword; // Trường Password trong Login
	private UserDao userDao;
	private JPanel login;
	private JTextField txtPhoneNumber;
	private JTextField txtAddress;
	private JPasswordField txtConfirmPassword;
	private JButton btnLogin;

	public PanelLoginRegister() {
		setLayout(new CardLayout());
		userDao = new UserDao();
		initLoginPanel();
		initRegisterPanel();
		showLogin();
	}

	private void initLoginPanel() {
		login = new JPanel();
		login.setBackground(Color.WHITE);
		login.setLayout(null);

		var label = new JLabel("Sign In");
		label.setForeground(new Color(128, 128, 192));
		label.setBounds(233, 77, 109, 37);
		label.setFont(label.getFont().deriveFont(label.getFont().getStyle() | Font.BOLD, 30f));
		login.add(label);

		// Tạo txtLoginEmail
		txtLoginEmail = new JTextField("Email");
		txtLoginEmail.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					btnLogin.doClick(); // Kích hoạt nút Login
				}
			}
		});
		txtLoginEmail.setBorder(null);
		txtLoginEmail.setBounds(118, 139, 353, 36);
		txtLoginEmail.addFocusListener(new java.awt.event.FocusListener() {
			@Override
			public void focusGained(java.awt.event.FocusEvent e) {
				// Khi click vào txtLoginEmail
				if (txtLoginEmail.getText().equals("Email")) {
					txtLoginEmail.setText(""); // Xóa text placeholder
					txtLoginEmail.setForeground(Color.BLACK); // Chuyển màu text thành đen
				}
			}

			@Override
			public void focusLost(java.awt.event.FocusEvent e) {
				// Khi click ra ngoài txtLoginEmail
				if (txtLoginEmail.getText().trim().isEmpty()) {
					txtLoginEmail.setText("Email"); // Hiển thị lại placeholder
					txtLoginEmail.setForeground(Color.GRAY); // Chuyển màu text thành xám
				}
			}
		});
		txtLoginEmail.setForeground(Color.GRAY);
		txtLoginEmail.setBackground(new Color(236, 236, 255)); // Đặt cùng màu nền với panel
		txtLoginEmail.setFont(new Font("Arial", Font.PLAIN, 14));
		login.add(txtLoginEmail);

		// Tạo emailField với icon và txtLoginEmail
		var emailField = createStyledField(new ImageIcon(Menu.class.getResource("/icon10/email.png")), "Email");
		emailField.add(txtLoginEmail, BorderLayout.CENTER); // Thêm txtLoginEmail vào giữa
		emailField.setBounds(118, 139, 353, 36);
		login.add(emailField);

		var PASSWORD_PLACEHOLDER = "Password";

		txtLoginPassword = new JPasswordField(PASSWORD_PLACEHOLDER);
		txtLoginPassword.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				txtLoginPasswordKeyPressed(e);
			}
		});
		txtLoginPassword.setEchoChar((char) 0); // Tắt ký tự thay thế (hiển thị text bình thường)

		txtLoginPassword.addFocusListener(new java.awt.event.FocusListener() {
			@Override
			public void focusGained(java.awt.event.FocusEvent e) {
				// Khi click vào txtLoginEmail
				if (new String(txtLoginPassword.getPassword()).equals(PASSWORD_PLACEHOLDER)) {
					txtLoginPassword.setText(""); // Xóa text placeholder
					txtLoginPassword.setForeground(Color.BLACK); // Chuyển màu text thành đen
					txtLoginPassword.setEchoChar('●'); // Hiển thị ký tự thay thế (●) khi nhập mật khẩu
				}
			}

			@Override
			public void focusLost(java.awt.event.FocusEvent e) {
				// Khi click ra ngoài txtLoginEmail
				if (new String(txtLoginPassword.getPassword()).trim().isEmpty()) {
					txtLoginPassword.setText("Password"); // Hiển thị lại placeholder
					txtLoginPassword.setForeground(Color.GRAY); // Chuyển màu text thành xám
				}
			}
		});
		txtLoginPassword.setForeground(Color.GRAY);
		txtLoginPassword.setBackground(new Color(236, 236, 255)); // Đặt cùng màu nền với panel
		txtLoginPassword.setFont(new Font("Arial", Font.PLAIN, 14));

		txtLoginPassword.setBounds(118, 185, 353, 36);
		txtLoginPassword.setBorder(null);
		login.add(txtLoginPassword);

		var passwordlField = createStyledField(new ImageIcon(Menu.class.getResource("/icon10/locked-computer.png")),
				"Password");
		passwordlField.add(txtLoginPassword, BorderLayout.CENTER); // Thêm txtLoginEmail vào giữa
		passwordlField.setBounds(118, 185, 353, 36);
		login.add(passwordlField);

		btnLogin = new JButton("SIGN IN") {
			/**
			 *
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
				var g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

				// Vẽ nền bo tròn
				g2.setColor(getBackground());
				g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40); // Bo góc 40px

				// Vẽ text
				super.paintComponent(g2);
				g2.dispose();
			}

			@Override
			protected void paintBorder(Graphics g) {
				// Không vẽ viền
			}
		};

		btnLogin.setContentAreaFilled(false); // Xóa nền mặc định
		btnLogin.setFocusPainted(false); // Xóa hiệu ứng focus
		btnLogin.setBorder(null); // Xóa viền mặc định
		btnLogin.setForeground(Color.WHITE); // Màu chữ
		btnLogin.setBackground(new Color(128, 128, 192)); // Màu nền xanh ngọc
		btnLogin.setBounds(190, 294, 200, 40);
		btnLogin.setFont(new Font("Arial", Font.BOLD, 16)); // Font chữ to, đậm
		btnLogin.addActionListener(this::btnLoginActionPerformed);
		login.add(btnLogin);

		var btnForgotPassword = new JButton("Forgot Your Password?");
		btnForgotPassword.setForeground(new Color(128, 128, 128));
		btnForgotPassword.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnForgotPassword.setBorder(null);
		btnForgotPassword.setBackground(new Color(255, 255, 255));
		btnForgotPassword.setBounds(160, 232, 236, 40);
		btnForgotPassword.addActionListener(e -> showForgotPasswordDialog());
		login.add(btnForgotPassword);

		add(login, "login");
	}

	private void initRegisterPanel() {
		register = new JPanel();
		register.setBackground(Color.WHITE);
		register.setLayout(null);

		var label = new JLabel("Create Account");
		label.setForeground(new Color(128, 128, 192));
		label.setBounds(176, 77, 257, 37);
		label.setFont(label.getFont().deriveFont(Font.BOLD, 30f));
		register.add(label);

		// Trường Email
		txtEmail = new JTextField("Email");
		txtEmail.setBorder(null);
		txtEmail.setForeground(Color.GRAY);
		txtEmail.setBackground(new Color(236, 236, 255));
		txtEmail.setFont(new Font("Arial", Font.PLAIN, 14));
		txtEmail.setBounds(118, 139, 353, 36);
		txtEmail.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(java.awt.event.FocusEvent e) {
				if (txtEmail.getText().equals("Email")) {
					txtEmail.setText("");
					txtEmail.setForeground(Color.BLACK);
				}
			}

			@Override
			public void focusLost(java.awt.event.FocusEvent e) {
				if (txtEmail.getText().trim().isEmpty()) {
					txtEmail.setText("Email");
					txtEmail.setForeground(Color.GRAY);
				}
			}
		});
		var emailField = createStyledField(new ImageIcon(Menu.class.getResource("/icon10/email.png")), "Email");
		emailField.add(txtEmail, BorderLayout.CENTER);
		emailField.setBounds(118, 139, 353, 36);
		register.add(emailField);

		// Trường Password
		txtPassword = new JPasswordField("Password");
		txtPassword.setBorder(null);
		txtPassword.setForeground(Color.GRAY);
		txtPassword.setBackground(new Color(236, 236, 255));
		txtPassword.setFont(new Font("Arial", Font.PLAIN, 14));
		txtPassword.setBounds(118, 185, 353, 36);
		txtPassword.setEchoChar((char) 0);
		txtPassword.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(java.awt.event.FocusEvent e) {
				if (new String(txtPassword.getPassword()).equals("Password")) {
					txtPassword.setText("");
					txtPassword.setForeground(Color.BLACK);
					txtPassword.setEchoChar('●');
				}
			}

			@Override
			public void focusLost(java.awt.event.FocusEvent e) {
				if (new String(txtPassword.getPassword()).trim().isEmpty()) {
					txtPassword.setText("Password");
					txtPassword.setForeground(Color.GRAY);
					txtPassword.setEchoChar((char) 0);
				}
			}
		});
		var passwordField = createStyledField(new ImageIcon(Menu.class.getResource("/icon10/locked-computer.png")),
				"Password");
		passwordField.add(txtPassword, BorderLayout.CENTER);
		passwordField.setBounds(118, 185, 353, 36);
		register.add(passwordField);

		// Trường Confirm Password
		txtConfirmPassword = new JPasswordField("Confirm Password");
		txtConfirmPassword.setBorder(null);
		txtConfirmPassword.setForeground(Color.GRAY);
		txtConfirmPassword.setBackground(new Color(236, 236, 255));
		txtConfirmPassword.setFont(new Font("Arial", Font.PLAIN, 14));
		txtConfirmPassword.setBounds(118, 231, 353, 36);
		txtConfirmPassword.setEchoChar((char) 0);
		txtConfirmPassword.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(java.awt.event.FocusEvent e) {
				if (new String(txtConfirmPassword.getPassword()).equals("Confirm Password")) {
					txtConfirmPassword.setText("");
					txtConfirmPassword.setForeground(Color.BLACK);
					txtConfirmPassword.setEchoChar('●');
				}
			}

			@Override
			public void focusLost(java.awt.event.FocusEvent e) {
				if (new String(txtConfirmPassword.getPassword()).trim().isEmpty()) {
					txtConfirmPassword.setText("Confirm Password");
					txtConfirmPassword.setForeground(Color.GRAY);
					txtConfirmPassword.setEchoChar((char) 0);
				}
			}
		});
		var confirmPasswordField = createStyledField(new ImageIcon(Menu.class.getResource("/icon10/padlock.png")),
				"Confirm Password");
		confirmPasswordField.add(txtConfirmPassword, BorderLayout.CENTER);
		confirmPasswordField.setBounds(118, 231, 353, 36);
		register.add(confirmPasswordField);

		// Trường Full Name
		txtFullName = new JTextField("Full Name");
		txtFullName.setBorder(null);
		txtFullName.setForeground(Color.GRAY);
		txtFullName.setBackground(new Color(236, 236, 255));
		txtFullName.setFont(new Font("Arial", Font.PLAIN, 14));
		txtFullName.setBounds(118, 277, 353, 36);
		txtFullName.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(java.awt.event.FocusEvent e) {
				if (txtFullName.getText().equals("Full Name")) {
					txtFullName.setText("");
					txtFullName.setForeground(Color.BLACK);
				}
			}

			@Override
			public void focusLost(java.awt.event.FocusEvent e) {
				if (txtFullName.getText().trim().isEmpty()) {
					txtFullName.setText("Full Name");
					txtFullName.setForeground(Color.GRAY);
				}
			}
		});
		var fullNameField = createStyledField(new ImageIcon(Menu.class.getResource("/icon10/user.png")), "Full Name");
		fullNameField.add(txtFullName, BorderLayout.CENTER);
		fullNameField.setBounds(118, 277, 353, 36);
		register.add(fullNameField);

		// Trường Phone Number
		txtPhoneNumber = new JTextField("Phone Number");
		txtPhoneNumber.setBorder(null);
		txtPhoneNumber.setForeground(Color.GRAY);
		txtPhoneNumber.setBackground(new Color(236, 236, 255));
		txtPhoneNumber.setFont(new Font("Arial", Font.PLAIN, 14));
		txtPhoneNumber.setBounds(118, 323, 353, 36);
		txtPhoneNumber.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(java.awt.event.FocusEvent e) {
				if (txtPhoneNumber.getText().equals("Phone Number")) {
					txtPhoneNumber.setText("");
					txtPhoneNumber.setForeground(Color.BLACK);
				}
			}

			@Override
			public void focusLost(java.awt.event.FocusEvent e) {
				if (txtPhoneNumber.getText().trim().isEmpty()) {
					txtPhoneNumber.setText("Phone Number");
					txtPhoneNumber.setForeground(Color.GRAY);
				}
			}
		});
		var phoneNumberField = createStyledField(new ImageIcon(Menu.class.getResource("/icon10/phone.png")),
				"Phone Number");
		phoneNumberField.add(txtPhoneNumber, BorderLayout.CENTER);
		phoneNumberField.setBounds(118, 323, 353, 36);
		register.add(phoneNumberField);

		// Trường Address
		txtAddress = new JTextField("Address");
		txtAddress.setBorder(null);
		txtAddress.setForeground(Color.GRAY);
		txtAddress.setBackground(new Color(236, 236, 255));
		txtAddress.setFont(new Font("Arial", Font.PLAIN, 14));
		txtAddress.setBounds(118, 369, 353, 36);
		txtAddress.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(java.awt.event.FocusEvent e) {
				if (txtAddress.getText().equals("Address")) {
					txtAddress.setText("");
					txtAddress.setForeground(Color.BLACK);
				}
			}

			@Override
			public void focusLost(java.awt.event.FocusEvent e) {
				if (txtAddress.getText().trim().isEmpty()) {
					txtAddress.setText("Address");
					txtAddress.setForeground(Color.GRAY);
				}
			}
		});
		var addressField = createStyledField(new ImageIcon(Menu.class.getResource("/icon10/address.png")), "Address");
		addressField.add(txtAddress, BorderLayout.CENTER);
		addressField.setBounds(118, 369, 353, 36);
		register.add(addressField);

		// Nút SIGN UP
		var btnRegister = new JButton("SIGN UP") {
			/**
			 *
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
				var g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(getBackground());
				g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
				super.paintComponent(g2);
				g2.dispose();
			}

			@Override
			protected void paintBorder(Graphics g) {
			}
		};
		btnRegister.setContentAreaFilled(false);
		btnRegister.setFocusPainted(false);
		btnRegister.setBorder(null);
		btnRegister.setForeground(Color.WHITE);
		btnRegister.setBackground(new Color(128, 128, 192)); // Màu tím nhạt
		btnRegister.setBounds(190, 420, 200, 40);
		btnRegister.setFont(new Font("Arial", Font.BOLD, 16));
		btnRegister.addActionListener(e -> {
			var password = new String(txtPassword.getPassword()).trim();
			var confirmPassword = new String(txtConfirmPassword.getPassword()).trim();

			if (!password.equals(confirmPassword)) {
				JOptionPane.showMessageDialog(this, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
			} else {
				btnRegisterActionPerformed(e);
			}
		});
		register.add(btnRegister);

		add(register, "register");
	}

	public void showLogin() {
		((CardLayout) getLayout()).show(this, "login");
	}

	public void showRegister(boolean isRegister) {
		((CardLayout) getLayout()).show(this, isRegister ? "register" : "login");
	}

	private boolean isValidEmail(String email) {
		var regex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
		var pattern = Pattern.compile(regex);
		var matcher = pattern.matcher(email);
		return matcher.matches();
	}

	protected void btnRegisterActionPerformed(ActionEvent e) {
	    var fullName = txtFullName.getText().trim();
	    var email = txtEmail.getText().trim();
	    var pass = new String(txtPassword.getPassword()).trim();
	    var confirmPassword = new String(txtConfirmPassword.getPassword()).trim();
	    var phone = txtPhoneNumber.getText().trim();
	    var address = txtAddress.getText().trim();

	    // 1. Kiểm tra các trường không được để trống hoặc giữ giá trị mặc định
	    if (fullName.isEmpty() || fullName.equalsIgnoreCase("Full Name")) {
	        JOptionPane.showMessageDialog(this, "Full Name cannot be empty or default.", "Error", JOptionPane.ERROR_MESSAGE);
	        txtFullName.requestFocus();
	        return;
	    }
	    if (email.isEmpty() || email.equalsIgnoreCase("Email")) {
	        JOptionPane.showMessageDialog(this, "Email cannot be empty or default.", "Error", JOptionPane.ERROR_MESSAGE);
	        txtEmail.requestFocus();
	        return;
	    }
	    if (pass.isEmpty() || pass.equalsIgnoreCase("Password")) {
	        JOptionPane.showMessageDialog(this, "Password cannot be empty or default.", "Error", JOptionPane.ERROR_MESSAGE);
	        txtPassword.requestFocus();
	        return;
	    }
	    if (confirmPassword.isEmpty() || confirmPassword.equalsIgnoreCase("Confirm Password")) {
	        JOptionPane.showMessageDialog(this, "Confirm Password cannot be empty or default.", "Error", JOptionPane.ERROR_MESSAGE);
	        txtConfirmPassword.requestFocus();
	        return;
	    }
	    if (phone.isEmpty() || phone.equalsIgnoreCase("Phone Number")) {
	        JOptionPane.showMessageDialog(this, "Phone Number cannot be empty or default.", "Error", JOptionPane.ERROR_MESSAGE);
	        txtPhoneNumber.requestFocus();
	        return;
	    }
	    if (address.isEmpty() || address.equalsIgnoreCase("Address")) {
	        JOptionPane.showMessageDialog(this, "Address cannot be empty or default.", "Error", JOptionPane.ERROR_MESSAGE);
	        txtAddress.requestFocus();
	        return;
	    }

	    // 2. Kiểm tra định dạng email
	    if (!email.endsWith("@gmail.com")) {
	        JOptionPane.showMessageDialog(this, "Email must end with @gmail.com.", "Error", JOptionPane.ERROR_MESSAGE);
	        txtEmail.requestFocus();
	        return;
	    }

	    // 3. Kiểm tra xem email đã tồn tại chưa
	    if (userDao.isEmailExists(email)) {
	        JOptionPane.showMessageDialog(this, "Email " + email + " is already in use. Please use a different email.", "Error", JOptionPane.ERROR_MESSAGE);
	        txtEmail.requestFocus();
	        return;
	    }

	    // 4. Kiểm tra mật khẩu
	    if (pass.length() < 6 || !pass.matches(".*[A-Z].*") || !pass.matches(".*[a-z].*") || !pass.matches(".*\\d.*") || !pass.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
	        JOptionPane.showMessageDialog(this, "Password must be at least 6 characters long and include uppercase, lowercase, a number, and a special character.", "Error", JOptionPane.ERROR_MESSAGE);
	        txtPassword.requestFocus();
	        return;
	    }

	    // 5. Kiểm tra mật khẩu xác nhận
	    if (!pass.equals(confirmPassword)) {
	        JOptionPane.showMessageDialog(this, "Passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
	        txtConfirmPassword.requestFocus();
	        return;
	    }

	    // 6. Kiểm tra số điện thoại
	    if (!phone.matches("\\d{10,15}")) {
	        JOptionPane.showMessageDialog(this, "Phone number must contain only digits and be 10 to 15 characters long.", "Error", JOptionPane.ERROR_MESSAGE);
	        txtPhoneNumber.requestFocus();
	        return;
	    }

	    // 7. Kiểm tra xem số điện thoại đã tồn tại chưa
	    if (userDao.isPhoneExists(phone)) {
	        JOptionPane.showMessageDialog(this, "Phone number " + phone + " is already in use. Please use a different phone number.", "Error", JOptionPane.ERROR_MESSAGE);
	        txtPhoneNumber.requestFocus();
	        return;
	    }

	    // 8. Kiểm tra tên chỉ chứa chữ cái và dấu
	    if (!fullName.matches("[a-zA-ZÀ-ỹ\\s]+")) {
	        JOptionPane.showMessageDialog(this, "Full Name must only contain letters and spaces.", "Error", JOptionPane.ERROR_MESSAGE);
	        txtFullName.requestFocus();
	        return;
	    }

	    // Nếu tất cả hợp lệ, tiến hành tạo đối tượng UserEntity
	    var user = new UserEntity();
	    user.setFullName(fullName);
	    user.setPassword(pass);
	    user.setEmail(email);
	    user.setPhoneNumber(phone);
	    user.setAddress(address);
	    user.setUserRole(1); // Gán quyền user mặc định
	    user.setActive(false); // Mặc định chưa kích hoạt

	    // Thực hiện đăng ký
	    var success = userDao.UserRegister(user);
	    if (success) {
	        JOptionPane.showMessageDialog(this, "Registration successful! Please wait for admin approval.", "Success", JOptionPane.INFORMATION_MESSAGE);
	        clearRegisterFields(); // Xóa sạch trường nhập
	        showLogin(); // Quay lại giao diện đăng nhập
	    } else {
	        JOptionPane.showMessageDialog(this, "Registration failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
	    }
	}

	// Phương thức kiểm tra định dạng số điện thoại
//	private boolean isValidPhoneNumber(String phone) {
//		var regex = "^[0-9]{10,15}$"; // Chỉ cho phép số và độ dài từ 10 đến 15 ký tự
//		return phone.matches(regex);
//	}

	protected void btnLoginActionPerformed(ActionEvent e) {
		var email = txtLoginEmail.getText().trim();
		var password = new String(txtLoginPassword.getPassword()).trim();

		if (email.isEmpty() || password.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Please enter both email and password.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (!userDao.isEmailExists(email)) {
			JOptionPane.showMessageDialog(this, "Email does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		// kiểm tra tài khoàn đã được duyệt chưa
		var user = userDao.getUser(email);
		if (!user.isActive()) {
			JOptionPane.showMessageDialog(this, "Your account is pending approval by admin.", "Access Denied",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		if (userDao.isPasswordExists(password, email)) {
//			JOptionPane.showMessageDialog(this, "Login successful! Welcome.", "Success",
//					JOptionPane.INFORMATION_MESSAGE);
			var parentWindow = SwingUtilities.getWindowAncestor(this);
			var session = UserSession.getInstance();
			session.setUsername(user.getFullName()); // Lưu username
			session.setUserId(user.getUserID()); // Lưu userId
			session.setAvatar(user.getAvatar()); // Lưu đường dẫn đến file ảnh đại diện
			session.setUserRole(user.getUserRole());
			session.setEmail(user.getEmail());

			if (parentWindow != null) {
				parentWindow.dispose();
			}
			// mở giao diện chính
			java.awt.EventQueue.invokeLater(() -> {
				var mainFrame = new Main();
				mainFrame.setVisible(true); // Đảm bảo cửa sổ Main hiển thị
			});
		} else {
			JOptionPane.showMessageDialog(this, "Invalid email or password.", "Error", JOptionPane.ERROR_MESSAGE);
		}

	}

	private void clearRegisterFields() {
		txtFullName.setText("");
//		txtUsername.setText("");
		txtEmail.setText("");
		txtPassword.setText("");
		txtConfirmPassword.setText("");
		txtPhoneNumber.setText("");
		txtAddress.setText("");
	}

	// hiển thị dialog để nhập email
	private void showForgotPasswordDialog() {
		var forgotPasswordDialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Forgot Password");
		forgotPasswordDialog.getContentPane().setLayout(new MigLayout("wrap, center", "[center]", "push[]25[]push"));
		forgotPasswordDialog.setSize(400, 200);
		forgotPasswordDialog.setLocationRelativeTo(null);

		var txtEmail = new JTextField();
		txtEmail.setBorder(BorderFactory.createTitledBorder("Enter your email"));
		forgotPasswordDialog.getContentPane().add(txtEmail, "w 80%");

		var btnSend = new JButton("Send Password");
		btnSend.addActionListener(e -> {
			var email = txtEmail.getText().trim();
			// kiểm tra email không để trống
			if (email.isEmpty()) {
				JOptionPane.showMessageDialog(forgotPasswordDialog, "Please enter your email.", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			// kiểm tra định dạng email
			if (!isValidEmail(email)) {
				JOptionPane.showMessageDialog(forgotPasswordDialog, "Invalid email format.", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			// kiểm tra xem email có tồn tại hay không
			if (!userDao.isEmailExists(email)) {
				JOptionPane.showMessageDialog(forgotPasswordDialog, "Email does not exist.", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			var newPassword = generateRandomPassword();

			var bcryptPassword = org.mindrot.jbcrypt.BCrypt.hashpw(newPassword, org.mindrot.jbcrypt.BCrypt.gensalt());
			userDao.updatePassword(email, bcryptPassword);

			if (sendPasswordResetEmail(email, newPassword)) {
				JOptionPane.showMessageDialog(forgotPasswordDialog, "A new password has been sent to your email.",
						"Success", JOptionPane.INFORMATION_MESSAGE);
				forgotPasswordDialog.dispose();
			} else {
				JOptionPane.showMessageDialog(forgotPasswordDialog,
						"Failed to send password reset email. Please try again later.", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
			forgotPasswordDialog.dispose();
		});
		forgotPasswordDialog.getContentPane().add(btnSend, "w 50%, h 40!");

		forgotPasswordDialog.setVisible(true);
	}

	// tạo password random
	private String generateRandomPassword() {
		var chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%";
		var sb = new StringBuilder();
		var random = new java.util.Random();
		for (var i = 0; i < 8; i++) {
			sb.append(chars.charAt(random.nextInt(chars.length())));
		}
		return sb.toString();
	}

	private boolean sendPasswordResetEmail(String email, String newPassword) {
		final var fromEmail = "nguyenphu0809@gmail.com";
		final var password = "czjt ddxd cyps nyjx\r\n";
		final var subject = "Your New Password";
		final var body = "Your new password is: " + newPassword;

		var props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");

		var session = Session.getInstance(props, new javax.mail.Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(fromEmail, password);
			}
		});

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(fromEmail));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
			message.setSubject(subject);
			message.setText(body);

			Transport.send(message);
			return true;
		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		}
	}

	private static JPanel createStyledField(Icon icon, String placeholder) {
		var panel = new JPanel(new BorderLayout());
		panel.setBackground(new Color(236, 236, 255)); // Màu nền nhạt
		panel.setBorder(BorderFactory.createLineBorder(new Color(236, 236, 255), 1, true)); // Đường viền bo góc

		// Icon bên trái
		var iconLabel = new JLabel(icon);
		iconLabel.setBorder(new EmptyBorder(0, 10, 0, 10)); // Padding cho icon

		panel.add(iconLabel, BorderLayout.WEST);

		return panel;
	}

	protected void txtLoginPasswordKeyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			btnLogin.doClick(); // Kích hoạt nút Login
		}
	}
}
