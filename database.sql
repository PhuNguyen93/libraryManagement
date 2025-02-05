USE master;

IF DB_ID('library') IS NOT NULL DROP DATABASE library;
CREATE DATABASE library;
GO

USE library;
GO

-- Bảng quản lý người dùng (Users)
CREATE TABLE Users (
    UserID INT PRIMARY KEY IDENTITY, -- Khóa chính, tự động tăng
    FullName NVARCHAR(100) NOT NULL, -- Họ và tên của người dùng
    Username NVARCHAR(50) UNIQUE NOT NULL, -- Tên đăng nhập, duy nhất
    [Password] NVARCHAR(255) NOT NULL, -- Mật khẩu
    Email NVARCHAR(100) UNIQUE, -- Email liên hệ
    PhoneNumber NVARCHAR(15) UNIQUE, -- Số điện thoại
    [Address] NVARCHAR(255), -- Địa chỉ liên hệ
    UserRole INT NOT NULL CHECK (UserRole IN (1, 2)), -- Vai trò (1: Nhân viên, 2: Admin)
    Avatar NVARCHAR(255), -- Đường dẫn ảnh đại diện
	IsActive BIT DEFAULT 0, -- Trạng thái hoạt động (0: chưa được duyệt, 1: đã được duyệt)
    IsDeleted BIT DEFAULT 0, -- Trạng thái xóa (0: không xóa, 1: đã xóa)
    CreatedAt DATETIME DEFAULT GETDATE(), -- Ngày tạo bản ghi
    CreatedBy INT, -- Người tạo bản ghi (liên kết với UserID)
    UpdatedAt DATETIME DEFAULT GETDATE(), -- Ngày cập nhật bản ghi
    UpdatedBy INT -- Người cập nhật bản ghi (liên kết với UserID)
);


-- Bảng quản lý sinh viên (Students)
CREATE TABLE Students (
    StudentID INT PRIMARY KEY IDENTITY, -- Khóa chính, tự động tăng
    FullName NVARCHAR(100) NOT NULL, -- Họ và tên sinh viên
    DateOfBirth DATE NOT NULL, -- Ngày sinh
    Gender NVARCHAR(10) CHECK (Gender IN ('Male', 'Female', 'Other')) NOT NULL, -- Giới tính
    Email NVARCHAR(100) UNIQUE, -- Email liên hệ
    PhoneNumber NVARCHAR(15) UNIQUE, -- Số điện thoại
    [Address] NVARCHAR(255), -- Địa chỉ liên hệ
    Avatar NVARCHAR(255), -- Đường dẫn ảnh đại diện
    GraduationYear INT CHECK (GraduationYear > YEAR(GETDATE())), -- Năm kết thúc (bắt buộc nhập, lớn hơn năm hiện tại)
    SchoolName NVARCHAR(255) NOT NULL, -- Tên trường học
    UserID INT FOREIGN KEY REFERENCES Users(UserID), -- Liên kết với bảng Users
    IsDeleted BIT DEFAULT 0, -- Trạng thái xóa (0: không xóa, 1: đã xóa)
    CreatedAt DATETIME DEFAULT GETDATE(), -- Ngày tạo bản ghi
    CreatedBy INT, -- Người tạo bản ghi (liên kết với UserID)
    UpdatedAt DATETIME DEFAULT GETDATE(), -- Ngày cập nhật bản ghi
    UpdatedBy INT -- Người cập nhật bản ghi (liên kết với UserID)
);

ALTER TABLE Students
ADD GraduationYear INT DEFAULT (YEAR(GETDATE())) CONSTRAINT CHK_GraduationYear CHECK (GraduationYear >= YEAR(GETDATE()));


-- Thêm các cột vào bảng Students
ALTER TABLE Students
ADD 
    TotalBooksRented INT DEFAULT 0,          -- Số lượng sách đã thuê
    LateReturnsCount INT DEFAULT 0,          -- Số lần trả sách trễ
    DamagedBooksCount INT DEFAULT 0,         -- Số lần sách hư hại
    TotalOrders INT DEFAULT 0;
                   -- Tổng số đơn
	ALTER TABLE Students
ADD StudentCode NVARCHAR(50);


