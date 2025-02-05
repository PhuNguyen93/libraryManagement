package dialog;

import java.awt.Color;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

import dao.BookManagementDao;
import dao.BorrowHistoryDao;
import dao.BorrowRecordsDao;
import dao.PaymentDao;
import dao.StudentDao;
import entity.BookManagementEntity;
import entity.BorrowHistoryEntity;
import entity.BorrowRecordsEntity;
import entity.PaymentEntity;
import entity.UserSession;
import main.Chart;
import main.ReloadDataListener;
import service.ConnectDB;

public class CashPaymentDialog extends JDialog {
	private static final long serialVersionUID = 1L;

	private JTextField textTotalAmount;
	private JTextField textCustomerPay;
	private JTextField textChange;
	private JButton btnPaid;
	private JButton btnCancel;
	private DefaultTableModel glRentalModel; // Rental book table model
	private String glStudentCD; // Student code
	private UserSession userSession;
	private Window parentForm;
	private ReloadDataListener reloadDataListener;
	private String recipientEmail;
	private Chart chart;
	private final Map<Integer, Boolean> bookExistsCache = new HashMap<>();

	public CashPaymentDialog(Window parent, double totalAmount, String studentCD, DefaultTableModel rentalModel,
			ReloadDataListener listener, Chart chart) {
		super(parent, "Cash Payment", ModalityType.DOCUMENT_MODAL);
		this.reloadDataListener = listener;
		this.chart = chart;

		setSize(400, 300);
		setLocationRelativeTo(parent);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);

		this.parentForm = parent;
		this.glRentalModel = rentalModel;
		this.glStudentCD = studentCD;
		userSession = UserSession.getInstance();

		// Label: Total Amount
		var lblTotalAmount = new JLabel("Total Amount:");
		lblTotalAmount.setFont(new Font("Arial", Font.BOLD, 14));
		lblTotalAmount.setBounds(30, 30, 120, 30);
		getContentPane().add(lblTotalAmount);

		// TextField: Total Amount (read-only)
		textTotalAmount = new JTextField(String.format("%,.0f", totalAmount));
		textTotalAmount.setEnabled(false);
		textTotalAmount.setEditable(false);
		textTotalAmount.setFont(new Font("Arial", Font.PLAIN, 14));
		textTotalAmount.setBounds(160, 30, 200, 30);
		textTotalAmount.setBackground(new Color(224, 255, 255));
		getContentPane().add(textTotalAmount);

		// Label: Customer Pay
		var lblCustomerPay = new JLabel("Customer Pay:");
		lblCustomerPay.setFont(new Font("Arial", Font.BOLD, 14));
		lblCustomerPay.setBounds(30, 80, 120, 30);
		getContentPane().add(lblCustomerPay);

