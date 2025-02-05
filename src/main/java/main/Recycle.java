package main;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import dao.AuthorDao;
import dao.BookManagementDao;
import dao.CategoriesDao;
import dao.PublisherDao;
import dao.StudentDao;
import dao.UserDao;
import entity.AuthorEntity;
import entity.BookManagementEntity;
import entity.StudentEntity;
import gui.Main;
import service.ConnectDB;
import java.awt.event.ActionListener;

public class Recycle extends JPanel {

	private static final long serialVersionUID = 1L;
	private JScrollPane scrollPaneAuthor;
	private JTable tableAuthor;
	private JTabbedPane tabbedPane;
	private JButton btnRestoreAuth;
	private JTextField searchAuthor;
	private JScrollPane scrollPanePublisher;
	private JTable tablePublisher;
	private JTextField searchPublisher; // Thanh tìm kiếm cho Publisher
	private JScrollPane scrollPaneCategory; // ScrollPane cho Category
	private JTable tableCategory; // Table cho Category
	private JTextField searchCategory; // Thanh tìm kiếm cho Category
	private JButton btnSearchCategory; // Nút tìm kiếm cho Category
	private JButton btnDeleteAuth1;
	private JButton btnDeletePub;

	private JScrollPane scrollPaneUser; // ScrollPane cho User
	private JTable tableUser; // Table để hiển thị danh sách User
	private JTextField searchUser; // Thanh tìm kiếm cho User
	private JButton btnSearchUser; // Nút tìm kiếm cho User
	private JButton btnAddUser; // Nút thêm mới User
	private JButton btnDeleteUser; // Nút xóa User
	private JButton btnRestoreUser; // Nút khôi phục User đã xóa

	private JPanel panelStudent;
	private JScrollPane scrollPaneStudent;
	private JTable tableStudent;
	private JTextField searchStudent;
	private JButton btnRestoreStudent;
	private JButton btnDeleteStudent1;
	private JButton btnSearchStudent;

	private JPanel panelBook;
	private JScrollPane scrollPaneBook;
	private JTable tableBook;
	private JTextField searchBook;
	private JButton btnRestoreBook;
	private JButton btnDeleteBook1;
	private JButton btnSearchBook;
	private boolean isSelected = true;
	private JButton btnAddAuthor_2_1;

