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
import java.time.LocalDateTime;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import dao.BookManagementDao;
import dao.BorrowHistoryDao;
import dao.BorrowRecordsDao;
import entity.BookManagementEntity;
import entity.BorrowHistoryEntity;
import main.ReloadDataListener;
import service.ConnectDB;

public class PayCorrectDateDialog extends JDialog {
	private static final long serialVersionUID = 1L;

	private JTextField textTotalAmount;
	private JTextField textCustomerPay;
	private JTextField textChange;
	private JButton btnPaid;
	private JButton btnCancel;
	private Window parentForm;
	private ReloadDataListener reloadDataListener;
	private int recordID;
	private BigDecimal totalAmount;
	private BookManagementEntity book;

	public PayCorrectDateDialog(Window parent, int borrowRecordID, BookManagementEntity book, String StudentCD,
			String strDueDate, ReloadDataListener listener) {
		super(parent, "Pay Dame", ModalityType.APPLICATION_MODAL);
		setSize(444, 311);
		setLocationRelativeTo(parent);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);

		this.parentForm = parent;
		this.reloadDataListener = listener;
		this.recordID = borrowRecordID;
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

		// Label: Total Amount
		var lblTotalAmount = new JLabel("Total Amount:");
		lblTotalAmount.setFont(new Font("Arial", Font.BOLD, 14));
		lblTotalAmount.setBounds(30, 91, 150, 30);
		getContentPane().add(lblTotalAmount);


		// TextField: Total Amount (read-only)
		totalAmount = depositAmount.negate(); // Lấy giá trị âm của depositAmount ;
		textTotalAmount = new JTextField(String.format("%,.0f", totalAmount));
		textTotalAmount.setEnabled(false);
		textTotalAmount.setEditable(false);
		textTotalAmount.setFont(new Font("Arial", Font.PLAIN, 14));
		textTotalAmount.setBounds(190, 92, 200, 30);
		textTotalAmount.setBackground(new Color(224, 255, 255));
		getContentPane().add(textTotalAmount);
		// Label: Customer Pay
		var lblCustomerPay = new JLabel("Customer Pay:");
		lblCustomerPay.setFont(new Font("Arial", Font.BOLD, 14));
		lblCustomerPay.setBounds(30, 132, 150, 30);
		getContentPane().add(lblCustomerPay);

		// TextField: Customer Pay Input
		textCustomerPay = new JTextField(String.format("%,.0f", totalAmount));
		textCustomerPay.setFont(new Font("Arial", Font.PLAIN, 14));
		textCustomerPay.setBounds(190, 133, 200, 30);
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
		lblChange.setBounds(30, 173, 150, 30);
		getContentPane().add(lblChange);

		// TextField: Change (read-only)
		textChange = new JTextField("0.00");
		textChange.setEnabled(false);
		textChange.setEditable(false);
		textChange.setFont(new Font("Arial", Font.PLAIN, 14));
		textChange.setBounds(190, 174, 200, 30);
		textChange.setBackground(new Color(224, 255, 255));
		getContentPane().add(textChange);

		// Button: Paid
		btnPaid = new JButton("");
		btnPaid.setIcon(new ImageIcon(PayCorrectDateDialog.class.getResource("/icon7/asset.png")));
		btnPaid.setFont(new Font("Arial", Font.BOLD, 14));
		btnPaid.setBounds(151, 225, 52, 40);
		btnPaid.addActionListener(this::handlePaidAction);
		btnPaid.setEnabled(true); // Disable until valid data is entered
		getContentPane().add(btnPaid);

		// Button: Cancel
		btnCancel = new JButton("");
		btnCancel.setIcon(new ImageIcon(PayCorrectDateDialog.class.getResource("/icon5/cross.png")));
		btnCancel.setFont(new Font("Arial", Font.BOLD, 14));
		btnCancel.setBounds(251, 225, 52, 40);
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
		var choice = JOptionPane.showConfirmDialog(this, // Component cha
				"Do you want Return this book", "Confirm Book Return", // Tiêu đề
				JOptionPane.YES_NO_OPTION, // Tùy chọn Yes/No
				JOptionPane.WARNING_MESSAGE // Kiểu icon
		);
		if (choice == JOptionPane.NO_OPTION) {
			return;
		}

		if (updateBorrowRecord() && updateBookReturn()) {

			insertHistory(recordID);
			closeDialog();

		}
	}

	private boolean updateBookReturn() {
		var bookdao = new BookManagementDao();

		// Chuyển đổi từ String sang int
		var newStockQuantity = book.getStockQuantity() + 1;
		var newQuantity = book.getQuantity() + 1;
		if (!bookdao.updateBookStockRetal(book.getBookID(), newStockQuantity, newQuantity)) {
			JOptionPane.showMessageDialog(this, "Failed update BookID: " + book.getBookID() + " is out of stock  ",
					"Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}

	private void insertHistory(int id) {
		var borrowhisdao = new BorrowHistoryDao();
		var bookHis = new BorrowHistoryEntity();
		bookHis.setRecordID(id);
		bookHis.setAction("Returned");
		bookHis.setActionDate(LocalDateTime.now());
		bookHis.setDeleted(false);
		if (!borrowhisdao.insert(bookHis)) {
			JOptionPane.showMessageDialog(this, "Failed to save record history", "Error", JOptionPane.ERROR_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(this, "Update Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);

		}
	}
	private boolean updateBorrowRecord() {
		var borrowRecordsDao = new BorrowRecordsDao(ConnectDB.getCon());
		if (!borrowRecordsDao.updateStatus(recordID, "Returned")) {
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