		// TextField: Customer Pay Input
		textCustomerPay = new JTextField();
		textCustomerPay.setFont(new Font("Arial", Font.PLAIN, 14));
		textCustomerPay.setBounds(160, 80, 200, 30);
		textCustomerPay.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				formatCustomerPayInput();
				calculateChange();
			}
		});
		getContentPane().add(textCustomerPay);

		// Label: Change
		var lblChange = new JLabel("Change:");
		lblChange.setFont(new Font("Arial", Font.BOLD, 14));
		lblChange.setBounds(30, 130, 120, 30);
		getContentPane().add(lblChange);

		// TextField: Change (read-only)
		textChange = new JTextField("0.00");
		textChange.setEnabled(false);
		textChange.setEditable(false);
		textChange.setFont(new Font("Arial", Font.PLAIN, 14));
		textChange.setBounds(160, 130, 200, 30);
		textChange.setBackground(new Color(224, 255, 255));
		getContentPane().add(textChange);

		// Button: Paid
		btnPaid = new JButton("");
		btnPaid.setIcon(new ImageIcon(CashPaymentDialog.class.getResource("/icon7/asset.png")));
		btnPaid.setFont(new Font("Arial", Font.BOLD, 14));
		btnPaid.setBounds(118, 200, 52, 40);
		btnPaid.addActionListener(this::handlePaidAction);
		btnPaid.setEnabled(false); // Disable until valid data is entered
		getContentPane().add(btnPaid);

		// Button: Cancel
		btnCancel = new JButton("");
		btnCancel.setIcon(new ImageIcon(CashPaymentDialog.class.getResource("/icon5/cross.png")));
		btnCancel.setFont(new Font("Arial", Font.BOLD, 14));
		btnCancel.setBounds(220, 200, 52, 40);
		btnCancel.addActionListener(e -> dispose());
		getContentPane().add(btnCancel);
	}

	private void formatCustomerPayInput() {
		try {
			var input = textCustomerPay.getText().replaceAll(",", "");
			if (!input.isEmpty()) {
				var value = Double.parseDouble(input);
				var formatted = String.format("%,.0f", value);
				textCustomerPay.setText(formatted);
			}
		} catch (NumberFormatException ex) {
			// Do nothing if an error occurs
		}
	}

	private void calculateChange() {
		try {
			var decimalFormat = new DecimalFormat();
			decimalFormat.setGroupingUsed(true);
			decimalFormat.setParseBigDecimal(true);

			var totalAmount = decimalFormat.parse(textTotalAmount.getText()).doubleValue();
			var customerPay = decimalFormat.parse(textCustomerPay.getText()).doubleValue();
			var change = customerPay - totalAmount;

			textChange.setText(String.format("%,.2f", change));
			btnPaid.setEnabled(change >= 0);
		} catch (ParseException | NumberFormatException ex) {
			textChange.setText("Invalid");
			btnPaid.setEnabled(false);
		}
	}

	private void handlePaidAction(ActionEvent e) {
		try {
			if (glRentalModel == null || glRentalModel.getRowCount() == 0) {
				JOptionPane.showMessageDialog(this, "Rental data is not available or empty.", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			// Lấy UserID từ UserSession

			// Lấy thông tin sinh viên
			var studentDao = new StudentDao();
			var student = studentDao.selectByStudentCd(glStudentCD);
			if (student == null) {
				JOptionPane.showMessageDialog(this, "Student not found. Please check the student code.", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			recipientEmail = student.getEmail();
			if (recipientEmail == null || recipientEmail.trim().isEmpty()) {
				JOptionPane.showMessageDialog(this, "Student email is not available.", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			// Tạo và lưu Payment
			var payment = initializePayment(student.getStudentID());
			var paymentDao = new PaymentDao();
			paymentDao.insertPayment(payment);

			// Lấy PaymentID
			var paymentID = paymentDao.getLastInsertedPaymentID();
			if (paymentID == -1) {
				JOptionPane.showMessageDialog(this, "Failed to retrieve PaymentID.", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			// Kiểm tra và tạo danh sách BorrowRecords
			var borrowRecords = initializeBorrowRecords(paymentID);

			// Lưu BorrowRecords vào cơ sở dữ liệu
			var borrowRecordsDao = new BorrowRecordsDao(ConnectDB.getCon());

			var quantity = student.getTotalBooksRented();
			for (BorrowRecordsEntity record : borrowRecords) {
				quantity = quantity + record.getQuantity();
				if (!updateBook(record)) {
					return;
				}

				if (!borrowRecordsDao.insert(record)) {
					JOptionPane.showMessageDialog(this,
							"Failed to save rental record for BookID: " + record.getBookID(), "Error",
							JOptionPane.ERROR_MESSAGE);
				}

				insertHistory(borrowRecordsDao.getMaxRecordID());

			}
			student.setTotalBooksRented(quantity);
			student.setTotalOrders(student.getTotalOrders() + 1);
			studentDao.updateStudentStatistics(student, userSession.getUserId());
			if (chart != null) {
				chart.refreshData();
			}
			sendConfirmationEmail(); // Gọi phương thức gửi email
			JOptionPane.showMessageDialog(this, "Payment and borrow records saved successfully!", "Success",
					JOptionPane.INFORMATION_MESSAGE);
			dispose();
			this.setVisible(false);
			parentForm.dispose();
			parentForm.setVisible(false);
			// Gọi phương thức reloadData() để reload lại JTable trong BookRental
			if (reloadDataListener != null) {
				reloadDataListener.reloadData();
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, "An error occurred: " + ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private PaymentEntity initializePayment(int studentID) {
		var payment = new PaymentEntity();
		payment.setUserID(userSession.getUserId()); // Lấy từ UserSession
		payment.setStudentID(studentID); // Gán StudentID liên quan
		payment.setAmount(Double.parseDouble(textTotalAmount.getText().replace(",", "")));
		payment.setPaymentMethod("Cash");
		payment.setDescription("Payment for book rental");
		payment.setAmountGiven(Double.parseDouble(textCustomerPay.getText().replace(",", "")));
		payment.setTotalOrderAmount(Double.parseDouble(textTotalAmount.getText().replace(",", "")));
		payment.setChangeAmount(Double.parseDouble(textChange.getText().replace(",", "")));
		payment.setDeleted(false);
		return payment;
	}

	private List<BorrowRecordsEntity> initializeBorrowRecords(int paymentID) {
		List<BorrowRecordsEntity> borrowRecords = new ArrayList<>();
		for (var i = 0; i < glRentalModel.getRowCount(); i++) {
			try {
				// Kiểm tra và lấy BookID từ cột tương ứng
				var isbnValue = (String) glRentalModel.getValueAt(i, 0);

				var bookdao = new BookManagementDao();
				var book = new BookManagementEntity();
				book = bookdao.getBookWithDetails(isbnValue);
				// Tạo bản ghi BorrowRecordsEntity
				var record = new BorrowRecordsEntity();
				record.setUserID(userSession.getUserId());
				record.setBookID(book.getBookID());
				record.setQuantity(Integer.parseInt(glRentalModel.getValueAt(i, 2).toString())); // Cột Quantity
				record.setNumberOfDays(Integer.parseInt(glRentalModel.getValueAt(i, 4).toString())); // Cột NumberOfDays
				record.setBorrowDate(new java.sql.Date(System.currentTimeMillis()));
				record.setDueReturnDate(new java.sql.Date(
						System.currentTimeMillis() + record.getNumberOfDays() * 24L * 60 * 60 * 1000));
				record.setStatus("Borrowed");
				record.setFineAmount(0.0);
				record.setBorrowPrice(
						Double.parseDouble(glRentalModel.getValueAt(i, 3).toString()) * record.getQuantity());
				record.setDeleted(false);
				record.setPaymentID(paymentID);
				record.setCreatedBy(userSession.getUserId());
				record.setUpdatedBy(userSession.getUserId());

				borrowRecords.add(record);
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "Invalid data at row " + (i + 1) + ": " + ex.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
		return borrowRecords;
	}

	private boolean checkBookExists(int bookID) {
		if (bookExistsCache.containsKey(bookID)) {
			return bookExistsCache.get(bookID);
		}
		try (var con = ConnectDB.getCon(); var ps = con.prepareStatement("SELECT 1 FROM Books WHERE BookID = ?")) {
			ps.setInt(1, bookID);
			try (var rs = ps.executeQuery()) {
				var exists = rs.next();
				bookExistsCache.put(bookID, exists);
				return exists;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	private boolean updateBook(BorrowRecordsEntity record) {
		var bookdao = new BookManagementDao();
		var book = bookdao.getBookWithDetails(record.getBookID());
		if (record.getQuantity() > book.getQuantity()) {
			JOptionPane.showMessageDialog(this, "Out of Stock" + record.getBookID(), "Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		var newStockQuantity = book.getStockQuantity();
		var newQuantity = book.getQuantity() - record.getQuantity();
		if (!bookdao.updateBookStockRetal(book.getBookID(), newStockQuantity, newQuantity)) {
			JOptionPane.showMessageDialog(this, "Failed! BookID: " + record.getBookID() + " is out of stock  ", "Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}

	private void insertHistory(int id) {
		var borrowhisdao = new BorrowHistoryDao();
		var bookHis = new BorrowHistoryEntity();
		bookHis.setRecordID(id);
		bookHis.setAction("Borrowed");
		bookHis.setActionDate(LocalDateTime.now());
		bookHis.setDeleted(false);
		if (!borrowhisdao.insert(bookHis)) {
			JOptionPane.showMessageDialog(this, "Failed to save record history", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void sendConfirmationEmail() {
		final var senderEmail = "nguyenphu0809@gmail.com"; // Sender's email address
		final var senderPassword = "yykh yooo tfnt wgmt"; // Sender's email password (use App Password if using Gmail
															// with 2FA)
		final var host = "smtp.gmail.com"; // SMTP host for Gmail

		var props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", "587"); // SMTP port for TLS

		var session = Session.getInstance(props, new javax.mail.Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(senderEmail, senderPassword);
			}
		});

		// Enable debug mode to view detailed logs in the console
		session.setDebug(true);

		// Create a SwingWorker to send the email in the background
		SwingWorker<Void, Void> emailWorker = new SwingWorker<>() {
			@Override
			protected Void doInBackground() throws Exception {
				try {
					Message message = new MimeMessage(session);
					message.setFrom(new InternetAddress(senderEmail)); // Set sender's email
					message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail)); // Set
																											// recipient's
																											// email
					message.setSubject("Library Book Borrowing Invoice"); // Email subject

					// Get the total amount paid
					var totalAmount = textTotalAmount.getText();

					// Get the amount given by the customer
					var amountGiven = textCustomerPay.getText();

					// Get the change amount
					var changeAmount = textChange.getText();

					// Get the current date and time
					var currentDate = java.time.LocalDate.now().toString();
					var currentTime = java.time.LocalTime.now().withNano(0).toString();

					// Build the email content as HTML to resemble an invoice
					var messageBuilder = new StringBuilder();
					messageBuilder.append("<html>").append("<body>")
							.append("<h1 style='text-align: center;'>Library Book Borrowing Invoice</h1>")
							.append("<p><strong>Date:</strong> ").append(currentDate).append(" ").append(currentTime)
							.append("</p>").append("<hr>").append("<h2>Customer Information</h2>")
							.append("<p><strong>Student Code:</strong> ").append(glStudentCD).append("</p>")
							.append("<p><strong>Email:</strong> ").append(recipientEmail).append("</p>").append("<hr>")
							.append("<h2>Borrowed Books</h2>")
							.append("<table border='1' cellpadding='5' cellspacing='0'>").append("<tr>")
							.append("<th>Book Name</th>").append("<th>Quantity</th>").append("<th>Unit Price</th>")
							.append("<th>Total Price</th>").append("<th>Due Return Date</th>").append("</tr>");

					// Iterate through the borrowed books and add them to the table
					for (var i = 0; i < glRentalModel.getRowCount(); i++) {
						var bookName = glRentalModel.getValueAt(i, 1).toString();
						var quantity = Integer.parseInt(glRentalModel.getValueAt(i, 2).toString());
						var unitPrice = String.format("%,.2f",
								Double.parseDouble(glRentalModel.getValueAt(i, 3).toString()));
						var totalPriceDouble = Double.parseDouble(glRentalModel.getValueAt(i, 3).toString()) * quantity;
						var totalPrice = String.format("%,.2f", totalPriceDouble);
						var dueDate = glRentalModel.getValueAt(i, 5).toString(); // Assuming column 5 is Due Return
																					// Date

						messageBuilder.append("<tr>").append("<td>").append(bookName).append("</td>")
								.append("<td style='text-align: center;'>").append(quantity).append("</td>")
								.append("<td style='text-align: right;'>").append(unitPrice).append("</td>")
								.append("<td style='text-align: right;'>").append(totalPrice).append("</td>")
								.append("<td>").append(dueDate).append("</td>").append("</tr>");
					}

					messageBuilder.append("</table>").append("<hr>").append("<h2>Payment Details</h2>")
							.append("<p><strong>Total Amount:</strong> $").append(totalAmount).append("</p>")
							.append("<p><strong>Amount Given:</strong> $").append(amountGiven).append("</p>")
							.append("<p><strong>Change:</strong> $").append(changeAmount).append("</p>").append("<hr>")
							.append("<p>Thank you for using our library services.</p>")
							.append("<p>Sincerely,<br>The Library</p>").append("</body>").append("</html>");

					var emailContent = messageBuilder.toString();

					message.setContent(emailContent, "text/html; charset=utf-8"); // Set email content

					Transport.send(message); // Send the email

				} catch (MessagingException ex) {
					throw ex; // Rethrow the exception to handle it in the done() method
				}
				return null;
			}

			@Override
			protected void done() {
				try {
					get(); // Check for exceptions
					JOptionPane.showMessageDialog(CashPaymentDialog.this,
							"Confirmation email sent to " + recipientEmail, "Notification",
							JOptionPane.INFORMATION_MESSAGE);
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(CashPaymentDialog.this, "Failed to send email: " + ex.getMessage(),
							"Error", JOptionPane.ERROR_MESSAGE);
					ex.printStackTrace();
				}
			}
		};

		emailWorker.execute(); // Start the SwingWorker
	}
}