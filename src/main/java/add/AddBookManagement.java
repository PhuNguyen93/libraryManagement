package add;

import java.awt.Color;
import java.awt.Font;
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
import main.Chart;

public class AddBookManagement extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JPanel panelImage;
	private JPanel panelCategories; // Panel chứa các JCheckBox cho Categories
	private List<JCheckBox> checkBoxCategories; // Danh sách JCheckBox cho Categories
	private JTextField txtISBN, txtTitle, txtQuantity, txtStockQuantity, txtPrice, txtRentalPrice, txtDepositPercentage,
			txtFineMultiplier;
	private JComboBox<String> comboLanguage; // Thay đổi từ JTextField sang JComboBox

	private JButton btnUploadImage;
	private String imagePath = null;

	private BookManagement parentForm;
	private JComboBox<ComboItem> comboAuthors;
	private JComboBox<ComboItem> comboPublishers;
	private UserSession userSession;
	private Chart chart;

	public AddBookManagement(BookManagement parent, Chart chart) {
		this.parentForm = parent;
		this.chart = chart;
		userSession = UserSession.getInstance();
		setTitle("Add New Book");
		setBounds(100, 100, 685, 630);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		contentPanel.setBounds(0, 0, 669, 600);
		getContentPane().add(contentPanel);
		contentPanel.setLayout(null);

		// Panel Image
		panelImage = new JPanel();
		panelImage.setBounds(20, 20, 100, 100);
		panelImage.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		contentPanel.add(panelImage);

		// Upload Image Button
		btnUploadImage = new JButton("");
		btnUploadImage.setIcon(new ImageIcon(AddBookManagement.class.getResource("/icon5/image-.png")));
		var uploadIcon = AddBookManagement.class.getResource("/icon5/upload.png");
		if (uploadIcon != null) {
			btnUploadImage.setIcon(new ImageIcon(uploadIcon));
		} else {
		}
		btnUploadImage.setBounds(20, 123, 37, 34);
		btnUploadImage.addActionListener(this::uploadImageActionPerformed);
		contentPanel.add(btnUploadImage);

		// Labels and Fields
		String[] labels = { "ISBN:", "Title:", "Author:", "Publisher:", "Quantity:", "Stock Quantity:", "Price:",
				"Rental Price:", "Deposit Percentage (%):", "Fine Multiplier:", "Language:", "Categories:" }; // Thêm
																												// Deposit
																												// Percentage
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

			case "Quantity:" -> txtQuantity = textField;
			case "Stock Quantity:" -> {
				txtStockQuantity = textField;
				txtStockQuantity.setEditable(false);
				txtStockQuantity.setEnabled(false);
			}
			case "Price:" -> txtPrice = textField;
			case "Rental Price:" -> txtRentalPrice = textField;
			case "Deposit Percentage (%):" -> {
				txtDepositPercentage = textField; // Thêm trường Deposit Percentage
				txtDepositPercentage.setText("80.00");
				if (userSession.getUserRole() != 2) {
					txtDepositPercentage.setEnabled(false);
				}
			}
			case "Fine Multiplier:" -> {
				txtFineMultiplier = textField; // Thêm trường Deposit Percentage
				txtFineMultiplier.setText("1.5");
				if (userSession.getUserRole() != 2) {
					txtFineMultiplier.setEnabled(false);
				}
			}
			case "Language:" -> {
			    comboLanguage = new JComboBox<>(new String[] { "Tiếng Việt", "Tiếng Anh" }); // Tạo ComboBox với hai giá trị
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
		txtQuantity.addKeyListener(new java.awt.event.KeyAdapter() {
			@Override
			public void keyReleased(java.awt.event.KeyEvent e) {
				var input = txtQuantity.getText().trim();
				txtStockQuantity.setText(input);
			}
		});
		// Save Button
		var btnSave = new JButton("");
		btnSave.setIcon(new ImageIcon(AddBookManagement.class.getResource("/icon3/save.png")));
		var saveIcon = AddBookManagement.class.getResource("/icon3/save.png");
		if (saveIcon != null) {
			btnSave.setIcon(new ImageIcon(saveIcon));
		} else {
		}
		btnSave.setBounds(134, 525, 57, 53);
		btnSave.addActionListener(this::saveBookActionPerformed);
		contentPanel.add(btnSave);

		// Cancel Button
		var btnCancel = new JButton("");
		btnCancel.setIcon(new ImageIcon(AddBookManagement.class.getResource("/icon5/cross.png")));
		var cancelIcon = AddBookManagement.class.getResource("/icon5/cancel.png");
		if (cancelIcon != null) {
			btnCancel.setIcon(new ImageIcon(cancelIcon));
		} else {
		}
		btnCancel.setBounds(223, 525, 57, 53);
		btnCancel.addActionListener(e -> dispose());
		contentPanel.add(btnCancel);

		// Load data
		loadAuthors();
		loadPublishers();
		loadCategories();
	}

	private void loadAuthors() {
		var dao = new AuthorDao();
		var authors = dao.getAllAuthors();
		for (ComboItem author : authors) {
			comboAuthors.addItem(author);
		}
	}

	private void loadPublishers() {
		var dao = new PublisherDao();
		var publishers = dao.getAllPublishers();
		for (ComboItem publisher : publishers) {
			comboPublishers.addItem(publisher);
		}
	}

	private void loadCategories() {
		var dao = new CategoriesDao();
		var categories = dao.getAllCategories();
		checkBoxCategories = new ArrayList<>();

		// Tạo GridLayout với số cột là 1 để xếp checkbox theo chiều dọc
		panelCategories.setLayout(new java.awt.GridLayout(0, 1, 5, 5)); // 5px khoảng cách giữa các checkbox

		for (ComboItem category : categories) {
			var checkBox = new JCheckBox(category.getName());
			checkBox.putClientProperty("id", category.getId());
			checkBox.putClientProperty("name", category.getName());
			checkBoxCategories.add(checkBox);
			panelCategories.add(checkBox);
		}

		// Đảm bảo panelCategories hiển thị đúng
		panelCategories.revalidate();
		panelCategories.repaint();
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

	private List<String> getSelectedCategoryName() {
		List<String> categoryNames = new ArrayList<>();
		for (JCheckBox checkBox : checkBoxCategories) {
			if (checkBox.isSelected()) {
				categoryNames.add((String) checkBox.getClientProperty("name"));
			}
		}
		return categoryNames;
	}
	private int getSelectedAuthorId() {
		var selectedAuthor = (ComboItem) comboAuthors.getSelectedItem();
		return selectedAuthor != null ? selectedAuthor.getId() : -1;
	}

	private int getSelectedPublisherId() {
		var selectedPublisher = (ComboItem) comboPublishers.getSelectedItem();
		return selectedPublisher != null ? selectedPublisher.getId() : -1;
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
				imagePath = uniqueFileName;
				setImage(targetPath);
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Failed to upload image: " + ex.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void setImage(String imagePath) {
		try {
			var imageIcon = new ImageIcon(
					new ImageIcon(imagePath).getImage().getScaledInstance(90, 90, java.awt.Image.SCALE_SMOOTH));
			var label = new JLabel(imageIcon);
			panelImage.removeAll();
			panelImage.add(label);
			panelImage.revalidate();
			panelImage.repaint();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void saveBookActionPerformed(ActionEvent e) {
		try {
			// Kiểm tra ISBN
			var isbn = txtISBN.getText().trim();
			if (isbn.isEmpty()) {
				JOptionPane.showMessageDialog(this, "ISBN is required!", "Validation Error",
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			// Kiểm tra trùng lặp ISBN
			var dao = new BookManagementDao();
			var existingBook = dao.selectByIsbn(isbn); // Hàm selectByIsbn cần được thêm trong DAO
			if (existingBook != null) {
				JOptionPane.showMessageDialog(this, "ISBN already exists!", "Validation Error",
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			// Kiểm tra tiêu đề
			var title = txtTitle.getText().trim();
			if (title.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Title is required!", "Validation Error",
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			// Kiểm tra ngôn ngữ
			var language = (String) comboLanguage.getSelectedItem(); // Lấy giá trị được chọn từ ComboBox

			// Kiểm tra các trường số
			try {
				var quantity = Integer.parseInt(txtQuantity.getText().trim());
				var stockQuantity = Integer.parseInt(txtStockQuantity.getText().trim());
				var price = new BigDecimal(txtPrice.getText().trim());
				var rentalPrice = new BigDecimal(txtRentalPrice.getText().trim());
				var depositPercentage = new BigDecimal(txtDepositPercentage.getText().trim());

				if (quantity < 0 || stockQuantity < 0 || price.compareTo(BigDecimal.ZERO) < 0
						|| rentalPrice.compareTo(BigDecimal.ZERO) < 0
						|| depositPercentage.compareTo(BigDecimal.ZERO) <= 0) {
					JOptionPane.showMessageDialog(this,
							"Quantity, Stock Quantity, Price, Rental Price, and depositPercentage must be positive numbers!",
							"Validation Error", JOptionPane.WARNING_MESSAGE);
					return;
				}
				if (depositPercentage.compareTo(BigDecimal.ZERO) < 0
						|| depositPercentage.compareTo(new BigDecimal(300)) > 0) {
					JOptionPane.showMessageDialog(this, "Deposit Percentage must be between 0 and 300!",
							"Validation Error", JOptionPane.WARNING_MESSAGE);
					return;
				}
				var fineMultiplier = new BigDecimal(txtFineMultiplier.getText().trim());
				if (fineMultiplier.compareTo(BigDecimal.ZERO) < 1) {
					JOptionPane.showMessageDialog(this, "Fine Multiplier must be a positive number!",
							"Validation Error", JOptionPane.WARNING_MESSAGE);
					return;
				}

				// Gán giá trị cho BookEntity
				var book = new BookManagementEntity();
				book.setIsbn(isbn);
				book.setTitle(title);
				book.setLanguage(language);
				book.setQuantity(quantity);
				book.setStockQuantity(stockQuantity);
				book.setPrice(price);
				book.setRentalPrice(rentalPrice);
				book.setDepositPercentage(depositPercentage);
				book.setFineMultiplier(fineMultiplier); // Gán giá trị FineMultiplier

				// Kiểm tra Author và Publisher
				var authorId = getSelectedAuthorId();
				var publisherId = getSelectedPublisherId();
				if (authorId == -1 || publisherId == -1) {
					JOptionPane.showMessageDialog(this, "Please select both Author and Publisher!", "Validation Error",
							JOptionPane.WARNING_MESSAGE);
					return;
				}
				book.setAuthorID(authorId);
				book.setPublisherID(publisherId);

				// Lấy danh mục đã chọn
				var selectedCategoryIds = getSelectedCategoryIds();
				if (selectedCategoryIds.isEmpty()) {
					JOptionPane.showMessageDialog(this, "Please select at least one Category!", "Validation Error",
							JOptionPane.WARNING_MESSAGE);
					return;
				}
				var categories = new StringBuilder();
				for (String category : getSelectedCategoryName()) {
					categories.append(category).append(", ");
				}
				if (categories.length() > 0) {
					categories.setLength(categories.length() - 2); // Xóa dấu phẩy cuối
				}
				book.setCategory(categories.toString());

				// Thêm ảnh (nếu có)
				book.setImage(imagePath);

				// Thêm sách vào cơ sở dữ liệu
				dao.insert(book);

				// Lưu danh mục vào bảng BookCategories
				var bookId = dao.getLastInsertedBookId(); // Hàm cần thêm trong DAO
				for (var categoryId : selectedCategoryIds) {
					dao.insertBookCategory(bookId, categoryId);
				}

				JOptionPane.showMessageDialog(this, "Book added successfully!");
				parentForm.reloadTable();
				// update chart
				if (chart != null) {
					chart.refreshData();
				}
				dispose();
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this,
						"Quantity, Stock Quantity, Price, and Rental Price must be valid numbers!", "Validation Error",
						JOptionPane.WARNING_MESSAGE);
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Failed to add book: " + ex.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "An unexpected error occurred: " + ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

}
