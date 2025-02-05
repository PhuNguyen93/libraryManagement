package dialog;

import java.awt.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ViewOrderDetailDialog extends JDialog {
    private static final long serialVersionUID = 1L;

    public ViewOrderDetailDialog(Window parent, String bookImage, String bookTitle, String author, String publisher, String category,
    		Date borrowDate, Date returnDate, BigDecimal depositAmount, BigDecimal rentalPrice, BigDecimal finneAmount, 
                                 BigDecimal totalPrice) {
        super(parent, "Book Details", ModalityType.APPLICATION_MODAL);
        setSize(500, 600);
        setLocationRelativeTo(parent);
        getContentPane().setLayout(null);

        // Gradient Panel
        JPanel gradientPanel = new JPanel() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
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
        gradientPanel.setBounds(0, 0, 500, 600);
        setContentPane(gradientPanel);

        DecimalFormat currencyFormat = new DecimalFormat("#,##0.00");

        // Panel chứa hình ảnh sách
        var imagePanel = new JPanel();
        imagePanel.removeAll();
        imagePanel.setBounds(20, 20, 120, 150);
        imagePanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JLabel lblBookImage = new JLabel();
        lblBookImage.setIcon(loadBookCover(bookImage));
        gradientPanel.add(imagePanel);

        imagePanel.add(lblBookImage);
        imagePanel.revalidate();
        imagePanel.repaint();
        
        // Các nhãn thông tin
        JLabel lblBookTitle = new JLabel("Book Title: " + bookTitle);
        lblBookTitle.setBounds(160, 30, 300, 25);
        lblBookTitle.setFont(new Font("Arial", Font.BOLD, 14));
        lblBookTitle.setForeground(Color.WHITE);
        gradientPanel.add(lblBookTitle);

        JLabel lblAuthor = new JLabel("Author: " + author);
        lblAuthor.setBounds(160, 70, 300, 25);
        lblAuthor.setFont(new Font("Arial", Font.PLAIN, 14));
        lblAuthor.setForeground(Color.WHITE);
        gradientPanel.add(lblAuthor);

        JLabel lblPublisher = new JLabel("Publisher: " + publisher);
        lblPublisher.setBounds(160, 110, 300, 25);
        lblPublisher.setFont(new Font("Arial", Font.PLAIN, 14));
        lblPublisher.setForeground(Color.WHITE);
        gradientPanel.add(lblPublisher);

        JLabel lblCategory = new JLabel("Category: " + category);
        lblCategory.setBounds(160, 150, 300, 25);
        lblCategory.setFont(new Font("Arial", Font.PLAIN, 14));
        lblCategory.setForeground(Color.WHITE);
        gradientPanel.add(lblCategory);

        JLabel lblBorrowDate = new JLabel("Borrow Date: " + borrowDate);
        lblBorrowDate.setBounds(160, 186, 300, 25);
        lblBorrowDate.setFont(new Font("Arial", Font.PLAIN, 14));
        lblBorrowDate.setForeground(Color.WHITE);
        gradientPanel.add(lblBorrowDate);

        JLabel lblReturnDate = new JLabel("Return Date: " + returnDate);
        lblReturnDate.setBounds(160, 222, 300, 25);
        lblReturnDate.setFont(new Font("Arial", Font.PLAIN, 14));
        lblReturnDate.setForeground(Color.WHITE);
        gradientPanel.add(lblReturnDate);

        JLabel lblDepositAmount = new JLabel("Deposit: " + currencyFormat.format(depositAmount));
        lblDepositAmount.setBounds(160, 258, 300, 25);
        lblDepositAmount.setFont(new Font("Arial", Font.PLAIN, 14));
        lblDepositAmount.setForeground(Color.WHITE);
        gradientPanel.add(lblDepositAmount);

        JLabel lblRentalPrice = new JLabel("Rental Price: " + currencyFormat.format(rentalPrice));
        lblRentalPrice.setBounds(160, 294, 300, 25);
        lblRentalPrice.setFont(new Font("Arial", Font.PLAIN, 14));
        lblRentalPrice.setForeground(Color.WHITE);
        gradientPanel.add(lblRentalPrice);

        JLabel lblTotalPrice = new JLabel("Total Price: " + currencyFormat.format(totalPrice));
        lblTotalPrice.setBounds(20, 371, 300, 25);
        lblTotalPrice.setFont(new Font("Arial", Font.BOLD, 14));
        lblTotalPrice.setForeground(Color.YELLOW);
        gradientPanel.add(lblTotalPrice);

        // Nút Close
        JButton btnClose = new GradientButton("Close");
        btnClose.setBounds(150, 500, 200, 40);
        btnClose.addActionListener(e -> dispose());
        gradientPanel.add(btnClose);
        
        JLabel lblFineAmount = new JLabel("Fine Amount: " + currencyFormat.format(finneAmount));
        lblFineAmount.setForeground(Color.WHITE);
        lblFineAmount.setFont(new Font("Arial", Font.PLAIN, 14));
        lblFineAmount.setBounds(160, 330, 300, 25);
        gradientPanel.add(lblFineAmount);
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
		var scaledImage = icon.getImage().getScaledInstance(120, 150, Image.SCALE_SMOOTH);
		return new ImageIcon(scaledImage);
	}
    // GradientButton class
    class GradientButton extends JButton {
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

            // Gradient từ trên xuống dưới
            GradientPaint gradient = new GradientPaint(0, 0, Color.decode("#c2e59c"), 0, getHeight(), Color.decode("#64b3f4"));

            g2d.setPaint(gradient);
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 50, 50);

            // Vẽ viền
            g2d.setColor(Color.WHITE);
            g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 50, 50);

            g2d.dispose();
            super.paintComponent(g);
        }
    }
}
