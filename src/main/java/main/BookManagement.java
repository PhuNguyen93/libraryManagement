package main;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileOutputStream;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import add.AddBookManagement;
import component.CustomCard;
import component.CustomScrollBarUI;
import component.RoundedPanel;
import dao.BookCardDao;
import dao.BookManagementDao;
import edit.BookManagementEdit;
import entity.BookManagementEntity;
import view.BookManagementViewDialog;

public class BookManagement extends JPanel {

	private static final long serialVersionUID = 1L;
	private JPanel panelTable;
	private JTable tableBookManagement;
	private DefaultTableModel tableModel;
	private JTextField pageInput;
	private JLabel pageInfo;
	private JTextField searchField;
	private int currRow;

	private int currentPage = 1; // Current page
	private final int rowsPerPage = 6; // Rows per page
	private java.util.List<BookManagementEntity> BookManagements; // List of BookManagements
	private java.util.List<BookManagementEntity> filteredBookManagements; // Filtered BookManagements based on search
	private BookManagementViewDialog currentBookManagementViewDialog; // Bảng hiện tại
	private BookManagementEdit currentBookManagementEditDialog; // Thêm biến toàn cục để giữ bảng hiện tại
	private AddBookManagement currentAddBookManagementDialog; // Biến toàn cục để giữ tham chiếu đến AddBookManagement
	private JButton btnDelete;
	private boolean isRental = true;
	private Chart chart;

