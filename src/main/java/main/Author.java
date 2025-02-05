package main;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileOutputStream;
import java.time.LocalDate;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import add.AddAuthorDialog;
import add.AddCategoriesDialog;
import add.AddPublisherDialog;
import component.CustomCard;
import dao.AuthorDao;
import dao.BibliographicCarDao;
import dao.CategoriesDao;
import dao.PublisherDao;
import edit.AuthorEditDialog;
import edit.CategoryEditDialog;
import edit.PublisherEditDialog;

public class Author extends JPanel {

	private static final long serialVersionUID = 1L;
	private JScrollPane scrollPaneAuthor;
	private JTable tableAuthor;
	private JTabbedPane tabbedPane;
	private JButton btnAddAuthor;
	private JScrollPane scrollPanePublisher;
	private JTable tablePublisher;
	private JButton btnAddPublisher;
	private JScrollPane scrollPaneCategory;
	private JTable tableCategory;
	private JButton btnAddCategory;

	// Thanh tìm kiếm cho Author, Publisher, Category
	private JTextField searchAuthor, searchPublisher, searchCategory;
	private JButton btnEditAuthor;
	private JButton btnDeleteAuthor;
	private JButton btnExcelAuthor;
	private JButton btnEditPublisher;
	private JButton btnDeletePublisher;
	private JButton btnExcelPublisher;
	private JButton btnEditCategory;
	private JButton btnDeleteCategory;
	private JButton btnExcelCategory;
	private JButton btnResetAuthor;

	private JPanel cardContainer; // Container chứa thẻ thông tin

	private JButton btnResetPublisher;
	private JButton btnResetCategory;

