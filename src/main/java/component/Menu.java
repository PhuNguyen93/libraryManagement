package component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import dao.UserDao;
import edit.ChangePasswordDialog;
import edit.UserInfoDialog;
import entity.UserSession;
import gui.LoginFrame;

public class Menu extends JPanel {

	private static final long serialVersionUID = 1L;
	private JPanel panelChart;
	private JLabel lblUserName;
	private RoundedPanel Avatar;
	private JButton btnBookRental;
	private JButton btnStudent;
	private JButton btnBook;
	private JButton btnAuthor;
	private JButton btnPersonnel;
	private JButton btnOrder;
	private JButton btnDamage;
	private JButton btnWarehouse;
	private JButton btnRecycle;
	private JButton btnLogout;
	private JLabel lblMyData;

	private JPanel studentPanel; // Panel cho Student
	private JPanel authorPanel; // Panel cho Author
	private JPanel bookRentalPanel; // Panel cho Book Rental
	private JPanel recyclePanel;

	private JPanel bookManagementPanel; // Panel cho Student
	private JPanel wareHousePanel; // Panel cho Author
	private JPanel personnelPanel; // Panel cho Book Rental
	private JPanel orderPanel;
	private JPanel damagePanel;
	private JPanel chartPanel; // Chart Panel
	private UserSession userSession;
	private JLabel avatarImage;;
	private UserInfoDialog userInfoDialog;

