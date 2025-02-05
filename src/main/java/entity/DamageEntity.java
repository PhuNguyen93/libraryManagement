package entity;

import java.math.BigDecimal;
import java.util.Date;

public class DamageEntity {
	private int damageID;
	private int bookID;
	private int reportedBy;
	private String reportedName;
	private Date damageDate;
	private String damageDescription;
	private String damageSeverity; // Low, Medium, High
	private String status; // Pending, Under Repair, Replaced, Discarded
	private BigDecimal repairCost;
	private boolean isDeleted;
	private Date createdAt;
	private int createdBy;
	private Date updatedAt;
	private int updatedBy;
	private String bookImage;
	private String bookName;

	// Getters and Setters
	public int getDamageID() {
		return damageID;
	}

	public void setDamageID(int damageID) {
		this.damageID = damageID;
	}

	public int getBookID() {
		return bookID;
	}

	public void setBookID(int bookID) {
		this.bookID = bookID;
	}

	public int getReportedBy() {
		return reportedBy;
	}

	public void setReportedBy(int reportedBy) {
		this.reportedBy = reportedBy;
	}

	public Date getDamageDate() {
		return damageDate;
	}

	public void setDamageDate(Date damageDate) {
		this.damageDate = damageDate;
	}

	public String getDamageDescription() {
		return damageDescription;
	}

	public void setDamageDescription(String damageDescription) {
		this.damageDescription = damageDescription;
	}

	public String getDamageSeverity() {
		return damageSeverity;
	}

	public void setDamageSeverity(String damageSeverity) {
		this.damageSeverity = damageSeverity;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BigDecimal getRepairCost() {
		return repairCost;
	}

	public void setRepairCost(BigDecimal repairCost) {
		this.repairCost = repairCost;
	}

	public boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
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

	
	public String getReportedName() {
		return reportedName;
	}

	public void setReportedName(String reportedName) {
		this.reportedName = reportedName;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	
	
	public String getBookImage() {
		return bookImage;
	}

	public void setBookImage(String bookImage) {
		this.bookImage = bookImage;
	}

	
	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	@Override
	public String toString() {
		return "DamageEntity{" + "damageID=" + damageID + ", bookID=" + bookID + ", reportedBy=" + reportedBy
				+ ", damageDate=" + damageDate + ", damageDescription='" + damageDescription + '\''
				+ ", damageSeverity='" + damageSeverity + '\'' + ", status='" + status + '\'' + ", repairCost="
				+ repairCost + ", isDeleted=" + isDeleted + ", createdAt=" + createdAt + ", createdBy=" + createdBy
				+ ", updatedAt=" + updatedAt + ", updatedBy=" + updatedBy + '}';
	}
}
