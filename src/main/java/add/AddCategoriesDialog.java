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

import dao.CategoriesDao;
import entity.CategoriesEntity;
import main.Author;

public class AddCategoriesDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField txtCategoryName, txtDescription;

	private Author parentForm;

	public AddCategoriesDialog(Author parent) {
		this.parentForm = parent;
		setTitle("Add New Category");
		setBounds(100, 100, 450, 300);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		contentPanel.setBounds(0, 0, 434, 261);
		getContentPane().add(contentPanel);
		contentPanel.setLayout(null);

		// Labels and Fields
		String[] labels = { "Category Name:", "Description:" };
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
			case "Category Name:" -> txtCategoryName = textField;
			case "Description:" -> txtDescription = textField;
			}

			contentPanel.add(textField);
		}

		// Save Button
		var btnSave = new JButton("");
		btnSave.setIcon(new ImageIcon(AddCategoriesDialog.class.getResource("/icon3/save.png")));
		btnSave.setBounds(100, 200, 57, 53);
		btnSave.addActionListener(this::saveCategoryActionPerformed);
		contentPanel.add(btnSave);

		// Cancel Button
		var btnCancel = new JButton("");
		btnCancel.setIcon(new ImageIcon(AddCategoriesDialog.class.getResource("/icon5/cross.png")));
		btnCancel.setBounds(200, 200, 57, 53);
		btnCancel.addActionListener(e -> dispose());
		contentPanel.add(btnCancel);
	}

	private void saveCategoryActionPerformed(ActionEvent e) {
		try {
			// Validate Category Name
			var categoryName = txtCategoryName.getText().trim();
			if (categoryName.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Category Name is required!", "Validation Error",
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			// Kiểm tra trùng lặp Category Name
			var dao = new CategoriesDao();
			var existingCategories = dao.getAllCategoryNames();
			if (existingCategories.contains(categoryName)) {
				JOptionPane.showMessageDialog(this, "Category Name already exists!", "Validation Error",
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			// Kiểm tra hợp lệ tên thể loại (chỉ được chứa chữ cái và ký tự đặc biệt)
			if (!categoryName.matches("^[^0-9]+$")) {
				JOptionPane.showMessageDialog(this, "Category Name must not contain numbers!", "Validation Error",
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			// Validate Description
			var description = txtDescription.getText().trim();
			if (description.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Description is required!", "Validation Error",
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			// Create CategoryEntity and set values
			var category = new CategoriesEntity();
			category.setCategoryName(categoryName);
			category.setDescription(description);
			category.setIsDeleted(false);

			// Add to database
			dao.insert(category);

			JOptionPane.showMessageDialog(this, "Category added successfully!");
			parentForm.panelCategoryActivated(); // Refresh parent form
			dispose();
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Failed to add category: " + ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}


}
