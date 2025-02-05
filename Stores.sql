
-- Bảng quản lý sinh viên (Students)

CREATE OR ALTER PROCEDURE deleteStu
@StudentID INT
AS
BEGIN
    IF EXISTS (SELECT 1 FROM Students WHERE StudentID = @StudentID)
    BEGIN
        DELETE FROM Students WHERE StudentID = @StudentID;
        PRINT 'Xóa thành công';
    END
    ELSE
    BEGIN
        PRINT 'StudentID không tồn tại';
    END
END;
GO

CREATE OR ALTER PROCEDURE updateStudent
    @StudentID INT,
    @FullName NVARCHAR(100),
    @DateOfBirth DATE,
    @Gender NVARCHAR(10),
    @Email NVARCHAR(100),
    @PhoneNumber NVARCHAR(15),
    @Address NVARCHAR(255),
    @Avatar NVARCHAR(255),
    @EnrollmentYear INT,
    @SchoolName NVARCHAR(255),
    @UserID INT,
    @TotalBooksRented INT,
    @LateReturnsCount INT,
    @DamagedBooksCount INT,
    @TotalOrders INT,
    @StudentCode NVARCHAR(50)
AS
BEGIN
    UPDATE Students
    SET 
        FullName = @FullName,
        DateOfBirth = @DateOfBirth,
        Gender = @Gender,
        Email = @Email,
        PhoneNumber = @PhoneNumber,
        Address = @Address,
        Avatar = @Avatar,
        EnrollmentYear = @EnrollmentYear,
        SchoolName = @SchoolName,
        UserID = @UserID,
        TotalBooksRented = @TotalBooksRented,
        LateReturnsCount = @LateReturnsCount,
        DamagedBooksCount = @DamagedBooksCount,
        TotalOrders = @TotalOrders,
        StudentCode = @StudentCode,
        UpdatedAt = GETDATE()
    WHERE StudentID = @StudentID;
END;

-- Bảng tác giả (Authors)

CREATE PROCEDURE UpdateAuthor
    @AuthorID INT,
    @FullName NVARCHAR(100),
    @DateOfBirth DATE,
    @Nationality NVARCHAR(50),
    @UpdatedBy INT
AS
BEGIN
    UPDATE Authors
    SET 
        FullName = @FullName,
        DateOfBirth = @DateOfBirth,
        Nationality = @Nationality,
        UpdatedAt = GETDATE(),
        UpdatedBy = @UpdatedBy
    WHERE 
        AuthorID = @AuthorID AND IsDeleted = 0;
END;
CREATE PROCEDURE DeleteAuthor
    @AuthorID INT,
    @UpdatedBy INT
AS
BEGIN
    UPDATE Authors
    SET 
        IsDeleted = 1,
        UpdatedAt = GETDATE(),
        UpdatedBy = @UpdatedBy
    WHERE 
        AuthorID = @AuthorID;
END;

-- Bảng nhà xuất bản (Publishers)

CREATE PROCEDURE UpdatePublisher
    @PublisherID INT,
    @Name NVARCHAR(255),
    @Address NVARCHAR(255),
    @PhoneNumber NVARCHAR(15),
    @Email NVARCHAR(100),
    @UpdatedBy INT
AS
BEGIN
    UPDATE Publishers
    SET 
        [Name] = @Name,
        [Address] = @Address,
        PhoneNumber = @PhoneNumber,
        Email = @Email,
        UpdatedAt = GETDATE(),
        UpdatedBy = @UpdatedBy
    WHERE 
        PublisherID = @PublisherID AND IsDeleted = 0;
END;
CREATE PROCEDURE DeletePublisher
    @PublisherID INT,
    @UpdatedBy INT
AS
BEGIN
    UPDATE Publishers
    SET 
        IsDeleted = 1,
        UpdatedAt = GETDATE(),
        UpdatedBy = @UpdatedBy
    WHERE 
        PublisherID = @PublisherID;
END;

-- Bảng danh mục sách (Categories)

CREATE PROCEDURE UpdateCategory
    @CategoryID INT,
    @CategoryName NVARCHAR(100),
    @Description NVARCHAR(255),
    @UpdatedBy INT
