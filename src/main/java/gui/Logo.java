package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

public class Logo extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	private Point mousepoint;
	private Timer timer;
	private CustomProgressBar progressBar;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				var frame = new Logo();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Logo() {
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				thisMouseDragged(e);
			}
		});
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				thisMousePressed(e);
			}
		});
		setUndecorated(true);
		setBackground(new Color(0, 0, 0, 0));

		// Sử dụng trực tiếp ImageIcon để hỗ trợ ảnh động .gif
		var gifIcon = new ImageIcon("lib/aprotrain.gif");

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 920, 535);
		setLocationRelativeTo(null);

		contentPane = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(java.awt.Graphics g) {
				super.paintComponent(g);
				// Sử dụng ImageIcon để vẽ ảnh động
				gifIcon.paintIcon(this, g, 0, 0);
			}
		};

		contentPane.setBackground(new Color(0, 0, 0, 0));

		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		// Custom ProgressBar
		progressBar = new CustomProgressBar();
		progressBar.setFont(new Font("Tahoma", Font.BOLD, 14));
		progressBar.setBounds(60, 420, 797, 25);
		contentPane.add(progressBar);

		timer = new Timer(40, e -> {
			progressBar.setValue(progressBar.getValue() + 1);
			if (progressBar.getValue() == 100) {
				timer.stop();
				this.dispose();
				LoginFrame.main(null);
			}
		});
		timer.start();
	}

	protected void thisMousePressed(MouseEvent e) {
		mousepoint = e.getPoint();
	}

	protected void thisMouseDragged(MouseEvent e) {
		var current = e.getLocationOnScreen();
		setLocation(current.x - mousepoint.x, current.y - mousepoint.y);
	}

	/**
	 * Custom ProgressBar class with no border.
	 */
	class CustomProgressBar extends JProgressBar {
		private static final long serialVersionUID = 1L;

		public CustomProgressBar() {
			super();
			setOpaque(false);
			setPreferredSize(new Dimension(800, 25));
			setBorder(null); // Remove the default border
		}

		@Override
		protected void paintComponent(Graphics g) {
			var g2 = (Graphics2D) g;
			var width = getWidth();
			var height = getHeight();

			// Enable anti-aliasing
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			// Draw background with rounded corners
			g2.setColor(new Color(200, 200, 200, 120)); // Light gray background
			g2.fillRoundRect(0, 0, width, height, 20, 20);

			// Draw progress bar with gradient
			var progressWidth = (int) (width * getPercentComplete());
			var gradient = new GradientPaint(0, 0, new Color(255, 102, 0), progressWidth, 0,
					new Color(255, 178, 102));
			g2.setPaint(gradient);
			g2.fillRoundRect(0, 0, progressWidth, height, 20, 20);

			// Draw text
			g2.setFont(getFont());
			var text = getValue() + "%";
			var stringWidth = g2.getFontMetrics().stringWidth(text);
			var stringHeight = g2.getFontMetrics().getAscent();
			g2.setColor(Color.BLACK);
			g2.drawString(text, (width - stringWidth) / 2, (height + stringHeight) / 2 - 2);
		}
	}
}
