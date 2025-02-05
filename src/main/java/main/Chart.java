package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JPanel;

import dao.ChartDao;

public class Chart extends JPanel {

	private static final long serialVersionUID = 1L;

	// Dynamic data for Bar Chart - Weekly Book Loans Statistics
	private int[] barValues;
	private String[] barLabels;
	private Color[] barColors = { new Color(100, 150, 255) }; // Bar color

	// Dynamic data for Pie Chart - Book Category Statistics
	private int[] pieValues;
	private String[] pieLabels;
	private List<Color> pieColors = new ArrayList<>();
	private Set<Color> usedColors = new HashSet<>();

	// Dynamic data for Line Chart - Weekly Revenue
	private double[] lineValues;
	private String[] lineLabels;

	// Sample data for Area Chart (unchanged)
	private int[] areaValues = { 20, 30, 50, 70, 60, 40, 80 };
	private String[] areaLabels = { "Q1", "Q2", "Q3", "Q4", "Q5", "Q6", "Q7" };

	// ChartDao object for data retrieval
	private ChartDao chartDao;

	// Current Year and Month
	private int currentYear;
	private int currentMonth;
	private int[] damagedBooksValues;
	private String[] damagedBooksLabels;

	public Chart(int year, int month) {
		setPreferredSize(new Dimension(1200, 800)); // Increase overall size if needed
		setBackground(Color.WHITE);
		setLayout(null); // Use null layout to manually position components

		this.currentYear = year;
		this.currentMonth = month;

		// Initialize ChartDao and retrieve data for Pie Chart
		chartDao = new ChartDao();
		updatePieChartData();

		// Retrieve data for Bar Chart - Weekly Book Loans in a specific month
		updateBarChartData(currentYear, currentMonth);

		// Retrieve data for Line Chart - Weekly Revenue in a specific month
		updateLineChartData(currentYear, currentMonth);

	}
	// Method to generate a random color
	private Color getRandomColor() {
	    Color color;
	    do {
	        int r = (int) (Math.random() * 256);
	        int g = (int) (Math.random() * 256);
	        int b = (int) (Math.random() * 256);
	        color = new Color(r, g, b);
	    } while (usedColors.contains(color)); // Ensure color is not used
	    usedColors.add(color);
	    return color;
	}
	public void updatePieChartData() {
	    var pieData = chartDao.getBookCountByCategoryName(); // Lấy dữ liệu mới từ cơ sở dữ liệu
	    pieLabels = pieData.keySet().toArray(new String[0]);
	    pieValues = pieData.values().stream().mapToInt(Integer::intValue).toArray();

	    // Reset usedColors for new data
	    usedColors.clear();

	    // Generate colors for each category
	    pieColors.clear();
	    for (int i = 0; i < pieLabels.length; i++) {
	        pieColors.add(getRandomColor());
	    }

	    repaint(); // Vẽ lại biểu đồ
	}

	public void updateBarChartData(int year, int month) {
		this.currentYear = year;
		this.currentMonth = month;

		// Retrieve data for Bar Chart - Weekly Book Loans in a specific month
		var barData = chartDao.getWeeklyLoanCountInMonth(currentYear, currentMonth);

		// Determine the number of weeks in the month
		var yearMonthObject = YearMonth.of(currentYear, currentMonth);
		var totalWeeks = getWeeksInMonth(yearMonthObject);

		// Create a complete Map with all weeks, assign 0 if no data
		Map<String, Integer> completeBarData = new LinkedHashMap<>();
		for (var week = 1; week <= totalWeeks; week++) {
			var key = "W" + week;
			completeBarData.put(key, barData.getOrDefault(key, 0));
		}

		// Convert Map to arrays for Bar Chart
		barLabels = new String[completeBarData.size()];
		barValues = new int[completeBarData.size()];
		var index = 0;
		for (Map.Entry<String, Integer> entry : completeBarData.entrySet()) {
			barLabels[index] = entry.getKey(); // "W1", "W2", etc.
			barValues[index] = entry.getValue();
			index++;
		}

		// Repaint the chart with updated data
		repaint();
	}

	public void updateLineChartData(int year, int month) {
		var revenueData = chartDao.getWeeklyRevenue(year, month);
		var yearMonthObject = YearMonth.of(year, month);
		var totalWeeks = getWeeksInMonth(yearMonthObject);

		// Create a complete Map with all weeks, assign 0 if no data
		Map<String, Double> completeRevenueData = new LinkedHashMap<>();
		for (var week = 1; week <= totalWeeks; week++) {
			var key = "W" + week;
			completeRevenueData.put(key, revenueData.getOrDefault(key, 0.0));
		}

		// Convert Map to arrays for Line Chart
		lineLabels = completeRevenueData.keySet().toArray(new String[0]);
		lineValues = completeRevenueData.values().stream().mapToDouble(Double::doubleValue).toArray();

		// Repaint the chart with updated data
		repaint();
	}