AS
BEGIN
    UPDATE Categories
    SET 
        CategoryName = @CategoryName,
        [Description] = @Description,
        UpdatedAt = GETDATE(),
        UpdatedBy = @UpdatedBy
    WHERE 
        CategoryID = @CategoryID AND IsDeleted = 0;
END;
CREATE PROCEDURE DeleteCategory
    @CategoryID INT,
    @UpdatedBy INT
AS
BEGIN
    UPDATE Categories
    SET 
        IsDeleted = 1,
        UpdatedAt = GETDATE(),
        UpdatedBy = @UpdatedBy
    WHERE 
        CategoryID = @CategoryID;
END;

-- Bảng quản lý sách (Books)

CREATE PROCEDURE UpdateBook
    @BookID INT,
    @Title NVARCHAR(255),
    @AuthorID INT,
    @PublisherID INT,
    @ISBN NVARCHAR(20),
    @Category NVARCHAR(100),
    @Quantity INT,
    @StockQuantity INT,
    @Price DECIMAL(10, 2),
    @RentalPrice DECIMAL(10, 2),
    @Language NVARCHAR(50),
    @Image NVARCHAR(255),
    @UpdatedBy INT
AS
BEGIN
    UPDATE Books
    SET 
        Title = @Title,
        AuthorID = @AuthorID,
        PublisherID = @PublisherID,
        ISBN = @ISBN,
        Category = @Category,
        Quantity = @Quantity,
        StockQuantity = @StockQuantity,
        Price = @Price,
        RentalPrice = @RentalPrice,
        [Language] = @Language,
        [Image] = @Image,
        UpdatedAt = GETDATE(),
        UpdatedBy = @UpdatedBy
    WHERE 
        ISBN = @ISBN AND IsDeleted = 0;
END;

CREATE PROCEDURE DeleteBook
    @BookID INT,
    @UpdatedBy INT
AS
BEGIN
    UPDATE Books
    SET 
        IsDeleted = 1,
        UpdatedAt = GETDATE(),
        UpdatedBy = @UpdatedBy
    WHERE 
        BookID = @BookID;
END;

-- Bảng hồ sơ mượn trả (BorrowRecords)

CREATE PROCEDURE UpdateBorrowRecord
    @RecordID INT,
    @DueReturnDate DATETIME,
    @Status NVARCHAR(50),
    @FineAmount DECIMAL(10, 2),
    @UpdatedBy INT
AS
BEGIN
    UPDATE BorrowRecords
    SET 
        DueReturnDate = @DueReturnDate,
        [Status] = @Status,
        FineAmount = @FineAmount,
        UpdatedAt = GETDATE(),
        UpdatedBy = @UpdatedBy
    WHERE RecordID = @RecordID AND IsDeleted = 0;
END;

GO
CREATE PROCEDURE DeleteBorrowRecord
    @RecordID INT
AS
BEGIN
    UPDATE BorrowRecords
    SET 
        IsDeleted = 1,
        UpdatedAt = GETDATE()
    WHERE RecordID = @RecordID;
END;
GO

CREATE PROCEDURE InsertBorrowRecord
    @UserID INT,
    @BookID INT,
    @DueReturnDate DATETIME,
    @BorrowPrice DECIMAL(10, 2),
    @CreatedBy INT
AS
BEGIN
    INSERT INTO BorrowRecords (UserID, BookID, DueReturnDate, BorrowPrice, CreatedBy)
    VALUES (@UserID, @BookID, @DueReturnDate, @BorrowPrice, @CreatedBy);
END;
GO

CREATE PROCEDURE SelectAllBorrowRecords
AS
BEGIN
    SELECT 
        RecordID,
        UserID,
        BookID,
        BorrowDate,
        DueReturnDate,
        [Status],
        FineAmount,
        BorrowPrice,
        IsDeleted,
        CreatedAt,
        CreatedBy,
        UpdatedAt,
        UpdatedBy
    FROM BorrowRecords
    WHERE IsDeleted = 0
    ORDER BY BorrowDate DESC;
END;
GO

CREATE PROCEDURE SelectBorrowRecordByID
    @RecordID INT
