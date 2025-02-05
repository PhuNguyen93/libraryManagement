package dialog;

import java.awt.Color;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.LocalDateTime;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import dao.BookManagementDao;
import dao.BorrowHistoryDao;
import dao.BorrowRecordsDao;
import dao.DamageDao;
import dao.StudentDao;
import entity.BookManagementEntity;
import entity.BorrowHistoryEntity;
import entity.DamageEntity;
import entity.UserSession;
import main.ReloadDataListener;
import service.ConnectDB;

public class PayDameDialog extends JDialog {
	private static final long serialVersionUID = 1L;

	private JTextField textTotalAmount;
	private JTextField textCustomerPay;
	private JTextField textChange;
	private JButton btnPaid;
	private JButton btnCancel;
	private Window parentForm;
	private ReloadDataListener reloadDataListener;
	private int recordID;
	private String StudentCD;
	private UserSession userSession;
	private JTextArea textDamageDescription;
	private JComboBox comboDamageSeverity;
	private BigDecimal totalAmount, damageFee;
	private BookManagementEntity book;

	public PayDameDialog(Window parent, int borrowRecordID, BookManagementEntity book, String StudentCD,
			ReloadDataListener listener) {
		super(parent, "Pay Dame", ModalityType.APPLICATION_MODAL);
		setSize(500, 450); // Tăng kích thước dialog để chứa các thành phần mới
		setLocationRelativeTo(parent);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);
		this.parentForm = parent;
		this.reloadDataListener = listener;
		this.recordID = borrowRecordID;
		this.StudentCD = StudentCD;
		this.book = book;
		userSession = UserSession.getInstance();

		// Tính toán tiền đặt cọc và tiền phạt sách hư
		var depositAmount = book.getDepositPercentage().multiply(book.getPrice()).divide(BigDecimal.valueOf(100));
		this.damageFee = book.getPrice(); // Tiền phạt sách hư lấy từ giá sách
		this.totalAmount = depositAmount.subtract(damageFee); // Tổng tiền = tiền đặt cọc - tiền phạt
		// Label: Deposit Amount
		var lblDepositAmount = new JLabel("Deposit Amount:");
		lblDepositAmount.setFont(new Font("Arial", Font.BOLD, 14));
		lblDepositAmount.setBounds(30, 30, 150, 30);
		getContentPane().add(lblDepositAmount);

		// TextField: Deposit Amount (read-only)
		var textDepositAmount = new JTextField(String.format("%,.0f", depositAmount));
		textDepositAmount.setEnabled(false);
		textDepositAmount.setEditable(false);
		textDepositAmount.setFont(new Font("Arial", Font.PLAIN, 14));
		textDepositAmount.setBounds(190, 31, 200, 30);
		textDepositAmount.setBackground(new Color(224, 255, 255));
		getContentPane().add(textDepositAmount);

		// Label: Damage Fee
		var lblDamageFee = new JLabel("Damage Fee:");
		lblDamageFee.setFont(new Font("Arial", Font.BOLD, 14));
		lblDamageFee.setBounds(30, 70, 150, 30);
		getContentPane().add(lblDamageFee);

		// TextField: Damage Fee (read-only)
		var textDamageFee = new JTextField(String.format("%,.0f", damageFee));
		textDamageFee.setEnabled(false);
		textDamageFee.setEditable(false);
		textDamageFee.setFont(new Font("Arial", Font.PLAIN, 14));
		textDamageFee.setBounds(190, 71, 200, 30);
		textDamageFee.setBackground(new Color(224, 255, 255));
		getContentPane().add(textDamageFee);

		// Label: Total Amount
		var lblTotalAmount = new JLabel("Total Amount:");
		lblTotalAmount.setFont(new Font("Arial", Font.BOLD, 14));
		lblTotalAmount.setBounds(30, 110, 120, 30);
		getContentPane().add(lblTotalAmount);

		// TextField: Total Amount (read-only)
		textTotalAmount = new JTextField(String.format("%,.0f", totalAmount));
		textTotalAmount.setEnabled(false);
		textTotalAmount.setEditable(false);
		textTotalAmount.setFont(new Font("Arial", Font.PLAIN, 14));
		textTotalAmount.setBounds(190, 111, 200, 30);
		textTotalAmount.setBackground(new Color(224, 255, 255));
		getContentPane().add(textTotalAmount);

		// Label: Customer Pay
		var lblCustomerPay = new JLabel("Customer Pay:");
		lblCustomerPay.setFont(new Font("Arial", Font.BOLD, 14));
		lblCustomerPay.setBounds(30, 150, 120, 30);
		getContentPane().add(lblCustomerPay);

		// TextField: Customer Pay Input
		textCustomerPay = new JTextField();
		textCustomerPay.setFont(new Font("Arial", Font.PLAIN, 14));
		textCustomerPay.setBounds(190, 151, 200, 30);
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
		lblChange.setBounds(30, 190, 120, 30);
		getContentPane().add(lblChange);

		// TextField: Change (read-only)
		textChange = new JTextField("0.00");
		textChange.setEnabled(false);
		textChange.setEditable(false);
		textChange.setFont(new Font("Arial", Font.PLAIN, 14));
		textChange.setBounds(190, 191, 200, 30);
		textChange.setBackground(new Color(224, 255, 255));
		getContentPane().add(textChange);

