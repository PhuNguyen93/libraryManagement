package dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import main.Chart;
import main.ReloadDataListener;
import view.BookRentalViewDialog;

public class BookRentalDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private JTable tableRentalBook; // Khai báo ở cấp độ class

	private boolean isUpdatingTable = false; // Cờ kiểm soát cập nhật bảng
	private JButton btnCashPay;
	private double totalRentalPrice;
	private String glStudentCD;
	private DefaultTableModel glRentalModel;
	private ReloadDataListener reloadDataListener;
	private Chart chart;

	public BookRentalDialog(Window parent, String studentCode, String studentName, String phoneNumber, String email,
			DefaultTableModel rentalModel, ReloadDataListener listener, Chart chart) {
		super(parent, "Book Rental Details", ModalityType.APPLICATION_MODAL);
		this.reloadDataListener = listener;
		this.chart = chart;
		setSize(800, 551);
		setLocationRelativeTo(parent);
		getContentPane().setLayout(null);
		this.glStudentCD = studentCode;
		this.glRentalModel = rentalModel;
		// Panel chứa thông tin sinh viên
		var studentPanel = new JPanel();
		studentPanel.setLayout(null);
		studentPanel.setBounds(10, 10, 760, 121);
		studentPanel.setBorder(BorderFactory.createTitledBorder("Student Information"));

		var lblStudentCode = new JLabel("Student Code:");
		lblStudentCode.setBounds(20, 30, 100, 25);
		studentPanel.add(lblStudentCode);

		var textStudentCode = new JTextField(studentCode);
		textStudentCode.setBounds(130, 30, 200, 25);
		textStudentCode.setFocusable(false); // Không cho phép focus hoặc click chuột vào
		textStudentCode.setEditable(false);
		studentPanel.add(textStudentCode);

		// Sử dụng màu Light Cyan
		var lightCyan = new Color(224, 255, 255);
		textStudentCode.setBackground(lightCyan);
		textStudentCode.setForeground(Color.BLACK); // Chữ màu đen

		var lblStudentName = new JLabel("Student Name:");
		lblStudentName.setBounds(20, 70, 100, 25);
		studentPanel.add(lblStudentName);

		var textStudentName = new JTextField(studentName);
		textStudentName.setBounds(130, 70, 200, 25);
		textStudentName.setFocusable(false); // Không cho phép focus hoặc click chuột vào
		textStudentName.setEditable(false);
		studentPanel.add(textStudentName);

		// Sử dụng màu Light Cyan
		var lightCyan1 = new Color(224, 255, 255);
		textStudentName.setBackground(lightCyan1);
		textStudentName.setForeground(Color.BLACK); // Chữ màu đen

		var lblPhoneNumber = new JLabel("Phone Number:");
		lblPhoneNumber.setBounds(400, 30, 100, 25);
		studentPanel.add(lblPhoneNumber);

		var textPhoneNumber = new JTextField(phoneNumber);
		textPhoneNumber.setBounds(510, 30, 200, 25);
		textPhoneNumber.setFocusable(false); // Không cho phép focus hoặc click chuột vào
		textPhoneNumber.setEditable(false);
		studentPanel.add(textPhoneNumber);
		// Sử dụng màu Light Cyan
		var lightCyan11 = new Color(224, 255, 255);
		textPhoneNumber.setBackground(lightCyan11);
		textPhoneNumber.setForeground(Color.BLACK); // Chữ màu đen

		var lblEmail = new JLabel("Email:");
		lblEmail.setBounds(400, 70, 100, 25);
		studentPanel.add(lblEmail);

		var textEmail = new JTextField(email);
		textEmail.setBounds(510, 70, 200, 25);
		textEmail.setFocusable(false); // Không cho phép focus hoặc click chuột vào
		textEmail.setEditable(false);
		studentPanel.add(textEmail);
		// Sử dụng màu Light Cyan
		var lightCyan111 = new Color(224, 255, 255);
		textEmail.setBackground(lightCyan111);
		textEmail.setForeground(Color.BLACK); // Chữ màu đen

		getContentPane().add(studentPanel);

		// Panel chứa bảng RentalBook
		var rentalPanel = new JPanel();
		rentalPanel.setBounds(10, 172, 760, 281);
		rentalPanel.setBorder(BorderFactory.createTitledBorder("Rental Book Details"));

		if (rentalModel.findColumn("Price") == -1) {
			rentalModel.addColumn("Price");
		}

		if (rentalModel.findColumn("Deposit Percentage") == -1) {
			rentalModel.addColumn("Deposit Percentage");
		}

		// Thêm cột Total Price vào model nếu chưa có
		if (rentalModel.findColumn("Total Price") == -1) {
			rentalModel.addColumn("Total Price");
		}

		tableRentalBook = new JTable(rentalModel); // Local variable, not the class-level variable
		tableRentalBook.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				tableRentalBookMouseReleased(e);
			}
		});
		tableRentalBook.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableRentalBook.setRowHeight(30);
		tableRentalBook.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
		tableRentalBook.getTableHeader().setBackground(new Color(211, 211, 211));
		rentalPanel.setLayout(null);
		var scrollPane = new JScrollPane(tableRentalBook);
		scrollPane.setBounds(6, 16, 748, 254);
		rentalPanel.add(scrollPane);

		getContentPane().add(rentalPanel);

		// Thêm JLabel hiển thị tổng tiền
		var lblTotalRentalPrice = new JLabel("Total Rental Price:");
		lblTotalRentalPrice.setBounds(224, 471, 100, 30);
		getContentPane().add(lblTotalRentalPrice);

		var textTotalRentalPrice = new JTextField("0.00");
		textTotalRentalPrice.setBounds(334, 471, 150, 30);
		textTotalRentalPrice.setFocusable(false); // Không cho phép focus hoặc click chuột vào
		textTotalRentalPrice.setEditable(false);
		getContentPane().add(textTotalRentalPrice);

		// Sử dụng màu Light Cyan
		var lightCyan1111 = new Color(224, 255, 255);
		textTotalRentalPrice.setBackground(lightCyan1111);
		textTotalRentalPrice.setForeground(Color.BLACK); // Chữ màu đen

		// Tính tổng tiền khi khởi tạo dialog
		updateTotalPriceColumn(rentalModel); // Tính giá trị cho cột Total Price
		totalRentalPrice = calculateTotalRentalPrice(rentalModel);
		textTotalRentalPrice.setText(String.format("%,d", (long) totalRentalPrice));

		// Thêm TableModelListener để theo dõi thay đổi dữ liệu trong bảng
		rentalModel.addTableModelListener(e -> {
			updateTotalPriceColumn(rentalModel); // Tính lại giá trị cột Total Price
			var updatedTotal = calculateTotalRentalPrice(rentalModel);
			textTotalRentalPrice.setText(String.format("%.2f", updatedTotal));
		});

		// Nút Close để đóng dialog
		var btnClose = new JButton("");
		btnClose.setIcon(new ImageIcon(BookRentalDialog.class.getResource("/icon5/cross.png")));
		btnClose.setBounds(651, 464, 41, 37);
		btnClose.addActionListener(e -> dispose());
		getContentPane().add(btnClose);

		var btnPayQR = new JButton("");
		btnPayQR.addActionListener(this::btnNewButtonActionPerformed);
		btnPayQR.setIcon(new ImageIcon(BookRentalDialog.class.getResource("/icon6/pay-32.png")));
		btnPayQR.setBounds(507, 464, 41, 37);
		getContentPane().add(btnPayQR);

		btnCashPay = new JButton("");
		btnCashPay.setBackground(new Color(240, 240, 240));
		btnCashPay.addActionListener(this::btnCashPayActionPerformed);
		btnCashPay.setFont(new Font("Tahoma", Font.PLAIN, 7));
		btnCashPay.setIcon(new ImageIcon(BookRentalDialog.class.getResource("/icon7/payment-32.png")));
		btnCashPay.setBounds(566, 464, 41, 37);
		getContentPane().add(btnCashPay);

		var lblNewLabel = new JLabel("Ask the customer to check the books before making payment.\n\n\n\n\n\n\n");
		lblNewLabel.setForeground(new Color(255, 0, 128));
		lblNewLabel.setFont(new Font("Rockwell", Font.BOLD, 16));
		lblNewLabel.setBounds(145, 135, 489, 30);
		getContentPane().add(lblNewLabel);

		formatTableColumns(); // Định dạng số tiền với dấu phẩy

	}

	private void formatTableColumns() {
		var currencyRenderer = new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				if (value != null) {
					try {
						// Định dạng số tiền với dấu phẩy
						var amount = Double.parseDouble(value.toString().replace(",", ""));
						value = String.format("%,d", (long) amount);
					} catch (NumberFormatException ex) {
						// Nếu không thể định dạng, giữ nguyên giá trị
					}
				}
				return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			}

		};
		// Percentage renderer for Deposit Percentage
		var percentageRenderer = new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				if (value != null) {
					try {
						// Append "%" to the value
						var percentage = Double.parseDouble(value.toString());
						value = String.format("%.2f %%", percentage);
					} catch (NumberFormatException ex) {
						// Keep the original value if formatting fails
					}
				}
				return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			}
		};
		// Apply the currency renderer to "Rental Price", "Price", and "Total Price"
		// columns
		tableRentalBook.getColumnModel().getColumn(3).setCellRenderer(currencyRenderer); // Rental Price
		tableRentalBook.getColumnModel().getColumn(tableRentalBook.getColumnCount() - 2)
				.setCellRenderer(currencyRenderer); // Price
		tableRentalBook.getColumnModel().getColumn(tableRentalBook.getColumnCount() - 1)
				.setCellRenderer(currencyRenderer); // Total Price
		// Apply the percentage renderer to the "Deposit Percentage" column
		tableRentalBook.getColumnModel().getColumn(tableRentalBook.getColumnCount() - 3)
				.setCellRenderer(percentageRenderer); // Deposit Percentage
	}

	// Phương thức tính tổng tiền thuê sách
	private double calculateTotalRentalPrice(DefaultTableModel rentalModel) {
		var total = 0.0;
		for (var i = 0; i < rentalModel.getRowCount(); i++) {
			var value = rentalModel.getValueAt(i, rentalModel.getColumnCount() - 1);
			if (value != null) { // Kiểm tra giá trị null
				try {
					var totalPrice = Double.parseDouble(value.toString());
					total += totalPrice;
				} catch (NumberFormatException e) {
					System.err.println("Invalid value in Total Price column: " + value);
				}
			}
		}
		return total;
	}

	// Phương thức cập nhật cột Total Price
	private void updateTotalPriceColumn(DefaultTableModel rentalModel) {
		if (isUpdatingTable) {
			return; // Nếu đang cập nhật, thoát để tránh vòng lặp vô hạn
		}
		isUpdatingTable = true; // Đặt cờ để ngăn chặn sự kiện liên tục
		try {
			for (var i = 0; i < rentalModel.getRowCount(); i++) {
				try {
					var rentalPriceObj = rentalModel.getValueAt(i, 3);
					var quantityObj = rentalModel.getValueAt(i, 2);
					var numberOfDaysObj = rentalModel.getValueAt(i, 4);
					var priceobj = rentalModel.getValueAt(i, rentalModel.getColumnCount() - 3);
					var depositPercentageObj = rentalModel.getValueAt(i, rentalModel.getColumnCount() - 2);
					// Kiểm tra giá trị null
					if (rentalPriceObj == null || quantityObj == null || numberOfDaysObj == null) {
						rentalModel.setValueAt("0.00", i, rentalModel.getColumnCount() - 1);
						continue;
					}

					// Chuyển đổi giá trị
					var rentalPrice = Double.parseDouble(rentalPriceObj.toString());
					var quantity = Integer.parseInt(quantityObj.toString());
					var numberOfDays = Integer.parseInt(numberOfDaysObj.toString());
					var depositPercentage = Double.parseDouble(depositPercentageObj.toString());
					var price = Double.parseDouble(priceobj.toString());

					// Tính tiền thuê và tiền đặt cọc

					var depositAmount = price * depositPercentage / 100; // Deposit Amount
					var rentalAmount = rentalPrice * quantity * numberOfDays; // Rental Amount
					var totalPrice = depositAmount + rentalAmount;

					rentalModel.setValueAt(String.format("%.2f", totalPrice), i, rentalModel.getColumnCount() - 1);
				} catch (Exception e) {
					// Nếu xảy ra lỗi, đặt giá trị mặc định là 0.00
					rentalModel.setValueAt("0.00", i, rentalModel.getColumnCount() - 1);
					System.err.println("Error calculating Total Price for row " + i + ": " + e.getMessage());
				}
			}
		} finally {
			isUpdatingTable = false; // Đặt lại cờ
		}
	}

	// Sự kiện khi nhấn nút btnNewButtonActionPerformed
	protected void btnNewButtonActionPerformed(ActionEvent e) {
		try {
			if (tableRentalBook == null || tableRentalBook.getModel() == null) {
				throw new NullPointerException("TableRentalBook is null.");
			}

			// Tính tổng tiền từ bảng RentalBook
			var totalAmount = calculateTotalRentalPrice((DefaultTableModel) tableRentalBook.getModel());

			// Hiển thị QR Code trong một JDialog
			SwingUtilities.invokeLater(() -> {
				var qrDialog = new JDialog(this, "QR Code Generator", true);
				qrDialog.setSize(300, 300);
				qrDialog.getContentPane().setLayout(new BorderLayout());

				// Thêm QRCodeGeneratorPanel vào JDialog
				qrDialog.getContentPane().add(new QRCodeGeneratorPanel(this, totalRentalPrice, glStudentCD,
						glRentalModel, reloadDataListener), BorderLayout.CENTER);

				qrDialog.setLocationRelativeTo(this);
				qrDialog.setVisible(true);
			});
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error opening Pay dialog: " + ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	protected void btnCashPayActionPerformed(ActionEvent e) {
		var cashpayment = new CashPaymentDialog(this, totalRentalPrice, glStudentCD, glRentalModel, reloadDataListener,
				chart);
		cashpayment.setLocationRelativeTo(this); // Hiển thị ở giữa màn hình
		cashpayment.setVisible(true);
	}

	protected void tableRentalBookMouseReleased(MouseEvent e) {
		// Kiểm tra sự kiện chuột phải hoặc Control + Click (dành cho MacOS)
		if ((e.isPopupTrigger() || (e.isControlDown() && e.getButton() == MouseEvent.BUTTON3))
				&& e.getComponent() instanceof JTable) {
			var row = tableRentalBook.rowAtPoint(e.getPoint()); // Lấy dòng được nhấp chuột
			if (row >= 0 && row < tableRentalBook.getRowCount()) {
				tableRentalBook.setRowSelectionInterval(row, row); // Chọn dòng được nhấp chuột
				showPopupMenu(e, row); // Hiển thị menu chuột phải
			}
		}
	}

	private void showPopupMenu(MouseEvent e, int row) {
		var popupMenu = new JPopupMenu();

		// Tùy chọn "View Details"
		var menuItemViewDetails = new JMenuItem("View Details");
		menuItemViewDetails.addActionListener(event -> openBookRentalViewDialog(row)); // Mở dialog
		popupMenu.add(menuItemViewDetails);

		// Hiển thị popup menu tại vị trí chuột
		popupMenu.show(tableRentalBook, e.getX(), e.getY());
	}

	private void openBookRentalViewDialog(int row) {
		try {
			// Lấy tên cột
			var columnNames = new String[tableRentalBook.getColumnCount()];
			for (var i = 0; i < tableRentalBook.getColumnCount(); i++) {
				columnNames[i] = tableRentalBook.getColumnName(i); // Lấy tên cột
			}

			// Lấy dữ liệu của dòng được chọn và chuyển thành mảng một chiều
			var rentalData = new Object[tableRentalBook.getColumnCount()];
			for (var i = 0; i < tableRentalBook.getColumnCount(); i++) {
				rentalData[i] = tableRentalBook.getValueAt(row, i); // Lấy dữ liệu từng cột
			}

			// Kiểm tra số lượng phần tử trong rentalData và columnNames
			if (rentalData.length != columnNames.length) {
				throw new IllegalArgumentException("Mismatch between number of columns and data. Columns: "
						+ columnNames.length + ", Data: " + rentalData.length);
			}

			// Hiển thị dialog BookRentalViewDialog
			var dialog = new BookRentalViewDialog(SwingUtilities.getWindowAncestor(this), columnNames, rentalData);
			dialog.setLocationRelativeTo(this); // Hiển thị ở giữa màn hình
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error opening rental details: " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}
