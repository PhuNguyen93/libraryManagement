package dao;

import service.ConnectDB;

public class OrderCardDao {

    // Tổng tiền sách cho thuê ngày hôm nay
    public float getTotalRentalRevenueToday() {
        var sql = """
            SELECT SUM(BorrowRecords.BorrowPrice * BorrowRecords.NumberOfDays) AS TotalRentalRevenueToday
            FROM BorrowRecords
            WHERE CONVERT(DATE, BorrowRecords.BorrowDate) = CONVERT(DATE, GETDATE())
              AND BorrowRecords.IsDeleted = 0
        """;
        try (var con = ConnectDB.getCon();
             var stmt = con.prepareStatement(sql);
             var rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getFloat("TotalRentalRevenueToday");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Phần trăm số tiền sách cho thuê ngày hôm nay so với tháng này
    public float getPercentageRentalRevenueToday() {
        var sql = """
            SELECT 
                (CAST(
                    (SELECT SUM(BorrowRecords.BorrowPrice * BorrowRecords.NumberOfDays)
                     FROM BorrowRecords
                     WHERE CONVERT(DATE, BorrowRecords.BorrowDate) = CONVERT(DATE, GETDATE())
                       AND BorrowRecords.IsDeleted = 0
                    ) AS FLOAT)
                /
                CAST(
                    (SELECT SUM(BorrowRecords.BorrowPrice * BorrowRecords.NumberOfDays)
                     FROM BorrowRecords
                     WHERE YEAR(BorrowRecords.BorrowDate) = YEAR(GETDATE())
                       AND MONTH(BorrowRecords.BorrowDate) = MONTH(GETDATE())
                       AND BorrowRecords.IsDeleted = 0
                    ) AS FLOAT)
                ) * 100 AS PercentageRentalRevenueToday
        """;
        try (var con = ConnectDB.getCon();
             var stmt = con.prepareStatement(sql);
             var rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getFloat("PercentageRentalRevenueToday");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Tổng tiền sách trả trong ngày hôm nay
    public float getTotalReturnRevenueToday() {
        var sql = """
            SELECT SUM(BorrowRecords.BorrowPrice * BorrowRecords.NumberOfDays) AS TotalReturnRevenueToday
            FROM BorrowRecords
            WHERE CONVERT(DATE, BorrowRecords.UpdatedAt) = CONVERT(DATE, GETDATE())
              AND BorrowRecords.[Status] in ('Returned','Overdue')
              AND BorrowRecords.IsDeleted = 0
        """;
        try (var con = ConnectDB.getCon();
             var stmt = con.prepareStatement(sql);
             var rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getFloat("TotalReturnRevenueToday");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Phần trăm số tiền sách trả trong ngày hôm nay so với tháng này
    public float getPercentageReturnRevenueToday() {
        var sql = """
            SELECT 
                (CAST(
                    (SELECT SUM(BorrowRecords.BorrowPrice * BorrowRecords.NumberOfDays)
                     FROM BorrowRecords
                     WHERE CONVERT(DATE, BorrowRecords.UpdatedAt) = CONVERT(DATE, GETDATE())
                       AND BorrowRecords.[Status] in ('Returned','Overdue')
                       AND BorrowRecords.IsDeleted = 0
                    ) AS FLOAT)
                /
                CAST(
                    (SELECT SUM(BorrowRecords.BorrowPrice * BorrowRecords.NumberOfDays)
                     FROM BorrowRecords
                     WHERE YEAR(BorrowRecords.UpdatedAt) = YEAR(GETDATE())
                       AND MONTH(BorrowRecords.UpdatedAt) = MONTH(GETDATE())
                       AND BorrowRecords.[Status] in ('Returned','Overdue')
                       AND BorrowRecords.IsDeleted = 0
                    ) AS FLOAT)
                ) * 100 AS PercentageReturnRevenueToday;
        """;
        try (var con = ConnectDB.getCon();
             var stmt = con.prepareStatement(sql);
             var rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getFloat("PercentageReturnRevenueToday");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Tổng tiền sách hư hại trong ngày hôm nay
    public float getTotalDamagedBookCostToday() {
        var sql = """
            SELECT SUM(BorrowRecords.FineAmount) AS  TotalDamagedBookCostToday
            FROM BorrowRecords
            WHERE CONVERT(DATE, BorrowRecords.BorrowDate) = CONVERT(DATE, GETDATE())
        	  AND BorrowRecords.Status = 'Returned' and BorrowRecords.FineAmount > 0
              AND BorrowRecords.IsDeleted = 0;
        """;
        try (var con = ConnectDB.getCon();
             var stmt = con.prepareStatement(sql);
             var rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getFloat("TotalDamagedBookCostToday");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Phần trăm số tiền sách hư hại trong ngày hôm nay so với tháng này
    public float getPercentageDamagedCostToday() {
        var sql = """
            SELECT 
                (CAST(
                    (SELECT SUM(BorrowRecords.FineAmount)
                     FROM BorrowRecords
                     WHERE CONVERT(DATE, BorrowRecords.UpdatedAt) = CONVERT(DATE, GETDATE())
                       AND BorrowRecords.Status = 'Returned' and BorrowRecords.FineAmount > 0
						AND BorrowRecords.IsDeleted = 0
                    ) AS FLOAT)
                /
                CAST(
                    (SELECT SUM(BorrowRecords.FineAmount)
                     FROM BorrowRecords
                     WHERE YEAR(BorrowRecords.UpdatedAt) = YEAR(GETDATE())
                       AND MONTH(BorrowRecords.UpdatedAt) = MONTH(GETDATE())
                       AND BorrowRecords.Status = 'Returned' and BorrowRecords.FineAmount > 0
						AND BorrowRecords.IsDeleted = 0
                    ) AS FLOAT)
                ) * 100 AS PercentageDamagedCostToday
        """;
        try (var con = ConnectDB.getCon();
             var stmt = con.prepareStatement(sql);
             var rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getFloat("PercentageDamagedCostToday");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
