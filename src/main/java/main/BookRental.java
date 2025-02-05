package main;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

import com.toedter.calendar.JDateChooser;

import dao.BookManagementDao;
import dao.BorrowRecordsDao;
import dao.StudentDao;
import dao.UserDao;
import dialog.BookRentalDialog;
import dialog.BookReturnDialog;
import entity.BookManagementEntity;
import entity.BorrowRecordsEntity;
import entity.StudentEntity;
import entity.UserEntity;
import service.ConnectDB;

public class BookRental extends JPanel implements ReloadDataListener {

	private static final long serialVersionUID = 1L;
	private JTable tableStudent, tableReturn, tableBook;
	private JTextField searchStudent, searchReturn, searchBook;
	private JPanel panelRental, panelReturn;
	private JScrollPane scrollBook;
	private JLabel lblStudentCode;
	private JLabel lblStudentName;
	private JLabel lblNumberPhone;
	private JLabel lblStudentEmail;
	private JTextField textStudentCode;
	private JTextField textStudentName;
	private JTextField textNumberPhone;
	private JTextField textStudentEmail;
	private JScrollPane scrollRentalBook;
	private JTable tableRentalBook;
	private JButton btnBookrental;
	private JTabbedPane tabbedPane;
	private int initialize = 0;
	private List<StudentEntity> students;
	private List<BookManagementEntity> books;
	private List<BorrowRecordsEntity> borrowRecords;
	private Chart chart;

	public BookRental(Chart chart) {
		this.chart = chart;

		setBackground(new Color(244, 243, 240));
		setBounds(0, 0, 946, 706);
		setLayout(null);

		tabbedPane = new JTabbedPane();
		tabbedPane.setBounds(20, 11, 906, 679);
//		// Khởi tạo giao diện
//		// Tải dữ liệu trong nền
//		SwingWorker<Void, Void> worker = new SwingWorker<>() {
//			@Override
//			protected Void doInBackground() throws Exception {
//				loadStudentData();
//				loadBookData();
//				return null;
//			}
//
//			@Override
//			protected void done() {
//				// Cập nhật giao diện sau khi dữ liệu đã được tải
//				tableStudent.updateUI();
//				tableBook.updateUI();
//			}
//		};
//		worker.execute();
		// Panel Rental
		panelRental = new JPanel();
		panelRental.setBackground(new Color(255, 255, 255));
		panelRental.setBorder(null);
		panelRental.setLayout(null);

		// Panel Return
		panelReturn = new JPanel();
		panelReturn.setBackground(new Color(255, 255, 255));
		panelReturn.setBorder(null);
		panelReturn.setLayout(null);

		// Tab Rental
		var tabRentalIcon = new ImageIcon(getClass().getResource("/hinh/8.png"));
		tabbedPane.addTab("Rental", tabRentalIcon, panelRental, "This is Book Rental Management");
		tabbedPane.addTab("Rental", panelRental);

		// Tab Return
		var tabReturnIcon = new ImageIcon(getClass().getResource("/hinh/9.png"));
		tabbedPane.addTab("Return", tabReturnIcon, panelReturn, "This is Book Return Management");
		initializeRentalComponents();
		initializeRentalBookTable();
		loadStudentData();
		loadBookData(); // Thêm phương thức loadBookData

		add(tabbedPane);

		tabbedPane.addChangeListener(e -> {
			var selectedIndex = tabbedPane.getSelectedIndex();

			if (selectedIndex == 0) {
				tableBook.updateUI();
				tableStudent.updateUI();
				initialize++;
				if (initialize == 1) {
					initializeReturnComponents();
					loadReturnData();
				}
			}
			if (selectedIndex == 1) {
				initialize++;
				if (initialize == 1) {
					initializeReturnComponents();
					loadReturnData();
				} else {
					tableReturn.updateUI();

				}
			}

		});

	}

	private void initializeRentalComponents() {
		// Button thêm sinh viên
		var btnAddStudent = new JButton("");
		btnAddStudent.addActionListener(this::btnAddStudentActionPerformed);
		btnAddStudent.setIcon(new ImageIcon(BookRental.class.getResource("/icon3/add.png")));
		btnAddStudent.setBounds(10, 310, 62, 45);
		btnAddStudent.setToolTipText("Add Student");
		panelRental.add(btnAddStudent);

		// Thanh tìm kiếm sinh viên
		searchStudent = new JTextField("Search by Student...");
		searchStudent.setBounds(82, 310, 274, 45);
		searchStudent.setForeground(Color.GRAY);
		searchStudent.setFont(new Font("Arial", Font.PLAIN, 14));
		searchStudent.setBackground(Color.WHITE);
		searchStudent.setBorder(new RoundedBorder(15));
		searchStudent.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (searchStudent.getText().equals("Search by Student...")) {
					searchStudent.setText("");
					searchStudent.setForeground(Color.BLACK);
				}

			}

