package component;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.GroupLayout;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class CellStatus extends JPanel {
    private static final long serialVersionUID = 1L;
    private component.TableStatus status;

    public CellStatus(component.TableStatus.StatusType type) {
        setBackground(Color.WHITE); // Đặt nền trắng
        initComponents();          // Khởi tạo giao diện
        status.setType(type);      // Đặt kiểu trạng thái (Approved hoặc Pending)
    }

    private void initComponents() {
        status = new component.TableStatus();
        status.setHorizontalAlignment(SwingConstants.CENTER);
        status.setText("TableStatus");
        status.setPreferredSize(new Dimension(80, 30)); // Đảm bảo kích thước không quá lớn

        var layout = new GroupLayout(this);
        this.setLayout(layout);

        layout.setHorizontalGroup(
            layout.createSequentialGroup() // Tạo layout ngang
                .addGap(10) // Padding bên trái
                .addComponent(status, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(10) // Padding bên phải
        );

        layout.setVerticalGroup(
            layout.createSequentialGroup() // Tạo layout dọc
                .addGap(5) // Padding bên trên
                .addComponent(status, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(5) // Padding bên dưới
        );
    }

}
