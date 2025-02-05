package add;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
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

import com.toedter.calendar.JDateChooser;

import dao.StudentDao;
import entity.StudentEntity;
import entity.UserSession;
import main.Student;

public class AddStudent extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JPanel panelAvatar;
	private JTextField txtStudentCode, txtFullName, txtPhoneNumber, txtAddress, txtSchoolName, txtGraduationYear,
			txtEmail;
	private JRadioButton maleRadio, femaleRadio, otherRadio;
	private JButton btnUploadAvatar;
	private String avatarPath = null;
	private JDateChooser dateChooser; // JDateChooser thay cho txtDateOfBirth
	private UserSession userSession;

	private Student parentForm;

	public AddStudent(Student parent) {
		userSession = UserSession.getInstance();
		this.parentForm = parent;
		setTitle("Add New Student");
		setBounds(100, 100, 582, 516);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		contentPanel.setBounds(0, 0, 669, 479);
		getContentPane().add(contentPanel);
		contentPanel.setLayout(null);

		// Panel Avatar
		panelAvatar = new JPanel();
		panelAvatar.setBounds(20, 20, 100, 100);
		panelAvatar.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		contentPanel.add(panelAvatar);

		btnUploadAvatar = new JButton("");
		btnUploadAvatar.setIcon(new ImageIcon(AddStudent.class.getResource("/icon5/social.png")));
		btnUploadAvatar.setBounds(20, 123, 40, 41);
		btnUploadAvatar.addActionListener(this::uploadAvatarActionPerformed);
		contentPanel.add(btnUploadAvatar);

		// Labels và Fields
		String[] labels = { "Student Code:", "Full Name:", "Date of Birth:", "Gender:", "Phone Number:", "Address:",
				"School Name:", "Graduation Year:", "Email:" };

		int labelX = 150, labelY = 20, labelWidth = 120, labelHeight = 30;
		int fieldX = 280, fieldY = 20, fieldWidth = 250, fieldHeight = 30;

		for (var i = 0; i < labels.length; i++) {
			// Label
			var label = new JLabel(labels[i]);
			label.setBounds(labelX, labelY + i * 40, labelWidth, labelHeight);
			label.setFont(new Font("Arial", Font.PLAIN, 14));
			contentPanel.add(label);

			// Field hoặc RadioButton
			if (labels[i].equals("Gender:")) {
				maleRadio = new JRadioButton("Male");
				maleRadio.setBounds(fieldX, fieldY + i * 40, 70, fieldHeight);
				contentPanel.add(maleRadio);

				femaleRadio = new JRadioButton("Female");
				femaleRadio.setBounds(fieldX + 80, fieldY + i * 40, 80, fieldHeight);
				contentPanel.add(femaleRadio);

				otherRadio = new JRadioButton("Other");
				otherRadio.setBounds(fieldX + 170, fieldY + i * 40, 80, fieldHeight);
				contentPanel.add(otherRadio);

				var genderGroup = new ButtonGroup();
				genderGroup.add(maleRadio);
				genderGroup.add(femaleRadio);
			} else if (labels[i].equals("Date of Birth:")) {
				// JDateChooser cho trường Date of Birth
				dateChooser = new JDateChooser();
				dateChooser.setBounds(fieldX, fieldY + i * 40, fieldWidth, fieldHeight);
				dateChooser.setDateFormatString("yyyy-MM-dd"); // Định dạng ngày
				contentPanel.add(dateChooser);
			} else {
				var textField = new JTextField();
				textField.setBounds(fieldX, fieldY + i * 40, fieldWidth, fieldHeight);
				contentPanel.add(textField);

				switch (labels[i]) {
				case "Student Code:" -> txtStudentCode = textField;
				case "Full Name:" -> txtFullName = textField;
				case "Phone Number:" -> txtPhoneNumber = textField;
				case "Address:" -> txtAddress = textField;
				case "School Name:" -> txtSchoolName = textField;
				case "Graduation Year:" -> txtGraduationYear = textField;
				case "Email:" -> txtEmail = textField;
				}
			}
		}

		// Save Button
		var btnSave = new JButton("");
		btnSave.setIcon(new ImageIcon(AddStudent.class.getResource("/icon3/save.png")));
		btnSave.setBounds(234, 415, 57, 53);
		btnSave.addActionListener(this::saveStudentActionPerformed);
		contentPanel.add(btnSave);

		// Cancel Button
		var btnCancel = new JButton("");
		btnCancel.setIcon(new ImageIcon(AddStudent.class.getResource("/icon5/cross.png")));
		btnCancel.setBounds(320, 415, 57, 53);
		btnCancel.addActionListener(e -> dispose());
		contentPanel.add(btnCancel);
	}

	private void uploadAvatarActionPerformed(ActionEvent e) {
		var fileChooser = new javax.swing.JFileChooser();
		fileChooser.setDialogTitle("Select Avatar Image");
		fileChooser.setFileSelectionMode(javax.swing.JFileChooser.FILES_ONLY);
		var fileFilter = new javax.swing.filechooser.FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png",
				"gif");
		fileChooser.setFileFilter(fileFilter);

		if (fileChooser.showOpenDialog(this) == javax.swing.JFileChooser.APPROVE_OPTION) {
			var selectedFile = fileChooser.getSelectedFile();
			var sourcePath = selectedFile.getAbsolutePath();

			var targetFolder = "src/main/resources/avatar/";
			var extension = selectedFile.getName().substring(selectedFile.getName().lastIndexOf('.'));
			var uniqueFileName = System.currentTimeMillis() + extension;
			var targetPath = targetFolder + uniqueFileName;

			try {
				java.nio.file.Files.copy(java.nio.file.Paths.get(sourcePath), java.nio.file.Paths.get(targetPath),
						java.nio.file.StandardCopyOption.REPLACE_EXISTING);
				avatarPath = uniqueFileName; // Save file name
				setAvatar(targetPath);
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Failed to upload avatar: " + ex.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void setAvatar(String avatarPath) {
		try {
			var imageIcon = new ImageIcon(
					new ImageIcon(avatarPath).getImage().getScaledInstance(90, 90, java.awt.Image.SCALE_SMOOTH));
			var label = new JLabel(imageIcon);
			panelAvatar.removeAll();
			panelAvatar.add(label);
			panelAvatar.revalidate();
			panelAvatar.repaint();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void saveStudentActionPerformed(ActionEvent e) {
	    try {
	        // Kiểm tra các trường không được để trống
	        if (txtStudentCode.getText().trim().isEmpty() || txtFullName.getText().trim().isEmpty()
	                || dateChooser.getDate() == null || txtPhoneNumber.getText().trim().isEmpty()
	                || txtAddress.getText().trim().isEmpty() || txtSchoolName.getText().trim().isEmpty()
	                || txtGraduationYear.getText().trim().isEmpty() || txtEmail.getText().trim().isEmpty()) {
	            JOptionPane.showMessageDialog(this, "All fields are required!", "Validation Error",
	                    JOptionPane.WARNING_MESSAGE);
	            return;
	        }

	        // Kiểm tra StudentCode chỉ chứa chữ cái và số, tối đa 15 ký tự
	        if (!txtStudentCode.getText().trim().matches("^[a-zA-Z0-9]{1,15}$")) {
	            JOptionPane.showMessageDialog(this,
	                    "StudentCode must contain only letters and numbers, and be less than 15 characters!",
	                    "Validation Error", JOptionPane.WARNING_MESSAGE);
	            txtStudentCode.requestFocus();
	            return;
	        }

	        // Kiểm tra trùng lặp StudentCode
	        var dao = new StudentDao();
	        var inputStudentCode = txtStudentCode.getText().trim();

	        if (dao.isStudentCodeExists(inputStudentCode)) {
	            JOptionPane.showMessageDialog(this, "StudentCode already exists!", "Validation Error",
	                    JOptionPane.WARNING_MESSAGE);
	            txtStudentCode.requestFocus();
	            return;
	        }

	        // Kiểm tra Full Name không được chứa số hoặc ký tự đặc biệt
	        if (!txtFullName.getText().trim().matches("^[\\p{L}\\s]+$")) {
	            JOptionPane.showMessageDialog(this,
	                    "Full Name must only contain letters and spaces, no numbers or special characters allowed!",
	                    "Validation Error", JOptionPane.WARNING_MESSAGE);
	            txtFullName.requestFocus();
	            return;
	        }

	        // Lấy ngày sinh từ JDateChooser và chuyển thành LocalDate
	        var dateOfBirth = dateChooser.getDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();

	        // Kiểm tra ngày sinh phải trên 16 tuổi
	        var today = java.time.LocalDate.now(); // Ngày hiện tại
	        var sixteenYearsAgo = today.minusYears(16); // Giới hạn ngày (16 năm trước)

	        if (!dateOfBirth.isBefore(sixteenYearsAgo)) {
	            JOptionPane.showMessageDialog(this, "Date of Birth must indicate the student is older than 16 years!",
	                    "Validation Error", JOptionPane.WARNING_MESSAGE);
	            dateChooser.requestFocus();
	            return;
	        }

	        // Kiểm tra PhoneNumber chỉ chứa số
	        if (!txtPhoneNumber.getText().trim().matches("\\d+")) {
	            JOptionPane.showMessageDialog(this, "Phone Number must contain only numbers!", "Validation Error",
	                    JOptionPane.WARNING_MESSAGE);
	            txtPhoneNumber.requestFocus();
	            return;
	        }

	        // Kiểm tra trùng lặp PhoneNumber
	        var inputPhoneNumber = txtPhoneNumber.getText().trim();
	        if (dao.isPhoneNumberExists(inputPhoneNumber, 0)) { // Truyền 0 vì đây là thêm mới
	            JOptionPane.showMessageDialog(this, "Phone Number already exists!", "Validation Error",
	                    JOptionPane.WARNING_MESSAGE);
	            txtPhoneNumber.requestFocus();
	            return;
	        }

	        // Kiểm tra Email phải có định dạng @gmail.com
	        if (!txtEmail.getText().trim().matches("^[a-zA-Z0-9._%+-]+@gmail\\.com$")) {
	            JOptionPane.showMessageDialog(this, "Email must have a valid @gmail.com format!", "Validation Error",
	                    JOptionPane.WARNING_MESSAGE);
	            txtEmail.requestFocus();
	            return;
	        }

	        // Kiểm tra trùng lặp Email
	        var inputEmail = txtEmail.getText().trim().toLowerCase();
	        if (dao.isEmailExists(inputEmail)) {
	            JOptionPane.showMessageDialog(this, "Email already exists!", "Validation Error",
	                    JOptionPane.WARNING_MESSAGE);
	            txtEmail.requestFocus();
	            return;
	        }

	        // Kiểm tra GraduationYear hợp lệ
	        var graduationYear = Integer.parseInt(txtGraduationYear.getText().trim());
	        var currentYear = java.time.Year.now().getValue();
	        if (graduationYear < currentYear) {
	            JOptionPane.showMessageDialog(this,
	                    "Graduation Year must be greater than or equal to the current year!", "Validation Error",
	                    JOptionPane.WARNING_MESSAGE);
	            txtGraduationYear.requestFocus();
	            return;
	        }

	        // Thêm sinh viên
	        var student = new StudentEntity();
	        student.setStudentCode(txtStudentCode.getText().trim());
	        student.setFullName(txtFullName.getText().trim());
	        student.setDateOfBirth(dateOfBirth);
	        student.setPhoneNumber(txtPhoneNumber.getText().trim());
	        student.setAddress(txtAddress.getText().trim());
	        student.setSchoolName(txtSchoolName.getText().trim());
	        student.setEnrollmentYear(graduationYear);
	        student.setEmail(txtEmail.getText().trim());

	        // Thiết lập giới tính
	        if (maleRadio.isSelected()) {
	            student.setGender("Male");
	        } else if (femaleRadio.isSelected()) {
	            student.setGender("Female");
	        } else if (otherRadio.isSelected()) {
	            student.setGender("Other");
	        }

	        // Thiết lập avatar
	        student.setAvatar(avatarPath);

	        // Tạm thời thiết lập UserID = 1
	        student.setUserID(userSession.getUserId());

	        // Gọi DAO để thêm sinh viên
	        dao.insert(student);
			sendConfirmationEmail(student.getEmail(), student.getFullName(), student.getStudentCode());

	        JOptionPane.showMessageDialog(this, "Student added successfully!");
	        parentForm.reloadTable(); // Reload lại bảng
	        dispose(); // Đóng form AddStudent
	    } catch (Exception ex) {
	        JOptionPane.showMessageDialog(this, "Failed to add student: " + ex.getMessage(), "Error",
	                JOptionPane.ERROR_MESSAGE);
	    }
	}

	private void sendConfirmationEmail(String recipientEmail, String studentName, String studentCode) {
		final var senderEmail = "nguyenphu0809@gmail.com"; // Địa chỉ email người gửi
		final var senderPassword = "yykh yooo tfnt wgmt"; // Mật khẩu email người gửi (hoặc App Password nếu dùng
															// Gmail)
		final var host = "smtp.gmail.com"; // Host SMTP (dùng Gmail)

		var props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", "587"); // Cổng SMTP

		var session = Session.getInstance(props, new javax.mail.Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(senderEmail, senderPassword);
			}
		});

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(senderEmail)); // Đặt email người gửi
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail)); // Đặt email người
																									// nhận
			message.setSubject("Registration Successful - Library Account Created");

			var emailContent = new StringBuilder();
			emailContent.append("<html>").append("<body>").append("<h2>Dear ").append(studentName).append(",</h2>")
					.append("<p>Congratulations! Your library account has been successfully created.</p>")
					.append("<p><strong>Student Code:</strong> ").append(studentCode).append("</p>")
					.append("<p>You can borrow books from the library.</p>").append("<br>")
					.append("<p>Best regards,<br>Library Team</p>").append("</body>").append("</html>");

			message.setContent(emailContent.toString(), "text/html; charset=utf-8"); // Đặt nội dung email

			Transport.send(message); // Gửi email

			JOptionPane.showMessageDialog(this, "Confirmation email sent to " + recipientEmail);
		} catch (MessagingException e) {
			JOptionPane.showMessageDialog(this, "Failed to send email: " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
}