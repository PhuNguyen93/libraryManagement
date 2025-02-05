package component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class CustomScrollBarUI extends BasicScrollBarUI {

	@Override
	protected void configureScrollBarColors() {
		this.thumbColor = new Color(211, 211, 211); // Màu của thanh trượt (thumb)
		this.trackColor = Color.WHITE; // Màu nền (track)
	}

	@Override
	protected JButton createDecreaseButton(int orientation) {
		return createEmptyButton(); // Loại bỏ nút mũi tên trên/dưới
	}

	@Override
	protected JButton createIncreaseButton(int orientation) {
		return createEmptyButton(); // Loại bỏ nút mũi tên trên/dưới
	}

	private JButton createEmptyButton() {
		var button = new JButton();
		button.setPreferredSize(new Dimension(0, 0)); // Nút rỗng
		return button;
	}

	@Override
	protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
		g.setColor(thumbColor); // Set màu của thanh trượt
		var g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// Giảm chiều ngang của thanh trượt
		var width = 6; // Chiều ngang của thanh trượt (thu nhỏ)
		var x = thumbBounds.x + (thumbBounds.width - width) / 2; // Căn giữa thanh trượt

		// Giảm chiều dài của thanh trượt
		var height = Math.min(thumbBounds.height, 100); // Chiều dài tối đa là 100px
		var y = thumbBounds.y + (thumbBounds.height - height) / 2; // Căn giữa thanh trượt

		g2.fillRoundRect(x, y, width, height, 10, 10); // Vẽ thanh trượt với góc bo
	}

	@Override
	protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
		g.setColor(trackColor); // Set màu nền
		g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height); // Vẽ track
	}
}


