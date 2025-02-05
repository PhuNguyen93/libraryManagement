package entity;

public class PublisherEntity {
	private int PublisherID;
	private String Name;
	private String Address;
	private String PhoneNumber;
	private String Email;
	private boolean IsDeleted;
	private boolean isSelected;
	
	public PublisherEntity() {
		super();
	}

	public PublisherEntity(int publisherID, String name, String address, String phoneNumber, String email,
			boolean isDeleted) {
		super();
		PublisherID = publisherID;
		Name = name;
		Address = address;
		PhoneNumber = phoneNumber;
		Email = email;
		IsDeleted = isDeleted;
	}

	public int getPublisherID() {
		return PublisherID;
	}

	public void setPublisherID(int publisherID) {
		PublisherID = publisherID;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public String getPhoneNumber() {
		return PhoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		PhoneNumber = phoneNumber;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
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
		return "PublisherEntity [PublisherID=" + PublisherID + ", Name=" + Name + ", Address=" + Address
				+ ", PhoneNumber=" + PhoneNumber + ", Email=" + Email + ", IsDeleted=" + IsDeleted + "]";
	}


}
