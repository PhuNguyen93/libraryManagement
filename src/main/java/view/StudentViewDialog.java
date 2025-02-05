package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.FileOutputStream;

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
import javax.swing.border.EmptyBorder;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import dao.StudentDao;
import entity.StudentEntity;

public class StudentViewDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanelView = new JPanel();
	private JPanel panelAvatar;
	private JTextField txtStudentCode, txtFullName, txtDateOfBirth, txtPhoneNumber, txtAddress, txtSchoolName,
			txtEnrollmentYear, txtAccountCreator, txtEmail, txtTotalBooksRented, txtLateReturnsCount,
			txtDamagedBooksCount, txtTotalOrders;
	private JRadioButton maleRadio, femaleRadio, otherRadio;

	public StudentViewDialog(StudentEntity student) {
		setTitle("Student");
		setBounds(100, 100, 638, 701);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		contentPanelView.setBounds(0, 0, 669, 591);
		contentPanelView.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanelView);
		contentPanelView.setLayout(null);

		// Panel Avatar
		panelAvatar = new JPanel();
		panelAvatar.setBounds(20, 20, 100, 100);
		panelAvatar.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Viền đen
		contentPanelView.add(panelAvatar);

		// Close Button
		var closeButton = new JButton("");
		closeButton.setIcon(new ImageIcon(StudentViewDialog.class.getResource("/icon5/cross.png")));
		closeButton.setBounds(254, 602, 57, 53);
		getContentPane().add(closeButton);

		var btnExcelView = new JButton("");
		btnExcelView.addActionListener(this::btnExcelViewActionPerformed);
		btnExcelView.setIcon(new ImageIcon(StudentViewDialog.class.getResource("/icon4/xls.png")));
		btnExcelView.setBounds(160, 602, 57, 53);
		getContentPane().add(btnExcelView);
		closeButton.addActionListener(this::closeButtonActionPerformed);

		// Left column labels
		String[] labels = { "Student Code:", "Full Name:", "Date of Birth:", "Gender:", "Phone Number:", "Address:",
				"School Name:", "Enrollment Year:", "Account Creator:", "Email:", "Total Books Rented:",
				"Late Returns Count:", "Damaged Books Count:", "Total Orders:" };

		// Components
		int labelX = 150, labelY = 20, labelWidth = 150, labelHeight = 30;
		int fieldX = 320, fieldY = 20, fieldWidth = 250, fieldHeight = 30;

		for (var i = 0; i < labels.length; i++) {
			// Add label
			var label = new JLabel(labels[i]);
			label.setBounds(labelX, labelY + i * 40, labelWidth, labelHeight);
			label.setFont(new Font("Arial", Font.PLAIN, 14));
			contentPanelView.add(label);

			// Add JTextField or Gender RadioButtons
			if (labels[i].equals("Gender:")) {
				// RadioButtons for Gender
				maleRadio = new JRadioButton("Male");
				maleRadio.setBounds(fieldX, fieldY + i * 40, 70, fieldHeight);
				maleRadio.setFont(new Font("Arial", Font.PLAIN, 14));
				maleRadio.setEnabled(false);
				contentPanelView.add(maleRadio);

				femaleRadio = new JRadioButton("Female");
				femaleRadio.setBounds(fieldX + 80, fieldY + i * 40, 80, fieldHeight);
				femaleRadio.setFont(new Font("Arial", Font.PLAIN, 14));
				femaleRadio.setEnabled(false);
				contentPanelView.add(femaleRadio);

				otherRadio = new JRadioButton("Other");
				otherRadio.setBounds(fieldX + 170, fieldY + i * 40, 80, fieldHeight);
				otherRadio.setFont(new Font("Arial", Font.PLAIN, 14));
				otherRadio.setEnabled(false);
				contentPanelView.add(otherRadio);

				// Group RadioButtons to allow only one selection
				var genderGroup = new ButtonGroup();
				genderGroup.add(maleRadio);
				genderGroup.add(femaleRadio);
				genderGroup.add(otherRadio);
			} else {
				// JTextField for other fields
				var textField = new JTextField();
				textField.setBounds(fieldX, fieldY + i * 40, fieldWidth, fieldHeight);
				textField.setEditable(false);
				textField.setBackground(Color.WHITE);
				contentPanelView.add(textField);

				switch (labels[i]) {
				case "Student Code:" -> txtStudentCode = textField;
				case "Full Name:" -> txtFullName = textField;
				case "Date of Birth:" -> txtDateOfBirth = textField;
				case "Phone Number:" -> txtPhoneNumber = textField;
				case "Address:" -> txtAddress = textField;
				case "School Name:" -> txtSchoolName = textField;
				case "Enrollment Year:" -> txtEnrollmentYear = textField;
				case "Account Creator:" -> txtAccountCreator = textField;
				case "Email:" -> txtEmail = textField;
				case "Total Books Rented:" -> txtTotalBooksRented = textField;
				case "Late Returns Count:" -> txtLateReturnsCount = textField;
				case "Damaged Books Count:" -> txtDamagedBooksCount = textField;
				case "Total Orders:" -> txtTotalOrders = textField;
				}
			}
		}

		// Load Student Data into Form Fields
		loadStudentData(student);

		// Disable all text fields
		disableAllTextFields();
	}

	private void loadStudentData(StudentEntity student) {
		txtStudentCode.setText(student.getStudentCode());
		txtFullName.setText(student.getFullName());

		if (student.getDateOfBirth() != null) {
			txtDateOfBirth.setText(student.getDateOfBirth().toString());
		} else {
			txtDateOfBirth.setText("");
		}

		txtPhoneNumber.setText(student.getPhoneNumber());
		txtAddress.setText(student.getAddress());
		txtSchoolName.setText(student.getSchoolName());
		txtEnrollmentYear.setText(student.getEnrollmentYear() != 0 ? String.valueOf(student.getEnrollmentYear()) : "");
		txtEmail.setText(student.getEmail());
		txtTotalBooksRented.setText(String.valueOf(student.getTotalBooksRented()));
		txtLateReturnsCount.setText(String.valueOf(student.getLateReturnsCount()));
		txtDamagedBooksCount.setText(String.valueOf(student.getDamagedBooksCount()));
		txtTotalOrders.setText(String.valueOf(student.getTotalOrders()));

		// Gender
		if (student.getGender() != null) {
			switch (student.getGender()) {
			case "Male" -> maleRadio.setSelected(true);
			case "Female" -> femaleRadio.setSelected(true);
			case "Other" -> otherRadio.setSelected(true);
			default -> {
			}
			}
		}

		// Account Creator
		var studentDao = new StudentDao();
		var accountCreatorName = studentDao.getUserNameById(student.getUserID());
		txtAccountCreator.setText(accountCreatorName);

		// Avatar
		var targetFolder = "src/main/resources/avatar/";
		var targetPath = targetFolder + student.getAvatar();
		setAvatar(targetPath);
	}

	private void setAvatar(String avatarPath) {
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
		}
	}

	private void disableAllTextFields() {
		var lightBlue = new Color(173, 216, 230); // Màu xanh nhạt

		txtStudentCode.setEnabled(false);
		txtStudentCode.setBackground(lightBlue);
		txtStudentCode.setForeground(Color.BLACK);

		txtFullName.setEnabled(false);
		txtFullName.setBackground(lightBlue);
		txtFullName.setForeground(Color.BLACK);

		txtDateOfBirth.setEnabled(false);
		txtDateOfBirth.setBackground(lightBlue);
		txtDateOfBirth.setForeground(Color.BLACK);

		txtPhoneNumber.setEnabled(false);
		txtPhoneNumber.setBackground(lightBlue);
		txtPhoneNumber.setForeground(Color.BLACK);

		txtAddress.setEnabled(false);
		txtAddress.setBackground(lightBlue);
		txtAddress.setForeground(Color.BLACK);

		txtSchoolName.setEnabled(false);
		txtSchoolName.setBackground(lightBlue);
		txtSchoolName.setForeground(Color.BLACK);

		txtEnrollmentYear.setEnabled(false);
		txtEnrollmentYear.setBackground(lightBlue);
		txtEnrollmentYear.setForeground(Color.BLACK);

		txtAccountCreator.setEnabled(false);
		txtAccountCreator.setBackground(lightBlue);
		txtAccountCreator.setForeground(Color.BLACK);

		txtEmail.setEnabled(false);
		txtEmail.setBackground(lightBlue);
		txtEmail.setForeground(Color.BLACK);

		txtTotalBooksRented.setEnabled(false);
		txtTotalBooksRented.setBackground(lightBlue);
		txtTotalBooksRented.setForeground(Color.BLACK);

		txtLateReturnsCount.setEnabled(false);
		txtLateReturnsCount.setBackground(lightBlue);
		txtLateReturnsCount.setForeground(Color.BLACK);

		txtDamagedBooksCount.setEnabled(false);
		txtDamagedBooksCount.setBackground(lightBlue);
		txtDamagedBooksCount.setForeground(Color.BLACK);

		txtTotalOrders.setEnabled(false);
		txtTotalOrders.setBackground(lightBlue);
		txtTotalOrders.setForeground(Color.BLACK);
	}


	private void closeButtonActionPerformed(ActionEvent e) {
		dispose();
	}

	protected void btnExcelViewActionPerformed(ActionEvent e) {
		try {
			// Tạo file Excel
			var workbook = new XSSFWorkbook();
			var sheet = workbook.createSheet("Student Info");

			// Tạo hàng tiêu đề
			var headerRow = sheet.createRow(0);
			String[] headers = { "Field", "Value" };
			for (var i = 0; i < headers.length; i++) {
				var cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);
			}

			// Thêm dữ liệu vào file Excel
			var rowNum = 1;
			String[][] data = { { "Student Code", txtStudentCode.getText() }, { "Full Name", txtFullName.getText() },
					{ "Date of Birth", txtDateOfBirth.getText() },
					{ "Gender", maleRadio.isSelected() ? "Male" : femaleRadio.isSelected() ? "Female" : "Other" },
					{ "Phone Number", txtPhoneNumber.getText() }, { "Address", txtAddress.getText() },
					{ "School Name", txtSchoolName.getText() }, { "Enrollment Year", txtEnrollmentYear.getText() },
					{ "Account Creator", txtAccountCreator.getText() }, { "Email", txtEmail.getText() },
					{ "Total Books Rented", txtTotalBooksRented.getText() },
					{ "Late Returns Count", txtLateReturnsCount.getText() },
					{ "Damaged Books Count", txtDamagedBooksCount.getText() },
					{ "Total Orders", txtTotalOrders.getText() } };

			for (String[] rowData : data) {
				var row = sheet.createRow(rowNum++);
				row.createCell(0).setCellValue(rowData[0]); // Field
				row.createCell(1).setCellValue(rowData[1]); // Value
			}

			// Tự động điều chỉnh độ rộng cột
			sheet.autoSizeColumn(0);
			sheet.autoSizeColumn(1);

			// Lưu file Excel
			var filePath = System.getProperty("user.home") + "/Documents/StudentInfo.xlsx";
			try (var fileOut = new FileOutputStream(filePath)) {
				workbook.write(fileOut);
			}
			workbook.close();

			JOptionPane.showMessageDialog(this, "Exported to Excel successfully!\nFile saved at: " + filePath);
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error exporting to Excel: " + ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

}
