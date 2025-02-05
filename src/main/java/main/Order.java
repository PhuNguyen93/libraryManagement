package main;

import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import component.CustomCard;
import component.CustomScrollBarUI;
import component.RoundedPanel;
import dao.BorrowRecordsDao;
import dao.OrderCardDao;
import dao.PaymentDao;
import entity.BorrowRecordsEntity;
import entity.PaymentEntity;
import gui.ShowReport;
import service.ConnectDB;
import view.ViewOrderDialog;

public class Order extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTable tableOrder;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private List<PaymentEntity> payments;

    public Order() {
        setBackground(new Color(244, 243, 240));
        setLayout(null);
        addInfoCards();

        searchField = new JTextField("Search here...");
        searchField.setBounds(75, 27, 796, 29);
        searchField.setForeground(Color.GRAY);
        searchField.setFont(new Font("Arial", Font.PLAIN, 16));
        searchField.setBackground(Color.WHITE);
        searchField.setBorder(null);
        searchField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals("Search here...")) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Search here...");
                    searchField.setForeground(Color.GRAY);
                }
            }
        });
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterOrderData();
            }
        });
        add(searchField);
        
        // Icon menu
        var menuIcon = new JLabel(new ImageIcon(Order.class.getResource("/icon4/redo.png")));
        menuIcon.setBounds(885, 20, 45, 44);
        menuIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                reloadPage(); // Gọi phương thức reloadPage khi click vào menuIcon
            }
        });
        add(menuIcon);

        JPanel panelTable = new RoundedPanel(15);
        panelTable.setForeground(new Color(0, 0, 0));
        panelTable.setBackground(new Color(255, 255, 255));
        panelTable.setBorder(null);
        panelTable.setBounds(20, 292, 910, 398);
        panelTable.setLayout(null);
        add(panelTable);

        tableOrder = new JTable();
        tableOrder.setAutoCreateRowSorter(true);
        tableOrder.setBackground(Color.WHITE);
        tableOrder.setFont(new Font("Arial", Font.PLAIN, 14));
        tableOrder.setRowHeight(80);
        tableOrder.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableOrder.setShowVerticalLines(false);

        JScrollPane scrollPane = new JScrollPane(tableOrder);
        scrollPane.setBounds(20, 11, 869, 358);
        scrollPane.setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUI(new CustomScrollBarUI());
        scrollPane.getHorizontalScrollBar().setUI(new CustomScrollBarUI());
        panelTable.add(scrollPane);

        JButton btnViewOrder = new JButton("");
        btnViewOrder.setIcon(new ImageIcon(Order.class.getResource("/icon3/file.png")));
        btnViewOrder.setBounds(20, 243, 45, 38);
        btnViewOrder.addActionListener(e -> handleViewOrder());
        add(btnViewOrder);
        
        JButton btnNewButton = new JButton("");
        btnNewButton.setIcon(new ImageIcon(Order.class.getResource("/icon13/report.png")));
        btnNewButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		var Rp = new ShowReport();
				Rp.Report();
				Rp.setVisible(true);
        	}
        });
        btnNewButton.setBounds(94, 243, 45, 38);
        add(btnNewButton);

        loadOrderData();
    }

    @Override
    protected void paintComponent(Graphics grphcs) {
        super.paintComponent(grphcs);
        var g2 = (Graphics2D) grphcs;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Color.WHITE);
        g2.fillRoundRect(20, 20, 910, 44, 15, 15);

        g2.setColor(new Color(240, 240, 240));
        g2.drawRoundRect(20, 20, 910, 44, 15, 15);
    }
    
    private void reloadPage() {
        loadOrderData(); // Tải lại dữ liệu từ cơ sở dữ liệu

        removeInfoCards(); // Xóa các thẻ thông tin cũ
        addInfoCards(); // Thêm lại các thẻ thông tin mới

        searchField.setText("Search here..."); // Reset ô tìm kiếm
        revalidate();
        repaint(); // Làm mới giao diện

        JOptionPane.showMessageDialog(this, "Page reloaded successfully!");
    }

    private void removeInfoCards() {
        for (Component comp : getComponents()) {
            if (comp instanceof JPanel && comp.getBounds().equals(new Rectangle(20, 82, 910, 150))) {
                remove(comp); // Xóa panel chứa thẻ thông tin
            }
        }
    }
    

    private ImageIcon loadAvatar(String image) {
        String imagePath = "src/main/resources/avatar/" + image; // Đường dẫn tới file ảnh
        ImageIcon icon = new ImageIcon(imagePath);
        if (icon.getIconWidth() == -1) {
            // Nếu không tìm thấy ảnh, sử dụng ảnh mặc định
            icon = new ImageIcon("src/main/resources/avatar/default.png");
        }

        // Thay đổi kích thước ảnh (nếu cần)
        Image scaledImage = icon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    private void loadOrderData() {
        PaymentDao paymentDao = new PaymentDao();
        payments = paymentDao.select();
        reloadTableWithPayments(payments);
        customizeTable(tableOrder);
    }
    private void reloadTableWithPayments(List<PaymentEntity> payments) {
        DefaultTableModel model = new DefaultTableModel(new String[]{
                "Student Avatar", "Student Code", "Student Name", "Borrow Date", "Total Order Amount", "PaymentID"
        }, 0);
        DecimalFormat currencyFormat = new DecimalFormat("##,###"); // Format tiền tệ

        for (PaymentEntity record : payments) {
            model.addRow(new Object[]{
                    loadAvatar(record.getStudentAvatar()), // Hiển thị avatar
                    record.getStudentCode(),
                    record.getStudentName(),
                    record.getPaymentDate(),
                    currencyFormat.format(record.getTotalOrderAmount()), // Format tiền
                    record.getPaymentID()
            });
        }

        tableOrder.setModel(model);
        customizeTable(tableOrder); // Tùy chỉnh bảng
    }

    private void filterOrderData() {
        String searchText = searchField.getText().trim().toLowerCase();
        if (searchText.equals("search here...") || searchText.isEmpty()) {
            loadOrderData(); // Tải lại dữ liệu gốc nếu ô tìm kiếm trống
            return;
        }

        DefaultTableModel filteredModel = new DefaultTableModel(new String[]{
                "Student Avatar", "Student Code", "Student Name", "Borrow Date", "Total Order Amount", "PaymentID"
        }, 0);

        for (PaymentEntity record : payments) {
            // Kiểm tra điều kiện tìm kiếm theo StudentName hoặc StudentCode
            if (record.getStudentName().toLowerCase().contains(searchText) || 
                record.getStudentCode().toLowerCase().contains(searchText)) {
                filteredModel.addRow(new Object[]{
                        loadAvatar(record.getStudentAvatar()), // Hiển thị avatar
                        record.getStudentCode(),
                        record.getStudentName(),
                        record.getPaymentDate(),
                        record.getTotalOrderAmount(),
                        record.getPaymentID()
                });
            }
        }

        tableOrder.setModel(filteredModel);
        customizeTable(tableOrder);
    }

    private void addInfoCards() {
        var cardPanel = new JPanel();
        cardPanel.setLayout(new GridLayout(1, 3, 20, 0)); // 3 cards horizontally, 20px gap
        cardPanel.setBounds(20, 82, 910, 150);
        cardPanel.setBackground(new Color(244, 243, 240)); // Background color of cardPanel

        // Tạo DAO để lấy dữ liệu
        var dao = new OrderCardDao();
        
     // Định dạng số tiền
        var decimalFormat = new DecimalFormat("##,###");

        // Card 1: Tổng tiền sách cho thuê ngày hôm nay
        var totalRentalRevenueToday = dao.getTotalRentalRevenueToday();
        var percentageRentalRevenueToday = dao.getPercentageRentalRevenueToday();
        var card1 = new CustomCard();
        card1.setColors(new Color(100, 149, 237), new Color(70, 130, 180)); // Gradient màu xanh
        card1.setData("Rental Revenue Today",
        		 decimalFormat.format(totalRentalRevenueToday) + " VND",
                 String.format("%.2f%% of Monthly Revenue", percentageRentalRevenueToday),
                new ImageIcon(getClass().getResource("/hinh/stock.png")));

        // Card 2: Tổng tiền sách trả ngày hôm nay
        var totalReturnRevenueToday = dao.getTotalReturnRevenueToday();
        var percentageReturnRevenueToday = dao.getPercentageReturnRevenueToday();
        var card2 = new CustomCard();
        card2.setColors(new Color(186, 85, 211), new Color(148, 0, 211)); // Gradient màu tím
        card2.setData("Return Revenue Today",
        		decimalFormat.format(totalReturnRevenueToday) + " VND",
                String.format("%.2f%% of Monthly Revenue", percentageReturnRevenueToday),
                new ImageIcon(getClass().getResource("/hinh/profit.png")));

        // Card 3: Tổng tiền sách hư hại ngày hôm nay
        var totalDamagedBookCostToday = dao.getTotalDamagedBookCostToday();
        var percentageDamagedCostToday = dao.getPercentageDamagedCostToday();
        var card3 = new CustomCard();
        card3.setColors(new Color(255, 204, 102), new Color(255, 140, 0)); // Gradient màu vàng
        card3.setData("Damaged Books Cost",
        		decimalFormat.format(totalDamagedBookCostToday) + " VND",
                String.format("%.2f%% of Monthly Costs", percentageDamagedCostToday),
                new ImageIcon(getClass().getResource("/hinh/flag.png")));

        // Add các card vào panel
        cardPanel.add(card1);
        cardPanel.add(card2);
        cardPanel.add(card3);

        add(cardPanel);
    }

    private void customizeTable(JTable table) {
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(80); // Đặt chiều cao hàng để phù hợp với hình ảnh
        table.setGridColor(new Color(224, 224, 224));

        // Center align cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);

        for (int i = 0; i < table.getColumnCount(); i++) {
            if (i != 0) { // Không căn giữa cột Avatar
                table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        }

        // Render cột Avatar
        table.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                if (value instanceof ImageIcon) {
                    return new JLabel((ImageIcon) value);
                }
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        });

        // Customize header
        JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 40));
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                JLabel headerRenderer = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                headerRenderer.setBackground(Color.WHITE);
                headerRenderer.setForeground(Color.BLACK);
                headerRenderer.setFont(new Font("Arial", Font.BOLD, 16));
                headerRenderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
                return headerRenderer;
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(new Color(211, 211, 211)); // Màu của viền gạch dưới
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Vẽ đường viền dưới
                int y = getHeight() - 1; // Vị trí y của đường viền (dưới cùng của header)
                g2.drawLine(0, y, getWidth(), y); // Vẽ đường ngang
            }
        });
    }

    private void handleViewOrder() {
        int selectedRow = tableOrder.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an order to view details.");
            return;
        }

        int paymentID = (int) tableOrder.getValueAt(selectedRow, 5);
        PaymentEntity selectedPayment = payments.stream()
                .filter(payment -> payment.getPaymentID() == paymentID)
                .findFirst()
                .orElse(null);

        if (selectedPayment != null) {
            PaymentDao paymentDao = new PaymentDao();
            BorrowRecordsDao borrowDao = new BorrowRecordsDao(ConnectDB.getCon());
            List<BorrowRecordsEntity> details = paymentDao.getOrderDetails(paymentID);
            List<Map<String, Object>> borrowRecords = borrowDao.selectBrrowByPaymentID(paymentID);

            DecimalFormat currencyFormat = new DecimalFormat("#,##0.00");
            DefaultTableModel orderDetailsModel = new DefaultTableModel(
                    new Object[]{"Book Title", "Quantity", "Status", "Due Return Date", "Total"}, 0);

            for (BorrowRecordsEntity detail : details) {
                orderDetailsModel.addRow(new Object[]{
                        detail.getBookName(),
                        detail.getQuantity(),
                        detail.getStatus(),
                        detail.getDueReturnDate(),
                        currencyFormat.format(detail.getTotal())
                });
            }

            ViewOrderDialog dialog = new ViewOrderDialog(SwingUtilities.getWindowAncestor(this),
                    borrowRecords, selectedPayment.getStudentCode(),
                    selectedPayment.getStudentName(),
                    selectedPayment.getPhoneNumber(),
                    selectedPayment.getEmail(), orderDetailsModel);
            dialog.setVisible(true);
        }
    }

}
