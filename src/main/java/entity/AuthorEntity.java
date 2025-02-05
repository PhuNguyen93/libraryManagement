package entity;

import java.time.LocalDate;

public class AuthorEntity {
	private int idAuthorID;
	private String FullName;
	private LocalDate DateOfBirth;
	private String Nationality;
	private boolean IsDeleted;
	private boolean isSelected;

	public AuthorEntity() {
		super();
	}

	public AuthorEntity(int idAuthorID, String fullName, LocalDate dateOfBirth, String nationality, boolean isDeleted) {
		super();
		this.idAuthorID = idAuthorID;
		FullName = fullName;
		DateOfBirth = dateOfBirth;
		Nationality = nationality;
		IsDeleted = isDeleted;
	}

	public int getIdAuthorID() {
		return idAuthorID;
	}

	public void setIdAuthorID(int idAuthorID) {
		this.idAuthorID = idAuthorID;
	}

	public String getFullName() {
		return FullName;
	}

	public void setFullName(String fullName) {
		FullName = fullName;
	}

	public LocalDate getDateOfBirth() {
		return DateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		DateOfBirth = dateOfBirth;
	}

	public String getNationality() {
		return Nationality;
	}

	public void setNationality(String nationality) {
		Nationality = nationality;
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
		return "AuthorEntity [idAuthorID=" + idAuthorID + ", FullName=" + FullName + ", DateOfBirth=" + DateOfBirth
				+ ", Nationality=" + Nationality + ", IsDeleted=" + IsDeleted + "]";
	}

}