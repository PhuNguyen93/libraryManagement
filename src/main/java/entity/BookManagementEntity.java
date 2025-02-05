package entity;

import java.math.BigDecimal;
import java.util.Date;

public class BookManagementEntity {
	private int bookID; // ID của sách
	private String title; // Tiêu đề sách
	private int authorID; // ID tác giả
	private int publisherID; // ID nhà xuất bản
	private String isbn; // Mã ISBN của sách
	private String category; // Thể loại sách
	private int quantity; // Số lượng sách hiện có
	private int stockQuantity; // Số lượng tồn kho
	private BigDecimal price; // Giá tiền sách
	private BigDecimal rentalPrice; // Giá thuê sách
	private String language; // Ngôn ngữ của sách
	private String image; // Đường dẫn ảnh bìa sách
	private boolean isDeleted; // Trạng thái xóa (true: đã xóa, false: chưa xóa)
	private Date createdAt; // Ngày tạo
	private int createdBy; // Người tạo
	private Date updatedAt; // Ngày cập nhật
	private int updatedBy; // Người cập nhật
	// Thuộc tính bổ sung
	private String authorName; // Tên tác giả
	private String publisherName; // Tên nhà xuất bản
	private boolean isSelected; // Thêm cột Select
	private BigDecimal depositPercentage; // Thêm thuộc tính DepositPercentage
	private BigDecimal fineMultiplier; // Bội số phạt
	private boolean stockIn; // Thêm trạng thái nhập xuất kho (true/false)
	private boolean isRental;


	public BookManagementEntity() {
		super();
	}


	public BookManagementEntity(int bookID, String title, int authorID, int publisherID, String isbn, String category,
			int quantity, int stockQuantity, BigDecimal price, BigDecimal rentalPrice, String language, String image,
			boolean isDeleted, Date createdAt, int createdBy, Date updatedAt, int updatedBy, String authorName,
			String publisherName, boolean isSelected, BigDecimal depositPercentage, BigDecimal fineMultiplier,
			boolean stockIn) {
		super();
		this.bookID = bookID;
		this.title = title;
		this.authorID = authorID;
		this.publisherID = publisherID;
		this.isbn = isbn;
		this.category = category;
		this.quantity = quantity;
		this.stockQuantity = stockQuantity;
		this.price = price;
		this.rentalPrice = rentalPrice;
		this.language = language;
		this.image = image;
		this.isDeleted = isDeleted;
		this.createdAt = createdAt;
		this.createdBy = createdBy;
		this.updatedAt = updatedAt;
		this.updatedBy = updatedBy;
		this.authorName = authorName;
		this.publisherName = publisherName;
		this.isSelected = isSelected;
		this.depositPercentage = depositPercentage;
		this.fineMultiplier = fineMultiplier;
		this.stockIn = stockIn;
	}



	// Getter và Setter cho isSelected
	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	// Getters và Setters
	public int getBookID() {
		return bookID;
	}

	public void setBookID(int bookID) {
		this.bookID = bookID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getAuthorID() {
		return authorID;
	}

	public void setAuthorID(int authorID) {
		this.authorID = authorID;
	}

	public int getPublisherID() {
		return publisherID;
	}

	public void setPublisherID(int publisherID) {
		this.publisherID = publisherID;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getStockQuantity() {
		return stockQuantity;
	}

	public void setStockQuantity(int stockQuantity) {
		this.stockQuantity = stockQuantity;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getRentalPrice() {
		return rentalPrice;
	}

	public void setRentalPrice(BigDecimal rentalPrice) {
		this.rentalPrice = rentalPrice;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
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

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public String getPublisherName() {
		return publisherName;
	}

	public void setPublisherName(String publisherName) {
		this.publisherName = publisherName;
	}

	public BigDecimal getDepositPercentage() {
		return depositPercentage;
	}

	public void setDepositPercentage(BigDecimal depositPercentage) {
		this.depositPercentage = depositPercentage;
	}

	public BigDecimal getFineMultiplier() {
		return fineMultiplier;
	}

	public void setFineMultiplier(BigDecimal fineMultiplier) {
		this.fineMultiplier = fineMultiplier;
	}

	public boolean isStockIn() {
		return stockIn;
	}

	public void setStockIn(boolean stockIn) {
		this.stockIn = stockIn;
	}

	
	
	public boolean isRental() {
		return isRental;
	}


	public void setRental(boolean isRetal) {
		this.isRental = isRetal;
	}


	@Override
	public String toString() {
		return "BookManagementEntity [bookID=" + bookID + ", title=" + title + ", authorID=" + authorID
				+ ", publisherID=" + publisherID + ", isbn=" + isbn + ", category=" + category + ", quantity="
				+ quantity + ", stockQuantity=" + stockQuantity + ", price=" + price + ", rentalPrice=" + rentalPrice
				+ ", language=" + language + ", image=" + image + ", isDeleted=" + isDeleted + ", createdAt="
				+ createdAt + ", createdBy=" + createdBy + ", updatedAt=" + updatedAt + ", updatedBy=" + updatedBy
				+ ", authorName=" + authorName + ", publisherName=" + publisherName + ", isSelected=" + isSelected
				+ ", depositPercentage=" + depositPercentage + ", fineMultiplier=" + fineMultiplier + ", stockIn="
				+ stockIn + "]";
	}


}