	public Author() {
		setBackground(new Color(244, 243, 240));
		setBounds(0, 0, 946, 706);
		setLayout(null);

		tabbedPane = new JTabbedPane();
		tabbedPane.setBounds(20, 11, 906, 679);

		// Panel Author
		var panelAuthor = new JPanel();
		panelAuthor.setBackground(new Color(255, 255, 255));
		panelAuthor.setBorder(null);
		setBackground(new Color(244, 243, 240));
		addInfoCards(); // Thêm phần thẻ thông tin
		panelAuthor.setLayout(null);

		// Panel Publisher
		var panelPublisher = new JPanel();
		panelPublisher.setBackground(new Color(255, 255, 255));
		setBackground(new Color(244, 243, 240));
		panelPublisher.setLayout(null);

		// Panel Category
		var panelCategory = new JPanel();
		panelCategory.setBackground(new Color(255, 255, 255));
		setBackground(new Color(244, 243, 240));
		panelCategory.setLayout(null);

		// Tab Author
		var tab3Icon = new ImageIcon(getClass().getResource("/hinh/8.png"));
		tabbedPane.addTab("Author", tab3Icon, panelAuthor, "This is Author Management");

		scrollPaneAuthor = new JScrollPane();
		scrollPaneAuthor.setBorder(BorderFactory.createEmptyBorder()); // Remove border
		scrollPaneAuthor.getViewport().setBorder(null); // Remove viewport border
		scrollPaneAuthor.setBounds(0, 235, 901, 410);
		panelAuthor.add(scrollPaneAuthor);

		tableAuthor = new JTable();
		tableAuthor.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				tableAuthorMouseReleased(e);
			}
		});
		tableAuthor.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableAuthor.setAutoCreateRowSorter(true);
		tableAuthor.setBorder(null);
		tableAuthor.setShowVerticalLines(false);
		scrollPaneAuthor.setViewportView(tableAuthor);

		btnAddAuthor = new JButton("");
		btnAddAuthor.addActionListener(this::btnAddAuthorActionPerformed);
		btnAddAuthor.setIcon(new ImageIcon(Author.class.getResource("/icon3/add.png")));
		btnAddAuthor.setBounds(10, 179, 62, 45);
		btnAddAuthor.setToolTipText("Add Author");
		panelAuthor.add(btnAddAuthor);

		// Thanh tìm kiếm cho Author
		searchAuthor = new JTextField("Search by Author Name...");
		searchAuthor.setBounds(385, 179, 343, 45);
		searchAuthor.setForeground(Color.GRAY);
		searchAuthor.setFont(new Font("Arial", Font.PLAIN, 14));
		searchAuthor.setBackground(Color.WHITE);
		searchAuthor.setBorder(new RoundedBorder(15)); // Bo góc cho thanh tìm kiếm Author, bỏ viền

		searchAuthor.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (searchAuthor.getText().equals("Search by Author Name...")) {
					searchAuthor.setText("");
					searchAuthor.setForeground(Color.BLACK);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (searchAuthor.getText().isEmpty()) {
					searchAuthor.setText("Search by Author Name...");
					searchAuthor.setForeground(Color.GRAY);
				}
			}
		});

		// Tự động tìm kiếm khi người dùng gõ vào thanh tìm kiếm
		searchAuthor.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				onSearch(); // Gọi hàm tìm kiếm khi người dùng nhập văn bản
			}
		});
		panelAuthor.add(searchAuthor);

		btnEditAuthor = new JButton("");
		btnEditAuthor.addActionListener(this::btnEditAuthorActionPerformed);
		btnEditAuthor.setIcon(new ImageIcon(Author.class.getResource("/icon3/pen.png")));
		btnEditAuthor.setBounds(95, 179, 62, 45);
		panelAuthor.add(btnEditAuthor);

		btnDeleteAuthor = new JButton("");
		btnDeleteAuthor.addActionListener(this::btnDeleteAuthorActionPerformed);
		btnDeleteAuthor.setIcon(new ImageIcon(Author.class.getResource("/icon3/bin.png")));
		btnDeleteAuthor.setBounds(181, 179, 62, 45);
		panelAuthor.add(btnDeleteAuthor);

		btnExcelAuthor = new JButton("");
		btnExcelAuthor.addActionListener(this::btnExcelAuthorActionPerformed);
		btnExcelAuthor.setIcon(new ImageIcon(Author.class.getResource("/icon4/xls.png")));
		btnExcelAuthor.setBounds(266, 179, 62, 45);
		panelAuthor.add(btnExcelAuthor);

		btnResetAuthor = new JButton("");
		btnResetAuthor.addActionListener(this::btnResetAuthorActionPerformed);
		btnResetAuthor.setBounds(748, 179, 62, 45);
		panelAuthor.add(btnResetAuthor);
		btnResetAuthor.setIcon(new ImageIcon(Author.class.getResource("/icon4/redo.png")));

		// Tab Publisher
		var tab2Icon = new ImageIcon(getClass().getResource("/hinh/5.png"));
		tabbedPane.addTab("Publisher", tab2Icon, panelPublisher, "This is Publisher Management");

		scrollPanePublisher = new JScrollPane();
		scrollPanePublisher.setBounds(0, 235, 901, 578);
		scrollPanePublisher.setBorder(BorderFactory.createEmptyBorder()); // Remove border
		scrollPanePublisher.getViewport().setBorder(null); // Remove viewport border
		panelPublisher.add(scrollPanePublisher);

		tablePublisher = new JTable();
		tablePublisher.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				tablePublisherMouseReleased(e);
			}
		});
		tablePublisher.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tablePublisher.setAutoCreateRowSorter(true);
		tablePublisher.setShowVerticalLines(false);
		scrollPanePublisher.setViewportView(tablePublisher);

		btnAddPublisher = new JButton("");
		btnAddPublisher.addActionListener(this::btnAddPublisherActionPerformed);
		btnAddPublisher.setToolTipText("Add Publisher");
		btnAddPublisher.setIcon(new ImageIcon(Author.class.getResource("/icon3/add.png")));
		btnAddPublisher.setBounds(10, 179, 62, 45);
		panelPublisher.add(btnAddPublisher);

		// Thanh tìm kiếm cho Publisher
		searchPublisher = new JTextField("Search by Publisher Name...");
		searchPublisher.setBounds(385, 179, 343, 45);
		searchPublisher.setForeground(Color.GRAY);
		searchPublisher.setFont(new Font("Arial", Font.PLAIN, 14));
		searchPublisher.setBackground(Color.WHITE);
		searchPublisher.setBorder(new RoundedBorder(15)); // Bo góc cho thanh tìm kiếm Author, bỏ viền

		searchPublisher.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (searchPublisher.getText().equals("Search by Publisher Name...")) {
					searchPublisher.setText("");
					searchPublisher.setForeground(Color.BLACK);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (searchPublisher.getText().isEmpty()) {
					searchPublisher.setText("Search by Publisher Name...");
					searchPublisher.setForeground(Color.GRAY);
				}
			}
		});

		// Tự động tìm kiếm khi người dùng gõ vào thanh tìm kiếm
		searchPublisher.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				onSearchPublisher(); // Gọi hàm tìm kiếm cho Publisher khi người dùng nhập văn bản
			}
		});
		panelPublisher.add(searchPublisher);

		btnEditPublisher = new JButton("");
		btnEditPublisher.addActionListener(this::btnEditPublisherActionPerformed);
		btnEditPublisher.setIcon(new ImageIcon(Author.class.getResource("/icon3/pen.png")));
		btnEditPublisher.setBounds(95, 179, 62, 45);
		panelPublisher.add(btnEditPublisher);

		btnDeletePublisher = new JButton("");
		btnDeletePublisher.addActionListener(this::btnDeletePublisherActionPerformed);
		btnDeletePublisher.setIcon(new ImageIcon(Author.class.getResource("/icon3/bin.png")));
		btnDeletePublisher.setBounds(181, 179, 62, 45);
		panelPublisher.add(btnDeletePublisher);

		btnExcelPublisher = new JButton("");
		btnExcelPublisher.addActionListener(this::btnExcelPublisherActionPerformed);
		btnExcelPublisher.setIcon(new ImageIcon(Author.class.getResource("/icon4/xls.png")));
		btnExcelPublisher.setBounds(266, 179, 62, 45);
		panelPublisher.add(btnExcelPublisher);

		btnResetPublisher = new JButton("");
		btnResetPublisher.addActionListener(this::btnResetPublisherActionPerformed);
		btnResetPublisher.setIcon(new ImageIcon(Author.class.getResource("/icon4/redo.png")));
		btnResetPublisher.setBounds(748, 179, 62, 45);
		panelPublisher.add(btnResetPublisher);

		// Tab Category
		var tabCategoryIcon = new ImageIcon(getClass().getResource("/hinh/6.png"));
		tabbedPane.addTab("Category", tabCategoryIcon, panelCategory, "This is Category Management");

		scrollPaneCategory = new JScrollPane();
		scrollPaneCategory.setBounds(0, 235, 901, 578);
		scrollPaneCategory.setBorder(BorderFactory.createEmptyBorder()); // Remove border
		scrollPaneCategory.getViewport().setBorder(null); // Remove viewport border
		panelCategory.add(scrollPaneCategory);

		tableCategory = new JTable();
		tableCategory.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				tableCategoryMouseReleased(e);
			}
		});
		tableCategory.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableCategory.setAutoCreateRowSorter(true);
		tableCategory.setBorder(null);
		tableCategory.setShowVerticalLines(false);
		scrollPaneCategory.setViewportView(tableCategory);

		btnAddCategory = new JButton("");
		btnAddCategory.addActionListener(this::btnAddCategoryActionPerformed);
		btnAddCategory.setToolTipText("Add Category");
		btnAddCategory.setIcon(new ImageIcon(Author.class.getResource("/icon3/add.png")));
		btnAddCategory.setBounds(10, 179, 62, 45);
		panelCategory.add(btnAddCategory);

		// Thanh tìm kiếm cho Category
		searchCategory = new JTextField("Search by Category Name...");
		searchCategory.setBounds(385, 179, 343, 45);
		searchCategory.setForeground(Color.GRAY);
		searchCategory.setFont(new Font("Arial", Font.PLAIN, 14));
		searchCategory.setBackground(Color.WHITE);
		searchCategory.setBorder(new RoundedBorder(15)); // Bo góc cho thanh tìm kiếm Author, bỏ viền
		searchCategory.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (searchCategory.getText().equals("Search by Category Name...")) {
					searchCategory.setText("");
					searchCategory.setForeground(Color.BLACK);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (searchCategory.getText().isEmpty()) {
					searchCategory.setText("Search by Category Name...");
					searchCategory.setForeground(Color.GRAY);
				}
			}
		});

		// Tự động tìm kiếm khi người dùng gõ vào thanh tìm kiếm
		searchCategory.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				onSearchCategory(); // Gọi hàm tìm kiếm cho Category khi người dùng nhập văn bản
			}
		});
		panelCategory.add(searchCategory);

		btnEditCategory = new JButton("");
		btnEditCategory.addActionListener(this::btnEditCategoryActionPerformed);
		btnEditCategory.setIcon(new ImageIcon(Author.class.getResource("/icon3/pen.png")));
		btnEditCategory.setBounds(95, 179, 62, 45);
		panelCategory.add(btnEditCategory);

		btnDeleteCategory = new JButton("");
		btnDeleteCategory.addActionListener(this::btnDeleteCategoryActionPerformed);
		btnDeleteCategory.setIcon(new ImageIcon(Author.class.getResource("/icon3/bin.png")));
		btnDeleteCategory.setBounds(181, 179, 62, 45);
		panelCategory.add(btnDeleteCategory);

		btnExcelCategory = new JButton("");
		btnExcelCategory.addActionListener(this::btnExcelCategoryActionPerformed);
		btnExcelCategory.setIcon(new ImageIcon(Author.class.getResource("/icon4/xls.png")));
		btnExcelCategory.setBounds(266, 179, 62, 45);
		panelCategory.add(btnExcelCategory);

		btnResetCategory = new JButton("");
		btnResetCategory.addActionListener(this::btnResetCategoryActionPerformed);
		btnResetCategory.setIcon(new ImageIcon(Author.class.getResource("/icon4/redo.png")));
		btnResetCategory.setBounds(748, 179, 62, 45);
		panelCategory.add(btnResetCategory);

		add(tabbedPane);

		tabbedPane.addChangeListener(e -> {
			if (tabbedPane.getSelectedIndex() == 1) {
				panelPublisherActivated();
			} else if (tabbedPane.getSelectedIndex() == 2) {
				panelCategoryActivated();
			}
		});

		panel1Activated();
	}

	private void addInfoCards() {
		// Kiểm tra nếu container chưa được khởi tạo
		if (cardContainer == null) {
			cardContainer = new JPanel();
			cardContainer.setLayout(new GridLayout(1, 3, 20, 0)); // 3 cards horizontally, 20px gap
			cardContainer.setBounds(33, 50, 880, 150); // Đặt vị trí
			cardContainer.setName("infoCards"); // Đặt tên để tìm kiếm khi cần xóa
			cardContainer.setBackground(new Color(255, 255, 255)); // Màu nền
			add(cardContainer); // Thêm vào Author panel
		}

		cardContainer.removeAll(); // Xóa tất cả các card cũ trước khi thêm mới

		// Sử dụng DAO để lấy dữ liệu
		var dao = new BibliographicCarDao();

		// Card 1: Total Authors
		var totalAuthors = dao.getTotalAuthors();
		var percentageAuthorsWithBooks = dao.getPercentageAuthorsWithBooks();
		var card1 = new CustomCard();
		card1.setColors(new Color(100, 149, 237), new Color(70, 130, 180)); // Gradient màu xanh
		card1.setData("Total Authors", totalAuthors + " Authors",
				String.format("With Books: %.2f%%", percentageAuthorsWithBooks),
				new ImageIcon(getClass().getResource("/icon12/a1.png")));

		// Card 2: Total Publishers
		var totalPublishersWithBooks = dao.getTotalPublishersWithBooks();
		var percentagePublishersWithBooks = dao.getPercentagePublishersWithBooks();
		var card2 = new CustomCard();
		card2.setColors(new Color(186, 85, 211), new Color(148, 0, 211)); // Gradient màu tím
		card2.setData("Total Publishers", totalPublishersWithBooks + " Publishers",
				String.format("With Books: %.2f%%", percentagePublishersWithBooks),
				new ImageIcon(getClass().getResource("/icon12/a2.png")));

		// Card 3: Total Categories
		var totalCategoriesWithBooks = dao.getTotalCategoriesWithBooks();
		var percentageCategoriesWithBooks = dao.getPercentageCategoriesWithBooks();
		var card3 = new CustomCard();
		card3.setColors(new Color(255, 204, 102), new Color(255, 140, 0)); // Gradient màu vàng
		card3.setData("Total Categories", totalCategoriesWithBooks + " Categories",
				String.format("With Books: %.2f%%", percentageCategoriesWithBooks),
				new ImageIcon(getClass().getResource("/icon12/a3.png")));

		// Add các card vào container
		cardContainer.add(card1);
		cardContainer.add(card2);
		cardContainer.add(card3);

		// Cập nhật lại giao diện
		cardContainer.revalidate();
		cardContainer.repaint();
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

	// Hàm tự động tìm kiếm cho Author
	private void onSearch() {
		var searchText = searchAuthor.getText().trim().toLowerCase();

		var dao = new AuthorDao();
		var bookCounts = dao.getBookCountByAuthor(); // Lấy số lượng sách của từng tác giả

		var filteredData = dao.select().stream()
				.filter(author -> author.getFullName().toLowerCase().contains(searchText)) // Lọc theo tên tác giả
				.toList();

		var model = (DefaultTableModel) tableAuthor.getModel();
		model.setRowCount(0); // Xóa dữ liệu cũ

		var count = 1;
		for (var author : filteredData) {
			int bookCount = bookCounts.getOrDefault(author.getIdAuthorID(), 0); // Lấy số lượng sách (mặc định 0 nếu
																				// không có)

			model.addRow(new Object[] { count++, // Số thứ tự
					author.getIdAuthorID(), // AuthorId
					author.getFullName(), // FullName
					author.getDateOfBirth(), // DateOfBirth
					author.getNationality(), // Nationality
					bookCount // Số lượng sách
			});
		}
	}

	// Hàm tự động tìm kiếm cho Publisher
	// Hàm tự động tìm kiếm cho Publisher
	private void onSearchPublisher() {
		var searchText = searchPublisher.getText().trim().toLowerCase();

		var dao = new PublisherDao();
		var bookCounts = dao.getBookCountByPublisher(); // Lấy số lượng sách liên kết với từng nhà xuất bản

		var filteredData = dao.select().stream()
				.filter(publisher -> publisher.getName().toLowerCase().contains(searchText)) // Lọc theo tên nhà xuất
																								// bản
				.toList();

		var model = (DefaultTableModel) tablePublisher.getModel();
		model.setRowCount(0); // Xóa dữ liệu cũ

		var count = 1;
		for (var publisher : filteredData) {
			int bookCount = bookCounts.getOrDefault(publisher.getPublisherID(), 0); // Lấy số lượng sách (mặc định 0 nếu
																					// không có)

			model.addRow(new Object[] { count++, // Số thứ tự
					publisher.getPublisherID(), // PublisherID (ẩn)
					publisher.getName(), // Name
					publisher.getAddress(), // Address
					publisher.getPhoneNumber(), // PhoneNumber
					publisher.getEmail(), // Email
					bookCount // Số lượng sách
			});
		}

	}

	// Hàm tự động tìm kiếm cho Category
	private void onSearchCategory() {
		var searchText = searchCategory.getText().trim().toLowerCase();

		var dao = new CategoriesDao();
		var bookCounts = dao.getBookCountByCategory(); // Lấy số lượng sách liên kết với từng danh mục

		var filteredData = dao.select().stream()
				.filter(category -> category.getCategoryName().toLowerCase().contains(searchText)) // Lọc theo tên danh
																									// mục
				.toList();

		var model = (DefaultTableModel) tableCategory.getModel();
		model.setRowCount(0); // Xóa dữ liệu cũ

		var count = 1;
		for (var category : filteredData) {
			int bookCount = bookCounts.getOrDefault(category.getCategoryID(), 0); // Lấy số lượng sách (mặc định 0 nếu
																					// không có)

			model.addRow(new Object[] { count++, // Số thứ tự
					category.getCategoryID(), // CategoryID (ẩn)
					category.getCategoryName(), // CategoryName
					category.getDescription(), // Description
					bookCount // Số lượng sách
			});
		}
	}

	public void panelCategoryActivated() {
		var dao = new CategoriesDao();

		var model = new DefaultTableModel() {
			private static final long serialVersionUID = 1L;

			@Override
			public Class<?> getColumnClass(int column) {
				return switch (column) {
				case 0 -> Integer.class; // No.
				case 1 -> Integer.class; // CategoryID
				case 2 -> String.class; // CategoryName
				case 3 -> String.class; // Description
				case 4 -> Integer.class; // BookCount
				default -> String.class;
				};
			}

			@Override
			public boolean isCellEditable(int row, int column) {
				return false; // Không cho phép chỉnh sửa bất kỳ cột nào
			}
		};

		// Thêm các cột vào bảng
		model.addColumn("No."); // Số thứ tự
		model.addColumn("CategoryID"); // CategoryID
		model.addColumn("CategoryName"); // Tên danh mục
		model.addColumn("Description"); // Mô tả
		model.addColumn("Book Count"); // Số lượng sách

		var categories = dao.select();
		var bookCounts = dao.getBookCountByCategory(); // Lấy số lượng sách liên kết với từng danh mục

		var count = 1;
		for (var category : categories) {
			int bookCount = bookCounts.getOrDefault(category.getCategoryID(), 0); // Lấy số lượng sách (mặc định 0 nếu
																					// không có)

			model.addRow(new Object[] { count++, // Số thứ tự
					category.getCategoryID(), // CategoryID
					category.getCategoryName(), // CategoryName
					category.getDescription(), // Description
					bookCount // Book Count
			});
		}

		tableCategory.setModel(model);

		// Ẩn cột CategoryID
		tableCategory.getColumnModel().getColumn(1).setMinWidth(0);
		tableCategory.getColumnModel().getColumn(1).setMaxWidth(0);
		tableCategory.getColumnModel().getColumn(1).setPreferredWidth(0);

		customizeTable(tableCategory);
		tableCategory.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		tableCategory.getColumnModel().getColumn(0).setPreferredWidth(100); // No.
		tableCategory.getColumnModel().getColumn(2).setPreferredWidth(250); // CategoryName
		tableCategory.getColumnModel().getColumn(3).setPreferredWidth(400); // Description
		tableCategory.getColumnModel().getColumn(4).setPreferredWidth(134); // BookCount
	}

	// Đổi từ private hoặc protected thành public
	public void panel1Activated() {
		var dao = new AuthorDao();

		var model = new DefaultTableModel() {
			private static final long serialVersionUID = 1L;

			@Override
			public Class<?> getColumnClass(int column) {
				return switch (column) {
				case 0 -> Integer.class; // No (Số thứ tự)
				case 1 -> Integer.class; // AuthorId
				case 2 -> String.class; // FullName
				case 3 -> LocalDate.class; // DateOfBirth
				case 4 -> String.class; // Nationality
				case 5 -> Integer.class; // BookCount
				default -> String.class;
				};
			}

			@Override
			public boolean isCellEditable(int row, int column) {
				return false; // Không cho phép chỉnh sửa bất kỳ cột nào
			}
		};

		// Thêm các cột vào bảng
		model.addColumn("No."); // Số thứ tự
		model.addColumn("AuthorId"); // AuthorId (ẩn)
		model.addColumn("FullName"); // Tên tác giả
		model.addColumn("DateOfBirth"); // Ngày sinh
		model.addColumn("Nationality"); // Quốc tịch
		model.addColumn("BookCount"); // Số lượng sách

		// Lấy số lượng sách từ DAO
		var bookCounts = dao.getBookCountByAuthor();

		// Duyệt qua danh sách tác giả và thêm vào bảng
		var count = 1;
		for (var author : dao.select()) {
			int bookCount = bookCounts.getOrDefault(author.getIdAuthorID(), 0); // Lấy số lượng sách, mặc định là 0 nếu
																				// không có
			model.addRow(new Object[] { count++, // No
					author.getIdAuthorID(), // AuthorId
					author.getFullName(), // FullName
					author.getDateOfBirth(), // DateOfBirth
					author.getNationality(), // Nationality
					bookCount // Số lượng sách
			});
		}

		tableAuthor.setModel(model);

		// Ẩn cột AuthorId (nếu cần)
		tableAuthor.getColumnModel().getColumn(1).setMinWidth(0);
		tableAuthor.getColumnModel().getColumn(1).setMaxWidth(0);
		tableAuthor.getColumnModel().getColumn(1).setPreferredWidth(0);

		customizeTable(tableAuthor);

		// Tắt chế độ tự động điều chỉnh kích thước cột
		tableAuthor.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		// Đặt chiều rộng cho từng cột
		tableAuthor.getColumnModel().getColumn(0).setPreferredWidth(100); // No.
		tableAuthor.getColumnModel().getColumn(2).setPreferredWidth(300); // FullName
		tableAuthor.getColumnModel().getColumn(3).setPreferredWidth(150); // DateOfBirth
		tableAuthor.getColumnModel().getColumn(4).setPreferredWidth(184); // Nationality
		tableAuthor.getColumnModel().getColumn(5).setPreferredWidth(150); // BookCount
	}

	public void panelPublisherActivated() {
		var dao = new PublisherDao();

		var model = new DefaultTableModel() {
			private static final long serialVersionUID = 1L;

			@Override
			public Class<?> getColumnClass(int column) {
				return switch (column) {
				case 0 -> Integer.class; // No (Số thứ tự)
				case 1 -> Integer.class; // PublisherID
				case 2 -> String.class; // Name
				case 3 -> String.class; // Address
				case 4 -> String.class; // PhoneNumber
				case 5 -> String.class; // Email
				case 6 -> Integer.class; // BookCount
				default -> String.class;
				};
			}

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		// Thêm các cột vào bảng
		model.addColumn("No.");
		model.addColumn("PublisherID");
		model.addColumn("Name");
		model.addColumn("Address");
		model.addColumn("Phone Number");
		model.addColumn("Email");
		model.addColumn("Book Count"); // Thêm cột số lượng sách

		var publishers = dao.select();
		var bookCounts = dao.getBookCountByPublisher(); // Lấy số lượng sách liên kết với từng nhà xuất bản

		var count = 1;
		for (var publisher : publishers) {
			int bookCount = bookCounts.getOrDefault(publisher.getPublisherID(), 0); // Lấy số lượng sách (mặc định 0 nếu
																					// không có)

			model.addRow(new Object[] { count++, // Số thứ tự
					publisher.getPublisherID(), // PublisherID
					publisher.getName(), // Name
					publisher.getAddress(), // Address
					publisher.getPhoneNumber(), // PhoneNumber
					publisher.getEmail(), // Email
					bookCount // Book Count
			});
		}

		tablePublisher.setModel(model);

		// Ẩn cột PublisherID
		tablePublisher.getColumnModel().getColumn(1).setMinWidth(0);
		tablePublisher.getColumnModel().getColumn(1).setMaxWidth(0);
		tablePublisher.getColumnModel().getColumn(1).setPreferredWidth(0);

		customizeTable(tablePublisher);
		tablePublisher.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		tablePublisher.getColumnModel().getColumn(0).setPreferredWidth(100);
		tablePublisher.getColumnModel().getColumn(2).setPreferredWidth(206);
		tablePublisher.getColumnModel().getColumn(3).setPreferredWidth(202);
		tablePublisher.getColumnModel().getColumn(4).setPreferredWidth(110);
		tablePublisher.getColumnModel().getColumn(5).setPreferredWidth(146);
		tablePublisher.getColumnModel().getColumn(6).setPreferredWidth(120); // Đặt chiều rộng cho cột BookCount
	}

	private void customizeTable(JTable table) {
		table.setFont(new Font("Arial", Font.PLAIN, 14));
		table.setRowHeight(40); // Chiều cao của mỗi hàng
		table.setGridColor(new Color(224, 224, 224));

		var centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
		for (var i = 0; i < table.getColumnCount(); i++) {
			table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}

		var header = table.getTableHeader();
		header.setPreferredSize(new Dimension(header.getPreferredSize().width, 40));
		header.setDefaultRenderer(new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				var headerRenderer = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
						column);
				headerRenderer.setBackground(new Color(211, 211, 211));
				headerRenderer.setForeground(Color.BLACK);
				headerRenderer.setFont(new Font("Arial", Font.BOLD, 16));
				((DefaultTableCellRenderer) headerRenderer).setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
				return headerRenderer;
			}
		});
	}

	protected void btnEditAuthorActionPerformed(ActionEvent e) {
		// Kiểm tra xem có hàng nào được chọn trong bảng
		var selectedRow = tableAuthor.getSelectedRow();
		if (selectedRow != -1) {
			try {
				// Lấy AuthorId từ cột thứ hai
				var authorID = (int) tableAuthor.getValueAt(selectedRow, 1);

				// Khởi tạo DAO và lấy dữ liệu Author từ cơ sở dữ liệu
				var dao = new AuthorDao();
				var selectedAuthor = dao.selectById(authorID);

				// Nếu tìm thấy Author, mở AuthorEditDialog
				if (selectedAuthor != null) {
					var dialog = new AuthorEditDialog(this, selectedAuthor);
					dialog.setVisible(true);
				} else {
					JOptionPane.showMessageDialog(null, "Author not found in the database!", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(null,
						"An error occurred while trying to edit the author: " + ex.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(null, "Please select a row to edit!", "Warning", JOptionPane.WARNING_MESSAGE);
		}
	}

	protected void btnAddAuthorActionPerformed(ActionEvent e) {
		// Tạo và hiển thị dialog thêm mới tác giả
		try {
			var addAuthorDialog = new AddAuthorDialog(this);
			addAuthorDialog.setVisible(true);
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "An error occurred while opening Add Author Dialog: " + ex.getMessage(),
					"Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	protected void btnDeleteAuthorActionPerformed(ActionEvent e) {
		var selectedRow = tableAuthor.getSelectedRow();
		if (selectedRow != -1) {
			try {
				var authorID = (int) tableAuthor.getValueAt(selectedRow, 1); // Lấy AuthorID
				var confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this author?",
						"Confirmation", JOptionPane.YES_NO_OPTION);

				if (confirm == JOptionPane.YES_OPTION) {
					var currentUserId = AuthUtil.getCurrentUserId(); // Sử dụng AuthUtil
					var dao = new AuthorDao();
					dao.delete(authorID, currentUserId); // Gọi DAO để xóa
					JOptionPane.showMessageDialog(this, "Author deleted successfully!");
					panel1Activated(); // Làm mới bảng
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(this, "Please select a row to delete!", "Warning",
					JOptionPane.WARNING_MESSAGE);
		}
	}

	protected void tableAuthorMouseReleased(MouseEvent e) {
		// Kiểm tra sự kiện chuột phải hoặc Control + Click (dành cho MacOS)
		if ((e.isPopupTrigger() || (e.isControlDown() && e.getButton() == MouseEvent.BUTTON3))
				&& e.getComponent() instanceof JTable) {
			showPopupMenu(e); // Hiển thị menu chuột phải hoặc Control + Click
		}
	}

	private void showPopupMenu(java.awt.event.MouseEvent e) {
		var row = tableAuthor.rowAtPoint(e.getPoint()); // Lấy dòng được nhấp chuột phải
		if (row >= 0 && row < tableAuthor.getRowCount()) {
			tableAuthor.setRowSelectionInterval(row, row); // Chọn dòng được nhấp chuột
		} else {
			tableAuthor.clearSelection(); // Hủy chọn nếu không nhấp vào dòng nào
		}

		// Tạo popup menu
		var popupMenu = new JPopupMenu();

		// Thêm tùy chọn "Edit"
		var menuItemEdit = new JMenuItem("Edit");
		menuItemEdit.addActionListener(event -> {
			var selectedRow = tableAuthor.getSelectedRow();
			if (selectedRow != -1) {
				var authorID = (int) tableAuthor.getValueAt(selectedRow, 1); // Lấy AuthorId từ cột ẩn
				var dao = new AuthorDao();
				var selectedAuthor = dao.selectById(authorID);
				if (selectedAuthor != null) {
					var dialog = new AuthorEditDialog(this, selectedAuthor);
					dialog.setVisible(true);
				} else {
					JOptionPane.showMessageDialog(this, "Author not found in the database!", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		popupMenu.add(menuItemEdit);

		// Thêm tùy chọn "Delete"
		var menuItemDelete = new JMenuItem("Delete");
		menuItemDelete.addActionListener(event -> {
			var selectedRow = tableAuthor.getSelectedRow();
			if (selectedRow != -1) {
				var authorID = (int) tableAuthor.getValueAt(selectedRow, 1); // Lấy AuthorId từ cột ẩn
				var currentUserId = AuthUtil.getCurrentUserId(); // Lấy ID người dùng hiện tại
				var confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this author?",
						"Confirmation", JOptionPane.YES_NO_OPTION);
				if (confirm == JOptionPane.YES_OPTION) {
					var dao = new AuthorDao();
					dao.delete(authorID, currentUserId); // Xóa tác giả
					JOptionPane.showMessageDialog(this, "Author deleted successfully!");
					panel1Activated(); // Tải lại bảng
				}
			}
		});
		popupMenu.add(menuItemDelete);

		// Hiển thị popup menu
		popupMenu.show(tableAuthor, e.getX(), e.getY());
	}

	protected void btnAddCategoryActionPerformed(ActionEvent e) {
		try {
			var addCategoryDialog = new AddCategoriesDialog(this);
			addCategoryDialog.setVisible(true);
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this,
					"An error occurred while opening Add Category Dialog: " + ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	protected void btnEditCategoryActionPerformed(ActionEvent e) {
		var selectedRow = tableCategory.getSelectedRow();
		if (selectedRow != -1) {
			try {
				// Lấy CategoryID từ cột tương ứng
				var categoryID = (int) tableCategory.getValueAt(selectedRow, 1);

				// Khởi tạo DAO và lấy dữ liệu Category từ cơ sở dữ liệu
				var dao = new CategoriesDao();
				var selectedCategory = dao.selectById(categoryID);

				// Nếu tìm thấy Category, mở CategoryEditDialog
				if (selectedCategory != null) {
					var dialog = new CategoryEditDialog(this, selectedCategory);
					dialog.setVisible(true);
				} else {
					JOptionPane.showMessageDialog(null, "Category not found in the database!", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(null,
						"An error occurred while trying to edit the category: " + ex.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(null, "Please select a row to edit!", "Warning", JOptionPane.WARNING_MESSAGE);
		}
	}

	protected void btnDeleteCategoryActionPerformed(ActionEvent e) {
		var selectedRow = tableCategory.getSelectedRow();
		if (selectedRow != -1) {
			try {
				var categoryID = (int) tableCategory.getValueAt(selectedRow, 1); // Lấy CategoryID
				var confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this category?",
						"Confirmation", JOptionPane.YES_NO_OPTION);

				if (confirm == JOptionPane.YES_OPTION) {
					var currentUserId = AuthUtil.getCurrentUserId(); // Sử dụng AuthUtil
					var dao = new CategoriesDao();
					dao.delete(categoryID, currentUserId); // Gọi DAO để xóa
					JOptionPane.showMessageDialog(this, "Category deleted successfully!");
					panelCategoryActivated(); // Làm mới bảng
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(this, "Please select a row to delete!", "Warning",
					JOptionPane.WARNING_MESSAGE);
		}
	}

	protected void tableCategoryMouseReleased(MouseEvent e) {
		// Kiểm tra sự kiện chuột phải hoặc Control + Click (dành cho MacOS)
		if ((e.isPopupTrigger() || (e.isControlDown() && e.getButton() == MouseEvent.BUTTON3))
				&& e.getComponent() instanceof JTable) {
			showPopupMenuCategory(e); // Hiển thị menu chuột phải hoặc Control + Click
		}
	}

	private void showPopupMenuCategory(MouseEvent e) {
		var row = tableCategory.rowAtPoint(e.getPoint()); // Lấy dòng được nhấp chuột phải
		if (row >= 0 && row < tableCategory.getRowCount()) {
			tableCategory.setRowSelectionInterval(row, row); // Chọn dòng được nhấp chuột
		} else {
			tableCategory.clearSelection(); // Hủy chọn nếu không nhấp vào dòng nào
		}

		// Tạo popup menu
		var popupMenu = new JPopupMenu();

		// Thêm tùy chọn "Edit"
		var menuItemEdit = new JMenuItem("Edit");
		menuItemEdit.addActionListener(event -> {
			var selectedRow = tableCategory.getSelectedRow();
			if (selectedRow != -1) {
				var categoryID = (int) tableCategory.getValueAt(selectedRow, 1); // Lấy CategoryID từ cột ẩn
				var dao = new CategoriesDao();
				var selectedCategory = dao.selectById(categoryID); // Lấy thông tin danh mục từ DAO
				if (selectedCategory != null) {
					var dialog = new CategoryEditDialog(this, selectedCategory); // Hiển thị dialog sửa danh mục
					dialog.setVisible(true);
				} else {
					JOptionPane.showMessageDialog(this, "Category not found in the database!", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		popupMenu.add(menuItemEdit);

		// Thêm tùy chọn "Delete"
		var menuItemDelete = new JMenuItem("Delete");
		menuItemDelete.addActionListener(event -> {
			var selectedRow = tableCategory.getSelectedRow();
			if (selectedRow != -1) {
				var categoryID = (int) tableCategory.getValueAt(selectedRow, 1); // Lấy CategoryID từ cột ẩn
				var currentUserId = AuthUtil.getCurrentUserId(); // Lấy ID người dùng hiện tại
				var confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this category?",
						"Confirmation", JOptionPane.YES_NO_OPTION);
				if (confirm == JOptionPane.YES_OPTION) {
					var dao = new CategoriesDao();
					dao.delete(categoryID, currentUserId); // Gọi phương thức xóa trong DAO
					JOptionPane.showMessageDialog(this, "Category deleted successfully!");
					panelCategoryActivated(); // Cập nhật lại bảng
				}
			}
		});
		popupMenu.add(menuItemDelete);

		// Hiển thị popup menu
		popupMenu.show(tableCategory, e.getX(), e.getY());
	}

	public class AuthUtil {
		public static int getCurrentUserId() {
			// Lấy ID người dùng hiện tại từ session hoặc thông tin đăng nhập
			return 1; // Tạm thời trả về 1
		}
	}

	protected void btnAddPublisherActionPerformed(ActionEvent e) {
		try {
			// Tạo và hiển thị dialog thêm mới nhà xuất bản
			var addPublisherDialog = new AddPublisherDialog(this);
			addPublisherDialog.setVisible(true);
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this,
					"An error occurred while opening Add Publisher Dialog: " + ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	protected void btnEditPublisherActionPerformed(ActionEvent e) {
		var selectedRow = tablePublisher.getSelectedRow();
		if (selectedRow != -1) {
			try {
				var publisherID = (int) tablePublisher.getValueAt(selectedRow, 1); // Lấy PublisherID từ cột ẩn
				var dao = new PublisherDao();
				var selectedPublisher = dao.selectById(publisherID);

				if (selectedPublisher != null) {
					var dialog = new PublisherEditDialog(this, selectedPublisher);
					dialog.setVisible(true);
				} else {
					JOptionPane.showMessageDialog(null, "Publisher not found in the database!", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(null,
						"An error occurred while trying to edit the publisher: " + ex.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(null, "Please select a row to edit!", "Warning", JOptionPane.WARNING_MESSAGE);
		}
	}

	protected void btnDeletePublisherActionPerformed(ActionEvent e) {
		var selectedRow = tablePublisher.getSelectedRow();
		if (selectedRow != -1) {
			try {
				var publisherID = (int) tablePublisher.getValueAt(selectedRow, 1); // Lấy PublisherID
				var confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this publisher?",
						"Confirmation", JOptionPane.YES_NO_OPTION);

				if (confirm == JOptionPane.YES_OPTION) {
					var currentUserId = AuthUtil.getCurrentUserId(); // Sử dụng AuthUtil
					var dao = new PublisherDao();
					dao.delete(publisherID, currentUserId); // Gọi DAO để xóa
					JOptionPane.showMessageDialog(this, "Publisher deleted successfully!");
					panelPublisherActivated(); // Làm mới bảng
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(this, "Please select a row to delete!", "Warning",
					JOptionPane.WARNING_MESSAGE);
		}
	}

	protected void tablePublisherMouseReleased(MouseEvent e) {
		// Kiểm tra sự kiện chuột phải hoặc Control + Click (dành cho MacOS)
		if ((e.isPopupTrigger() || (e.isControlDown() && e.getButton() == MouseEvent.BUTTON3))
				&& e.getComponent() instanceof JTable) {
			showPopupMenuPublisher(e); // Gọi đúng phương thức
		}
	}

	private void showPopupMenuPublisher(MouseEvent e) {
		var row = tablePublisher.rowAtPoint(e.getPoint()); // Lấy dòng được nhấp chuột phải
		if (row >= 0 && row < tablePublisher.getRowCount()) {
			tablePublisher.setRowSelectionInterval(row, row); // Chọn dòng được nhấp chuột
		} else {
			tablePublisher.clearSelection(); // Hủy chọn nếu không nhấp vào dòng nào
		}

		var popupMenu = new JPopupMenu();

		// Thêm tùy chọn "Edit"
		var menuItemEdit = new JMenuItem("Edit");
		menuItemEdit.addActionListener(event -> {
			var selectedRow = tablePublisher.getSelectedRow();
			if (selectedRow != -1) {
				var publisherID = (int) tablePublisher.getValueAt(selectedRow, 1); // Lấy PublisherID từ cột ẩn
				var dao = new PublisherDao();
				var selectedPublisher = dao.selectById(publisherID); // Lấy thông tin nhà xuất bản từ DAO
				if (selectedPublisher != null) {
					var dialog = new PublisherEditDialog(this, selectedPublisher); // Hiển thị dialog sửa nhà xuất bản
					dialog.setVisible(true);
				} else {
					JOptionPane.showMessageDialog(this, "Publisher not found in the database!", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		popupMenu.add(menuItemEdit);

		// Thêm tùy chọn "Delete"
		var menuItemDelete = new JMenuItem("Delete");
		menuItemDelete.addActionListener(event -> {
			var selectedRow = tablePublisher.getSelectedRow();
			if (selectedRow != -1) {
				var publisherID = (int) tablePublisher.getValueAt(selectedRow, 1); // Lấy PublisherID từ cột ẩn
				var confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this publisher?",
						"Confirmation", JOptionPane.YES_NO_OPTION);
				if (confirm == JOptionPane.YES_OPTION) {
					var dao = new PublisherDao();
					var currentUserId = AuthUtil.getCurrentUserId(); // Lấy ID người dùng hiện tại
					dao.delete(publisherID, currentUserId); // Gọi phương thức xóa trong DAO
					JOptionPane.showMessageDialog(this, "Publisher deleted successfully!");
					panelPublisherActivated(); // Cập nhật lại bảng
				}
			}
		});
		popupMenu.add(menuItemDelete);

		// Hiển thị popup menu
		popupMenu.show(tablePublisher, e.getX(), e.getY());
	}

	protected void btnExcelAuthorActionPerformed(ActionEvent e) {
		try {
			// Lấy toàn bộ danh sách tác giả từ cơ sở dữ liệu
			var dao = new AuthorDao();
			var allAuthors = dao.select();

			if (allAuthors.isEmpty()) {
				JOptionPane.showMessageDialog(this, "No data to export!", "Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}

			// Tạo file Excel
			Workbook workbook = new XSSFWorkbook();
			var sheet = workbook.createSheet("Authors");

			// Tạo hàng tiêu đề
			var headerRow = sheet.createRow(0);
			String[] headers = { "AuthorID", "FullName", "DateOfBirth", "Nationality", "BookCount" };

			for (var i = 0; i < headers.length; i++) {
				var cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);
			}

			// Lấy số lượng sách liên kết với mỗi tác giả
			var bookCounts = dao.getBookCountByAuthor();

			// Thêm dữ liệu từ danh sách tác giả
			var rowNum = 1;
			for (var author : allAuthors) {
				var row = sheet.createRow(rowNum++);
				row.createCell(0).setCellValue(author.getIdAuthorID()); // AuthorID
				row.createCell(1).setCellValue(author.getFullName()); // FullName
				row.createCell(2)
						.setCellValue(author.getDateOfBirth() != null ? author.getDateOfBirth().toString() : ""); // DateOfBirth
				row.createCell(3).setCellValue(author.getNationality()); // Nationality
				row.createCell(4).setCellValue(bookCounts.getOrDefault(author.getIdAuthorID(), 0)); // BookCount
			}

			// Lưu file Excel
			var filePath = System.getProperty("user.home") + "/Documents/Authors.xlsx";
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

	protected void btnExcelPublisherActionPerformed(ActionEvent e) {
		try {
			// Lấy toàn bộ danh sách nhà xuất bản từ cơ sở dữ liệu
			var dao = new PublisherDao();
			var allPublishers = dao.select();

			// Lấy dữ liệu số lượng sách theo PublisherID
			var bookCounts = dao.getBookCountByPublisher();

			if (allPublishers.isEmpty()) {
				JOptionPane.showMessageDialog(this, "No data to export!", "Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}

			// Tạo file Excel
			Workbook workbook = new XSSFWorkbook();
			var sheet = workbook.createSheet("Publishers");

			// Tạo hàng tiêu đề
			var headerRow = sheet.createRow(0);
			String[] headers = { "PublisherID", "Name", "Address", "PhoneNumber", "Email", "BookCounts" };

			for (var i = 0; i < headers.length; i++) {
				var cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);
			}

			// Thêm dữ liệu từ danh sách nhà xuất bản
			var rowNum = 1;
			for (var publisher : allPublishers) {
				var row = sheet.createRow(rowNum++);
				row.createCell(0).setCellValue(publisher.getPublisherID());
				row.createCell(1).setCellValue(publisher.getName());
				row.createCell(2).setCellValue(publisher.getAddress() != null ? publisher.getAddress() : "");
				row.createCell(3).setCellValue(publisher.getPhoneNumber() != null ? publisher.getPhoneNumber() : "");
				row.createCell(4).setCellValue(publisher.getEmail() != null ? publisher.getEmail() : "");

				// Lấy số lượng sách từ Map `bookCounts`
				var bookCount = bookCounts.getOrDefault(publisher.getPublisherID(), 0);
				row.createCell(5).setCellValue(bookCount); // Cột "BookCounts"
			}

			// Lưu file Excel
			var filePath = System.getProperty("user.home") + "/Documents/Publishers.xlsx";
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

	protected void btnExcelCategoryActionPerformed(ActionEvent e) {
		try {
			// Lấy toàn bộ danh sách danh mục từ cơ sở dữ liệu
			var dao = new CategoriesDao();
			var allCategories = dao.select();

			// Lấy dữ liệu số lượng sách theo CategoryID
			var bookCounts = dao.getBookCountByCategory();

			if (allCategories.isEmpty()) {
				JOptionPane.showMessageDialog(this, "No data to export!", "Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}

			// Tạo file Excel
			Workbook workbook = new XSSFWorkbook();
			var sheet = workbook.createSheet("Categories");

			// Tạo hàng tiêu đề
			var headerRow = sheet.createRow(0);
			String[] headers = { "CategoryID", "CategoryName", "Description", "BookCounts" };

			for (var i = 0; i < headers.length; i++) {
				var cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);
			}

			// Thêm dữ liệu từ danh sách danh mục
			var rowNum = 1;
			for (var category : allCategories) {
				var row = sheet.createRow(rowNum++);
				row.createCell(0).setCellValue(category.getCategoryID());
				row.createCell(1).setCellValue(category.getCategoryName());
				row.createCell(2).setCellValue(category.getDescription() != null ? category.getDescription() : "");

				// Lấy số lượng sách từ Map `bookCounts`
				var bookCount = bookCounts.getOrDefault(category.getCategoryID(), 0);
				row.createCell(3).setCellValue(bookCount); // Cột "BookCounts"
			}

			// Lưu file Excel
			var filePath = System.getProperty("user.home") + "/Documents/Categories.xlsx";
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

	private void reloadPage() {
		try {
			// Tải lại dữ liệu từ cơ sở dữ liệu
			panel1Activated(); // Tải lại bảng Author
			panelPublisherActivated(); // Tải lại bảng Publisher
			panelCategoryActivated(); // Tải lại bảng Category

			// Xóa các thẻ thông tin cũ
			removeInfoCards();

			// Thêm lại thẻ thông tin mới
			addInfoCards();

			// Đặt lại các cài đặt tìm kiếm (nếu có)
			searchAuthor.setText("Search by Author Name...");
			searchPublisher.setText("Search by Publisher Name...");
			searchCategory.setText("Search by Category Name...");

			// Cập nhật lại giao diện
			revalidate();
			repaint();

			// Hiển thị thông báo thành công
			JOptionPane.showMessageDialog(this, "Page and info cards reloaded successfully!", "Success",
					JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error while reloading data: " + ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void removeInfoCards() {
		if (cardContainer != null) {
			cardContainer.removeAll(); // Xóa tất cả các card trong container
			cardContainer.revalidate();
			cardContainer.repaint();
		}
	}

	protected void btnResetAuthorActionPerformed(ActionEvent e) {
		reloadPage(); // Làm mới bảng và cập nhật lại thẻ thông tin
	}

	protected void btnResetPublisherActionPerformed(ActionEvent e) {
		reloadPage();
	}

	protected void btnResetCategoryActionPerformed(ActionEvent e) {
		reloadPage();
	}
}
