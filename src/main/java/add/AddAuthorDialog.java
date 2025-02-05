package add;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.ZoneId;

import javax.swing.ImageIcon;
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

public class AddAuthorDialog extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
	private JTextField txtFullName, txtNationality;
	private JDateChooser dateChooserDateOfBirth; // Sử dụng JDateChooser

    private Author parentForm;

    public AddAuthorDialog(Author parent) {
        this.parentForm = parent;
        setTitle("Add New Author");
        setBounds(100, 100, 450, 350);
        setLocationRelativeTo(null);
        getContentPane().setLayout(null);
        contentPanel.setBounds(0, 0, 434, 311);
        getContentPane().add(contentPanel);
        contentPanel.setLayout(null);

        // Labels and Fields
		String[] labels = { "Full Name:", "Date of Birth:", "Nationality:" };
        int labelX = 20, labelY = 20, labelWidth = 150, labelHeight = 30;
        int fieldX = 180, fieldY = 20, fieldWidth = 200, fieldHeight = 30;

        for (var i = 0; i < labels.length; i++) {
            var label = new JLabel(labels[i]);
            label.setBounds(labelX, labelY + i * 40, labelWidth, labelHeight);
            label.setFont(new Font("Arial", Font.PLAIN, 14));
            contentPanel.add(label);

			if (labels[i].equals("Date of Birth:")) {
				dateChooserDateOfBirth = new JDateChooser();
				dateChooserDateOfBirth.setBounds(fieldX, fieldY + i * 40, fieldWidth, fieldHeight);
				dateChooserDateOfBirth.setDateFormatString("yyyy-MM-dd");
				contentPanel.add(dateChooserDateOfBirth);
			} else {
				var textField = new JTextField();
				textField.setBounds(fieldX, fieldY + i * 40, fieldWidth, fieldHeight);

				switch (labels[i]) {
				case "Full Name:" -> txtFullName = textField;
				case "Nationality:" -> txtNationality = textField;
				}

				contentPanel.add(textField);
            }
        }

        // Save Button
        var btnSave = new JButton("");
        btnSave.setIcon(new ImageIcon(AddAuthorDialog.class.getResource("/icon3/save.png")));
        btnSave.setBounds(100, 250, 57, 53);
        btnSave.addActionListener(this::saveAuthorActionPerformed);
        contentPanel.add(btnSave);

        // Cancel Button
        var btnCancel = new JButton("");
        btnCancel.setIcon(new ImageIcon(AddAuthorDialog.class.getResource("/icon5/cross.png")));
        btnCancel.setBounds(200, 250, 57, 53);
        btnCancel.addActionListener(e -> dispose());
        contentPanel.add(btnCancel);
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

            // Tạo AuthorEntity và gán giá trị
            var author = new AuthorEntity();
            author.setFullName(fullName);
            author.setDateOfBirth(dateOfBirth);
            author.setNationality(nationality);
            author.setIsDeleted(false);

            // Thêm vào cơ sở dữ liệu
            var dao = new AuthorDao();
            dao.insert(author);

            JOptionPane.showMessageDialog(this, "Author added successfully!");
            parentForm.panel1Activated();
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to add author: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
