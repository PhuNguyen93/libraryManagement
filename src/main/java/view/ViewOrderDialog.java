package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import dialog.ViewOrderDetailDialog;

public class ViewOrderDialog extends JDialog {

    private static final long serialVersionUID = 1L;
    private JTable tableOrderDetails;
    private JTextField textTotalOrderPrice;
    private DefaultTableModel orderModel;
    private List<Map<String, Object>> borrowRecords;

    public ViewOrderDialog(Window parent, List<Map<String, Object>> borrowRecords, String studentCode, String studentName, String phoneNumber, 
    		String email, DefaultTableModel orderModel) {
        super(parent, "Order Details", ModalityType.APPLICATION_MODAL);
        setSize(800, 520);
        setLocationRelativeTo(parent);
        getContentPane().setLayout(null);
        this.borrowRecords = borrowRecords;
        this.orderModel = orderModel;

        // Panel chứa thông tin sinh viên
        var studentPanel = new JPanel();
        studentPanel.setLayout(null);
        studentPanel.setBounds(10, 10, 760, 121);
        studentPanel.setBorder(BorderFactory.createTitledBorder("Student Information"));

        var lblStudentCode = new JLabel("Student Code:");
        lblStudentCode.setBounds(20, 30, 100, 25);
        studentPanel.add(lblStudentCode);

        var textStudentCode = new JTextField(studentCode);
        textStudentCode.setBounds(130, 30, 200, 25);
        textStudentCode.setFocusable(false);
        textStudentCode.setEditable(false);
        textStudentCode.setBackground(new Color(224, 255, 255));
        studentPanel.add(textStudentCode);

        var lblStudentName = new JLabel("Student Name:");
        lblStudentName.setBounds(20, 70, 100, 25);
        studentPanel.add(lblStudentName);

        var textStudentName = new JTextField(studentName);
        textStudentName.setBounds(130, 70, 200, 25);
        textStudentName.setFocusable(false);
        textStudentName.setEditable(false);
        textStudentName.setBackground(new Color(224, 255, 255));
        studentPanel.add(textStudentName);

        var lblPhoneNumber = new JLabel("Phone Number:");
        lblPhoneNumber.setBounds(400, 30, 100, 25);
        studentPanel.add(lblPhoneNumber);

        var textPhoneNumber = new JTextField(phoneNumber);
        textPhoneNumber.setBounds(510, 30, 200, 25);
        textPhoneNumber.setFocusable(false);
        textPhoneNumber.setEditable(false);
        textPhoneNumber.setBackground(new Color(224, 255, 255));
        studentPanel.add(textPhoneNumber);

        var lblEmail = new JLabel("Email:");
        lblEmail.setBounds(400, 70, 100, 25);
        studentPanel.add(lblEmail);

        var textEmail = new JTextField(email);
        textEmail.setBounds(510, 70, 200, 25);
        textEmail.setFocusable(false);
        textEmail.setEditable(false);
        textEmail.setBackground(new Color(224, 255, 255));
        studentPanel.add(textEmail);

        getContentPane().add(studentPanel);

        // Panel chứa bảng OrderDetails
        var orderPanel = new JPanel();
        orderPanel.setBounds(10, 142, 760, 281);
        orderPanel.setBorder(BorderFactory.createTitledBorder("Order Details"));
        orderPanel.setLayout(new BorderLayout());

        tableOrderDetails = new JTable(orderModel);
        tableOrderDetails.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableOrderDetails.setRowHeight(30);
        tableOrderDetails.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        tableOrderDetails.getTableHeader().setBackground(new Color(211, 211, 211));

        // Căn giữa dữ liệu trong bảng
        var renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
        for (int i = 0; i < tableOrderDetails.getColumnCount(); i++) {
            tableOrderDetails.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        var scrollPane = new JScrollPane(tableOrderDetails);
        orderPanel.add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(orderPanel);

        // Hiển thị tổng tiền đơn hàng
        var lblTotalOrderPrice = new JLabel("Total Order Price:");
        lblTotalOrderPrice.setBounds(224, 432, 120, 30);
        getContentPane().add(lblTotalOrderPrice);

        textTotalOrderPrice = new JTextField("0.00");
        textTotalOrderPrice.setBounds(344, 432, 150, 30);
        textTotalOrderPrice.setFocusable(false);
        textTotalOrderPrice.setEditable(false);
        textTotalOrderPrice.setBackground(new Color(224, 255, 255));
        getContentPane().add(textTotalOrderPrice);

        // Tính tổng tiền khi khởi tạo dialog
        updateTotalOrderPrice(orderModel);

        // Nút Close để đóng dialog
        var btnClose = new JButton("");
        btnClose.setIcon(new ImageIcon(ViewOrderDialog.class.getResource("/icon5/cross.png")));
        btnClose.setBounds(660, 430, 48, 44);
        btnClose.addActionListener(e -> dispose());
        getContentPane().add(btnClose);
        // Tùy chỉnh Renderer cho cột Status với nền hình oval
        tableOrderDetails.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                return new OvalPanel(value != null ? value.toString() : "", isSelected);
            }
        });
        
        JPopupMenu popupMenu = new GradientPopupMenu(
        		new Color(255, 182, 193), // Hồng pastel nhạt
        		new Color(221, 160, 221)  // Tím pastel nhạt

        	);

        JMenuItem viewItem = new JMenuItem("View");
        popupMenu.add(viewItem);

        // Hiển thị menu chuột phải
        tableOrderDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
				var row = tableOrderDetails.rowAtPoint(e.getPoint());
				if (row >= 0 && row < tableOrderDetails.getRowCount()) {
					tableOrderDetails.setRowSelectionInterval(row, row); // Chọn hàng được nhấn
					popupMenu.show(tableOrderDetails, e.getX(), e.getY());
				}
                
            }
        });


        viewItem.addActionListener(e -> {
            int selectedRow = tableOrderDetails.getSelectedRow();
            if (selectedRow != -1) {
                // Lấy dữ liệu từ dòng được chọn
            	 // Lấy dữ liệu từ dòng được chọn
            	
                Map<String, Object> record = borrowRecords.get(selectedRow);
                
    			var bookCover = (String) record.get("BookImage");
                String bookTitle = (String) record.get("BookName");
                String author = (String) record.get("AuthorName");
                String publisher = (String) record.get("PublisherName");
                String category = (String) record.get("Category");
                Date borrowDate = (Date) record.get("BorrowDate");
                Date returnDate = (Date) record.get("DueReturnDate");
                var intNumberOfDay = (Integer) record.get("NumberOfDays");
                var price = new BigDecimal(record.get("Price").toString());
                var depositPercentage = new BigDecimal(record.get("DepositPercentage").toString()); // Sử dụng DepositPercentage đúng
                var fineAmount = new BigDecimal(record.get("FineAmount").toString());
                var borrowPrice = new BigDecimal(record.get("BorrowPrice").toString());
                var totalPrice = new BigDecimal(record.get("Total").toString());
                var numberOfDay = BigDecimal.valueOf(intNumberOfDay);

                // Tính depositAmount
                var depositAmount = price.multiply(depositPercentage).divide(BigDecimal.valueOf(100));

                // Tính rentalPrice
                var rentalPrice = borrowPrice.multiply(numberOfDay);
                

                // Hiển thị dialog chi tiết sách
                ViewOrderDetailDialog bookDetailDialog = new ViewOrderDetailDialog(
                    SwingUtilities.getWindowAncestor(ViewOrderDialog.this),
                    bookCover, bookTitle, author, publisher, category, borrowDate, returnDate,
                    depositAmount, rentalPrice, fineAmount, totalPrice
                );
                bookDetailDialog.setVisible(true);
            }
        });



    }
    private ImageIcon loadBookCover(String image) {
		var imagePath = "src/main/resources/images/" + image; // Đường dẫn tới file ảnh (có thể đổi đuôi thành .png
																// nếu cần)
		var icon = new ImageIcon(imagePath);
		if (icon.getIconWidth() == -1) {
			// Nếu không tìm thấy ảnh, sử dụng ảnh mặc định
			icon = new ImageIcon("image/default.png");
		}

		// Thay đổi kích thước ảnh (nếu cần)
		var scaledImage = icon.getImage().getScaledInstance(50, 70, Image.SCALE_SMOOTH);
		return new ImageIcon(scaledImage);
	}
    
	class OvalPanel extends JPanel {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private final String status;
        private final boolean isSelected;

        public OvalPanel(String status, boolean isSelected) {
            this.status = status;
            this.isSelected = isSelected;
            setOpaque(false); // Để nền trong suốt
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            // Bật Anti-Aliasing để làm mịn hình ảnh
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Màu nền tùy theo trạng thái
            Color backgroundColor;
            switch (status) {
                case "Damage":
                    backgroundColor = new Color(255, 182, 193); // Đỏ nhạt pastel
                    break;
                case "Overdue":
                    backgroundColor = new Color(255, 223, 186); // Cam nhạt pastel
                    break;
                case "Borrowed":
                    backgroundColor = new Color(173, 216, 230); // Xanh dương nhạt pastel
                    break;
                case "Returned":
                    backgroundColor = new Color(144, 238, 144); // Xanh lá nhạt pastel
                    break;
                default:
                    backgroundColor = Color.LIGHT_GRAY; // Mặc định là xám nhạt
                    break;
            }

            // Nếu được chọn, làm tối màu
            if (isSelected) {
                backgroundColor = backgroundColor.darker();
            }

            // Vẽ nền hình oval bo mềm hơn
            int arcWidth = getHeight(); // Bán kính góc bo theo chiều cao
            int arcHeight = getHeight();
            g2d.setColor(backgroundColor);
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), arcWidth, arcHeight);

            // Vẽ viền (nếu cần)
            g2d.setColor(backgroundColor.brighter());
            g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arcWidth, arcHeight);

            // Vẽ text
            g2d.setColor(Color.WHITE); // Màu chữ
            g2d.setFont(new Font("Arial", Font.BOLD, 14));
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(status);
            int textHeight = fm.getAscent();
            int x = (getWidth() - textWidth) / 2;
            int y = (getHeight() + textHeight) / 2 - 4;
            g2d.drawString(status, x, y);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(140, 40); // Kích thước hình oval
        }
    }

	public class GradientPopupMenu extends JPopupMenu {
	    /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private Color startColor;
	    private Color endColor;

	    public GradientPopupMenu(Color startColor, Color endColor) {
	        this.startColor = startColor;
	        this.endColor = endColor;
	        setOpaque(false); // Đặt nền trong suốt để vẽ gradient
	    }

	    @Override
	    protected void paintComponent(Graphics g) {
	        Graphics2D g2d = (Graphics2D) g;
	        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	        // Vẽ màu gradient
	        GradientPaint gradientPaint = new GradientPaint(0, 0, startColor, 0, getHeight(), endColor);
	        g2d.setPaint(gradientPaint);
	        g2d.fillRect(0, 0, getWidth(), getHeight());

	        super.paintComponent(g);
	    }
	}

    
    private void updateTotalOrderPrice(DefaultTableModel orderModel) {
        if (orderModel == null) return; // Kiểm tra null
        double totalOrderPrice = 0.0;
        for (int i = 0; i < orderModel.getRowCount(); i++) {
            Object value = orderModel.getValueAt(i, orderModel.getColumnCount() - 1);
            if (value != null) {
                try {
                    totalOrderPrice += Double.parseDouble(value.toString().replace(",", "").trim());
                } catch (NumberFormatException ex) {
                    System.err.println("Invalid value in Total Price column: " + value);
                }
            }
        }
        DecimalFormat df = new DecimalFormat("#,##0.00");
        textTotalOrderPrice.setText(df.format(totalOrderPrice));
    }

}