-- Bảng tác giả (Authors)
CREATE TABLE Authors (
    AuthorID INT PRIMARY KEY IDENTITY, -- Khóa chính, tự động tăng
    FullName NVARCHAR(100) NOT NULL, -- Họ và tên tác giả
    DateOfBirth DATE, -- Ngày sinh tác giả
    Nationality NVARCHAR(50), -- Quốc tịch của tác giả
    IsDeleted BIT DEFAULT 0, -- Trạng thái xóa (0: không xóa, 1: đã xóa)
    CreatedAt DATETIME DEFAULT GETDATE(), -- Ngày tạo bản ghi
    CreatedBy INT, -- Người tạo bản ghi (liên kết với UserID)
    UpdatedAt DATETIME DEFAULT GETDATE(), -- Ngày cập nhật bản ghi
    UpdatedBy INT -- Người cập nhật bản ghi (liên kết với UserID)
);


-- Bảng nhà xuất bản (Publishers)
CREATE TABLE Publishers (
    PublisherID INT PRIMARY KEY IDENTITY, -- Khóa chính, tự động tăng
    [Name] NVARCHAR(255) NOT NULL UNIQUE, -- Tên nhà xuất bản
    [Address] NVARCHAR(255), -- Địa chỉ nhà xuất bản
    PhoneNumber NVARCHAR(15) UNIQUE, -- Số điện thoại liên hệ
    Email NVARCHAR(100) UNIQUE, -- Email liên hệ
    IsDeleted BIT DEFAULT 0, -- Trạng thái xóa (0: không xóa, 1: đã xóa)
    CreatedAt DATETIME DEFAULT GETDATE(), -- Ngày tạo bản ghi
    CreatedBy INT, -- Người tạo bản ghi (liên kết với UserID)
    UpdatedAt DATETIME DEFAULT GETDATE(), -- Ngày cập nhật bản ghi
    UpdatedBy INT -- Người cập nhật bản ghi (liên kết với UserID)
);

-- Bảng danh mục sách (Categories)
CREATE TABLE Categories (
    CategoryID INT PRIMARY KEY IDENTITY, -- Khóa chính, tự động tăng
    CategoryName NVARCHAR(100) NOT NULL UNIQUE, -- Tên danh mục sách
    [Description] NVARCHAR(255), -- Mô tả danh mục
    IsDeleted BIT DEFAULT 0, -- Trạng thái xóa (0: không xóa, 1: đã xóa)
    CreatedAt DATETIME DEFAULT GETDATE(), -- Ngày tạo bản ghi
    CreatedBy INT, -- Người tạo bản ghi (liên kết với UserID)
    UpdatedAt DATETIME DEFAULT GETDATE(), -- Ngày cập nhật bản ghi
    UpdatedBy INT -- Người cập nhật bản ghi (liên kết với UserID)
);

-- Bảng quản lý sách (Books)
CREATE TABLE Books (
    BookID INT PRIMARY KEY IDENTITY, -- Khóa chính, tự động tăng
    Title NVARCHAR(255) NOT NULL, -- Tiêu đề sách
    AuthorID INT FOREIGN KEY REFERENCES Authors(AuthorID), -- Liên kết với bảng Authors
    PublisherID INT FOREIGN KEY REFERENCES Publishers(PublisherID), -- Liên kết với bảng Publishers
    ISBN NVARCHAR(20) UNIQUE, -- Mã ISBN của sách
    Category NVARCHAR(100), -- Thể loại sách
    Quantity INT NOT NULL DEFAULT 0, -- Số lượng sách hiện có
    StockQuantity INT NOT NULL DEFAULT 0, -- Số lượng tồn kho 
    Price DECIMAL(10, 2) NOT NULL DEFAULT 0, -- Giá tiền sách
    RentalPrice DECIMAL(10, 2) NOT NULL DEFAULT 0, -- Giá thuê sách
    [Language] NVARCHAR(50) NOT NULL, -- Ngôn ngữ của sách
    [Image] NVARCHAR(255), -- Đường dẫn ảnh bìa sách
    IsDeleted BIT DEFAULT 0, -- Trạng thái xóa (0: không xóa, 1: đã xóa)
    CreatedAt DATETIME DEFAULT GETDATE(), -- Ngày tạo bản ghi
    CreatedBy INT, -- Người tạo bản ghi (liên kết với UserID)
    UpdatedAt DATETIME DEFAULT GETDATE(), -- Ngày cập nhật bản ghi
    UpdatedBy INT -- Người cập nhật bản ghi (liên kết với UserID)
);

