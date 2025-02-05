package edit;

import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import dao.PublisherDao;
import entity.PublisherEntity;
import main.Author;

public class PublisherEditDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField txtName, txtAddress, txtPhoneNumber, txtEmail;
	private PublisherEntity currentPublisher;
	private Author parentForm;

	public PublisherEditDialog(Author parentForm, PublisherEntity publisher) {
		this.parentForm = parentForm;
		this.currentPublisher = publisher;

		setTitle("Edit Publisher");
		setBounds(100, 100, 450, 400);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);
		contentPanel.setBounds(0, 0, 434, 361);
		contentPanel.setLayout(null);
		getContentPane().add(contentPanel);

		// Labels and Fields
		String[] labels = { "Name:", "Address:", "Phone Number:", "Email:" };
		int labelX = 30, labelY = 40, labelWidth = 120, labelHeight = 30;
		int fieldX = 160, fieldY = 40, fieldWidth = 220, fieldHeight = 30;

		for (var i = 0; i < labels.length; i++) {
			var label = new JLabel(labels[i]);
			label.setBounds(labelX, labelY + i * 50, labelWidth, labelHeight);
			label.setFont(new Font("Arial", Font.PLAIN, 14));
			contentPanel.add(label);

			var textField = new JTextField();
			textField.setBounds(fieldX, fieldY + i * 50, fieldWidth, fieldHeight);

			switch (labels[i]) {
			case "Name:" -> txtName = textField;
			case "Address:" -> txtAddress = textField;
			case "Phone Number:" -> txtPhoneNumber = textField;
			case "Email:" -> txtEmail = textField;
			}

			contentPanel.add(textField);
		}

		// Save Button
		var btnSave = new JButton("Save");
		btnSave.setBounds(100, 300, 90, 30);
		btnSave.addActionListener(this::savePublisherActionPerformed);
		contentPanel.add(btnSave);

		// Cancel Button
		var btnCancel = new JButton("Cancel");
		btnCancel.setBounds(230, 300, 90, 30);
		btnCancel.addActionListener(e -> dispose());
		contentPanel.add(btnCancel);

		// Load Publisher Data
		loadPublisherData();
	}

	private void loadPublisherData() {
		txtName.setText(currentPublisher.getName());
		txtAddress.setText(currentPublisher.getAddress());
		txtPhoneNumber.setText(currentPublisher.getPhoneNumber());
		txtEmail.setText(currentPublisher.getEmail());
	}

	private void savePublisherActionPerformed(ActionEvent e) {
		try {
			// Validate Name
			var name = txtName.getText().trim();
			if (name.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Name is required!", "Validation Error",
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			// Check for duplicate Name
			var dao = new PublisherDao();
			var allPublishers = dao.select();
			for (var pub : allPublishers) {
				if (pub.getName().equalsIgnoreCase(name) && pub.getPublisherID() != currentPublisher.getPublisherID()) {
					JOptionPane.showMessageDialog(this, "Name already exists!", "Validation Error",
							JOptionPane.WARNING_MESSAGE);
					return;
				}
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
			if (!phoneNumber.matches("^[0-9()+-]{1,12}$")) {
				JOptionPane.showMessageDialog(this,
						"Phone Number must be a valid format and contain at most 12 characters!", "Validation Error",
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			// Check for duplicate Phone Number
			for (var pub : allPublishers) {
				if (pub.getPhoneNumber().equals(phoneNumber)
						&& pub.getPublisherID() != currentPublisher.getPublisherID()) {
					JOptionPane.showMessageDialog(this, "Phone Number already exists!", "Validation Error",
							JOptionPane.WARNING_MESSAGE);
					return;
				}
			}

			// Validate Email
			var email = txtEmail.getText().trim();
			if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
				JOptionPane.showMessageDialog(this, "Invalid Email format!", "Validation Error",
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			// Check for duplicate Email
			for (var pub : allPublishers) {
				if (pub.getEmail().equalsIgnoreCase(email)
						&& pub.getPublisherID() != currentPublisher.getPublisherID()) {
					JOptionPane.showMessageDialog(this, "Email already exists!", "Validation Error",
							JOptionPane.WARNING_MESSAGE);
					return;
				}
			}

			// Update PublisherEntity
			currentPublisher.setName(name);
			currentPublisher.setAddress(address);
			currentPublisher.setPhoneNumber(phoneNumber);
			currentPublisher.setEmail(email);

			// Update database
			dao.update(currentPublisher);

			JOptionPane.showMessageDialog(this, "Publisher updated successfully!");
			dispose();

			// Reload the table in the parent form
			if (parentForm != null) {
				parentForm.panelPublisherActivated();
			}

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Failed to update publisher: " + ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}