			@Override
			public void focusLost(FocusEvent e) {
				if (searchStudent.getText().isEmpty()) {
					searchStudent.setText("Search by Student...");
					searchStudent.setForeground(Color.GRAY);
				}
			}
		});
		searchStudent.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				filterStudentData();
			}
		});
		panelRental.add(searchStudent);

		// Button thêm sách
		var btnAddBook = new JButton("");
		btnAddBook.addActionListener(this::btnAddBookActionPerformed);
		btnAddBook.setIcon(new ImageIcon(BookRental.class.getResource("/icon3/add.png")));
		btnAddBook.setBounds(476, 310, 62, 45);
		btnAddBook.setToolTipText("Add Book");
		panelRental.add(btnAddBook);

		// Thanh tìm kiếm sách
		// Thanh tìm kiếm sách
		searchBook = new JTextField("Search by Book...");
		searchBook.setBounds(548, 310, 274, 45);
		searchBook.setForeground(Color.GRAY);
		searchBook.setFont(new Font("Arial", Font.PLAIN, 14));
		searchBook.setBackground(Color.WHITE);
		searchBook.setBorder(new RoundedBorder(15));
		searchBook.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (searchBook.getText().equals("Search by Book...")) {
					searchBook.setText("");
					searchBook.setForeground(Color.BLACK);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (searchBook.getText().isEmpty()) {
					searchBook.setText("Search by Book...");
					searchBook.setForeground(Color.GRAY);
				}
			}
		});
		searchBook.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				filterBookData(); // Gọi phương thức tìm kiếm
			}
		});
		panelRental.add(searchBook);

		// Bảng hiển thị sách
		tableBook = new JTable();
		tableBook.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableBook.setAutoCreateRowSorter(true);
		tableBook.setBorder(null);
		tableBook.setBackground(new Color(255, 255, 255));
		tableBook.setShowVerticalLines(false);
		tableBook.setRowHeight(40);
		customizeTable(tableBook);

		// Thêm bảng sách vào JScrollPane
		scrollBook = new JScrollPane(tableBook);
		scrollBook.setBorder(null);
		scrollBook.setBounds(463, 366, 438, 279);
		panelRental.add(scrollBook);
		scrollBook.setBorder(BorderFactory.createEmptyBorder()); // Remove border
		scrollBook.getViewport().setBorder(null); // Remove viewport border

		// Bảng hiển thị sinh viên
		tableStudent = new JTable();
		tableStudent.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableStudent.setBorder(null);
		tableStudent.setBackground(new Color(255, 255, 255));
		tableStudent.setShowVerticalLines(false);
		tableStudent.setAutoCreateRowSorter(true);
		tableStudent.setRowHeight(70); // Tăng chiều cao hàng để phù hợp với Avatar
		customizeTable(tableStudent);

		// Thêm bảng vào JScrollPane
		var scrollStudent = new JScrollPane(tableStudent);
		scrollStudent.setBounds(0, 366, 442, 279);
		panelRental.add(scrollStudent);
		scrollStudent.setBorder(null);
		scrollStudent.setBorder(BorderFactory.createEmptyBorder()); // Remove border

		lblStudentCode = new JLabel("Student Code:");
		lblStudentCode.setForeground(new Color(0, 0, 255));
		lblStudentCode.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblStudentCode.setBounds(24, 22, 108, 34);
		panelRental.add(lblStudentCode);

		lblStudentName = new JLabel("Student Name:");
		lblStudentName.setForeground(new Color(0, 0, 255));
		lblStudentName.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblStudentName.setBounds(385, 22, 108, 34);
		panelRental.add(lblStudentName);

		lblNumberPhone = new JLabel("Number Phone:");
		lblNumberPhone.setForeground(new Color(0, 0, 255));
		lblNumberPhone.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNumberPhone.setBounds(25, 79, 108, 34);
		panelRental.add(lblNumberPhone);

		lblStudentEmail = new JLabel("Student Email:");
		lblStudentEmail.setForeground(new Color(0, 0, 255));
		lblStudentEmail.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblStudentEmail.setBounds(385, 79, 108, 34);
		panelRental.add(lblStudentEmail);

		textStudentCode = new JTextField();
		textStudentCode.setBackground(new Color(206, 255, 255));
		textStudentCode.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textStudentCode.setBounds(142, 22, 185, 34);
		panelRental.add(textStudentCode);
		textStudentCode.setColumns(10);
		textStudentCode.setEditable(false); // Không cho chỉnh sửa
		textStudentCode.setFocusable(false); // Không cho phép click chuột vào

		textStudentName = new JTextField();
		textStudentName.setBackground(new Color(206, 255, 255));
		textStudentName.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textStudentName.setColumns(10);
		textStudentName.setBounds(503, 22, 185, 34);
		panelRental.add(textStudentName);
		textStudentName.setEditable(false); // Không cho chỉnh sửa
		textStudentName.setFocusable(false); // Không cho phép click chuột vào

		textNumberPhone = new JTextField();
		textNumberPhone.setBackground(new Color(206, 255, 255));
		textNumberPhone.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textNumberPhone.setColumns(10);
		textNumberPhone.setBounds(143, 79, 184, 34);
		panelRental.add(textNumberPhone);
		textNumberPhone.setEditable(false); // Không cho chỉnh sửa
		textNumberPhone.setFocusable(false); // Không cho phép click chuột vào

		textStudentEmail = new JTextField();
		textStudentEmail.setBackground(new Color(206, 255, 255));
		textStudentEmail.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textStudentEmail.setColumns(10);
		textStudentEmail.setBounds(503, 79, 185, 34);
		panelRental.add(textStudentEmail);
		textStudentEmail.setEditable(false); // Không cho chỉnh sửa
		textStudentEmail.setFocusable(false); // Không cho phép click chuột vào

		scrollRentalBook = new JScrollPane();

		// Đặt vị trí và kích thước của JScrollPane
		scrollRentalBook.setBounds(24, 124, 848, 163);

		// Thiết lập màu nền cho JScrollPane và Viewport
		scrollRentalBook.setBackground(new Color(255, 255, 255)); // Màu nền trắng cho JScrollPane
		scrollRentalBook.getViewport().setBackground(new Color(255, 255, 255)); // Màu nền trắng cho Viewport bên trong

		// Loại bỏ viền của JScrollPane
		scrollRentalBook.setBorder(BorderFactory.createEmptyBorder()); // Xóa viền bên ngoài JScrollPane

		panelRental.add(scrollRentalBook);

		tableRentalBook = new JTable();
		tableRentalBook.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableRentalBook.addInputMethodListener(new InputMethodListener() {
			@Override
			public void caretPositionChanged(InputMethodEvent event) {
			}

			@Override
			public void inputMethodTextChanged(InputMethodEvent event) {
				tableRentalBookInputMethodTextChanged(event);
			}
		});
		tableRentalBook.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				tableRentalBookKeyTyped(e);
			}

			@Override
			public void keyReleased(KeyEvent e) {
				tableRentalBookKeyReleased(e);
			}
		});
		tableRentalBook.setBackground(new Color(255, 255, 255));
		scrollRentalBook.setViewportView(tableRentalBook);

		btnBookrental = new JButton("");
		btnBookrental.setIcon(new ImageIcon(BookRental.class.getResource("/icon6/thueSach.png")));
		btnBookrental.addActionListener(this::btnBookrentalActionPerformed);
		btnBookrental.setBounds(758, 45, 62, 68);
		panelRental.add(btnBookrental);

		btnReloadStudent = new JButton("");
		btnReloadStudent.addActionListener(this::btnReloadStudentActionPerformed);
		btnReloadStudent.setIcon(new ImageIcon(BookRental.class.getResource("/icon4/redo.png")));
		btnReloadStudent.setBounds(366, 310, 62, 45);
		panelRental.add(btnReloadStudent);

		btnReloadBook = new JButton("");
		btnReloadBook.addActionListener(this::btnReloadBookActionPerformed);
		btnReloadBook.setIcon(new ImageIcon(BookRental.class.getResource("/icon4/redo.png")));
		btnReloadBook.setBounds(829, 310, 62, 45);
		panelRental.add(btnReloadBook);
		scrollStudent.getViewport().setBorder(null); // Remove viewport border

	}

	private void filterBookData() {
		var searchText = searchBook.getText().trim().toLowerCase();

		// Nếu ô tìm kiếm trống hoặc chứa văn bản mặc định, hiển thị toàn bộ dữ liệu
		if (searchText.equals("search by book...") || searchText.isEmpty()) {
			loadBookData(); // Hiển thị toàn bộ dữ liệu sách
			return;
		}

		var bookDao = new BookManagementDao();
		var filteredBooks = bookDao.selectInStock().stream()
				.filter(book -> book.getIsbn().toLowerCase().contains(searchText)
				|| book.getTitle().toLowerCase().contains(searchText)).toList();

		var model = (DefaultTableModel) tableBook.getModel();
		model.setRowCount(0); // Xóa dữ liệu cũ

		for (var book : filteredBooks) {
			ImageIcon imageIcon = null;
			if (book.getImage() != null && !book.getImage().isEmpty()) {
				var resource = "src/main/resources/images/" + book.getImage();
				imageIcon = new ImageIcon(
						new ImageIcon(resource).getImage().getScaledInstance(50, 70, Image.SCALE_SMOOTH));
			}

			if (imageIcon == null) {
				imageIcon = new ImageIcon(
						new ImageIcon(getClass().getClassLoader().getResource("images/default_book.png")).getImage()
								.getScaledInstance(70, 70, Image.SCALE_SMOOTH));
			}

			model.addRow(new Object[] { imageIcon, book.getIsbn(), book.getTitle(),
					book.getPrice() != null ? book.getPrice().doubleValue() : 0.0,
					book.getRentalPrice() != null ? book.getRentalPrice().doubleValue() : 0.0 });
		}
	}

	private void initializeReturnComponents() {
		// Button thêm trả sách
		var btnAddReturn = new JButton("");
		btnAddReturn.addActionListener(this::btnAddReturnActionPerformed);
		btnAddReturn.setIcon(new ImageIcon(BookRental.class.getResource("/icon7/return-32.png")));
		btnAddReturn.setBounds(22, 24, 51, 45);
		btnAddReturn.setToolTipText("Add Return");
		panelReturn.add(btnAddReturn);

		// Thanh tìm kiếm trả sách
		searchReturn = new JTextField("Search by Return Info...");
		searchReturn.setBounds(94, 24, 343, 45);
		searchReturn.setForeground(Color.GRAY);
		searchReturn.setFont(new Font("Arial", Font.PLAIN, 14));
		searchReturn.setBackground(Color.WHITE);
		searchReturn.setBorder(new RoundedBorder(15));

		searchReturn.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (searchReturn.getText().equals("Search by Return Info...")) {
					searchReturn.setText("");
					searchReturn.setForeground(Color.BLACK);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (searchReturn.getText().isEmpty()) {
					searchReturn.setText("Search by Return Info...");
					searchReturn.setForeground(Color.GRAY);
				}
			}
		});

		searchReturn.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				filterReturnData(); // Gọi phương thức tìm kiếm
			}
		});

		panelReturn.add(searchReturn);

		// Bảng hiển thị trả sách
		tableReturn = new JTable();
		tableReturn.setAutoCreateRowSorter(true);
		tableReturn.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableReturn.setBorder(null);
		tableReturn.setBackground(new Color(255, 255, 255));
		tableReturn.setShowVerticalLines(false);
		customizeTable(tableReturn);

		// Thêm bảng trả sách vào JScrollPane
		var scrollReturn = new JScrollPane(tableReturn);
		scrollReturn.setBorder(null);
		scrollReturn.setBounds(10, 89, 866, 545);
		scrollReturn.setBorder(BorderFactory.createEmptyBorder()); // Remove border
		scrollReturn.getViewport().setBorder(null); // Remove viewport border
		scrollReturn.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollReturn.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		panelReturn.add(scrollReturn);

		btnReloadReturn = new JButton("");
		btnReloadReturn.addActionListener(this::btnReloadReturnActionPerformed);
		btnReloadReturn.setIcon(new ImageIcon(BookRental.class.getResource("/icon4/redo.png")));
		btnReloadReturn.setBounds(447, 24, 51, 45);
		panelReturn.add(btnReloadReturn);

	}

	// Phương thức tìm kiếm trả sách
	private void filterReturnData() {
		var searchText = searchReturn.getText().trim().toLowerCase();

		if (searchText.equals("search by return info...") || searchText.isEmpty()) {
			loadReturnData(); // Hiển thị toàn bộ dữ liệu trả sách
			return;
		}

		var filteredRecords = borrowRecords.stream()
				.filter(record -> record.getBookName().toLowerCase().contains(searchText)
						|| record.getUserName().toLowerCase().contains(searchText)
						|| record.getStudentName().toLowerCase().contains(searchText)
//						|| (record.getStudentCode() != null
//								&& record.getStudentCode().toLowerCase().contains(searchText))
						|| record.getStudentCode().toLowerCase().contains(searchText))

				.toList();

		var model = (DefaultTableModel) tableReturn.getModel();
		model.setRowCount(0); // Xóa dữ liệu cũ

		// Gắn model vào tableReturn
		tableReturn.setModel(model);
		for (var record : filteredRecords) {
			// Tải hình ảnh bìa sách
			var bookCover = loadBookCover(record.getBookImage());

			model.addRow(new Object[] { bookCover, record.getBookName(), record.getIsbn(), record.getDueReturnDate(),
					record.getUserEmail(), record.getUserName(), record.getStudentCode(), record.getStudentName(),
					record.getQuantity(), // Số lượng
					record.getBorrowDate(), // Ngày mượn
					record.getRecordID(), record.getStudentPhoneNum(), record.getStudentEmail(), });
		}
	}

	private void loadStudentData() {
		// Lấy danh sách sinh viên từ cơ sở dữ liệu
		var studentDao = new StudentDao();
		students = studentDao.selectValidCard();

		// Tạo DefaultTableModel với các cột bổ sung
		var model = new DefaultTableModel(
				new Object[] { "Avatar", "Code", "Name", "Phone", "Email", "Graduation Year" }, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public Class<?> getColumnClass(int column) {
				return switch (column) {
				case 0 -> ImageIcon.class; // Cột Avatar là hình ảnh
				case 1, 2, 3, 4, 5 -> String.class; // Các cột khác là chuỗi
				default -> Object.class;
				};
			}

			@Override
			public boolean isCellEditable(int row, int column) {
				return false; // Không cho phép chỉnh sửa
			}
		};

		// Đổ dữ liệu vào model
		for (StudentEntity student : students) {
			ImageIcon avatarIcon = null;
			if (student.getAvatar() != null && !student.getAvatar().isEmpty()) {
				var resource = "src/main/resources/avatar/" + student.getAvatar();
				avatarIcon = new ImageIcon(
						new ImageIcon(resource).getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH));

			}

			if (avatarIcon == null) {
				avatarIcon = new ImageIcon(
						new ImageIcon(getClass().getClassLoader().getResource("avatar/default_avatar.png")).getImage()
								.getScaledInstance(70, 70, Image.SCALE_SMOOTH));
			}

			model.addRow(new Object[] { avatarIcon, student.getStudentCode(), student.getFullName(),
					student.getPhoneNumber(), student.getEmail(), student.getEnrollmentYear() });
		}

		// Gắn model vào tableStudent
		tableStudent.setModel(model);

		// Ẩn các cột Phone và Email