ALTER TABLE Books
ADD DepositPercentage DECIMAL(5, 2) DEFAULT 0.00 NOT NULL; -- Phần trăm số tiền cọc sách

ALTER TABLE Books
ADD FineMultiplier DECIMAL(5, 2) DEFAULT 1.00 NOT NULL; -- Giá trị mặc định là 1.00

-- Thêm cột WarehouseStatus kiểu BIT (True/False)
ALTER TABLE Books
ADD StockIn BIT NOT NULL DEFAULT 0; -- Giá trị mặc định là FALSE

-- Bảng liên kết sách với danh mục (BookCategories)
CREATE TABLE BookCategories (
    BookID INT NOT NULL FOREIGN KEY REFERENCES Books(BookID), -- Liên kết với bảng Books
    CategoryID INT NOT NULL FOREIGN KEY REFERENCES Categories(CategoryID), -- Liên kết với bảng Categories
    PRIMARY KEY (BookID, CategoryID) -- Khóa chính là sự kết hợp của BookID và CategoryID
);

-- Bảng hồ sơ mượn trả (BorrowRecords) chi tiết đơn hàng
CREATE TABLE BorrowRecords (
    RecordID INT PRIMARY KEY IDENTITY, -- Khóa chính, tự động tăng
    UserID INT NOT NULL FOREIGN KEY REFERENCES Users(UserID), -- Liên kết với bảng Users
    BookID INT NOT NULL FOREIGN KEY REFERENCES Books(BookID), -- Liên kết với bảng Books
    BorrowDate DATETIME NOT NULL DEFAULT GETDATE(), -- Ngày mượn sách
    DueReturnDate DATETIME, -- Ngày phải trả hoặc ngày trả sách thực tế
    [Status] NVARCHAR(50) CHECK (Status IN ('Borrowed', 'Returned', 'Overdue')) DEFAULT 'Borrowed', -- Trạng thái (Đang mượn, Đã trả, Quá hạn)
    FineAmount DECIMAL(10, 2) DEFAULT 0, -- Tiền phạt nếu có
    BorrowPrice DECIMAL(10, 2) NOT NULL DEFAULT 0, -- Giá tiền mượn sách
    IsDeleted BIT DEFAULT 0, -- Trạng thái xóa (0: không xóa, 1: đã xóa)
    CreatedAt DATETIME DEFAULT GETDATE(), -- Ngày tạo bản ghi
    CreatedBy INT, -- Người tạo bản ghi (liên kết với UserID)
    UpdatedAt DATETIME DEFAULT GETDATE(), -- Ngày cập nhật bản ghi
    UpdatedBy INT -- Người cập nhật bản ghi (liên kết với UserID)
);
-- Thêm cột PaymentID vào bảng BorrowRecords và thiết lập khóa ngoại liên kết với bảng Payments
ALTER TABLE BorrowRecords
ADD PaymentID INT;

ALTER TABLE BorrowRecords
ADD CONSTRAINT FK_BorrowRecords_Payments
FOREIGN KEY (PaymentID) REFERENCES Payments(PaymentID);

ALTER TABLE BorrowRecords
ADD Quantity INT NOT NULL DEFAULT 1; -- Số lượng mặc định là 1

ALTER TABLE BorrowRecords
ADD NumberOfDays  INT NOT NULL DEFAULT 1; -- Số lượng mặc định là 1

