package component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;

public class TableStatus extends JLabel {
    private static final long serialVersionUID = 1L;

    public enum StatusType {
        APPROVED, PENDING
    }

    public TableStatus() {
        setFont(new Font("Arial", Font.BOLD, 12));
        setOpaque(true);
        setHorizontalAlignment(CENTER);
        setVerticalAlignment(CENTER);
        setForeground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8)); // Padding nhỏ hơn
        setPreferredSize(new Dimension(80, 20)); // Xác định kích thước cụ thể
    }


    public void setType(StatusType type) {
        if (type == StatusType.APPROVED) {
            setBackground(new Color(135, 206, 250)); // Màu xanh dương
            setText("Approved");
        } else if (type == StatusType.PENDING) {
            setBackground(new Color(221, 160, 221)); // Màu tím nhạt
            setText("Pending");
        }
    }
}
