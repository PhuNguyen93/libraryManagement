package entity;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class PaymentEntity {
    private int paymentID;
    private int userID;
    private double amount;
    private Date paymentDate;
    private String paymentMethod;
    private String description;
    private double amountGiven;
    private double totalOrderAmount;
    private double changeAmount;
    private boolean isDeleted;
	private int studentID;

	private String studentAvatar;
	private String studentCode;
	private String studentName;
	private double totalBooksBorrowed;
	private double totalBooksReturned;
	private double totalFineAmount;
	
	private List<BorrowRecordsEntity> orderDetails; // Chi tiết đơn hàng
	private String phoneNumber;             // Số điện thoại
	private String email;                   // Email

	
	public PaymentEntity() {
		super();
	}



	public PaymentEntity(int paymentID, int userID, double amount, Date paymentDate, String paymentMethod,
			String description, double amountGiven, double totalOrderAmount, double changeAmount, boolean isDeleted,
			int studentID) {
		super();
		this.paymentID = paymentID;
		this.userID = userID;
		this.amount = amount;
		this.paymentDate = paymentDate;
		this.paymentMethod = paymentMethod;
		this.description = description;
		this.amountGiven = amountGiven;
		this.totalOrderAmount = totalOrderAmount;
		this.changeAmount = changeAmount;
		this.isDeleted = isDeleted;
		this.studentID = studentID;
	}

	// Getters and Setters
	// Getter và Setter cho orderDetails
	public List<BorrowRecordsEntity> getOrderDetails() {
	    return orderDetails;
	}

	public void setOrderDetails(List<BorrowRecordsEntity> orderDetails) {
	    this.orderDetails = orderDetails;
	}

	// Getter và Setter cho phoneNumber
	public String getPhoneNumber() {
	    return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
	    this.phoneNumber = phoneNumber;
	}

	// Getter và Setter cho email
	public String getEmail() {
	    return email;
	}

	public void setEmail(String email) {
	    this.email = email;
	}

	public int getPaymentID() {
		return paymentID;
	}

	public void setPaymentID(int paymentID) {
        this.paymentID = paymentID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmountGiven() {
        return amountGiven;
    }

    public void setAmountGiven(double amountGiven) {
        this.amountGiven = amountGiven;
    }

    public double getTotalOrderAmount() {
        return totalOrderAmount;
    }

    public void setTotalOrderAmount(double totalOrderAmount) {
        this.totalOrderAmount = totalOrderAmount;
    }

    public double getChangeAmount() {
        return changeAmount;
    }

    public void setChangeAmount(double changeAmount) {
        this.changeAmount = changeAmount;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

	public int getStudentID() {
		return studentID;
	}

	public void setStudentID(int studentID) {
		this.studentID = studentID;
	}

	
	
	public String getStudentAvatar() {
		return studentAvatar;
	}


	

	public String getStudentCode() {
		return studentCode;
	}



	public void setStudentCode(String studentCode) {
		this.studentCode = studentCode;
	}



	public void setStudentAvatar(String studentAvatar) {
		this.studentAvatar = studentAvatar;
	}



	public String getStudentName() {
		return studentName;
	}



	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}



	public double getTotalBooksBorrowed() {
		return totalBooksBorrowed;
	}



	public void setTotalBooksBorrowed(double totalBooksBorrowed) {
		this.totalBooksBorrowed = totalBooksBorrowed;
	}



	public double getTotalBooksReturned() {
		return totalBooksReturned;
	}



	public void setTotalBooksReturned(double totalBooksReturned) {
		this.totalBooksReturned = totalBooksReturned;
	}



	public double getTotalFineAmount() {
		return totalFineAmount;
	}



	public void setTotalFineAmount(double totalFineAmount) {
		this.totalFineAmount = totalFineAmount;
	}



	@Override
	public String toString() {
		return "PaymentEntity [paymentID=" + paymentID + ", userID=" + userID + ", amount=" + amount + ", paymentDate="
				+ paymentDate + ", paymentMethod=" + paymentMethod + ", description=" + description + ", amountGiven="
				+ amountGiven + ", totalOrderAmount=" + totalOrderAmount + ", changeAmount=" + changeAmount
				+ ", isDeleted=" + isDeleted + ", studentID=" + studentID + "]";
	}

}