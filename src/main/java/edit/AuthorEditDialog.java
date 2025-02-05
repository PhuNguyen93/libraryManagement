package edit;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.toedter.calendar.JDateChooser;

import dao.AuthorDao;
import entity.AuthorEntity;
import main.Author;

public class AuthorEditDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private final JPanel contentPanel = new JPanel();
	private JTextField txtFullName, txtNationality;
	private JDateChooser dateChooserDateOfBirth; // Sử dụng JDateChooser
	private AuthorEntity currentAuthor;
	private Author parentForm; // Tham chiếu tới form cha

	public AuthorEditDialog(Author parentForm, AuthorEntity author) {
		this.parentForm = parentForm; // Gán tham chiếu tới form cha
		this.currentAuthor = author;

		setTitle("Edit Author");
		setBounds(100, 100, 450, 350);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);
		contentPanel.setBounds(0, 0, 434, 311);
		contentPanel.setLayout(null);
		getContentPane().add(contentPanel);

		// Labels and Fields
		String[] labels = { "Full Name:", "Date of Birth:", "Nationality:" };
		int labelX = 50, labelY = 50, labelWidth = 150, labelHeight = 30;
		int fieldX = 200, fieldY = 50, fieldWidth = 200, fieldHeight = 30;

		for (var i = 0; i < labels.length; i++) {
			var label = new JLabel(labels[i]);
			label.setBounds(labelX, labelY + i * 50, labelWidth, labelHeight);
			label.setFont(new Font("Arial", Font.PLAIN, 14));
			contentPanel.add(label);

			if (labels[i].equals("Date of Birth:")) {
				dateChooserDateOfBirth = new JDateChooser();
				dateChooserDateOfBirth.setBounds(fieldX, fieldY + i * 50, fieldWidth, fieldHeight);
				dateChooserDateOfBirth.setDateFormatString("yyyy-MM-dd");
				contentPanel.add(dateChooserDateOfBirth);
			} else {
				var textField = new JTextField();
				textField.setBounds(fieldX, fieldY + i * 50, fieldWidth, fieldHeight);

				switch (labels[i]) {
				case "Full Name:" -> txtFullName = textField;
				case "Nationality:" -> txtNationality = textField;
				}
				contentPanel.add(textField);
			}
		}

		// Save Button
		var btnSave = new JButton("Save");
		btnSave.setBounds(100, 250, 90, 30);
		btnSave.addActionListener(this::saveAuthorActionPerformed);
		contentPanel.add(btnSave);

		// Cancel Button
		var btnCancel = new JButton("Cancel");
		btnCancel.setBounds(230, 250, 90, 30);
		btnCancel.addActionListener(e -> dispose());
		contentPanel.add(btnCancel);

		// Load Author Data
		loadAuthorData();
	}

	private void loadAuthorData() {
		txtFullName.setText(currentAuthor.getFullName());
		txtNationality.setText(currentAuthor.getNationality());
		// Chuyển đổi LocalDate sang Date để thiết lập giá trị cho JDateChooser
		if (currentAuthor.getDateOfBirth() != null) {
			var date = Date.from(currentAuthor.getDateOfBirth().atStartOfDay(ZoneId.systemDefault()).toInstant());
			dateChooserDateOfBirth.setDate(date);
		}
	}

	private void saveAuthorActionPerformed(ActionEvent e) {
		try {
			// Validate Full Name
			var fullName = txtFullName.getText().trim();
			if (fullName.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Full Name is required!", "Validation Error",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			if (!fullName.matches("^[^0-9]*$")) { // Kiểm tra không chứa số
				JOptionPane.showMessageDialog(this, "Full Name must not contain numbers!", "Validation Error",
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			// Validate Date of Birth
			var selectedDate = dateChooserDateOfBirth.getDate();
			if (selectedDate == null) {
				JOptionPane.showMessageDialog(this, "Date of Birth is required!", "Validation Error",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			// Chuyển đổi Date sang LocalDate
			var dateOfBirth = selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			var today = LocalDate.now();
			if (today.minusYears(16).isBefore(dateOfBirth)) { // Kiểm tra tuổi phải lớn hơn hoặc bằng 16
				JOptionPane.showMessageDialog(this, "Author must be at least 16 years old!", "Validation Error",
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			// Validate Nationality
			var nationality = txtNationality.getText().trim();
			if (nationality.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Nationality is required!", "Validation Error",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			if (!nationality.matches("^[^0-9]*$")) { // Kiểm tra không chứa số
				JOptionPane.showMessageDialog(this, "Nationality must not contain numbers!", "Validation Error",
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			// Update AuthorEntity object
			currentAuthor.setFullName(fullName);
			currentAuthor.setDateOfBirth(dateOfBirth);
			currentAuthor.setNationality(nationality);

			// Update Author in the database
			var dao = new AuthorDao();
			dao.update(currentAuthor);

			JOptionPane.showMessageDialog(this, "Author updated successfully!");
			dispose();

			// Reload the table in the parent form
			if (parentForm != null) {
				parentForm.panel1Activated();
			}

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Failed to save author: " + ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}
