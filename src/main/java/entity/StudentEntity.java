package entity;

import java.time.LocalDate;

public class StudentEntity {
	private int studentID;
	private String studentCode;
	private String avatar;
	private String fullName;
	private LocalDate dateOfBirth;
	private String gender;
	private String email;
	private String phoneNumber;
	private String address;
	private int enrollmentYear;
	private String schoolName;
	private int userID;
	private int totalBooksRented; // Số lượng sách đã thuê
	private int lateReturnsCount; // Số lần trả sách trễ
	private int damagedBooksCount; // Số lần sách hư hại
	private int totalOrders; // Tổng số đơn
	private boolean isDeleted;
	private boolean isPayment;
	
	public StudentEntity() {
		super();
	}

	public StudentEntity(int studentID, String studentCode, String avatar, String fullName, LocalDate dateOfBirth,
			String gender, String email, String phoneNumber, String address, int enrollmentYear, String schoolName,
			int userID, int totalBooksRented, int lateReturnsCount, int damagedBooksCount, int totalOrders,
			boolean isDeleted) {
		super();
		this.studentID = studentID;
		this.studentCode = studentCode;
		this.avatar = avatar;
		this.fullName = fullName;
		this.dateOfBirth = dateOfBirth;
		this.gender = gender;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.enrollmentYear = enrollmentYear;
		this.schoolName = schoolName;
		this.userID = userID;
		this.totalBooksRented = totalBooksRented;
		this.lateReturnsCount = lateReturnsCount;
		this.damagedBooksCount = damagedBooksCount;
		this.totalOrders = totalOrders;
		this.isDeleted = isDeleted;
	}

	public int getStudentID() {
		return studentID;
	}

	public void setStudentID(int studentID) {
		this.studentID = studentID;
	}

	public String getStudentCode() {
		return studentCode;
	}

	public void setStudentCode(String studentCode) {
		this.studentCode = studentCode;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getEnrollmentYear() {
		return enrollmentYear;
	}

	public void setEnrollmentYear(int enrollmentYear) {
		this.enrollmentYear = enrollmentYear;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public int getTotalBooksRented() {
		return totalBooksRented;
	}

	public void setTotalBooksRented(int totalBooksRented) {
		this.totalBooksRented = totalBooksRented;
	}

	public int getLateReturnsCount() {
		return lateReturnsCount;
	}

	public void setLateReturnsCount(int lateReturnsCount) {
		this.lateReturnsCount = lateReturnsCount;
	}

	public int getDamagedBooksCount() {
		return damagedBooksCount;
	}

	public void setDamagedBooksCount(int damagedBooksCount) {
		this.damagedBooksCount = damagedBooksCount;
	}

	public int getTotalOrders() {
		return totalOrders;
	}

	public void setTotalOrders(int totalOrders) {
		this.totalOrders = totalOrders;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	
	
	public boolean isPayment() {
		return isPayment;
	}

	public void setPayment(boolean isPayment) {
		this.isPayment = isPayment;
	}

	@Override
	public String toString() {
		return "StudentEntity [studentID=" + studentID + ", studentCode=" + studentCode + ", avatar=" + avatar
				+ ", fullName=" + fullName + ", dateOfBirth=" + dateOfBirth + ", gender=" + gender + ", email=" + email
				+ ", phoneNumber=" + phoneNumber + ", address=" + address + ", enrollmentYear=" + enrollmentYear
				+ ", schoolName=" + schoolName + ", userID=" + userID + ", totalBooksRented=" + totalBooksRented
				+ ", lateReturnsCount=" + lateReturnsCount + ", damagedBooksCount=" + damagedBooksCount
				+ ", totalOrders=" + totalOrders + ", isDeleted=" + isDeleted + "]";
	}
}
