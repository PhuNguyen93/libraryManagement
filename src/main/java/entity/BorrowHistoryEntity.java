package entity;

import java.time.LocalDateTime;

public class BorrowHistoryEntity {
	private int historyID;
	private int recordID;
	private String action; // Borrowed, Returned, Overdue
	private LocalDateTime actionDate;
	private boolean isDeleted;

	// Constructor không tham số
	public BorrowHistoryEntity() {
	}

	// Constructor đầy đủ tham số
	public BorrowHistoryEntity(int historyID, int recordID, String action, LocalDateTime actionDate,
			boolean isDeleted) {
		this.historyID = historyID;
		this.recordID = recordID;
		this.action = action;
		this.actionDate = actionDate;
		this.isDeleted = isDeleted;
	}

	// Getters và Setters
	public int getHistoryID() {
		return historyID;
	}

	public void setHistoryID(int historyID) {
		this.historyID = historyID;
	}

	public int getRecordID() {
		return recordID;
	}

	public void setRecordID(int recordID) {
		this.recordID = recordID;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public LocalDateTime getActionDate() {
		return actionDate;
	}

	public void setActionDate(LocalDateTime actionDate) {
		this.actionDate = actionDate;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	@Override
	public String toString() {
		return "BorrowHistoryEntity{" + "historyID=" + historyID + ", recordID=" + recordID + ", action='" + action
				+ '\'' + ", actionDate=" + actionDate + ", isDeleted=" + isDeleted + '}';
	}
}
