package test;

import service.ConnectDB;

public class TestConnect {

	public static void main(String[] args) {
		try (var conn = ConnectDB.getCon();) {
			System.out.println("Connected");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
