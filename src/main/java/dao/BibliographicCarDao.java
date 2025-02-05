package dao;

import service.ConnectDB;

public class BibliographicCarDao {

	// Lấy tổng số Authors đang có
	public int getTotalAuthors() {
		var totalAuthors = 0;
		var sql = "SELECT COUNT(*) AS TotalAuthors FROM Authors WHERE IsDeleted = 0";
		try (var con = ConnectDB.getCon(); var stmt = con.prepareStatement(sql); var rs = stmt.executeQuery()) {
			if (rs.next()) {
				totalAuthors = rs.getInt("TotalAuthors");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return totalAuthors;
	}

	// Lấy phần trăm Authors có sách
	public float getPercentageAuthorsWithBooks() {
		var percentageAuthorsWithBooks = 0F;
		var sql = """
				    SELECT
				        (CAST((SELECT COUNT(DISTINCT Authors.AuthorID)
				               FROM Authors
				               INNER JOIN Books ON Authors.AuthorID = Books.AuthorID
				               WHERE Authors.IsDeleted = 0 AND Books.IsDeleted = 0) AS FLOAT)
				        /
				        CAST((SELECT COUNT(*) FROM Authors WHERE IsDeleted = 0) AS FLOAT)) * 100 AS PercentageAuthorsWithBooks
				""";
		try (var con = ConnectDB.getCon(); var stmt = con.prepareStatement(sql); var rs = stmt.executeQuery()) {
			if (rs.next()) {
				percentageAuthorsWithBooks = rs.getFloat("PercentageAuthorsWithBooks");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return percentageAuthorsWithBooks;
	}

	// Lấy tổng số Publishers đang có
	public int getTotalPublishersWithBooks() {
		var totalPublishersWithBooks = 0;
		var sql = """
				SELECT COUNT(*) AS TotalPublishers
				FROM Publishers
				WHERE IsDeleted = 0;
				""";
		try (var con = ConnectDB.getCon(); var stmt = con.prepareStatement(sql); var rs = stmt.executeQuery()) {
			if (rs.next()) {
				// Sử dụng đúng tên cột được định nghĩa trong SQL
				totalPublishersWithBooks = rs.getInt("TotalPublishers");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return totalPublishersWithBooks;
	}


	// Lấy phần trăm Publishers có sách
	public float getPercentagePublishersWithBooks() {
		var percentagePublishersWithBooks = 0F;
		var sql = """
				    SELECT
				        (CAST((SELECT COUNT(DISTINCT Publishers.PublisherID)
				               FROM Publishers
				               INNER JOIN Books ON Publishers.PublisherID = Books.PublisherID
				               WHERE Publishers.IsDeleted = 0 AND Books.IsDeleted = 0) AS FLOAT)
				        /
				        CAST((SELECT COUNT(*) FROM Publishers WHERE IsDeleted = 0) AS FLOAT)) * 100 AS PercentagePublishersWithBooks
				""";
		try (var con = ConnectDB.getCon(); var stmt = con.prepareStatement(sql); var rs = stmt.executeQuery()) {
			if (rs.next()) {
				percentagePublishersWithBooks = rs.getFloat("PercentagePublishersWithBooks");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return percentagePublishersWithBooks;
	}

	// Lấy tổng số Categories đang có
	public int getTotalCategoriesWithBooks() {
		var totalCategoriesWithBooks = 0;
		var sql = """
				SELECT COUNT(*) AS TotalCategories
				FROM Categories
				WHERE IsDeleted = 0;
				""";
		try (var con = ConnectDB.getCon(); var stmt = con.prepareStatement(sql); var rs = stmt.executeQuery()) {
			if (rs.next()) {
				// Sử dụng đúng tên cột được định nghĩa trong SQL
				totalCategoriesWithBooks = rs.getInt("TotalCategories");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return totalCategoriesWithBooks;
	}


	// Lấy phần trăm Categories có sách
	public float getPercentageCategoriesWithBooks() {
		var percentageCategoriesWithBooks = 0F;
		var sql = """
				    SELECT
				        (CAST((SELECT COUNT(DISTINCT Categories.CategoryID)
				               FROM Categories
				               INNER JOIN BookCategories ON Categories.CategoryID = BookCategories.CategoryID
				               INNER JOIN Books ON BookCategories.BookID = Books.BookID
				               WHERE Categories.IsDeleted = 0 AND Books.IsDeleted = 0) AS FLOAT)
				        /
				        CAST((SELECT COUNT(*) FROM Categories WHERE IsDeleted = 0) AS FLOAT)) * 100 AS PercentageCategoriesWithBooks
				""";
		try (var con = ConnectDB.getCon(); var stmt = con.prepareStatement(sql); var rs = stmt.executeQuery()) {
			if (rs.next()) {
				percentageCategoriesWithBooks = rs.getFloat("PercentageCategoriesWithBooks");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return percentageCategoriesWithBooks;
	}
}