	public void refreshData() {
		var currentDate = java.time.LocalDate.now();
		this.currentYear = currentDate.getYear();
		this.currentMonth = currentDate.getMonthValue();

		updatePieChartData();
		updateBarChartData(currentYear, currentMonth);
		updateLineChartData(currentYear, currentMonth);
		repaint();
	}

	private int getWeeksInMonth(YearMonth yearMonth) {
		var firstDayOfMonth = yearMonth.atDay(1).getDayOfWeek().getValue(); // 1 = Monday, 7 = Sunday
		var daysInMonth = yearMonth.lengthOfMonth();
		return (int) Math.ceil((firstDayOfMonth - 1 + daysInMonth) / 7.0);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		var g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// Draw Bar Chart - Weekly Book Loans in a specific month
		drawBarChart(g2, 100, 150, 400, 200); // Increase height to accommodate Y-axis

		// Draw Pie Chart - Book Categories
		drawPieChart(g2, 630, 250, 100);

		// Draw Line Chart - Weekly Revenue
		drawLineChart(g2, 100, 450, 350, 200);

		// Draw Area Chart (unchanged)
		drawAreaChart(g2, 550, 450, 350, 200);
	}

	/**
	 * Draw Bar Chart - Weekly Book Loans in a specific month
	 */
	private void drawBarChart(Graphics2D g2, int x, int y, int width, int height) {
		if (barDataIsEmpty()) {
			// No data to display
			g2.setColor(Color.BLACK);
			g2.setFont(new Font("Arial", Font.BOLD, 16));
			g2.drawString("No data to display Bar Chart", x, y - 20);
			return;
		}

		var barWidth = 40; // Width of each bar
		var maxBarHeight = height; // Maximum height (subtract 60 to accommodate labels and Y-axis)
		var maxValue = 0;

		// Find the maximum value to determine scaling
		for (int value : barValues) {
			if (value > maxValue) {
				maxValue = value;
			}
		}
		if (maxValue == 0) {
			maxValue = 1; // Prevent division by zero
		}

		var spacing = 15; // Spacing between bars

		// Draw X and Y axes
		g2.setColor(Color.BLACK);
		g2.drawLine(x, y + maxBarHeight, x + (barWidth + spacing) * barValues.length + spacing, y + maxBarHeight); // X-axis
		g2.drawLine(x, y, x, y + maxBarHeight); // Y-axis

		// Draw Y-axis ticks
		var numTicks = 5;
		g2.setFont(new Font("Arial", Font.PLAIN, 12));
		for (var i = 0; i <= numTicks; i++) {
			var tickValue = (int) Math.ceil((maxValue / (double) numTicks) * i);
			var tickY = y + maxBarHeight - (int) ((tickValue / (double) maxValue) * maxBarHeight);
			g2.drawLine(x - 5, tickY, x, tickY); // Tick mark
			g2.drawString(String.valueOf(tickValue), x - 40, tickY + 5); // Tick label
		}

		// Draw Y-axis label
		g2.setFont(new Font("Arial", Font.BOLD, 14));
		// Draw the bars
		var xPos = x + spacing;
		for (var i = 0; i < barValues.length; i++) {
			var barHeight = (barValues[i] * maxBarHeight) / maxValue; // Calculate bar height proportionally
			g2.setColor(barColors[0]); // Bar color
			g2.fillRect(xPos, y + maxBarHeight - barHeight, barWidth, barHeight);

			// Draw bar border
			g2.setColor(Color.BLACK);
			g2.drawRect(xPos, y + maxBarHeight - barHeight, barWidth, barHeight);

			// Display value above the bar
			g2.setFont(new Font("Arial", Font.BOLD, 12));
			var valueStr = String.valueOf(barValues[i]);
			var valueWidth = g2.getFontMetrics().stringWidth(valueStr);
			g2.drawString(valueStr, xPos + (barWidth - valueWidth) / 2, y + maxBarHeight - barHeight - 5);

			// Display label below the bar
			g2.setFont(new Font("Arial", Font.PLAIN, 12));
			var label = barLabels[i];
			var labelWidth = g2.getFontMetrics().stringWidth(label);
			g2.drawString(label, xPos + (barWidth - labelWidth) / 2, y + maxBarHeight + 25);

			xPos += barWidth + spacing; // Move to the next bar position
		}

		// Display chart title
		g2.setFont(new Font("Arial", Font.BOLD, 16));
		g2.drawString("Bar Chart - Weekly Book Loans for " + currentMonth + "/" + currentYear, x, y - 30);
	}

