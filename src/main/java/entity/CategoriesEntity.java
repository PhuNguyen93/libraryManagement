package entity;

public class CategoriesEntity {
	private int CategoryID;
	private String CategoryName;
	private String Description;
	private boolean IsDeleted;
	private boolean isSelected;

	public CategoriesEntity() {
		super();
	}

	public CategoriesEntity(int categoryID, String categoryName, String description, boolean isDeleted) {
		super();
		CategoryID = categoryID;
		CategoryName = categoryName;
		Description = description;
		IsDeleted = isDeleted;
	}

	public int getCategoryID() {
		return CategoryID;
	}

	public void setCategoryID(int categoryID) {
		CategoryID = categoryID;
	}

	public String getCategoryName() {
		return CategoryName;
	}

	public void setCategoryName(String categoryName) {
		CategoryName = categoryName;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public boolean isIsDeleted() {
		return IsDeleted;
	}

	public void setIsDeleted(boolean isDeleted) {
		IsDeleted = isDeleted;
	}

	
	
	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	@Override
	public String toString() {
		return "CategoriesEntity [CategoryID=" + CategoryID + ", CategoryName=" + CategoryName + ", Description="
				+ Description + ", IsDeleted=" + IsDeleted + "]";
	}

}
