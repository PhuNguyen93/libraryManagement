package component;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.GroupLayout;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle;

public class CustomCard extends JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private Color color1;
	private Color color2;
	private JLabel lbIcon;
	private JLabel lbTitle;
	private JLabel lbValues;
	private JLabel lbDescription;

	public CustomCard() {
		initComponents();
		setOpaque(false); // Để JPanel hỗ trợ gradient
		color1 = Color.BLACK;
		color2 = Color.WHITE;
	}

	public void setColors(Color color1, Color color2) {
		this.color1 = color1;
		this.color2 = color2;
	}

	public void setData(String title, String values, String description, Icon icon) {
		lbIcon.setIcon(icon);
		lbTitle.setText(title);
		lbValues.setText(values);
		lbDescription.setText(description);
	}

	private void initComponents() {
		lbIcon = new JLabel();
		lbTitle = new JLabel();
		lbValues = new JLabel();
		lbDescription = new JLabel();

		lbTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
		lbTitle.setForeground(Color.WHITE);

		lbValues.setFont(new Font("SansSerif", Font.BOLD, 18));
		lbValues.setForeground(Color.WHITE);

		lbDescription.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lbDescription.setForeground(Color.WHITE);

		var layout = new GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addGap(25, 25, 25)
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(lbDescription)
								.addComponent(lbValues).addComponent(lbTitle).addComponent(lbIcon))
						.addContainerGap(25, Short.MAX_VALUE)));
		layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addGap(10, 10, 10).addComponent(lbIcon).addGap(18, 18, 18)
						.addComponent(lbTitle).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(lbValues).addGap(18, 18, 18).addComponent(lbDescription)
						.addContainerGap(25, Short.MAX_VALUE)));
	}

	@Override
	protected void paintComponent(Graphics grphcs) {
		var g2 = (Graphics2D) grphcs;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		var gradient = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
		g2.setPaint(gradient);
		g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

		g2.setColor(new Color(255, 255, 255, 50));
		g2.fillOval(getWidth() - (getHeight() / 2), 10, getHeight(), getHeight());
		g2.fillOval(getWidth() - (getHeight() / 2) - 20, getHeight() / 2 + 20, getHeight(), getHeight());

		super.paintComponent(grphcs);
	}
}