	private boolean barDataIsEmpty() {
		return barLabels == null || barValues == null || barLabels.length == 0 || barValues.length == 0;
	}

	/**
	 * Draw Pie Chart - Book Categories
	 */
	private void drawPieChart(Graphics2D g2, int centerX, int centerY, int radius) {
	    if (pieDataIsEmpty()) {
	        // No data to display
	        g2.setColor(Color.BLACK);
	        g2.setFont(new Font("Arial", Font.BOLD, 16));
	        g2.drawString("No data to display Pie Chart", centerX - radius, centerY - radius - 20);
	        return;
	    }

	    // Calculate total value to determine proportions
	    var total = 0;
	    for (int value : pieValues) {
	        total += value;
	    }

	    // Draw pie sections
	    var startAngle = 0;
	    for (var i = 0; i < pieValues.length; i++) {
	        var arcAngle = (int) Math.round((double) pieValues[i] / total * 360); // Calculate angle for the section
	        g2.setColor(pieColors.get(i));
	        g2.fillArc(centerX - radius, centerY - radius, radius * 2, radius * 2, startAngle, arcAngle);
	        startAngle += arcAngle;
	    }

	    // Draw legend
	    var legendX = centerX + radius + 20; // X position for legend
	    var legendY = centerY - radius; // Y position for legend

	    for (var i = 0; i < pieValues.length; i++) {
	        g2.setColor(pieColors.get(i));
	        g2.fillRect(legendX, legendY + (i * 30), 20, 20); // Draw color box
	        g2.setColor(Color.BLACK);
	        g2.setFont(new Font("Arial", Font.PLAIN, 14));
	        g2.drawString(pieLabels[i] + " - " + pieValues[i], legendX + 30, legendY + 15 + (i * 30)); // Draw label
	    }

	    // Display chart title
	    g2.setFont(new Font("Arial", Font.BOLD, 16));
	    g2.drawString("Pie Chart - Book Categories", centerX - radius, centerY - radius - 30);
	}
	private boolean pieDataIsEmpty() {
		return pieLabels == null || pieValues == null || pieLabels.length == 0 || pieValues.length == 0;
	}

