package entity;

/**
 * Entity class for the BookCategories table
 */
public class BookCategoriesEntity {
	private int bookID; // Liên kết với BookID từ bảng Books
	private int categoryID; // Liên kết với CategoryID từ bảng Categories

	// Constructor không tham số
	public BookCategoriesEntity() {
	}

	// Constructor đầy đủ tham số
	public BookCategoriesEntity(int bookID, int categoryID) {
		this.bookID = bookID;
		this.categoryID = categoryID;
	}

	// Getter và Setter cho BookID
	public int getBookID() {
		return bookID;
	}

	public void setBookID(int bookID) {
		this.bookID = bookID;
	}

	// Getter và Setter cho CategoryID
	public int getCategoryID() {
		return categoryID;
	}

	public void setCategoryID(int categoryID) {
		this.categoryID = categoryID;
	}

	// Phương thức toString
	@Override
	public String toString() {
		return "BookCategoriesEntity{" + "bookID=" + bookID + ", categoryID=" + categoryID + '}';
	}
}
