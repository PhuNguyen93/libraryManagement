package view;

import java.awt.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class BookRentalViewDialog extends JDialog {

    private static final long serialVersionUID = 1L;
    private JTextField txtBookID, txtBookName, txtQuantity, txtRentalPrice, txtBorrowDate, txtReturnDate,
            txtNumberOfDays, txtTotalRentalPrice, txtPrice, txtDepositPercentage, txtDepositAmount, txtTotalPrice;

    public BookRentalViewDialog(Window parent, String[] columnNames, Object[] rentalData) {
        super(parent, "Rental Details", ModalityType.APPLICATION_MODAL);
        setBounds(100, 100, 480, 770);
        setLocationRelativeTo(parent);
        setLayout(null);

        // Gradient Panel
        JPanel gradientPanel = new JPanel() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void paintComponent(Graphics grphcs) {
                Graphics2D g2 = (Graphics2D) grphcs;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Gradient màu nền
                GradientPaint gradient = new GradientPaint(
                        0, 0, Color.decode("#F8CDDA"),
                        0, getHeight(), Color.decode("#1D2B64")
                );
                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        gradientPanel.setLayout(null);
        gradientPanel.setBounds(0, 0, 480, 770);
        setContentPane(gradientPanel); // Đặt gradientPanel làm nội dung chính

        // Content Panel
        JPanel contentPanelView = new JPanel();
        contentPanelView.setBounds(20, 20, 440, 710);
        contentPanelView.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPanelView.setOpaque(false); // Để nền trong suốt
        contentPanelView.setLayout(null);
        gradientPanel.add(contentPanelView);

        // Định dạng số tiền
        var currencyFormat = new DecimalFormat("#,###");

        String[] labels = { "Book ID", "Book Name", "Quantity", "Rental Price", "Borrow Date", "Return Date",
                "Number Of Days", "Total Rental Price", "Book Price", "Deposit Percentage", "Deposit Amount",
                "Total Price" };

        // Labels và Text Fields
        int labelX = 30, labelY = 30, labelWidth = 180, labelHeight = 35;
        int fieldX = 220, fieldY = 30, fieldWidth = 180, fieldHeight = 35;
        var rentalPrice = Double.parseDouble(rentalData[3].toString());
        var numberOfDays = Double.parseDouble(rentalData[4].toString());
        var totalRentalPrice = rentalPrice * numberOfDays;

        var totalPrice = Double.parseDouble(rentalData[8].toString());

        for (var i = 0; i < labels.length; i++) {
            var label = new JLabel(labels[i]);
            label.setBounds(labelX, labelY + i * 50, labelWidth, labelHeight);
            label.setFont(new Font("Arial", Font.BOLD, 14));
            label.setForeground(Color.WHITE); // Màu chữ trắng trên nền gradient
            contentPanelView.add(label);

            var textField = new JTextField();
            textField.setBounds(fieldX, fieldY + i * 50, fieldWidth, fieldHeight);
            textField.setFont(new Font("Arial", Font.PLAIN, 14));
            textField.setHorizontalAlignment(JTextField.LEFT);
            textField.setEditable(false);
            textField.setFocusable(false);
            textField.setBackground(new Color(240, 248, 255)); // Màu xanh nhạt pastel
            textField.setBorder(BorderFactory.createLineBorder(new Color(173, 216, 230))); // Viền xanh nhạt

            var displayValue = "";
            if (i < 9) {
                displayValue = rentalData[i] != null ? rentalData[i].toString() : "";
            }
            textField.setText(displayValue);

            switch (labels[i]) {
                case "Book ID" -> txtBookID = textField;
                case "Book Name" -> txtBookName = textField;
                case "Quantity" -> txtQuantity = textField;
                case "Rental Price" -> {
                    txtRentalPrice = textField;
                    var value = Double.parseDouble(displayValue);
                    displayValue = currencyFormat.format(value);
                    textField.setText(displayValue);
                }
                case "Borrow Date" -> {
                    txtBorrowDate = textField;
                    textField.setText(LocalDate.now().toString());
                }
                case "Return Date" -> txtReturnDate = textField;
                case "Number Of Days" -> {
                    txtNumberOfDays = textField;
                    displayValue = rentalData[4] != null ? rentalData[4].toString() : "";
                    textField.setText(displayValue);
                }
                case "Total Rental Price" -> {
                    txtTotalRentalPrice = textField;
                    displayValue = currencyFormat.format(totalRentalPrice);
                    textField.setText(displayValue);
                }
                case "Book Price" -> {
                    txtPrice = textField;
                    displayValue = rentalData[7] != null ? rentalData[7].toString() : "";
                    var value = Double.parseDouble(displayValue);
                    displayValue = currencyFormat.format(value);
                    textField.setText(displayValue);
                }
                case "Deposit Percentage" -> {
                    txtDepositPercentage = textField;
                    displayValue = rentalData[6] != null ? rentalData[6].toString() : "";
                    textField.setText(displayValue + " %");
                }
                case "Deposit Amount" -> {
                    txtDepositAmount = textField;
                    var depositAmount = totalPrice - totalRentalPrice;
                    displayValue = currencyFormat.format(depositAmount);
                    textField.setText(displayValue);
                }
                case "Total Price" -> {
                    txtTotalPrice = textField;
                    var value = Double.parseDouble(rentalData[8].toString());
                    displayValue = currencyFormat.format(value);
                    textField.setText(displayValue);
                }
            }
            contentPanelView.add(textField);
        }

        // Close Button
        var closeButton = new GradientButton("Close"); // Sử dụng GradientButton
        closeButton.setBounds(120, fieldY + (labels.length + 2) * 44, 200, 40); // Đặt vị trí và kích thước
        closeButton.addActionListener(e -> dispose()); // Sự kiện đóng dialog
        contentPanelView.add(closeButton); // Thêm nút vào content panel
    }

    // Định nghĩa lớp GradientButton
    static class GradientButton extends JButton {
        private static final long serialVersionUID = 1L;

        public GradientButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setForeground(Color.WHITE);
            setFont(new Font("Arial", Font.BOLD, 14));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            GradientPaint gradient = new GradientPaint(0, 0, Color.decode("#c2e59c"), 0, getHeight(), Color.decode("#64b3f4"));
            g2d.setPaint(gradient);
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 50, 50);

            g2d.setColor(Color.WHITE);
            g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 50, 50);

            g2d.dispose();
            super.paintComponent(g);
        }
    }
}