		// Label: Damage Severity
		var lblDamageSeverity = new JLabel("Damage Severity:");
		lblDamageSeverity.setFont(new Font("Arial", Font.BOLD, 14));
		lblDamageSeverity.setBounds(30, 230, 150, 30);
		getContentPane().add(lblDamageSeverity);

		// ComboBox: Damage Severity
		comboDamageSeverity = new JComboBox<>(new String[] { "Low", "Medium", "High" });
		comboDamageSeverity.setFont(new Font("Arial", Font.PLAIN, 14));
		comboDamageSeverity.setBounds(190, 231, 200, 30);
		getContentPane().add(comboDamageSeverity);

		// Label: Damage Description
		var lblDamageDescription = new JLabel("Damage Description:");
		lblDamageDescription.setFont(new Font("Arial", Font.BOLD, 14));
		lblDamageDescription.setBounds(30, 280, 150, 30);
		getContentPane().add(lblDamageDescription);

		// TextArea: Damage Description Input
		textDamageDescription = new JTextArea();
		textDamageDescription.setFont(new Font("Arial", Font.PLAIN, 14));
		textDamageDescription.setLineWrap(true); // Tự động xuống dòng
		textDamageDescription.setWrapStyleWord(true); // Ngắt từ hợp lý
		textDamageDescription.setBounds(190, 281, 250, 60); // Kích thước lớn hơn để hiển thị nhiều dòng
		textDamageDescription.setBorder(BorderFactory.createLineBorder(Color.GRAY)); // Viền cho JTextArea
		getContentPane().add(textDamageDescription);

		// Button: Paid
		btnPaid = new JButton("");
		btnPaid.setIcon(new ImageIcon(PayDameDialog.class.getResource("/icon7/asset.png")));
		btnPaid.setFont(new Font("Arial", Font.BOLD, 14));
		btnPaid.setBounds(150, 360, 52, 40);
		btnPaid.addActionListener(this::handlePaidAction);
		btnPaid.setEnabled(false); // Disable until valid data is entered
		getContentPane().add(btnPaid);

		// Button: Cancel
		btnCancel = new JButton("");
		btnCancel.setIcon(new ImageIcon(PayDameDialog.class.getResource("/icon5/cross.png")));
		btnCancel.setFont(new Font("Arial", Font.BOLD, 14));
		btnCancel.setBounds(250, 360, 52, 40);
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
			textChange.setText("");
			btnPaid.setEnabled(false);
		}
	}

	private void handlePaidAction(ActionEvent e) {
		var choice = JOptionPane.showConfirmDialog(this, // Component cha
				"Do you want Report Damage this book", "Confirm Book Return", // Tiêu đề
				JOptionPane.YES_NO_OPTION, // Tùy chọn Yes/No
				JOptionPane.WARNING_MESSAGE // Kiểu icon
		);
		if (choice == JOptionPane.NO_OPTION) {
			return;
		}
		if (updateBorrowRecord() && updateBookReturn() && insertDamage()) {
			updateStudentReturn();
			insertHistory();
			closeDialog();
		}
	}

	private void insertHistory() {
		var borrowhisdao = new BorrowHistoryDao();
		var bookHis = new BorrowHistoryEntity();
		bookHis.setRecordID(recordID);
		bookHis.setAction("Returned");
		bookHis.setActionDate(LocalDateTime.now());
		bookHis.setDeleted(false);
		if (!borrowhisdao.insert(bookHis)) {
			JOptionPane.showMessageDialog(this, "Failed to save record history", "Error", JOptionPane.ERROR_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(this, "Update Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	private boolean insertDamage() {
		var damagedao = new DamageDao();
		var damage = new DamageEntity();
		damage.setBookID(book.getBookID());
		damage.setReportedBy(userSession.getUserId());
		damage.setDamageDate(new Timestamp(System.currentTimeMillis()));
		damage.setDamageDescription(textDamageDescription.getText());
		damage.setDamageSeverity(comboDamageSeverity.getSelectedItem().toString());
		damage.setStatus("Pending");
		damage.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		damage.setCreatedBy(userSession.getUserId());
		if (!damagedao.insertDamage(damage)) {
			JOptionPane.showMessageDialog(this, "Failed to save Damage", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}

	private boolean updateBookReturn() {
		var bookdao = new BookManagementDao();

		// Chuyển đổi từ String sang int
		var newStockQuantity = book.getStockQuantity() - 1;
		var newQuantity = book.getQuantity();
		if (!bookdao.updateBookStockRetal(book.getBookID(), newStockQuantity, newQuantity)) {
			JOptionPane.showMessageDialog(this, "Failed update BookID: " + book.getBookID() + "   ",
					"Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}

	private void updateStudentReturn() {
		var studentDao = new StudentDao();
		var student = studentDao.selectByStudentCd(StudentCD);

		student.setDamagedBooksCount(student.getDamagedBooksCount() + 1);
		studentDao.updateStudentStatistics(student, userSession.getUserId());

	}

	private boolean updateBorrowRecord() {
		var borrowRecordsDao = new BorrowRecordsDao(ConnectDB.getCon());
		if (!borrowRecordsDao.updateStatus(recordID, "Returned")
				|| !borrowRecordsDao.updateFineAmount(recordID, damageFee)) {
			return false;
		}
		return true;
	}

	private void closeDialog() {
		this.dispose();
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
