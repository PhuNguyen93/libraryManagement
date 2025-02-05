package view;

import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import dao.BookManagementDao;
import dao.StudentDao;
import entity.UserEntity;

public class UserViewDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private JTextField txtFullName, txtEmail, txtPhoneNumber, txtAddress, txtBooksAdded, txtStudentsAdded;
	private JRadioButton activeRadio, inactiveRadio;
	private JPanel panelAvatar;
	private BookManagementDao bookManagementDao;
	private StudentDao studentDao;

	public UserViewDialog(UserEntity user) {
		this.bookManagementDao = new BookManagementDao();
		this.studentDao = new StudentDao();

		setTitle("User Details");
		setBounds(100, 100, 580, 572); // Kích thước dialog
		setLocationRelativeTo(null); // Căn giữa
		getContentPane().setLayout(null);

		// Gradient Panel
		JPanel gradientPanel = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(java.awt.Graphics g) {
				var g2d = (java.awt.Graphics2D) g;
				var gradient = new java.awt.GradientPaint(0, 0, Color.decode("#F8CDDA"), 0, getHeight(),
						Color.decode("#1D2B64"));
				g2d.setPaint(gradient);
				g2d.fillRect(0, 0, getWidth(), getHeight());
			}
		};
		gradientPanel.setBounds(0, 0, 580, 520);
		gradientPanel.setLayout(null);
		setContentPane(gradientPanel);

		// Avatar Panel
		panelAvatar = new JPanel();
		panelAvatar.setBounds(20, 20, 100, 100);
		panelAvatar.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		gradientPanel.add(panelAvatar);

		// Labels and Text Fields
		String[] labels = { "Full Name:", "Email:", "Phone Number:", "Address:", "Books Added:", "Students Added:",
				"Status:" };
		int labelX = 140, labelY = 30, labelWidth = 120, labelHeight = 30;
		int fieldX = 270, fieldY = 30, fieldWidth = 260, fieldHeight = 30;

		for (var i = 0; i < labels.length; i++) {
			JLabel label = new JLabel(labels[i]);
			label.setBounds(labelX, labelY + i * 50, labelWidth, labelHeight);
			label.setFont(new Font("Arial", Font.BOLD, 14));
			label.setForeground(Color.WHITE);
			gradientPanel.add(label);

			if (labels[i].equals("Status:")) {
				activeRadio = new JRadioButton("Active");
				activeRadio.setBounds(fieldX, fieldY + i * 50, 100, fieldHeight);
				activeRadio.setEnabled(false);
				gradientPanel.add(activeRadio);

				inactiveRadio = new JRadioButton("Inactive");
				inactiveRadio.setBounds(fieldX + 120, fieldY + i * 50, 100, fieldHeight);
				inactiveRadio.setEnabled(false);
				gradientPanel.add(inactiveRadio);

				var group = new ButtonGroup();
				group.add(activeRadio);
				group.add(inactiveRadio);
			} else {
				var textField = new JTextField();
				textField.setBounds(fieldX, fieldY + i * 50, fieldWidth, fieldHeight);
				textField.setEditable(false);
				textField.setFocusable(false);
				textField.setBackground(new Color(240, 248, 255));
				textField.setBorder(BorderFactory.createLineBorder(new Color(173, 216, 230)));
				gradientPanel.add(textField);

				switch (labels[i]) {
				case "Full Name:" -> txtFullName = textField;
				case "Email:" -> txtEmail = textField;
				case "Phone Number:" -> txtPhoneNumber = textField;
				case "Address:" -> txtAddress = textField;
				case "Books Added:" -> txtBooksAdded = textField;
				case "Students Added:" -> txtStudentsAdded = textField;
				}
			}
		}

		// Close Button
		var closeButton = new GradientButton("Close");
		closeButton.setBounds(210, 450, 140, 40);
		closeButton.addActionListener(e -> dispose());
		gradientPanel.add(closeButton);

		// Load Data
		loadUserData(user);
	}

	private void loadUserData(UserEntity user) {
		txtFullName.setText(user.getFullName());
		txtEmail.setText(user.getEmail());
		txtPhoneNumber.setText(user.getPhoneNumber());
		txtAddress.setText(user.getAddress());
		txtBooksAdded.setText(String.valueOf(bookManagementDao.countBooksByUserId(user.getUserID())));
		txtStudentsAdded.setText(String.valueOf(studentDao.countStudentsByUserId(user.getUserID())));

		if (user.isActive()) {
			activeRadio.setSelected(true);
		} else {
			inactiveRadio.setSelected(true);
		}

		// Avatar
		var targetFolder = "src/main/resources/avatar/";
		setAvatar(targetFolder + user.getAvatar());
	}

	private void setAvatar(String avatarPath) {
		try {
			var imageIcon = new ImageIcon(
					new ImageIcon(avatarPath).getImage().getScaledInstance(90, 90, java.awt.Image.SCALE_SMOOTH));
			var label = new JLabel(imageIcon);
			label.setBounds((panelAvatar.getWidth() - 90) / 2, (panelAvatar.getHeight() - 90) / 2, 90, 90);
			panelAvatar.removeAll();
			panelAvatar.add(label);
			panelAvatar.revalidate();
			panelAvatar.repaint();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Gradient Button
	class GradientButton extends JButton {
		private static final long serialVersionUID = 1L;

		public GradientButton(String text) {
			super(text);
			setContentAreaFilled(false);
			setFocusPainted(false);
			setFont(new Font("Arial", Font.BOLD, 14));
			setForeground(Color.WHITE);
		}

		@Override
		protected void paintComponent(java.awt.Graphics g) {
			var g2d = (java.awt.Graphics2D) g.create();
			var gradient = new java.awt.GradientPaint(0, 0, Color.decode("#c2e59c"), 0, getHeight(),
					Color.decode("#64b3f4"));
			g2d.setPaint(gradient);
			g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
			g2d.dispose();
			super.paintComponent(g);
		}
	}
}