	/**
	 * Create the panel.
	 */
	public Menu() {
		setMinimumSize(new Dimension(800, 800));
		setOpaque(false);
		setLayout(null);

		panelChart = new JPanel();
		panelChart.setOpaque(false);
		panelChart.setBounds(10, 30, 195, 65);
		add(panelChart);
		panelChart.setLayout(null);

		lblUserName = new JLabel("Library");
		lblUserName.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				lblNewLabelMouseClicked(e);
			}
		});
		lblUserName.setForeground(new Color(255, 255, 255));
		lblUserName.setFont(new Font("SansSerif", Font.BOLD, 18));
		lblUserName.setToolTipText("");
		lblUserName.setBounds(76, 11, 109, 43);
		panelChart.add(lblUserName);

		Avatar = new RoundedPanel();
		Avatar.setBounds(10, 11, 53, 53);
		Avatar.setLayout(null);
		panelChart.add(Avatar);

		btnBookRental = new JButton("Book Rental");
		btnBookRental.setIcon(new ImageIcon(Menu.class.getResource("/icon2/open-book.png")));
		btnBookRental.setBounds(10, 106, 195, 49);
		add(btnBookRental);
		btnBookRental.setHorizontalAlignment(SwingConstants.LEFT);
		btnBookRental.setIconTextGap(20);
		btnBookRental.setFont(btnBookRental.getFont().deriveFont(14.0f));
		setupButton(btnBookRental);
		btnBookRental.addActionListener(this::btnBookRentalActionPerformed);

		btnStudent = new JButton("Student");
		btnStudent.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				btnStudentMouseClicked(e);
			}
		});
		btnStudent.addActionListener(this::btnStudentActionPerformed);
		btnStudent.setIcon(new ImageIcon(Menu.class.getResource("/icon2/reading-book.png")));
		btnStudent.setBounds(10, 166, 195, 49);
		add(btnStudent);
		btnStudent.setHorizontalAlignment(SwingConstants.LEFT);
		btnStudent.setIconTextGap(20);
		btnStudent.setFont(btnStudent.getFont().deriveFont(14.0f));
		setupButton(btnStudent);

		btnBook = new JButton("Book Management");
		btnBook.addActionListener(this::btnBookActionPerformed);
		btnBook.setIcon(new ImageIcon(Menu.class.getResource("/icon2/execution.png")));
		btnBook.setBounds(10, 226, 195, 49);
		add(btnBook);
		btnBook.setHorizontalAlignment(SwingConstants.LEFT);
		btnBook.setIconTextGap(20);
		btnBook.setFont(btnBook.getFont().deriveFont(14.0f));
		setupButton(btnBook);

		btnAuthor = new JButton("Bibliographic Data");
		btnAuthor.addActionListener(this::btnAuthorActionPerformed);
		btnAuthor.setIcon(new ImageIcon(Menu.class.getResource("/icon3/key.png")));
		btnAuthor.setBounds(10, 286, 195, 49);
		add(btnAuthor);
		btnAuthor.setHorizontalAlignment(SwingConstants.LEFT);
		btnAuthor.setIconTextGap(20);
		btnAuthor.setFont(btnAuthor.getFont().deriveFont(14.0f));
		setupButton(btnAuthor);

		btnPersonnel = new JButton("Personnel");
		btnPersonnel.addActionListener(this::btnPersonnelActionPerformed);
		btnPersonnel.setIcon(new ImageIcon(Menu.class.getResource("/icon3/teamwork.png")));
		btnPersonnel.setBounds(10, 427, 195, 49);
		add(btnPersonnel);
		btnPersonnel.setHorizontalAlignment(SwingConstants.LEFT);
		btnPersonnel.setIconTextGap(20);
		btnPersonnel.setFont(btnPersonnel.getFont().deriveFont(14.0f));
		setupButton(btnPersonnel);

		btnOrder = new JButton("Borrow History");
		btnOrder.addActionListener(this::btnOrderActionPerformed);
		btnOrder.setIcon(new ImageIcon(Menu.class.getResource("/icon3/shopping-list.png")));
		btnOrder.setBounds(10, 487, 195, 49);
		add(btnOrder);
		btnOrder.setHorizontalAlignment(SwingConstants.LEFT);
		btnOrder.setIconTextGap(20);
		btnOrder.setFont(btnOrder.getFont().deriveFont(14.0f));
		setupButton(btnOrder);

		btnDamage = new JButton("Damage");
		btnDamage.addActionListener(this::btnDamageActionPerformed);
		btnDamage.setIcon(new ImageIcon(Menu.class.getResource("/icon3/broken.png")));
		btnDamage.setBounds(10, 547, 195, 49);
		add(btnDamage);
		btnDamage.setHorizontalAlignment(SwingConstants.LEFT);
		btnDamage.setIconTextGap(20);
		btnDamage.setFont(btnDamage.getFont().deriveFont(14.0f));
		setupButton(btnDamage);

		btnWarehouse = new JButton("Warehouse");
		btnWarehouse.addActionListener(this::btnWarehouseActionPerformed);
		btnWarehouse.setIcon(new ImageIcon(Menu.class.getResource("/icon3/warehouse.png")));
		btnWarehouse.setBounds(10, 346, 195, 49);
		add(btnWarehouse);
		btnWarehouse.setHorizontalAlignment(SwingConstants.LEFT);
		btnWarehouse.setIconTextGap(20);
		btnWarehouse.setFont(btnWarehouse.getFont().deriveFont(14.0f));
		setupButton(btnWarehouse);

		btnRecycle = new JButton("Recycle Bin");
		btnRecycle.addActionListener(this::btnRecycleActionPerformed);
		btnRecycle.setIcon(new ImageIcon(Menu.class.getResource("/icon3/recycle-bin.png")));
		btnRecycle.setBounds(10, 607, 195, 49);
		add(btnRecycle);
		btnRecycle.setHorizontalAlignment(SwingConstants.LEFT);
		btnRecycle.setIconTextGap(20);
		btnRecycle.setFont(btnRecycle.getFont().deriveFont(14.0f));
		setupButton(btnRecycle);

		btnLogout = new JButton("Logout");
		btnLogout.addActionListener(this::btnLogoutActionPerformed);
		btnLogout.setIcon(new ImageIcon(Menu.class.getResource("/icon3/logout.png")));
		btnLogout.setBounds(10, 667, 195, 49);
		add(btnLogout);
		btnLogout.setHorizontalAlignment(SwingConstants.LEFT);
		btnLogout.setIconTextGap(20);
		btnLogout.setFont(btnLogout.getFont().deriveFont(14.0f));
		setupButton(btnLogout);

		lblMyData = new JLabel("Admin Data");
		lblMyData.setToolTipText("");
		lblMyData.setForeground(Color.WHITE);
		lblMyData.setFont(new Font("SansSerif", Font.BOLD, 18));
		lblMyData.setBounds(10, 395, 197, 30);
		add(lblMyData);

		btnEditAvatar = new JButton("");
		btnEditAvatar.setBounds(10, 30, 78, 65);
		add(btnEditAvatar);
