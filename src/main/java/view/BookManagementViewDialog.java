package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.FileOutputStream;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import dao.BookManagementDao;
import entity.BookManagementEntity;

public class BookManagementViewDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanelView = new JPanel();
	private JPanel panelImage;
	private JTextField txtISBN, txtTitle, txtAuthorName, txtPublisherName, txtCategory, txtQuantity, txtStockQuantity,
			txtPrice, txtRentalPrice, txtDepositPercentage, txtFineMultiplier, txtLanguage;

	public BookManagementViewDialog(BookManagementEntity bookManagement) {
		setTitle("Book Details");
		setBounds(100, 100, 800, 630);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		contentPanelView.setBounds(0, 0, 800, 630);
		contentPanelView.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanelView.setBackground(new Color(255, 255, 255)); // Màu nền xanh nhạt
		getContentPane().add(contentPanelView);
		contentPanelView.setLayout(null);

		// Panel for Book Cover Image
		panelImage = new JPanel();
		panelImage.setBounds(20, 20, 340, 460);
		contentPanelView.add(panelImage);

		var btnExcelView = new JButton("");
		btnExcelView.setBounds(348, 506, 57, 53);
		contentPanelView.add(btnExcelView);
		btnExcelView.addActionListener(this::btnExcelViewActionPerformed);
		btnExcelView.setIcon(new ImageIcon(BookManagementViewDialog.class.getResource("/icon4/xls.png")));

		// Close Button
		var closeButton = new JButton("");
		closeButton.setBounds(437, 506, 57, 53);
		contentPanelView.add(closeButton);
		closeButton.setIcon(new ImageIcon(BookManagementViewDialog.class.getResource("/icon5/cross.png")));
		closeButton.addActionListener(this::closeButtonActionPerformed);

		// Labels and Text Fields
		String[] labels = { "ISBN:", "Book Name:", "Author Name:", "Publisher Name:", "Category:", "Quantity:",
				"Stock Quantity:", "Price:", "Rental Price:", "Deposit Percentage:", "Fine Multiplier:", "Language:" };

		int labelX = 390, labelY = 20, labelWidth = 150, labelHeight = 30;
		int fieldX = 560, fieldY = 20, fieldWidth = 200, fieldHeight = 30;

		for (var i = 0; i < labels.length; i++) {
			// Add Label
			var label = new JLabel(labels[i]);
			label.setBounds(labelX, labelY + i * 40, labelWidth, labelHeight);
			label.setFont(new Font("Arial", Font.PLAIN, 14));
			contentPanelView.add(label);

			// Add Text Field
			var textField = new JTextField();
			textField.setBounds(fieldX, fieldY + i * 40, fieldWidth, fieldHeight);
			textField.setEditable(false); // Không cho chỉnh sửa
			textField.setFocusable(false); // Không cho click chuột vào
			textField.setBackground(new Color(240, 248, 255)); // Màu nền xanh nhạt hơn
			textField.setBorder(BorderFactory.createLineBorder(new Color(173, 216, 230))); // Viền xanh
			contentPanelView.add(textField);

			// Map labels to corresponding text fields
			switch (labels[i]) {
			case "ISBN:" -> txtISBN = textField;
			case "Book Name:" -> txtTitle = textField;
			case "Author Name:" -> txtAuthorName = textField;
			case "Publisher Name:" -> txtPublisherName = textField;
			case "Category:" -> txtCategory = textField;
			case "Quantity:" -> txtQuantity = textField;
			case "Stock Quantity:" -> txtStockQuantity = textField;
			case "Price:" -> txtPrice = textField;
			case "Rental Price:" -> txtRentalPrice = textField;
			case "Deposit Percentage:" -> txtDepositPercentage = textField;
			case "Fine Multiplier:" -> txtFineMultiplier = textField;
			case "Language:" -> txtLanguage = textField;
			}
		}

		// Load data into fields
		loadBookManagementData(bookManagement);
	}

	private void loadBookManagementData(BookManagementEntity bookManagement) {
		var dao = new BookManagementDao();
		bookManagement = dao.getBookWithDetails(bookManagement.getIsbn());
		txtISBN.setText(bookManagement.getIsbn());
		txtTitle.setText(bookManagement.getTitle());
		txtAuthorName.setText(bookManagement.getAuthorName());
		txtPublisherName.setText(bookManagement.getPublisherName());
		txtCategory.setText(bookManagement.getCategory());
		txtQuantity.setText(String.valueOf(bookManagement.getQuantity()));
		txtStockQuantity.setText(String.valueOf(bookManagement.getStockQuantity()));
		txtDepositPercentage.setText(String.valueOf(bookManagement.getDepositPercentage()) + " %");
		txtFineMultiplier.setText(String.valueOf(bookManagement.getFineMultiplier()));
		// Định dạng giá trị tiền tệ cho Price
		if (bookManagement.getPrice() != null) {
			txtPrice.setText(String.format("%,.0f VND", bookManagement.getPrice()));
		} else {
			txtPrice.setText("");
		}

		// Định dạng giá trị tiền tệ cho RentalPrice
		if (bookManagement.getRentalPrice() != null) {
			txtRentalPrice.setText(String.format("%,.0f VND", bookManagement.getRentalPrice()));
		} else {
			txtRentalPrice.setText("");
		}

		txtLanguage.setText(bookManagement.getLanguage());

		// Load Image
		var imagePath = "images/" + bookManagement.getImage();
		setImage(imagePath);
	}


	private void setImage(String imagePath) {
		try {
			var resource = "src/main/resources/" + imagePath;
			var imageIcon = new ImageIcon(
					new ImageIcon(resource).getImage().getScaledInstance(330, 450, Image.SCALE_SMOOTH));
			var label = new JLabel(imageIcon);

			panelImage.removeAll();
			panelImage.add(label);
			panelImage.revalidate();
			panelImage.repaint();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error loading image: " + imagePath, "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void closeButtonActionPerformed(ActionEvent e) {
		dispose();
	}

	private void btnExcelViewActionPerformed(ActionEvent e) {
		try {
			var workbook = new XSSFWorkbook();
			var sheet = workbook.createSheet("Book Details");

			var headerRow = sheet.createRow(0);
			String[] headers = { "Field", "Value" };
			for (var i = 0; i < headers.length; i++) {
				var cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);
			}

			var rowNum = 1;
			String[][] data = { { "ISBN", txtISBN.getText() }, { "Title", txtTitle.getText() },
					{ "Author Name", txtAuthorName.getText() }, { "Publisher Name", txtPublisherName.getText() },
					{ "Category", txtCategory.getText() }, { "Quantity", txtQuantity.getText() },
					{ "Stock Quantity", txtStockQuantity.getText() }, { "Price", txtPrice.getText() },
					{ "Rental Price", txtRentalPrice.getText() },
					{ "Deposit Percentage", txtDepositPercentage.getText() },
					{ "Fine Multiplier", txtFineMultiplier.getText() }, { "Language", txtLanguage.getText() } };

			for (String[] rowData : data) {
				var row = sheet.createRow(rowNum++);
				row.createCell(0).setCellValue(rowData[0]);
				row.createCell(1).setCellValue(rowData[1]);
			}

			sheet.autoSizeColumn(0);
			sheet.autoSizeColumn(1);

			var filePath = System.getProperty("user.home") + "/Documents/BookDetails.xlsx";
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
