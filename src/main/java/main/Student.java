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

import add.AddStudent;
import component.CustomCard;
import component.CustomScrollBarUI;
import component.RoundedPanel;
import dao.StudentCardDao;
import dao.StudentDao;
import edit.StudentEdit;
import entity.StudentEntity;
import view.StudentViewDialog;

public class Student extends JPanel {

	private static final long serialVersionUID = 1L;
	private JPanel panelTable;
	private JTable tableStudent;
	private DefaultTableModel tableModel;
	private JTextField pageInput;
	private JLabel pageInfo;
	private JTextField searchField;
	private int currRow;

	private int currentPage = 1; // Current page
	private final int rowsPerPage = 6; // Rows per page
	private java.util.List<StudentEntity> students; // List of students
	private java.util.List<StudentEntity> filteredStudents; // Filtered students based on search
	private StudentViewDialog currentStudentViewDialog; // Bảng hiện tại
	private StudentEdit currentStudentEditDialog; // Thêm biến toàn cục để giữ bảng hiện tại
	private AddStudent currentAddStudentDialog; // Biến toàn cục để giữ tham chiếu đến AddStudent hiện tại
	private JButton btnDelete;
	private boolean ispayment = true;

	public Student() {
		setBackground(new Color(244, 243, 240));
		setBounds(0, 0, 946, 706);
		setLayout(null);
		addInfoCards(); // Gọi phương thức addInfoCards để thêm các thẻ thông tin

		// Search field
		searchField = new JTextField("Search by student name...");
		searchField.setBounds(75, 27, 796, 29);
		searchField.setForeground(Color.GRAY);
		searchField.setFont(new Font("Arial", Font.PLAIN, 16));
		searchField.setBackground(Color.WHITE);
		searchField.setBorder(null);
		searchField.addFocusListener(new java.awt.event.FocusAdapter() {
			@Override
			public void focusGained(java.awt.event.FocusEvent e) {
				if (searchField.getText().equals("Search by student name...")) {
					searchField.setText("");
					searchField.setForeground(Color.BLACK);
				}
			}

			@Override
			public void focusLost(java.awt.event.FocusEvent e) {
				if (searchField.getText().isEmpty()) {
					searchField.setText("Search by student name...");
					searchField.setForeground(Color.GRAY);
				}
			}
		});

		// Add key listener to update table on each keystroke
		searchField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				filterStudents(); // Filter and update table based on search input
			}
		});
		add(searchField);
		// Icon menu
		var menuIcon = new JLabel(new ImageIcon(Student.class.getResource("/icon4/redo.png")));
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
		tableStudent = new JTable();
		tableStudent.setAutoCreateRowSorter(true);
		tableStudent.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) { // Chuột phải
					var row = tableStudent.rowAtPoint(e.getPoint());
					if (row >= 0 && row < tableStudent.getRowCount()) {
						tableStudent.setRowSelectionInterval(row, row); // Chọn hàng được nhấn
						showPopupMenu(e);
					}
				} else if (e.getButton() == MouseEvent.BUTTON1) {
					currRow = tableStudent.rowAtPoint(e.getPoint());

				} // Chuột trái

			}
		});
		tableStudent.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableStudent.setShowVerticalLines(false);
		tableStudent.setFont(new Font("Arial", Font.PLAIN, 14));
		tableStudent.setRowHeight(80); // Set row height to 80 for Avatar imagesStudentCode

		// Create DefaultTableModel
		tableModel = new DefaultTableModel(new String[] { "StudentID", "Avatar", "Code", "Name", "Books Rented",
				"Late Returns", "Damaged Books", "Orders", "isPayment" }, 0) {
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
		tableStudent.setModel(tableModel);

		// Ẩn cột StudentID
		tableStudent.getColumnModel().getColumn(0).setMinWidth(0);
		tableStudent.getColumnModel().getColumn(0).setMaxWidth(0);
		tableStudent.getColumnModel().getColumn(0).setPreferredWidth(0);

		tableStudent.getColumnModel().getColumn(8).setMinWidth(0);
		tableStudent.getColumnModel().getColumn(8).setMaxWidth(0);
		tableStudent.getColumnModel().getColumn(8).setPreferredWidth(0);
		// Customize the table
		customizeTable(tableStudent);

		// Cập nhật phần cấu hình JScrollPane
		var scrollPane = new JScrollPane(tableStudent);
		scrollPane.setBounds(20, 11, 869, 358);

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

		tableStudent.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				var selectedRow = tableStudent.getSelectedRow();
				if (selectedRow !=-1) {
					Object valueAt = tableStudent.getValueAt(selectedRow, 8);
			        ispayment =  (Boolean) valueAt;
			        btnDelete.setEnabled(!ispayment);
				}
			}
			
		});

		// Previous button
		var btnPrevious = new JButton("");
		btnPrevious.setIcon(new ImageIcon(Student.class.getResource("/iconNext/previous.png")));
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
		btnNext.setIcon(new ImageIcon(Student.class.getResource("/iconNext/next-button.png")));
		btnNext.setBounds(478, 373, 65, 30);
		btnNext.addActionListener(e -> goToNextPage());
		panelTable.add(btnNext);

		// Page info
		pageInfo = new JLabel();
		pageInfo.setBounds(580, 373, 200, 30);
		panelTable.add(pageInfo);

		// Buttons
		var btnAdd = new JButton(new ImageIcon(Student.class.getResource("/icon3/add.png")));
		btnAdd.addActionListener(this::btnAddActionPerformed);
		btnAdd.setBounds(20, 243, 45, 38);
		add(btnAdd);

		var btnEdit = new JButton(new ImageIcon(Student.class.getResource("/icon3/pen.png")));
		btnEdit.addActionListener(this::btnEditActionPerformed);
		btnEdit.setBounds(85, 243, 45, 38);
		add(btnEdit);

		btnDelete = new JButton(new ImageIcon(Student.class.getResource("/icon3/bin.png")));
		btnDelete.addActionListener(this::btnDeleteActionPerformed);
		btnDelete.setBounds(150, 243, 45, 38);
		add(btnDelete);

		var btnView = new JButton(new ImageIcon(Student.class.getResource("/icon4/search.png")));
		btnView.addActionListener(this::btnViewActionPerformed);
		btnView.setBounds(216, 243, 45, 38);
		add(btnView);

		var btnExcel = new JButton(new ImageIcon(Student.class.getResource("/icon4/xls.png")));
		btnExcel.addActionListener(this::btnExcelActionPerformed);
		btnExcel.setBounds(283, 243, 45, 38);
		add(btnExcel);

		// Load data into the table
		loadStudentData();
		updateTable();
		
	}
	public void reloadTableWithStudents(List<StudentEntity> students) {
	    tableModel.setRowCount(0); // Xóa tất cả dữ liệu hiện tại trong bảng

	    // Lặp qua danh sách sinh viên để thêm vào bảng
	    for (StudentEntity student : students) {
	        ImageIcon avatarIcon = null;
	        if (student.getAvatar() != null && !student.getAvatar().isEmpty()) {
	            var resource = "src/main/resources/avatar/" + student.getAvatar();
	            avatarIcon = new ImageIcon(
	                    new ImageIcon(resource).getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH));
	        }

	        if (avatarIcon == null) {
	            var defaultResource = getClass().getClassLoader().getResource("avatar/default_avatar.png");
	            if (defaultResource != null) {
	                avatarIcon = new ImageIcon(
	                        new ImageIcon(defaultResource).getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH));
	            }
	        }

	        tableModel.addRow(new Object[] {
	            student.getStudentID(),
	            avatarIcon,
	            student.getStudentCode(),
	            student.getFullName(),
	            student.getTotalBooksRented(),
	            student.getLateReturnsCount(),
	            student.getDamagedBooksCount(),
	            student.getTotalOrders()
	        });
	    }

	    // Cập nhật giao diện
	    tableStudent.revalidate();
	    tableStudent.repaint();
	}

	private void reloadPage() {
		// Tải lại dữ liệu từ cơ sở dữ liệu
		loadStudentData();

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
		cardPanel.setBackground(new Color(244, 243, 240)); // Background color of cardPanel

		// Tạo DAO để lấy dữ liệu
		var dao = new StudentCardDao();

		// Card 1: Total Students Borrowing Books
		var totalStudentsBorrowingBooks = dao.getTotalStudentsBorrowingBooks();
		var percentageStudentsBorrowingBooks = dao.getPercentageStudentsBorrowingBooks();
		var card1 = new CustomCard();
		card1.setColors(new Color(100, 149, 237), new Color(70, 130, 180)); // Gradient màu xanh
		card1.setData("Borrowing Students", totalStudentsBorrowingBooks + " Students",
				String.format("Percentage: %.2f%%", percentageStudentsBorrowingBooks),
				new ImageIcon(getClass().getResource("/icon12/s1.png")));

		// Card 2: Students Borrowing Today
		var totalStudentsBorrowingToday = dao.getTotalStudentsBorrowingToday();
		var percentageStudentsBorrowingToday = dao.getPercentageStudentsBorrowingToday();
		var card2 = new CustomCard();
		card2.setColors(new Color(186, 85, 211), new Color(148, 0, 211)); // Gradient màu tím
		card2.setData("Borrowing Today", totalStudentsBorrowingToday + " Students",
				String.format("Percentage: %.2f%%", percentageStudentsBorrowingToday),
				new ImageIcon(getClass().getResource("/icon12/s2.png")));

		// Card 3: Students Returning Today
		var totalStudentsReturningToday = dao.getTotalStudentsReturningToday();
		var percentageStudentsReturningToday = dao.getPercentageStudentsReturningToday();
		var card3 = new CustomCard();
		card3.setColors(new Color(255, 204, 102), new Color(255, 140, 0)); // Gradient màu vàng
		card3.setData("Returning Today", totalStudentsReturningToday + " Students",
				String.format("Percentage: %.2f%%", percentageStudentsReturningToday),
				new ImageIcon(getClass().getResource("/icon12/s3.png")));

		// Add các card vào panel
		cardPanel.add(card1);
		cardPanel.add(card2);
		cardPanel.add(card3);

		add(cardPanel);
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
			if (i != 1) { // Cột Avatar (index = 1)
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

	private void loadStudentData() {
		// Get student data from StudentDao
		var studentDao = new StudentDao();
		students = studentDao.select();
		filteredStudents = students; // Initially, all students are displayed
		
	}

	private void filterStudents() {
		var searchText = searchField.getText().trim().toLowerCase();

		// Nếu ô tìm kiếm trống hoặc chứa văn bản mặc định
		if (searchText.equals("search by student...") || searchText.isEmpty()) {
			filteredStudents = students;
		} else {
			// Lọc theo cả StudentCode và FullName
			filteredStudents = students.stream()
					.filter(student -> student.getStudentCode().toLowerCase().contains(searchText)
							|| student.getFullName().toLowerCase().contains(searchText))
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
		var end = Math.min(start + rowsPerPage, filteredStudents.size()); // Tính chỉ số kết thúc

		for (var i = start; i < end; i++) {
			var student = filteredStudents.get(i);

			// Tải hình ảnh avatar
			ImageIcon avatarIcon = null;
			if (student.getAvatar() != null && !student.getAvatar().isEmpty()) {
				var resource = "src/main/resources/avatar/" + student.getAvatar();
				if (resource != null) {
					avatarIcon = new ImageIcon(
							new ImageIcon(resource).getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH));
				}
			}

			if (avatarIcon == null) {
				// Sử dụng ảnh mặc định nếu không tìm thấy avatar
				var defaultResource = getClass().getClassLoader().getResource("avatar/default_avatar.png");
				if (defaultResource != null) {
					avatarIcon = new ImageIcon(
							new ImageIcon(defaultResource).getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH));
				}
			}

			// Thêm dòng vào bảng
			tableModel.addRow(new Object[] { student.getStudentID(), avatarIcon, student.getStudentCode(),
					student.getFullName(), student.getTotalBooksRented(), student.getLateReturnsCount(),
					student.getDamagedBooksCount(), student.getTotalOrders(), student.isPayment(), });
		}

		// Hiển thị thông tin số trang và số dòng
		pageInfo.setText("Page " + currentPage + " / " + ((filteredStudents.size() + rowsPerPage - 1) / rowsPerPage)
				+ " | Total Rows: " + filteredStudents.size());
		
	}

	private void goToPreviousPage() {
		if (currentPage > 1) {
			currentPage--;
			pageInput.setText(String.valueOf(currentPage));
			updateTable();
		}
	}

	private void goToNextPage() {
		if (currentPage * rowsPerPage < filteredStudents.size()) {
			currentPage++;
			pageInput.setText(String.valueOf(currentPage));
			updateTable();
		}
	}

	private void goToPage() {
		try {
			var page = Integer.parseInt(pageInput.getText());
			var totalPages = (filteredStudents.size() + rowsPerPage - 1) / rowsPerPage;

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
			var selectedRow = tableStudent.getSelectedRow();
			if (selectedRow != -1) {
				var studentID = (int) tableModel.getValueAt(selectedRow, 0); // Lấy StudentID từ cột ẩn
				var selectedStudent = students.stream().filter(student -> student.getStudentID() == studentID)
						.findFirst();

				selectedStudent.ifPresent(student -> {
					// Đóng bảng hiện tại nếu có
					if (currentStudentViewDialog != null && currentStudentViewDialog.isShowing()) {
						currentStudentViewDialog.dispose();
					}

					// Hiển thị bảng mới và lưu tham chiếu
					currentStudentViewDialog = new StudentViewDialog(student);
					currentStudentViewDialog.setVisible(true);
				});
			} else {
				JOptionPane.showMessageDialog(null, "Please select a student first!", "Warning",
						JOptionPane.WARNING_MESSAGE);
			}
		});

		// Menu item để chỉnh sửa
		var editItem = new JMenuItem("Edit");
		editItem.addActionListener(event -> {
			var selectedRow = tableStudent.getSelectedRow();
			if (selectedRow != -1) {
				var studentCode = tableModel.getValueAt(selectedRow, 2).toString();
				var studentDao = new StudentDao();
				var student = studentDao.selectByStudentCd(studentCode);
				var accountCreatorName = studentDao.getUserNameById(student.getUserID());

				if (currentStudentEditDialog != null && currentStudentEditDialog.isShowing()) {
					currentStudentEditDialog.dispose();
				}

				currentStudentEditDialog = new StudentEdit();
				currentStudentEditDialog.setStudentData(student, accountCreatorName, this);
				currentStudentEditDialog.setLocationRelativeTo(null); // Hiển thị ở giữa màn hình
				currentStudentEditDialog.setVisible(true);
			} else {
				JOptionPane.showMessageDialog(null, "Please select a row to edit!", "Warning",
						JOptionPane.WARNING_MESSAGE);
			}
		});

		// Menu item để xóa
		var deleteItem = new JMenuItem("Delete");
		deleteItem.setEnabled(!ispayment);
		deleteItem.addActionListener(this::deleteRow);

		// Thêm các item vào menu chuột phải
		popup.add(viewDetailsItem);
		popup.add(editItem); // Thêm mục Edit
		popup.add(deleteItem);

		// Hiển thị popup tại vị trí chuột
		popup.show(tableStudent, e.getX(), e.getY());
	}

	private void deleteRow(ActionEvent e) {
	    var selectedRow = tableStudent.getSelectedRow();
	    if (selectedRow == -1) {
	        JOptionPane.showMessageDialog(null, "Please select a row to delete!", "Warning",
	                JOptionPane.WARNING_MESSAGE);
	        return;
	    }

	    var result = JOptionPane.showConfirmDialog(null, "Are you sure you want to mark this row as deleted?", "Delete",
	            JOptionPane.YES_NO_OPTION);
	    if (result == JOptionPane.YES_OPTION) {
	        try {
	            var dao = new StudentDao();
	            var studentID = (int) tableModel.getValueAt(selectedRow, 0); // Lấy StudentID từ cột ẩn
	            dao.delete(studentID); // Gọi Stored Procedure để cập nhật IsDeleted = 1

	            // Đồng bộ hóa danh sách `students` và `filteredStudents`
	            var studentToDelete = filteredStudents.stream()
	                    .filter(student -> student.getStudentID() == studentID)
	                    .findFirst()
	                    .orElse(null);
	            if (studentToDelete != null) {
	                students.remove(studentToDelete);
	                filteredStudents.remove(studentToDelete);
	            }

	            // Cập nhật lại bảng
	            updateTable();
	            JOptionPane.showMessageDialog(null, "Student marked as deleted successfully!");
	        } catch (Exception ex) {
	            ex.printStackTrace();
	            JOptionPane.showMessageDialog(null, "Delete failed: " + ex.getMessage(), "Error",
	                    JOptionPane.ERROR_MESSAGE);
	        }
	    }
	}


	protected void btnDeleteActionPerformed(ActionEvent e) {
	    if (currRow >= 0 && currRow < tableStudent.getRowCount()) {
	        tableStudent.setRowSelectionInterval(currRow, currRow); // Chọn hàng được nhấn
	        deleteRow(e);
	    } else {
	        JOptionPane.showMessageDialog(null, "Please, choose a row first", "Error", JOptionPane.ERROR_MESSAGE);
	    }
	}


	protected void btnViewActionPerformed(ActionEvent e) {
		if (currRow >= 0 && currRow < tableStudent.getRowCount()) {
			var studentID = (int) tableModel.getValueAt(currRow, 0); // Lấy StudentID từ cột ẩn
			var selectedStudent = students.stream().filter(student -> student.getStudentID() == studentID).findFirst();

			selectedStudent.ifPresent(student -> {
				// Đóng bảng hiện tại nếu có
				if (currentStudentViewDialog != null && currentStudentViewDialog.isShowing()) {
					currentStudentViewDialog.dispose();
				}

				// Hiển thị bảng mới và lưu tham chiếu
				currentStudentViewDialog = new StudentViewDialog(student);
				currentStudentViewDialog.setVisible(true);
			});
		} else {
			JOptionPane.showMessageDialog(null, "Please select a student first!", "Warning",
					JOptionPane.WARNING_MESSAGE);
		}
	}

	protected void btnEditActionPerformed(ActionEvent e) {
		var selectedRow = tableStudent.getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(null, "Please select a row to edit!", "Warning", JOptionPane.WARNING_MESSAGE);
			return;
		}

		// Lấy StudentCode từ hàng được chọn
		var studentCode = tableModel.getValueAt(selectedRow, 2).toString();

		// Lấy dữ liệu sinh viên từ DAO
		var studentDao = new StudentDao();
		var student = studentDao.selectByStudentCd(studentCode);

		// Lấy tên Account Creator từ UserID
		var accountCreatorName = studentDao.getUserNameById(student.getUserID());

		// Đóng bảng hiện tại nếu có
		if (currentStudentEditDialog != null && currentStudentEditDialog.isShowing()) {
			currentStudentEditDialog.dispose();
		}

		// Tạo bảng mới và hiển thị
		currentStudentEditDialog = new StudentEdit();
		currentStudentEditDialog.setStudentData(student, accountCreatorName, this);
		currentStudentEditDialog.setLocationRelativeTo(null); // Hiển thị ở giữa màn hình
		currentStudentEditDialog.setVisible(true);
	}

	public void reloadTable() {

		// Tải lại dữ liệu sinh viên từ database
		loadStudentData();
		// Cập nhật lại bảng
		updateTable();

	}

	protected void btnAddActionPerformed(ActionEvent e) {
		// Đóng các bảng khác nếu có
		if (currentStudentViewDialog != null && currentStudentViewDialog.isShowing()) {
			currentStudentViewDialog.dispose(); // Đóng StudentViewDialog nếu đang hiển thị
		}

		if (currentStudentEditDialog != null && currentStudentEditDialog.isShowing()) {
			currentStudentEditDialog.dispose(); // Đóng StudentEdit nếu đang hiển thị
		}

		// Kiểm tra nếu AddStudent đang mở
		if (currentAddStudentDialog != null && currentAddStudentDialog.isShowing()) {
			currentAddStudentDialog.toFront(); // Đưa bảng AddStudent hiện tại lên trước
			return; // Không tạo bảng mới
		}

		// Tạo và hiển thị AddStudent
		currentAddStudentDialog = new AddStudent(this); // Truyền tham chiếu form cha
		currentAddStudentDialog.setLocationRelativeTo(null); // Hiển thị ở giữa màn hình
		currentAddStudentDialog.setVisible(true); // Hiển thị bảng AddStudent
	}

	protected void btnExcelActionPerformed(ActionEvent e) {
		try {
			// Lấy toàn bộ danh sách sinh viên từ cơ sở dữ liệu
			var dao = new StudentDao();
			var allStudents = dao.select();

			if (allStudents.isEmpty()) {
				JOptionPane.showMessageDialog(this, "No data to export!", "Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}

			// Tạo file Excel
			Workbook workbook = new XSSFWorkbook();
			var sheet = workbook.createSheet("Students");

			// Tạo hàng tiêu đề
			var headerRow = sheet.createRow(0);
			String[] headers = { "StudentID", "StudentCode", "FullName", "DateOfBirth", "Gender", "Email",
					"PhoneNumber", "Address", "GraduationYear", "SchoolName", "Books Rented", "Late Returns",
					"Damaged Books", "Orders" };

			for (var i = 0; i < headers.length; i++) {
				var cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);
			}

			// Thêm dữ liệu từ danh sách sinh viên
			var rowNum = 1;
			for (StudentEntity student : allStudents) {
				var row = sheet.createRow(rowNum++);
				row.createCell(0).setCellValue(student.getStudentID());
				row.createCell(1).setCellValue(student.getStudentCode());
				row.createCell(2).setCellValue(student.getFullName());
				row.createCell(3)
						.setCellValue(student.getDateOfBirth() != null ? student.getDateOfBirth().toString() : "");
				row.createCell(4).setCellValue(student.getGender() != null ? student.getGender() : "");
				row.createCell(5).setCellValue(student.getEmail() != null ? student.getEmail() : "");
				row.createCell(6).setCellValue(student.getPhoneNumber() != null ? student.getPhoneNumber() : "");
				row.createCell(7).setCellValue(student.getAddress() != null ? student.getAddress() : "");
				row.createCell(8).setCellValue(student.getEnrollmentYear());
				row.createCell(9).setCellValue(student.getSchoolName());
				row.createCell(10).setCellValue(student.getTotalBooksRented());
				row.createCell(11).setCellValue(student.getLateReturnsCount());
				row.createCell(12).setCellValue(student.getDamagedBooksCount());
				row.createCell(13).setCellValue(student.getTotalOrders());
			}

			// Lưu file Excel
			var filePath = System.getProperty("user.home") + "/Documents/Students.xlsx";
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
