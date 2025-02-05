package main;

import dao.DamageDao;
import entity.DamageEntity;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import component.CustomScrollBarUI;
import component.RoundedPanel;

import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;
import javax.swing.table.TableRowSorter;


public class Damage extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTable tableDamage;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JButton btnUpdateUnderRepair;
    private JButton btnReplaced;
    private JButton btnDiscarded;

    public Damage() {
        setBackground(new Color(244, 243, 240));
        setLayout(null);

        searchField = new JTextField("Search by book name, description, or user name...");
        searchField.setBounds(75, 27, 796, 29);
        searchField.setForeground(Color.GRAY);
        searchField.setFont(new Font("Arial", Font.PLAIN, 16));
        searchField.setBackground(Color.WHITE);
        searchField.setBorder(null);
        searchField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals("Search by book name, description, or user name...")) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Search by book name, description, or user name...");
                    searchField.setForeground(Color.GRAY);
                }
            }
        });

        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                loadDamageData(searchField.getText().trim());
            }
        });

        add(searchField);

        // Panel chứa bảng
        JPanel panelTable = new RoundedPanel(15);
        panelTable.setForeground(new Color(0, 0, 0));
        panelTable.setBackground(new Color(255, 255, 255));
        panelTable.setBorder(null);
        panelTable.setBounds(31, 135, 890, 558);
        panelTable.setLayout(null);
        add(panelTable);

        // Bảng JTable
        tableDamage = new JTable();
        tableDamage.setAutoCreateRowSorter(true);
        tableDamage.setBackground(new Color(255, 255, 255));
        tableDamage.setFont(new Font("Arial", Font.PLAIN, 14));
        tableDamage.setRowHeight(80);
        tableDamage.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
     // Tắt các đường gạch dọc
        tableDamage.setShowVerticalLines(false); // Tắt đường gạch dọc
        tableDamage.setShowHorizontalLines(true); // Nếu muốn giữ đường gạch ngang
        tableDamage.setGridColor(Color.LIGHT_GRAY); // Tùy chọn: Đặt màu cho các đường gạch ngang

        tableDamage.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (tableDamage.getSelectedColumn() == 1 || tableDamage.getSelectedColumn() == 8) { // Cột Repair Cost
                    handleEditRepairCost();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tableDamage);
        scrollPane.setBounds(20, 20, 860, 520);
        scrollPane.setBackground(new Color(255, 255, 255));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUI(new CustomScrollBarUI());
        scrollPane.getHorizontalScrollBar().setUI(new CustomScrollBarUI());
        panelTable.add(scrollPane);

        // Các nút chức năng
        btnUpdateUnderRepair = new JButton("Update Under Repair");
        btnUpdateUnderRepair.setFont(new Font("Rockwell", Font.BOLD, 14));
        btnUpdateUnderRepair.setIcon(new ImageIcon(Damage.class.getResource("/icon13/maintenance.png")));
        btnUpdateUnderRepair.setBounds(31, 77, 219, 47);
        btnUpdateUnderRepair.addActionListener(e -> updateStatus("Under Repair"));
        add(btnUpdateUnderRepair);

        btnReplaced = new JButton("Replaced");
        btnReplaced.setFont(new Font("Rockwell", Font.BOLD, 14));
        btnReplaced.setIcon(new ImageIcon(Damage.class.getResource("/icon13/alter.png")));
        btnReplaced.setBounds(288, 77, 135, 47);
        btnReplaced.addActionListener(e -> handleReplaced());
        add(btnReplaced);

        btnDiscarded = new JButton("Discarded");
        btnDiscarded.setFont(new Font("Rockwell", Font.BOLD, 14));
        btnDiscarded.setIcon(new ImageIcon(Damage.class.getResource("/icon13/discard.png")));
        btnDiscarded.setBounds(469, 77, 171, 47);
        btnDiscarded.addActionListener(e -> updateStatus("Discarded"));
        add(btnDiscarded);

        // Tải dữ liệu ban đầu
        loadDamageData("");
    }

    private void loadDamageData(String searchQuery) {
        List<DamageEntity> damages = DamageDao.searchDamagedBooks(searchQuery);

        tableModel = new DefaultTableModel(new String[] {
                "DamageID", "Book Cover", "Book Name", "Reported By", "Description",
                "Damage Date", "Severity", "Status", "Repair Cost", "BookID"
        }, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
//                return column == 8; // Chỉ cho phép chỉnh sửa cột RepairCost
            	return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return switch (columnIndex) {
                    case 1 -> ImageIcon.class; // Book Cover
                    case 5 -> java.util.Date.class; // Damage Date
                    case 8 -> BigDecimal.class; // Repair Cost
                    default -> String.class;
                };
            }
        };
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Định dạng ngày
        DecimalFormat currencyFormat = new DecimalFormat("##,###");

