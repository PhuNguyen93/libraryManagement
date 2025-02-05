package dialog;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class Pay extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPayments = new JPanel();
	private JPanel panelQRCode;
	private double gltotalAmount; // Variable to store total amount

	// Constructor to accept total amount
	public Pay(double totalAmount, JDialog parent) {
		super(parent, "Child Dialog", ModalityType.DOCUMENT_MODAL); // Đặt dialog cha và kiểu modal
        setSize(300, 200);
        setLocationRelativeTo(parent); // Căn giữa dialog con so với dialog cha
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);

		this.gltotalAmount = totalAmount; // Assign total amount
		setTitle("Payments");
		setBounds(100, 100, 450, 402);
		setLocationRelativeTo(null); // Center the dialog
		getContentPane().setLayout(null);
		contentPayments.setBounds(0, 0, 434, 326);
		contentPayments.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPayments);
		contentPayments.setLayout(null);

		panelQRCode = new JPanel();
		panelQRCode.setBounds(155, 11, 150, 150);
		panelQRCode.setBackground(Color.WHITE);

		var buttonPayments = new JPanel();
		buttonPayments.setBounds(0, 325, 434, 38);
		getContentPane().add(buttonPayments);
		buttonPayments.setLayout(null);

		// OK Button
		var okButton = new JButton("OK");
		okButton.setBounds(312, 5, 47, 23);
		okButton.addActionListener(this::btnOkActionPerformed); // Custom ActionListener
		buttonPayments.add(okButton);
		getRootPane().setDefaultButton(okButton);

		// Cancel Button
		var cancelButton = new JButton("Cancel");
		cancelButton.setBounds(364, 5, 65, 23);
		cancelButton.addActionListener(this::btnCancelActionPerformed); // Custom ActionListener
		buttonPayments.add(cancelButton);
	}

	// Event handler for "Generate QR Code"
	private void btnGenerateQRCodeActionPerformed(ActionEvent e) {
		try {
			// QR Code information
			var name = "Tên: Aptech";
			var bank = "Ngân Hàng: Vietcom Bank";
			var account = "Số Tài Khoản: 1234566789";
			var amount = "Tổng Tiền: " + String.format("%.2f", gltotalAmount); // Use totalAmount passed in constructor
			var qrData = String.join("\n", name, bank, account, amount);

			// Generate QR Code
			var qrImage = generateQRCodeImage(qrData, 150, 150);

			// Display QR Code in panelQRCode
			var qrLabel = new JLabel(new ImageIcon(qrImage));
			panelQRCode.removeAll();
			panelQRCode.add(qrLabel);
			panelQRCode.revalidate();
			panelQRCode.repaint();

			JOptionPane.showMessageDialog(this, "QR Code generated successfully!");
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error generating QR Code: " + ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	// Event handler for "OK" button
	private void btnOkActionPerformed(ActionEvent e) {
		JOptionPane.showMessageDialog(this, "Payment processed successfully!");
		dispose(); // Close the dialog
	}

	// Event handler for "Cancel" button
	private void btnCancelActionPerformed(ActionEvent e) {
		dispose(); // Close the dialog
	}

	// Method to generate QR Code
	private BufferedImage generateQRCodeImage(String data, int width, int height) throws Exception {
		var qrCodeWriter = new QRCodeWriter();
		Map<EncodeHintType, Object> hints = new HashMap<>();
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

		var bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, width, height, hints);

		var image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (var x = 0; x < width; x++) {
			for (var y = 0; y < height; y++) {
				image.setRGB(x, y, bitMatrix.get(x, y) ? Color.BLACK.getRGB() : Color.WHITE.getRGB());
			}
		}
		return image;
	}
}