	/**
	 * Draw Line Chart - Weekly Revenue
	 */
	/**
	 * Draw Line Chart - Weekly Revenue
	 */
	/**
	 * Draw Line Chart - Weekly Revenue
	 */
	private void drawLineChart(Graphics2D g2, int x, int y, int width, int height) {
		if (lineDataIsEmpty()) {
			// No data to display
			g2.setColor(Color.BLACK);
			g2.setFont(new Font("Arial", Font.BOLD, 16));
			g2.drawString("No data to display Line Chart", x, y - 20);
			return;
		}

		var maxLineValue = Arrays.stream(lineValues).max().orElse(1.0);
		if (maxLineValue == 0.0) {
			maxLineValue = 1.0; // Prevent division by zero
		}

		var spacing = lineValues.length > 1 ? width / (lineValues.length - 1) : width;

		// Draw X and Y axes
		g2.setColor(Color.BLACK);
		g2.drawLine(x, y + height, x + width, y + height); // X-axis
		g2.drawLine(x, y, x, y + height); // Y-axis

		// Draw Y-axis ticks with increased spacing
		var numTicks = 5;
		g2.setFont(new Font("Arial", Font.PLAIN, 10)); // Smaller font for tick labels
		for (var i = 0; i <= numTicks; i++) {
			var tickValue = (maxLineValue / numTicks) * i;
			var tickY = y + height - (int) ((tickValue / maxLineValue) * height);
			g2.drawLine(x - 5, tickY, x, tickY); // Tick mark
			var tickLabel = String.format("%.0f", tickValue);
			g2.drawString(tickLabel, x - 70, tickY + 5); // Increased left spacing
		}

		// Draw Y-axis label
		g2.setFont(new Font("Arial", Font.BOLD, 12));

		// Draw lines connecting points
		g2.setColor(Color.BLUE);
		g2.setStroke(new BasicStroke(2)); // Thicker line for better visibility
		for (var i = 0; i < lineValues.length - 1; i++) {
			var x1 = x + i * spacing;
			var y1 = y + height - (int) ((lineValues[i] / maxLineValue) * height);
			var x2 = x + (i + 1) * spacing;
			var y2 = y + height - (int) ((lineValues[i + 1] / maxLineValue) * height);
			g2.drawLine(x1, y1, x2, y2);
		}
		g2.setStroke(new BasicStroke(1)); // Reset stroke

		// Draw points and labels
		g2.setFont(new Font("Arial", Font.PLAIN, 10)); // Smaller font for data labels
		for (var i = 0; i < lineValues.length; i++) {
			var xPos = x + i * spacing;
			var yPos = y + height - (int) ((lineValues[i] / maxLineValue) * height);

			// Draw the point
			g2.setColor(Color.RED);
			g2.fillOval(xPos - 4, yPos - 4, 8, 8); // Draw circular point

			// Draw the revenue value with background for readability
			var revenueStr = String.format("%.2f", lineValues[i]);
			var stringWidth = g2.getFontMetrics().stringWidth(revenueStr);
			var stringHeight = g2.getFontMetrics().getHeight();

			// Position labels above and to the right of the point with small offset
			var labelX = xPos + 5; // 5 pixels to the right
			var labelY = yPos - 10; // 10 pixels above

			// Ensure label does not go beyond the panel boundaries
			if (labelX + stringWidth > x + width) {
				labelX = xPos - stringWidth - 5; // Position to the left if exceeding
			}
			if (labelY - stringHeight < y) {
				labelY = yPos + 15; // Position below if exceeding top
			}

			// Draw a filled rectangle as background for better readability
			g2.setColor(new Color(255, 255, 255, 200)); // Semi-transparent white
			g2.fillRect(labelX - 2, labelY - stringHeight + 2, stringWidth + 4, stringHeight);

			// Draw the revenue value
			g2.setColor(Color.BLACK);
			g2.drawString(revenueStr, labelX, labelY);
		}

		// Display week labels below the X-axis
		g2.setFont(new Font("Arial", Font.PLAIN, 10)); // Smaller font for week labels
		for (var i = 0; i < lineLabels.length; i++) {
			var xPos = x + i * spacing;
			var weekLabel = lineLabels[i];
			var labelWidth = g2.getFontMetrics().stringWidth(weekLabel);
			g2.drawString(weekLabel, xPos - labelWidth / 2, y + height + 15);
		}

		// Display chart title
		g2.setFont(new Font("Arial", Font.BOLD, 14));
		g2.drawString("Line Chart - Weekly Revenue for " + currentMonth + "/" + currentYear, x, y - 30);
	}

	private boolean lineDataIsEmpty() {
		return lineLabels == null || lineValues == null || lineLabels.length == 0 || lineValues.length == 0;
	}

	/**
	 * Draw Area Chart (unchanged)
	 */
	private void drawAreaChart(Graphics2D g2, int x, int y, int width, int height) {
		var maxAreaHeight = height; // Maximum height
		var maxValue = 100; // Maximum Y-axis value
		var spacing = width / (areaValues.length - 1); // Spacing between points

		// Draw X and Y axes
		g2.setColor(Color.BLACK);
		g2.drawLine(x, y + maxAreaHeight, x + width, y + maxAreaHeight); // X-axis
		g2.drawLine(x, y, x, y + maxAreaHeight); // Y-axis

		// Draw the area
		g2.setColor(new Color(100, 200, 255, 100));
		for (var i = 0; i < areaValues.length - 1; i++) {
			var x1 = x + i * spacing;
			var y1 = y + maxAreaHeight - (areaValues[i] * maxAreaHeight) / maxValue;
			var x2 = x + (i + 1) * spacing;
			var y2 = y + maxAreaHeight - (areaValues[i + 1] * maxAreaHeight) / maxValue;

			// Fill the area below the line
			int[] xPoints = { x1, x2, x2, x1 };
			int[] yPoints = { y1, y2, y + maxAreaHeight, y + maxAreaHeight };
			g2.fillPolygon(xPoints, yPoints, 4);
		}

		// Draw points and labels
		g2.setFont(new Font("Arial", Font.PLAIN, 12));
		for (var i = 0; i < areaValues.length; i++) {
			var x1 = x + i * spacing;
			var y1 = y + maxAreaHeight - (areaValues[i] * maxAreaHeight) / maxValue;
			g2.setColor(Color.BLACK);
			g2.drawString(areaLabels[i], x1 - 10, y + maxAreaHeight + 15);
			g2.drawString(String.valueOf(areaValues[i]), x1 - 10, y1 - 10);
		}

		// Display chart title
		g2.setFont(new Font("Arial", Font.BOLD, 16));
		g2.drawString("Area Chart - Products by Quarter", x, y - 30);
	}
}