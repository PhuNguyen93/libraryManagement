package gui;

import javax.swing.JLayeredPane;
import javax.swing.UIManager;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.TimingTargetAdapter;

import LoginRegister.PanelCover;
import LoginRegister.PanelLoginRegister;
import net.miginfocom.swing.MigLayout;

public class LoginFrame extends javax.swing.JFrame {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private MigLayout layout;
	private PanelCover cover;
	private PanelLoginRegister loginAndRegister;
	private boolean isLogin = true;
	private final double addSize = 30;
	private final double coverSize = 40;
	private final double loginSize = 60;
	private JLayeredPane bg;

	public LoginFrame() {
		setResizable(false);
		setTitle("Login & Register");
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setSize(933, 537);
		setLocationRelativeTo(null);

		initComponents();
		init();

	}

	private void initComponents() {
		bg = new JLayeredPane();
		bg.setBackground(new java.awt.Color(255, 255, 255));
		bg.setOpaque(true);
		setContentPane(bg);
	}

	private void init() {
		layout = new MigLayout("fill, insets 0");
		cover = new PanelCover();
		loginAndRegister = new PanelLoginRegister();

		// Đặt trạng thái mặc định là đăng ký
		isLogin = true; // Hiển thị giao diện đăng ký ngay từ đầu
		cover.login(isLogin);
		loginAndRegister.showRegister(!isLogin); // Đồng bộ với trạng thái ban đầu

		TimingTarget target = new TimingTargetAdapter() {
			@Override
			public void timingEvent(float fraction) {
				double fractionCover = isLogin ? (1f - fraction) : fraction;
				double fractionLogin = isLogin ? fraction : (1f - fraction);

				// Giới hạn giá trị trong khoảng [0, 1]
				fractionCover = Math.max(0, Math.min(1, fractionCover));
				fractionLogin = Math.max(0, Math.min(1, fractionLogin));

				// Làm tròn giá trị về 3 chữ số thập phân
				fractionCover = Math.round(fractionCover * 1000) / 1000.0;
				fractionLogin = Math.round(fractionLogin * 1000) / 1000.0;

				var size = coverSize + (fraction <= 0.5f ? fraction * addSize : addSize - fraction * addSize);

				layout.setComponentConstraints(cover, "width " + size + "%, pos " + fractionCover + "al 0 n 100%");
				layout.setComponentConstraints(loginAndRegister,
						"width " + loginSize + "%, pos " + fractionLogin + "al 0 n 100%");
				bg.revalidate();
			}

			@Override
			public void end() {
				isLogin = !isLogin;
				cover.login(isLogin);
				loginAndRegister.showRegister(!isLogin);
			}
		};

		var animator = new Animator(800, target);
		animator.setAcceleration(0.5f);
		animator.setDeceleration(0.5f);

		bg.setLayout(layout);
		// Cập nhật vị trí các thành phần phù hợp với trạng thái mặc định
		bg.add(cover, "width " + coverSize + "%, pos 1al 0 n 100%"); // Đặt cover sang trạng thái đăng ký
		bg.add(loginAndRegister, "width " + loginSize + "%, pos 0al 0 n 100%"); // Đặt loginAndRegister ở đúng trạng
																				// thái

		cover.setEvent(ae -> {
			if (!animator.isRunning()) {
				animator.start();
			}
		});
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		java.awt.EventQueue.invokeLater(() -> {
			new LoginFrame().setVisible(true);
		});
	}
}