-- Bảng quản lý sách hư hại (DamagedBooks)
CREATE TABLE DamagedBooks (
    DamageID INT PRIMARY KEY IDENTITY, -- Khóa chính, tự động tăng
    BookID INT NOT NULL FOREIGN KEY REFERENCES Books(BookID), -- Liên kết với bảng Books
    ReportedBy INT FOREIGN KEY REFERENCES Users(UserID), -- Người báo cáo (liên kết với Users)
    DamageDate DATETIME NOT NULL DEFAULT GETDATE(), -- Ngày phát hiện sách hư hại
    DamageDescription NVARCHAR(MAX), -- Mô tả chi tiết tình trạng hư hại
    DamageSeverity NVARCHAR(50) CHECK (DamageSeverity IN ('Low', 'Medium', 'High')), -- Mức độ hư hại
    [Status] NVARCHAR(50) CHECK (Status IN ('Pending', 'Under Repair', 'Replaced', 'Discarded')) DEFAULT 'Pending', -- Trạng thái xử lý
    RepairCost DECIMAL(10, 2) DEFAULT 0, -- Chi phí sửa chữa nếu có
    IsDeleted BIT DEFAULT 0, -- Trạng thái xóa (0: không xóa, 1: đã xóa)
    CreatedAt DATETIME DEFAULT GETDATE(), -- Ngày tạo bản ghi
    CreatedBy INT, -- Người tạo bản ghi (liên kết với UserID)
    UpdatedAt DATETIME DEFAULT GETDATE(), -- Ngày cập nhật bản ghi
    UpdatedBy INT -- Người cập nhật bản ghi (liên kết với UserID)
);


-- Bảng thống kê doanh thu (RevenueStatistics)
CREATE TABLE RevenueStatistics (
    RevenueID INT PRIMARY KEY IDENTITY, -- Khóa chính, tự động tăng
    [Date] DATE NOT NULL, -- Ngày thống kê
    TotalBooksSold INT DEFAULT 0, -- Tổng số lượng sách bán được
    TotalRevenue DECIMAL(15, 2) DEFAULT 0, -- Tổng doanh thu
    NetRevenue DECIMAL(15, 2) DEFAULT 0, -- Doanh thu ròng (được cập nhật khi cần)
    PaymentID INT FOREIGN KEY REFERENCES Payments(PaymentID), -- Liên kết với bảng Payments
    BookID INT FOREIGN KEY REFERENCES Books(BookID), -- Liên kết với bảng Books
    IsDeleted BIT DEFAULT 0, -- Trạng thái xóa (0: không xóa, 1: đã xóa)
    CreatedAt DATETIME DEFAULT GETDATE(), -- Ngày tạo bản ghi
    CreatedBy INT, -- Người tạo bản ghi (liên kết với UserID)
    UpdatedAt DATETIME DEFAULT GETDATE(), -- Ngày cập nhật bản ghi
    UpdatedBy INT -- Người cập nhật bản ghi (liên kết với UserID)
);

-- Bảng log lịch sử (AuditLogs)
CREATE TABLE AuditLogs (
    LogID INT PRIMARY KEY IDENTITY, -- Khóa chính, tự động tăng
    TableName NVARCHAR(50), -- Tên bảng được thao tác
    [Action] NVARCHAR(50), -- Hành động (INSERT, UPDATE, DELETE)
    RecordID INT, -- ID bản ghi bị thay đổi
    UserID INT FOREIGN KEY REFERENCES Users(UserID), -- Người thực hiện hành động
    UpdatedAt DATETIME DEFAULT GETDATE(), -- Ngày cập nhật bản ghi
    Details NVARCHAR(MAX) -- Chi tiết hành động
);

-- Bảng thanh toán (Payments) đơn hàng tổng
CREATE TABLE Payments (
    PaymentID INT PRIMARY KEY IDENTITY, -- Khóa chính, tự động tăng
    UserID INT FOREIGN KEY REFERENCES Users(UserID), -- Người thanh toán (liên kết với Users)
    Amount DECIMAL(15, 2) NOT NULL, -- Số tiền thanh toán
    PaymentDate DATETIME DEFAULT GETDATE(), -- Ngày thanh toán
    PaymentMethod NVARCHAR(50) CHECK (PaymentMethod IN ('Cash', 'Card', 'Online')), -- Phương thức thanh toán
    [Description] NVARCHAR(MAX), -- Mô tả chi tiết giao dịch
    AmountGiven DECIMAL(15, 2) NOT NULL DEFAULT 0, -- Số tiền khách đưa
    TotalOrderAmount DECIMAL(15, 2) NOT NULL DEFAULT 0, -- Tổng giá trị đơn hàng
    ChangeAmount DECIMAL(15, 2) NOT NULL DEFAULT 0, -- Số tiền thối lại
    IsDeleted BIT DEFAULT 0 -- Trạng thái xóa (0: không xóa, 1: đã xóa)
);