	public BookManagement(Chart chart) {
		this.chart = chart;
		setBackground(new Color(244, 243, 240));
		setBounds(0, 0, 946, 706);
		setLayout(null);
		addInfoCards(); // Gọi phương thức addInfoCards để thêm các thẻ thông tin

		// Search field
		searchField = new JTextField("Search by Book...");
		searchField.setBounds(75, 27, 796, 29);
		searchField.setForeground(Color.GRAY);
		searchField.setFont(new Font("Arial", Font.PLAIN, 16));
		searchField.setBackground(Color.WHITE);
		searchField.setBorder(null);
		searchField.addFocusListener(new java.awt.event.FocusAdapter() {
			@Override
			public void focusGained(java.awt.event.FocusEvent e) {
				if (searchField.getText().equals("Search by Book...")) {
					searchField.setText("");
					searchField.setForeground(Color.BLACK);
				}
			}

			@Override
			public void focusLost(java.awt.event.FocusEvent e) {
				if (searchField.getText().isEmpty()) {
					searchField.setText("Search by Book...");
					searchField.setForeground(Color.GRAY);
				}
			}
		});

		// Add key listener to update table on each keystroke
		searchField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				filterBookManagements(); // Filter and update table based on search input
			}
		});
		add(searchField);
		// Icon menu
		var menuIcon = new JLabel(new ImageIcon(BookManagement.class.getResource("/icon4/redo.png")));
		menuIcon.setBounds(885, 20, 45, 44);
		menuIcon.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				reloadPage(); // Gọi phương thức reloadPage khi click vào menuIcon
			}
		});
		add(menuIcon);


		// Panel container for table
		// Thay JPanel bằng RoundedPanel với góc bo 15px
		panelTable = new RoundedPanel(15);
		panelTable.setForeground(new Color(0, 0, 0));
		panelTable.setBackground(new Color(255, 255, 255)); // Màu nền
		panelTable.setBorder(null); // Xóa viền mặc định
		panelTable.setBounds(20, 292, 910, 414); // Kích thước và vị trí
		panelTable.setLayout(null); // Layout tùy chỉnh
		add(panelTable); // Thêm panel vào giao diện chính

		// Configure JTable
		tableBookManagement = new JTable();
		tableBookManagement.setAutoCreateRowSorter(true);
		tableBookManagement.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) { // Chuột phải
					var row = tableBookManagement.rowAtPoint(e.getPoint());
					if (row >= 0 && row < tableBookManagement.getRowCount()) {
						tableBookManagement.setRowSelectionInterval(row, row); // Chọn hàng được nhấn
						showPopupMenu(e);
					}
				} else if (e.getButton() == MouseEvent.BUTTON1) {
					currRow = tableBookManagement.rowAtPoint(e.getPoint());

				} // Chuột trái

			}
		});
		tableBookManagement.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableBookManagement.setShowVerticalLines(false);
		tableBookManagement.setFont(new Font("Arial", Font.PLAIN, 14));
		tableBookManagement.setRowHeight(80); // Set row height to 80 for Avatar imagesBookManagementCode

		// Create DefaultTableModel
		tableModel = new DefaultTableModel(new String[] { "BookID", "Image", "ISBN", "Book Name", "Quantity",
				"Stock Quantity", "price", "Rental Price", "isRental",  }, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public Class<?> getColumnClass(int column) {

				return switch (column) {
				case 0 -> Integer.class;
				case 1 -> ImageIcon.class;
				case 2 -> String.class;
				case 3 -> String.class;
				case 4 -> Integer.class;
				case 5 -> Integer.class;
				case 6 -> Integer.class;
				case 7 -> Integer.class;
				case 8 -> Boolean.class;
				default -> String.class;
				};
			}

			@Override
			public boolean isCellEditable(int row, int column) {
				return switch (column) {
				case 0 -> false;
				case 1 -> false;
				case 2 -> false;
				case 3 -> false;
				case 4 -> false;
				case 5 -> false;
				case 6 -> false;
				case 7 -> false;
				default -> true;
				};
			}

		};
		tableBookManagement.setModel(tableModel);

		// Ẩn cột BookManagementID
		tableBookManagement.getColumnModel().getColumn(0).setMinWidth(0);
		tableBookManagement.getColumnModel().getColumn(0).setMaxWidth(0);
		tableBookManagement.getColumnModel().getColumn(0).setPreferredWidth(0);

		tableBookManagement.getColumnModel().getColumn(8).setMinWidth(0);
		tableBookManagement.getColumnModel().getColumn(8).setMaxWidth(0);
		tableBookManagement.getColumnModel().getColumn(8).setPreferredWidth(0);
		// Customize the table
		customizeTable(tableBookManagement);

		// Cấu hình JScrollPane
		var scrollPane = new JScrollPane(tableBookManagement);
		scrollPane.setBounds(20, 11, 869, 345);

		// Đặt nền trắng cho JViewport
		scrollPane.getViewport().setBackground(Color.WHITE);

		// Loại bỏ viền của JScrollPane
		scrollPane.setBorder(BorderFactory.createEmptyBorder());

		// Sử dụng CustomScrollBarUI cho thanh cuộn dọc
		scrollPane.getVerticalScrollBar().setUI(new CustomScrollBarUI());

		// Sử dụng CustomScrollBarUI cho thanh cuộn ngang
		scrollPane.getHorizontalScrollBar().setUI(new CustomScrollBarUI());

		// Tô trắng cả JScrollPane (bao gồm khu vực bên ngoài track)
		scrollPane.setBackground(Color.WHITE);
		scrollPane.getVerticalScrollBar().setBackground(Color.WHITE);
		scrollPane.getHorizontalScrollBar().setBackground(Color.WHITE);

		// Thêm JScrollPane vào panelTable
		panelTable.add(scrollPane);

		tableBookManagement.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				var selectedRow = tableBookManagement.getSelectedRow();
				if (selectedRow !=-1) {
					Object valueAt = tableBookManagement.getValueAt(selectedRow, 8);
					isRental =  (Boolean) valueAt;
			        btnDelete.setEnabled(!isRental);
				}
			}
			
		});

		// Previous button
		var btnPrevious = new JButton("");
		btnPrevious.setIcon(new ImageIcon(BookManagement.class.getResource("/iconNext/previous.png")));
		btnPrevious.setBounds(327, 373, 65, 30);
		btnPrevious.addActionListener(e -> goToPreviousPage());
		panelTable.add(btnPrevious);

		// Page input
		pageInput = new JTextField(String.valueOf(currentPage));
		pageInput.setBounds(410, 373, 50, 30);
		pageInput.setHorizontalAlignment(JTextField.CENTER);
		pageInput.addActionListener(e -> goToPage());
		panelTable.add(pageInput);

		// Next button
		var btnNext = new JButton("");
		btnNext.setIcon(new ImageIcon(BookManagement.class.getResource("/iconNext/next-button.png")));
		btnNext.setBounds(478, 373, 65, 30);
		btnNext.addActionListener(e -> goToNextPage());
		panelTable.add(btnNext);

		// Page info
		pageInfo = new JLabel();
		pageInfo.setBounds(580, 373, 200, 30);
		panelTable.add(pageInfo);

		// Buttons
		var btnAdd = new JButton(new ImageIcon(BookManagement.class.getResource("/icon3/add.png")));
		btnAdd.addActionListener(this::btnAddActionPerformed);
		btnAdd.setBounds(20, 243, 45, 38);
		add(btnAdd);

		var btnEdit = new JButton(new ImageIcon(BookManagement.class.getResource("/icon3/pen.png")));
		btnEdit.addActionListener(this::btnEditActionPerformed);
