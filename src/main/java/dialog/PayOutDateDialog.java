package dialog;

import java.awt.Color;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import dao.BookManagementDao;
import dao.BorrowHistoryDao;
import dao.BorrowRecordsDao;
import dao.StudentDao;
import entity.BookManagementEntity;
import entity.BorrowHistoryEntity;
import entity.UserSession;
import main.ReloadDataListener;
import service.ConnectDB;

public class PayOutDateDialog extends JDialog {
	private static final long serialVersionUID = 1L;

	private JTextField textTotalAmount;
	private JTextField textCustomerPay;
	private JTextField textChange;
	private JButton btnPaid;
	private JButton btnCancel;
	private JLabel lblOutDate;
	private JTextField textOutDate;
	private Window parentForm;
	private ReloadDataListener reloadDataListener;
	private int recordID;
	private String StudentCD;
	private UserSession userSession;
	private BigDecimal totalAmount, lateFee;
	private LocalDate dtDueDate;
	private BookManagementEntity book;

	public PayOutDateDialog(Window parent, int borrowRecordID, BookManagementEntity book, String StudentCD,
			String strDueDate, ReloadDataListener listener) {
		super(parent, "Pay Dame", ModalityType.APPLICATION_MODAL);
		setSize(480, 350);
		setLocationRelativeTo(parent);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);
		// Định dạng ngày
		var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		var dueDate = LocalDate.parse(strDueDate, formatter);
        var currentDate = LocalDate.now();

		// Tính số ngày giữa hiện tại và dueDate
		var daysBetween = ChronoUnit.DAYS.between(dueDate, currentDate);


		this.parentForm = parent;
		this.reloadDataListener = listener;
		this.recordID = borrowRecordID;
		this.StudentCD = StudentCD;

		this.dtDueDate = dueDate;
		userSession = UserSession.getInstance();
		this.book = book;
		// Label: Deposit Amount
		var lblDepositAmount = new JLabel("Deposit Amount:");
		lblDepositAmount.setFont(new Font("Arial", Font.BOLD, 14));
		lblDepositAmount.setBounds(30, 50, 150, 30);
		getContentPane().add(lblDepositAmount);

		// TextField: Deposit Amount (read-only)
		var depositAmount = book.getDepositPercentage().multiply(book.getPrice()).divide(BigDecimal.valueOf(100));
		var textDepositAmount = new JTextField(String.format("%,.0f", depositAmount));
		textDepositAmount.setEnabled(false);
		textDepositAmount.setEditable(false);
		textDepositAmount.setFont(new Font("Arial", Font.PLAIN, 14));
		textDepositAmount.setBounds(190, 51, 200, 30);
		textDepositAmount.setBackground(new Color(224, 255, 255));
		getContentPane().add(textDepositAmount);

		// Label: Late Fee
		var lblLateFee = new JLabel("Late Fee:");
		lblLateFee.setFont(new Font("Arial", Font.BOLD, 14));
		lblLateFee.setBounds(30, 90, 150, 30);
		getContentPane().add(lblLateFee);

		// TextField: Late Fee (read-only)
		lateFee = book.getFineMultiplier().multiply(BigDecimal.valueOf(daysBetween))
				.multiply(book.getRentalPrice());
		var textLateFee = new JTextField(String.format("%,.0f", lateFee));
		textLateFee.setEnabled(false);
		textLateFee.setEditable(false);
		textLateFee.setFont(new Font("Arial", Font.PLAIN, 14));
		textLateFee.setBounds(190, 91, 200, 30);
		textLateFee.setBackground(new Color(224, 255, 255));
		getContentPane().add(textLateFee);

		// Label: Total Amount
		var lblTotalAmount = new JLabel("Total Amount:");
		lblTotalAmount.setFont(new Font("Arial", Font.BOLD, 14));
		lblTotalAmount.setBounds(30, 130, 150, 30);
		getContentPane().add(lblTotalAmount);


		// TextField: Total Amount (read-only)
		totalAmount = lateFee.subtract(depositAmount);
		textTotalAmount = new JTextField(String.format("%,.0f", totalAmount));
		textTotalAmount.setEnabled(false);
		textTotalAmount.setEditable(false);
		textTotalAmount.setFont(new Font("Arial", Font.PLAIN, 14));
		textTotalAmount.setBounds(190, 131, 200, 30);
		textTotalAmount.setBackground(new Color(224, 255, 255));
		getContentPane().add(textTotalAmount);
		// Label: Customer Pay
		var lblCustomerPay = new JLabel("Customer Pay:");
		lblCustomerPay.setFont(new Font("Arial", Font.BOLD, 14));
		lblCustomerPay.setBounds(30, 170, 150, 30);
		getContentPane().add(lblCustomerPay);

