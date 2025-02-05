package main;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import dao.BookManagementDao;
import entity.BookManagementEntity;

public class WareHouse extends JPanel {

	private static final long serialVersionUID = 1L;
	private JScrollPane scrollPaneBook, scrollPaneOut, scrollPaneAdd;
	private JTabbedPane tabbedPane;
	private JTextField searchBook, searchOut, searchAdd;
	private JTable tableBook;
	private Map<String, Boolean> selectedStates = new HashMap<>();
	private Set<String> removedBooks = new HashSet<>();
	private JTable tableAdd;

//	private DefaultTableModel addTableModel;
//	private DefaultTableModel bookTableModel;
	private JTable tableOut;
	private JButton btnStockIn_1;
	private JButton btnStockIn_2;

	public WareHouse() {
		setBackground(new Color(244, 243, 240));
		setBounds(0, 0, 946, 706);
		setLayout(null);

		tabbedPane = new JTabbedPane();
		tabbedPane.setBounds(20, 11, 906, 679);
		// **Khởi tạo tableAdd và tableBook**
		tableAdd = new JTable(
				new DefaultTableModel(new Object[] { "Select", "ISBN", "Title", "Quantity", "Stock Quantity" }, 0));
		tableBook = new JTable(new DefaultTableModel(new Object[] { "Select", "ISBN", "Title", "Quantity" }, 0));
		tableBook.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		// Lấy model của bảng tableAdd và tableBook
//		addTableModel = (DefaultTableModel) tableAdd.getModel();
//		bookTableModel = (DefaultTableModel) tableBook.getModel();
		// Panel Book
		var panelBook = new JPanel();
		panelBook.setBackground(new Color(255, 255, 255));
		panelBook.setBorder(null);
		panelBook.setLayout(null);

		scrollPaneBook = new JScrollPane(tableBook);
		scrollPaneBook.setBorder(BorderFactory.createEmptyBorder());
		scrollPaneBook.setBounds(0, 67, 901, 578);
		panelBook.add(scrollPaneBook);

		// Thanh tìm kiếm cho Book
		searchBook = createSearchField1("Search by Book Name or ISBN...");
		searchBook.setBounds(240, 11, 343, 45);
		searchBook.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				filterBookData(searchBook.getText());
			}
		});
		panelBook.add(searchBook);

		tabbedPane.addTab("Book", new ImageIcon(getClass().getResource("/hinh/8.png")), panelBook, "Book Management");

		btnStockIn_1 = new JButton("");
		btnStockIn_1.setIcon(new ImageIcon(WareHouse.class.getResource("/icon12/add-stock.png")));
		btnStockIn_1.addActionListener(this::btnStockIn_1ActionPerformed);
		btnStockIn_1.setBounds(10, 11, 62, 45);
		panelBook.add(btnStockIn_1);

		// Panel Out
		var panelOut = new JPanel();
		panelOut.setBackground(new Color(255, 255, 255));
		panelOut.setBorder(null);
		panelOut.setLayout(null);

		// Khởi tạo tableOut trước
		tableOut = new JTable();
		tableOut.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// Gán tableOut vào scrollPaneOut
		scrollPaneOut = new JScrollPane(tableOut);
		scrollPaneOut.setBorder(BorderFactory.createEmptyBorder());
		scrollPaneOut.setBounds(0, 67, 901, 578);

		// Thêm scrollPaneOut vào panelOut
		panelOut.add(scrollPaneOut);


		// Thanh tìm kiếm cho Out
		searchOut = createSearchField("Search by Out Name...");
		searchOut.setBounds(240, 11, 343, 45);
		searchOut.addKeyListener(new KeyAdapter() {
		    @Override
		    public void keyReleased(KeyEvent e) {
		        filterOutTableData(searchOut.getText()); // Gọi phương thức lọc dữ liệu
		    }
		});

		panelOut.add(searchOut);

		tabbedPane.addTab("Out", new ImageIcon(getClass().getResource("/hinh/5.png")), panelOut, "Out Management");
		tabbedPane.addChangeListener(e -> {
			var selectedIndex = tabbedPane.getSelectedIndex();

			if (selectedIndex == 0) {
				tableBook.updateUI();
				reloadTableBookata();
			}
			if (selectedIndex == 1) {
				tableOut.updateUI();
				reloadTableOutData();
			} else {
				tableAdd.updateUI();
				reloadTableAddData();
			}

		});
		// Panel Add
		var panelAdd = new JPanel();
		panelAdd.setBackground(new Color(255, 255, 255));
		panelAdd.setBorder(null);
		panelAdd.setLayout(null);

		// Ô nhập số lượng
		var quantityField = new JTextField();
		quantityField.setBounds(682, 11, 80, 45); // Đặt vị trí và kích thước
		quantityField.setFont(new Font("Arial", Font.PLAIN, 14));
		quantityField.setHorizontalAlignment(JTextField.CENTER); // Căn giữa
		panelAdd.add(quantityField);

		// Nút Update
		var btnUpdateQuantity = new JButton("");
		btnUpdateQuantity.setIcon(new ImageIcon(WareHouse.class.getResource("/icon12/add-kho.png")));
		btnUpdateQuantity.setBounds(788, 12, 62, 45); // Đặt vị trí và kích thước
		btnUpdateQuantity.setFont(new Font("Arial", Font.PLAIN, 12));
		btnUpdateQuantity.setToolTipText("Update Quantity");
		panelAdd.add(btnUpdateQuantity);

		// Ô nhập số lượng
		var quantityFieldOut = new JTextField();
		quantityFieldOut.setVisible(false);
		quantityFieldOut.setBounds(682, 11, 80, 45); // Vị trí và kích thước
		quantityFieldOut.setFont(new Font("Arial", Font.PLAIN, 14));
		quantityFieldOut.setHorizontalAlignment(JTextField.CENTER); // Căn giữa
		panelOut.add(quantityFieldOut);

		// Nút "Update"
		var btnUpdateQuantityOut = new JButton("Update");
		btnUpdateQuantityOut.setVisible(false);
		btnUpdateQuantityOut.setBounds(788, 11, 80, 45); // Vị trí và kích thước
		btnUpdateQuantityOut.setFont(new Font("Arial", Font.PLAIN, 12));
		btnUpdateQuantityOut.setToolTipText("Update Quantity");
		panelOut.add(btnUpdateQuantityOut);

		// Xử lý sự kiện khi nhấn nút "Update"
		btnUpdateQuantityOut.addActionListener(e -> {
			try {
				var newQuantity = Integer.parseInt(quantityFieldOut.getText().trim()); // Lấy số lượng từ ô nhập
				if (newQuantity < 0) {
					JOptionPane.showMessageDialog(panelOut, "Quantity cannot be negative!", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				// Cập nhật số lượng cho các hàng được chọn
				for (var i = 0; i < tableOut.getRowCount(); i++) {
					var isChecked = (boolean) tableOut.getValueAt(i, 0); // Cột "Select"
					if (isChecked) {
						tableOut.setValueAt(newQuantity, i, 3); // Cập nhật cột "Quantity"
					}
				}

				JOptionPane.showMessageDialog(panelOut, "Quantity updated successfully!", "Success",
						JOptionPane.INFORMATION_MESSAGE);
				quantityFieldOut.setText(""); // Xóa nội dung trong ô nhập
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(panelOut, "Invalid quantity! Please enter a valid number.", "Error",
						JOptionPane.ERROR_MESSAGE);


			}
		});
		btnStockIn_2 = new JButton("");
		btnStockIn_2.setIcon(new ImageIcon(WareHouse.class.getResource("/icon12/add-stock.png")));
		btnStockIn_2.addActionListener(this::btnStockIn_2ActionPerformed);
		btnStockIn_2.setBounds(10, 11, 62, 45);
		panelOut.add(btnStockIn_2);
		// Xử lý sự kiện khi nhấn nút Update
		btnUpdateQuantity.addActionListener(e -> {
			try {
				// Lấy số lượng mới từ ô nhập
				var newQuantity = Integer.parseInt(quantityField.getText().trim());
				if (newQuantity < 0) {
					JOptionPane.showMessageDialog(panelAdd, "Quantity cannot be negative!", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				// Tạo danh sách tạm thời để lưu các hàng được chọn
				java.util.List<Integer> selectedRows = new java.util.ArrayList<>();

				// Duyệt qua tất cả các hàng trong bảng `tableAdd`
				for (var i = 0; i < tableAdd.getRowCount(); i++) {
					var isChecked = (boolean) tableAdd.getValueAt(i, 0); // Cột "Select" (checkbox)
					if (isChecked) {
						selectedRows.add(i); // Thêm chỉ số hàng vào danh sách tạm
					}
				}

				// Duyệt qua danh sách các hàng được chọn và cập nhật số lượng
				for (var i = selectedRows.size() - 1; i >= 0; i--) {
					var updateQuantity = 0;
					var updateStock = 0;
					int rowIndex = selectedRows.get(i);
					var quality = Integer.parseInt(tableAdd.getValueAt(rowIndex, 3).toString()); // Lấy quality
					var stockQuality = Integer.parseInt(tableAdd.getValueAt(rowIndex, 4).toString()); // Lấy quality

					var isbn = tableAdd.getValueAt(rowIndex, 1).toString(); // Lấy ISBN
					updateQuantity = quality + newQuantity;
					updateStock = stockQuality + newQuantity;

//					tableAdd.setValueAt(updateQuantity, rowIndex, 3); // Cập nhật số lượng mới vào cột "Quantity"

					// Cập nhật số lượng vào cơ sở dữ liệu
					updateBookQuantityInDatabase(isbn, updateQuantity, updateStock);
				}
				reloadTableAddData();

				// Hiển thị thông báo thành công
				JOptionPane.showMessageDialog(panelAdd, "Quantity updated successfully for selected rows!", "Success",
						JOptionPane.INFORMATION_MESSAGE);

				// Xóa nội dung ô nhập `quantityField`
				quantityField.setText("");

			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(panelAdd, "Invalid quantity! Please enter a valid number.", "Error",
						JOptionPane.ERROR_MESSAGE);
			} catch (Exception ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(panelAdd, "An error occurred while updating quantity!", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		});


		scrollPaneAdd = new JScrollPane();
		scrollPaneAdd.setBorder(BorderFactory.createEmptyBorder());
		scrollPaneAdd.setBounds(0, 67, 901, 578);
		panelAdd.add(scrollPaneAdd);

		tableAdd = new JTable();
		tableAdd.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPaneAdd.setViewportView(tableAdd);

		// Khởi tạo DefaultTableModel cho tableAdd
		var addTableModel = new DefaultTableModel(
				new Object[] { "Select", "ISBN", "Title", "Quantity", "Stock Quantity" }, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public Class<?> getColumnClass(int columnIndex) {
				// Cột "Select" là Boolean, cột "Quantity" là Integer, các cột còn lại là String
				return columnIndex == 0 ? Boolean.class : (columnIndex == 3 ? Integer.class : String.class);
			}

			@Override
			public boolean isCellEditable(int row, int column) {
				// Chỉ cho phép chỉnh sửa cột "Select" và "Quantity"
				return column == 0;
			}
		};

		// Gán model cho tableAdd
		tableAdd.setModel(addTableModel);
	    tableAdd.getColumnModel().getColumn(4).setMinWidth(0);
	    tableAdd.getColumnModel().getColumn(4).setMaxWidth(0);
	    tableAdd.getColumnModel().getColumn(4).setPreferredWidth(0);
//		tableAdd.getModel().addTableModelListener(e -> {
//			if (e.getType() == javax.swing.event.TableModelEvent.UPDATE) {
//				var row = e.getFirstRow();
//				var column = e.getColumn();
//
//				if (column == 3) { // Chỉ xử lý khi cột "Quantity" được chỉnh sửa
//					var isbn = addTableModel.getValueAt(row, 1).toString(); // Lấy ISBN
//					var title = addTableModel.getValueAt(row, 2).toString(); // Lấy Title
//					var quantity = (int) addTableModel.getValueAt(row, 3); // Lấy số lượng mới
//					var stockQuantity = (int) addTableModel.getValueAt(row, 4); // Lấy số lượng mới
//
//					// Cập nhật vào database
//					updateBookQuantityInDatabase(isbn, quantity, stockQuantity);
//
//					// Thêm hàng đã chỉnh sửa lại vào tableBook
//					bookTableModel.addRow(new Object[] { false, isbn, title, quantity });
//
//					// Xóa hàng khỏi tableAdd
//					addTableModel.removeRow(row);
//
//					// Dọn trạng thái checkbox trong danh sách
//					removedBooks.remove(isbn);
//				}
//			}
//		});

		tableAdd.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent e) {
				var row = tableAdd.rowAtPoint(e.getPoint());
				var column = tableAdd.columnAtPoint(e.getPoint());

				// Nếu người dùng nhấn vào hàng nhưng không nhấn vào checkbox, tự động đổi trạng
				// thái checkbox
				if (row != -1 && column != -1 && column != 0) {
					var isChecked = (boolean) tableAdd.getValueAt(row, 0);
					tableAdd.setValueAt(!isChecked, row, 0);
				}
			}
		});


		// Tùy chỉnh giao diện cho tableAdd
		customizeTableWithCheckbox(tableAdd);
		// Cài đặt kích thước cột
		tableAdd.getColumnModel().getColumn(0).setPreferredWidth(80); // Cột "Select"
		tableAdd.getColumnModel().getColumn(1).setPreferredWidth(300); // Cột "ISBN"
		tableAdd.getColumnModel().getColumn(2).setPreferredWidth(346); // Cột "Title"
		tableAdd.getColumnModel().getColumn(3).setPreferredWidth(150); // Cột "Quantity"

		tableAdd.getColumnModel().getColumn(4).setMinWidth(0);
		tableAdd.getColumnModel().getColumn(4).setMaxWidth(0);
		tableAdd.getColumnModel().getColumn(4).setPreferredWidth(0);

		// Thanh tìm kiếm cho Add
		searchAdd = createSearchField2("Search by Add Name...");
		searchAdd.setBounds(240, 11, 343, 45);

		// Thêm sự kiện tìm kiếm
		searchAdd.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				filterAddTableData(searchAdd.getText()); // Gọi phương thức lọc dữ liệu
			}
		});
		panelAdd.add(searchAdd);


		tabbedPane.addTab("Add", new ImageIcon(getClass().getResource("/hinh/6.png")), panelAdd, "Add Management");

		add(tabbedPane);

		// Load dữ liệu sách
		loadBookData();
	}

	private void updateBookQuantityInDatabase(String isbn, int newQuantity, int stockQuantity) {
		try {
			// Khởi tạo DAO và gọi phương thức cập nhật
			var dao = new BookManagementDao();
			var success = dao.updateBookQuantity(isbn, newQuantity, stockQuantity);

			if (!success) {
				JOptionPane.showMessageDialog(this, "Failed to update quantity in database for ISBN: " + isbn,
						"Database Error", JOptionPane.ERROR_MESSAGE);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, "An error occurred while updating the database: " + ex.getMessage(),
					"Database Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private JTextField createSearchField1(String placeholder) {
		var searchFieldBook = new JTextField(placeholder);
		searchFieldBook.setForeground(Color.GRAY);
		searchFieldBook.setFont(new Font("Arial", Font.PLAIN, 14));
		searchFieldBook.setBackground(Color.WHITE);
		searchFieldBook.setBorder(new RoundedBorder(15));

		searchFieldBook.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (searchFieldBook.getText().equals(placeholder)) {
					searchFieldBook.setText("");
					searchFieldBook.setForeground(Color.BLACK);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (searchFieldBook.getText().isEmpty()) {
					searchFieldBook.setText(placeholder);
					searchFieldBook.setForeground(Color.GRAY);
				}
			}
		});

		return searchFieldBook;
	}

	private JTextField createSearchField(String placeholder) {
		var searchFieldOut = new JTextField(placeholder);
		searchFieldOut.setForeground(Color.GRAY);
		searchFieldOut.setFont(new Font("Arial", Font.PLAIN, 14));
		searchFieldOut.setBackground(Color.WHITE);
		searchFieldOut.setBorder(new RoundedBorder(15));

		searchFieldOut.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (searchFieldOut.getText().equals(placeholder)) {
					searchFieldOut.setText("");
					searchFieldOut.setForeground(Color.BLACK);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (searchFieldOut.getText().isEmpty()) {
					searchFieldOut.setText(placeholder);
					searchFieldOut.setForeground(Color.GRAY);
				}
			}
		});

		return searchFieldOut;
	}
	private void filterOutTableData(String query) {
	    if (query == null || query.trim().isEmpty()) {
	        // Nếu query rỗng, load lại toàn bộ dữ liệu
	        reloadTableOutData();
	        return;
	    }

	    // Lấy model gốc của tableOut
	    var outTableModel = (DefaultTableModel) tableOut.getModel();
	    var filteredModel = new DefaultTableModel(new Object[] { "Select", "ISBN", "Title", "Quantity" }, 0) {
	        private static final long serialVersionUID = 1L;

	        @Override
	        public Class<?> getColumnClass(int columnIndex) {
	            return columnIndex == 0 ? Boolean.class : String.class; // Cột "Select" là Boolean
	        }

	        @Override
	        public boolean isCellEditable(int row, int column) {
	            return column == 0; // Chỉ cho phép chỉnh sửa cột "Select"
	        }
	    };

	    // Lọc dữ liệu từ model gốc theo query
	    for (var i = 0; i < outTableModel.getRowCount(); i++) {
	        var select = outTableModel.getValueAt(i, 0);
	        var isbn = outTableModel.getValueAt(i, 1).toString();
	        var title = outTableModel.getValueAt(i, 2).toString();
	        var quantity = outTableModel.getValueAt(i, 3).toString();

	        // Kiểm tra query với cột ISBN và Title
	        if (isbn.toLowerCase().contains(query.toLowerCase()) || title.toLowerCase().contains(query.toLowerCase())) {
	            filteredModel.addRow(new Object[] { select, isbn, title, quantity });
	        }
	    }

	    // Cập nhật model cho tableOut
	    tableOut.setModel(filteredModel);
	    customizeTableWithCheckbox(tableOut);
	}

	private void filterAddTableData(String query) {
	    if (query == null || query.trim().isEmpty()) {
	        // Nếu query rỗng, load lại toàn bộ dữ liệu
	        reloadTableAddData();
	        return;
	    }

	    var addTableModel = (DefaultTableModel) tableAdd.getModel();
	    var filteredModel = new DefaultTableModel(
	            new Object[] { "Select", "ISBN", "Title", "Quantity", "Stock Quantity" }, 0) {
	        private static final long serialVersionUID = 1L;

	        @Override
	        public Class<?> getColumnClass(int columnIndex) {
	            return columnIndex == 0 ? Boolean.class : (columnIndex == 3 ? Integer.class : String.class);
	        }

	        @Override
	        public boolean isCellEditable(int row, int column) {
	            return column == 0; // Chỉ cho phép chỉnh sửa cột "Select"
	        }
	    };

	    // Lọc dữ liệu theo query
	    for (var i = 0; i < addTableModel.getRowCount(); i++) {
	        var select = addTableModel.getValueAt(i, 0);
	        var isbn = addTableModel.getValueAt(i, 1).toString();
	        var title = addTableModel.getValueAt(i, 2).toString();
	        var quantity = addTableModel.getValueAt(i, 3).toString();
	        var stockQuantity = addTableModel.getValueAt(i, 4).toString();

	        // Kiểm tra query với ISBN hoặc Title
	        if (isbn.toLowerCase().contains(query.toLowerCase()) || title.toLowerCase().contains(query.toLowerCase())) {
	            filteredModel.addRow(new Object[] { select, isbn, title, quantity, stockQuantity });
	        }
	    }

	    // Cập nhật model cho tableAdd
	    tableAdd.setModel(filteredModel);

	    // Giữ nguyên cấu hình ẩn cột "Stock Quantity"
	    tableAdd.getColumnModel().getColumn(4).setMinWidth(0);
	    tableAdd.getColumnModel().getColumn(4).setMaxWidth(0);
	    tableAdd.getColumnModel().getColumn(4).setPreferredWidth(0);

	    // Áp dụng lại tùy chỉnh bảng
	    customizeTableWithCheckbox(tableAdd);
	}


	private JTextField createSearchField2(String placeholder) {
		var searchFieldAdd = new JTextField(placeholder); // Placeholder mặc định
		searchFieldAdd.setForeground(Color.GRAY); // Màu chữ mặc định
		searchFieldAdd.setFont(new Font("Arial", Font.PLAIN, 14)); // Font chữ
		searchFieldAdd.setBackground(Color.WHITE); // Màu nền
		searchFieldAdd.setBorder(new RoundedBorder(15)); // Viền tròn cho ô nhập

		// Xử lý sự kiện focus (khi con trỏ chuột được đặt vào hoặc rời khỏi ô nhập)
		searchFieldAdd.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				// Khi ô nhập được focus và có placeholder, xóa placeholder
				if (searchFieldAdd.getText().equals(placeholder)) {
					searchFieldAdd.setText(""); // Xóa placeholder
					searchFieldAdd.setForeground(Color.BLACK); // Đổi màu chữ sang đen
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				// Khi ô nhập mất focus và trống, hiển thị lại placeholder
				if (searchFieldAdd.getText().isEmpty()) {
					searchFieldAdd.setText(placeholder); // Đặt lại placeholder
					searchFieldAdd.setForeground(Color.GRAY); // Đổi màu chữ về xám
				}
			}
		});

		return searchFieldAdd; // Trả về ô tìm kiếm
	}


	private void loadBookData() {
		var dao = new BookManagementDao();
		var books = dao.select();

		// Kiểm tra nếu không có dữ liệu từ DAO
		if (books == null || books.isEmpty()) {
			System.out.println("Không có dữ liệu từ cơ sở dữ liệu.");
			return;
		}

		// Lưu trạng thái checkbox nếu bảng đã có dữ liệu
		var selectedStates = new HashMap<String, Boolean>();
		for (var i = 0; i < tableBook.getRowCount(); i++) {
			var isbn = tableBook.getValueAt(i, 1).toString(); // Lấy ISBN từ cột thứ 2
			var isChecked = (boolean) tableBook.getValueAt(i, 0); // Lấy trạng thái checkbox từ cột đầu tiên
			selectedStates.put(isbn, isChecked);
		}

		// Tạo DefaultTableModel cho tableBook
		var bookTableModel = new DefaultTableModel(new Object[] { "Select", "ISBN", "Title", "Quantity" }, 0) {
			private static final long serialVersionUID = 1L;

			@Override
		    public Class<?> getColumnClass(int columnIndex) {
		        // Cột "Select" là Boolean, cột "Quantity" là Integer
		        return switch (columnIndex) {
		            case 0 -> Boolean.class;
		            case 3 -> Integer.class; // Đảm bảo "Quantity" là số
		            default -> String.class;
		        };
		    }

		    @Override
		    public boolean isCellEditable(int row, int column) {
		        return column == 0; // Chỉ cho phép chỉnh sửa cột "Select"
		    }
		};
		
		// Gán model cho tableBook
		tableBook.setModel(bookTableModel);

		// Thêm RowSorter để sắp xếp
		tableBook.setAutoCreateRowSorter(true);

		// Lắng nghe sự kiện nhấp chuột vào cột
		tableBook.getTableHeader().addMouseListener(new java.awt.event.MouseAdapter() {
		    @Override
		    public void mouseClicked(java.awt.event.MouseEvent e) {
		        int column = tableBook.columnAtPoint(e.getPoint()); // Lấy cột được nhấp
		        if (column == 3) { // Cột "Quantity"
		            // Sắp xếp theo cột "Quantity"
		            tableBook.getRowSorter().toggleSortOrder(column);
		        }
		    }
		});
		// Tạo DefaultTableModel cho tableOut
		var outTableModel = new DefaultTableModel(new Object[] { "Select", "ISBN", "Title", "Quantity" }, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public Class<?> getColumnClass(int columnIndex) {
				// Cột "Select" là Boolean để hiển thị checkbox
				return columnIndex == 0 ? Boolean.class : String.class;
			}

			@Override
			public boolean isCellEditable(int row, int column) {
				// Chỉ cho phép chỉnh sửa cột "Select" và "Quantity"
				return column == 0;
			}
		};

		// Tạo DefaultTableModel cho tableOut
		var addTableModel = new DefaultTableModel(
				new Object[] { "Select", "ISBN", "Title", "Quantity", "Stock Quantity" }, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public Class<?> getColumnClass(int columnIndex) {
				// Cột "Select" là Boolean để hiển thị checkbox
				return columnIndex == 0 ? Boolean.class : String.class;
			}

			@Override
			public boolean isCellEditable(int row, int column) {
				// Chỉ cho phép chỉnh sửa cột "Select" và "Quantity"
				return column == 0;
			}
			
		};
		// Lặp qua danh sách sách từ DAO và thêm vào bảng
		for (BookManagementEntity book : books) {
			if (book.isStockIn()) {
				addTableModel.addRow(new Object[] { false, book.getIsbn(), book.getTitle(), book.getQuantity(),
						book.getStockQuantity() });
			} else if (book.getQuantity() == 0) {
				// Thêm sách có số lượng bằng 0 vào tableOut
				outTableModel.addRow(new Object[] { false, book.getIsbn(), book.getTitle(), 0 });
			} else {
				// Giữ trạng thái checkbox nếu có
				var isChecked = selectedStates.getOrDefault(book.getIsbn(), false);
				bookTableModel.addRow(new Object[] { isChecked, book.getIsbn(), book.getTitle(), book.getQuantity() });
			}
		}

		// Gán model cho tableBook và tuỳ chỉnh giao diện
		tableBook.setModel(bookTableModel);
		customizeTableWithCheckbox(tableBook);
		setColumnWidths(tableBook, new int[] { 80, 300, 346, 150 });

		// Gán model cho tableOut và tuỳ chỉnh giao diện
		tableOut.setModel(outTableModel);
		customizeTableWithCheckbox(tableOut);
		setColumnWidths(tableOut, new int[] { 80, 300, 346, 150 });
//		// Gán model cho tableBook và tuỳ chỉnh giao diện
		tableAdd.setModel(addTableModel);
		customizeTableWithCheckbox(tableAdd);
		setColumnWidths(tableAdd, new int[] { 80, 300, 346, 150 });

		// Thêm sự kiện chuột để tự động check/uncheck checkbox
		tableBook.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent e) {
				var row = tableBook.rowAtPoint(e.getPoint());
				var column = tableBook.columnAtPoint(e.getPoint());

				// Nếu click vào bất kỳ vị trí nào trong hàng, tự động đổi trạng thái checkbox
				if (row != -1 && column != -1 && column != 0) {
					var isChecked = (boolean) tableBook.getValueAt(row, 0);
					tableBook.setValueAt(!isChecked, row, 0);
				}
			}
		});

		// Gán model cho tableOut và tuỳ chỉnh giao diện
		tableOut.setModel(outTableModel);
		customizeTableWithCheckbox(tableOut);
		setColumnWidths(tableOut, new int[] { 80, 300, 346, 150 });

		// Thêm sự kiện chuột để tự động check/uncheck checkbox
		tableOut.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent e) {
				var row = tableOut.rowAtPoint(e.getPoint());
				var column = tableOut.columnAtPoint(e.getPoint());

				// Nếu click vào bất kỳ vị trí nào trong hàng, tự động đổi trạng thái checkbox
				if (row != -1 && column != -1 && column != 0) {
					var isChecked = (boolean) tableOut.getValueAt(row, 0);
					tableOut.setValueAt(!isChecked, row, 0);
				}
			}
		});

	}

	// Phương thức tùy chỉnh bảng có JCheckBox
	private void customizeTableWithCheckbox(JTable table) {
		table.setFont(new Font("Arial", Font.PLAIN, 14));
		table.setRowHeight(40); // Chiều cao của mỗi hàng
		table.setGridColor(new Color(224, 224, 224)); // Màu đường kẻ bảng

		var header = table.getTableHeader();
		header.setPreferredSize(new Dimension(header.getPreferredSize().width, 40)); // Chiều cao header

		// Áp dụng renderer tùy chỉnh cho header
		header.setDefaultRenderer(new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				var headerRenderer = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
						column);
				headerRenderer.setBackground(new Color(211, 211, 211)); // Màu nền header
				headerRenderer.setForeground(Color.BLACK); // Màu chữ header
				headerRenderer.setFont(new Font("Arial", Font.BOLD, 16)); // Font chữ header
				((DefaultTableCellRenderer) headerRenderer).setHorizontalAlignment(DefaultTableCellRenderer.CENTER); // Căn
																														// giữa
				return headerRenderer;
			}
		});

		var centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);

		// Căn giữa các cột, trừ cột đầu tiên
		for (var i = 1; i < table.getColumnCount(); i++) {
			table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}
	}


	private void filterBookData(String query) {
		var dao = new BookManagementDao();
		var books = dao.select();

		// Lưu trạng thái checkbox vào Map
		for (var i = 0; i < tableBook.getRowCount(); i++) {
			var isbn = tableBook.getValueAt(i, 1).toString(); // Lấy ISBN
			var isChecked = (boolean) tableBook.getValueAt(i, 0); // Lấy trạng thái checkbox
			selectedStates.put(isbn, isChecked); // Lưu vào Map
		}

		// Lưu kích thước cột
		var columnWidths = getColumnWidths(tableBook);

		// Tạo DefaultTableModel với cột đầu tiên là Boolean (JCheckBox)
		var tableModel = new DefaultTableModel(new Object[] { "Select", "ISBN", "Title", "Quantity" }, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public Class<?> getColumnClass(int columnIndex) {
				return columnIndex == 0 ? Boolean.class : String.class;
			}

			@Override
			public boolean isCellEditable(int row, int column) {
				return column == 0;
			}
		};

		for (BookManagementEntity book : books) {
			// Bỏ qua các sách đã bị chuyển
			if (removedBooks.contains(book.getIsbn())) {
				continue;
			}

			if (book.getIsbn().toLowerCase().contains(query.toLowerCase())
					|| book.getTitle().toLowerCase().contains(query.toLowerCase())) {
				boolean isChecked = selectedStates.getOrDefault(book.getIsbn(), false); // Giữ trạng thái checkbox
				tableModel.addRow(new Object[] { isChecked, book.getIsbn(), book.getTitle(), book.getQuantity() });
			}
		}

		tableBook.setModel(tableModel);
		customizeTableWithCheckbox(tableBook);

		// Khôi phục kích thước cột
		setColumnWidths(tableBook, columnWidths);
	}

	// Lưu kích thước cột
	private int[] getColumnWidths(JTable table) {
		var columnCount = table.getColumnCount();
		var widths = new int[columnCount];
		for (var i = 0; i < columnCount; i++) {
			widths[i] = table.getColumnModel().getColumn(i).getPreferredWidth();
		}
		return widths;
	}

	// Khôi phục kích thước cột
	private void setColumnWidths(JTable table, int[] widths) {
		for (var i = 0; i < widths.length; i++) {
			table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
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

	protected void btnAddBookActionPerformed(ActionEvent e) {
		var bookTableModel = (DefaultTableModel) tableBook.getModel();
		var addTableModel = (DefaultTableModel) tableAdd.getModel();

		// Duyệt qua các hàng trong bảng tableBook
		for (var i = tableBook.getRowCount() - 1; i >= 0; i--) {
			var isSelected = (boolean) tableBook.getValueAt(i, 0); // Cột "Select"
			if (isSelected) {
				// Lấy dữ liệu của hàng
				var isbn = tableBook.getValueAt(i, 1).toString();
				var title = tableBook.getValueAt(i, 2);
				var quantity = tableBook.getValueAt(i, 3);

				// Thêm hàng vào tableAdd với checkbox mặc định là "false"
				addTableModel.addRow(new Object[] { false, isbn, title, quantity });

				// Thêm ISBN vào danh sách các sách đã bị chuyển
				removedBooks.add(isbn);

				// Kiểm tra xem chỉ số i có hợp lệ trước khi xóa
				if (i >= 0 && i < bookTableModel.getRowCount()) {
					bookTableModel.removeRow(i);
				}
			}
		}
	}

	protected void btnStockIn_1ActionPerformed(ActionEvent e) {
		// Lấy model của bảng
		var model = (DefaultTableModel) tableBook.getModel();
		moveToStockIn(model);

	}

	protected void btnStockIn_2ActionPerformed(ActionEvent e) {
		var model = (DefaultTableModel) tableOut.getModel();
		moveToStockIn(model);
	}
	private void moveToStockIn(DefaultTableModel model) {
		var hasSelected = false;
		// Duyệt qua tất cả các dòng của bảng
		var choice = JOptionPane.showConfirmDialog(this,
				"Do you want to move the book into the warehouse?",
				"Confirm Stock-In", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

		if (choice == JOptionPane.YES_OPTION) {
			var rowsToRemove = new java.util.ArrayList<Integer>();

			for (var i = 0; i < model.getRowCount(); i++) {
				var isChecked = model.getValueAt(i, 0) instanceof Boolean && (boolean) model.getValueAt(i, 0);
				if (isChecked) {
					rowsToRemove.add(i);
					hasSelected = true; // Đánh dấu đã chọn ít nhất một dòng
					var isbn = model.getValueAt(i, 1).toString(); // Lấy ISBN của dòng hiện tại
					var title = model.getValueAt(i, 2).toString(); // Lấy tiêu đề sách

					// Cập nhật giá trị `stockIn` trong cơ sở dữ liệu
					var dao = new BookManagementDao();
					var success = dao.updateStockIn(isbn, true); // Cập nhật `stockIn` thành true

					if (!success) {
						JOptionPane.showMessageDialog(this,
								"Unable to update the stock-in status for the book \"" + title + "\".", "Error",
								JOptionPane.ERROR_MESSAGE);
						break;
					}

				}
			}
			for (var i = rowsToRemove.size() - 1; i >= 0; i--) {
				model.removeRow(rowsToRemove.get(i));
			}
			if (rowsToRemove.size() > 0) {
				// Hiển thị thông báo thành công
				JOptionPane.showMessageDialog(this,
						"The book has been successfully moved into the warehouse.", "Success",
						JOptionPane.INFORMATION_MESSAGE);
			}
			// Nếu không có dòng nào được chọn, hiển thị thông báo
			if (!hasSelected) {
				JOptionPane.showMessageDialog(this, "Please select at least one book to stock in.", "Notification",
						JOptionPane.WARNING_MESSAGE);
			}
		}
	}

	private void reloadTableAddData() {
		// Clear the current data in the table
		var addTableModel = (DefaultTableModel) tableAdd.getModel();
		addTableModel.setRowCount(0);

		// Reload data from the data source
		var dao = new BookManagementDao();
		var books = dao.select();

		// Add rows back into the table
		for (BookManagementEntity book : books) {
			// Only include books that meet certain conditions (e.g., books not already in
			// stock)
			if (book.isStockIn()) {
				addTableModel.addRow(new Object[] { false, book.getIsbn(), book.getTitle(), book.getQuantity(),
						book.getStockQuantity() });
			}
		}

		tableAdd.getColumnModel().getColumn(4).setMinWidth(0);
		tableAdd.getColumnModel().getColumn(4).setMaxWidth(0);
		tableAdd.getColumnModel().getColumn(4).setPreferredWidth(0);
		// Optionally, reapply any customizations to the table
		customizeTableWithCheckbox(tableAdd);
	}

	private void reloadTableBookata() {
		// Clear the current data in the table
		var addTableModel = (DefaultTableModel) tableBook.getModel();
		addTableModel.setRowCount(0);

		// Reload data from the data source
		var dao = new BookManagementDao();
		var books = dao.select();

		// Add rows back into the table
		for (BookManagementEntity book : books) {
			// Only include books that meet certain conditions (e.g., books not already in
			// stock)
			if (!book.isStockIn() && book.getQuantity() > 0) {
				addTableModel.addRow(new Object[] { false, book.getIsbn(), book.getTitle(), book.getQuantity() });
			}
		}

		// Optionally, reapply any customizations to the table
		customizeTableWithCheckbox(tableAdd);
	}

	private void reloadTableOutData() {
		// Clear the current data in the table
		var addTableModel = (DefaultTableModel) tableOut.getModel();
		addTableModel.setRowCount(0);

		// Reload data from the data source
		var dao = new BookManagementDao();
		var books = dao.select();

		// Add rows back into the table
		for (BookManagementEntity book : books) {
			// Only include books that meet certain conditions (e.g., books not already in
			// stock)
			if (!book.isStockIn() && book.getQuantity() == 0) {
				addTableModel.addRow(new Object[] { false, book.getIsbn(), book.getTitle(), book.getQuantity() });
			}
		}

		// Optionally, reapply any customizations to the table
		customizeTableWithCheckbox(tableAdd);
	}
}

