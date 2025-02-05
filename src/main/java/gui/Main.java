package gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import component.Menu;
import entity.UserSession;
import main.Author;
import main.BookManagement;
import main.BookRental;
import main.Chart;
import main.Damage;
import main.Order;
import main.Personnel;
import main.Recycle;
import main.Student;
import main.WareHouse;

public class Main extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private Chart chartMain; // Chart Panel
	private Student studentMain;
	private Author authorMain;
	private BookRental bookRentalMain;
	private Recycle recycleMain;
	private BookManagement bookManagementMain;
	private WareHouse wareHouseMain;
	private Personnel personnelMain;
	private Order orderMain;
	private Damage damageMain;
	private UserSession userSession;

///ok
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(() -> {
			try {
				new Logo().setVisible(true);// hiển thị logo
//				new LoginFrame().setVisible(true); // hiển thị login
//				var frame = new Main();// đóng lại login
//				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Main() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				thisWindowClosing(e);
			}
		});

		setResizable(false);
		setTitle("Library Management");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1165, 750);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(0, 0, 0, 0));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		setLocationRelativeTo(null);
		contentPane.setLayout(null);

		var menuPanel = new Menu();
		menuPanel.setBounds(0, -13, 215, 736);
		contentPane.add(menuPanel);
		menuPanel.setLayout(null);

		// áp dụng phân quyền
		menuPanel.setupPermissions(UserSession.getInstance().getUserRole());

		// Add Chart panel
		chartMain = new Chart(java.time.LocalDate.now().getYear(), java.time.LocalDate.now().getMonthValue());
		chartMain = new Chart(2025, 1);
		chartMain.setBounds(211, 0, 985, 723);
		contentPane.add(chartMain);
		chartMain.setVisible(true); // Display Chart Panel first
		menuPanel.setChartPanel(chartMain);

		// Add Student panel
		studentMain = new Student();
		studentMain.setBounds(211, 0, 985, 723);
		contentPane.add(studentMain);
		studentMain.setVisible(false);
		menuPanel.setStudentPanel(studentMain);

		// Add Author panel
		authorMain = new Author();
		authorMain.setBounds(211, 0, 985, 723);
		contentPane.add(authorMain);
		authorMain.setVisible(false);
		menuPanel.setAuthorPanel(authorMain);

		// Add Book Rental panel
		bookRentalMain = new BookRental(chartMain);
		bookRentalMain.setBounds(211, 0, 985, 723);
		contentPane.add(bookRentalMain);
		bookRentalMain.setVisible(false);
		menuPanel.setBookRentalPanel(bookRentalMain);

		// Add Recycle panel
		recycleMain = new Recycle();
		recycleMain.setBounds(211, 0, 985, 723);
		contentPane.add(recycleMain);
		recycleMain.setVisible(false);
		menuPanel.setRecyclePanel(recycleMain);

		// Add Book Management panel
		bookManagementMain = new BookManagement(chartMain);
		bookManagementMain.setBounds(211, 0, 985, 723);
		contentPane.add(bookManagementMain);
		bookManagementMain.setVisible(false);
		menuPanel.setBookManagementPanel(bookManagementMain);

		// Add Warehouse panel
		wareHouseMain = new WareHouse();
		wareHouseMain.setBounds(211, 0, 985, 723);
		contentPane.add(wareHouseMain);
		wareHouseMain.setVisible(false);
		menuPanel.setWareHousePanel(wareHouseMain);

		// Add Personnel panel
		personnelMain = new Personnel();
		personnelMain.setBounds(211, 0, 985, 723);
		contentPane.add(personnelMain);
		personnelMain.setVisible(false);
		menuPanel.setPersonnelPanel(personnelMain);

		// Add Order panel
		orderMain = new Order();
		orderMain.setBounds(211, 0, 985, 723);
		contentPane.add(orderMain);
		orderMain.setVisible(false);
		menuPanel.setOrderPanel(orderMain);

		// Add Damage panel
		damageMain = new Damage();
		damageMain.setBounds(211, 0, 985, 723);
		contentPane.add(damageMain);
		damageMain.setVisible(false);
		menuPanel.setDamagePanel(damageMain);

	}
	public static Student getStudentPanel() {
	    return ((Main) JFrame.getFrames()[0]).studentMain;
	}
	
	public static BookManagement getBookPanel() {
	    return ((Main) JFrame.getFrames()[0]).bookManagementMain;
	}


	protected void thisWindowClosed(WindowEvent e) {
//		new Menu().logout();
	}

	protected void thisWindowClosing(WindowEvent e) {
//		new Menu().logout();
		userSession = UserSession.getInstance();
		userSession.clearSession();
	}
}
