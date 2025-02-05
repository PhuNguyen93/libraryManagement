package main;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
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
import javax.swing.SortOrder;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import component.CustomScrollBarUI;
import component.RoundedPanel;
import component.TableStatus;
import dao.BookManagementDao;
import dao.StudentDao;
import dao.UserDao;
import edit.UserEdit;
import entity.UserEntity;
import view.UserViewDialog;

public class Personnel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTable tablePersonnel;
	private DefaultTableModel tableModel;
	private JTextField pageInput;
	private JLabel pageInfo;
	private JTextField searchField;
	private int currentPage = 1;
	private final int rowsPerPage = 5;
	private List<UserEntity> user;
	private List<UserEntity> filteredUsers;
	private UserEdit userEdit;
	private JButton btnView;
	private int currRow;
	private UserViewDialog currentUserViewDialog;

	public Personnel() {
		setBackground(new Color(244, 243, 240));
		setBounds(0, 0, 946, 706);
		setLayout(null);

		// Search field
		searchField = new JTextField("Search by name or email...");
		searchField.setBounds(75, 27, 796, 29);
		searchField.setForeground(Color.GRAY);
		searchField.setFont(new Font("Arial", Font.PLAIN, 16));
		searchField.setBackground(Color.WHITE);
		searchField.setBorder(null);
		searchField.addFocusListener(new java.awt.event.FocusAdapter() {
			@Override
			public void focusGained(java.awt.event.FocusEvent e) {
				if (searchField.getText().equals("Search by name or email...")) {
					searchField.setText("");
					searchField.setForeground(Color.BLACK);
				}
			}

			@Override
			public void focusLost(java.awt.event.FocusEvent e) {
				if (searchField.getText().isEmpty()) {
					searchField.setText("Search by name or email...");
					searchField.setForeground(Color.GRAY);
				}
			}
		});
		searchField.addKeyListener(new java.awt.event.KeyAdapter() {
			@Override
			public void keyReleased(java.awt.event.KeyEvent e) {
				filterUsers();
			}
		});
		add(searchField);

		// Table container panel
		// Phần panel chứa bảng sử dụng RoundedPanel
		var panelTable = new RoundedPanel(15);
		panelTable.setForeground(new Color(0, 0, 0));
		panelTable.setBackground(new Color(255, 255, 255)); // Màu nền trắng
		panelTable.setBorder(null);
		panelTable.setBounds(23, 150, 897, 545);
		panelTable.setLayout(null);
		add(panelTable);

		// Tùy chỉnh JTable
		tablePersonnel = new JTable();
		tablePersonnel.setAutoCreateRowSorter(true);
		tablePersonnel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tablePersonnel.setShowVerticalLines(false);
		tablePersonnel.setFont(new Font("Arial", Font.PLAIN, 14));
		tablePersonnel.setRowHeight(80); // Tăng chiều cao hàng để hiển thị hình ảnh
		tablePersonnel.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mousePressed(MouseEvent e) {
		        if (e.getButton() == MouseEvent.BUTTON3) {
		            var row = tablePersonnel.rowAtPoint(e.getPoint());
		            if (row >= 0 && row < tablePersonnel.getRowCount()) {
		                tablePersonnel.setRowSelectionInterval(row, row);
		                showPopupMenu(e);
		            }
		        } else if (e.getButton() == MouseEvent.BUTTON1) {
		            currRow = tablePersonnel.rowAtPoint(e.getPoint());
		        }
		    }
		});

		// Thiết lập model của bảng
		tableModel = new DefaultTableModel(
		    new String[] { "UserID", "Avatar", "Full Name", "Email", "Books Added", "Students Added", "Status" }, 0) {
		    private static final long serialVersionUID = 1L;

		    @Override
		    public Class<?> getColumnClass(int column) {
		        return switch (column) {
		            case 0 -> Integer.class; // UserID
		            case 1 -> ImageIcon.class; // Avatar
		            case 2, 3, 6 -> String.class; // Full Name, Email, Status
		            case 4, 5 -> Integer.class; // Books Added, Students Added
		            default -> Object.class;
		        };
		    }

		    @Override
		    public boolean isCellEditable(int row, int column) {
		        return false; // Không cho phép chỉnh sửa dữ liệu trong bảng
		    }
		};

		tablePersonnel.setModel(tableModel);
		tablePersonnel.getColumnModel().getColumn(6).setCellRenderer(new StatusRenderer());

		// Ẩn cột không cần thiết
		tablePersonnel.getColumnModel().getColumn(0).setMinWidth(0);
		tablePersonnel.getColumnModel().getColumn(0).setMaxWidth(0);
		tablePersonnel.getColumnModel().getColumn(0).setPreferredWidth(0);

		// Tùy chỉnh bảng
		customizeTable(tablePersonnel);

		// JScrollPane chứa JTable
		var scrollPane = new JScrollPane(tablePersonnel);
		scrollPane.setBounds(20, 11, 869, 452);

		// Đặt nền trắng cho JViewport
		scrollPane.getViewport().setBackground(Color.WHITE);

		// Loại bỏ viền của JScrollPane
		scrollPane.setBorder(BorderFactory.createEmptyBorder());

		// Sử dụng CustomScrollBarUI cho thanh cuộn
		scrollPane.getVerticalScrollBar().setUI(new CustomScrollBarUI());
		scrollPane.getHorizontalScrollBar().setUI(new CustomScrollBarUI());

		panelTable.add(scrollPane);

		// Nút điều hướng phân trang
		var btnPrevious = new JButton(new ImageIcon(Personnel.class.getResource("/iconNext/previous.png")));
		btnPrevious.setBounds(322, 488, 65, 30);
		btnPrevious.addActionListener(e -> goToPreviousPage());
		panelTable.add(btnPrevious);

		pageInput = new JTextField(String.valueOf(currentPage));
		pageInput.setBounds(405, 488, 50, 30);
		pageInput.setHorizontalAlignment(JTextField.CENTER);
		pageInput.addActionListener(e -> goToPage());
		panelTable.add(pageInput);

		var btnNext = new JButton(new ImageIcon(Personnel.class.getResource("/iconNext/next-button.png")));
		btnNext.setBounds(473, 488, 65, 30);
		btnNext.addActionListener(e -> goToNextPage());
		panelTable.add(btnNext);

		pageInfo = new JLabel();
		pageInfo.setBounds(575, 488, 200, 30);
		panelTable.add(pageInfo);


		// Approve Button
		var btnApprove = new JButton("");
		btnApprove.setIcon(new ImageIcon(Personnel.class.getResource("/icon12/active-user.png")));
		btnApprove.setFont(new Font("Arial", Font.BOLD, 12));
		btnApprove.setBackground(new Color(240, 240, 240)); // Màu xanh dương
		btnApprove.setForeground(Color.WHITE);
		btnApprove.setBounds(23, 86, 59, 38);
		btnApprove.addActionListener(e -> approveSelectedRow());
		add(btnApprove);

		// Revoke Button
		var btnRevoke = new JButton("");
		btnRevoke.setIcon(new ImageIcon(Personnel.class.getResource("/icon12/user-lock.png"))); // Đặt icon cho nút Revoke
		btnRevoke.setFont(new Font("Arial", Font.BOLD, 12));
