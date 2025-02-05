package gui;

import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import dao.PaymentDao;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.swing.JRViewer;

public class ShowReport extends JFrame {
	public ShowReport() {
		setTitle("Revenue for the Month");
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	public void Report() {
	    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Chỉ đóng cửa sổ này
		setBounds(100, 100, 880, 601);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		dispose();
		// viewer = new JRViewer((JasperPrint) null);
		// contentPane.add(viewer, BorderLayout.CENTER);
		showReport();
	}
	public void showReport() {
	    try {
	        var dao = new PaymentDao();
	        Map<String, Object> map = new HashMap<>();
	        var payments = dao.selectAll();
	        if (payments == null || payments.isEmpty()) {
	            JOptionPane.showMessageDialog(null, "No payment data available.");
	            return;
	        }
	        JRBeanCollectionDataSource paymentDataSource = new JRBeanCollectionDataSource(payments);
	        map.put("Payment", paymentDataSource);

	        String file = "Report/Payment.jrxml";
	        JasperReport rp = JasperCompileManager.compileReport(file);

	        JasperPrint print = JasperFillManager.fillReport(rp, map, new JREmptyDataSource());

	        // Tạo JLabel để hiển thị tiêu đề
	        JLabel lblTitle = new JLabel("Revenue for the Month", JLabel.CENTER);
	        lblTitle.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 18));
	        lblTitle.setBorder(new javax.swing.border.EmptyBorder(10, 10, 10, 10)); // Padding cho tiêu đề

	        // Tạo JPanel chứa JRViewer để điều chỉnh khoảng cách
	        JPanel panelViewer = new JPanel(new BorderLayout());
	        panelViewer.setBorder(new javax.swing.border.EmptyBorder(10, 50, 10, 50)); // Tăng padding trên để đẩy bảng lên
	        panelViewer.add(new JRViewer(print), BorderLayout.CENTER);

	        // Đảm bảo contentPane đã được khởi tạo và có BorderLayout
	        if (contentPane == null) {
	            contentPane = new JPanel(new BorderLayout());
	            setContentPane(contentPane);
	        }

	        // Thêm JLabel vào vùng NORTH
	        contentPane.add(lblTitle, BorderLayout.NORTH);

	        // Thêm JPanel chứa JRViewer vào vùng CENTER
	        contentPane.add(panelViewer, BorderLayout.CENTER);

	        this.pack();
	    } catch (JRException e) {
	        JOptionPane.showMessageDialog(null, e.getMessage());
	    }
	}


}