AS
BEGIN
    SELECT 
        RecordID,
        UserID,
        BookID,
        BorrowDate,
        DueReturnDate,
        [Status],
        FineAmount,
        BorrowPrice,
        IsDeleted,
        CreatedAt,
        CreatedBy,
        UpdatedAt,
        UpdatedBy
    FROM BorrowRecords
    WHERE RecordID = @RecordID AND IsDeleted = 0;
END;
GO

CREATE PROCEDURE CheckBorrowStatus
    @RecordID INT
AS
BEGIN
    SELECT 
        [Status],
        FineAmount
    FROM BorrowRecords
    WHERE RecordID = @RecordID AND IsDeleted = 0;
END;
GO

CREATE PROCEDURE UpdateFineForOverdue
    @RecordID INT,
    @FineAmount DECIMAL(10, 2),
    @UpdatedBy INT
AS
BEGIN
    UPDATE BorrowRecords
    SET 
        FineAmount = @FineAmount,
        [Status] = 'Overdue',
        UpdatedAt = GETDATE(),
        UpdatedBy = @UpdatedBy
    WHERE RecordID = @RecordID AND [Status] = 'Borrowed' AND IsDeleted = 0;
END;
GO
/****** Object:  StoredProcedure [dbo].[sp_InsertPayment]    Script Date: 1/2/2025 12:17:52 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
ALTER PROCEDURE [dbo].[sp_InsertPayment]
    @UserID INT,
    @Amount DECIMAL(15, 2),
    @PaymentMethod NVARCHAR(50),
    @Description NVARCHAR(MAX),
    @AmountGiven DECIMAL(15, 2),
    @TotalOrderAmount DECIMAL(15, 2),
    @ChangeAmount DECIMAL(15, 2),
    @IsDeleted BIT,
	@StudentID INT
AS
BEGIN
    INSERT INTO Payments (UserID, Amount, PaymentDate, PaymentMethod, [Description], AmountGiven, TotalOrderAmount, ChangeAmount, IsDeleted, StudentID)
    VALUES (@UserID, @Amount, GETDATE(), @PaymentMethod, @Description, @AmountGiven, @TotalOrderAmount, @ChangeAmount, @IsDeleted, @StudentID);
END;
GO

CREATE PROCEDURE sp_UpdatePayment
    @PaymentID INT,
    @UserID INT,
    @Amount DECIMAL(15, 2),
    @PaymentMethod NVARCHAR(50),
    @Description NVARCHAR(MAX),
    @AmountGiven DECIMAL(15, 2),
    @TotalOrderAmount DECIMAL(15, 2),
    @ChangeAmount DECIMAL(15, 2),
    @IsDeleted BIT
AS
BEGIN
    UPDATE Payments
    SET 
        UserID = @UserID,
        Amount = @Amount,
        PaymentMethod = @PaymentMethod,
        [Description] = @Description,
        AmountGiven = @AmountGiven,
        TotalOrderAmount = @TotalOrderAmount,
        ChangeAmount = @ChangeAmount,
        IsDeleted = @IsDeleted,
        PaymentDate = GETDATE()
    WHERE PaymentID = @PaymentID;
END;
GO

CREATE PROCEDURE sp_DeletePayment
    @PaymentID INT
AS
BEGIN
    UPDATE Payments
    SET IsDeleted = 1
    WHERE PaymentID = @PaymentID;
END;
GO

CREATE PROCEDURE sp_SelectAllPayments
AS
BEGIN
    SELECT 
        PaymentID,
        UserID,
        Amount,
        PaymentDate,
        PaymentMethod,
        [Description],
        AmountGiven,
        TotalOrderAmount,
        ChangeAmount,
        IsDeleted
    FROM Payments
    WHERE IsDeleted = 0;
END;
GO

CREATE PROCEDURE sp_SelectPaymentByID
    @PaymentID INT
AS
BEGIN
    SELECT 
        PaymentID,
        UserID,
        Amount,
        PaymentDate,
        PaymentMethod,
        [Description],
        AmountGiven,
        TotalOrderAmount,
        ChangeAmount,
        IsDeleted
    FROM Payments
    WHERE PaymentID = @PaymentID AND IsDeleted = 0;
END;
GO

CREATE PROCEDURE sp_CountPayments
AS
BEGIN
    SELECT COUNT(*) AS TotalPayments
    FROM Payments
    WHERE IsDeleted = 0;
END;
GO

