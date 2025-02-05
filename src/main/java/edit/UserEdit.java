package edit;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import dao.UserDao;
import entity.UserEntity;

public class UserEdit extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanelEdit = new JPanel();
	private JPanel panelAvatar;
	private JTextField txtFullName, txtEmail, txtPhoneNumber, txtAddress;
	private JRadioButton activeRadio, inactiveRadio;
	private JButton btnUpdateAvatar;
	private UserEntity mainUser;

	/**
	 * Main method to test UserEdit UI.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		try {
			var dialog = new UserEdit();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Constructor for UserEdit.
	 */
	public UserEdit() {
		setTitle("User Edit");
		setBounds(100, 100, 573, 351); // Update height to fit new fields
		setLocationRelativeTo(null); // Center the dialog
		getContentPane().setLayout(null);
		contentPanelEdit.setBounds(0, 0, 557, 312);
		contentPanelEdit.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanelEdit);
		contentPanelEdit.setLayout(null);

		// Panel for Avatar
		panelAvatar = new JPanel();
		panelAvatar.setBounds(20, 20, 100, 100);
		panelAvatar.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		contentPanelEdit.add(panelAvatar);

		// Button to update avatar
		btnUpdateAvatar = new JButton("");
		btnUpdateAvatar.setIcon(new ImageIcon(UserEdit.class.getResource("/icon5/social.png")));
		btnUpdateAvatar.addActionListener(this::btnUpdateAvatarActionPerformed);
		btnUpdateAvatar.setBounds(20, 121, 41, 41);
		contentPanelEdit.add(btnUpdateAvatar);

		// Labels and Fields
		String[] labels = { "Full Name:", "Email:", "Phone Number:", "Address:", "Status:" }; // Loại bỏ "Username"
		int labelX = 150, labelY = 20, labelWidth = 120, labelHeight = 30;
		int fieldX = 280, fieldY = 20, fieldWidth = 250, fieldHeight = 30;

		for (var i = 0; i < labels.length; i++) {
		    var label = new JLabel(labels[i]);
		    label.setBounds(labelX, labelY + i * 40, labelWidth, labelHeight);
		    label.setFont(new Font("Arial", Font.PLAIN, 14));
		    contentPanelEdit.add(label);

		    if (labels[i].equals("Status:")) {
		        // Radio Buttons for Status
		        activeRadio = new JRadioButton("Active");
		        activeRadio.setBounds(fieldX, fieldY + i * 40, 100, fieldHeight);
		        contentPanelEdit.add(activeRadio);

		        inactiveRadio = new JRadioButton("Inactive");
		        inactiveRadio.setBounds(fieldX + 110, fieldY + i * 40, 100, fieldHeight);
		        contentPanelEdit.add(inactiveRadio);

		        var statusGroup = new ButtonGroup();
		        statusGroup.add(activeRadio);
		        statusGroup.add(inactiveRadio);
		    } else {
		        var textField = new JTextField();
		        textField.setBounds(fieldX, fieldY + i * 40, fieldWidth, fieldHeight);

		        // Đặt thuộc tính đặc biệt cho trường Email
		        if (labels[i].equals("Email:")) {
		            textField.setEditable(false); // Ngăn chỉnh sửa
		            textField.setFocusable(false); // Ngăn click chuột
		            textField.setBackground(new Color(220, 220, 220)); // Màu nền xám nhạt để làm rõ
		        }

		        contentPanelEdit.add(textField);

		        switch (labels[i]) {
		            case "Full Name:" -> txtFullName = textField;
		            case "Email:" -> txtEmail = textField;
		            case "Phone Number:" -> txtPhoneNumber = textField;
		            case "Address:" -> txtAddress = textField;
		        }
		    }
		}



		// Save and Close Buttons
		var saveButton = new JButton("");
		saveButton.setIcon(new ImageIcon(UserEdit.class.getResource("/icon3/save.png")));
		saveButton.addActionListener(this::saveButtonActionPerformed);
		saveButton.setBounds(335, 237, 57, 53);
		contentPanelEdit.add(saveButton);

		var closeButton = new JButton("");
		closeButton.setIcon(new ImageIcon(UserEdit.class.getResource("/icon5/cross.png")));
		closeButton.addActionListener(e -> dispose());
		closeButton.setBounds(415, 237, 57, 53);
		contentPanelEdit.add(closeButton);
	}

	/**
	 * Method to populate user data into the fields.
	 */
	public void setUserData(UserEntity user) {
		this.mainUser = user;
		txtFullName.setText(user.getFullName());
		txtEmail.setText(user.getEmail());
		txtPhoneNumber.setText(user.getPhoneNumber());
		txtAddress.setText(user.getAddress());
		if (user.isActive()) {
			activeRadio.setSelected(true);
		} else {
			inactiveRadio.setSelected(true);
		}

		// Set avatar if available
		var targetFolder = "src/main/resources/avatar/";
		var targetPath = targetFolder + user.getAvatar();
		setAvatar(targetPath);
	}

	/**
	 * Method to set the avatar image in the panel.
	 */
	private void setAvatar(String avatarPath) {
		try {
			var imageIcon = new ImageIcon(
					new ImageIcon(avatarPath).getImage().getScaledInstance(90, 90, java.awt.Image.SCALE_SMOOTH));
			var label = new JLabel(imageIcon);
			label.setBounds((panelAvatar.getWidth() - 90) / 2, (panelAvatar.getHeight() - 90) / 2, 90, 90);
			panelAvatar.removeAll();
			panelAvatar.add(label);
			panelAvatar.revalidate();
			panelAvatar.repaint();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Failed to load avatar.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	protected void btnUpdateAvatarActionPerformed(ActionEvent e) {
		// Tạo JFileChooser để chọn tệp
		var fileChooser = new javax.swing.JFileChooser();
		fileChooser.setDialogTitle("Select Avatar Image");
		fileChooser.setFileSelectionMode(javax.swing.JFileChooser.FILES_ONLY);

		// Lọc chỉ các tệp hình ảnh (JPG, PNG)
		var fileFilter = new javax.swing.filechooser.FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png",
				"gif");
		fileChooser.setFileFilter(fileFilter);

		// Hiển thị dialog để chọn file
		var result = fileChooser.showOpenDialog(this);
		if (result == javax.swing.JFileChooser.APPROVE_OPTION) {
			// Lấy đường dẫn tệp đã chọn
			var selectedFile = fileChooser.getSelectedFile();
//			var avatarPath = selectedFile.getAbsolutePath();
			var sourcePath = selectedFile.getAbsolutePath();

			// Tên file được lưu trong folder avatar (sử dụng tên gốc của file)
			var targetFolder = "src/main/resources/avatar/";
			var extension = "";
			var lastDotIndex = selectedFile.getName().lastIndexOf('.');
			if (lastDotIndex > 0) {
				extension = selectedFile.getName().substring(lastDotIndex);
			}

			var uniqueFileName = System.currentTimeMillis() + extension; // Tên file: timestamp + đuôi file
			var targetPath = targetFolder + uniqueFileName;
			;

			try {
				// Tạo thư mục nếu chưa tồn tại
				var targetDirectory = new java.io.File(targetFolder);
				if (!targetDirectory.exists()) {
					targetDirectory.mkdirs();
				}

				// Sao chép tệp vào thư mục avatar
				java.nio.file.Files.copy(java.nio.file.Paths.get(sourcePath), java.nio.file.Paths.get(targetPath),
						java.nio.file.StandardCopyOption.REPLACE_EXISTING);
				// Hiển thị avatar trong panelAvatar
				setAvatar(targetPath);
				mainUser.setAvatar(uniqueFileName);
				// Hiển thị thông báo thành công
//				JOptionPane.showMessageDialog(this, "Avatar updated successfully!", "Success",
//						JOptionPane.INFORMATION_MESSAGE);
			} catch (Exception ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(this, "Failed to update avatar: " + ex.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void saveButtonActionPerformed(ActionEvent e) {
	    // Lấy giá trị từ các trường
	    var fullName = txtFullName.getText().trim();
	    var phoneNumber = txtPhoneNumber.getText().trim();
	    var address = txtAddress.getText().trim();

	    // Kiểm tra các trường không được để trống
	    if (fullName.isEmpty()) {
	        JOptionPane.showMessageDialog(this, "Full Name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
	        txtFullName.requestFocus();
	        return;
	    }
	    if (phoneNumber.isEmpty()) {
	        JOptionPane.showMessageDialog(this, "Phone Number cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
	        txtPhoneNumber.requestFocus();
	        return;
	    }
	    if (address.isEmpty()) {
	        JOptionPane.showMessageDialog(this, "Address cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
	        txtAddress.requestFocus();
	        return;
	    }

	    // Kiểm tra giá trị hợp lệ cho Full Name
	    if (!fullName.matches("[a-zA-ZÀ-ỹ\\s]+")) {
	        JOptionPane.showMessageDialog(this, "Full Name must only contain letters and spaces.", "Error", JOptionPane.ERROR_MESSAGE);
	        txtFullName.requestFocus();
	        return;
	    }

	    // Kiểm tra giá trị hợp lệ cho Phone Number
	    if (!phoneNumber.matches("\\d{10,15}")) {
	        JOptionPane.showMessageDialog(this, "Phone Number must contain only digits and be 10 to 15 characters long.", "Error", JOptionPane.ERROR_MESSAGE);
	        txtPhoneNumber.requestFocus();
	        return;
	    }

	    // Kiểm tra xem số điện thoại đã tồn tại chưa
	    var userDao = new UserDao();
	    if (userDao.isPhoneExists(phoneNumber) && !phoneNumber.equals(mainUser.getPhoneNumber())) {
	        JOptionPane.showMessageDialog(this, "Phone Number is already in use. Please use a different phone number.", "Error", JOptionPane.ERROR_MESSAGE);
	        txtPhoneNumber.requestFocus();
	        return;
	    }

	    // Cập nhật thông tin người dùng
	    mainUser.setFullName(fullName);
	    mainUser.setPhoneNumber(phoneNumber);
	    mainUser.setAddress(address);
	    mainUser.setActive(activeRadio.isSelected());

	    // Lưu thông tin vào cơ sở dữ liệu
	    userDao.update(mainUser);

	    JOptionPane.showMessageDialog(this, "User updated successfully!");
	    dispose();
	}


}