//		btnRevoke.setBackground(new Color(255, 69, 0)); // Màu đỏ cam
//		btnRevoke.setForeground(Color.WHITE);
		btnRevoke.setBounds(92, 86, 59, 38);
		btnRevoke.addActionListener(e -> revokeSelectedRow());
		add(btnRevoke);

		// Edit Button
		var btnEdit = new JButton("");
		btnEdit.setIcon(new ImageIcon(Personnel.class.getResource("/icon3/pen.png"))); // Đặt icon cho nút Edit
		btnEdit.setFont(new Font("Arial", Font.BOLD, 12));
//		btnEdit.setBackground(new Color(255, 140, 0)); // Màu cam
//		btnEdit.setForeground(Color.WHITE);
		btnEdit.setBounds(161, 86, 59, 38);
		btnEdit.addActionListener(e -> editSelectedRow());
		add(btnEdit);

		btnView = new JButton("");
		btnView.setIcon(new ImageIcon(Personnel.class.getResource("/icon3/file.png"))); // Đặt icon cho nút Edit
		btnView.addActionListener(e -> viewSelectedRow(e));
		btnView.setFont(new Font("Arial", Font.BOLD, 12));
//		btnView.setBackground(new Color(255, 140, 0));
		btnView.setBounds(230, 87, 59, 38);
		add(btnView);

		// Load data into the table
		loadPersonnelData();
		updateTable();
	}

	protected void showPopupMenu(MouseEvent e) {
		var popupMenu = new JPopupMenu();

		// Menu item: Approve
		var approveItem = new JMenuItem("Approve");
		approveItem.addActionListener(event -> approveSelectedRow());

		// Menu item: Revoke
		var revokeItem = new JMenuItem("Revoke");
		revokeItem.addActionListener(event -> revokeSelectedRow());

		// Menu item: Edit
		var editItem = new JMenuItem("Edit");
		editItem.addActionListener(event -> editSelectedRow());

		// Menu item: View
		var viewItem = new JMenuItem("View");
		viewItem.addActionListener(event -> viewSelectedRow(null));

		// Add items to popup menu
		popupMenu.add(approveItem);
		popupMenu.add(revokeItem);
		popupMenu.add(editItem);
		popupMenu.add(viewItem);

		// Hiển thị menu tại vị trí chuột
		popupMenu.show(tablePersonnel, e.getX(), e.getY());
	}

	@Override
	protected void paintComponent(Graphics grphcs) {
		super.paintComponent(grphcs);
		var g2 = (Graphics2D) grphcs;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// Vẽ nền trắng cho thanh tìm kiếm
		g2.setColor(Color.WHITE);
		g2.fillRoundRect(10, 20, 926, 44, 15, 15);

		// Vẽ viền cho thanh tìm kiếm
		g2.setColor(new Color(240, 240, 240));
		g2.drawRoundRect(10, 20, 926, 44, 15, 15);
	}

	public class StatusRenderer extends DefaultTableCellRenderer {
	    private static final long serialVersionUID = 1L;

	    @Override
	    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
	            boolean hasFocus, int row, int column) {
	        JLabel label = new JLabel(value.toString(), SwingConstants.CENTER);
	        label.setOpaque(true);
	        label.setFont(new Font("Arial", Font.BOLD, 12));
	        label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

	        if ("Approved".equalsIgnoreCase(value.toString())) {
	            label.setBackground(new Color(34, 139, 34));
	            label.setForeground(Color.WHITE);
	        } else if ("Pending".equalsIgnoreCase(value.toString())) {
	            label.setBackground(new Color(255, 165, 0));
	            label.setForeground(Color.BLACK);
	        }

	        if (isSelected) {
	            label.setBackground(table.getSelectionBackground());
	            label.setForeground(table.getSelectionForeground());
	        }

	        return label;
	    }
	}
	// Thêm renderer cho cột Status
	private void customizeTable(JTable table) {
	    table.setFont(new Font("Arial", Font.PLAIN, 14));
	    table.setRowHeight(80); // Chiều cao hàng
	    table.setGridColor(new Color(224, 224, 224)); // Màu lưới

	    // Center align cells (trừ cột Avatar và Status)
	    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
	    centerRenderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);

	    for (int i = 0; i < table.getColumnCount(); i++) {
	        if (i != 1 && i != 6) { // Không căn giữa cột Avatar và Status
	            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
	        }
	    }

	    // Avatar renderer
	    table.getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
	        private static final long serialVersionUID = 1L;

	        @Override
	        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
	                boolean hasFocus, int row, int column) {
	            if (value instanceof ImageIcon) {
	                JLabel label = new JLabel((ImageIcon) value, JLabel.CENTER);
	                label.setOpaque(true);
	                label.setBackground(isSelected ? table.getSelectionBackground() : Color.WHITE);
	                return label;
	            }
	            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	        }
	    });
	    
	    // Cung cấp comparator cho cột Status
	    TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
	    sorter.setComparator(6, Comparator.comparing(Object::toString)); // Sắp xếp dựa trên chuỗi
	    table.setRowSorter(sorter);

	    table.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
	        private static final long serialVersionUID = 1L;

	        @Override
	        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
	                boolean hasFocus, int row, int column) {
	            // Tạo JPanel để chứa JLabel
	            JPanel panel = new JPanel();
	            panel.setOpaque(true);
	            panel.setBackground(isSelected ? table.getSelectionBackground() : Color.WHITE);
	            panel.setLayout(new java.awt.GridBagLayout()); // Sử dụng GridBagLayout để căn giữa

	            // Tạo JLabel cho trạng thái
	            JLabel label = new JLabel(value.toString(), SwingConstants.CENTER);
	            label.setOpaque(true);
	            label.setFont(new Font("Arial", Font.BOLD, 12));
	            label.setHorizontalAlignment(SwingConstants.CENTER);
	            label.setVerticalAlignment(SwingConstants.CENTER);
	            label.setPreferredSize(new Dimension(100, 40)); // Đặt kích thước cố định
	            label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Padding xung quanh

	            // Áp dụng màu nền và màu chữ dựa trên trạng thái
	            if ("Approved".equalsIgnoreCase(value.toString())) {
	                label.setBackground(Color.decode("#CCCCFF")); // Màu #CCCCFF
	                label.setForeground(Color.BLACK); // Chữ trắng
	            } else if ("Pending".equalsIgnoreCase(value.toString())) {
	                label.setBackground(Color.decode("#FF6666")); // Màu #FF6666
	                label.setForeground(Color.BLACK); // Chữ trắng
	            }

	            // Thêm JLabel vào JPanel và căn giữa
	            panel.add(label);
	            return panel;
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
	            var headerRenderer = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	            headerRenderer.setBackground(Color.WHITE); // Nền trắng
	            headerRenderer.setForeground(Color.BLACK); // Chữ đen
	            headerRenderer.setFont(new Font("Arial", Font.BOLD, 16)); // Font Arial, đậm, cỡ 16
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
	 // Thêm sự kiện sắp xếp cho tiêu đề
	    table.getTableHeader().addMouseListener(new MouseAdapter() {
	        @Override
	        public void mouseClicked(MouseEvent e) {
	            int columnIndex = table.columnAtPoint(e.getPoint()); // Lấy chỉ số cột được nhấp
	            if (columnIndex != -1) {
	                sortByColumn(columnIndex);
	            }
	        }
	    });
	}
	private void sortByColumn(int columnIndex) {
	    // Lấy comparator tùy thuộc vào cột được nhấp
	    Comparator<UserEntity> comparator = switch (columnIndex) {
	        case 2 -> Comparator.comparing(UserEntity::getFullName, String.CASE_INSENSITIVE_ORDER); // Full Name
	        case 3 -> Comparator.comparing(UserEntity::getEmail, String.CASE_INSENSITIVE_ORDER);    // Email
	        case 4 -> Comparator.comparingInt(user -> { // Books Added
	            var bookManagementDao = new BookManagementDao();
	            return bookManagementDao.countBooksByUserId(user.getUserID());
	        });
	        case 5 -> Comparator.comparingInt(user -> { // Students Added
	            var studentDao = new StudentDao();
	            return studentDao.countStudentsByUserId(user.getUserID());
	        });
	        case 6 -> Comparator.comparing(user -> user.isActive() ? "Approved" : "Pending");      // Status
	        default -> Comparator.comparingInt(UserEntity::getUserID);                            // Default: UserID
	    };

	    // Đảo ngược thứ tự nếu đã sắp xếp
	    boolean ascending = true;
	    if (tablePersonnel.getRowSorter() != null && tablePersonnel.getRowSorter().getSortKeys() != null) {
	        var sortKey = tablePersonnel.getRowSorter().getSortKeys().stream()
	                .filter(key -> key.getColumn() == columnIndex).findFirst();
	        if (sortKey.isPresent() && sortKey.get().getSortOrder() == SortOrder.ASCENDING) {
	            ascending = false;
	        }
	    }

	    // Sắp xếp toàn bộ danh sách `filteredUsers`
	    filteredUsers.sort(ascending ? comparator : comparator.reversed());

	    // Quay về trang đầu tiên
	    currentPage = 1;

	    // Cập nhật hiển thị bảng
	    pageInput.setText(String.valueOf(currentPage));
	    updateTable();
	}



	private void loadPersonnelData() {
		var userDao = new UserDao();
		user = userDao.selectAll();
		filteredUsers = user; // Initially, display all users
	}

	public void reloadTable() {
		loadPersonnelData(); // Load lại dữ liệu từ DAO
		updateTable(); // Cập nhật hiển thị trên giao diện
	}

	private void updateTable() {
	    tableModel.setRowCount(0); // Xóa tất cả hàng cũ

	    var studentDao = new StudentDao();
	    var bookManagementDao = new BookManagementDao();

	    var start = (currentPage - 1) * rowsPerPage;
	    var end = Math.min(start + rowsPerPage, filteredUsers.size());

	    for (var i = start; i < end; i++) {
	        var user = filteredUsers.get(i);

	        // Lấy số lượng sinh viên và sách do user này thêm vào
	        var studentsAdded = studentDao.countStudentsByUserId(user.getUserID());
	        var booksAdded = bookManagementDao.countBooksByUserId(user.getUserID());

	        ImageIcon avatarIcon = null;
	        if (user.getAvatar() != null && !user.getAvatar().isEmpty()) {
	            avatarIcon = new ImageIcon(new ImageIcon("src/main/resources/avatar/" + user.getAvatar()).getImage()
	                    .getScaledInstance(70, 70, Image.SCALE_SMOOTH));
	        }
	        if (avatarIcon == null) {
	            avatarIcon = new ImageIcon(new ImageIcon("src/main/resources/avatar/default_avatar.png").getImage()
	                    .getScaledInstance(70, 70, Image.SCALE_SMOOTH));
	        }

	        // Thêm hàng mới vào bảng, bao gồm cột Email
	        tableModel.addRow(new Object[] {
	            user.getUserID(),
	            avatarIcon,
	            user.getFullName(),
	            user.getEmail(), // Cột Email
	            booksAdded, 
	            studentsAdded, 
	            user.isActive() ? TableStatus.StatusType.APPROVED : TableStatus.StatusType.PENDING
	        });
	    }

	    // Cập nhật thông tin phân trang
	    var totalRows = filteredUsers.size();
	    pageInfo.setText("Page " + currentPage + " / " + ((totalRows + rowsPerPage - 1) / rowsPerPage)
	            + " | Total Rows: " + totalRows);
	}


	private void filterUsers() {
		var searchText = searchField.getText().trim().toLowerCase();

		if (searchText.equals("search by name or email...") || searchText.isEmpty()) {
			filteredUsers = user;
		} else {
			filteredUsers = user.stream().filter(user -> user.getFullName().toLowerCase().contains(searchText)
					|| user.getEmail().toLowerCase().contains(searchText)).toList();
		}

		currentPage = 1;
		pageInput.setText(String.valueOf(currentPage));
		updateTable();
	}

	private void goToPreviousPage() {
		if (currentPage > 1) {
			currentPage--;
			pageInput.setText(String.valueOf(currentPage));
			updateTable();
		}
	}

	private void goToNextPage() {
		var totalPages = (filteredUsers.size() + rowsPerPage - 1) / rowsPerPage;
		if (currentPage < totalPages) {
			currentPage++;
			pageInput.setText(String.valueOf(currentPage));
			updateTable();
		}
	}

	private void goToPage() {
		try {
			var page = Integer.parseInt(pageInput.getText());
			var totalPages = (filteredUsers.size() + rowsPerPage - 1) / rowsPerPage;

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

	private void approveSelectedRow() {
	    var selectedRow = tablePersonnel.getSelectedRow();
	    if (selectedRow == -1) {
	        JOptionPane.showMessageDialog(this, "Please select a row to approve!", "Warning",
	                JOptionPane.WARNING_MESSAGE);
	        return;
	    }

	    // Chuyển đổi chỉ số hiển thị sang chỉ số mô hình
	    var modelRow = tablePersonnel.convertRowIndexToModel(selectedRow);

	    var userId = (int) tableModel.getValueAt(modelRow, 0); // Lấy UserID từ model
	    var email = tableModel.getValueAt(modelRow, 3).toString(); // Lấy email từ model
	    var status = tableModel.getValueAt(modelRow, 6).toString(); // Lấy trạng thái hiện tại từ model

	    if ("Approved".equalsIgnoreCase(status)) {
	        JOptionPane.showMessageDialog(this, "This user is already approved.", "Info",
	                JOptionPane.INFORMATION_MESSAGE);
	        return;
	    }

	    // Cập nhật trạng thái trong cơ sở dữ liệu
	    var userDao = new UserDao();
	    boolean isUpdated = userDao.updateUserStatus(userId, true); // Lưu trạng thái "Approved" vào DB
	    if (isUpdated) {
	        // Cập nhật trạng thái trong danh sách filteredUsers
	        filteredUsers.stream()
	                .filter(user -> user.getUserID() == userId)
	                .forEach(user -> user.setActive(true));

	        // Cập nhật trạng thái trong danh sách user nếu cần
	        user.stream()
	                .filter(u -> u.getUserID() == userId)
	                .forEach(u -> u.setActive(true));

	        // Cập nhật trạng thái trong bảng
	        tableModel.setValueAt("Approved", modelRow, 6); // Sử dụng modelRow để cập nhật
	        JOptionPane.showMessageDialog(this, "User approved successfully!");

	        // Gửi email xác nhận
	        sendConfirmationEmail(email);
	    } else {
	        JOptionPane.showMessageDialog(this, "Failed to update user status in the database.", "Error",
	                JOptionPane.ERROR_MESSAGE);
	    }
	}


	private void revokeSelectedRow() {
	    var selectedRow = tablePersonnel.getSelectedRow();
	    if (selectedRow == -1) {
	        JOptionPane.showMessageDialog(this, "Please select a row to revoke!", "Warning",
	                JOptionPane.WARNING_MESSAGE);
	        return;
	    }

	    // Chuyển đổi chỉ số hiển thị sang chỉ số mô hình
	    var modelRow = tablePersonnel.convertRowIndexToModel(selectedRow);

	    var userId = (int) tableModel.getValueAt(modelRow, 0); // Lấy UserID từ model
	    var status = tableModel.getValueAt(modelRow, 6).toString(); // Lấy trạng thái hiện tại từ model

	    if ("Pending".equalsIgnoreCase(status)) {
	        JOptionPane.showMessageDialog(this, "This user is already in Pending status.", "Info",
	                JOptionPane.INFORMATION_MESSAGE);
	        return;
	    }

	    var userDao = new UserDao();
	    boolean isUpdated = userDao.updateUserStatus(userId, false); // Chuyển trạng thái sang "Pending"

	    if (isUpdated) {
	        tableModel.setValueAt("Pending", modelRow, 6); // Cập nhật trạng thái trong bảng
	        JOptionPane.showMessageDialog(this, "User status revoked successfully!");
	        reloadTable(); // Cập nhật lại bảng để đồng bộ với dữ liệu mới
	    } else {
	        JOptionPane.showMessageDialog(this, "Failed to revoke user status. Please try again.", "Error",
	                JOptionPane.ERROR_MESSAGE);
	    }
	}


	private void editSelectedRow() {
		var selectedRow = tablePersonnel.getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(this, "Please select a row to edit!", "Warning", JOptionPane.WARNING_MESSAGE);
			return;
		}

		var userId = (int) tableModel.getValueAt(selectedRow, 0); // Lấy UserID từ bảng

		var userDao = new UserDao();
		var user = userDao.getUserById(userId);

		if (user == null) {
			JOptionPane.showMessageDialog(this, "User not found!", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (userEdit != null && userEdit.isShowing()) {
			userEdit.dispose();
		}

		userEdit = new UserEdit();
		userEdit.setUserData(user);

		userEdit.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosed(java.awt.event.WindowEvent e) {
				loadPersonnelData();
				updateTable();
			}
		});
		userEdit.setVisible(true);

//		loadPersonnelData();
//		updateTable();
	}

	private void viewSelectedRow(ActionEvent e) {
		if (currRow >= 0 && currRow < tablePersonnel.getRowCount()) {
			var userId = (int) tableModel.getValueAt(currRow, 0); // Lấy UserID từ bảng
			var selectedUser = user.stream().filter(user -> user.getUserID() == userId).findFirst();

			selectedUser.ifPresent(user -> {
				// Đóng dialog hiện tại nếu có
				if (currentUserViewDialog != null && currentUserViewDialog.isShowing()) {
					currentUserViewDialog.dispose();
				}

				// Hiển thị dialog mới
				currentUserViewDialog = new UserViewDialog(user);
				currentUserViewDialog.setVisible(true);
			});
		} else {
			JOptionPane.showMessageDialog(null, "Please select a user first!", "Warning", JOptionPane.WARNING_MESSAGE);
		}
	}

	private void sendConfirmationEmail(String recipientEmail) {
		final var senderEmail = "nguyenphu0809@gmail.com"; // Địa chỉ email người gửi
		final var senderPassword = "yykh yooo tfnt wgmt"; // Mật khẩu email người gửi (hoặc App Password nếu dùng
															// Gmail)
		final var host = "smtp.gmail.com"; // Host SMTP (dùng Gmail)

		var props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", "587"); // Cổng SMTP

		var session = Session.getInstance(props, new javax.mail.Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(senderEmail, senderPassword);
			}
		});

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(senderEmail)); // Đặt email người gửi
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail)); // Đặt email người
																									// nhận
			message.setSubject("Registration Approved"); // Chủ đề email
			message.setText(
					"Dear user,\n\nYour registration has been approved successfully!\n\nBest regards,\nYour Team"); // Nội
																													// dung
																													// email

			Transport.send(message); // Gửi email

			JOptionPane.showMessageDialog(this, "Confirmation email sent to " + recipientEmail);
		} catch (MessagingException e) {
			JOptionPane.showMessageDialog(this, "Failed to send email: " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
}