-- Thêm cột StudentID vào bảng Payments và thiết lập khóa ngoại liên kết với bảng Students
ALTER TABLE Payments
ADD StudentID INT;

ALTER TABLE Payments
ADD CONSTRAINT FK_Payments_Students
FOREIGN KEY (StudentID) REFERENCES Students(StudentID);

-- Bảng lịch sử mượn trả (BorrowHistory)
CREATE TABLE BorrowHistory (
    HistoryID INT PRIMARY KEY IDENTITY, -- Khóa chính, tự động tăng
    RecordID INT FOREIGN KEY REFERENCES BorrowRecords(RecordID), -- Liên kết với bảng BorrowRecords
    [Action] NVARCHAR(50) CHECK (Action IN ('Borrowed', 'Returned', 'Overdue')), -- Hành động (mượn, trả, quá hạn)
    ActionDate DATETIME DEFAULT GETDATE(), -- Ngày thực hiện hành động
    IsDeleted BIT DEFAULT 0 -- Trạng thái xóa (0: không xóa, 1: đã xóa)
);

-- Bảng đánh giá sách (BookReviews)
CREATE TABLE BookReviews (
    ReviewID INT PRIMARY KEY IDENTITY, -- Khóa chính, tự động tăng
    BookID INT FOREIGN KEY REFERENCES Books(BookID), -- Liên kết với bảng Books
    UserID INT FOREIGN KEY REFERENCES Users(UserID), -- Người đánh giá (liên kết với Users)
    Rating INT CHECK (Rating >= 1 AND Rating <= 5), -- Điểm đánh giá (1-5)
    ReviewText NVARCHAR(MAX), -- Nội dung đánh giá
    ReviewDate DATETIME DEFAULT GETDATE(), -- Ngày đánh giá
    IsDeleted BIT DEFAULT 0 -- Trạng thái xóa (0: không xóa, 1: đã xóa)
);


-- Bảng thông báo (Notifications)
CREATE TABLE Notifications (
    NotificationID INT PRIMARY KEY IDENTITY, -- Khóa chính, tự động tăng
    UserID INT FOREIGN KEY REFERENCES Users(UserID), -- Người nhận thông báo (liên kết với Users)
    [Message] NVARCHAR(MAX) NOT NULL, -- Nội dung thông báo
    SentDate DATETIME DEFAULT GETDATE(), -- Ngày gửi thông báo
    IsRead BIT DEFAULT 0, -- Trạng thái đã đọc (0: chưa đọc, 1: đã đọc)
    IsDeleted BIT DEFAULT 0 -- Trạng thái xóa (0: không xóa, 1: đã xóa)
);
-- Bảng lưu trữ mã duy nhất cho từng bản sao của sách.
CREATE TABLE BookCopies (
    CopyID INT PRIMARY KEY IDENTITY, -- Khóa chính, tự động tăng
    BookID INT NOT NULL FOREIGN KEY REFERENCES Books(BookID), -- Liên kết với bảng Books
    UniqueCode NVARCHAR(50) UNIQUE NOT NULL, -- Mã duy nhất của mỗi cuốn sách
    [Status] NVARCHAR(50) CHECK (Status IN ('Available', 'Borrowed', 'Damaged', 'Lost')) DEFAULT 'Available', -- Trạng thái của bản sao
    IsDeleted BIT DEFAULT 0, -- Trạng thái xóa (0: không xóa, 1: đã xóa)
    CreatedAt DATETIME DEFAULT GETDATE(), -- Ngày tạo bản ghi
    CreatedBy INT, -- Người tạo bản ghi (liên kết với UserID)
    UpdatedAt DATETIME DEFAULT GETDATE(), -- Ngày cập nhật bản ghi
    UpdatedBy INT -- Người cập nhật bản ghi (liên kết với UserID)
);