//		btnEdit.addActionListener(this::btnEditActionPerformed);
		btnEdit.setBounds(85, 243, 45, 38);
		add(btnEdit);

		btnDelete = new JButton(new ImageIcon(BookManagement.class.getResource("/icon3/bin.png")));
		btnDelete.addActionListener(this::btnDeleteActionPerformed);
		btnDelete.setBounds(150, 243, 45, 38);
		add(btnDelete);

		var btnView = new JButton(new ImageIcon(BookManagement.class.getResource("/icon4/search.png")));
		btnView.addActionListener(this::btnViewActionPerformed);
		btnView.setBounds(216, 243, 45, 38);
		add(btnView);

		var btnExcel = new JButton(new ImageIcon(BookManagement.class.getResource("/icon4/xls.png")));
		btnExcel.addActionListener(this::btnExcelActionPerformed);
		btnExcel.setBounds(283, 243, 45, 38);
		add(btnExcel);

		// Load data into the table
		loadBookManagementData();
		updateTable();
	}

	private void reloadPage() {
		// Tải lại dữ liệu từ cơ sở dữ liệu
		loadBookManagementData();

		// Xóa các thẻ thông tin cũ
		removeInfoCards();

		// Thêm lại thẻ thông tin mới
		addInfoCards();

		// Đặt lại các cài đặt tìm kiếm và phân trang
		searchField.setText("Search by Book...");
		currentPage = 1;
		pageInput.setText(String.valueOf(currentPage));

		// Cập nhật lại bảng
		updateTable();

		// Cập nhật lại giao diện
		revalidate();
		repaint();

		// Hiển thị thông báo thành công
		JOptionPane.showMessageDialog(this, "Page and info cards reloaded successfully!");
	}

	private void removeInfoCards() {
		// Tìm và xóa các component liên quan đến thẻ thông tin
		for (Component comp : getComponents()) {
			if (comp instanceof JPanel && comp.getBounds().equals(new java.awt.Rectangle(20, 82, 910, 150))) {
				remove(comp); // Xóa panel chứa thẻ thông tin
			}
		}
	}


	private void addInfoCards() {
		var cardPanel = new JPanel();
		cardPanel.setLayout(new GridLayout(1, 3, 20, 0)); // 3 cards horizontally, 20px gap
		cardPanel.setBounds(20, 82, 910, 150);
		cardPanel.setBackground(new Color(244, 243, 240)); // Background color của cardPanel

		// Sử dụng DashboardDao để lấy dữ liệu
		var dao = new BookCardDao();

		// Card 1: Stock Total
		var totalBooksBorrowed = dao.getTotalBooksBorrowed();
		var percentageBooksBorrowed = dao.getPercentageBooksBorrowed();
		var card1 = new CustomCard();
		card1.setColors(new Color(100, 149, 237), new Color(70, 130, 180)); // Gradient màu xanh
		card1.setData("Books currently being rented", totalBooksBorrowed + " Books",
				"Currently Borrowed: " + String.format("%.2f%%", percentageBooksBorrowed),
				new ImageIcon(getClass().getResource("/icon12/b1.png")));

		// Card 2: Books Borrowed Today
		var totalBooksBorrowedToday = dao.getTotalBooksBorrowedToday();
		var percentageBooksBorrowedToday = dao.getPercentageBooksBorrowedToday();
		var card2 = new CustomCard();
		card2.setColors(new Color(186, 85, 211), new Color(148, 0, 211)); // Gradient màu tím
		card2.setData("Books Borrowed Today", totalBooksBorrowedToday + " Books",
				String.format("%.2f%% Compared to Total Borrowed", percentageBooksBorrowedToday),
				new ImageIcon(getClass().getResource("/icon12/b2.png")));

		// Card 3: Books Returned Today
		var totalBooksReturnedToday = dao.getTotalBooksReturnedToday();
		var percentageBooksReturnedToday = dao.getPercentageBooksReturnedToday();
		var card3 = new CustomCard();
		card3.setColors(new Color(255, 204, 102), new Color(255, 140, 0)); // Gradient màu vàng
		card3.setData("Books Returned Today", totalBooksReturnedToday + " Books",
				String.format("%.2f%% Compared to Borrowed", percentageBooksReturnedToday),
				new ImageIcon(getClass().getResource("/icon12/b3.png")));

		// Add các card vào panel
		cardPanel.add(card1);
		cardPanel.add(card2);
		cardPanel.add(card3);

		add(cardPanel);
	}
	public void reloadTableWithBooks(List<BookManagementEntity> books) {
	    tableModel.setRowCount(0); // Xóa toàn bộ dữ liệu hiện tại trong bảng

	    for (BookManagementEntity book : books) {
	        ImageIcon bookImageIcon = null;
	        if (book.getImage() != null && !book.getImage().isEmpty()) {
	            var resource = "src/main/resources/images/" + book.getImage();
	            bookImageIcon = new ImageIcon(
	                    new ImageIcon(resource).getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH));
	        }

	        if (bookImageIcon == null) {
	            var defaultResource = getClass().getClassLoader().getResource("images/default_book.png");
	            if (defaultResource != null) {
	                bookImageIcon = new ImageIcon(
	                        new ImageIcon(defaultResource).getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH));
	            }
	        }

	        tableModel.addRow(new Object[] {
	            book.getBookID(),
	            bookImageIcon,
	            book.getTitle(),
	            book.getAuthorID(),
	            book.getPublisherID(),
	            book.getIsbn(),
	            book.getCategory(),
	            book.getQuantity(),
	            book.getStockQuantity(),
	            book.getPrice(),
	            book.getRentalPrice(),
	            book.getLanguage()
	        });
	    }

	    tableBookManagement.revalidate();
	    tableBookManagement.repaint();
	}


	private void customizeTable(JTable table) {
		table.setFont(new Font("Arial", Font.PLAIN, 14));
		table.setRowHeight(80); // Đặt chiều cao hàng để phù hợp với hình ảnh
		table.setGridColor(new Color(224, 224, 224));

		// Center align cells
		var centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);

		for (var i = 0; i < table.getColumnCount(); i++) {
			// Căn giữa các cột, ngoại trừ cột Avatar
			if (i != 1 && i != 6 && i != 7) { // Không căn giữa cột Avatar và các cột tiền
				table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
			}
		}

		// Render cột Avatar
		table.getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				if (value instanceof ImageIcon) {
					return new JLabel((ImageIcon) value); // Hiển thị ImageIcon trong JLabel
				}
				return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			}
		});

		// Render cột Price và RentalPrice với định dạng tiền tệ
		table.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				var label = (DefaultTableCellRenderer) super.getTableCellRendererComponent(table, value, isSelected,
						hasFocus, row, column);
				if (value instanceof Number) {
					label.setText(String.format("%,d VND", ((Number) value).longValue())); // Định dạng tiền VND
				}
				label.setHorizontalAlignment(CENTER);
				return label;
			}
		});

		table.getColumnModel().getColumn(7).setCellRenderer(new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				var label = (DefaultTableCellRenderer) super.getTableCellRendererComponent(table, value, isSelected,
						hasFocus, row, column);
				if (value instanceof Number) {
					label.setText(String.format("%,d VND", ((Number) value).longValue())); // Định dạng tiền VND
				}
				label.setHorizontalAlignment(CENTER);
				return label;
			}
		});

		// Customize header
		var header = table.getTableHeader();
		header.setPreferredSize(new Dimension(header.getPreferredSize().width, 40));
		header.setDefaultRenderer(new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				var headerRenderer = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
						column);
				headerRenderer.setBackground(Color.WHITE);

				headerRenderer.setForeground(Color.BLACK);
				headerRenderer.setFont(new Font("Arial", Font.BOLD, 16));
				((DefaultTableCellRenderer) headerRenderer).setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
				return headerRenderer;
			}

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);

				// Vẽ đường gạch dưới
				var g2 = (Graphics2D) g;
				g2.setColor(new Color(211, 211, 211)); // Màu của viền gạch dưới
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

				// Vẽ đường viền dưới
				var y = getHeight() - 1; // Vị trí y của đường viền (dưới cùng của header)
				g2.drawLine(0, y, getWidth(), y); // Vẽ đường ngang
			}
		});

	}


	private void loadBookManagementData() {
		// Get BookManagement data from BookManagementDao
		var BookManagementDao = new BookManagementDao();
		BookManagements = BookManagementDao.select();
		filteredBookManagements = BookManagements; // Initially, all BookManagements are displayed
	}

	private void filterBookManagements() {
		var searchText = searchField.getText().trim().toLowerCase();

		// Nếu ô tìm kiếm trống hoặc chứa văn bản mặc định
		if (searchText.equals("search by BookManagement...") || searchText.isEmpty()) {
			filteredBookManagements = BookManagements;
		} else {
			// Lọc theo cả BookManagementCode và FullName
			filteredBookManagements = BookManagements.stream()
					.filter(BookManagement -> BookManagement.getIsbn().toLowerCase().contains(searchText)
							|| BookManagement.getTitle().toLowerCase().contains(searchText))
					.toList();
		}

		// Đặt lại trang về trang đầu tiên
		currentPage = 1;
		pageInput.setText(String.valueOf(currentPage));
		updateTable();
	}

	private void updateTable() {
		tableModel.setRowCount(0); // Xóa dữ liệu cũ trong bảng

		var start = (currentPage - 1) * rowsPerPage; // Tính chỉ số bắt đầu
		var end = Math.min(start + rowsPerPage, filteredBookManagements.size()); // Tính chỉ số kết thúc

		for (var i = start; i < end; i++) {
			var BookManagement = filteredBookManagements.get(i);

			// Tải hình ảnh avatar
			ImageIcon avatarIcon = null;
			if (BookManagement.getImage() != null && !BookManagement.getImage().isEmpty()) {
				var resource = "src/main/resources/images/" + BookManagement.getImage();
				if (resource != null) {
					avatarIcon = new ImageIcon(
							new ImageIcon(resource).getImage().getScaledInstance(50, 70, Image.SCALE_SMOOTH));
				}
			}

			if (avatarIcon == null) {
				// Sử dụng ảnh mặc định nếu không tìm thấy avatar
				var defaultResource = getClass().getClassLoader().getResource("images/default_avatar.png");
				if (defaultResource != null) {
					avatarIcon = new ImageIcon(
							new ImageIcon(defaultResource).getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH));
				}
			}

			// Thêm dòng vào bảng
			tableModel.addRow(new Object[] { BookManagement.getBookID(), avatarIcon, BookManagement.getIsbn(),
					BookManagement.getTitle(), BookManagement.getQuantity(), BookManagement.getStockQuantity(),
					BookManagement.getPrice(), BookManagement.getRentalPrice(), BookManagement.isRental() });
		}

		// Hiển thị thông tin số trang và số dòng
		pageInfo.setText(
				"Page " + currentPage + " / " + ((filteredBookManagements.size() + rowsPerPage - 1) / rowsPerPage)
						+ " | Total Rows: " + filteredBookManagements.size());
	}

	private void goToPreviousPage() {
		if (currentPage > 1) {
			currentPage--;
			pageInput.setText(String.valueOf(currentPage));
			updateTable();
		}
	}

	private void goToNextPage() {
		if (currentPage * rowsPerPage < filteredBookManagements.size()) {
			currentPage++;
			pageInput.setText(String.valueOf(currentPage));
			updateTable();
		}
	}

	private void goToPage() {
		try {
			var page = Integer.parseInt(pageInput.getText());
			var totalPages = (filteredBookManagements.size() + rowsPerPage - 1) / rowsPerPage;

			if (page >= 1 && page <= totalPages) {
				currentPage = page;
				updateTable();
			} else {
				pageInput.setText(String.valueOf(currentPage));
			}
		} catch (NumberFormatException e) {
			pageInput.setText(String.valueOf(currentPage));
		}
	}

	@Override
	protected void paintComponent(Graphics grphcs) {
		super.paintComponent(grphcs);
		var g2 = (Graphics2D) grphcs;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2.setColor(Color.WHITE);
		g2.fillRoundRect(20, 20, 910, 44, 15, 15);

		g2.setColor(new Color(240, 240, 240));
		g2.drawRoundRect(20, 20, 910, 44, 15, 15);
	}

	private void showPopupMenu(MouseEvent e) {
	    var popup = new JPopupMenu();

	    // Menu item để xem chi tiết
	    var viewDetailsItem = new JMenuItem("View");
	    viewDetailsItem.addActionListener(event -> {
	        var selectedRow = tableBookManagement.getSelectedRow();
	        if (selectedRow != -1) {
	            var bookID = (int) tableModel.getValueAt(selectedRow, 0); // Lấy BookID từ cột ẩn
	            var selectedBook = BookManagements.stream()
	                    .filter(book -> book.getBookID() == bookID).findFirst();

	            selectedBook.ifPresent(book -> {
	                // Đóng bảng hiện tại nếu có
	                if (currentBookManagementViewDialog != null && currentBookManagementViewDialog.isShowing()) {
	                    currentBookManagementViewDialog.dispose();
	                }

	                // Hiển thị bảng mới và lưu tham chiếu
	                currentBookManagementViewDialog = new BookManagementViewDialog(book);
	                currentBookManagementViewDialog.setVisible(true);
	            });
	        } else {
	            JOptionPane.showMessageDialog(null, "Please select a row first!", "Warning",
	                    JOptionPane.WARNING_MESSAGE);
	        }
	    });

	    // Menu item để chỉnh sửa
	    var editItem = new JMenuItem("Edit");
	    editItem.addActionListener(event -> {
	        var selectedRow = tableBookManagement.getSelectedRow();
	        if (selectedRow != -1) {
	            var bookID = (int) tableModel.getValueAt(selectedRow, 0); // Lấy BookID từ cột ẩn
	            var selectedBook = BookManagements.stream()
	                    .filter(book -> book.getBookID() == bookID).findFirst();

	            selectedBook.ifPresent(book -> {
	                // Đóng dialog chỉnh sửa hiện tại nếu có
	                if (currentBookManagementEditDialog != null && currentBookManagementEditDialog.isShowing()) {
	                    currentBookManagementEditDialog.dispose();
	                }

	                // Hiển thị dialog chỉnh sửa mới
					currentBookManagementEditDialog = new BookManagementEdit(this, book);
	                currentBookManagementEditDialog.setLocationRelativeTo(this); // Hiển thị ở giữa màn hình
	                currentBookManagementEditDialog.setVisible(true);
	            });
	        } else {
	            JOptionPane.showMessageDialog(null, "Please select a row to edit!", "Warning",
	                    JOptionPane.WARNING_MESSAGE);
	        }
	    });

	    // Menu item để xóa
	    var deleteItem = new JMenuItem("Delete");
	    deleteItem.setEnabled(!isRental);
	    deleteItem.addActionListener(this::deleteRow);

	    // Thêm các item vào menu chuột phải
	    popup.add(viewDetailsItem);
	    popup.add(editItem); // Thêm mục Edit
	    popup.add(deleteItem);

	    // Hiển thị popup tại vị trí chuột
	    popup.show(tableBookManagement, e.getX(), e.getY());
	}


	private void deleteRow(ActionEvent e) {
		var selectedRow = tableBookManagement.getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(null, "Please select a row to delete!", "Warning",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		var result = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this row?", "Delete",
				JOptionPane.YES_NO_OPTION);
		if (result == JOptionPane.YES_OPTION) {
			try {
				var dao = new BookManagementDao();
				var bookID = (int) tableModel.getValueAt(selectedRow, 0); // Lấy BookID từ cột ẩn
				var updatedBy = 1; // Giá trị người dùng thực hiện thao tác (ví dụ: userID = 1)

				dao.delete(bookID, updatedBy); // Gọi phương thức delete với 2 tham số

				// Đồng bộ hóa danh sách
				var bookToDelete = filteredBookManagements.stream().filter(book -> book.getBookID() == bookID)
						.findFirst().orElse(null);
				if (bookToDelete != null) {
					BookManagements.remove(bookToDelete);
					filteredBookManagements.remove(bookToDelete);
				}

				// Cập nhật lại bảng
				updateTable();
				JOptionPane.showMessageDialog(null, "Delete successful!");
			} catch (Exception ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(null, "Delete failed: " + ex.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}


	protected void btnDeleteActionPerformed(ActionEvent e) {
		if (currRow >= 0 && currRow < tableBookManagement.getRowCount()) {
			tableBookManagement.setRowSelectionInterval(currRow, currRow); // Chọn hàng được nhấn
			deleteRow(e);
		} else {
			JOptionPane.showMessageDialog(null, "Please, choose row frist", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	protected void btnViewActionPerformed(ActionEvent e) {
		if (currRow >= 0 && currRow < tableBookManagement.getRowCount()) {
			var BookManagementID = (int) tableModel.getValueAt(currRow, 0); // Lấy BookManagementID từ cột ẩn
			var selectedBookManagement = BookManagements.stream()
					.filter(BookManagement -> BookManagement.getBookID() == BookManagementID).findFirst();

			selectedBookManagement.ifPresent(BookManagement -> {
				// Đóng bảng hiện tại nếu có
				if (currentBookManagementViewDialog != null && currentBookManagementViewDialog.isShowing()) {
					currentBookManagementViewDialog.dispose();
				}

				// Hiển thị bảng mới và lưu tham chiếu
				currentBookManagementViewDialog = new BookManagementViewDialog(BookManagement);
				currentBookManagementViewDialog.setVisible(true);
			});
		} else {
			JOptionPane.showMessageDialog(null, "Please select a BookManagement first!", "Warning",
					JOptionPane.WARNING_MESSAGE);
		}
	}

	protected void btnEditActionPerformed(ActionEvent e) {
		if (currRow >= 0 && currRow < tableBookManagement.getRowCount()) {
			var BookManagementID = (int) tableModel.getValueAt(currRow, 0); // Lấy BookManagementID từ cột ẩn
			var selectedBookManagement = BookManagements.stream()
					.filter(BookManagement -> BookManagement.getBookID() == BookManagementID).findFirst();

			selectedBookManagement.ifPresent(BookManagement -> {
				// Đóng dialog chỉnh sửa hiện tại nếu có
				if (currentBookManagementEditDialog != null && currentBookManagementEditDialog.isShowing()) {
					currentBookManagementEditDialog.dispose();
				}

				// Hiển thị dialog chỉnh sửa mới
				currentBookManagementEditDialog = new BookManagementEdit(this, BookManagement);
				currentBookManagementEditDialog.setLocationRelativeTo(this); // Hiển thị ở giữa màn hình
				currentBookManagementEditDialog.setVisible(true);
			});
		} else {
			JOptionPane.showMessageDialog(null, "Please select a row to edit!", "Warning", JOptionPane.WARNING_MESSAGE);
		}
	}


	public void reloadTable() {

		// Tải lại dữ liệu sinh viên từ database
		loadBookManagementData();
		// Cập nhật lại bảng
		updateTable();

	}

	protected void btnAddActionPerformed(ActionEvent e) {
	    if (currentAddBookManagementDialog != null && currentAddBookManagementDialog.isShowing()) {
	        currentAddBookManagementDialog.toFront();
	        return;
	    }

	    currentAddBookManagementDialog = new AddBookManagement(this, chart);
	    currentAddBookManagementDialog.setLocationRelativeTo(this);
	    currentAddBookManagementDialog.setVisible(true);
	}


	protected void btnExcelActionPerformed(ActionEvent e) {
		try {
			// Lấy toàn bộ danh sách sinh viên từ cơ sở dữ liệu
			var dao = new BookManagementDao();
			var allBookManagements = dao.select();

			if (allBookManagements.isEmpty()) {
				JOptionPane.showMessageDialog(this, "No data to export!", "Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}

			// Tạo file Excel
			Workbook workbook = new XSSFWorkbook();
			var sheet = workbook.createSheet("BookManagements");

			// Tạo hàng tiêu đề
			var headerRow = sheet.createRow(0);
			String[] headers = { "BookManagementID", "BookManagementCode", "FullName", "DateOfBirth", "Gender", "Email",
					"PhoneNumber", "Address", "EnrollmentYear", "SchoolName", "Books Rented", "Late Returns",
					"Damaged Books", "Orders" };

			for (var i = 0; i < headers.length; i++) {
				var cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);
			}

			// Thêm dữ liệu từ danh sách sách
			var rowNum = 1;
			for (BookManagementEntity book : allBookManagements) {
				var row = sheet.createRow(rowNum++);
				row.createCell(0).setCellValue(book.getBookID()); // ID sách
				row.createCell(1).setCellValue(book.getIsbn() != null ? book.getIsbn() : ""); // ISBN
				row.createCell(2).setCellValue(book.getTitle() != null ? book.getTitle() : ""); // Tiêu đề sách
				row.createCell(3).setCellValue(book.getAuthorID()); // ID tác giả
				row.createCell(4).setCellValue(book.getPublisherID()); // ID nhà xuất bản
				row.createCell(5).setCellValue(book.getCategory() != null ? book.getCategory() : ""); // Thể loại
				row.createCell(6).setCellValue(book.getQuantity()); // Số lượng hiện có
				row.createCell(7).setCellValue(book.getStockQuantity()); // Số lượng tồn kho
				row.createCell(8).setCellValue(book.getPrice() != null ? book.getPrice().toString() : ""); // Giá tiền
				row.createCell(9).setCellValue(book.getRentalPrice() != null ? book.getRentalPrice().toString() : ""); // Giá
																														// thuê
				row.createCell(10).setCellValue(book.getLanguage() != null ? book.getLanguage() : ""); // Ngôn ngữ
				row.createCell(11).setCellValue(book.getImage() != null ? book.getImage() : ""); // Đường dẫn ảnh bìa
				row.createCell(12).setCellValue(book.isDeleted() ? "Yes" : "No"); // Trạng thái xóa
				row.createCell(13).setCellValue(book.getCreatedAt() != null ? book.getCreatedAt().toString() : ""); // Ngày
																													// tạo
				row.createCell(14).setCellValue(book.getCreatedBy()); // Người tạo
				row.createCell(15).setCellValue(book.getUpdatedAt() != null ? book.getUpdatedAt().toString() : ""); // Ngày
																													// cập
																													// nhật
				row.createCell(16).setCellValue(book.getUpdatedBy()); // Người cập nhật
			}

			// Lưu file Excel
			var filePath = System.getProperty("user.home") + "/Documents/BookManagements.xlsx";
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
