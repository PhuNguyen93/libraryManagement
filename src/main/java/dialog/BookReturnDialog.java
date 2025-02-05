package dialog;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import dao.BookManagementDao;
import dao.BorrowRecordsDao;
import main.ReloadDataListener;
import service.ConnectDB;
import java.awt.Color;
import java.awt.Font;

public class BookReturnDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private JButton btnReturnBoook;
	private JButton btnDameBoook;
	private JTextField txtBookName, txtISBN, txtQuantity, txtBorrowDate, txtDueDate;
	private JTextField txtStudentCode, txtStudentName, txtPhoneNumber, txtEmail;
	private int borrowRecordID;
	private ReloadDataListener reloadDataListener;

	public BookReturnDialog(Window parent, Object[] rowData, ReloadDataListener listener) {
		super(parent, "Book Return Details", ModalityType.APPLICATION_MODAL);
		this.reloadDataListener = listener;
		setSize(700, 538);
		setLocationRelativeTo(parent);
		getContentPane().setLayout(null);
		this.borrowRecordID = (int) rowData[10];
		// Panel chứa thông tin sinh viên
		var studentPanel = new JPanel();
		studentPanel.setLayout(null);
		studentPanel.setBounds(10, 10, 660, 150);
		studentPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
				"Student Information", TitledBorder.LEADING, TitledBorder.TOP));
		getContentPane().add(studentPanel);

		var lblStudentCode = new JLabel("Student Code:");
		lblStudentCode.setBounds(20, 30, 100, 25);
		studentPanel.add(lblStudentCode);

		txtStudentCode = new JTextField(rowData[6].toString());
		txtStudentCode.setBounds(130, 30, 150, 25);
		txtStudentCode.setEditable(false);
		studentPanel.add(txtStudentCode);

		var lblStudentName = new JLabel("Student Name:");
		lblStudentName.setBounds(320, 30, 100, 25);
		studentPanel.add(lblStudentName);

		txtStudentName = new JTextField(rowData[7].toString());
		txtStudentName.setBounds(430, 30, 200, 25);
		txtStudentName.setEditable(false);
		studentPanel.add(txtStudentName);

		var lblPhoneNumber = new JLabel("Phone Number:");
		lblPhoneNumber.setBounds(20, 70, 100, 25);
		studentPanel.add(lblPhoneNumber);

		txtPhoneNumber = new JTextField(rowData[11].toString()); // Thêm dữ liệu số điện thoại nếu có
		txtPhoneNumber.setBounds(130, 70, 150, 25);
		txtPhoneNumber.setEditable(false);
		studentPanel.add(txtPhoneNumber);

		var lblEmail = new JLabel("Email:");
		lblEmail.setBounds(320, 70, 100, 25);
		studentPanel.add(lblEmail);

		txtEmail = new JTextField(rowData[12].toString()); // Thêm dữ liệu email nếu có
		txtEmail.setBounds(430, 70, 200, 25);
		txtEmail.setEditable(false);
		studentPanel.add(txtEmail);

		// Panel chứa thông tin sách trả
		var bookPanel = new JPanel();
		bookPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Book Information",
				TitledBorder.LEADING, TitledBorder.TOP));
		bookPanel.setBounds(10, 205, 660, 200);
		bookPanel.setLayout(null);
		getContentPane().add(bookPanel);

		// Hình ảnh cuốn sách
		var lblBookCover = new JLabel("Book Cover:");
		lblBookCover.setBounds(20, 30, 100, 25);
		bookPanel.add(lblBookCover);

		var lblBookImage = new JLabel();
		lblBookImage.setBounds(130, 20, 100, 100); // Kích thước hình ảnh
		if (rowData[0] instanceof ImageIcon) {
			lblBookImage.setIcon((ImageIcon) rowData[0]); // Gắn hình ảnh từ rowData[0]
		}
		bookPanel.add(lblBookImage);

		// Các nhãn và text field khác
		var lblBookName = new JLabel("Book Name:");
		lblBookName.setBounds(250, 30, 100, 25);
		bookPanel.add(lblBookName);

		txtBookName = new JTextField(rowData[1].toString());
		txtBookName.setBounds(360, 30, 200, 25);
		txtBookName.setEditable(false);
		bookPanel.add(txtBookName);

		var lblISBN = new JLabel("ISBN:");
		lblISBN.setBounds(20, 130, 100, 25);
		bookPanel.add(lblISBN);

		txtISBN = new JTextField(rowData[2].toString());
		txtISBN.setBounds(130, 130, 150, 25);
		txtISBN.setBorder(null);
		txtISBN.setEditable(false);
		bookPanel.add(txtISBN);

		var lblQuantity = new JLabel("Quantity:");
		lblQuantity.setBounds(250, 70, 100, 25);
		bookPanel.add(lblQuantity);

		txtQuantity = new JTextField(rowData[8].toString());
		txtQuantity.setBounds(360, 70, 200, 25);
		txtQuantity.setEditable(false);
		bookPanel.add(txtQuantity);

		var lblBorrowDate = new JLabel("Borrow Date:");
		lblBorrowDate.setBounds(250, 110, 100, 25);
		bookPanel.add(lblBorrowDate);

		txtBorrowDate = new JTextField(rowData[9].toString());
		txtBorrowDate.setBounds(360, 110, 200, 25);
		txtBorrowDate.setEditable(false);
		bookPanel.add(txtBorrowDate);

		var lblDueDate = new JLabel("Due Date:");
		lblDueDate.setBounds(250, 150, 100, 25);
		bookPanel.add(lblDueDate);

		txtDueDate = new JTextField(rowData[3].toString());
		txtDueDate.setBounds(360, 150, 200, 25);
		txtDueDate.setEditable(false);
		bookPanel.add(txtDueDate);

		// Panel chứa các nút chức năng
		var buttonPanel = new JPanel();
		buttonPanel.setLayout(null);
		buttonPanel.setBounds(10, 405, 660, 80);
		getContentPane().add(buttonPanel);

		var isOverDate = checkCanReturn(rowData[3].toString());
		btnReturnBoook = new JButton(new ImageIcon(BookReturnDialog.class.getResource("/icon7/return-32.png")));
		btnReturnBoook.addActionListener(this::btnReturnBoookActionPerformed);
		btnReturnBoook.setBounds(168, 15, 58, 50);
		btnReturnBoook.setEnabled(isOverDate);
		buttonPanel.add(btnReturnBoook);

		btnDameBoook = new JButton(new ImageIcon(BookReturnDialog.class.getResource("/icon7/torn-page.png")));
		btnDameBoook.addActionListener(this::btnDameBoookActionPerformed);
		btnDameBoook.setBounds(278, 15, 58, 50);
		buttonPanel.add(btnDameBoook);

		var btnOutDate = new JButton(new ImageIcon(BookReturnDialog.class.getResource("/icon9/time-out.png")));
		btnOutDate.addActionListener(this::btnOutDateActionPerformed);
		btnOutDate.setBounds(388, 15, 58, 50);
		btnOutDate.setEnabled(!isOverDate);
		buttonPanel.add(btnOutDate);

		var btnClose = new JButton(new ImageIcon(BookReturnDialog.class.getResource("/icon5/cross.png")));
		btnClose.setBounds(560, 15, 58, 50);
		btnClose.addActionListener(e -> dispose());
		buttonPanel.add(btnClose);
		
		JLabel lblNewLabel = new JLabel("The librarian should check the books before returning them.\n\n\n\n\n\n\n");
		lblNewLabel.setFont(new Font("Rockwell", Font.BOLD, 16));
		lblNewLabel.setForeground(new Color(255, 0, 128));
		lblNewLabel.setBounds(108, 171, 486, 23);
		getContentPane().add(lblNewLabel);
		if (!isBorrow(borrowRecordID)) {
			closeDialog();
		}
	}

	protected void btnDameBoookActionPerformed(ActionEvent e) {
		var bookdao = new BookManagementDao();
		var book = bookdao.getBookWithDetails(txtISBN.getText());
		var dialog = new PayDameDialog(SwingUtilities.getWindowAncestor(this), borrowRecordID, book,
				txtStudentCode.getText(),
				reloadDataListener);
		dialog.setVisible(true);
		// Đóng BookReturnDialog sau khi PayOutDateDialog được xử lý
		if (!dialog.isVisible()) { // Chỉ đóng nếu dialog con đã bị tắt
			closeDialog();
		}
	}

	protected void btnOutDateActionPerformed(ActionEvent e) {
		var bookdao = new BookManagementDao();
		var book = bookdao.getBookWithDetails(txtISBN.getText());
		var dialog1 = new PayOutDateDialog(SwingUtilities.getWindowAncestor(this), borrowRecordID, book,
				 txtStudentCode.getText(), txtDueDate.getText(), reloadDataListener);
		dialog1.setVisible(true);
		// Đóng BookReturnDialog sau khi PayOutDateDialog được xử lý
		if (!dialog1.isVisible()) { // Chỉ đóng nếu dialog con đã bị tắt
			closeDialog();
		}
	}

	private boolean checkCanReturn(String dueReturnDateString) {
		// Chuyển String thành Timestamp
		var dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		// Chuyển Timestamp sang LocalDateTime
		var dueReturnDate = LocalDate.parse(dueReturnDateString, dateFormatter);

		// Lấy ngày giờ hiện tại
		var currentDateTime = LocalDate.now();
		if (dueReturnDate.isBefore(currentDateTime)) {
			return false;
		}
		return true;
	}

	protected void btnReturnBoookActionPerformed(ActionEvent e) {

		var choice = JOptionPane.showConfirmDialog(this, // Component cha
				"Do you want Return this book",
				"Confirm Book Return", // Tiêu đề
				JOptionPane.YES_NO_OPTION, // Tùy chọn Yes/No
				JOptionPane.WARNING_MESSAGE // Kiểu icon
		);
		if (choice == JOptionPane.NO_OPTION) {
			return;
		}
		var bookdao = new BookManagementDao();
		var book = bookdao.getBookWithDetails(txtISBN.getText());

		var dialog3 = new PayCorrectDateDialog(SwingUtilities.getWindowAncestor(this), borrowRecordID, book,
				txtStudentCode.getText(), txtDueDate.getText(), reloadDataListener);
		dialog3.setVisible(true);

		// Đóng BookReturnDialog sau khi PayOutDateDialog được xử lý
		if (!dialog3.isVisible()) { // Chỉ đóng nếu dialog con đã bị tắt
			closeDialog();
		}
	}


	private void closeDialog() {
		this.dispose();
		this.setVisible(false);
		if (reloadDataListener != null) {
			reloadDataListener.reloadData();
		}
	}

	private boolean isBorrow(int recordID) {
		var borrowRecordsDao = new BorrowRecordsDao(ConnectDB.getCon());
		if (!borrowRecordsDao.selectBrrowIsRetal(recordID)) {
			return false;
		}
		return true;
	}
}
