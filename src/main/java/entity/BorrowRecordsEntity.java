package entity;

import java.util.Date;

public class BorrowRecordsEntity {
	private int recordID;
	private int userID;
	private String userName;
	private int bookID;

	private String bookName;
	private int quantity;
	private int numberOfDays;
	private Date borrowDate;
	private Date dueReturnDate;
	private String status;
	private double fineAmount;
	private double borrowPrice;
	private boolean isDeleted;
	private Date createdAt;
	private int createdBy;
	private Date updatedAt;
	private int updatedBy;
	private int paymentID;
	private String isbn;
	private int studentID;
	private String studentName;
	private String studentCode;
	private String studentPhoneNum;
	private String studentEmail;
	private String userEmail;
	private String bookImage;
	private double total;

	public BorrowRecordsEntity() {
		super();
	}


	public BorrowRecordsEntity(int recordID, int userID, String userName, int bookID, String bookName, int quantity,
			int numberOfDays, Date borrowDate, Date dueReturnDate, String status, double fineAmount, double borrowPrice,
			boolean isDeleted, Date createdAt, int createdBy, Date updatedAt, int updatedBy, int paymentID,
			int studentID, String studentName, String studentCode) {
		super();
		this.recordID = recordID;
		this.userID = userID;
		this.userName = userName;
		this.bookID = bookID;
		this.bookName = bookName;
		this.quantity = quantity;
		this.numberOfDays = numberOfDays;
		this.borrowDate = borrowDate;
		this.dueReturnDate = dueReturnDate;
		this.status = status;
		this.fineAmount = fineAmount;
		this.borrowPrice = borrowPrice;
		this.isDeleted = isDeleted;
		this.createdAt = createdAt;
		this.createdBy = createdBy;
		this.updatedAt = updatedAt;
		this.updatedBy = updatedBy;
		this.paymentID = paymentID;
		this.studentID = studentID;
		this.studentName = studentName;
		this.studentCode = studentCode;
	}



	// Getters and Setters
	public String getStudentCode() {
		return studentCode;
	}

	public void setStudentCode(String studentCode) {
		this.studentCode = studentCode;
	}
	public int getRecordID() {
		return recordID;
	}

	public void setRecordID(int recordID) {
		this.recordID = recordID;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public int getBookID() {
		return bookID;
	}

	public void setBookID(int bookID) {
		this.bookID = bookID; // Corrected to match the field type
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getNumberOfDays() {
		return numberOfDays;
	}

	public void setNumberOfDays(int numberOfDays) {
		this.numberOfDays = numberOfDays;
	}

	public Date getBorrowDate() {
		return borrowDate;
	}

	public void setBorrowDate(Date borrowDate) {
		this.borrowDate = borrowDate;
	}

	public Date getDueReturnDate() {
		return dueReturnDate;
	}

	public void setDueReturnDate(Date dueReturnDate) {
		this.dueReturnDate = dueReturnDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public double getFineAmount() {
		return fineAmount;
	}

	public void setFineAmount(double fineAmount) {
		this.fineAmount = fineAmount;
	}

	public double getBorrowPrice() {
		return borrowPrice;
	}

	public void setBorrowPrice(double borrowPrice) {
		this.borrowPrice = borrowPrice;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean deleted) {
		isDeleted = deleted;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public int getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}

	public int getPaymentID() {
		return paymentID;
	}

	public void setPaymentID(int paymentID) {
		this.paymentID = paymentID;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getStudentID() {
		return studentID;
	}

	public void setStudentID(int studentID) {
		this.studentID = studentID;
	}

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	// Add Other Entity

	@Override
	public String toString() {
		return "BorrowRecordsEntity [recordID=" + recordID + ", userID=" + userID + ", userName=" + userName
				+ ", bookID=" + bookID + ", bookName=" + bookName + ", quantity=" + quantity + ", numberOfDays="
				+ numberOfDays + ", borrowDate=" + borrowDate + ", dueReturnDate=" + dueReturnDate + ", status="
				+ status + ", fineAmount=" + fineAmount + ", borrowPrice=" + borrowPrice + ", isDeleted=" + isDeleted
				+ ", createdAt=" + createdAt + ", createdBy=" + createdBy + ", updatedAt=" + updatedAt + ", updatedBy="
				+ updatedBy + ", paymentID=" + paymentID + ", studentID=" + studentID + ", studentName=" + studentName
				+ ", studentCode=" + studentCode + "]";
	}


	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getStudentPhoneNum() {
		return studentPhoneNum;
	}

	public void setStudentPhoneNum(String studentPhoneNum) {
		this.studentPhoneNum = studentPhoneNum;
	}

	public String getStudentEmail() {
		return studentEmail;
	}

	public void setStudentEmail(String studentEmail) {
		this.studentEmail = studentEmail;
	}


	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getBookImage() {
		return bookImage;
	}

	public void setBookImage(String bookImage) {
		this.bookImage = bookImage;
	}


	public double getTotal() {
		return total;
	}


	public void setTotal(double total) {
		this.total = total;
	}
	
}
