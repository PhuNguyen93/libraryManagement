package LoginRegister;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

public class PanelCover extends JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private JLabel title;
	private JLabel description;
	private JButton button;
	private ActionListener event;

	public PanelCover() {
		setOpaque(false);
		setLayout(new MigLayout("wrap, fill", "[center]", "push[]25[]10[]push"));
		init();
	}

	private void init() {
		title = new JLabel("Welcome Back!");
		title.setFont(new Font("Arial", Font.BOLD, 30));
		title.setForeground(Color.WHITE);
		add(title);

		description = new JLabel("To keep connected with us please login");
		description.setForeground(Color.WHITE);
		description.setFont(new Font("Arial", Font.PLAIN, 14));
		add(description);

		button = new JButton("SIGN IN") {
			@Override
			protected void paintComponent(Graphics g) {
				var g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

				// Vẽ nền bo tròn


				// Vẽ text
				super.paintComponent(g2);
				g2.dispose();
			}

			@Override
			protected void paintBorder(Graphics g) {
				var g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

				// Vẽ viền bo tròn màu trắng
				g2.setColor(Color.WHITE); // Màu viền
				g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 40, 40); // Bo góc 40px
				g2.dispose();
			}
		};

		button.setFont(new Font("Arial", Font.BOLD, 14));
		button.setForeground(Color.WHITE);
		button.setFocusPainted(false);
		button.setContentAreaFilled(false);
		button.addActionListener(e -> {
			if (event != null) {
				event.actionPerformed(e);
			}
		});
		add(button, "w 50%, h 40!");
	}

	public void setEvent(ActionListener event) {
		this.event = event;
	}

	public void login(boolean login) {
		if (login) {
			title.setText("Hello, Friend!");
			description.setText("Enter your personal details and start your journey with us");
			button.setText("SIGN UP");

		} else {
			title.setText("Welcome Back!");
			description.setText("To keep connected with us please login");
			button.setText("SIGN IN");
		}
		repaint();
		revalidate();
	}

	@Override
	protected void paintComponent(Graphics grphcs) {
		var g2 = (Graphics2D) grphcs;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// Gradient từ màu #F8CDDA (màu sáng) đến màu #1D2B64 (màu đậm)
		var gradient = new GradientPaint(0, 0, Color.decode("#F8CDDA"), 0, getHeight(), Color.decode("#1D2B64"));
		g2.setPaint(gradient);

		// Vẽ nền với bo góc
		g2.fillRoundRect(0, 0, getWidth(), getHeight(), 0, 0);

		super.paintComponent(grphcs);
	}

}