//		tableStudent.getColumnModel().removeColumn(tableStudent.getColumnModel().getColumn(4)); // Cột Email
//		tableStudent.getColumnModel().removeColumn(tableStudent.getColumnModel().getColumn(3)); // Cột Phone

		tableStudent.getColumnModel().getColumn(3).setMinWidth(0);
		tableStudent.getColumnModel().getColumn(3).setMaxWidth(0);
		tableStudent.getColumnModel().getColumn(3).setPreferredWidth(0);

		tableStudent.getColumnModel().getColumn(4).setMinWidth(0);
		tableStudent.getColumnModel().getColumn(4).setMaxWidth(0);
		tableStudent.getColumnModel().getColumn(4).setPreferredWidth(0);
	}

	private void loadBookData() {
		// Sử dụng BookManagementDao để lấy danh sách sách từ cơ sở dữ liệu
		var bookDao = new BookManagementDao();
		books = bookDao.selectInStock();

		// Tạo DefaultTableModel chỉ với 5 cột
		var model = new DefaultTableModel(
				new Object[] { "Image", "ISBN", "Book Name", "Rental Price", "Price", "Deposit Percentage", "Price" },
				0) {
			private static final long serialVersionUID = 1L;

			@Override
			public Class<?> getColumnClass(int column) {
				return switch (column) {
				case 0 -> ImageIcon.class; // Cột Image là hình ảnh
				case 1, 2 -> String.class; // ISBN và Title là chuỗi
				case 3, 4, 5, 6 -> Double.class; // Price và Rental Price là số thập phân
				default -> Object.class;
				};
			}

			@Override
			public boolean isCellEditable(int row, int column) {
				return false; // Không cho phép chỉnh sửa
			}
		};

		// Đổ dữ liệu vào model
		for (var book : books) {
			ImageIcon imageIcon = null;
			if (book.getImage() != null && !book.getImage().isEmpty()) {
				var resource = "src/main/resources/images/" + book.getImage();
				imageIcon = new ImageIcon(
						new ImageIcon(resource).getImage().getScaledInstance(50, 70, Image.SCALE_SMOOTH));
			}

			if (imageIcon == null) {
				imageIcon = new ImageIcon(
						new ImageIcon(getClass().getClassLoader().getResource("images/default_book.png")).getImage()
								.getScaledInstance(70, 70, Image.SCALE_SMOOTH));
			}

			model.addRow(new Object[] { imageIcon, book.getIsbn(), book.getTitle(),
					book.getRentalPrice() != null ? book.getRentalPrice().doubleValue() : 0.0,
					book.getPrice() != null ? book.getPrice().doubleValue() : 0.0,
					book.getDepositPercentage() != null ? book.getDepositPercentage().doubleValue() : 0.0,
					book.getPrice() != null ? book.getPrice().doubleValue() : 0.0 // Thêm giá sách (Price)
			});
		}

		// Gắn model vào tableBook
		tableBook.setModel(model);
		// Ẩn cột `Deposit Percentage`
		tableBook.getColumnModel().getColumn(5).setMinWidth(0);
		tableBook.getColumnModel().getColumn(5).setMaxWidth(0);
		tableBook.getColumnModel().getColumn(5).setPreferredWidth(0);
		// Ẩn cột `Price`
		tableBook.getColumnModel().getColumn(6).setMinWidth(0);
		tableBook.getColumnModel().getColumn(6).setMaxWidth(0);
		tableBook.getColumnModel().getColumn(6).setPreferredWidth(0);

		tableBook.getColumnModel().getColumn(4).setMinWidth(0);
		tableBook.getColumnModel().getColumn(4).setMaxWidth(0);
		tableBook.getColumnModel().getColumn(4).setPreferredWidth(0);

		// Tạo renderer tùy chỉnh cho cột "Rental Price"
		var priceRenderer = new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 1L;
			private final DecimalFormat formatter = new DecimalFormat("#,###");

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				if (value instanceof Double || value instanceof Float) {
					value = formatter.format(value); // Định dạng giá trị thành "20,000"
				}
				var component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				setHorizontalAlignment(CENTER); // Căn giữa nội dung
				return component;
			}
		};

		// Gắn renderer vào cột "Rental Price"
		tableBook.getColumnModel().getColumn(3).setCellRenderer(priceRenderer);

	}

	private void filterStudentData() {
		var searchText = searchStudent.getText().trim().toLowerCase();

		if (searchText.equals("search by student...") || searchText.isEmpty()) {
			loadStudentData(); // Hiển thị toàn bộ dữ liệu
			return;
		}

		var filteredStudents = students.stream()
				.filter(student -> student.getStudentCode().toLowerCase().contains(searchText)
						|| student.getFullName().toLowerCase().contains(searchText))
				.toList();

		var model = (DefaultTableModel) tableStudent.getModel();
		model.setRowCount(0); // Xóa dữ liệu cũ
		for (StudentEntity student : filteredStudents) {
			ImageIcon avatarIcon = null;
			if (student.getAvatar() != null && !student.getAvatar().isEmpty()) {
				var resource = "src/main/resources/avatar/" + student.getAvatar();
				avatarIcon = new ImageIcon(
						new ImageIcon(resource).getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH));
			}

			if (avatarIcon == null) {
				avatarIcon = new ImageIcon(
						new ImageIcon(getClass().getClassLoader().getResource("avatar/default_avatar.png")).getImage()
								.getScaledInstance(70, 70, Image.SCALE_SMOOTH));
			}

			model.addRow(new Object[] { avatarIcon, student.getStudentCode(), student.getFullName(),
					student.getPhoneNumber(), student.getEmail() });
		}
	}

	private void customizeTable(JTable table) {
		table.setFont(new Font("Arial", Font.PLAIN, 14)); // Đặt font cho bảng
		table.setGridColor(new Color(224, 224, 224)); // Màu đường kẻ bảng
		table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16)); // Font của tiêu đề bảng
		table.getTableHeader().setPreferredSize(new Dimension(100, 40)); // Chiều cao của tiêu đề bảng
		table.setRowHeight(80); // Chiều cao của mỗi hàng trong bảng

		// Tùy chỉnh tiêu đề (Header)
		var headerRenderer = new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				var headerLabel = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
				headerLabel.setBackground(new Color(220, 240, 255));
				headerLabel.setForeground(new Color(128, 117, 180));
				setHorizontalAlignment(CENTER); // Căn giữa tiêu đề
				return headerLabel;
			}
		};
		table.getTableHeader().setDefaultRenderer(headerRenderer);

		// Tạo renderer để căn giữa nội dung trong bảng
		var centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
		table.setDefaultRenderer(Object.class, centerRenderer); // Căn giữa mọi kiểu dữ liệu
	}

	private void loadReturnData() {
		// Lấy danh sách bản ghi mượn sách từ DAO
		var borrowRecordsDao = new BorrowRecordsDao(ConnectDB.getCon());
		borrowRecords = borrowRecordsDao.selectBrrow();

		// Tạo DefaultTableModel cho bảng tableReturn
		var model = new DefaultTableModel(new Object[] { "Book Cover", "Book Name", "ISBN", "Due Return Date",
				"Lender Email", "Lender Name", "Retal Student Code", "Retal Student Name", "Quantity", "Borrow Date",
				"RecordID", "Student Phone", "Student Email" }, 0) {

			private static final long serialVersionUID = 1L;

			@Override
			public Class<?> getColumnClass(int column) {
				return switch (column) {
				case 0 -> ImageIcon.class; // Cột Image là hình ảnh
				case 1, 2, 4, 5, 6, 7 -> String.class; // ISBN và Title là chuỗi
				case 8 -> Double.class;
				case 3, 9, 11, 12 -> String.class;
				case 10 -> int.class;
				default -> Object.class;
				};
			}

			@Override
			public boolean isCellEditable(int row, int column) {
				return false; // Không cho phép chỉnh sửa dữ liệu
			}
		};

		for (BorrowRecordsEntity record : borrowRecords) {
			// Tải hình ảnh bìa sách
			var bookCover = loadBookCover(record.getBookImage());
			model.addRow(
					new Object[] { bookCover, record.getBookName(), record.getIsbn(), record.getDueReturnDate(),
							record.getUserEmail(),
							record.getUserName(), record.getStudentCode(),
							record.getStudentName(), record.getQuantity(), // Số lượng
							record.getBorrowDate(), // Ngày mượn
							record.getRecordID(),
							record.getStudentPhoneNum(),
							record.getStudentEmail(),
			});
		}

		// Gắn model vào tableReturn
		tableReturn.setModel(model);

		// Tùy chỉnh hiển thị bảng
		customizeTableReturn();

//		tableReturn.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		adjustColumnWidths(tableReturn);

	}

	// Phương thức tải ảnh bìa sách từ thư mục "image"
	private ImageIcon loadBookCover(String image) {
		var imagePath = "src/main/resources/images/" + image; // Đường dẫn tới file ảnh (có thể đổi đuôi thành .png
																// nếu cần)
		var icon = new ImageIcon(imagePath);
		if (icon.getIconWidth() == -1) {
			// Nếu không tìm thấy ảnh, sử dụng ảnh mặc định
			icon = new ImageIcon("image/default.png");
		}

		// Thay đổi kích thước ảnh (nếu cần)
		var scaledImage = icon.getImage().getScaledInstance(50, 70, Image.SCALE_SMOOTH);
		return new ImageIcon(scaledImage);
	}

	private void customizeTableReturn() {
		var header = tableReturn.getTableHeader();
		// Tùy chỉnh renderer cho tiêu đề
		header.setDefaultRenderer(new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
//				var label = new JLabel(
//						"<html><center>" + value.toString().replace(" ", "<br>") + "</center></html>");
				var label = new JLabel(value.toString());
				label.setFont(new Font("Arial", Font.BOLD, 14));
				label.setHorizontalAlignment(SwingConstants.CENTER);
				label.setVerticalAlignment(SwingConstants.CENTER); // Căn giữa theo chiều dọc
				label.setBackground(new Color(220, 240, 255));
				label.setForeground(new Color(128, 117, 180));
				label.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY)); // Viền dưới
				label.setOpaque(true); // Để màu nền có hiệu lực
				return label;
			}
		});
		header.setPreferredSize(new Dimension(header.getPreferredSize().width, 50)); // Chiều cao header
		header.setFont(new Font("Arial", Font.BOLD, 14)); // Font header
		tableReturn.setRowHeight(100); // Chiều cao mỗi hàng (đủ lớn để hiển thị hình ảnh)
		tableReturn.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				if (value instanceof ImageIcon) {
					return new JLabel((ImageIcon) value); // Hiển thị hình ảnh
				}
				return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			}
		});

	}

	private void adjustColumnWidths(JTable table) {
		for (var column = 0; column < table.getColumnCount(); column++) {
			var preferredWidth = 50; // Chiều rộng tối thiểu
//			for (var row = 0; row < table.getRowCount(); row++) {
//				var cellRenderer = table.getCellRenderer(row, column);
//				var c = table.prepareRenderer(cellRenderer, row, column);
//				preferredWidth = Math.max(c.getPreferredSize().width + 10, preferredWidth);
//			}
			table.getColumnModel().getColumn(column).setPreferredWidth(preferredWidth);
			if (column == 2 || column == 3 || column == 4 || column == 5 || column == 8 || column >= 10) {
				table.getColumnModel().getColumn(column).setMinWidth(0);
				table.getColumnModel().getColumn(column).setMaxWidth(0);
			}
		}

	}

	public class RoundedBorder implements Border {
		private int radius;

		public RoundedBorder(int radius) {
			this.radius = radius;
		}

		@Override
		public Insets getBorderInsets(Component c) {
			return new Insets(radius + 1, radius + 1, radius + 1, radius + 1);
		}

		@Override
		public boolean isBorderOpaque() {
			return true;
		}

		@Override
		public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
			g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
		}
	}

	protected void btnAddStudentActionPerformed(ActionEvent e) {
		var selectedRow = tableStudent.getSelectedRow(); // Lấy hàng được chọn trong bảng tableStudent

		if (selectedRow != -1) { // Kiểm tra xem có hàng nào được chọn không
			// Lấy dữ liệu từ các cột trong model (bao gồm cả cột ẩn)
			var studentCode = tableStudent.getModel().getValueAt(selectedRow, 1).toString(); // Cột "Code"
			var studentName = tableStudent.getModel().getValueAt(selectedRow, 2).toString(); // Cột "Name"
			var numberPhone = tableStudent.getModel().getValueAt(selectedRow, 3).toString(); // Cột "Phone" (ẩn)
			var studentEmail = tableStudent.getModel().getValueAt(selectedRow, 4).toString(); // Cột "Email" (ẩn)

			// Điền thông tin vào các ô nhập liệu
			textStudentCode.setText(studentCode);
			textStudentName.setText(studentName);
			textNumberPhone.setText(numberPhone);
			textStudentEmail.setText(studentEmail);
		} else {
			// Nếu không có hàng nào được chọn, hiển thị thông báo
			System.out.println("No student selected. Please select a student from the table.");
		}

	}

	private void initializeRentalBookTable() {
		// Tạo model cho bảng RentalBook
		var model = new DefaultTableModel(new Object[] { "Book ID", "Book Name", "Quantity", "Rental Price",
				"Number Of Days", "Return Date", "Deposit Percentage", "Price" }, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				// Chỉ cho phép chỉnh sửa cột "Quantity", "Return Date"
				return column == 5;
			}
		};

		tableRentalBook.setModel(model);
		// Ẩn cột `Deposit Percentage`
		tableRentalBook.getColumnModel().getColumn(6).setMinWidth(0);
		tableRentalBook.getColumnModel().getColumn(6).setMaxWidth(0);
		tableRentalBook.getColumnModel().getColumn(6).setPreferredWidth(0);
		// Ẩn cột `Price`
		tableRentalBook.getColumnModel().getColumn(7).setMinWidth(0);
		tableRentalBook.getColumnModel().getColumn(7).setMaxWidth(0);
		tableRentalBook.getColumnModel().getColumn(7).setPreferredWidth(0);

		// Tạo renderer tùy chỉnh để căn giữa tất cả các cột
		var centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER); // Căn giữa nội dung

		// Áp dụng renderer cho tất cả các cột trong bảng
		for (var i = 0; i < tableRentalBook.getColumnCount(); i++) {
			tableRentalBook.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}

		// Gắn JSpinnerCellEditor cho cột "Quantity"
		tableRentalBook.getColumnModel().getColumn(2).setCellEditor(new JSpinnerCellEditor());
		// Gắn JDateChooserCellEditor cho cột "Return Date"
		var returnDateEditor = new JDateChooserCellEditor();
		tableRentalBook.getColumnModel().getColumn(5).setCellEditor(returnDateEditor);

		// Lắng nghe khi kết thúc chỉnh sửa cột "Return Date"
		returnDateEditor.addCellEditorListener(new javax.swing.event.CellEditorListener() {
			@Override
			public void editingStopped(javax.swing.event.ChangeEvent e) {
				var row = tableRentalBook.getSelectedRow();
				if (row != -1) {
					updateNumberOfDays(row); // Tính lại số ngày khi giá trị "Return Date" thay đổi
				}
			}

			@Override
			public void editingCanceled(javax.swing.event.ChangeEvent e) {
				// Không cần xử lý nếu chỉnh sửa bị hủy
			}
		});
		// Tăng chiều cao hàng
		tableRentalBook.setRowHeight(30);

		// **Tùy chỉnh header**
		var headerRenderer = new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 1L;

	        @Override
	        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
	                boolean hasFocus, int row, int column) {
	            var headerLabel = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
	                    column);
	            headerLabel.setFont(new Font("Arial", Font.BOLD, 14)); // In đậm font chữ
	            headerLabel.setBackground(new Color(220, 240, 255));
				headerLabel.setForeground(new Color(128, 117, 180));
	            headerLabel.setHorizontalAlignment(JLabel.CENTER); // Căn giữa tiêu đề
	            return headerLabel;
	        }
	    };

		tableRentalBook.getTableHeader().setDefaultRenderer(headerRenderer);
		tableRentalBook.getTableHeader().setPreferredSize(new Dimension(100, 30)); // Đặt chiều cao header

		// Thêm TableModelListener để theo dõi thay đổi trong bảng
		model.addTableModelListener(e -> {
			var row = e.getFirstRow();
			var column = e.getColumn();

			// Kiểm tra nếu cột bị thay đổi là "Return Date"
			if (column == 5) {
				updateNumberOfDays(row);
			}
		});

		// Lắng nghe thay đổi dữ liệu trong bảng
		model.addTableModelListener(e -> {
			var row = e.getFirstRow();
			var column = e.getColumn();

			// Kiểm tra nếu cột thay đổi là cột "Quantity"
			if (column == 2) {
				checkQuantity(row);
			}

		});
		// Thêm chức năng chuột phải với menu xóa
		addDeleteFunctionality();
	}

	private void checkQuantity(int row) {

		var model = (DefaultTableModel) tableRentalBook.getModel();

		// Lấy Book ID và Quantity từ bảng
		var bookId = model.getValueAt(row, 0).toString();
		var quantity = Integer.parseInt(model.getValueAt(row, 2).toString());

		// Lấy giá trị Quantity từ BookManagementEntity
		var bookDao = new BookManagementDao();
		var book = new BookManagementEntity();
		book = bookDao.getBookWithDetails(bookId); // Phương thức này trả về một đối tượng
													// BookManagementEntity

		if (book != null) {
			var availableQuantity = book.getQuantity(); // Lấy số lượng có sẵn từ BookManagementEntity

			// So sánh và thông báo lỗi nếu không đủ số lượng
			if (quantity > availableQuantity) {
				JOptionPane.showMessageDialog(null,
						"The quantity entered exceeds the available stock for Book ID: " + bookId, "Error",
						JOptionPane.ERROR_MESSAGE);

				// Reset lại giá trị Quantity trong bảng
				model.setValueAt(availableQuantity, row, 2);
			}
		} else {
			JOptionPane.showMessageDialog(null, "Book ID: " + bookId + " not found in the database.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void addDeleteFunctionality() {
		// Tạo popup menu
		var popupMenu = new JPopupMenu();
		var deleteMenuItem = new JMenuItem("Delete");
		popupMenu.add(deleteMenuItem);

		// Thêm sự kiện xóa hàng khi chọn "Delete"
		deleteMenuItem.addActionListener(e -> {
			var selectedRow = tableRentalBook.getSelectedRow();
			if (selectedRow != -1) {
				((DefaultTableModel) tableRentalBook.getModel()).removeRow(selectedRow); // Xóa hàng được chọn
			} else {
				System.err.println("No row selected for deletion.");
			}
		});

		// Xử lý chuột phải để hiển thị popup menu và chọn hàng
		tableRentalBook.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mousePressed(java.awt.event.MouseEvent e) {
				if (e.isPopupTrigger()) {
					selectRowAtPoint(e);
				}
			}

			@Override
			public void mouseReleased(java.awt.event.MouseEvent e) {
				if (e.isPopupTrigger()) {
					selectRowAtPoint(e);
					popupMenu.show(e.getComponent(), e.getX(), e.getY()); // Hiển thị popup menu tại vị trí chuột
				}
			}

			// Chọn hàng tại vị trí chuột phải
			private void selectRowAtPoint(java.awt.event.MouseEvent e) {
				var row = tableRentalBook.rowAtPoint(e.getPoint());
				if (row >= 0 && row < tableRentalBook.getRowCount()) {
					tableRentalBook.setRowSelectionInterval(row, row); // Chọn hàng tại vị trí chuột
				} else {
					tableRentalBook.clearSelection(); // Hủy chọn nếu không có hàng nào tại vị trí chuột
				}
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				tableRentalBookMouseClicked(e);
			}
		});
	}

	private void updateNumberOfDays(int row) {
		var model = (DefaultTableModel) tableRentalBook.getModel();
		var returnDateValue = model.getValueAt(row, 5);

		if (returnDateValue instanceof java.util.Date returnDate) {
			var currentDate = new java.util.Date();

			// Tính toán số ngày giữa ngày hiện tại và ngày trả
			var diffInMillies = returnDate.getTime() - currentDate.getTime();
			var numberOfDays = (int) (diffInMillies / (1000 * 60 * 60 * 24)) + 1;

			// Đảm bảo số ngày không âm
			numberOfDays = Math.max(numberOfDays, 1);

			// Cập nhật cột "Number Of Days"
			model.setValueAt(numberOfDays, row, 4);
		}
	}

	public class JDateChooserCellEditor extends AbstractCellEditor implements TableCellEditor {
		private static final long serialVersionUID = 1L;
		private JDateChooser dateChooser;

		public JDateChooserCellEditor() {
			dateChooser = new JDateChooser();
			dateChooser.setDateFormatString("yyyy-MM-dd"); // Định dạng ngày
			dateChooser.setMinSelectableDate(new java.util.Date()); // Chỉ cho phép chọn ngày >= ngày hiện tại
		}

		@Override
		public Object getCellEditorValue() {
			var selectedDate = dateChooser.getDate();
			if (selectedDate != null) {
				return new java.sql.Date(selectedDate.getTime()); // Chuyển sang java.sql.Date để loại bỏ giờ
			}
			return null;
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
				int column) {
			if (value != null && value instanceof java.util.Date) {
				dateChooser.setDate((java.util.Date) value); // Đặt giá trị ngày hiện tại (nếu có)
			} else {
				dateChooser.setDate(new java.util.Date()); // Nếu không có giá trị, đặt mặc định là ngày hôm nay
			}
			return dateChooser;
		}
	}

	private int highlightRowIndex = -1; // Chỉ số của dòng cần highlight
	private JButton btnReloadReturn;
	private JButton btnReloadStudent;
	private JButton btnReloadBook;

	protected void btnAddBookActionPerformed(ActionEvent e) {
		var selectedRow = tableBook.getSelectedRow();

		if (selectedRow != -1) {
			var bookID = tableBook.getModel().getValueAt(selectedRow, 1).toString(); // Lấy BookID
			var bookName = tableBook.getModel().getValueAt(selectedRow, 2).toString(); // Lấy Book Name
			var rentalPrice = Double.parseDouble(tableBook.getModel().getValueAt(selectedRow, 3).toString()); // Lấy
																												// Rental
			var price = Double.parseDouble(tableBook.getModel().getValueAt(selectedRow, 4).toString()); // Giá tiền
			var depositPercentage = Double.parseDouble(tableBook.getModel().getValueAt(selectedRow, 5).toString());

			var lastAddedRowIndex = -1; // Biến để ghi lại dòng vừa được thêm hoặc cập nhật // Price
			var borrowRecordsDao = new BorrowRecordsDao(ConnectDB.getCon());

			if (borrowRecordsDao.selectBrrowIsRetal(textStudentCode.getText(), bookID)) {
				var choice = JOptionPane.showConfirmDialog(this, // Component cha
						"This student has already borrowed this book. Do you still want to proceed?", // Nội dung thông
																										// báo
						"Confirm Book Rental", // Tiêu đề
						JOptionPane.YES_NO_OPTION, // Tùy chọn Yes/No
						JOptionPane.WARNING_MESSAGE // Kiểu icon
				);
				if (choice == JOptionPane.NO_OPTION) {
					return;
				}
			}
			var rentalModel = (DefaultTableModel) tableRentalBook.getModel();
			var bookExists = false; // Biến để kiểm tra xem sách đã tồn tại trong bảng chưa

			for (var i = 0; i < rentalModel.getRowCount(); i++) {
				// Kiểm tra nếu BookID đã tồn tại trong bảng
				if (rentalModel.getValueAt(i, 0).toString().equals(bookID)) {
					// Nếu tồn tại, tăng Quantity thêm 1
					var currentQuantity = Integer.parseInt(rentalModel.getValueAt(i, 2).toString());
					rentalModel.setValueAt(currentQuantity, i, 2); // Tăng Quantity thêm 1
					bookExists = true;
					lastAddedRowIndex = i; // Lưu lại index của dòng được cập nhật
					break; // Thoát khỏi vòng lặp
				}
			}

			// Nếu sách chưa tồn tại trong bảng, thêm mới vào bảng
			if (!bookExists) {
				// Quantity mặc định là 1, NumberOfDays là 1
				rentalModel.addRow(new Object[] { bookID, bookName, 1, rentalPrice, 1, LocalDate.now().plusDays(1),
						depositPercentage, price });
				lastAddedRowIndex = rentalModel.getRowCount() - 1; // Lưu lại index của dòng mới thêm

			} else {
				System.out.println("This book is already added to the rental list.");
			}

			highlightRowIndex = lastAddedRowIndex;

			// Gắn renderer cho cột "Quantity" để căn giữa
			tableRentalBook.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
				private static final long serialVersionUID = 1L;

				@Override
				public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
						boolean hasFocus, int row, int column) {
					var component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
							column);
					setHorizontalAlignment(DefaultTableCellRenderer.CENTER); // Căn giữa

					return component;
				}
			});

			// Gắn renderer cho cột "Quantity" để căn giữa
			tableRentalBook.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
				private static final long serialVersionUID = 1L;

				@Override
				public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
						boolean hasFocus, int row, int column) {
					var component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
							column);
					// Highlight dòng vừa thêm hoặc cập nhật
					if (row == highlightRowIndex) {
						component.setBackground(Color.YELLOW); // Màu nền vàng
						component.setForeground(Color.BLACK); // Màu chữ đen
					} else {
						component.setBackground(Color.WHITE); // Màu nền mặc định (trắng)
						component.setForeground(Color.BLACK); // Màu chữ mặc định
					}
					return component;
				}
			});
			// Yêu cầu bảng render lại
			tableRentalBook.repaint();

			// Tự động cuộn đến dòng mới được thêm (nếu cần)
			tableRentalBook.scrollRectToVisible(tableRentalBook.getCellRect(lastAddedRowIndex, 0, true));

			// Gắn lại renderer cho cột "Rental Price" sau khi thêm dòng
			tableRentalBook.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
				private static final long serialVersionUID = 1L;
				private final DecimalFormat formatter = new DecimalFormat("#,###");

				@Override
				public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
						boolean hasFocus, int row, int column) {
					if (value instanceof Number) {
						value = formatter.format(value); // Định dạng số
					}
					var component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
							column);
					setHorizontalAlignment(DefaultTableCellRenderer.CENTER); // Căn giữa
					return component;
				}
			});
		} else {
			System.out.println("No book selected. Please select a book from the table.");
		}
	}

	public class JSpinnerCellEditor extends AbstractCellEditor implements TableCellEditor {
		private static final long serialVersionUID = 1L;
		private JSpinner spinner;

		public JSpinnerCellEditor() {
			spinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
			spinner.setFont(new Font("Tahoma", Font.PLAIN, 14));
			spinner.setPreferredSize(new Dimension(50, 30)); // Kích thước phù hợp
			spinner.setOpaque(true); // Đảm bảo JSpinner không bị ẩn
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
				int column) {
			if (value != null) {
				spinner.setValue(value);
			}
			return spinner;
		}

		@Override
		public Object getCellEditorValue() {
			return spinner.getValue();
		}
	}

	protected void btnBookrentalActionPerformed(ActionEvent e) {
		initialize++;
		if (initialize == 1) {
			initializeReturnComponents();
			loadReturnData();
		}
		// Dừng chỉnh sửa tất cả các ô đang chỉnh sửa trong bảng
		if (tableRentalBook.isEditing()) {
			var editor = tableRentalBook.getCellEditor();
			if (editor != null) {
				editor.stopCellEditing(); // Dừng chỉnh sửa và lưu giá trị
			}
		}

		// Lấy thông tin từ các JTextField
		var studentCode = textStudentCode.getText();
		var studentName = textStudentName.getText();
		var phoneNumber = textNumberPhone.getText();
		var email = textStudentEmail.getText();

		// Lấy DefaultTableModel gốc của tableRentalBook
		var originalModel = (DefaultTableModel) tableRentalBook.getModel();
		if (studentCode == null || studentCode.trim().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Please Add Student", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (originalModel == null || originalModel.getRowCount() == 0) {
			JOptionPane.showMessageDialog(null, "Please Add Book first!", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		// Tạo một bản sao của DefaultTableModel
		var rentalModel = new DefaultTableModel() {
			/**
			 *
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				// Giữ nguyên logic không cho phép chỉnh sửa dữ liệu
				return false;
			}
		};

		// Sao chép cột từ originalModel
		for (var i = 0; i < originalModel.getColumnCount(); i++) {
			rentalModel.addColumn(originalModel.getColumnName(i));
		}

		// Sao chép dữ liệu từ originalModel
		for (var i = 0; i < originalModel.getRowCount(); i++) {
			var row = new Object[originalModel.getColumnCount()];
			for (var j = 0; j < originalModel.getColumnCount(); j++) {
				row[j] = originalModel.getValueAt(i, j);
			}
			rentalModel.addRow(row);
		}

		// Tạo và hiển thị BookRentalDialog
		var dialog = new BookRentalDialog(SwingUtilities.getWindowAncestor(this), studentCode, studentName, phoneNumber,
				email, rentalModel, this, chart);
		dialog.setVisible(true);
	}

	public void callcheckQuantity() {
		var selectedRow = tableRentalBook.getSelectedRow();

		checkQuantity(selectedRow);
	}

	protected void tableRentalBookKeyTyped(KeyEvent e) {
		callcheckQuantity();
	}

	protected void tableRentalBookKeyReleased(KeyEvent e) {
		callcheckQuantity();
	}

	protected void tableRentalBookInputMethodTextChanged(InputMethodEvent event) {
		callcheckQuantity();
	}

	protected void tableRentalBookPropertyChange(PropertyChangeEvent evt) {
		callcheckQuantity();
	}

	protected void tableRentalBookMouseClicked(MouseEvent e) {
		callcheckQuantity();
	}

	protected void btnAddReturnActionPerformed(ActionEvent e) {
		var selectedRow = tableReturn.getSelectedRow();
		System.out.println(selectedRow);
		if (selectedRow == -1) {
			// Hiển thị thông báo nếu không chọn dòng nào
			JOptionPane.showMessageDialog(this, "Please select a record to return!", "Warning",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		var model = (DefaultTableModel) tableReturn.getModel();
		var rowData = new Object[model.getColumnCount()];

		for (var i = 0; i < model.getColumnCount(); i++) {
			// Kiểm tra và chuyển đổi kiểu dữ liệu nếu cần
			if (i == 10) { // Giả sử cột 10 là "Record ID"
				rowData[i] = Integer.parseInt(model.getValueAt(selectedRow, i).toString());
			} else {
				rowData[i] = model.getValueAt(selectedRow, i);
			}
		}

		// Hiển thị BookReturnDialog
		var dialog = new BookReturnDialog(SwingUtilities.getWindowAncestor(this), rowData, this);
		dialog.setVisible(true);
	}

	@Override
	public void reloadData() {
		clearTableRetal(tableRentalBook);
		loadBookData();
		loadReturnData();
	}

	private void clearTableRetal(JTable table) {
		// Lấy DefaultTableModel từ JTable
		var model = (DefaultTableModel) table.getModel();

		// Xóa toàn bộ dữ liệu nhưng giữ lại cấu trúc cột
		model.setRowCount(0);
	}

	protected void btnReloadStudentActionPerformed(ActionEvent e) {
		loadStudentData();
	}

	protected void btnReloadBookActionPerformed(ActionEvent e) {
		loadBookData();
	}

	protected void btnReloadReturnActionPerformed(ActionEvent e) {
		loadReturnData();
	}

}