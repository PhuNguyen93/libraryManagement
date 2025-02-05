package component;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class RoundedPanel extends JPanel {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private int cornerRadius; // Bán kính bo góc

	public RoundedPanel(int cornerRadius) {
		this.cornerRadius = cornerRadius;
		setOpaque(false); // Để có thể nhìn thấy nền bo góc
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		var g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// Vẽ nền bo góc
		g2.setColor(getBackground());
		g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);

		// Vẽ viền bo góc
		g2.setColor(Color.WHITE); // Màu viền (có thể thay đổi nếu cần)
		g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);
	}
}
