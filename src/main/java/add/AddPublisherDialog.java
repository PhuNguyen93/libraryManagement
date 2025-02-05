package add;

import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import dao.PublisherDao;
import entity.PublisherEntity;
import main.Author;

public class AddPublisherDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField txtName, txtAddress, txtPhoneNumber, txtEmail;

	private Author parentForm;

	public AddPublisherDialog(Author parent) {
		this.parentForm = parent;
		setTitle("Add New Publisher");
		setBounds(100, 100, 450, 350);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		contentPanel.setBounds(0, 0, 434, 311);
		getContentPane().add(contentPanel);
		contentPanel.setLayout(null);

		// Labels and Fields
		String[] labels = { "Name:", "Address:", "Phone Number:", "Email:" };
		int labelX = 20, labelY = 20, labelWidth = 150, labelHeight = 30;
		int fieldX = 180, fieldY = 20, fieldWidth = 200, fieldHeight = 30;

		for (var i = 0; i < labels.length; i++) {
			var label = new JLabel(labels[i]);
			label.setBounds(labelX, labelY + i * 40, labelWidth, labelHeight);
			label.setFont(new Font("Arial", Font.PLAIN, 14));
			contentPanel.add(label);

			var textField = new JTextField();
			textField.setBounds(fieldX, fieldY + i * 40, fieldWidth, fieldHeight);

			switch (labels[i]) {
			case "Name:" -> txtName = textField;
			case "Address:" -> txtAddress = textField;
			case "Phone Number:" -> txtPhoneNumber = textField;
			case "Email:" -> txtEmail = textField;
			}

			contentPanel.add(textField);
		}

		// Save Button
		var btnSave = new JButton("");
		btnSave.setIcon(new ImageIcon(AddPublisherDialog.class.getResource("/icon3/save.png")));
		btnSave.setBounds(100, 250, 57, 53);
		btnSave.addActionListener(this::savePublisherActionPerformed);
		contentPanel.add(btnSave);

		// Cancel Button
		var btnCancel = new JButton("");
		btnCancel.setIcon(new ImageIcon(AddPublisherDialog.class.getResource("/icon5/cross.png")));
		btnCancel.setBounds(200, 250, 57, 53);
		btnCancel.addActionListener(e -> dispose());
		contentPanel.add(btnCancel);
	}

	private void savePublisherActionPerformed(ActionEvent e) {
		try {
			var dao = new PublisherDao();

			// Validate Name
			var name = txtName.getText().trim();
			if (name.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Name is required!", "Validation Error",
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			// Check duplicate Name
			if (dao.select().stream().anyMatch(p -> p.getName().equalsIgnoreCase(name))) {
				JOptionPane.showMessageDialog(this, "Name already exists!", "Validation Error",
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			// Validate Address
			var address = txtAddress.getText().trim();
			if (address.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Address is required!", "Validation Error",
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			// Validate Phone Number
			var phoneNumber = txtPhoneNumber.getText().trim();
			if (!phoneNumber.matches("^[0-9+()]{1,12}$")) {
				JOptionPane.showMessageDialog(this,
						"Invalid phone number! Only digits, '+', and '()' are allowed, and must not exceed 12 characters.",
						"Validation Error", JOptionPane.WARNING_MESSAGE);
				return;
			}

			// Check duplicate Phone Number
			if (dao.select().stream().anyMatch(p -> p.getPhoneNumber().equals(phoneNumber))) {
				JOptionPane.showMessageDialog(this, "Phone Number already exists!", "Validation Error",
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			// Validate Email
			var email = txtEmail.getText().trim();
			if (!email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$")) {
				JOptionPane.showMessageDialog(this, "Invalid email format!", "Validation Error",
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			// Check duplicate Email
			if (dao.select().stream().anyMatch(p -> p.getEmail().equalsIgnoreCase(email))) {
				JOptionPane.showMessageDialog(this, "Email already exists!", "Validation Error",
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			// Create PublisherEntity and set values
			var publisher = new PublisherEntity();
			publisher.setName(name);
			publisher.setAddress(address);
			publisher.setPhoneNumber(phoneNumber);
			publisher.setEmail(email);
			publisher.setIsDeleted(false);

			// Insert Publisher into the database
			dao.insert(publisher);

			JOptionPane.showMessageDialog(this, "Publisher added successfully!");

			// Reload the table in the parent form
			if (parentForm != null) {
				parentForm.panelPublisherActivated();
			}

			// Close the dialog
			dispose();
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Failed to add publisher: " + ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}