//		btnEditAvatar.addActionListener(this::btnNewButtonActionPerformed);

		// Thêm thuộc tính để nút trong suốt và không viền
		btnEditAvatar.setBorder(null); // Không viền
		btnEditAvatar.setContentAreaFilled(false); // Bỏ nền
		btnEditAvatar.setBorderPainted(false); // Không vẽ viền
		btnEditAvatar.setFocusPainted(false); // Không focus
		btnEditAvatar.setOpaque(false);

		avatarImage = new JLabel();
		avatarImage.setPreferredSize(new Dimension(53, 53));
		avatarImage.setBounds(0, 0, 53, 53);
		userSession = UserSession.getInstance();
		var imagePath = "src/main/resources/avatar/avaaaa.png";
		if (userSession.getAvatar() != null) {

			imagePath = "src/main/resources/avatar/" + userSession.getAvatar();
			lblUserName.setText(userSession.getUsername());
		}
		if (imagePath != null) {
			var icon = new ImageIcon(imagePath);
			avatarImage.setIcon(new ImageIcon(icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
		}
		Avatar.add(avatarImage);
		Avatar.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {
					showPopupMenu(e);
				}
			}
		});
	}

	private void clearEffct(JButton button) {
		button.setOpaque(false);
		button.setContentAreaFilled(false);
		button.setBorderPainted(false);
		button.setFocusPainted(false);
		button.setBackground(new Color(255, 255, 255, 250));
		button.setForeground(Color.WHITE);
	}

	private JButton selectedButton = null;
	private JButton btnEditAvatar;

	private void setupButton(JButton button) {
		button.setOpaque(false);
		button.setContentAreaFilled(false);
		button.setBorderPainted(false);
		button.setFocusPainted(false);
		clearEffct(button);

		button.setBackground(new Color(255, 255, 255, 250));
		button.setForeground(Color.WHITE);

		button.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent e) {
				if (button != selectedButton) {
					button.setContentAreaFilled(true);
					button.setBackground(new Color(255, 255, 255, 250));
					button.setForeground(Color.BLACK);
				}
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent e) {
				if (button != selectedButton) {
					button.setBackground(new Color(255, 255, 255, 250));
					button.setContentAreaFilled(false);
					button.setForeground(Color.WHITE);
				}
			}
		});

		button.addActionListener(e -> {
			if (selectedButton != null) {
				clearEffct(selectedButton);
			}
			selectedButton = button;
			button.setContentAreaFilled(true);
			button.setBackground(new Color(255, 255, 255, 250));
			button.setForeground(Color.BLACK);
		});
	}

	@Override
	protected void paintComponent(Graphics grphcs) {
		var g2 = (Graphics2D) grphcs;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		var g = new GradientPaint(0, 0, Color.decode("#F8CDDA"), 0, getHeight(), Color.decode("#1D2B64"));
		g2.setPaint(g);
		g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

		super.paintComponent(grphcs);
	}

	class RoundedPanel extends JPanel {
		private static final long serialVersionUID = 1L;

		@Override
		protected void paintComponent(Graphics g) {
			var g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			g2.setColor(getBackground());
			g2.fillOval(0, 0, getWidth(), getHeight());

			super.paintChildren(g);
		}
	}

	private void setPanelVisible(JPanel panelObj, boolean isVisible) {
		if (chartPanel != null) {
			chartPanel.setVisible(false);
		}
		if (studentPanel != null) {
			studentPanel.setVisible(false);
		}
		if (authorPanel != null) {
			authorPanel.setVisible(false);
		}
		if (bookRentalPanel != null) {
			bookRentalPanel.setVisible(false);
		}
		if (recyclePanel != null) {
			recyclePanel.setVisible(false);
		}
		if (bookManagementPanel != null) {
			bookManagementPanel.setVisible(false);
		}
		if (wareHousePanel != null) {
			wareHousePanel.setVisible(false);
		}
		if (personnelPanel != null) {
			personnelPanel.setVisible(false);
		}
		if (orderPanel != null) {
			orderPanel.setVisible(false);
		}
		if (damagePanel != null) {
			damagePanel.setVisible(false);
		}

		if (panelObj != null) {
			panelObj.setVisible(isVisible);
		} else {
			System.out.println("Panel to show is null!");
		}
	}

	protected void btnStudentActionPerformed(ActionEvent e) {
	}

	public void setStudentPanel(JPanel panel) {
		this.studentPanel = panel;
	}

	protected void btnStudentMouseClicked(MouseEvent e) {

		setPanelVisible(studentPanel, true);

	}

	public void setAuthorPanel(JPanel panel) {
		this.authorPanel = panel;

	}

	public void setBookRentalPanel(JPanel panel) {
		this.bookRentalPanel = panel;

	}

	public void setRecyclePanel(JPanel panel) {
		this.recyclePanel = panel;

	}

	public void setBookManagementPanel(JPanel panel) {
		this.bookManagementPanel = panel;
	}

	public void setWareHousePanel(JPanel panel) {
		this.wareHousePanel = panel;
	}

	public void setPersonnelPanel(JPanel panel) {
		this.personnelPanel = panel;
	}

	public void setOrderPanel(JPanel panel) {
		this.orderPanel = panel;
	}

	public void setDamagePanel(JPanel panel) {
		this.damagePanel = panel;
	}

	public void setChartPanel(JPanel panel) {
		this.chartPanel = panel;
	}

	protected void btnAuthorActionPerformed(ActionEvent e) {
		setPanelVisible(authorPanel, true);

	}

	protected void btnBookRentalActionPerformed(ActionEvent e) {
		setPanelVisible(bookRentalPanel, true);
	}

	protected void btnRecycleActionPerformed(ActionEvent e) {
		setPanelVisible(recyclePanel, true);
	}

	protected void btnBookActionPerformed(ActionEvent e) {
		setPanelVisible(bookManagementPanel, true);
	}

	protected void btnWarehouseActionPerformed(ActionEvent e) {
		setPanelVisible(wareHousePanel, true);
	}

	protected void btnPersonnelActionPerformed(ActionEvent e) {
		setPanelVisible(personnelPanel, true);
	}

	protected void btnOrderActionPerformed(ActionEvent e) {
		setPanelVisible(orderPanel, true);
	}

	protected void btnDamageActionPerformed(ActionEvent e) {
		setPanelVisible(damagePanel, true);
	}

	protected void lblNewLabelMouseClicked(MouseEvent e) {
		setPanelVisible(chartPanel, true); // Hiển thị Chart Panel
		
	}

	protected void btnLogoutActionPerformed(ActionEvent e) {
		logout();
	}

	public void logout() {
		var currentWindow = SwingUtilities.getWindowAncestor(this);
		if (currentWindow != null) {
			currentWindow.dispose(); // Đóng form Main hiện tại
		}
		userSession = UserSession.getInstance();
		userSession.clearSession();
		new LoginFrame().setVisible(true);
	}

	private void showPopupMenu(MouseEvent e) {
		var popupMenu = new JPopupMenu();

		var changeInfo = new JMenuItem("Change Infomation");
		changeInfo.addActionListener(event -> openUserInfoDialog());
		popupMenu.add(changeInfo);

		var changePassword = new JMenuItem("Change Password");
		changePassword.addActionListener(event -> openChangePasswordDialog());
		popupMenu.add(changePassword);

		var logout = new JMenuItem("Log Out");
		logout.addActionListener(event -> logout());
		popupMenu.add(logout);

		popupMenu.show(Avatar, e.getX(), e.getY());
	}

	private void openUserInfoDialog() {
		var userSession = UserSession.getInstance();
		var userDao = new UserDao();
		var user = userDao.getUserById(userSession.getUserId());

		if (user == null) {
			JOptionPane.showMessageDialog(this, "User not found!", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		userInfoDialog = new UserInfoDialog(this);
		userInfoDialog.setUserData(user);
		userInfoDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		userInfoDialog.setVisible(true);
	}

	private void openChangePasswordDialog() {
		var changePasswordDialog = new ChangePasswordDialog();
		changePasswordDialog.setVisible(true);
	}

//	protected void btnNewButtonActionPerformed(ActionEvent e) {
//		// Tạo JFileChooser để chọn tệp
//		var fileChooser = new javax.swing.JFileChooser();
//		fileChooser.setDialogTitle("Select Avatar Image");
//		fileChooser.setFileSelectionMode(javax.swing.JFileChooser.FILES_ONLY);
//
//		// Lọc chỉ các tệp hình ảnh (JPG, PNG)
//		var fileFilter = new javax.swing.filechooser.FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png",
//				"gif");
//		fileChooser.setFileFilter(fileFilter);
//
//		// Hiển thị dialog để chọn file
//		var result = fileChooser.showOpenDialog(this);
//		if (result == javax.swing.JFileChooser.APPROVE_OPTION) {
//			// Lấy đường dẫn tệp đã chọn
//			var selectedFile = fileChooser.getSelectedFile();
////					var avatarPath = selectedFile.getAbsolutePath();
//			var sourcePath = selectedFile.getAbsolutePath();
//
//			// Tên file được lưu trong folder avatar (sử dụng tên gốc của file)
//			var targetFolder = "src/main/resources/avatar/";
//			var extension = "";
//			var lastDotIndex = selectedFile.getName().lastIndexOf('.');
//			if (lastDotIndex > 0) {
//				extension = selectedFile.getName().substring(lastDotIndex);
//			}
//
//			var uniqueFileName = System.currentTimeMillis() + extension; // Tên file: timestamp + đuôi file
//			var targetPath = targetFolder + uniqueFileName;
//			;
//
//			try {
//				// Tạo thư mục nếu chưa tồn tại
//				var targetDirectory = new java.io.File(targetFolder);
//				if (!targetDirectory.exists()) {
//					targetDirectory.mkdirs();
//				}
//
//				// Sao chép tệp vào thư mục avatar
//				java.nio.file.Files.copy(java.nio.file.Paths.get(sourcePath), java.nio.file.Paths.get(targetPath),
//						java.nio.file.StandardCopyOption.REPLACE_EXISTING);
//				// Hiển thị avatar trong panelAvatar
//				setAvatar(targetPath);
//				updateAvatarDB(uniqueFileName);
////				mainStudent.setAvatar(uniqueFileName);
//				// Hiển thị thông báo thành công
////						JOptionPane.showMessageDialog(this, "Avatar updated successfully!", "Success",
////								JOptionPane.INFORMATION_MESSAGE);
//			} catch (Exception ex) {
//				ex.printStackTrace();
//				JOptionPane.showMessageDialog(this, "Failed to update avatar: " + ex.getMessage(), "Error",
//						JOptionPane.ERROR_MESSAGE);
//			}
//		}
//	}
//
//	public void setAvatar(String avatarPath) {
//		try {
//			// Tạo ảnh với kích thước 90x90
//			var imageIcon = new ImageIcon(
//					new ImageIcon(avatarPath).getImage().getScaledInstance(90, 90, java.awt.Image.SCALE_SMOOTH));
//			var label = new JLabel(imageIcon);
//
//			// Xóa các component cũ và thêm label mới
//
//			avatarImage.setIcon(new ImageIcon(imageIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
//			avatarImage.removeAll();
//			avatarImage.add(label);
//			avatarImage.revalidate();
//			avatarImage.repaint();
//		} catch (Exception e) {
//			e.printStackTrace();
//			JOptionPane.showMessageDialog(this, "Failed to load avatar from path: " + avatarPath, "Error",
//					JOptionPane.ERROR_MESSAGE);
//		}
//	}
//
//	public void updateAvatarDB(String uniqueFileName) {
//		new UserDao().updateAvatar(userSession.getUserId(), uniqueFileName);
//		userSession.setAvatar(uniqueFileName);
//	}

	public void refreshUserInfo() {
		var userSession = UserSession.getInstance();

		// Cập nhật tên người dùng
		lblUserName.setText(userSession.getUsername());

		// Cập nhật ảnh đại diện
		var imagePath = "src/main/resources/avatar/default.png"; // Ảnh mặc định
		if (userSession.getAvatar() != null && !userSession.getAvatar().isEmpty()) {
			imagePath = "src/main/resources/avatar/" + userSession.getAvatar();
		}

		try {
			var icon = new ImageIcon(imagePath);
			avatarImage.setIcon(new ImageIcon(icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
		} catch (Exception e) {
			System.err.println("Failed to load avatar image: " + e.getMessage());
		}

		// Làm mới giao diện
		lblUserName.repaint();
		avatarImage.repaint();
	}

	public void setupPermissions(int userRole) {
		if (userRole == 1) { // Nhân viên
			disableButton(btnPersonnel);
			disableButton(btnOrder);
			disableButton(btnDamage);
			disableButton(btnRecycle);
		}
	}

	private void disableButton(JButton button) {
		button.setEnabled(false); // Vô hiệu hóa nút
		button.setForeground(Color.LIGHT_GRAY); // Thay đổi màu chữ
	}
}