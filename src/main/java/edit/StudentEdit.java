package edit;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;

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

import com.toedter.calendar.JDateChooser;

import dao.StudentDao;
import entity.StudentEntity;
import entity.UserSession;
import main.Student;

public class StudentEdit extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanelEdit = new JPanel();
	private JPanel panelAvatar;
	private JTextField txtStudentCode, txtFullName, txtPhoneNumber, txtAddress, txtSchoolName,
			txtGraduationYear, txtAccountCreator;
	private JRadioButton maleRadio, femaleRadio, otherRadio;
	private JButton btnUpdateAvatar;
	private JDateChooser dateChooser; // Thay thế cho txtDateOfBirth

	private StudentEntity mainStudent;
	private Student parentForm; // Tham chiếu tới form cha
	private JButton btnclose;
	private UserSession userSession;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		try {
			var dialog = new StudentEdit();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public StudentEdit() {
		userSession = UserSession.getInstance();
		setTitle("Student Edit");
		setBounds(100, 100, 600, 516);
		setLocationRelativeTo(null); // Hiển thị ở giữa màn hình
		getContentPane().setLayout(null);
		contentPanelEdit.setBounds(0, 0, 669, 479);
		contentPanelEdit.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanelEdit);
		contentPanelEdit.setLayout(null);

		// Panel Avatar
		panelAvatar = new JPanel();
		panelAvatar.setBounds(20, 20, 100, 100);
		panelAvatar.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		contentPanelEdit.add(panelAvatar);

		var saveButton = new JButton("");
		saveButton.addActionListener(this::saveButtonActionPerformed);
		saveButton.setBounds(153, 395, 57, 53);
		contentPanelEdit.add(saveButton);
		saveButton.setIcon(new ImageIcon(StudentEdit.class.getResource("/icon3/save.png")));
		saveButton.setActionCommand("OK");
		getRootPane().setDefaultButton(saveButton);

		btnUpdateAvatar = new JButton("");
		btnUpdateAvatar.setIcon(new ImageIcon(StudentEdit.class.getResource("/icon5/social.png")));
		btnUpdateAvatar.addActionListener(this::btnUpdateAvatarActionPerformed);
		btnUpdateAvatar.setBounds(20, 121, 41, 41);
		contentPanelEdit.add(btnUpdateAvatar);

		btnclose = new JButton("");
		btnclose.addActionListener(this::btncloseActionPerformed);
		btnclose.setIcon(new ImageIcon(StudentEdit.class.getResource("/icon5/cross.png")));
		btnclose.setBounds(233, 395, 57, 53);
		contentPanelEdit.add(btnclose);

		// Left column labels
		String[] labels = { "Student Code:", "Full Name:", "Date of Birth:", "Gender:", "Phone Number:", "Address:",
				"School Name:", "Graduation Year:", "Account Creator:" };

		// Components
		int labelX = 150, labelY = 20, labelWidth = 120, labelHeight = 30;
		int fieldX = 280, fieldY = 20, fieldWidth = 250, fieldHeight = 30;

		for (var i = 0; i < labels.length; i++) {
			// Label
			var label = new JLabel(labels[i]);
			label.setBounds(labelX, labelY + i * 40, labelWidth, labelHeight);
			label.setFont(new Font("Arial", Font.PLAIN, 14));
			contentPanelEdit.add(label);

			// Field hoặc RadioButton
			if (labels[i].equals("Gender:")) {
				maleRadio = new JRadioButton("Male");
				maleRadio.setBounds(fieldX, fieldY + i * 40, 70, fieldHeight);
				contentPanelEdit.add(maleRadio);

				femaleRadio = new JRadioButton("Female");
				femaleRadio.setBounds(fieldX + 80, fieldY + i * 40, 80, fieldHeight);
				contentPanelEdit.add(femaleRadio);

				otherRadio = new JRadioButton("Other");
				otherRadio.setBounds(fieldX + 170, fieldY + i * 40, 80, fieldHeight);
				contentPanelEdit.add(otherRadio);

				var genderGroup = new ButtonGroup();
				genderGroup.add(maleRadio);
				genderGroup.add(femaleRadio);
				genderGroup.add(otherRadio);
			} else if (labels[i].equals("Date of Birth:")) {
				// JDateChooser cho trường Date of Birth
				dateChooser = new JDateChooser();
				dateChooser.setBounds(fieldX, fieldY + i * 40, fieldWidth, fieldHeight);
				dateChooser.setDateFormatString("yyyy-MM-dd");
				contentPanelEdit.add(dateChooser);
			} else {
				var textField = new JTextField();
				textField.setBounds(fieldX, fieldY + i * 40, fieldWidth, fieldHeight);
				contentPanelEdit.add(textField);

				switch (labels[i]) {
				case "Student Code:" -> txtStudentCode = textField;
				case "Full Name:" -> txtFullName = textField;
				case "Phone Number:" -> txtPhoneNumber = textField;
				case "Address:" -> txtAddress = textField;
				case "School Name:" -> txtSchoolName = textField;
				case "Graduation Year:" -> txtGraduationYear = textField;
				case "Account Creator:" -> txtAccountCreator = textField;
				}
			}
		}
	}


	// Method to set data from StudentEntity
	public void setStudentData(StudentEntity student, String accountCreatorName, Student parent) {
		this.parentForm = parent; // Gán tham chiếu tới form cha

		mainStudent = new StudentEntity();
		mainStudent = student;
		txtStudentCode.setText(student.getStudentCode());
		txtStudentCode.setEnabled(false);
		txtFullName.setText(student.getFullName());

		// Kiểm tra null cho DateOfBirth
		if (student.getDateOfBirth() != null) {
			dateChooser.setDate(java.sql.Date.valueOf(student.getDateOfBirth()));
			}

		txtPhoneNumber.setText(student.getPhoneNumber());
		txtAddress.setText(student.getAddress());
		txtSchoolName.setText(student.getSchoolName());
		txtGraduationYear.setText(student.getEnrollmentYear() != 0 ? String.valueOf(student.getEnrollmentYear()) : "");
		txtAccountCreator.setText(accountCreatorName);
		txtAccountCreator.setEnabled(false);

		// Set Gender
		if (student.getGender() != null) {
			switch (student.getGender()) {
			case "Male" -> maleRadio.setSelected(true);
			case "Female" -> femaleRadio.setSelected(true);
			case "Other" -> otherRadio.setSelected(true);
			default -> {
				maleRadio.setSelected(false);
				femaleRadio.setSelected(false);
				otherRadio.setSelected(false);
			}
			}
			}

		// Gọi phương thức để hiển thị avatar
		var targetFolder = "src/main/resources/avatar/";
		var targetPath = targetFolder + student.getAvatar();
		setAvatar(targetPath);
	}

	protected void saveButtonActionPerformed(ActionEvent e) {
	    try {
	        // Kiểm tra dữ liệu đầu vào
	        if (txtFullName.getText().trim().isEmpty()) {
	            JOptionPane.showMessageDialog(this, "Full Name is required!", "Validation Error",
	                    JOptionPane.WARNING_MESSAGE);
	            txtFullName.requestFocus();
	            return;
	        }

	        // Kiểm tra Full Name không chứa số hoặc ký tự đặc biệt
	        if (!txtFullName.getText().trim().matches("^[\\p{L}\\s]+$")) { // Chỉ cho phép chữ cái (bao gồm Unicode) và khoảng trắng
	            JOptionPane.showMessageDialog(this,
	                    "Full Name must only contain letters and spaces, no numbers or special characters allowed!",
	                    "Validation Error", JOptionPane.WARNING_MESSAGE);
	            txtFullName.requestFocus();
	            return;
	        }

	        // Kiểm tra ngày sinh phải trên 16 tuổi
	        if (dateChooser.getDate() == null) {
	            JOptionPane.showMessageDialog(this, "Date of Birth is required!", "Validation Error",
	                    JOptionPane.WARNING_MESSAGE);
	            dateChooser.requestFocus();
	            return;
	        }

	        // Lấy giá trị GraduationYear từ JTextField
	        var graduationYearText = txtGraduationYear.getText().trim();

	        // Kiểm tra xem người dùng có nhập hay không
	        if (graduationYearText.isEmpty()) {
	            JOptionPane.showMessageDialog(this, "Graduation Year is required!", "Validation Error",
	                    JOptionPane.ERROR_MESSAGE);
	            return;
	        }

	        // Chuyển giá trị từ chuỗi sang số nguyên
	        var graduationYear = Integer.parseInt(graduationYearText);
	        // Lấy năm hiện tại
	        var currentYear = java.time.Year.now().getValue();

	        // Kiểm tra điều kiện
	        if (graduationYear < currentYear) {
	            JOptionPane.showMessageDialog(this,
	                    "Graduation Year must be greater than or equal to the current year!", "Validation Error",
	                    JOptionPane.ERROR_MESSAGE);
	            return; // Dừng lại nếu không hợp lệ
	        }

	        // Lấy ngày sinh từ JDateChooser và chuyển thành LocalDate
	        var dateOfBirth = dateChooser.getDate().toInstant().atZone(java.time.ZoneId.systemDefault())
	                .toLocalDate();

	        // Tính toán giới hạn ngày (16 năm trước)
	        var today = java.time.LocalDate.now(); // Ngày hiện tại
	        var sixteenYearsAgo = today.minusYears(16); // Ngày giới hạn (16 năm trước)

	        if (!dateOfBirth.isBefore(sixteenYearsAgo)) {
	            JOptionPane.showMessageDialog(this,
	                    "Date of Birth must indicate the student is older than 16 years!", "Validation Error",
	                    JOptionPane.WARNING_MESSAGE);
	            dateChooser.requestFocus();
	            return;
	        }

	        if (txtPhoneNumber.getText().trim().isEmpty()) {
	            JOptionPane.showMessageDialog(this, "Phone Number is required!", "Validation Error",
	                    JOptionPane.WARNING_MESSAGE);
	            txtPhoneNumber.requestFocus();
	            return;
	        }

	        if (!txtPhoneNumber.getText().trim().matches("\\d+")) {
	            JOptionPane.showMessageDialog(this, "Phone Number must contain only digits!", "Validation Error",
	                    JOptionPane.WARNING_MESSAGE);
	            txtPhoneNumber.requestFocus();
	            return;
	        }

	        // Kiểm tra số điện thoại có trùng không
	        var studentDao = new StudentDao();
	        if (studentDao.isPhoneNumberExists(txtPhoneNumber.getText().trim(), mainStudent.getStudentID())) {
	            JOptionPane.showMessageDialog(this, "Phone Number already exists!", "Validation Error",
	                    JOptionPane.WARNING_MESSAGE);
	            txtPhoneNumber.requestFocus();
	            return;
	        }

	        if (txtAddress.getText().trim().isEmpty()) {
	            JOptionPane.showMessageDialog(this, "Address is required!", "Validation Error",
	                    JOptionPane.WARNING_MESSAGE);
	            txtAddress.requestFocus();
	            return;
	        }

	        // Cập nhật dữ liệu sinh viên
	        mainStudent.setFullName(txtFullName.getText().trim());
	        mainStudent.setDateOfBirth(
	                java.time.LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(dateChooser.getDate())));
	        mainStudent.setPhoneNumber(txtPhoneNumber.getText().trim());
	        mainStudent.setAddress(txtAddress.getText().trim());
	        mainStudent.setEnrollmentYear(graduationYear);

	        // Gọi DAO để cập nhật thông tin
	        studentDao.update(mainStudent, userSession.getUserId());

	        JOptionPane.showMessageDialog(this, "Student information updated successfully!");
	        dispose();
	        if (parentForm != null) {
	            parentForm.reloadTable();
	        }
	    } catch (Exception ex) {
	        JOptionPane.showMessageDialog(this, "Failed to save: " + ex.getMessage(), "Error",
	                JOptionPane.ERROR_MESSAGE);
	    }
	}



	public void setAvatar(String avatarPath) {
		try {
			// Tạo ảnh với kích thước 90x90
			var imageIcon = new ImageIcon(
					new ImageIcon(avatarPath).getImage().getScaledInstance(90, 90, java.awt.Image.SCALE_SMOOTH));
			var label = new JLabel(imageIcon);

			// Căn giữa ảnh trong panelAvatar
			label.setBounds((panelAvatar.getWidth() - 90) / 2, (panelAvatar.getHeight() - 90) / 2, 90, 90);

			// Xóa các component cũ và thêm label mới
			panelAvatar.removeAll();
			panelAvatar.add(label);
			panelAvatar.revalidate();
			panelAvatar.repaint();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Failed to load avatar from path: " + avatarPath, "Error",
					JOptionPane.ERROR_MESSAGE);
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
				mainStudent.setAvatar(uniqueFileName);
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

	protected void btncloseActionPerformed(ActionEvent e) {
		dispose();
	}
}
