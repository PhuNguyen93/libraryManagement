package edit;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import dao.AuthorDao;
import dao.BookManagementDao;
import dao.CategoriesDao;
import dao.PublisherDao;
import entity.BookManagementEntity;
import entity.ComboItem;
import entity.UserSession;
import main.BookManagement;

public class BookManagementEdit extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JPanel panelImage, panelCategories;
	private List<JCheckBox> checkBoxCategories;
	private JTextField txtISBN, txtTitle, txtQuantity, txtStockQuantity, txtPrice, txtRentalPrice, txtDepositPercentage,
			txtFineMultiplier;
	private JComboBox<String> comboLanguage; // Thay đổi từ JTextField sang JComboBox

	private JComboBox<ComboItem> comboAuthors, comboPublishers;
	private JButton btnUploadImage;
	private BookManagement parentForm; // Tham chiếu tới form cha

	private BookManagementEntity currentBook;
	private UserSession userSession;

	public BookManagementEdit(BookManagement parentForm, BookManagementEntity book) {
		this.parentForm = parentForm; // Gán tham chiếu tới form cha
		userSession = UserSession.getInstance();
		this.currentBook = book;
		setTitle("Edit Book");
		setBounds(100, 100, 685, 677);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);
		contentPanel.setBounds(0, 0, 669, 638);
		getContentPane().add(contentPanel);
		contentPanel.setLayout(null);

		// Panel hiển thị ảnh bìa sách
		panelImage = new JPanel();
		panelImage.setBounds(20, 20, 120, 120);
		contentPanel.add(panelImage);

		// Nút upload ảnh
		btnUploadImage = new JButton("");
		btnUploadImage.setIcon(new ImageIcon(BookManagementEdit.class.getResource("/icon5/image-.png")));
		btnUploadImage.setBounds(20, 150, 38, 34);
		btnUploadImage.addActionListener(this::uploadImageActionPerformed);
		contentPanel.add(btnUploadImage);

		// Labels and Fields
		String[] labels = { "ISBN:", "Title:", "Author:", "Publisher:", "Quantity:", "Stock Quantity:", "Price:",
				"Rental Price:", "Deposit Percentage (%):", "Fine Multiplier:", "Language:", "Categories:" };
		int labelX = 150, labelY = 20, labelWidth = 160, labelHeight = 30;
		int fieldX = 330, fieldY = 20, fieldWidth = 250, fieldHeight = 30;

		for (var i = 0; i < labels.length; i++) {
			var label = new JLabel(labels[i]);
			label.setBounds(labelX, labelY + i * 40, labelWidth, labelHeight);
			label.setFont(new Font("Arial", Font.PLAIN, 14));
			contentPanel.add(label);

			var textField = new JTextField();
			textField.setBounds(fieldX, fieldY + i * 40, fieldWidth, fieldHeight);

			switch (labels[i]) {
			case "ISBN:" -> txtISBN = textField;

			case "Title:" -> txtTitle = textField;
			case "Author:" -> {
				comboAuthors = new JComboBox<>();
				comboAuthors.setBounds(fieldX, fieldY + i * 40, fieldWidth, fieldHeight);
				contentPanel.add(comboAuthors);
				textField = null;
			}
			case "Publisher:" -> {
				comboPublishers = new JComboBox<>();
				comboPublishers.setBounds(fieldX, fieldY + i * 40, fieldWidth, fieldHeight);
				contentPanel.add(comboPublishers);
				textField = null;
			}
			case "Categories:" -> {
				panelCategories = new JPanel();
				panelCategories.setLayout(new java.awt.GridLayout(0, 1, 5, 5)); // Bố cục GridLayout
				var scrollPane = new JScrollPane(panelCategories);
				scrollPane.setBounds(fieldX, fieldY + i * 40, fieldWidth, 100);
				scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));
				contentPanel.add(scrollPane);
				textField = null;
			}

			case "Quantity:" -> {
				txtQuantity = textField;
				txtQuantity.setEditable(false);
				txtQuantity.setEnabled(false);
			}
			case "Stock Quantity:" -> {
				txtStockQuantity = textField;
				txtStockQuantity.setEditable(false);
				txtStockQuantity.setEnabled(false);
			}
			case "Price:" -> txtPrice = textField;
			case "Rental Price:" -> txtRentalPrice = textField;
			case "Deposit Percentage (%):" -> {
				txtDepositPercentage = textField; // Thêm trường Deposit Percentage
				if (userSession.getUserRole() != 2) {
					txtDepositPercentage.setEnabled(false);
				}
			}
			case "Fine Multiplier:" -> {
				txtFineMultiplier = textField; // Thêm trường Deposit Percentage
				if (userSession.getUserRole() != 2) {
					txtFineMultiplier.setEnabled(false);
				}
			}
			case "Language:" -> {
				comboLanguage = new JComboBox<>(new String[] { "Tiếng Việt", "Tiếng Anh" }); // Tạo ComboBox với hai giá
																								// trị
				comboLanguage.setBounds(fieldX, fieldY + i * 40, fieldWidth, fieldHeight);
				comboLanguage.setFont(new Font("Arial", Font.PLAIN, 14));
				contentPanel.add(comboLanguage);
				textField = null; // Không thêm TextField
			}
			}

			if (textField != null) {
				contentPanel.add(textField);
			}
		}

		// Save Button
		var btnSave = new JButton("");
		btnSave.setIcon(new ImageIcon(BookManagementEdit.class.getResource("/icon3/save.png")));
		btnSave.setBounds(139, 560, 57, 53);
		btnSave.addActionListener(this::saveBookActionPerformed);
		contentPanel.add(btnSave);

		// Cancel Button
		var btnCancel = new JButton("");
		btnCancel.setIcon(new ImageIcon(BookManagementEdit.class.getResource("/icon5/cross.png")));
		btnCancel.setBounds(231, 560, 57, 53);
		btnCancel.addActionListener(e -> dispose());
		contentPanel.add(btnCancel);

		// Load data
		loadAuthors();
		loadPublishers();
		loadCategories();
		loadBookData();
	}

	private void loadAuthors() {
		var dao = new AuthorDao();
		var authors = dao.getAllAuthors();
		for (ComboItem author : authors) {
			comboAuthors.addItem(author);
			if (author.getId() == currentBook.getAuthorID()) {
				comboAuthors.setSelectedItem(author);
			}
		}
	}

	private void loadPublishers() {
		var dao = new PublisherDao();
		var publishers = dao.getAllPublishers();
		for (ComboItem publisher : publishers) {
			comboPublishers.addItem(publisher);
			if (publisher.getId() == currentBook.getPublisherID()) {
				comboPublishers.setSelectedItem(publisher);
			}
		}
	}

	private void loadCategories() {
		var dao = new CategoriesDao();
		var categories = dao.getAllCategories();
		checkBoxCategories = new ArrayList<>();

		panelCategories.setLayout(new java.awt.GridLayout(0, 1, 5, 5));
		for (ComboItem category : categories) {
			var checkBox = new JCheckBox(category.getName());
			checkBox.putClientProperty("id", category.getId());
			checkBox.setSelected(
					currentBook.getCategory() != null && currentBook.getCategory().contains(category.getName()));
			checkBoxCategories.add(checkBox);
			panelCategories.add(checkBox);
		}

		panelCategories.revalidate();
		panelCategories.repaint();
	}

	private void loadBookData() {
		txtISBN.setText(currentBook.getIsbn());
		txtISBN.setEditable(false); // ISBN không được chỉnh sửa
		txtISBN.setFocusable(false); // Ngăn không cho click chuột vào ISBN
		txtTitle.setText(currentBook.getTitle());
		txtQuantity.setText(String.valueOf(currentBook.getQuantity()));
		txtStockQuantity.setText(String.valueOf(currentBook.getStockQuantity()));
		txtPrice.setText(currentBook.getPrice() != null ? currentBook.getPrice().toString() : "");
		txtRentalPrice.setText(currentBook.getRentalPrice() != null ? currentBook.getRentalPrice().toString() : "");
		txtDepositPercentage.setText(
				currentBook.getDepositPercentage() != null ? currentBook.getDepositPercentage().toString() : "");
		txtFineMultiplier.setText(
				currentBook.getFineMultiplier() != null ? currentBook.getFineMultiplier().toString() : "");
		comboLanguage.setSelectedItem(currentBook.getLanguage() != null ? currentBook.getLanguage().toString() : "");

		// Hiển thị ảnh bìa sách
		if (currentBook.getImage() != null) {
			setImage("src/main/resources/images/" + currentBook.getImage());
		}
	}

	private void setImage(String imagePath) {
		try {
			var imageIcon = new ImageIcon(
					new ImageIcon(imagePath).getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH));
			var label = new JLabel(imageIcon);
			panelImage.removeAll();
			panelImage.add(label);
			panelImage.revalidate();
			panelImage.repaint();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Failed to load image: " + imagePath, "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void uploadImageActionPerformed(ActionEvent e) {
		var fileChooser = new javax.swing.JFileChooser();
		fileChooser.setDialogTitle("Select Book Cover Image");
		fileChooser.setFileSelectionMode(javax.swing.JFileChooser.FILES_ONLY);
		var fileFilter = new javax.swing.filechooser.FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png",
				"gif");
		fileChooser.setFileFilter(fileFilter);

		if (fileChooser.showOpenDialog(this) == javax.swing.JFileChooser.APPROVE_OPTION) {
			var selectedFile = fileChooser.getSelectedFile();
			var sourcePath = selectedFile.getAbsolutePath();

			var targetFolder = "src/main/resources/images/";
			var folder = new java.io.File(targetFolder);
			if (!folder.exists()) {
				folder.mkdirs();
			}

			var extension = selectedFile.getName().substring(selectedFile.getName().lastIndexOf('.'));
			var uniqueFileName = System.currentTimeMillis() + extension;
			var targetPath = targetFolder + uniqueFileName;

			try {
				java.nio.file.Files.copy(java.nio.file.Paths.get(sourcePath), java.nio.file.Paths.get(targetPath),
						java.nio.file.StandardCopyOption.REPLACE_EXISTING);
				setImage(targetPath);
				currentBook.setImage(uniqueFileName); // Cập nhật ảnh bìa
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Failed to upload image: " + ex.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void saveBookActionPerformed(ActionEvent e) {
		try {
			// Kiểm tra dữ liệu nhập cho ISBN
			var isbn = txtISBN.getText().trim();
			if (isbn.isEmpty()) {
				JOptionPane.showMessageDialog(this, "ISBN is required!", "Validation Error",
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			// Kiểm tra trùng lặp ISBN
			var dao = new BookManagementDao();
			var existingBook = dao.selectByIsbn(isbn);
			if (existingBook != null && existingBook.getBookID() != currentBook.getBookID()) {
				JOptionPane.showMessageDialog(this, "ISBN already exists!", "Validation Error",
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			// Kiểm tra tiêu đề sách
			var title = txtTitle.getText().trim();
			if (title.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Title is required!", "Validation Error",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			var depositPercentage = new BigDecimal(txtDepositPercentage.getText().trim());
			if (depositPercentage.compareTo(BigDecimal.ZERO) < 0
					|| depositPercentage.compareTo(new BigDecimal(300)) > 0) {
				JOptionPane.showMessageDialog(this, "Deposit Percentage must be between 0 and 300!", "Validation Error",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			var fineMultiplier = new BigDecimal(txtFineMultiplier.getText().trim());
			if (fineMultiplier.compareTo(BigDecimal.ZERO) < 1) {
				JOptionPane.showMessageDialog(this, "Fine Multiplier must be a positive number!", "Validation Error",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			// Kiểm tra ngôn ngữ chỉ được nhập chữ
			var language = (String) comboLanguage.getSelectedItem(); // Lấy giá trị được chọn từ ComboBox

			// Kiểm tra các trường số
			try {
				var quantity = Integer.parseInt(txtQuantity.getText().trim());
				var stockQuantity = Integer.parseInt(txtStockQuantity.getText().trim());
				var price = new BigDecimal(txtPrice.getText().trim());
				var rentalPrice = new BigDecimal(txtRentalPrice.getText().trim());

				if (quantity < 0 || stockQuantity < 0 || price.compareTo(BigDecimal.ZERO) < 0
						|| rentalPrice.compareTo(BigDecimal.ZERO) < 0) {
					JOptionPane.showMessageDialog(this,
							"Quantity, Stock Quantity, Price, and Rental Price must be positive numbers!",
							"Validation Error", JOptionPane.WARNING_MESSAGE);
					return;
				}

				// Lưu các giá trị hợp lệ
				currentBook.setQuantity(quantity);
				currentBook.setStockQuantity(stockQuantity);
				currentBook.setPrice(price);
				currentBook.setRentalPrice(rentalPrice);
				currentBook.setDepositPercentage(depositPercentage);
				currentBook.setFineMultiplier(fineMultiplier);
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this,
						"Quantity, Stock Quantity, Price, and Rental Price must be valid numbers!", "Validation Error",
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			// Lưu các giá trị khác
			currentBook.setIsbn(isbn);
			currentBook.setTitle(title);
			currentBook.setLanguage(language);

			// Lưu tác giả và nhà xuất bản
			var authorId = ((ComboItem) comboAuthors.getSelectedItem()).getId();
			var publisherId = ((ComboItem) comboPublishers.getSelectedItem()).getId();
			currentBook.setAuthorID(authorId);
			currentBook.setPublisherID(publisherId);

			var selectedCategoryIds = getSelectedCategoryIds();
			if (selectedCategoryIds.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Please select at least one Category!", "Validation Error",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			// Lưu danh mục
			var categorydao = new CategoriesDao();
			categorydao.deleteBookCategory(currentBook.getBookID());
			var selectedCategories = new StringBuilder();
			for (JCheckBox checkBox : checkBoxCategories) {
				if (checkBox.isSelected()) {
					selectedCategories.append(checkBox.getText()).append(", ");
					var categoryId = (Integer) checkBox.getClientProperty("id");
					dao.insertBookCategory(currentBook.getBookID(), categoryId);

				}
			}
			if (selectedCategories.length() > 0) {
				selectedCategories.setLength(selectedCategories.length() - 2);
			}
			currentBook.setCategory(selectedCategories.toString());
			
			// Cập nhật vào cơ sở dữ liệu
			dao.update(currentBook);
			

			JOptionPane.showMessageDialog(this, "Book updated successfully!");
			dispose();

			if (parentForm != null) {
				parentForm.reloadTable();
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Failed to save book: " + ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private List<Integer> getSelectedCategoryIds() {
		List<Integer> categoryIds = new ArrayList<>();
		for (JCheckBox checkBox : checkBoxCategories) {
			if (checkBox.isSelected()) {
				categoryIds.add((Integer) checkBox.getClientProperty("id"));
			}
		}
		return categoryIds;
	}

}