		// TextField: Customer Pay Input
		textCustomerPay = new JTextField();
		textCustomerPay.setFont(new Font("Arial", Font.PLAIN, 14));
		textCustomerPay.setBounds(190, 171, 200, 30);
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
		lblChange.setBounds(30, 210, 150, 30);
		getContentPane().add(lblChange);

		// TextField: Change (read-only)
		textChange = new JTextField("0.00");
		textChange.setEnabled(false);
		textChange.setEditable(false);
		textChange.setFont(new Font("Arial", Font.PLAIN, 14));
		textChange.setBounds(190, 211, 200, 30);
		textChange.setBackground(new Color(224, 255, 255));
		getContentPane().add(textChange);

		// Button: Paid
		btnPaid = new JButton("");
		btnPaid.setIcon(new ImageIcon(PayOutDateDialog.class.getResource("/icon7/asset.png")));
		btnPaid.setFont(new Font("Arial", Font.BOLD, 14));
		btnPaid.setBounds(150, 260, 52, 40);
		btnPaid.addActionListener(this::handlePaidAction);
		btnPaid.setEnabled(false); // Disable until valid data is entered
		getContentPane().add(btnPaid);

		// Button: Cancel
		btnCancel = new JButton("");
		btnCancel.setIcon(new ImageIcon(PayOutDateDialog.class.getResource("/icon5/cross.png")));
		btnCancel.setFont(new Font("Arial", Font.BOLD, 14));
		btnCancel.setBounds(250, 260, 52, 40);
		btnCancel.addActionListener(e -> dispose());
		getContentPane().add(btnCancel);

		lblOutDate = new JLabel("Out of Date:");
		lblOutDate.setFont(new Font("Arial", Font.BOLD, 14));
		lblOutDate.setBounds(30, 10, 150, 30);
		getContentPane().add(lblOutDate);

		textOutDate = new JTextField(String.valueOf(daysBetween));

		textOutDate.setBounds(190, 11, 200, 30);
		textOutDate.setEnabled(false);
		textOutDate.setEditable(false);
		textChange.setBackground(new Color(224, 255, 255));
		textOutDate.setColumns(10);
		getContentPane().add(textOutDate);

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
		var choice = JOptionPane.showConfirmDialog(this, // Component cha
				"Do you want Return this book", "Confirm Book Return", // Tiêu đề
				JOptionPane.YES_NO_OPTION, // Tùy chọn Yes/No
				JOptionPane.WARNING_MESSAGE // Kiểu icon
		);
		if (choice == JOptionPane.NO_OPTION) {
			return;
		}
		if (updateBorrowRecord() && updateBookReturn()) {
			updateStudentReturn();
			insertHistory();
			closeDialog();
		}

	}

	private void insertHistory() {
		var borrowhisdao = new BorrowHistoryDao();
		var bookHis = new BorrowHistoryEntity();
		bookHis.setRecordID(recordID);
		bookHis.setAction("Overdue");
		bookHis.setActionDate(LocalDateTime.now());
		bookHis.setDeleted(false);
		if (!borrowhisdao.insert(bookHis)) {
			JOptionPane.showMessageDialog(this, "Failed to save record history", "Error", JOptionPane.ERROR_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(this, "Update Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	private boolean updateBookReturn() {
		var bookdao = new BookManagementDao();

		// Chuyển đổi từ String sang int
		var newStockQuantity = book.getStockQuantity() + 1;
		var newQuantity = book.getQuantity() + 1;
		if (!bookdao.updateBookStockRetal(book.getBookID(), newStockQuantity, newQuantity)) {
			JOptionPane.showMessageDialog(this, "Failed update BookID: " + book.getBookID() + " ", "Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}

	private void updateStudentReturn() {
		var studentDao = new StudentDao();
		var student = studentDao.selectByStudentCd(StudentCD);

		student.setLateReturnsCount(student.getLateReturnsCount() + 1);
		studentDao.updateStudentStatistics(student, userSession.getUserId());

	}
	private boolean updateBorrowRecord() {
		var borrowRecordsDao = new BorrowRecordsDao(ConnectDB.getCon());
		if (!borrowRecordsDao.updateStatus(recordID, "Overdue")
				|| !borrowRecordsDao.updateFineAmount(recordID, lateFee)) {
			return false;
		}
		return true;
	}

	private void closeDialog() {
		dispose();
		this.setVisible(false);
		// Kiểm tra và đóng parentForm nếu nó là JDialog
		if (parentForm instanceof JDialog) {
			parentForm.dispose();
		}
		if (reloadDataListener != null) {
			reloadDataListener.reloadData();
		}

	}
}
