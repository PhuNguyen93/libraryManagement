package edit;

import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import dao.CategoriesDao;
import entity.CategoriesEntity;
import main.Author;

public class CategoryEditDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private final JPanel contentPanel = new JPanel();
	private JTextField txtCategoryName, txtDescription;
	private CategoriesEntity currentCategory;
	private Author parentForm; // Tham chiếu tới form cha

	public CategoryEditDialog(Author parentForm, CategoriesEntity category) {
		this.parentForm = parentForm; // Gán tham chiếu tới form cha
		this.currentCategory = category;

		setTitle("Edit Category");
		setBounds(100, 100, 450, 350);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);
		contentPanel.setBounds(0, 0, 434, 311);
		contentPanel.setLayout(null);
		getContentPane().add(contentPanel);

		// Labels and Fields
		String[] labels = { "Category Name:", "Description:" };
		int labelX = 50, labelY = 50, labelWidth = 150, labelHeight = 30;
		int fieldX = 200, fieldY = 50, fieldWidth = 200, fieldHeight = 30;

		for (var i = 0; i < labels.length; i++) {
			var label = new JLabel(labels[i]);
			label.setBounds(labelX, labelY + i * 50, labelWidth, labelHeight);
			label.setFont(new Font("Arial", Font.PLAIN, 14));
			contentPanel.add(label);

			var textField = new JTextField();
			textField.setBounds(fieldX, fieldY + i * 50, fieldWidth, fieldHeight);

			switch (labels[i]) {
			case "Category Name:" -> txtCategoryName = textField;
			case "Description:" -> txtDescription = textField;
			}
			contentPanel.add(textField);
		}

		// Save Button
		var btnSave = new JButton("Save");
		btnSave.setBounds(100, 250, 90, 30);
		btnSave.addActionListener(this::saveCategoryActionPerformed);
		contentPanel.add(btnSave);

		// Cancel Button
		var btnCancel = new JButton("Cancel");
		btnCancel.setBounds(230, 250, 90, 30);
		btnCancel.addActionListener(e -> dispose());
		contentPanel.add(btnCancel);

		// Load Category Data
		loadCategoryData();
	}

	private void loadCategoryData() {
		txtCategoryName.setText(currentCategory.getCategoryName());
		txtDescription.setText(currentCategory.getDescription());
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
			if (existingCategories.contains(categoryName) && !categoryName.equals(currentCategory.getCategoryName())) {
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

			// Update CategoryEntity object
			currentCategory.setCategoryName(categoryName);
			currentCategory.setDescription(description);

			// Update Category in the database
			dao.update(currentCategory);

			JOptionPane.showMessageDialog(this, "Category updated successfully!");
			dispose();

			// Reload the table in the parent form
			if (parentForm != null) {
				parentForm.panelCategoryActivated();
			}

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Failed to save category: " + ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}