//        DecimalFormat currencyFormat = new DecimalFormat("#,##0.00");

        for (DamageEntity damage : damages) {
            Date damageDate = damage.getDamageDate(); // Lấy trực tiếp kiểu Date từ getDamageDate()

            // Format lại ngày thành chuỗi
            String formattedDamageDate = damageDate != null ? dateFormat.format(damageDate) : "N/A";

            String formattedRepairCost = (damage.getRepairCost() != null)
                    ? currencyFormat.format(damage.getRepairCost()) + " VND"
                    : "0 VND";

            tableModel.addRow(new Object[] {
                    damage.getDamageID(),
                    new ImageIcon(new ImageIcon("src/main/resources/images/" + damage.getBookImage())
                            .getImage().getScaledInstance(50, 70, Image.SCALE_SMOOTH)),
                    damage.getBookName(),
                    damage.getReportedName(),
                    damage.getDamageDescription(),
                    formattedDamageDate, // Sử dụng chuỗi ngày đã định dạng
                    damage.getDamageSeverity(),
                    damage.getStatus(),
                    formattedRepairCost,
                    damage.getBookID()
            });
        }

        // Cập nhật model cho bảng
        tableDamage.setModel(tableModel);
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        tableDamage.setRowSorter(sorter);
        // Thiết lập lại màu nền
        tableDamage.setBackground(Color.WHITE); // Nền của JTable
        tableDamage.setOpaque(true);

        JScrollPane parentScrollPane = (JScrollPane) tableDamage.getParent().getParent();
        if (parentScrollPane != null) {
            parentScrollPane.getViewport().setBackground(Color.WHITE); // Nền của JScrollPane
        }

        // Gọi lại phương thức customizeTable để áp dụng renderer tùy chỉnh
        customizeTable();
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

    private void customizeTable() {
        // Ẩn cột DamageID và BookID
        tableDamage.getColumnModel().getColumn(0).setMinWidth(0);
        tableDamage.getColumnModel().getColumn(0).setMaxWidth(0);
        tableDamage.getColumnModel().getColumn(0).setPreferredWidth(0);

        tableDamage.getColumnModel().getColumn(9).setMinWidth(0);
        tableDamage.getColumnModel().getColumn(9).setMaxWidth(0);
        tableDamage.getColumnModel().getColumn(9).setPreferredWidth(0);

        // Renderer căn giữa nội dung và vẽ đường viền dưới
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;

//            @Override
//            public void paintComponent(Graphics g) {
//                // Không vẽ viền mặc định
//                setBorder(BorderFactory.createEmptyBorder());
//                super.paintComponent(g);
//
//                // Vẽ đường gạch dưới tùy chỉnh
//                Graphics2D g2 = (Graphics2D) g;
//                g2.setColor(new Color(211, 211, 211)); // Màu giống với header
//                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//
//                int y = getHeight() - 1; // Vị trí y của đường viền dưới
//                g2.drawLine(0, y, getWidth(), y); // Vẽ đường ngang
//            }
        };
        cellRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        for (int i = 0; i < tableDamage.getColumnCount(); i++) {
            tableDamage.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }

        // Renderer riêng cho cột hình ảnh (Book Cover)
        tableDamage.getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                if (value instanceof ImageIcon) {
                    return new JLabel((ImageIcon) value);
                }
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }

//            @Override
//            public void paintComponent(Graphics g) {
//                // Không vẽ viền mặc định
//                setBorder(BorderFactory.createEmptyBorder());
//                super.paintComponent(g);
//
//                // Vẽ đường gạch dưới tùy chỉnh
//                Graphics2D g2 = (Graphics2D) g;
//                g2.setColor(new Color(211, 211, 211)); // Màu giống với header
//                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//
//                int y = getHeight() - 1; // Vị trí y của đường viền dưới
//                g2.drawLine(0, y, getWidth(), y); // Vẽ đường ngang
//            }
        });
        tableDamage.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                if (value instanceof Date) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    value = sdf.format(value); // Chuyển Date thành chuỗi
                }
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        });

     // Renderer riêng cho cột "Status"
        tableDamage.getColumnModel().getColumn(7).setCellRenderer(new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                            boolean hasFocus, int row, int column) {
                // Tạo JPanel chứa JLabel
                JPanel panel = new JPanel();
                panel.setOpaque(true); // Đảm bảo JPanel có nền
                panel.setLayout(new java.awt.GridBagLayout()); // Sử dụng GridBagLayout để căn giữa nội dung
                panel.setBackground(isSelected ? table.getSelectionBackground() : Color.WHITE); // Nền của ô khi chọn

                // Tạo JLabel cho trạng thái
                JLabel label = new JLabel(value.toString(), SwingConstants.CENTER);
                label.setOpaque(true); // Đảm bảo JLabel có nền riêng
                label.setFont(new Font("Arial", Font.BOLD, 12));
                label.setPreferredSize(new Dimension(100, 40)); // Đặt kích thước cố định
                label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Padding xung quanh

                // Áp dụng màu nền và màu chữ dựa trên trạng thái
                String status = value.toString();
                switch (status) {
                    case "Under Repair" -> {
                        label.setBackground(Color.decode("#FFCC00")); // Màu tím nhạt
                        label.setForeground(Color.BLACK); // Chữ đen
                    }
                    case "Replaced" -> {
                        label.setBackground(Color.decode("#99CC00")); // Màu xanh lá nhạt
                        label.setForeground(Color.BLACK); // Chữ đen
                    }
                    case "Discarded" -> {
                        label.setBackground(Color.decode("#CCCCCC")); // Màu đỏ nhạt
                        label.setForeground(Color.BLACK); // Chữ trắng
                    }
                    default -> {
                        label.setBackground(Color.decode("#FF6666")); // Màu xám cho trạng thái khác
                        label.setForeground(Color.BLACK); // Chữ đen
                    }
                }

                // Nếu hàng được chọn, áp dụng màu riêng cho viền (không ảnh hưởng đến trạng thái bên trong)
                if (isSelected) {
                    panel.setBorder(BorderFactory.createLineBorder(Color.BLUE, 1)); // Viền xanh khi chọn
                } else {
                    panel.setBorder(BorderFactory.createEmptyBorder()); // Không viền khi không chọn
                }

                // Thêm JLabel vào JPanel
                panel.add(label);
                return panel;
            }
        });

        // Tùy chỉnh header của bảng
        JTableHeader header = tableDamage.getTableHeader();
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 40));
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                JLabel headerLabel = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                headerLabel.setBackground(Color.WHITE); // Nền trắng
                headerLabel.setForeground(Color.BLACK); // Chữ đen
                headerLabel.setFont(new Font("Arial", Font.BOLD, 16)); // Font Arial, đậm, cỡ 16
                headerLabel.setHorizontalAlignment(SwingConstants.CENTER); // Căn giữa
                return headerLabel;
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // Vẽ đường gạch dưới
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(new Color(211, 211, 211)); // Màu của viền gạch dưới
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Vẽ đường viền dưới
                int y = getHeight() - 1; // Vị trí y của đường viền (dưới cùng của header)
                g2.drawLine(0, y, getWidth(), y); // Vẽ đường ngang
            }
        });
    }


    private void handleEditRepairCost() {
        int selectedRow = tableDamage.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a row.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String status = tableDamage.getValueAt(selectedRow, 7).toString();
        if (!status.equals("Replaced") && !status.equals("Discarded")) {
            String input = JOptionPane.showInputDialog("Enter Repair Cost:");
            if (input != null) {
                try {
                    BigDecimal repairCost = new BigDecimal(input);
                    if (repairCost.compareTo(BigDecimal.ZERO) < 0) {
                        JOptionPane.showMessageDialog(null, "Repair cost must be non-negative.", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        int damageID = (int) tableDamage.getValueAt(selectedRow, 0);
                        DamageDao.updateRepairCost(damageID, repairCost);
                        loadDamageData("");
                        JOptionPane.showMessageDialog(null, "Repair cost updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void updateStatus(String status) {
        int selectedRow = tableDamage.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a row to update.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int damageID = (int) tableDamage.getValueAt(selectedRow, 0);
        String currentStatus = tableDamage.getValueAt(selectedRow, 7).toString();

        if (!currentStatus.equals("Replaced") && !currentStatus.equals("Discarded")) {
            DamageDao.updateStatus(damageID, status);
            if (status.equals("Replaced")) {
                int bookID = (int) tableDamage.getValueAt(selectedRow, 9);
                DamageDao.updateBookQuantity(bookID, 1);
            }
            loadDamageData("");
            JOptionPane.showMessageDialog(null, "Status updated to " + status + ".", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Cannot update status. Current status is " + currentStatus + ".", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleReplaced() {
        int selectedRow = tableDamage.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a row to update.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String status = tableDamage.getValueAt(selectedRow, 7).toString(); // Status column
        String repairCostString = tableDamage.getValueAt(selectedRow, 8).toString(); // RepairCost column (as String)

        // Xử lý chuỗi để chuyển thành BigDecimal
        BigDecimal repairCost;
        try {
            repairCostString = repairCostString.replaceAll("[^\\d.]", ""); // Loại bỏ ký tự không phải số
            repairCost = new BigDecimal(repairCostString);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid repair cost format.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!status.equals("Replaced") && !status.equals("Discarded")) {
            if (repairCost.compareTo(BigDecimal.ZERO) == 0) {
                JOptionPane.showMessageDialog(null, "Repair cost must be set before replacing.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            updateStatus("Replaced");
        } else {
            JOptionPane.showMessageDialog(null, "Cannot replace. Current status is " + status + ".", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