	public Recycle() {
		setBackground(new Color(244, 243, 240));
		setBounds(0, 0, 946, 706);
		setLayout(null);

		tabbedPane = new JTabbedPane();
		tabbedPane.setBounds(20, 11, 906, 679);

		// Panel Category
		var panelCategory = new JPanel();
		panelCategory.setBackground(Color.WHITE);
		panelCategory.setLayout(null);

		//

		// Tab Author
		var tab3Icon = new ImageIcon(getClass().getResource("/hinh/8.png"));

		// Tab Publisher
		var tab2Icon = new ImageIcon(getClass().getResource("/hinh/5.png"));

		// Tab Category
		var tabCategoryIcon = new ImageIcon(getClass().getResource("/hinh/6.png"));

		// Panel Author
		var panelAuthor = new JPanel();
		panelAuthor.setBorder(null);
		panelAuthor.setBackground(Color.WHITE);
		panelAuthor.setLayout(null);
		tabbedPane.addTab("Author", tab3Icon, panelAuthor, "This is Author Management");

		scrollPaneAuthor = new JScrollPane();
		scrollPaneAuthor.setBorder(BorderFactory.createEmptyBorder()); // Remove border
		scrollPaneAuthor.getViewport().setBorder(null); // Remove viewport border
		scrollPaneAuthor.setBounds(0, 68, 901, 578);
		panelAuthor.add(scrollPaneAuthor);

		tableAuthor = new JTable();
		tableAuthor.setAutoCreateRowSorter(true);
		tableAuthor.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableAuthor.setBorder(null);
		tableAuthor.setShowVerticalLines(false);
		scrollPaneAuthor.setViewportView(tableAuthor);
		//
		btnRestoreAuth = new JButton("");
		btnRestoreAuth.addActionListener(e -> {
			var selectedRow = tableAuthor.getSelectedRow(); // Lấy dòng được chọn trong bảng
			if (selectedRow == -1) {
				JOptionPane.showMessageDialog(null, "Please select an author to restore!", "Warning",
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			// Lấy thông tin AuthorID từ bảng
			var authorID = (int) tableAuthor.getValueAt(selectedRow, 4); // Giả sử cột đầu tiên là AuthorID
			var authorName = (String) tableAuthor.getValueAt(selectedRow, 1);

			// Hỏi xác nhận
			var confirm = JOptionPane.showConfirmDialog(null,
					"Are you sure you want to restore the author: " + authorName + "?", "Confirm Restore",
					JOptionPane.YES_NO_OPTION);

			if (confirm == JOptionPane.YES_OPTION) {
				try (var con = ConnectDB.getCon();
						var ps = con.prepareStatement("UPDATE Authors SET IsDeleted = 0 WHERE AuthorID = ?")) {
					// Cập nhật IsDeleted = 0
					ps.setInt(1, authorID);
					var rowsAffected = ps.executeUpdate();

					if (rowsAffected > 0) {
						JOptionPane.showMessageDialog(null, "Author restored successfully!", "Success",
								JOptionPane.INFORMATION_MESSAGE);
						((DefaultTableModel) tableAuthor.getModel()).removeRow(selectedRow); // Xóa dòng khỏi bảng
					} else {
						JOptionPane.showMessageDialog(null, "Failed to restore author. Please try again!", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(null, "An error occurred: " + ex.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnRestoreAuth.setIcon(new ImageIcon(Recycle.class.getResource("/icon12/clock.png")));
		btnRestoreAuth.setBounds(14, 12, 62, 45);
		btnRestoreAuth.setToolTipText("Restore Author");
		panelAuthor.add(btnRestoreAuth);

		searchAuthor = new JTextField("Search by Author Name...");
		searchAuthor.setBounds(451, 12, 343, 45);
		searchAuthor.setForeground(Color.GRAY);
		searchAuthor.setFont(new Font("Arial", Font.PLAIN, 14));
		searchAuthor.setBackground(Color.WHITE);
		searchAuthor.setBorder(javax.swing.BorderFactory.createLineBorder(Color.GRAY, 1));
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
		panelAuthor.add(searchAuthor);

		var btnSearchAuthor = new JButton("Search");
		btnSearchAuthor.setBounds(804, 12, 81, 44);
		btnSearchAuthor.setBackground(new Color(63, 81, 181));
		btnSearchAuthor.setForeground(Color.BLACK);
		btnSearchAuthor.setFont(new Font("Arial", Font.BOLD, 14));
		btnSearchAuthor.addActionListener(this::onSearch);
		panelAuthor.add(btnSearchAuthor);

		btnDeleteAuth1 = new JButton("");
		btnDeleteAuth1.addActionListener(e -> {
			// Kiểm tra dòng được chọn
			var selectedRow = tableAuthor.getSelectedRow();
			if (selectedRow == -1) {
				JOptionPane.showMessageDialog(null, "Please select an author to delete!", "Warning",
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			// Lấy AuthorID và tên từ bảng
			var authorID = (int) tableAuthor.getValueAt(selectedRow, 4); // Giả sử cột đầu tiên là AuthorID
			var authorName = (String) tableAuthor.getValueAt(selectedRow, 1);

			// Xác nhận xóa
			var confirm = JOptionPane.showConfirmDialog(null,
					"Are you sure you want to delete the author: " + authorName + "?", "Confirm Delete",
					JOptionPane.YES_NO_OPTION);
			JOptionPane.showMessageDialog(null, authorID);
			if (confirm == JOptionPane.YES_OPTION) {
				try (var con = ConnectDB.getCon();
						var ps = con.prepareStatement("DELETE FROM Authors WHERE AuthorID = ?")) {
					// Gán giá trị cho tham số
					ps.setInt(1, authorID);

					// Thực hiện lệnh xóa
					var rowsAffected = ps.executeUpdate();

					// Kiểm tra kết quả
					if (rowsAffected > 0) {
						JOptionPane.showMessageDialog(null, "Author deleted successfully!", "Success",
								JOptionPane.INFORMATION_MESSAGE);
						((DefaultTableModel) tableAuthor.getModel()).removeRow(selectedRow); // Xóa dòng khỏi bảng
					} else {
						JOptionPane.showMessageDialog(null, "No author found to delete. Please try again!", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(null, "An error occurred: " + ex.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnDeleteAuth1.setToolTipText("Delete Author");
		btnDeleteAuth1.setIcon(new ImageIcon(Recycle.class.getResource("/icon3/bin.png")));
		btnDeleteAuth1.setBounds(91, 12, 62, 45);
		panelAuthor.add(btnDeleteAuth1);

		// Panel Publisher
		var panelPublisher = new JPanel();
		panelPublisher.setBackground(Color.WHITE);
		panelPublisher.setLayout(null);
		tabbedPane.addTab("Publisher", tab2Icon, panelPublisher, "This is Publisher Management");

		scrollPanePublisher = new JScrollPane();
		scrollPanePublisher.setBounds(0, 68, 901, 578);
		scrollPanePublisher.setBorder(BorderFactory.createEmptyBorder()); // Remove border
		scrollPanePublisher.getViewport().setBorder(null); // Remove viewport border
		panelPublisher.add(scrollPanePublisher);

		tablePublisher = new JTable();
		tablePublisher.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tablePublisher.setAutoCreateRowSorter(true);
		tablePublisher.setShowVerticalLines(false);
		scrollPanePublisher.setViewportView(tablePublisher);

		var btnRePub = new JButton("");
		btnRePub.addActionListener(e -> {
			var selectedRow = tablePublisher.getSelectedRow(); // Lấy dòng được chọn trong bảng
			if (selectedRow == -1) {
				JOptionPane.showMessageDialog(null, "Please select an Publisher to restore!", "Warning",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			// Lấy thông tin publisherID từ bảng
			var publisherID = (int) tablePublisher.getValueAt(selectedRow, 5); // Giả sử cột đầu tiên là AuthorID
			var PublishersName = (String) tablePublisher.getValueAt(selectedRow, 1);
//
//			JOptionPane.showMessageDialog(null, publisherID);
//										        // Hỏi xác nhận
			var confirm = JOptionPane.showConfirmDialog(null,
					"Are you sure you want to restore the Publishers: " + PublishersName + "?", "Confirm Restore",
					JOptionPane.YES_NO_OPTION);

			if (confirm == JOptionPane.YES_OPTION) {
				try (var con = ConnectDB.getCon();
						var ps = con
								.prepareStatement("UPDATE Publishers SET IsDeleted = 0 WHERE PublisherID = ?")) {
					// Cập nhật IsDeleted = 0
					ps.setInt(1, publisherID);
					var rowsAffected = ps.executeUpdate();

					if (rowsAffected > 0) {
						JOptionPane.showMessageDialog(null, "publisher restored successfully!", "Success",
								JOptionPane.INFORMATION_MESSAGE);
						((DefaultTableModel) tableAuthor.getModel()).removeRow(selectedRow); // Xóa dòng khỏi bảng
					} else {
						JOptionPane.showMessageDialog(null, "Failed to restore author. Please try again!", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(null, "An error occurred: " + ex.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
				}
				panelPublisherActivated();
			}

		});
		btnRePub.setToolTipText("Add Publisher");
		btnRePub.setIcon(new ImageIcon(Recycle.class.getResource("/icon12/clock.png")));
		btnRePub.setBounds(14, 12, 62, 45);
		panelPublisher.add(btnRePub);

		searchPublisher = new JTextField("Search by Publisher Name...");
		searchPublisher.setBounds(451, 12, 343, 45);
		searchPublisher.setForeground(Color.GRAY);
		searchPublisher.setFont(new Font("Arial", Font.PLAIN, 14));
		searchPublisher.setBackground(Color.WHITE);
		searchPublisher.setBorder(javax.swing.BorderFactory.createLineBorder(Color.GRAY, 1));
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
		panelPublisher.add(searchPublisher);

		var btnSearchPublisher = new JButton("Search");
		btnSearchPublisher.setBounds(804, 12, 81, 44);
		btnSearchPublisher.setBackground(new Color(63, 81, 181));
		btnSearchPublisher.setForeground(Color.BLACK);
		btnSearchPublisher.setFont(new Font("Arial", Font.BOLD, 14));
		btnSearchPublisher.addActionListener(this::onSearchPublisher);
		panelPublisher.add(btnSearchPublisher);

		btnDeletePub = new JButton("");
		btnDeletePub.addActionListener(e -> {
			// Kiểm tra dòng được chọn
			var selectedRow = tablePublisher.getSelectedRow();
			if (selectedRow == -1) {
				JOptionPane.showMessageDialog(null, "Please select a publisher to delete!", "Warning",
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			// Lấy PublisherID và tên từ bảng
			var publisherID = (int) tablePublisher.getValueAt(selectedRow, 5); // Giả sử cột thứ 5 là PublisherID
			var publisherName = (String) tablePublisher.getValueAt(selectedRow, 1); // Cột thứ 2 là tên Publisher

			// Xác nhận xóa
			var confirm = JOptionPane.showConfirmDialog(null,
					"Are you sure you want to delete the publisher: " + publisherName + "?", "Confirm Delete",
					JOptionPane.YES_NO_OPTION);

			if (confirm == JOptionPane.YES_OPTION) {
				try (var con = ConnectDB.getCon();
						var ps = con.prepareStatement("DELETE FROM Publishers WHERE PublisherID = ?")) {
					// Gán giá trị cho tham số
					ps.setInt(1, publisherID);

					// Thực hiện lệnh xóa
					var rowsAffected = ps.executeUpdate();

					// Kiểm tra kết quả
					if (rowsAffected > 0) {
						JOptionPane.showMessageDialog(null, "Publisher deleted successfully!", "Success",
								JOptionPane.INFORMATION_MESSAGE);
						((DefaultTableModel) tablePublisher.getModel()).removeRow(selectedRow); // Xóa dòng khỏi
																								// bảng
					} else {
						JOptionPane.showMessageDialog(null, "No publisher found to delete. Please try again!",
								"Error", JOptionPane.ERROR_MESSAGE);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(null, "An error occurred: " + ex.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnDeletePub.setToolTipText("Add Author");
		btnDeletePub.setIcon(new ImageIcon(Recycle.class.getResource("/icon3/bin.png")));
		btnDeletePub.setBounds(91, 12, 62, 45);
		panelPublisher.add(btnDeletePub);
		tabbedPane.addTab("Category", tabCategoryIcon, panelCategory, "This is Category Management");

//		var panelUser = new JPanel();
//		panelUser.setVisible(false);
//		panelUser.setBackground(Color.WHITE);
//		panelUser.setLayout(null);
//
//		// Khởi tạo scrollPaneUser (biến thành viên)
//		scrollPaneUser = new JScrollPane();
//		scrollPaneUser.setBounds(0, 67, 901, 578);
//		scrollPaneUser.setBorder(BorderFactory.createEmptyBorder()); // Xóa viền
//		scrollPaneUser.getViewport().setBorder(null); // Xóa viền của viewport
//		panelUser.add(scrollPaneUser);

		// Khởi tạo tableUser (biến thành viên)
//		tableUser = new JTable();
//		tableUser.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//		tableUser.setAutoCreateRowSorter(true);
//		tableUser.setShowVerticalLines(false);
//		scrollPaneUser.setViewportView(tableUser);

//		tabbedPane.addTab("Users", null, panelUser, "Manage Users");
		

		scrollPaneCategory = new JScrollPane();
		scrollPaneCategory.setBounds(0, 68, 901, 578);
		scrollPaneCategory.setBorder(BorderFactory.createEmptyBorder()); // Remove border
		scrollPaneCategory.getViewport().setBorder(null); // Remove viewport border
		panelCategory.add(scrollPaneCategory);

		tableCategory = new JTable();
		tableCategory.setAutoCreateRowSorter(true);
		tableCategory.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableCategory.setBorder(null);
		tableCategory.setShowVerticalLines(false);
		scrollPaneCategory.setViewportView(tableCategory);

		searchCategory = new JTextField("Search by Category Name...");
		searchCategory.setBounds(451, 12, 343, 45);
		searchCategory.setForeground(Color.GRAY);
		searchCategory.setFont(new Font("Arial", Font.PLAIN, 14));
		searchCategory.setBackground(Color.WHITE);
		searchCategory.setBorder(javax.swing.BorderFactory.createLineBorder(Color.GRAY, 1));
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
		panelCategory.add(searchCategory);

		btnSearchCategory = new JButton("Search");
		btnSearchCategory.setBounds(804, 12, 81, 44);
		btnSearchCategory.setBackground(new Color(63, 81, 181));
		btnSearchCategory.setForeground(Color.BLACK);
		btnSearchCategory.setFont(new Font("Arial", Font.BOLD, 14));
		btnSearchCategory.addActionListener(this::onSearchCategory);
		panelCategory.add(btnSearchCategory);

		var btnAddPublisher_1 = new JButton("");
		btnAddPublisher_1.addActionListener(e -> {
			var selectedRow = tableCategory.getSelectedRow(); // Lấy dòng được chọn trong bảng
			if (selectedRow == -1) {
				JOptionPane.showMessageDialog(null, "Please select a Category to restore!", "Warning",
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			// Lấy thông tin UserID từ bảng
			var CategoryID = (int) tableCategory.getValueAt(selectedRow, 3); // Giả sử cột đầu tiên là UserID
			var CategoryName = (String) tableCategory.getValueAt(selectedRow, 1);

			// Hỏi xác nhận
			var confirm = JOptionPane.showConfirmDialog(null,
					"Are you sure you want to restore the Category: " + CategoryName + "?", "Confirm Restore",
					JOptionPane.YES_NO_OPTION);

			if (confirm == JOptionPane.YES_OPTION) {
				try (var con = ConnectDB.getCon();
						var ps = con.prepareStatement("UPDATE Categories SET IsDeleted = 0 WHERE CategoryID = ?")) {
					// Cập nhật IsDeleted = 0
					ps.setInt(1, CategoryID);
					var rowsAffected = ps.executeUpdate();

					if (rowsAffected > 0) {
						JOptionPane.showMessageDialog(null, "Category restored successfully!", "Success",
								JOptionPane.INFORMATION_MESSAGE);
						((DefaultTableModel) tableCategory.getModel()).removeRow(selectedRow); // Xóa dòng khỏi bảng
					} else {
						JOptionPane.showMessageDialog(null, "Failed to restore Category. Please try again!", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(null, "An error occurred: " + ex.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}

//
		});
		btnAddPublisher_1.setToolTipText("Add Publisher");
		btnAddPublisher_1.setIcon(new ImageIcon(Recycle.class.getResource("/icon12/clock.png")));
		btnAddPublisher_1.setBounds(14, 12, 62, 45);
		panelCategory.add(btnAddPublisher_1);

		btnAddAuthor_2_1 = new JButton("");
		btnAddAuthor_2_1.addActionListener(e -> {
			// Kiểm tra dòng được chọn
			var selectedRow = tableCategory.getSelectedRow();
			if (selectedRow == -1) {
				JOptionPane.showMessageDialog(null, "Please select a Categories to delete!", "Warning",
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			// Lấy PublisherID và tên từ bảng
			var CategoryID = (int) tableCategory.getValueAt(selectedRow, 3); // Giả sử cột đầu tiên là UserID
			var CategoryName = (String) tableCategory.getValueAt(selectedRow, 1);

			// Xác nhận xóa
			var confirm = JOptionPane.showConfirmDialog(null,
					"Are you sure you want to delete the publisher: " + CategoryName + "?", "Confirm Delete",
					JOptionPane.YES_NO_OPTION);

			if (confirm == JOptionPane.YES_OPTION) {
				try (var con = ConnectDB.getCon();
						var ps = con.prepareStatement("DELETE FROM Categories WHERE CategoryID = ?")) {
					// Gán giá trị cho tham số
					ps.setInt(1, CategoryID);

					// Thực hiện lệnh xóa
					var rowsAffected = ps.executeUpdate();

					// Kiểm tra kết quả
					if (rowsAffected > 0) {
						JOptionPane.showMessageDialog(null, "Category deleted successfully!", "Success",
								JOptionPane.INFORMATION_MESSAGE);
						((DefaultTableModel) tableCategory.getModel()).removeRow(selectedRow); // Xóa dòng khỏi
																								// bảng
					} else {
						JOptionPane.showMessageDialog(null, "No Category found to delete. Please try again!",
								"Error", JOptionPane.ERROR_MESSAGE);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(null, "An error occurred: " + ex.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnAddAuthor_2_1.setToolTipText("Add Author");
		btnAddAuthor_2_1.setIcon(new ImageIcon(Recycle.class.getResource("/icon3/bin.png")));
		btnAddAuthor_2_1.setBounds(91, 12, 62, 45);
		panelCategory.add(btnAddAuthor_2_1);

		add(tabbedPane);

		tabbedPane.addChangeListener(e -> {
			if (tabbedPane.getSelectedIndex() == 1) {
				panelPublisherActivated();
			} else if (tabbedPane.getSelectedIndex() == 2) {
				panelCategoryActivated();
			} else if (tabbedPane.getSelectedIndex() == 6) {
//				panelUserActivated();
			}
			else if (tabbedPane.getSelectedIndex() == 3) {
				panelStudentActivated();
			} else if (tabbedPane.getSelectedIndex() == 4) {
				panelBookActivated();
//				  JOptionPane.showMessageDialog(null, 1);
			}

		});

		panel1Activated();
		///

		// Scroll Pane for User Table

		// User Table

		// Restore User Button
		var btnRestoreUser = new JButton("");
		btnRestoreUser.setToolTipText("Restore User");
		btnRestoreUser.setIcon(new ImageIcon(Recycle.class.getResource("/icon4/redo.png")));
		btnRestoreUser.setBounds(10, 11, 62, 45);
		btnRestoreUser.addActionListener(e -> {
			var selectedRow = tableUser.getSelectedRow(); // Lấy dòng được chọn trong bảng
			if (selectedRow == -1) {
				JOptionPane.showMessageDialog(null, "Please select a user to restore!", "Warning",
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			// Lấy thông tin UserID từ bảng
			var userID = (int) tableUser.getValueAt(selectedRow, 8); // Giả sử cột đầu tiên là UserID
			var userName = (String) tableUser.getValueAt(selectedRow, 2);

			// Hỏi xác nhận
			var confirm = JOptionPane.showConfirmDialog(null,
					"Are you sure you want to restore the user: " + userName + "?", "Confirm Restore",
					JOptionPane.YES_NO_OPTION);

			if (confirm == JOptionPane.YES_OPTION) {
				try (var con = ConnectDB.getCon();
						var ps = con.prepareStatement("UPDATE Users SET IsDeleted = 0 WHERE UserID = ?")) {
					// Cập nhật IsDeleted = 0
					ps.setInt(1, userID);
					var rowsAffected = ps.executeUpdate();

					if (rowsAffected > 0) {
						JOptionPane.showMessageDialog(null, "User restored successfully!", "Success",
								JOptionPane.INFORMATION_MESSAGE);
						((DefaultTableModel) tableUser.getModel()).removeRow(selectedRow); // Xóa dòng khỏi bảng
					} else {
						JOptionPane.showMessageDialog(null, "Failed to restore user. Please try again!", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(null, "An error occurred: " + ex.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
//		panelUser.add(btnRestoreUser);

		// Delete User Button
		btnDeleteUser = new JButton("");
		btnDeleteUser.setToolTipText("Delete User");
		btnDeleteUser.setIcon(new ImageIcon(Recycle.class.getResource("/icon3/bin.png")));
		btnDeleteUser.setBounds(90, 11, 62, 45);
		btnDeleteUser.addActionListener(e -> {
			var selectedRow = tableUser.getSelectedRow(); // Lấy dòng được chọn trong bảng
			if (selectedRow == -1) {
				JOptionPane.showMessageDialog(null, "Please select a user to delete!", "Warning",
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			// Lấy thông tin UserID từ bảng
			var userID = (int) tableUser.getValueAt(selectedRow, 7); // Giả sử cột đầu tiên là UserID

			var userName = (String) tableUser.getValueAt(selectedRow, 2);

			// Hỏi xác nhận
			var confirm = JOptionPane.showConfirmDialog(null,
					"Are you sure you want to delete the user: " + userName + "?", "Confirm Delete",
					JOptionPane.YES_NO_OPTION);

			if (confirm == JOptionPane.YES_OPTION) {
				try (var con = ConnectDB.getCon();
						var ps = con.prepareStatement("DELETE FROM Users WHERE UserID = ?")) {
					// Xóa user
					ps.setInt(1, userID);
					var rowsAffected = ps.executeUpdate();

					if (rowsAffected > 0) {
						JOptionPane.showMessageDialog(null, "User deleted successfully!", "Success",
								JOptionPane.INFORMATION_MESSAGE);
						((DefaultTableModel) tableUser.getModel()).removeRow(selectedRow); // Xóa dòng khỏi bảng
					} else {
						JOptionPane.showMessageDialog(null, "Failed to delete user. Please try again!", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(null, "An error occurred: " + ex.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
//		panelUser.add(btnDeleteUser);

		// Search User TextField
		searchUser = new JTextField("Search by User Name...");
		searchUser.setBounds(448, 13, 343, 45);
		searchUser.setForeground(Color.GRAY);
		searchUser.setFont(new Font("Arial", Font.PLAIN, 14));
		searchUser.setBackground(Color.WHITE);
		searchUser.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
		searchUser.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (searchUser.getText().equals("Search by User Name...")) {
					searchUser.setText("");
					searchUser.setForeground(Color.BLACK);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (searchUser.getText().isEmpty()) {
					searchUser.setText("Search by User Name...");
					searchUser.setForeground(Color.GRAY);
				}
			}
		});
//		panelUser.add(searchUser);

		// Search User Button
		btnSearchUser = new JButton("Search");
		btnSearchUser.setBounds(804, 13, 81, 44);
		btnSearchUser.setBackground(new Color(63, 81, 181));
		btnSearchUser.setForeground(Color.BLACK);
		btnSearchUser.setFont(new Font("Arial", Font.BOLD, 14));
		btnSearchUser.addActionListener(e -> {
			// Lấy giá trị tìm kiếm từ ô tìm kiếm
			var searchValue = searchUser.getText().trim().toLowerCase();

			// Kiểm tra nếu ô tìm kiếm rỗng hoặc có giá trị mặc định
			if (searchValue.equals("") || searchValue.equals("search by username...")) {
				JOptionPane.showMessageDialog(null, "Please enter a valid search term.", "Info",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			// Khởi tạo đối tượng PublisherDao để truy vấn dữ liệu
			var dao = new UserDao();
			// Lọc danh sách người dùng đã bị xóa (dựa trên tên người dùng)
			var filteredUsers = dao.selectDeletedUser().stream()
					.filter(user -> user.getUsername().toLowerCase().contains(searchValue)).toList();

			// Cập nhật bảng hiển thị kết quả tìm kiếm
			var model = (DefaultTableModel) tableUser.getModel();
			model.setRowCount(0); // Xóa các dòng cũ trong bảng

			var count = 1;
			for (var user : filteredUsers) {
				model.addRow(new Object[] { count++, user.getUsername(), user.getFullName(), user.getEmail(),
						user.getPhoneNumber() });
			}

			// Nếu không tìm thấy kết quả, hiển thị thông báo
			if (filteredUsers.isEmpty()) {
				JOptionPane.showMessageDialog(null, "No users found matching the search criteria.", "Info",
						JOptionPane.INFORMATION_MESSAGE);
				panelUserActivated();
			}
		});
//		panelUser.add(btnSearchUser);
		// panel Student

		panelStudent = new JPanel();
		panelStudent.setBorder(null);
		panelStudent.setBackground(Color.WHITE);
		panelStudent.setLayout(null);
		tabbedPane.addTab("Student", tab3Icon, panelStudent, "This is Student Management");

		// Tạo JScrollPane cho bảng Student
		scrollPaneStudent = new JScrollPane();
		scrollPaneStudent.setBorder(BorderFactory.createEmptyBorder()); // Remove border
		scrollPaneStudent.getViewport().setBorder(null); // Remove viewport border
		scrollPaneStudent.setBounds(0, 68, 901, 578); // Xác định vị trí và kích thước
		panelStudent.add(scrollPaneStudent);

		// Tạo model cho bảng Student

		// Khởi tạo bảng Student với model
		tableStudent = new JTable();
		tableStudent.setAutoCreateRowSorter(true);
		tableStudent.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableStudent.setBorder(null);
		tableStudent.setShowVerticalLines(false);
		tableStudent.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableStudent.setFont(new Font("Arial", Font.PLAIN, 14));
		tableStudent.setRowHeight(80); // Set row height to 80 for Avatar imagesStudentCode
		// Đặt bảng vào JScrollPane
		scrollPaneStudent.setViewportView(tableStudent);

		// Tạo nút Restore cho Student
				btnRestoreStudent = new JButton("");
				btnRestoreStudent.addActionListener(e -> {
				    var selectedRow = tableStudent.getSelectedRow(); // Lấy dòng được chọn trong bảng
				    if (selectedRow == -1) {
				        JOptionPane.showMessageDialog(null, "Please select a student to restore!", "Warning",
				                JOptionPane.WARNING_MESSAGE);
				        return;
				    }

				    // Lấy thông tin StudentID từ bảng
				    var studentID = (int) tableStudent.getValueAt(selectedRow, 8); // Cột chứa StudentID
				    var studentName = (String) tableStudent.getValueAt(selectedRow, 2); // Cột chứa tên sinh viên

				    // Hỏi xác nhận
				    var confirm = JOptionPane.showConfirmDialog(null,
				            "Are you sure you want to restore the student: " + studentName + "?", "Confirm Restore",
				            JOptionPane.YES_NO_OPTION);

				    if (confirm == JOptionPane.YES_OPTION) {
				        try (var con = ConnectDB.getCon();
				             var ps = con.prepareStatement("UPDATE Students SET IsDeleted = 0 WHERE StudentID = ?")) {
				            // Cập nhật IsDeleted = 0
				            ps.setInt(1, studentID);
				            var rowsAffected = ps.executeUpdate();

				            if (rowsAffected > 0) {
				                JOptionPane.showMessageDialog(null, "Student restored successfully!", "Success",
				                        JOptionPane.INFORMATION_MESSAGE);
				                
				                // Xóa dòng khỏi bảng Recycle
				                ((DefaultTableModel) tableStudent.getModel()).removeRow(selectedRow);

				                // Gọi lại phương thức reload trên bảng chính
				                Main.getStudentPanel().reloadTable(); // Giả sử Main.getStudentPanel() trả về đối tượng `Student`
				            } else {
				                JOptionPane.showMessageDialog(null, "Failed to restore student. Please try again!", "Error",
				                        JOptionPane.ERROR_MESSAGE);
				            }
				        } catch (Exception ex) {
				            ex.printStackTrace();
				            JOptionPane.showMessageDialog(null, "An error occurred: " + ex.getMessage(), "Error",
				                    JOptionPane.ERROR_MESSAGE);
				        }
				    }
				});

				btnRestoreStudent.setIcon(new ImageIcon(Recycle.class.getResource("/icon12/clock.png")));
				btnRestoreStudent.setBounds(14, 12, 62, 45);
				btnRestoreStudent.setToolTipText("Restore Student");
				panelStudent.add(btnRestoreStudent);

		// Tạo ô tìm kiếm cho Student
		searchStudent = new JTextField("Search by Student Name...");
		searchStudent.setBounds(451, 12, 343, 45);
		searchStudent.setForeground(Color.GRAY);
		searchStudent.setFont(new Font("Arial", Font.PLAIN, 14));
		searchStudent.setBackground(Color.WHITE);
		searchStudent.setBorder(javax.swing.BorderFactory.createLineBorder(Color.GRAY, 1));
		searchStudent.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (searchStudent.getText().equals("Search by Student Name...")) {
					searchStudent.setText("");
					searchStudent.setForeground(Color.BLACK);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (searchStudent.getText().isEmpty()) {
					searchStudent.setText("Search by Student Name...");
					searchStudent.setForeground(Color.GRAY);
				}
			}
		});
		panelStudent.add(searchStudent);

		// Tạo nút tìm kiếm cho Student
		var btnSearchStudent = new JButton("Search");
		btnSearchStudent.setBounds(804, 12, 81, 44);
		btnSearchStudent.setBackground(new Color(63, 81, 181));
		btnSearchStudent.setForeground(Color.BLACK);
		btnSearchStudent.setFont(new Font("Arial", Font.BOLD, 14));
		btnSearchStudent.addActionListener(this::onSearchStudent); // Tạo method onSearch tương tự
		panelStudent.add(btnSearchStudent);

		// Tạo nút xóa cho Student
		btnDeleteStudent1 = new JButton("");
		btnDeleteStudent1.addActionListener(e -> {
		    var selectedRow = tableStudent.getSelectedRow();
		    if (selectedRow == -1) {
		        JOptionPane.showMessageDialog(null, "Please select a student to delete!", "Warning",
		                JOptionPane.WARNING_MESSAGE);
		        return;
		    }

		    // Lấy StudentID và tên sinh viên từ bảng
		    var studentID = (int) tableStudent.getValueAt(selectedRow, 8); // Cột đầu tiên là StudentID
		    var studentName = (String) tableStudent.getValueAt(selectedRow, 2); // Cột thứ ba là tên sinh viên

		    var confirm = JOptionPane.showConfirmDialog(null,
		            "Are you sure you want to delete the student: " + studentName + "?", "Confirm Delete",
		            JOptionPane.YES_NO_OPTION);
		    if (confirm == JOptionPane.YES_OPTION) {
		        try (var con = ConnectDB.getCon()) {

		            // Kiểm tra liên kết trong Payments (nếu có)
		            var checkPaymentsSql = "SELECT COUNT(*) FROM Payments WHERE StudentID = ?";
		            try (var checkPaymentsPs = con.prepareStatement(checkPaymentsSql)) {
		                checkPaymentsPs.setInt(1, studentID);
		                try (var rs = checkPaymentsPs.executeQuery()) {
		                    if (rs.next() && rs.getInt(1) > 0) {
		                        JOptionPane.showMessageDialog(null,
		                                "Cannot delete student because it is linked to payments.",
		                                "Error", JOptionPane.ERROR_MESSAGE);
		                        return;
		                    }
		                }
		            }

		            // Xóa sinh viên nếu không có liên kết
		            var deleteSql = "DELETE FROM Students WHERE StudentID = ?";
		            try (var deletePs = con.prepareStatement(deleteSql)) {
		                deletePs.setInt(1, studentID);
		                var rowsAffected = deletePs.executeUpdate();

		                if (rowsAffected > 0) {
		                    JOptionPane.showMessageDialog(null, "Student deleted successfully!", "Success",
		                            JOptionPane.INFORMATION_MESSAGE);
		                    ((DefaultTableModel) tableStudent.getModel()).removeRow(selectedRow); // Xóa dòng khỏi bảng
		                } else {
		                    JOptionPane.showMessageDialog(null, "No student found to delete. Please try again!", "Error",
		                            JOptionPane.ERROR_MESSAGE);
		                }
		            }
		        } catch (SQLException ex) {
		            JOptionPane.showMessageDialog(null,
		                    "A database error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		        } catch (Exception ex) {
		            JOptionPane.showMessageDialog(null,
		                    "An unexpected error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		        }
		    }
		});



		btnDeleteStudent1.setToolTipText("Delete Student");
		btnDeleteStudent1.setIcon(new ImageIcon(Recycle.class.getResource("/icon3/bin.png")));
		btnDeleteStudent1.setBounds(91, 12, 62, 45);
		panelStudent.add(btnDeleteStudent1);

		// Khởi tạo panel Sách
		panelBook = new JPanel();
		panelBook.setBorder(null);
		panelBook.setBackground(Color.WHITE);
		panelBook.setLayout(null);
		tabbedPane.addTab("Book", tab3Icon, panelBook, "This is Book Management");

		// Khởi tạo ScrollPane cho panel Sách
		scrollPaneBook = new JScrollPane();
		scrollPaneBook.setBorder(BorderFactory.createEmptyBorder());
		scrollPaneBook.getViewport().setBorder(null);
		scrollPaneBook.setBounds(0, 68, 901, 578);
		panelBook.add(scrollPaneBook);

		// Khởi tạo Bảng Sách
		tableBook = new JTable();
		tableBook.setAutoCreateRowSorter(true);
		tableBook.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableBook.setBorder(null);
		tableBook.setShowVerticalLines(false);
		scrollPaneBook.setViewportView(tableBook);
		tableBook.setFocusable(true);
		tableBook.setRowSelectionAllowed(true);
		// Khởi tạo ô tìm kiếm
		searchBook = new JTextField("Search by Book Title...");
		searchBook.setBounds(451, 12, 343, 45);
		searchBook.setForeground(Color.GRAY);
		searchBook.setFont(new Font("Arial", Font.PLAIN, 14));
		searchBook.setBackground(Color.WHITE);
		searchBook.setBorder(javax.swing.BorderFactory.createLineBorder(Color.GRAY, 1));
		searchBook.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (searchBook.getText().equals("Search by Book Title...")) {
					searchBook.setText("");
					searchBook.setForeground(Color.BLACK);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (searchBook.getText().isEmpty()) {
					searchBook.setText("Search by Book Title...");
					searchBook.setForeground(Color.GRAY);
				}
			}
		});
		panelBook.add(searchBook);

		// Khởi tạo nút tìm kiếm Sách
		btnSearchBook = new JButton("Search");
		
		btnSearchBook.addActionListener(this::onSearchBook); // Tạo method onSearch tương tự
		btnSearchBook.setBounds(804, 12, 81, 44);
		btnSearchBook.setBackground(new Color(63, 81, 181));
		btnSearchBook.setForeground(Color.BLACK);
		btnSearchBook.setFont(new Font("Arial", Font.BOLD, 14));

		panelBook.add(btnSearchBook);

		btnRestoreBook = new JButton("");
		btnRestoreBook.addActionListener(e -> {
		    var selectedRow = tableBook.getSelectedRow(); // Lấy dòng được chọn trong bảng
		    System.out.println("Selected Row: " + selectedRow);

		    if (selectedRow == -1) {
		        JOptionPane.showMessageDialog(null, "Please select a book to restore!", "Warning",
		                JOptionPane.WARNING_MESSAGE);
		        return;
		    }

		    try {
		        // In toàn bộ giá trị của dòng được chọn
		        for (int col = 0; col < tableBook.getColumnCount(); col++) {
		            System.out.println("Column " + col + ": " + tableBook.getValueAt(selectedRow, col));
		        }

		        // Lấy thông tin BookID và Title từ bảng
		        var bookID = (int) tableBook.getValueAt(selectedRow, 12); // Cột BookID
		        var bookTitle = (String) tableBook.getValueAt(selectedRow, 2); // Cột Title

		        if (bookTitle == null || bookTitle.trim().isEmpty()) {
		            bookTitle = "Unnamed Book"; // Xử lý trường hợp tiêu đề trống
		        }

		        // Hộp thoại xác nhận
		        var confirm = JOptionPane.showConfirmDialog(null,
		                "Are you sure you want to restore the book: " + bookTitle + "?", "Confirm Restore",
		                JOptionPane.YES_NO_OPTION);

		        if (confirm == JOptionPane.YES_OPTION) {
		            try (var con = ConnectDB.getCon();
		                 var ps = con.prepareStatement("UPDATE Books SET IsDeleted = 0 WHERE BookID = ?")) {
		                ps.setInt(1, bookID);

		                var rowsAffected = ps.executeUpdate();
		                if (rowsAffected > 0) {
		                    JOptionPane.showMessageDialog(null, "Book \"" + bookTitle + "\" restored successfully!", "Success",
		                            JOptionPane.INFORMATION_MESSAGE);

		                    // Xóa dòng khỏi bảng hiện tại
		                    ((DefaultTableModel) tableBook.getModel()).removeRow(selectedRow);

		                    // Reload bảng chính
		                    reloadBooksInMainTable();
		                } else {
		                    JOptionPane.showMessageDialog(null, "Failed to restore book. Please try again!", "Error",
		                            JOptionPane.ERROR_MESSAGE);
		                }
		            }
		        }
		    } catch (Exception ex) {
		        ex.printStackTrace();
		        JOptionPane.showMessageDialog(null, "An error occurred: " + ex.getMessage(), "Error",
		                JOptionPane.ERROR_MESSAGE);
		    }
		});


		btnRestoreBook.setBounds(14, 12, 62, 45);
		btnRestoreBook.setIcon(new ImageIcon(Recycle.class.getResource("/icon12/clock.png")));
		btnRestoreBook.setToolTipText("Restore Book");
		panelBook.add(btnRestoreBook);


		btnDeleteBook1 = new JButton("");
		btnRestoreBook.addActionListener(e -> {
		    var selectedRow = tableBook.getSelectedRow(); // Lấy dòng được chọn trong bảng
		    if (selectedRow == -1) {
		        JOptionPane.showMessageDialog(null, "Please select a book to restore!", "Warning",
		                JOptionPane.WARNING_MESSAGE);
		        return;
		    }

		    // Lấy thông tin BookID từ bảng
		    var bookID = (int) tableBook.getValueAt(selectedRow, 12); // Giả sử cột đầu tiên là BookID
		    var bookTitle = (String) tableBook.getValueAt(selectedRow, 2); // Giả sử cột thứ 3 là Title

		    // Hỏi xác nhận
		    var confirm = JOptionPane.showConfirmDialog(null,
		            "Are you sure you want to restore the book: " + bookTitle + "?", "Confirm Restore",
		            JOptionPane.YES_NO_OPTION);

		    if (confirm == JOptionPane.YES_OPTION) {
		        try (var con = ConnectDB.getCon();
		             var ps = con.prepareStatement("UPDATE Books SET IsDeleted = 0 WHERE BookID = ?")) {
		            // Cập nhật IsDeleted = 0
		            ps.setInt(1, bookID);
		            var rowsAffected = ps.executeUpdate();

		            if (rowsAffected > 0) {
		                JOptionPane.showMessageDialog(null, "Book restored successfully!", "Success",
		                        JOptionPane.INFORMATION_MESSAGE);

		                // Xóa dòng khỏi bảng hiện tại (Recycle)
		                ((DefaultTableModel) tableBook.getModel()).removeRow(selectedRow);

		                // Cập nhật lại bảng chính (Main Table)
		                reloadBooksInMainTable(); // Gọi lại phương thức để reload bảng chính
		            } else {
		                JOptionPane.showMessageDialog(null, "Failed to restore book. Please try again!", "Error",
		                        JOptionPane.ERROR_MESSAGE);
		            }
		        } catch (Exception ex) {
		            ex.printStackTrace();
		            JOptionPane.showMessageDialog(null, "An error occurred: " + ex.getMessage(), "Error",
		                    JOptionPane.ERROR_MESSAGE);
		        }
		    }
		});

		btnDeleteBook1.setBounds(91, 12, 62, 45);
		btnDeleteBook1.setToolTipText("Delete Book");
		btnDeleteBook1.setIcon(new ImageIcon(Recycle.class.getResource("/icon3/bin.png")));
		panelBook.add(btnDeleteBook1);
	}


	private void panelCategoryActivated() {
		var dao = new CategoriesDao();

		DefaultTableModel model = new DefaultTableModel() {
			private static final long serialVersionUID = 1L;

//	        @Override
//	        public boolean isCellEditable(int row, int column) {
//	            return column == 4; // Chỉ cột "Action" có thể chỉnh sửa
//	        }
		};

		model.addColumn("No.");
		model.addColumn("Category Name");
		model.addColumn("Description");
		model.addColumn("ID");
		model.addColumn("isSelected");

		var categories = dao.selectDeletedCategories(); // Lấy danh sách danh mục bị xóa
		var count = 1;
		for (var category : categories) {
			model.addRow(new Object[] { count++, category.getCategoryName(), category.getDescription(),
					category.getCategoryID(), category.isSelected() });
		}

		tableCategory.setModel(model); // Cập nhật model cho bảng
		tableCategory.getColumnModel().getColumn(4).setMinWidth(0);
		tableCategory.getColumnModel().getColumn(4).setMaxWidth(0);
		tableCategory.getColumnModel().getColumn(4).setPreferredWidth(0);
		
		tableCategory.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				var selectedRow = tableCategory.getSelectedRow();
				if (selectedRow !=-1) {
					Object valueAt = tableCategory.getValueAt(selectedRow, 4);
					isSelected =  (Boolean) valueAt;
					btnAddAuthor_2_1.setEnabled(!isSelected);
				}
			}
			
		});

		customizeCategoryTable(tableCategory);
		
	}
	private void customizeCategoryTable(JTable table) {
	    table.setFont(new Font("Arial", Font.PLAIN, 14));
	    table.setRowHeight(80); // Chiều cao của mỗi hàng
	    table.setGridColor(new Color(224, 224, 224)); // Màu lưới của bảng

	    // Căn chỉnh tất cả các cột
	    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
	    centerRenderer.setHorizontalAlignment(SwingConstants.CENTER); // Căn giữa nội dung cột

	    for (int i = 0; i < table.getColumnCount(); i++) {
	        table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
	    }

	    // Tùy chỉnh header
	    var header = table.getTableHeader();
	    header.setPreferredSize(new Dimension(header.getPreferredSize().width, 40)); // Đặt chiều cao header
	    header.setDefaultRenderer(new DefaultTableCellRenderer() {
	        private static final long serialVersionUID = 1L;

	        @Override
	        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
	            var headerRenderer = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	            headerRenderer.setBackground(new Color(211, 211, 211)); // Màu nền header
	            headerRenderer.setForeground(Color.BLACK); // Màu chữ header
	            headerRenderer.setFont(new Font("Arial", Font.BOLD, 16)); // Font chữ header
	            ((DefaultTableCellRenderer) headerRenderer).setHorizontalAlignment(SwingConstants.CENTER); // Căn giữa header
	            return headerRenderer;
	        }
	    });
	}


	private void panelUserActivated() {
		var dao = new UserDao();
		DefaultTableModel model = new DefaultTableModel() {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false; // Không cho phép chỉnh sửa bất kỳ cột nào
			}
		};

		model.addColumn("No.");
		model.addColumn("Avatar");
		model.addColumn("Full Name");
		model.addColumn("Username");
		model.addColumn("Email");
		model.addColumn("Phone Number");
		model.addColumn("Address");
		model.addColumn("Role");
		model.addColumn("ID");

		var users = dao.selectDeletedUser(); // Lấy danh sách tất cả người dùng
		var count = 1;
		for (var user : users) {
			model.addRow(
					new Object[] { count++, user.getAvatar(), user.getFullName(), user.getUsername(), user.getEmail(),
					user.getPhoneNumber(), user.getAddress(), user.getUserRole() == 2 ? "Admin" : "User",
					user.getUserID() });
		}

		tableUser.setModel(model); // Cập nhật model cho bảng
		customizeTable(tableUser); // Tùy chỉnh giao diện bảng (nếu cần)

	}

	private void onSearchCategory(ActionEvent e) {
		var searchText = searchCategory.getText().trim().toLowerCase();
		if (searchText.equals("search by category name...") || searchText.isEmpty()) {
			panelCategoryActivated();
			return;
		}

		var dao = new CategoriesDao();
		var filteredData = dao.selectDeletedCategories().stream()
				.filter(category -> category.getCategoryName().toLowerCase().contains(searchText)).toList();

		var model = (DefaultTableModel) tableCategory.getModel();
		model.setRowCount(0);
		var count = 1;

		for (var category : filteredData) {
			model.addRow(new Object[] { count++, category.getCategoryName(), category.getDescription(), category.isSelected() });
		}
	}

	@SuppressWarnings("unused")
	private void reloadStudentsInMainTable() {
	    try {
	        // Lấy lại danh sách sinh viên từ database
	        var studentDao = new StudentDao();
	        var students = studentDao.select(); // Lấy tất cả sinh viên chưa bị xóa

	        // Cập nhật lại bảng chính trong file Student
	        Main.getStudentPanel().reloadTableWithStudents(students); // Main.getStudentPanel() là tham chiếu đến bảng Student
	    } catch (Exception e) {
	        e.printStackTrace();
	        JOptionPane.showMessageDialog(null, "Failed to reload students: " + e.getMessage(), "Error",
	                JOptionPane.ERROR_MESSAGE);
	    }
	}
	@SuppressWarnings("unused")
	private void reloadBooksInMainTable() {
	    try {
	        var bookDao = new BookManagementDao();
	        var books = bookDao.select();
	        Main.getBookPanel().reloadTableWithBooks(books);
	    } catch (Exception e) {
	        e.printStackTrace();
	        JOptionPane.showMessageDialog(null, "Failed to reload books: " + e.getMessage(), "Error",
	                JOptionPane.ERROR_MESSAGE);
	    }
	}

	private void panelStudentActivated() {
	    var studentDao = new StudentDao();

	    DefaultTableModel model = new DefaultTableModel() {
	        private static final long serialVersionUID = 1L;

	        @Override
	        public Class<?> getColumnClass(int column) {
	            return switch (column) {
	                case 0 -> Integer.class; // Số thứ tự
	                case 1 -> ImageIcon.class; // Ảnh đại diện
	                case 2 -> String.class; // Họ và tên
	                case 3 -> String.class; // Ngày sinh
	                case 4 -> String.class; // Giới tính
	                case 5 -> String.class; // Email
	                case 6 -> String.class; // Số điện thoại
	                case 7 -> String.class; // Địa chỉ
	                case 8 -> Integer.class; // ID
	                default -> String.class;
	            };
	        }

	        @Override
	        public boolean isCellEditable(int row, int column) {
	            return false; // Tất cả các ô không thể chỉnh sửa
	        }
	    };

	    // Thêm các cột vào model
	    model.addColumn("No.");
	    model.addColumn("Avatar");
	    model.addColumn("Full Name");
	    model.addColumn("Date of Birth");
	    model.addColumn("Gender");
	    model.addColumn("Email");
	    model.addColumn("Phone Number");
	    model.addColumn("Address");
	    model.addColumn("ID");

	    // Lấy danh sách sinh viên đã bị xóa (IsDelete = 1)
	    var deletedStudents = studentDao.selectDeletedStudents();

	    if (deletedStudents != null && !deletedStudents.isEmpty()) {
	        var count = 1;
	        for (StudentEntity student : deletedStudents) {
	            // Tải hình ảnh avatar
	            ImageIcon avatarIcon = null;
	            if (student.getAvatar() != null && !student.getAvatar().isEmpty()) {
	                var resource = "src/main/resources/avatar/" + student.getAvatar();
	                avatarIcon = new ImageIcon(
	                        new ImageIcon(resource).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
	            }

	            if (avatarIcon == null) {
	                // Sử dụng ảnh mặc định nếu không tìm thấy avatar
	                var defaultResource = getClass().getClassLoader().getResource("avatar/default_avatar.png");
	                if (defaultResource != null) {
	                    avatarIcon = new ImageIcon(
	                            new ImageIcon(defaultResource).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
	                }
	            }

	            // Chuyển LocalDate sang String
	            var dateOfBirth = student.getDateOfBirth() != null ? student.getDateOfBirth().toString() : "N/A";

	            // Thêm dữ liệu vào bảng
	            model.addRow(new Object[] {
	                    count++, avatarIcon, student.getFullName(), dateOfBirth, student.getGender(),
	                    student.getEmail(), student.getPhoneNumber(), student.getAddress(), student.getStudentID()
	            });
	        }
	    } else {
	        // Nếu không có dữ liệu, hiển thị một dòng thông báo
	        model.addRow(new Object[] { "No students found", "", "", "", "", "", "", "", "" });
	    }
	    tableStudent.setModel(model);

	 // Kiểm tra và ẩn cột ID nếu tồn tại
	        tableStudent.getColumnModel().getColumn(0).setMinWidth(0);
	        tableStudent.getColumnModel().getColumn(0).setMaxWidth(0);
	        tableStudent.getColumnModel().getColumn(0).setPreferredWidth(0);
	        
	        tableStudent.getColumnModel().getColumn(8).setMinWidth(0);
	        tableStudent.getColumnModel().getColumn(8).setMaxWidth(0);
	        tableStudent.getColumnModel().getColumn(8).setPreferredWidth(0);
	 
		
	    customizeTable(tableStudent); // Hàm tùy chỉnh giao diện bảng nếu có
	}
	private void panel1Activated() {
		var authorDao = new AuthorDao();

		DefaultTableModel model = new DefaultTableModel() {
			/**
			 *
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Class<?> getColumnClass(int column) {

				return switch (column) {
				case 0 -> Integer.class;
				case 1 -> String.class;
				case 2 -> String.class;
				case 3 -> String.class;
				case 4 -> Integer.class;
				case 5 -> Boolean.class;

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
				default -> true;
				};
			}
		};

		model.addColumn("No.");
		model.addColumn("Author Name");
		model.addColumn("Date of Birth");
		model.addColumn("Nationality");
		model.addColumn("ID");
		model.addColumn("isSelected");

//	    model.addColumn("Action");

		var deletedAuthors = authorDao.selectDeletedAuthors();

		// Kiểm tra nếu danh sách deletedAuthors không rỗng
		if (deletedAuthors != null && !deletedAuthors.isEmpty()) {
			var count = 1;
			for (AuthorEntity author : deletedAuthors) {
				// Chuyển đổi LocalDate sang String (format ngày tháng)
				var dateOfBirth = author.getDateOfBirth() != null ? author.getDateOfBirth().toString() // Chuyển
																											// LocalDate
																											// thành
																											// chuỗi
						: "N/A"; // Nếu không có ngày sinh

				model.addRow(new Object[] { count++, author.getFullName(), dateOfBirth, author.getNationality(),
						author.getIdAuthorID(), author.isSelected() });
			}
		} else {
			// Nếu không có dữ liệu, hiển thị một dòng thông báo
			model.addRow(new Object[] { "No authors found", "", "", "", "" });
		}

		tableAuthor.setModel(model);
		tableAuthor.getColumnModel().getColumn(5).setMinWidth(0);
		tableAuthor.getColumnModel().getColumn(5).setMaxWidth(0);
		tableAuthor.getColumnModel().getColumn(5).setPreferredWidth(0);
		
		tableAuthor.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				var selectedRow = tableAuthor.getSelectedRow();
				if (selectedRow != -1) {
					Object valueAt = tableAuthor.getValueAt(selectedRow, 5);
					isSelected =  (Boolean) valueAt;
					btnDeleteAuth1.setEnabled(!isSelected);
				}
			}
			
		});
		customizeTable(tableAuthor); // Hàm này tùy chỉnh giao diện bảng nếu có
	}

	private void panelPublisherActivated() {
		var dao = new PublisherDao();

		DefaultTableModel model = new DefaultTableModel() {
			private static final long serialVersionUID = 1L;

//		        @Override
//		        public boolean isCellEditable(int row, int column) {
//		            return column == 5; // Chỉ cột "Action" có thể chỉnh sửa
//		        }
		};

		model.addColumn("No.");
		model.addColumn("Name");
		model.addColumn("Address");
		model.addColumn("Phone Number");
		model.addColumn("Email");
		model.addColumn("ID");
		model.addColumn("isSelected");

		var publishers = dao.selectDeletedPublishers(); // Lấy danh sách bị xóa
		var count = 1;
		for (var publisher : publishers) {
			model.addRow(new Object[] { count++, publisher.getName(), publisher.getAddress(),
					publisher.getPhoneNumber(), publisher.getEmail(), publisher.getPublisherID(), publisher.isSelected() });
		}

		tablePublisher.setModel(model); // Cập nhật model cho bảng
		tablePublisher.getColumnModel().getColumn(6).setMinWidth(0);
		tablePublisher.getColumnModel().getColumn(6).setMaxWidth(0);
		tablePublisher.getColumnModel().getColumn(6).setPreferredWidth(0);
		
		tablePublisher.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				var selectedRow = tablePublisher.getSelectedRow();
				if (selectedRow !=-1) {
					Object valueAt = tablePublisher.getValueAt(selectedRow, 6);
					isSelected =  (Boolean) valueAt;
					btnDeletePub.setEnabled(!isSelected);
				}
			}
			
		});
		customizeTable(tablePublisher); // Tùy chỉnh giao diện bảng

	}

	private void panelBookActivated() {
	    var bookDao = new BookManagementDao(); // Tạo đối tượng BookDao để truy vấn
	    
	    DefaultTableModel model = new DefaultTableModel() {
	        private static final long serialVersionUID = 1L;

	        @Override
	        public Class<?> getColumnClass(int column) {
	            return switch (column) {
	                case 0 -> Integer.class; // ID sách
	                case 1 -> ImageIcon.class; // Ảnh sách
	                case 2 -> String.class; // Tiêu đề sách
	                case 3 -> String.class; // Tác giả
	                case 4 -> String.class; // Nhà xuất bản
	                case 5 -> String.class; // ISBN
	                case 6 -> String.class; // Danh mục
	                case 7 -> Integer.class; // Số lượng
	                case 8 -> Integer.class; // Số lượng trong kho
	                case 9 -> BigDecimal.class; // Giá
	                case 10 -> BigDecimal.class; // Giá thuê
	                case 11 -> String.class; // Ngôn ngữ
	                case 12 -> Integer.class; // ID sách (cột ẩn)
	                default -> Object.class;
	            };
	        }

	        @Override
	        public boolean isCellEditable(int row, int column) {
	            return false; // Tất cả các ô không thể chỉnh sửa
	        }
	    };

	    // Thêm các cột vào model
	    model.addColumn("No.");
	    model.addColumn("Image");
	    model.addColumn("Title");
	    model.addColumn("Author");
	    model.addColumn("Publisher");
	    model.addColumn("ISBN");
	    model.addColumn("Category");
	    model.addColumn("Quantity");
	    model.addColumn("Stock Quantity");
	    model.addColumn("Price");
	    model.addColumn("Rental Price");
	    model.addColumn("Language");
	    model.addColumn("BookID"); // Cột ẩn chứa BookID

	    // Lấy danh sách sách đã bị xóa từ database
	    List<BookManagementEntity> books = bookDao.selectDeleteBook();

	    if (books != null && !books.isEmpty()) {
	        var count = 1;
	        for (BookManagementEntity book : books) {
	            // Tải ảnh bìa
	            ImageIcon bookImage = null;
	            if (book.getImage() != null && !book.getImage().isEmpty()) {
	                var imagePath = "src/main/resources/images/" + book.getImage();
	                bookImage = new ImageIcon(new ImageIcon(imagePath).getImage().getScaledInstance(50, 70, Image.SCALE_SMOOTH));
	            }

	            // Nếu không có ảnh, sử dụng ảnh mặc định
	            if (bookImage == null) {
	                var defaultImagePath = getClass().getClassLoader().getResource("images/default_avatar.png");
	                if (defaultImagePath != null) {
	                    bookImage = new ImageIcon(new ImageIcon(defaultImagePath).getImage().getScaledInstance(50, 70, Image.SCALE_SMOOTH));
	                }
	            }

	            // Thêm dữ liệu vào bảng
	            model.addRow(new Object[] {
	                count++,
	                bookImage,
	                book.getTitle(),
	                getAuthorName(book.getAuthorID()),
	                getPublisherName(book.getPublisherID()),
	                book.getIsbn(),
	                book.getCategory(),
	                book.getQuantity(),
	                book.getStockQuantity(),
	                book.getPrice(),
	                book.getRentalPrice(),
	                book.getLanguage(),
	                book.getBookID() // Cột BookID (ẩn)
	            });
	        }
	    } else {
	        // Nếu không có dữ liệu, hiển thị thông báo
	        JOptionPane.showMessageDialog(null, "No deleted books found.", "Info", JOptionPane.INFORMATION_MESSAGE);
	    }

	    // Thiết lập model cho bảng
	    tableBook.setModel(model);

	    // Ẩn cột BookID
	    tableBook.getColumnModel().getColumn(0).setMinWidth(0);
	    tableBook.getColumnModel().getColumn(0).setMaxWidth(0);
	    tableBook.getColumnModel().getColumn(0).setPreferredWidth(0);
	    
	    tableBook.getColumnModel().getColumn(6).setMinWidth(0);
	    tableBook.getColumnModel().getColumn(6).setMaxWidth(0);
	    tableBook.getColumnModel().getColumn(6).setPreferredWidth(0);
	    
	    tableBook.getColumnModel().getColumn(7).setMinWidth(0);
	    tableBook.getColumnModel().getColumn(7).setMaxWidth(0);
	    tableBook.getColumnModel().getColumn(7).setPreferredWidth(0);
	    
	    tableBook.getColumnModel().getColumn(8).setMinWidth(0);
	    tableBook.getColumnModel().getColumn(8).setMaxWidth(0);
	    tableBook.getColumnModel().getColumn(8).setPreferredWidth(0);
	    
	    tableBook.getColumnModel().getColumn(9).setMinWidth(0);
	    tableBook.getColumnModel().getColumn(9).setMaxWidth(0);
	    tableBook.getColumnModel().getColumn(9).setPreferredWidth(0);
	    
	    tableBook.getColumnModel().getColumn(10).setMinWidth(0);
	    tableBook.getColumnModel().getColumn(10).setMaxWidth(0);
	    tableBook.getColumnModel().getColumn(10).setPreferredWidth(0);
	    
	    tableBook.getColumnModel().getColumn(11).setMinWidth(0);
	    tableBook.getColumnModel().getColumn(11).setMaxWidth(0);
	    tableBook.getColumnModel().getColumn(11).setPreferredWidth(0);
	    
	    tableBook.getColumnModel().getColumn(12).setMinWidth(0);
	    tableBook.getColumnModel().getColumn(12).setMaxWidth(0);
	    tableBook.getColumnModel().getColumn(12).setPreferredWidth(0);

	    // Tùy chỉnh giao diện bảng
	    customizeTable(tableBook);
	}
	private String getAuthorName(int authorID) {
	    var authorDao = new AuthorDao();
	    return authorDao.getAuthorName(authorID).orElse("Unknown Author");
	}

	private String getPublisherName(int publisherID) {
	    var publisherDao = new PublisherDao();
	    return publisherDao.getPublisherName(publisherID).orElse("Unknown Publisher");
	}

//	private String getCategoryName(int categoryID) {
//	    var categoryDao = new CategoriesDao();
//	    return categoryDao.getCategoryName(categoryID).orElse("Unknown Category");
//	}

	private void customizeTable(JTable table) {
		table.setFont(new Font("Arial", Font.PLAIN, 14));
		table.setRowHeight(80); // Chiều cao của mỗi hàng
		table.setGridColor(new Color(224, 224, 224));

		// Căn chỉnh từng dòng (giữ nguyên các dòng trong Look and Feel)
		var centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
		for (var i = 0; i < table.getColumnCount(); i++) {
			table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}

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
		// Header tùy chỉnh
		var header = table.getTableHeader();
		header.setPreferredSize(new Dimension(header.getPreferredSize().width, 40)); // Đặt chiều cao là 40
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
				((DefaultTableCellRenderer) headerRenderer).setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
				return headerRenderer;
			}

		});
	}
	private void onSearch(ActionEvent e) {
		var searchText = searchAuthor.getText().trim().toLowerCase();
		if (searchText.equals("search by author name...") || searchText.isEmpty()) {
			panel1Activated();
			return;
		}

		var dao = new AuthorDao();
		var filteredData = dao.selectDeletedAuthors().stream()
				.filter(pro -> pro.getFullName().toLowerCase().contains(searchText))
				.toList();

		var model = (DefaultTableModel) tableAuthor.getModel();
		model.setRowCount(0);
		var count = 1;
		for (var pro : filteredData) {
			model.addRow(
					new Object[] { count++, pro.getFullName(), pro.getDateOfBirth(), pro.getNationality(),
							pro.getIdAuthorID(), pro.isSelected(), });
		}
	}


	private void onSearchPublisher(ActionEvent e) {
		var searchText = searchPublisher.getText().trim().toLowerCase();
		if (searchText.equals("search by publisher name...") || searchText.isEmpty()) {
			panelPublisherActivated();
			return;
		}

		var dao = new PublisherDao();
		var filteredData = dao.selectDeletedPublishers().stream()
				.filter(publisher -> publisher.getName().toLowerCase().contains(searchText)).toList();

		var model = (DefaultTableModel) tablePublisher.getModel();
		model.setRowCount(0);
		var count = 1;
		for (var publisher : filteredData) {
			model.addRow(new Object[] { count++, publisher.getName(), publisher.getAddress(),
					publisher.getPhoneNumber(), publisher.getEmail(), publisher.isSelected() });
		}
	}

	private void onSearchStudent(ActionEvent e) {
		var searchText = searchStudent.getText().trim().toLowerCase();
		if (searchText.equals("search by student name...") || searchText.isEmpty()) {
			panelStudentActivated(); // Load lại toàn bộ danh sách nếu không có gì để tìm kiếm
			return;
		}

		var dao = new StudentDao();
		var filteredData = dao.selectDeletedStudents().stream()
				.filter(student -> student.getFullName().toLowerCase().contains(searchText)).toList();

		var model = (DefaultTableModel) tableStudent.getModel();
		model.setRowCount(0); // Xóa toàn bộ dữ liệu hiện tại trong bảng

		var count = 1;
		for (var student : filteredData) {
			// Lấy đường dẫn Avatar và tạo ImageIcon nếu có
			ImageIcon avatar = null;
			if (student.getAvatar() != null && !student.getAvatar().isEmpty()) {
				avatar = new ImageIcon("src/main/resources/avatar/" +  student.getAvatar());
				// Resize ảnh nếu cần
				var img = avatar.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
				avatar = new ImageIcon(img);
			} else {
				avatar = new ImageIcon("default_avatar.png"); // Ảnh mặc định nếu không có Avatar
			}

			// Chuyển đổi LocalDate sang chuỗi
			var dateOfBirth = student.getDateOfBirth() != null ? student.getDateOfBirth().toString() : "N/A";

			// Thêm dữ liệu vào bảng
			model.addRow(new Object[] { count++, avatar, student.getFullName(), dateOfBirth, student.getGender(),
					student.getEmail(), student.getPhoneNumber(), student.getAddress(), student.getStudentID() });
		}
	}
	
	private void onSearchBook(ActionEvent e) {
		var searchText = searchBook.getText().trim().toLowerCase();
		if (searchText.equals("search by student name...") || searchText.isEmpty()) {
			panelBookActivated(); // Load lại toàn bộ danh sách nếu không có gì để tìm kiếm
			return;
		}

		var dao = new BookManagementDao();
		var filteredData = dao.selectDeleteBook().stream()
				.filter(book -> book.getTitle().toLowerCase().contains(searchText)).toList();

		var model = (DefaultTableModel) tableBook.getModel();
		model.setRowCount(0); // Xóa toàn bộ dữ liệu hiện tại trong bảng

		var count = 1;
		for (var book : filteredData) {
			// Lấy đường dẫn Avatar và tạo ImageIcon nếu có
			ImageIcon bookImage = null;
            if (book.getImage() != null && !book.getImage().isEmpty()) {
                var imagePath = "src/main/resources/images/" + book.getImage();
                bookImage = new ImageIcon(new ImageIcon(imagePath).getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH));
            }

            // Nếu không có ảnh, sử dụng ảnh mặc định
            if (bookImage == null) {
                var defaultImagePath = getClass().getClassLoader().getResource("images/default_avatar.png");
                if (defaultImagePath != null) {
                    bookImage = new ImageIcon(new ImageIcon(defaultImagePath).getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH));
                }
            }

            // Thêm dữ liệu vào bảng
            model.addRow(new Object[] {
                count++,
                bookImage,
                book.getTitle(),
                getAuthorName(book.getAuthorID()),
                getPublisherName(book.getPublisherID()),
                book.getIsbn(),
                book.getCategory(),
                book.getQuantity(),
                book.getStockQuantity(),
                book.getPrice(),
                book.getRentalPrice(),
                book.getLanguage(),
                book.getBookID() // Cột BookID (ẩn)
            });
		}
	}

}