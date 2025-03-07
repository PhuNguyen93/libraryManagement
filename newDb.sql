USE [master]
GO
/****** Object:  Database [library]    Script Date: 1/9/2025 5:10:40 PM ******/
DROP DATABASE [library];
GO

CREATE DATABASE [library]
GO

USE [library]
GO
/****** Object:  Table [dbo].[AuditLogs]    Script Date: 1/9/2025 5:10:40 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[AuditLogs](
	[LogID] [int] IDENTITY(1,1) NOT NULL,
	[TableName] [nvarchar](50) NULL,
	[Action] [nvarchar](50) NULL,
	[RecordID] [int] NULL,
	[UserID] [int] NULL,
	[ActionTime] [datetime] NULL,
	[Details] [nvarchar](max) NULL,
PRIMARY KEY CLUSTERED 
(
	[LogID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Authors]    Script Date: 1/9/2025 5:10:41 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Authors](
	[AuthorID] [int] IDENTITY(1,1) NOT NULL,
	[FullName] [nvarchar](100) NOT NULL,
	[DateOfBirth] [date] NULL,
	[Nationality] [nvarchar](50) NULL,
	[IsDeleted] [bit] NULL,
	[CreatedAt] [datetime] NULL,
	[CreatedBy] [int] NULL,
	[UpdatedAt] [datetime] NULL,
	[UpdatedBy] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[AuthorID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[BookCategories]    Script Date: 1/9/2025 5:10:41 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[BookCategories](
	[BookID] [int] NOT NULL,
	[CategoryID] [int] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[BookID] ASC,
	[CategoryID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[BookCopies]    Script Date: 1/9/2025 5:10:41 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[BookCopies](
	[CopyID] [int] IDENTITY(1,1) NOT NULL,
	[BookID] [int] NOT NULL,
	[UniqueCode] [nvarchar](50) NOT NULL,
	[Status] [nvarchar](50) NULL,
	[IsDeleted] [bit] NULL,
	[CreatedAt] [datetime] NULL,
	[CreatedBy] [int] NULL,
	[UpdatedAt] [datetime] NULL,
	[UpdatedBy] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[CopyID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[BookReviews]    Script Date: 1/9/2025 5:10:41 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[BookReviews](
	[ReviewID] [int] IDENTITY(1,1) NOT NULL,
	[BookID] [int] NULL,
	[UserID] [int] NULL,
	[Rating] [int] NULL,
	[ReviewText] [nvarchar](max) NULL,
	[ReviewDate] [datetime] NULL,
	[IsDeleted] [bit] NULL,
PRIMARY KEY CLUSTERED 
(
	[ReviewID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Books]    Script Date: 1/9/2025 5:10:41 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Books](
	[BookID] [int] IDENTITY(1,1) NOT NULL,
	[Title] [nvarchar](255) NOT NULL,
	[AuthorID] [int] NULL,
	[PublisherID] [int] NULL,
	[PublishedYear] [int] NULL,
	[ISBN] [nvarchar](20) NULL,
	[Category] [nvarchar](100) NULL,
	[Quantity] [int] NOT NULL,
	[StockQuantity] [int] NOT NULL,
	[Price] [decimal](10, 2) NOT NULL,
	[RentalPrice] [decimal](10, 2) NOT NULL,
	[Language] [nvarchar](50) NOT NULL,
	[Image] [nvarchar](255) NULL,
	[IsDeleted] [bit] NULL,
	[CreatedAt] [datetime] NULL,
	[CreatedBy] [int] NULL,
	[UpdatedAt] [datetime] NULL,
	[UpdatedBy] [int] NULL,
	[DepositPercentage] [decimal](5, 2) NOT NULL,
	[FineMultiplier] [decimal](5, 2) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[BookID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[BorrowHistory]    Script Date: 1/9/2025 5:10:41 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[BorrowHistory](
	[HistoryID] [int] IDENTITY(1,1) NOT NULL,
	[RecordID] [int] NULL,
	[Action] [nvarchar](50) NULL,
	[ActionDate] [datetime] NULL,
	[IsDeleted] [bit] NULL,
PRIMARY KEY CLUSTERED 
(
	[HistoryID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[BorrowRecords]    Script Date: 1/9/2025 5:10:41 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[BorrowRecords](
	[RecordID] [int] IDENTITY(1,1) NOT NULL,
	[UserID] [int] NOT NULL,
	[BookID] [int] NOT NULL,
	[BorrowDate] [datetime] NOT NULL,
	[DueReturnDate] [datetime] NULL,
	[Status] [nvarchar](50) NULL,
	[FineAmount] [decimal](10, 2) NULL,
	[BorrowPrice] [decimal](10, 2) NOT NULL,
	[IsDeleted] [bit] NULL,
	[CreatedAt] [datetime] NULL,
	[CreatedBy] [int] NULL,
	[UpdatedAt] [datetime] NULL,
	[UpdatedBy] [int] NULL,
	[Quantity] [int] NOT NULL,
	[NumberOfDays] [int] NOT NULL,
	[PaymentID] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[RecordID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Categories]    Script Date: 1/9/2025 5:10:41 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Categories](
	[CategoryID] [int] IDENTITY(1,1) NOT NULL,
	[CategoryName] [nvarchar](100) NOT NULL,
	[Description] [nvarchar](255) NULL,
	[IsDeleted] [bit] NULL,
	[CreatedAt] [datetime] NULL,
	[CreatedBy] [int] NULL,
	[UpdatedAt] [datetime] NULL,
	[UpdatedBy] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[CategoryID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[DamagedBooks]    Script Date: 1/9/2025 5:10:41 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[DamagedBooks](
	[DamageID] [int] IDENTITY(1,1) NOT NULL,
	[BookID] [int] NOT NULL,
	[ReportedBy] [int] NULL,
	[DamageDate] [datetime] NOT NULL,
	[DamageDescription] [nvarchar](max) NULL,
	[DamageSeverity] [nvarchar](50) NULL,
	[Status] [nvarchar](50) NULL,
	[RepairCost] [decimal](10, 2) NULL,
	[IsDeleted] [bit] NULL,
	[CreatedAt] [datetime] NULL,
	[CreatedBy] [int] NULL,
	[UpdatedAt] [datetime] NULL,
	[UpdatedBy] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[DamageID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Notifications]    Script Date: 1/9/2025 5:10:41 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Notifications](
	[NotificationID] [int] IDENTITY(1,1) NOT NULL,
	[UserID] [int] NULL,
	[Message] [nvarchar](max) NOT NULL,
	[SentDate] [datetime] NULL,
	[IsRead] [bit] NULL,
	[IsDeleted] [bit] NULL,
PRIMARY KEY CLUSTERED 
(
	[NotificationID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Payments]    Script Date: 1/9/2025 5:10:41 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Payments](
	[PaymentID] [int] IDENTITY(1,1) NOT NULL,
	[UserID] [int] NULL,
	[Amount] [decimal](15, 2) NOT NULL,
	[PaymentDate] [datetime] NULL,
	[PaymentMethod] [nvarchar](50) NULL,
	[Description] [nvarchar](max) NULL,
	[AmountGiven] [decimal](15, 2) NOT NULL,
	[TotalOrderAmount] [decimal](15, 2) NOT NULL,
	[ChangeAmount] [decimal](15, 2) NOT NULL,
	[IsDeleted] [bit] NULL,
	[StudentID] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[PaymentID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Publishers]    Script Date: 1/9/2025 5:10:41 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Publishers](
	[PublisherID] [int] IDENTITY(1,1) NOT NULL,
	[Name] [nvarchar](255) NOT NULL,
	[Address] [nvarchar](255) NULL,
	[PhoneNumber] [nvarchar](15) NULL,
	[Email] [nvarchar](100) NULL,
	[Website] [nvarchar](255) NULL,
	[IsDeleted] [bit] NULL,
	[CreatedAt] [datetime] NULL,
	[CreatedBy] [int] NULL,
	[UpdatedAt] [datetime] NULL,
	[UpdatedBy] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[PublisherID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[RevenueStatistics]    Script Date: 1/9/2025 5:10:41 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[RevenueStatistics](
	[RevenueID] [int] IDENTITY(1,1) NOT NULL,
	[Date] [date] NOT NULL,
	[TotalBooksSold] [int] NULL,
	[TotalRevenue] [decimal](15, 2) NULL,
	[NetRevenue] [decimal](15, 2) NULL,
	[PaymentID] [int] NULL,
	[BookID] [int] NULL,
	[IsDeleted] [bit] NULL,
	[CreatedAt] [datetime] NULL,
	[CreatedBy] [int] NULL,
	[UpdatedAt] [datetime] NULL,
	[UpdatedBy] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[RevenueID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Students]    Script Date: 1/9/2025 5:10:41 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Students](
	[StudentID] [int] IDENTITY(1,1) NOT NULL,
	[FullName] [nvarchar](100) NOT NULL,
	[DateOfBirth] [date] NOT NULL,
	[Gender] [nvarchar](10) NOT NULL,
	[Email] [nvarchar](100) NULL,
	[PhoneNumber] [nvarchar](15) NULL,
	[Address] [nvarchar](255) NULL,
	[Avatar] [nvarchar](255) NULL,
	[EnrollmentYear] [int] NULL,
	[SchoolName] [nvarchar](255) NOT NULL,
	[UserID] [int] NULL,
	[IsDeleted] [bit] NULL,
	[CreatedAt] [datetime] NULL,
	[CreatedBy] [int] NULL,
	[UpdatedAt] [datetime] NULL,
	[UpdatedBy] [int] NULL,
	[TotalBooksRented] [int] NULL,
	[LateReturnsCount] [int] NULL,
	[DamagedBooksCount] [int] NULL,
	[TotalOrders] [int] NULL,
	[StudentCode] [nvarchar](50) NULL,
	[GraduationYear] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[StudentID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Users]    Script Date: 1/9/2025 5:10:41 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Users](
	[UserID] [int] IDENTITY(1,1) NOT NULL,
	[FullName] [nvarchar](100) NOT NULL,
	[Username] [nvarchar](50) NOT NULL,
	[Password] [nvarchar](255) NOT NULL,
	[Email] [nvarchar](100) NULL,
	[PhoneNumber] [nvarchar](15) NULL,
	[Address] [nvarchar](255) NULL,
	[UserRole] [int] NOT NULL,
	[Avatar] [nvarchar](255) NULL,
	[IsDeleted] [bit] NULL,
	[CreatedAt] [datetime] NULL,
	[CreatedBy] [int] NULL,
	[UpdatedAt] [datetime] NULL,
	[UpdatedBy] [int] NULL,
	[IsActive] [bit] NULL,
PRIMARY KEY CLUSTERED 
(
	[UserID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
SET IDENTITY_INSERT [dbo].[Authors] ON 

INSERT [dbo].[Authors] ([AuthorID], [FullName], [DateOfBirth], [Nationality], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (1, N'Đào Duy Anh', CAST(N'1904-04-25' AS Date), N'Việt Nam', 0, CAST(N'2024-12-29T20:10:54.727' AS DateTime), 1, CAST(N'2025-01-07T09:59:35.953' AS DateTime), 1)
INSERT [dbo].[Authors] ([AuthorID], [FullName], [DateOfBirth], [Nationality], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (2, N'Bùi Mạnh Hùng', CAST(N'1975-02-15' AS Date), N'Việt Nam', 0, CAST(N'2024-12-29T20:10:54.727' AS DateTime), 1, CAST(N'2024-12-29T20:10:54.727' AS DateTime), NULL)
INSERT [dbo].[Authors] ([AuthorID], [FullName], [DateOfBirth], [Nationality], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (3, N'Vũ Dương Ninh', CAST(N'1945-08-30' AS Date), N'Việt Nam', 0, CAST(N'2024-12-29T20:10:54.727' AS DateTime), 1, CAST(N'2024-12-29T20:10:54.727' AS DateTime), NULL)
INSERT [dbo].[Authors] ([AuthorID], [FullName], [DateOfBirth], [Nationality], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (4, N'Nguyễn Thị Phương Hoa', CAST(N'1960-11-22' AS Date), N'Việt Nam', 0, CAST(N'2024-12-29T20:10:54.727' AS DateTime), 1, CAST(N'2024-12-29T20:10:54.727' AS DateTime), NULL)
INSERT [dbo].[Authors] ([AuthorID], [FullName], [DateOfBirth], [Nationality], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (5, N'Đinh Mạnh Tường', CAST(N'1980-09-10' AS Date), N'Việt Nam', 0, CAST(N'2024-12-29T20:10:54.727' AS DateTime), 1, CAST(N'2024-12-29T20:10:54.727' AS DateTime), NULL)
INSERT [dbo].[Authors] ([AuthorID], [FullName], [DateOfBirth], [Nationality], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (6, N'Đỗ Trung Tuấn', CAST(N'1955-03-18' AS Date), N'Việt Nam', 0, CAST(N'2024-12-29T20:10:54.727' AS DateTime), 1, CAST(N'2024-12-29T20:10:54.727' AS DateTime), NULL)
INSERT [dbo].[Authors] ([AuthorID], [FullName], [DateOfBirth], [Nationality], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (7, N'Huỳnh Quyết Thắng', CAST(N'1973-06-12' AS Date), N'Việt Nam', 0, CAST(N'2024-12-29T20:10:54.727' AS DateTime), 1, CAST(N'2024-12-29T20:10:54.727' AS DateTime), NULL)
INSERT [dbo].[Authors] ([AuthorID], [FullName], [DateOfBirth], [Nationality], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (8, N'Hồng Đức', CAST(N'1948-01-25' AS Date), N'Việt Nam', 0, CAST(N'2024-12-29T20:10:54.727' AS DateTime), 1, CAST(N'2024-12-29T20:10:54.727' AS DateTime), NULL)
INSERT [dbo].[Authors] ([AuthorID], [FullName], [DateOfBirth], [Nationality], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (9, N'Bùi Công Cường', CAST(N'1968-07-14' AS Date), N'Việt Nam', 0, CAST(N'2024-12-29T20:10:54.727' AS DateTime), 1, CAST(N'2024-12-29T20:10:54.727' AS DateTime), NULL)
INSERT [dbo].[Authors] ([AuthorID], [FullName], [DateOfBirth], [Nationality], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (10, N'Đoàn Văn Ban', CAST(N'1970-05-09' AS Date), N'Việt Nam', 0, CAST(N'2024-12-29T20:10:54.727' AS DateTime), 1, CAST(N'2024-12-29T20:10:54.727' AS DateTime), NULL)
INSERT [dbo].[Authors] ([AuthorID], [FullName], [DateOfBirth], [Nationality], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (11, N'Nguyễn Văn Ba', CAST(N'1962-10-02' AS Date), N'Việt Nam', 0, CAST(N'2024-12-29T20:10:54.727' AS DateTime), 1, CAST(N'2024-12-29T20:10:54.727' AS DateTime), NULL)
INSERT [dbo].[Authors] ([AuthorID], [FullName], [DateOfBirth], [Nationality], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (12, N'Đặng Thanh Hà', CAST(N'1943-12-12' AS Date), N'Việt Nam', 0, CAST(N'2024-12-29T20:10:54.727' AS DateTime), 1, CAST(N'2024-12-29T20:10:54.727' AS DateTime), NULL)
INSERT [dbo].[Authors] ([AuthorID], [FullName], [DateOfBirth], [Nationality], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (13, N'Trần Lộ', CAST(N'1965-03-15' AS Date), N'Việt Nam', 0, CAST(N'2024-12-29T20:10:54.727' AS DateTime), 1, CAST(N'2024-12-29T20:10:54.727' AS DateTime), NULL)
INSERT [dbo].[Authors] ([AuthorID], [FullName], [DateOfBirth], [Nationality], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (14, N'Nguyễn Xuân Nghĩa', CAST(N'1958-08-22' AS Date), N'Việt Nam', 0, CAST(N'2024-12-29T20:10:54.727' AS DateTime), 1, CAST(N'2024-12-29T20:10:54.727' AS DateTime), NULL)
INSERT [dbo].[Authors] ([AuthorID], [FullName], [DateOfBirth], [Nationality], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (15, N'Nguyễn Công Khanh', CAST(N'1950-04-10' AS Date), N'Việt Nam', 0, CAST(N'2024-12-29T20:10:54.727' AS DateTime), 1, CAST(N'2024-12-29T20:10:54.727' AS DateTime), NULL)
INSERT [dbo].[Authors] ([AuthorID], [FullName], [DateOfBirth], [Nationality], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (16, N'Trần Ngọc Thêm', CAST(N'1945-11-05' AS Date), N'Việt Nam', 0, CAST(N'2024-12-29T20:10:54.727' AS DateTime), 1, CAST(N'2024-12-29T20:10:54.727' AS DateTime), NULL)
INSERT [dbo].[Authors] ([AuthorID], [FullName], [DateOfBirth], [Nationality], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (17, N'Phan Kế Bính', CAST(N'1875-05-10' AS Date), N'Việt Nam', 0, CAST(N'2024-12-29T20:10:54.727' AS DateTime), 1, CAST(N'2024-12-29T20:10:54.727' AS DateTime), NULL)
INSERT [dbo].[Authors] ([AuthorID], [FullName], [DateOfBirth], [Nationality], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (18, N'Nguyễn Quang Ngọc', CAST(N'1952-02-20' AS Date), N'Việt Nam', 0, CAST(N'2024-12-29T20:10:54.727' AS DateTime), 1, CAST(N'2024-12-29T20:10:54.727' AS DateTime), NULL)
INSERT [dbo].[Authors] ([AuthorID], [FullName], [DateOfBirth], [Nationality], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (19, N'Mai Huy Bích', CAST(N'1960-09-16' AS Date), N'Việt Nam', 0, CAST(N'2024-12-29T20:10:54.727' AS DateTime), 1, CAST(N'2024-12-29T20:10:54.727' AS DateTime), NULL)
INSERT [dbo].[Authors] ([AuthorID], [FullName], [DateOfBirth], [Nationality], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (20, N'Author 1', CAST(N'1970-01-01' AS Date), N'USA', 1, CAST(N'2025-01-03T14:42:11.970' AS DateTime), 1, CAST(N'2025-01-03T14:42:11.970' AS DateTime), 1)
INSERT [dbo].[Authors] ([AuthorID], [FullName], [DateOfBirth], [Nationality], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (21, N'Author 2', CAST(N'1980-02-02' AS Date), N'UK', 1, CAST(N'2025-01-03T14:42:11.970' AS DateTime), 1, CAST(N'2025-01-03T14:42:11.970' AS DateTime), 1)
INSERT [dbo].[Authors] ([AuthorID], [FullName], [DateOfBirth], [Nationality], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (22, N'Author 3', CAST(N'1990-03-03' AS Date), N'Canada', 1, CAST(N'2025-01-03T14:42:11.970' AS DateTime), 1, CAST(N'2025-01-03T14:42:11.970' AS DateTime), 1)
INSERT [dbo].[Authors] ([AuthorID], [FullName], [DateOfBirth], [Nationality], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (23, N'Author 4', CAST(N'1985-04-04' AS Date), N'France', 1, CAST(N'2025-01-03T14:42:11.970' AS DateTime), 1, CAST(N'2025-01-03T14:42:11.970' AS DateTime), 1)
INSERT [dbo].[Authors] ([AuthorID], [FullName], [DateOfBirth], [Nationality], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (24, N'Author 5', CAST(N'1975-05-05' AS Date), N'Germany', 1, CAST(N'2025-01-03T14:42:11.970' AS DateTime), 1, CAST(N'2025-01-03T14:42:11.970' AS DateTime), 1)
INSERT [dbo].[Authors] ([AuthorID], [FullName], [DateOfBirth], [Nationality], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (25, N'Author 6', CAST(N'1995-06-06' AS Date), N'Japan', 1, CAST(N'2025-01-03T14:42:11.970' AS DateTime), 1, CAST(N'2025-01-03T14:42:11.970' AS DateTime), 1)
INSERT [dbo].[Authors] ([AuthorID], [FullName], [DateOfBirth], [Nationality], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (26, N'Author 7', CAST(N'1988-07-07' AS Date), N'India', 1, CAST(N'2025-01-03T14:42:11.970' AS DateTime), 1, CAST(N'2025-01-09T16:14:43.163' AS DateTime), 1)
INSERT [dbo].[Authors] ([AuthorID], [FullName], [DateOfBirth], [Nationality], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (27, N'Author 8', CAST(N'1992-08-08' AS Date), N'Australia', 1, CAST(N'2025-01-03T14:42:11.970' AS DateTime), 1, CAST(N'2025-01-03T14:42:11.970' AS DateTime), 1)
INSERT [dbo].[Authors] ([AuthorID], [FullName], [DateOfBirth], [Nationality], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (28, N'Author 9', CAST(N'1978-09-09' AS Date), N'Italy', 1, CAST(N'2025-01-03T14:42:11.970' AS DateTime), 1, CAST(N'2025-01-03T14:42:11.970' AS DateTime), 1)
INSERT [dbo].[Authors] ([AuthorID], [FullName], [DateOfBirth], [Nationality], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (29, N'Author 10', CAST(N'1983-10-10' AS Date), N'Spain', 1, CAST(N'2025-01-03T14:42:11.970' AS DateTime), 1, CAST(N'2025-01-09T15:58:52.230' AS DateTime), 1)
INSERT [dbo].[Authors] ([AuthorID], [FullName], [DateOfBirth], [Nationality], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (30, N'aaa aaa', CAST(N'1990-10-12' AS Date), N'English', 1, CAST(N'2025-01-06T16:12:13.710' AS DateTime), NULL, CAST(N'2025-01-07T10:03:09.010' AS DateTime), 1)
INSERT [dbo].[Authors] ([AuthorID], [FullName], [DateOfBirth], [Nationality], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (31, N'Cảnh aab', CAST(N'2009-01-01' AS Date), N'Việt Nam', 1, CAST(N'2025-01-07T09:44:11.437' AS DateTime), NULL, CAST(N'2025-01-09T16:18:59.200' AS DateTime), 1)
INSERT [dbo].[Authors] ([AuthorID], [FullName], [DateOfBirth], [Nationality], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (32, N'dddêr', CAST(N'2000-01-12' AS Date), N'anh', 1, CAST(N'2025-01-07T10:10:00.043' AS DateTime), NULL, CAST(N'2025-01-07T11:48:16.787' AS DateTime), 1)
INSERT [dbo].[Authors] ([AuthorID], [FullName], [DateOfBirth], [Nationality], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (34, N'Trương Ngọc Khắc Nguyên', CAST(N'1983-01-03' AS Date), N'Campuchia', 0, CAST(N'2025-01-09T15:14:22.503' AS DateTime), NULL, CAST(N'2025-01-09T16:19:37.387' AS DateTime), 1)
SET IDENTITY_INSERT [dbo].[Authors] OFF
GO
INSERT [dbo].[BookCategories] ([BookID], [CategoryID]) VALUES (1, 1)
INSERT [dbo].[BookCategories] ([BookID], [CategoryID]) VALUES (1, 2)
INSERT [dbo].[BookCategories] ([BookID], [CategoryID]) VALUES (2, 3)
INSERT [dbo].[BookCategories] ([BookID], [CategoryID]) VALUES (2, 6)
INSERT [dbo].[BookCategories] ([BookID], [CategoryID]) VALUES (3, 2)
INSERT [dbo].[BookCategories] ([BookID], [CategoryID]) VALUES (4, 2)
INSERT [dbo].[BookCategories] ([BookID], [CategoryID]) VALUES (4, 6)
INSERT [dbo].[BookCategories] ([BookID], [CategoryID]) VALUES (5, 3)
INSERT [dbo].[BookCategories] ([BookID], [CategoryID]) VALUES (6, 3)
INSERT [dbo].[BookCategories] ([BookID], [CategoryID]) VALUES (7, 3)
INSERT [dbo].[BookCategories] ([BookID], [CategoryID]) VALUES (8, 3)
INSERT [dbo].[BookCategories] ([BookID], [CategoryID]) VALUES (9, 3)
INSERT [dbo].[BookCategories] ([BookID], [CategoryID]) VALUES (9, 4)
INSERT [dbo].[BookCategories] ([BookID], [CategoryID]) VALUES (9, 7)
INSERT [dbo].[BookCategories] ([BookID], [CategoryID]) VALUES (10, 3)
INSERT [dbo].[BookCategories] ([BookID], [CategoryID]) VALUES (11, 3)
INSERT [dbo].[BookCategories] ([BookID], [CategoryID]) VALUES (12, 3)
INSERT [dbo].[BookCategories] ([BookID], [CategoryID]) VALUES (13, 5)
INSERT [dbo].[BookCategories] ([BookID], [CategoryID]) VALUES (14, 4)
INSERT [dbo].[BookCategories] ([BookID], [CategoryID]) VALUES (14, 6)
INSERT [dbo].[BookCategories] ([BookID], [CategoryID]) VALUES (15, 5)
INSERT [dbo].[BookCategories] ([BookID], [CategoryID]) VALUES (16, 1)
INSERT [dbo].[BookCategories] ([BookID], [CategoryID]) VALUES (16, 2)
INSERT [dbo].[BookCategories] ([BookID], [CategoryID]) VALUES (17, 3)
INSERT [dbo].[BookCategories] ([BookID], [CategoryID]) VALUES (18, 1)
INSERT [dbo].[BookCategories] ([BookID], [CategoryID]) VALUES (18, 2)
INSERT [dbo].[BookCategories] ([BookID], [CategoryID]) VALUES (18, 6)
INSERT [dbo].[BookCategories] ([BookID], [CategoryID]) VALUES (19, 1)
INSERT [dbo].[BookCategories] ([BookID], [CategoryID]) VALUES (19, 2)
INSERT [dbo].[BookCategories] ([BookID], [CategoryID]) VALUES (20, 6)
INSERT [dbo].[BookCategories] ([BookID], [CategoryID]) VALUES (21, 1)
INSERT [dbo].[BookCategories] ([BookID], [CategoryID]) VALUES (22, 1)
INSERT [dbo].[BookCategories] ([BookID], [CategoryID]) VALUES (22, 2)
INSERT [dbo].[BookCategories] ([BookID], [CategoryID]) VALUES (23, 2)
INSERT [dbo].[BookCategories] ([BookID], [CategoryID]) VALUES (23, 3)
GO
SET IDENTITY_INSERT [dbo].[Books] ON 

INSERT [dbo].[Books] ([BookID], [Title], [AuthorID], [PublisherID], [PublishedYear], [ISBN], [Category], [Quantity], [StockQuantity], [Price], [RentalPrice], [Language], [Image], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [DepositPercentage], [FineMultiplier]) VALUES (1, N'Văn hóa sử cương', 1, 1, NULL, N'978-604-0-00001-1', N'Văn hóa, Xã hội', 99, 104, CAST(50000.00 AS Decimal(10, 2)), CAST(15000.00 AS Decimal(10, 2)), N'Tiếng Việt', N'1735478778549.png', 0, CAST(N'2024-12-29T20:12:42.597' AS DateTime), 1, CAST(N'2025-01-07T15:48:28.267' AS DateTime), 0, CAST(80.00 AS Decimal(5, 2)), CAST(1.30 AS Decimal(5, 2)))
INSERT [dbo].[Books] ([BookID], [Title], [AuthorID], [PublisherID], [PublishedYear], [ISBN], [Category], [Quantity], [StockQuantity], [Price], [RentalPrice], [Language], [Image], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [DepositPercentage], [FineMultiplier]) VALUES (2, N'101 thắc mắc và giải đáp khi sử dụng chương trình microsoft project', 2, 2, NULL, N'978-604-0-00002-1', N'CNTT', 193, 205, CAST(60000.00 AS Decimal(10, 2)), CAST(20000.00 AS Decimal(10, 2)), N'Tiếng Việt', N'1735478767710.png', 0, CAST(N'2024-12-29T20:12:42.597' AS DateTime), 1, CAST(N'2025-01-07T15:48:14.533' AS DateTime), 0, CAST(80.00 AS Decimal(5, 2)), CAST(1.30 AS Decimal(5, 2)))
INSERT [dbo].[Books] ([BookID], [Title], [AuthorID], [PublisherID], [PublishedYear], [ISBN], [Category], [Quantity], [StockQuantity], [Price], [RentalPrice], [Language], [Image], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [DepositPercentage], [FineMultiplier]) VALUES (3, N'Viet Nam - ASEAN quan hệ đa phương và song phương', 3, 1, NULL, N'978-604-0-00003-1', N'Lịch sử, Văn hóa', 142, 162, CAST(55000.00 AS Decimal(10, 2)), CAST(18000.00 AS Decimal(10, 2)), N'Tiếng Việt', N'1735478751147.png', 0, CAST(N'2024-12-29T20:12:42.597' AS DateTime), 1, CAST(N'2025-01-07T15:48:00.980' AS DateTime), 0, CAST(80.00 AS Decimal(5, 2)), CAST(1.30 AS Decimal(5, 2)))
INSERT [dbo].[Books] ([BookID], [Title], [AuthorID], [PublisherID], [PublishedYear], [ISBN], [Category], [Quantity], [StockQuantity], [Price], [RentalPrice], [Language], [Image], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [DepositPercentage], [FineMultiplier]) VALUES (4, N'Cẩm nang gia đình Việt', 4, 4, NULL, N'978-604-0-00004-1', N'Xã hội', 119, 125, CAST(65000.00 AS Decimal(10, 2)), CAST(22000.00 AS Decimal(10, 2)), N'Tiếng Việt', N'1735478727288.png', 0, CAST(N'2024-12-29T20:12:42.597' AS DateTime), 1, CAST(N'2025-01-07T15:47:45.667' AS DateTime), 0, CAST(80.00 AS Decimal(5, 2)), CAST(1.30 AS Decimal(5, 2)))
INSERT [dbo].[Books] ([BookID], [Title], [AuthorID], [PublisherID], [PublishedYear], [ISBN], [Category], [Quantity], [StockQuantity], [Price], [RentalPrice], [Language], [Image], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [DepositPercentage], [FineMultiplier]) VALUES (5, N'Cấu trúc dữ liệu và thuật toán', 5, 2, NULL, N'978-604-0-00005-1', N'CNTT', 80, 88, CAST(75000.00 AS Decimal(10, 2)), CAST(30000.00 AS Decimal(10, 2)), N'Tiếng Việt', N'1735478713471.png', 0, CAST(N'2024-12-29T20:12:42.597' AS DateTime), 1, CAST(N'2025-01-07T15:47:26.873' AS DateTime), 0, CAST(80.00 AS Decimal(5, 2)), CAST(1.30 AS Decimal(5, 2)))
INSERT [dbo].[Books] ([BookID], [Title], [AuthorID], [PublisherID], [PublishedYear], [ISBN], [Category], [Quantity], [StockQuantity], [Price], [RentalPrice], [Language], [Image], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [DepositPercentage], [FineMultiplier]) VALUES (6, N'Cơ sở dữ liệu', 6, 2, NULL, N'978-604-0-00006-1', N'CNTT', 48, 65, CAST(85000.00 AS Decimal(10, 2)), CAST(35000.00 AS Decimal(10, 2)), N'Tiếng Việt', N'1735478695900.png', 0, CAST(N'2024-12-29T20:12:42.597' AS DateTime), 1, CAST(N'2025-01-07T15:47:18.627' AS DateTime), 0, CAST(80.00 AS Decimal(5, 2)), CAST(1.30 AS Decimal(5, 2)))
INSERT [dbo].[Books] ([BookID], [Title], [AuthorID], [PublisherID], [PublishedYear], [ISBN], [Category], [Quantity], [StockQuantity], [Price], [RentalPrice], [Language], [Image], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [DepositPercentage], [FineMultiplier]) VALUES (7, N'Điện toán đám mây', 7, 2, NULL, N'978-604-0-00007-1', N'CNTT', 89, 95, CAST(70000.00 AS Decimal(10, 2)), CAST(25000.00 AS Decimal(10, 2)), N'Tiếng Việt', N'1735478683672.png', 0, CAST(N'2024-12-29T20:12:42.597' AS DateTime), 1, CAST(N'2025-01-07T15:47:08.443' AS DateTime), 0, CAST(80.00 AS Decimal(5, 2)), CAST(1.30 AS Decimal(5, 2)))
INSERT [dbo].[Books] ([BookID], [Title], [AuthorID], [PublisherID], [PublishedYear], [ISBN], [Category], [Quantity], [StockQuantity], [Price], [RentalPrice], [Language], [Image], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [DepositPercentage], [FineMultiplier]) VALUES (8, N'Giáo trình kỹ thuật lập trình C', 8, 2, NULL, N'978-604-0-00008-1', N'CNTT', 100, 105, CAST(60000.00 AS Decimal(10, 2)), CAST(20000.00 AS Decimal(10, 2)), N'Tiếng Việt', N'1735478670031.png', 0, CAST(N'2024-12-29T20:12:42.597' AS DateTime), 1, CAST(N'2025-01-07T15:46:58.443' AS DateTime), 0, CAST(80.00 AS Decimal(5, 2)), CAST(1.50 AS Decimal(5, 2)))
INSERT [dbo].[Books] ([BookID], [Title], [AuthorID], [PublisherID], [PublishedYear], [ISBN], [Category], [Quantity], [StockQuantity], [Price], [RentalPrice], [Language], [Image], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [DepositPercentage], [FineMultiplier]) VALUES (9, N'Hệ mờ mạng nơ ron và ứng dụng', 9, 5, NULL, N'978-604-0-00009-1', N'CNTT', 68, 78, CAST(90000.00 AS Decimal(10, 2)), CAST(40000.00 AS Decimal(10, 2)), N'Tiếng Việt', N'1735478651916.png', 0, CAST(N'2024-12-29T20:12:42.597' AS DateTime), 1, CAST(N'2025-01-07T15:46:47.467' AS DateTime), 0, CAST(80.00 AS Decimal(5, 2)), CAST(1.50 AS Decimal(5, 2)))
INSERT [dbo].[Books] ([BookID], [Title], [AuthorID], [PublisherID], [PublishedYear], [ISBN], [Category], [Quantity], [StockQuantity], [Price], [RentalPrice], [Language], [Image], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [DepositPercentage], [FineMultiplier]) VALUES (10, N'Lập trình hướng đối tượng với Java', 10, 2, NULL, N'978-604-0-00010-1', N'CNTT', 107, 119, CAST(65000.00 AS Decimal(10, 2)), CAST(22000.00 AS Decimal(10, 2)), N'Tiếng Việt', N'1735478633159.png', 0, CAST(N'2024-12-29T20:12:42.597' AS DateTime), 1, CAST(N'2025-01-07T15:46:33.130' AS DateTime), 0, CAST(80.00 AS Decimal(5, 2)), CAST(1.50 AS Decimal(5, 2)))
INSERT [dbo].[Books] ([BookID], [Title], [AuthorID], [PublisherID], [PublishedYear], [ISBN], [Category], [Quantity], [StockQuantity], [Price], [RentalPrice], [Language], [Image], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [DepositPercentage], [FineMultiplier]) VALUES (11, N'Lý thuyết ngôn ngữ và tính toán', 11, 2, NULL, N'978-604-0-00011-1', N'CNTT', 130, 140, CAST(80000.00 AS Decimal(10, 2)), CAST(32000.00 AS Decimal(10, 2)), N'Tiếng Việt', N'1735478596646.png', 0, CAST(N'2024-12-29T20:12:42.597' AS DateTime), 1, CAST(N'2025-01-07T15:46:22.070' AS DateTime), 0, CAST(80.00 AS Decimal(5, 2)), CAST(1.50 AS Decimal(5, 2)))
INSERT [dbo].[Books] ([BookID], [Title], [AuthorID], [PublisherID], [PublishedYear], [ISBN], [Category], [Quantity], [StockQuantity], [Price], [RentalPrice], [Language], [Image], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [DepositPercentage], [FineMultiplier]) VALUES (12, N'Logic mờ và ứng dụng', 12, 5, NULL, N'978-604-0-00012-1', N'CNTT', 55, 78, CAST(85000.00 AS Decimal(10, 2)), CAST(35000.00 AS Decimal(10, 2)), N'Tiếng Việt', N'1735478581689.png', 0, CAST(N'2024-12-29T20:12:42.597' AS DateTime), 1, CAST(N'2025-01-07T15:46:11.363' AS DateTime), 0, CAST(80.00 AS Decimal(5, 2)), CAST(1.50 AS Decimal(5, 2)))
INSERT [dbo].[Books] ([BookID], [Title], [AuthorID], [PublisherID], [PublishedYear], [ISBN], [Category], [Quantity], [StockQuantity], [Price], [RentalPrice], [Language], [Image], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [DepositPercentage], [FineMultiplier]) VALUES (13, N'Nghệ thuật giải mã hành vi', 13, 5, NULL, N'978-604-0-00013-1', N'Tâm lý học', 82, 95, CAST(75000.00 AS Decimal(10, 2)), CAST(30000.00 AS Decimal(10, 2)), N'Tiếng Việt', N'1735478565541.png', 0, CAST(N'2024-12-29T20:12:42.597' AS DateTime), 1, CAST(N'2025-01-07T15:45:52.620' AS DateTime), 0, CAST(80.00 AS Decimal(5, 2)), CAST(1.50 AS Decimal(5, 2)))
INSERT [dbo].[Books] ([BookID], [Title], [AuthorID], [PublisherID], [PublishedYear], [ISBN], [Category], [Quantity], [StockQuantity], [Price], [RentalPrice], [Language], [Image], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [DepositPercentage], [FineMultiplier]) VALUES (14, N'Phương pháp và kỹ thuật trong nghiên cứu xã hội', 14, 5, NULL, N'978-604-0-00014-1', N'Khoa học, Xã hội', 64, 88, CAST(70000.00 AS Decimal(10, 2)), CAST(25000.00 AS Decimal(10, 2)), N'Tiếng Việt', N'1735478545713.png', 0, CAST(N'2024-12-29T20:12:42.597' AS DateTime), 1, CAST(N'2025-01-07T15:45:38.200' AS DateTime), 0, CAST(80.00 AS Decimal(5, 2)), CAST(1.50 AS Decimal(5, 2)))
INSERT [dbo].[Books] ([BookID], [Title], [AuthorID], [PublisherID], [PublishedYear], [ISBN], [Category], [Quantity], [StockQuantity], [Price], [RentalPrice], [Language], [Image], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [DepositPercentage], [FineMultiplier]) VALUES (15, N'Tâm lý trị liệu', 15, 5, NULL, N'978-604-0-00015-1', N'Tâm lý học', 0, 95, CAST(90000.00 AS Decimal(10, 2)), CAST(40000.00 AS Decimal(10, 2)), N'Tiếng Việt', N'1735478525522.png', 0, CAST(N'2024-12-29T20:12:42.597' AS DateTime), 1, CAST(N'2025-01-07T15:45:14.727' AS DateTime), 0, CAST(80.00 AS Decimal(5, 2)), CAST(1.50 AS Decimal(5, 2)))
INSERT [dbo].[Books] ([BookID], [Title], [AuthorID], [PublisherID], [PublishedYear], [ISBN], [Category], [Quantity], [StockQuantity], [Price], [RentalPrice], [Language], [Image], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [DepositPercentage], [FineMultiplier]) VALUES (16, N'Tìm về bản sắc văn hóa Việt Nam', 16, 1, NULL, N'978-604-0-00016-1', N'Lịch sử, Văn hóa', 0, 105, CAST(85000.00 AS Decimal(10, 2)), CAST(35000.00 AS Decimal(10, 2)), N'Tiếng Việt', N'1735478500931.png', 0, CAST(N'2024-12-29T20:12:42.597' AS DateTime), 1, CAST(N'2025-01-07T15:45:01.087' AS DateTime), 0, CAST(80.00 AS Decimal(5, 2)), CAST(1.50 AS Decimal(5, 2)))
INSERT [dbo].[Books] ([BookID], [Title], [AuthorID], [PublisherID], [PublishedYear], [ISBN], [Category], [Quantity], [StockQuantity], [Price], [RentalPrice], [Language], [Image], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [DepositPercentage], [FineMultiplier]) VALUES (17, N'Trí tuệ nhân tạo', 5, 2, NULL, N'978-604-0-00017-1', N'CNTT', 0, 95, CAST(95000.00 AS Decimal(10, 2)), CAST(45000.00 AS Decimal(10, 2)), N'Tiếng Việt', N'1735478485156.png', 0, CAST(N'2024-12-29T20:12:42.597' AS DateTime), 1, CAST(N'2025-01-07T15:44:45.027' AS DateTime), 0, CAST(80.00 AS Decimal(5, 2)), CAST(1.50 AS Decimal(5, 2)))
INSERT [dbo].[Books] ([BookID], [Title], [AuthorID], [PublisherID], [PublishedYear], [ISBN], [Category], [Quantity], [StockQuantity], [Price], [RentalPrice], [Language], [Image], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [DepositPercentage], [FineMultiplier]) VALUES (18, N'Việt Nam phong tục', 17, 3, NULL, N'978-604-0-00018-1', N'Lịch sử, Văn hóa', 0, 88, CAST(75000.00 AS Decimal(10, 2)), CAST(30000.00 AS Decimal(10, 2)), N'Tiếng Việt', N'1735478469856.png', 0, CAST(N'2024-12-29T20:12:42.597' AS DateTime), 1, CAST(N'2025-01-07T15:44:17.870' AS DateTime), 0, CAST(80.00 AS Decimal(5, 2)), CAST(15.00 AS Decimal(5, 2)))
INSERT [dbo].[Books] ([BookID], [Title], [AuthorID], [PublisherID], [PublishedYear], [ISBN], [Category], [Quantity], [StockQuantity], [Price], [RentalPrice], [Language], [Image], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [DepositPercentage], [FineMultiplier]) VALUES (19, N'Vùng đất Nam Bộ', 18, 1, NULL, N'978-604-0-00019-1', N'CNTT, Máy tính, Khoa học', 1, 80, CAST(70000.00 AS Decimal(10, 2)), CAST(25000.00 AS Decimal(10, 2)), N'Tiếng Việt', N'1735478422869.png', 0, CAST(N'2024-12-29T20:12:42.597' AS DateTime), 1, CAST(N'2025-01-07T15:43:28.847' AS DateTime), 0, CAST(80.00 AS Decimal(5, 2)), CAST(1.50 AS Decimal(5, 2)))
INSERT [dbo].[Books] ([BookID], [Title], [AuthorID], [PublisherID], [PublishedYear], [ISBN], [Category], [Quantity], [StockQuantity], [Price], [RentalPrice], [Language], [Image], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [DepositPercentage], [FineMultiplier]) VALUES (20, N'Xã hội học gia đình', 19, 4, NULL, N'978-604-0-00020-1', N'Văn hóa, Xã hội', 8, 6, CAST(60000.00 AS Decimal(10, 2)), CAST(20000.00 AS Decimal(10, 2)), N'Tiếng Việt', N'1735478246260.png', 0, CAST(N'2024-12-29T20:12:42.597' AS DateTime), 1, CAST(N'2025-01-06T23:32:53.990' AS DateTime), 0, CAST(80.00 AS Decimal(5, 2)), CAST(1.50 AS Decimal(5, 2)))
INSERT [dbo].[Books] ([BookID], [Title], [AuthorID], [PublisherID], [PublishedYear], [ISBN], [Category], [Quantity], [StockQuantity], [Price], [RentalPrice], [Language], [Image], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [DepositPercentage], [FineMultiplier]) VALUES (21, N'Test', 1, 1, NULL, N'123456', N'Lịch sử', 10, 10, CAST(100000.00 AS Decimal(10, 2)), CAST(10000.00 AS Decimal(10, 2)), N'Tiếng Việt', N'1736171212434.png', 1, CAST(N'2025-01-06T20:47:21.223' AS DateTime), 0, CAST(N'2025-01-06T23:36:25.217' AS DateTime), 1, CAST(80.00 AS Decimal(5, 2)), CAST(1.00 AS Decimal(5, 2)))
INSERT [dbo].[Books] ([BookID], [Title], [AuthorID], [PublisherID], [PublishedYear], [ISBN], [Category], [Quantity], [StockQuantity], [Price], [RentalPrice], [Language], [Image], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [DepositPercentage], [FineMultiplier]) VALUES (22, N'xa hoi', 1, 1, NULL, N'890-098-00', N'Lịch sử, Văn hóa', 20, 30, CAST(300000.00 AS Decimal(10, 2)), CAST(40000.00 AS Decimal(10, 2)), N'Tiếng Anh', N'1736181420750.png', 1, CAST(N'2025-01-06T23:38:37.000' AS DateTime), 0, CAST(N'2025-01-06T23:39:09.173' AS DateTime), 1, CAST(80.00 AS Decimal(5, 2)), CAST(1.50 AS Decimal(5, 2)))
INSERT [dbo].[Books] ([BookID], [Title], [AuthorID], [PublisherID], [PublishedYear], [ISBN], [Category], [Quantity], [StockQuantity], [Price], [RentalPrice], [Language], [Image], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [DepositPercentage], [FineMultiplier]) VALUES (23, N'Test Book', 1, 5, NULL, N'102133-1111-11110', N'Văn hóa, CNTT', 8, 11, CAST(2098442.00 AS Decimal(10, 2)), CAST(2098442.00 AS Decimal(10, 2)), N'Tiếng Việt', N'1736262340033.jpg', 1, CAST(N'2025-01-07T22:04:48.840' AS DateTime), 0, CAST(N'2025-01-08T21:23:19.000' AS DateTime), 1, CAST(80.00 AS Decimal(5, 2)), CAST(1.50 AS Decimal(5, 2)))
SET IDENTITY_INSERT [dbo].[Books] OFF
GO
SET IDENTITY_INSERT [dbo].[BorrowHistory] ON 

INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (8, 18, N'Borrowed', CAST(N'2025-01-05T14:32:08.283' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (9, 18, N'Borrowed', CAST(N'2025-01-05T14:32:08.333' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (10, 18, N'Borrowed', CAST(N'2025-01-05T14:32:08.377' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (11, 19, N'Borrowed', CAST(N'2025-01-05T14:37:39.170' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (12, 20, N'Borrowed', CAST(N'2025-01-05T14:37:39.220' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (13, 21, N'Borrowed', CAST(N'2025-01-05T14:58:59.217' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (14, 22, N'Borrowed', CAST(N'2025-01-05T14:58:59.297' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (15, 23, N'Borrowed', CAST(N'2025-01-05T14:58:59.343' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (16, 24, N'Borrowed', CAST(N'2025-01-05T15:05:48.270' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (17, 25, N'Borrowed', CAST(N'2025-01-05T15:05:48.360' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (18, 26, N'Borrowed', CAST(N'2025-01-05T15:07:07.193' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (19, 27, N'Borrowed', CAST(N'2025-01-05T15:09:49.503' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (20, 28, N'Borrowed', CAST(N'2025-01-05T15:09:49.617' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (21, 29, N'Borrowed', CAST(N'2025-01-05T15:09:49.703' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (22, 30, N'Borrowed', CAST(N'2025-01-05T18:07:29.257' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (23, 31, N'Borrowed', CAST(N'2025-01-05T18:07:29.310' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (24, 32, N'Borrowed', CAST(N'2025-01-05T18:07:29.383' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (25, 33, N'Borrowed', CAST(N'2025-01-05T18:07:29.457' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (26, 34, N'Borrowed', CAST(N'2025-01-05T18:07:29.523' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (27, 35, N'Borrowed', CAST(N'2025-01-05T18:09:46.280' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (28, 36, N'Borrowed', CAST(N'2025-01-05T18:09:46.333' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (29, 37, N'Borrowed', CAST(N'2025-01-05T18:09:46.380' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (30, 26, N'Returned', CAST(N'2025-01-05T19:29:07.543' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (31, 19, N'Returned', CAST(N'2025-01-05T19:36:15.983' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (32, 36, N'Returned', CAST(N'2025-01-05T19:42:48.617' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (33, 35, N'Returned', CAST(N'2025-01-05T19:43:06.910' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (34, 37, N'Returned', CAST(N'2025-01-05T19:43:13.740' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (35, 38, N'Borrowed', CAST(N'2025-01-05T21:08:09.020' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (36, 39, N'Borrowed', CAST(N'2025-01-05T21:08:09.073' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (37, 40, N'Borrowed', CAST(N'2025-01-05T21:08:09.167' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (38, 41, N'Borrowed', CAST(N'2025-01-05T21:28:58.290' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (39, 42, N'Borrowed', CAST(N'2025-01-05T21:29:27.767' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (40, 43, N'Borrowed', CAST(N'2025-01-05T21:36:42.477' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (41, 44, N'Borrowed', CAST(N'2025-01-05T21:36:42.593' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (42, 45, N'Borrowed', CAST(N'2025-01-05T21:36:42.710' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (43, 45, N'Returned', CAST(N'2025-01-05T21:38:43.170' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (44, 23, N'Returned', CAST(N'2025-01-05T21:53:22.323' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (45, 11, N'Returned', CAST(N'2025-01-05T21:55:02.877' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (46, 10, N'Overdue', CAST(N'2025-01-05T21:56:11.083' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (47, 4, N'Overdue', CAST(N'2025-01-05T22:03:57.517' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (48, 13, N'Overdue', CAST(N'2025-01-05T22:04:37.310' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (49, 5, N'Overdue', CAST(N'2025-01-05T22:26:55.160' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (50, 8, N'Returned', CAST(N'2025-01-05T22:27:56.837' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (51, 9, N'Overdue', CAST(N'2025-01-05T22:30:14.173' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (52, 3, N'Overdue', CAST(N'2025-01-05T22:33:10.953' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (53, 46, N'Borrowed', CAST(N'2025-01-05T23:18:27.930' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (54, 47, N'Borrowed', CAST(N'2025-01-05T23:18:28.010' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (55, 47, N'Returned', CAST(N'2025-01-05T23:19:09.910' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (56, 14, N'Overdue', CAST(N'2025-01-05T23:29:05.657' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (57, 46, N'Overdue', CAST(N'2025-01-06T14:02:26.507' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (58, 48, N'Borrowed', CAST(N'2025-01-06T23:35:52.647' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (59, 48, N'Returned', CAST(N'2025-01-07T19:56:44.850' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (60, 40, N'Returned', CAST(N'2025-01-07T21:26:56.883' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (61, 49, N'Borrowed', CAST(N'2025-01-07T21:34:12.660' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (62, 49, N'Returned', CAST(N'2025-01-07T21:36:06.510' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (63, 42, N'Returned', CAST(N'2025-01-07T21:56:14.137' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (64, 50, N'Borrowed', CAST(N'2025-01-07T22:01:59.870' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (65, 51, N'Borrowed', CAST(N'2025-01-07T22:07:28.637' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (66, 52, N'Borrowed', CAST(N'2025-01-07T22:08:16.740' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (67, 53, N'Borrowed', CAST(N'2025-01-07T22:19:43.057' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (68, 53, N'Returned', CAST(N'2025-01-07T22:20:46.770' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (69, 54, N'Borrowed', CAST(N'2025-01-08T17:42:58.717' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (70, 55, N'Borrowed', CAST(N'2025-01-08T17:42:58.763' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (71, 55, N'Returned', CAST(N'2025-01-08T17:43:54.987' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (72, 54, N'Returned', CAST(N'2025-01-08T17:44:22.090' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (73, 56, N'Borrowed', CAST(N'2025-01-09T01:05:10.767' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (74, 56, N'Returned', CAST(N'2025-01-09T01:05:57.053' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (75, 41, N'Returned', CAST(N'2025-01-09T01:08:38.797' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (76, 57, N'Borrowed', CAST(N'2025-01-09T01:22:35.957' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (77, 57, N'Returned', CAST(N'2025-01-09T01:23:07.310' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (78, 31, N'Overdue', CAST(N'2025-01-09T10:47:25.943' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (79, 30, N'Returned', CAST(N'2025-01-09T11:36:21.970' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (80, 29, N'Returned', CAST(N'2025-01-09T11:36:32.537' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (81, 28, N'Overdue', CAST(N'2025-01-09T11:38:47.500' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (82, 27, N'Overdue', CAST(N'2025-01-09T11:39:00.787' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (83, 58, N'Borrowed', CAST(N'2025-01-09T11:41:35.253' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (84, 59, N'Borrowed', CAST(N'2025-01-09T11:41:35.310' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (85, 60, N'Borrowed', CAST(N'2025-01-09T11:45:25.563' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (86, 61, N'Borrowed', CAST(N'2025-01-09T11:57:33.400' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (87, 62, N'Borrowed', CAST(N'2025-01-09T11:58:54.983' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (88, 62, N'Returned', CAST(N'2025-01-09T11:59:58.043' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (89, 61, N'Returned', CAST(N'2025-01-09T12:00:21.807' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (90, 21, N'Overdue', CAST(N'2025-01-09T12:00:53.633' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (91, 63, N'Borrowed', CAST(N'2025-01-09T12:04:53.393' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (92, 64, N'Borrowed', CAST(N'2025-01-09T12:12:42.490' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (93, 65, N'Borrowed', CAST(N'2025-01-09T12:18:58.857' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (94, 66, N'Borrowed', CAST(N'2025-01-09T12:21:19.657' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (95, 67, N'Borrowed', CAST(N'2025-01-09T12:26:08.613' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (96, 68, N'Borrowed', CAST(N'2025-01-09T12:30:23.433' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (97, 7, N'Overdue', CAST(N'2025-01-09T12:34:02.347' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (98, 68, N'Returned', CAST(N'2025-01-09T12:34:29.260' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (99, 69, N'Borrowed', CAST(N'2025-01-09T12:49:31.790' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (100, 69, N'Returned', CAST(N'2025-01-09T12:50:20.560' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (101, 69, N'Returned', CAST(N'2025-01-09T12:50:47.607' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (102, 22, N'Returned', CAST(N'2025-01-09T13:05:30.307' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (103, 6, N'Returned', CAST(N'2025-01-09T13:07:43.583' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (104, 15, N'Returned', CAST(N'2025-01-09T13:08:27.897' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (105, 70, N'Borrowed', CAST(N'2025-01-09T13:41:50.693' AS DateTime), 0)
INSERT [dbo].[BorrowHistory] ([HistoryID], [RecordID], [Action], [ActionDate], [IsDeleted]) VALUES (106, 71, N'Borrowed', CAST(N'2025-01-09T14:22:05.350' AS DateTime), 0)
GO
SET IDENTITY_INSERT [dbo].[BorrowHistory] OFF
GO
SET IDENTITY_INSERT [dbo].[BorrowRecords] ON 

INSERT [dbo].[BorrowRecords] ([RecordID], [UserID], [BookID], [BorrowDate], [DueReturnDate], [Status], [FineAmount], [BorrowPrice], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [Quantity], [NumberOfDays], [PaymentID]) VALUES (3, 4, 20, CAST(N'2025-01-02T00:00:00.000' AS DateTime), CAST(N'2025-01-03T00:00:00.000' AS DateTime), N'Overdue', CAST(120000.00 AS Decimal(10, 2)), CAST(80000.00 AS Decimal(10, 2)), 0, CAST(N'2025-01-02T18:17:17.820' AS DateTime), 4, CAST(N'2025-01-02T18:17:17.820' AS DateTime), 4, 4, 1, 37)
INSERT [dbo].[BorrowRecords] ([RecordID], [UserID], [BookID], [BorrowDate], [DueReturnDate], [Status], [FineAmount], [BorrowPrice], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [Quantity], [NumberOfDays], [PaymentID]) VALUES (4, 4, 19, CAST(N'2025-01-02T00:00:00.000' AS DateTime), CAST(N'2025-01-03T00:00:00.000' AS DateTime), N'Overdue', CAST(37500.00 AS Decimal(10, 2)), CAST(25000.00 AS Decimal(10, 2)), 0, CAST(N'2025-01-02T18:17:17.827' AS DateTime), 4, CAST(N'2025-01-02T18:17:17.827' AS DateTime), 4, 1, 1, 37)
INSERT [dbo].[BorrowRecords] ([RecordID], [UserID], [BookID], [BorrowDate], [DueReturnDate], [Status], [FineAmount], [BorrowPrice], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [Quantity], [NumberOfDays], [PaymentID]) VALUES (5, 4, 19, CAST(N'2025-01-02T00:00:00.000' AS DateTime), CAST(N'2025-01-03T00:00:00.000' AS DateTime), N'Overdue', CAST(37500.00 AS Decimal(10, 2)), CAST(25000.00 AS Decimal(10, 2)), 0, CAST(N'2025-01-02T18:25:07.293' AS DateTime), 4, CAST(N'2025-01-02T18:25:07.293' AS DateTime), 4, 1, 1, 39)
INSERT [dbo].[BorrowRecords] ([RecordID], [UserID], [BookID], [BorrowDate], [DueReturnDate], [Status], [FineAmount], [BorrowPrice], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [Quantity], [NumberOfDays], [PaymentID]) VALUES (6, 4, 20, CAST(N'2025-01-02T00:00:00.000' AS DateTime), CAST(N'2025-01-03T00:00:00.000' AS DateTime), N'Returned', CAST(60000.00 AS Decimal(10, 2)), CAST(20000.00 AS Decimal(10, 2)), 0, CAST(N'2025-01-02T18:25:12.327' AS DateTime), 4, CAST(N'2025-01-09T13:07:43.507' AS DateTime), 21, 1, 1, 39)
INSERT [dbo].[BorrowRecords] ([RecordID], [UserID], [BookID], [BorrowDate], [DueReturnDate], [Status], [FineAmount], [BorrowPrice], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [Quantity], [NumberOfDays], [PaymentID]) VALUES (7, 4, 19, CAST(N'2025-01-02T00:00:00.000' AS DateTime), CAST(N'2025-01-03T00:00:00.000' AS DateTime), N'Overdue', CAST(225000.00 AS Decimal(10, 2)), CAST(25000.00 AS Decimal(10, 2)), 0, CAST(N'2025-01-02T18:25:35.997' AS DateTime), 4, CAST(N'2025-01-02T18:25:35.997' AS DateTime), 4, 1, 1, 40)
INSERT [dbo].[BorrowRecords] ([RecordID], [UserID], [BookID], [BorrowDate], [DueReturnDate], [Status], [FineAmount], [BorrowPrice], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [Quantity], [NumberOfDays], [PaymentID]) VALUES (8, 4, 20, CAST(N'2025-01-02T00:00:00.000' AS DateTime), CAST(N'2025-01-03T00:00:00.000' AS DateTime), N'Returned', CAST(60000.00 AS Decimal(10, 2)), CAST(20000.00 AS Decimal(10, 2)), 0, CAST(N'2025-01-02T18:25:38.413' AS DateTime), 4, CAST(N'2025-01-02T18:25:38.413' AS DateTime), 4, 1, 1, 40)
INSERT [dbo].[BorrowRecords] ([RecordID], [UserID], [BookID], [BorrowDate], [DueReturnDate], [Status], [FineAmount], [BorrowPrice], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [Quantity], [NumberOfDays], [PaymentID]) VALUES (9, 4, 19, CAST(N'2025-01-02T00:00:00.000' AS DateTime), CAST(N'2025-01-03T00:00:00.000' AS DateTime), N'Overdue', CAST(37500.00 AS Decimal(10, 2)), CAST(25000.00 AS Decimal(10, 2)), 0, CAST(N'2025-01-02T18:28:44.213' AS DateTime), 4, CAST(N'2025-01-02T18:28:44.213' AS DateTime), 4, 1, 1, 41)
INSERT [dbo].[BorrowRecords] ([RecordID], [UserID], [BookID], [BorrowDate], [DueReturnDate], [Status], [FineAmount], [BorrowPrice], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [Quantity], [NumberOfDays], [PaymentID]) VALUES (10, 4, 20, CAST(N'2025-01-02T00:00:00.000' AS DateTime), CAST(N'2025-01-03T00:00:00.000' AS DateTime), N'Overdue', CAST(30000.00 AS Decimal(10, 2)), CAST(20000.00 AS Decimal(10, 2)), 0, CAST(N'2025-01-02T18:28:44.220' AS DateTime), 4, CAST(N'2025-01-02T18:28:44.220' AS DateTime), 4, 1, 1, 41)
INSERT [dbo].[BorrowRecords] ([RecordID], [UserID], [BookID], [BorrowDate], [DueReturnDate], [Status], [FineAmount], [BorrowPrice], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [Quantity], [NumberOfDays], [PaymentID]) VALUES (11, 4, 20, CAST(N'2025-01-02T00:00:00.000' AS DateTime), CAST(N'2025-01-03T00:00:00.000' AS DateTime), N'Returned', CAST(60000.00 AS Decimal(10, 2)), CAST(1200000.00 AS Decimal(10, 2)), 0, CAST(N'2025-01-02T19:42:14.937' AS DateTime), 4, CAST(N'2025-01-02T19:42:14.937' AS DateTime), 4, 60, 1, 42)
INSERT [dbo].[BorrowRecords] ([RecordID], [UserID], [BookID], [BorrowDate], [DueReturnDate], [Status], [FineAmount], [BorrowPrice], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [Quantity], [NumberOfDays], [PaymentID]) VALUES (12, 4, 19, CAST(N'2025-01-02T00:00:00.000' AS DateTime), CAST(N'2025-01-03T00:00:00.000' AS DateTime), N'Borrowed', CAST(0.00 AS Decimal(10, 2)), CAST(350000.00 AS Decimal(10, 2)), 0, CAST(N'2025-01-02T19:42:14.940' AS DateTime), 4, CAST(N'2025-01-02T19:42:14.940' AS DateTime), 4, 14, 1, 42)
INSERT [dbo].[BorrowRecords] ([RecordID], [UserID], [BookID], [BorrowDate], [DueReturnDate], [Status], [FineAmount], [BorrowPrice], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [Quantity], [NumberOfDays], [PaymentID]) VALUES (13, 4, 18, CAST(N'2025-01-02T00:00:00.000' AS DateTime), CAST(N'2025-01-03T00:00:00.000' AS DateTime), N'Overdue', CAST(45000.00 AS Decimal(10, 2)), CAST(30000.00 AS Decimal(10, 2)), 0, CAST(N'2025-01-02T19:42:14.940' AS DateTime), 4, CAST(N'2025-01-02T19:42:14.940' AS DateTime), 4, 1, 1, 42)
INSERT [dbo].[BorrowRecords] ([RecordID], [UserID], [BookID], [BorrowDate], [DueReturnDate], [Status], [FineAmount], [BorrowPrice], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [Quantity], [NumberOfDays], [PaymentID]) VALUES (14, 4, 11, CAST(N'2025-01-02T00:00:00.000' AS DateTime), CAST(N'2025-01-03T00:00:00.000' AS DateTime), N'Overdue', CAST(96000.00 AS Decimal(10, 2)), CAST(32000.00 AS Decimal(10, 2)), 0, CAST(N'2025-01-02T19:42:14.940' AS DateTime), 4, CAST(N'2025-01-02T19:42:14.940' AS DateTime), 4, 1, 1, 42)
INSERT [dbo].[BorrowRecords] ([RecordID], [UserID], [BookID], [BorrowDate], [DueReturnDate], [Status], [FineAmount], [BorrowPrice], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [Quantity], [NumberOfDays], [PaymentID]) VALUES (15, 4, 10, CAST(N'2025-01-02T00:00:00.000' AS DateTime), CAST(N'2025-01-03T00:00:00.000' AS DateTime), N'Returned', CAST(65000.00 AS Decimal(10, 2)), CAST(22000.00 AS Decimal(10, 2)), 0, CAST(N'2025-01-02T19:42:14.940' AS DateTime), 4, CAST(N'2025-01-09T13:08:27.820' AS DateTime), 21, 1, 1, 42)
INSERT [dbo].[BorrowRecords] ([RecordID], [UserID], [BookID], [BorrowDate], [DueReturnDate], [Status], [FineAmount], [BorrowPrice], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [Quantity], [NumberOfDays], [PaymentID]) VALUES (16, 4, 20, CAST(N'2025-01-02T00:00:00.000' AS DateTime), CAST(N'2025-01-03T00:00:00.000' AS DateTime), N'Borrowed', CAST(0.00 AS Decimal(10, 2)), CAST(280000.00 AS Decimal(10, 2)), 0, CAST(N'2025-01-02T19:45:38.997' AS DateTime), 4, CAST(N'2025-01-02T19:45:38.997' AS DateTime), 4, 14, 1, 43)
INSERT [dbo].[BorrowRecords] ([RecordID], [UserID], [BookID], [BorrowDate], [DueReturnDate], [Status], [FineAmount], [BorrowPrice], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [Quantity], [NumberOfDays], [PaymentID]) VALUES (17, 4, 20, CAST(N'2025-01-03T00:00:00.000' AS DateTime), CAST(N'2025-01-06T00:00:00.000' AS DateTime), N'Borrowed', CAST(0.00 AS Decimal(10, 2)), CAST(120000.00 AS Decimal(10, 2)), 0, CAST(N'2025-01-03T15:15:47.023' AS DateTime), 4, CAST(N'2025-01-03T15:15:47.023' AS DateTime), 4, 6, 3, 44)
INSERT [dbo].[BorrowRecords] ([RecordID], [UserID], [BookID], [BorrowDate], [DueReturnDate], [Status], [FineAmount], [BorrowPrice], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [Quantity], [NumberOfDays], [PaymentID]) VALUES (18, 4, 19, CAST(N'2025-01-03T00:00:00.000' AS DateTime), CAST(N'2025-01-04T00:00:00.000' AS DateTime), N'Borrowed', CAST(0.00 AS Decimal(10, 2)), CAST(125000.00 AS Decimal(10, 2)), 0, CAST(N'2025-01-03T15:15:47.023' AS DateTime), 4, CAST(N'2025-01-03T15:15:47.023' AS DateTime), 4, 5, 1, 44)
INSERT [dbo].[BorrowRecords] ([RecordID], [UserID], [BookID], [BorrowDate], [DueReturnDate], [Status], [FineAmount], [BorrowPrice], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [Quantity], [NumberOfDays], [PaymentID]) VALUES (19, 4, 13, CAST(N'2025-01-05T00:00:00.000' AS DateTime), CAST(N'2025-01-06T00:00:00.000' AS DateTime), N'Returned', CAST(0.00 AS Decimal(10, 2)), CAST(30000.00 AS Decimal(10, 2)), 0, CAST(N'2025-01-05T14:37:39.143' AS DateTime), 4, CAST(N'2025-01-05T14:37:39.143' AS DateTime), 4, 1, 1, 48)
INSERT [dbo].[BorrowRecords] ([RecordID], [UserID], [BookID], [BorrowDate], [DueReturnDate], [Status], [FineAmount], [BorrowPrice], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [Quantity], [NumberOfDays], [PaymentID]) VALUES (20, 4, 14, CAST(N'2025-01-05T00:00:00.000' AS DateTime), CAST(N'2025-01-06T00:00:00.000' AS DateTime), N'Borrowed', CAST(0.00 AS Decimal(10, 2)), CAST(75000.00 AS Decimal(10, 2)), 0, CAST(N'2025-01-05T14:37:39.203' AS DateTime), 4, CAST(N'2025-01-05T14:37:39.203' AS DateTime), 4, 3, 1, 48)
INSERT [dbo].[BorrowRecords] ([RecordID], [UserID], [BookID], [BorrowDate], [DueReturnDate], [Status], [FineAmount], [BorrowPrice], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [Quantity], [NumberOfDays], [PaymentID]) VALUES (21, 4, 14, CAST(N'2025-01-05T00:00:00.000' AS DateTime), CAST(N'2025-01-06T00:00:00.000' AS DateTime), N'Returned', CAST(112500.00 AS Decimal(10, 2)), CAST(25000.00 AS Decimal(10, 2)), 0, CAST(N'2025-01-05T14:58:59.193' AS DateTime), 4, CAST(N'2025-01-09T13:05:30.223' AS DateTime), 22, 1, 1, 49)
INSERT [dbo].[BorrowRecords] ([RecordID], [UserID], [BookID], [BorrowDate], [DueReturnDate], [Status], [FineAmount], [BorrowPrice], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [Quantity], [NumberOfDays], [PaymentID]) VALUES (22, 4, 13, CAST(N'2025-01-05T00:00:00.000' AS DateTime), CAST(N'2025-01-06T00:00:00.000' AS DateTime), N'Borrowed', CAST(75000.00 AS Decimal(10, 2)), CAST(30000.00 AS Decimal(10, 2)), 0, CAST(N'2025-01-05T14:58:59.280' AS DateTime), 4, CAST(N'2025-01-05T14:58:59.280' AS DateTime), 4, 1, 1, 49)
INSERT [dbo].[BorrowRecords] ([RecordID], [UserID], [BookID], [BorrowDate], [DueReturnDate], [Status], [FineAmount], [BorrowPrice], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [Quantity], [NumberOfDays], [PaymentID]) VALUES (23, 4, 12, CAST(N'2025-01-05T00:00:00.000' AS DateTime), CAST(N'2025-01-06T00:00:00.000' AS DateTime), N'Returned', CAST(85000.00 AS Decimal(10, 2)), CAST(70000.00 AS Decimal(10, 2)), 0, CAST(N'2025-01-05T14:58:59.330' AS DateTime), 4, CAST(N'2025-01-05T14:58:59.330' AS DateTime), 4, 2, 1, 49)
INSERT [dbo].[BorrowRecords] ([RecordID], [UserID], [BookID], [BorrowDate], [DueReturnDate], [Status], [FineAmount], [BorrowPrice], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [Quantity], [NumberOfDays], [PaymentID]) VALUES (24, 4, 13, CAST(N'2025-01-05T00:00:00.000' AS DateTime), CAST(N'2025-01-06T00:00:00.000' AS DateTime), N'Borrowed', CAST(0.00 AS Decimal(10, 2)), CAST(30000.00 AS Decimal(10, 2)), 0, CAST(N'2025-01-05T15:05:48.247' AS DateTime), 4, CAST(N'2025-01-05T15:05:48.247' AS DateTime), 4, 1, 1, 50)
INSERT [dbo].[BorrowRecords] ([RecordID], [UserID], [BookID], [BorrowDate], [DueReturnDate], [Status], [FineAmount], [BorrowPrice], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [Quantity], [NumberOfDays], [PaymentID]) VALUES (25, 4, 14, CAST(N'2025-01-05T00:00:00.000' AS DateTime), CAST(N'2025-01-06T00:00:00.000' AS DateTime), N'Borrowed', CAST(0.00 AS Decimal(10, 2)), CAST(25000.00 AS Decimal(10, 2)), 0, CAST(N'2025-01-05T15:05:48.337' AS DateTime), 4, CAST(N'2025-01-05T15:05:48.337' AS DateTime), 4, 1, 1, 50)
INSERT [dbo].[BorrowRecords] ([RecordID], [UserID], [BookID], [BorrowDate], [DueReturnDate], [Status], [FineAmount], [BorrowPrice], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [Quantity], [NumberOfDays], [PaymentID]) VALUES (26, 4, 13, CAST(N'2025-01-05T00:00:00.000' AS DateTime), CAST(N'2025-01-06T00:00:00.000' AS DateTime), N'Returned', CAST(0.00 AS Decimal(10, 2)), CAST(30000.00 AS Decimal(10, 2)), 0, CAST(N'2025-01-05T15:07:07.167' AS DateTime), 4, CAST(N'2025-01-05T15:07:07.167' AS DateTime), 4, 1, 1, 51)
INSERT [dbo].[BorrowRecords] ([RecordID], [UserID], [BookID], [BorrowDate], [DueReturnDate], [Status], [FineAmount], [BorrowPrice], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [Quantity], [NumberOfDays], [PaymentID]) VALUES (27, 5, 3, CAST(N'2025-01-05T00:00:00.000' AS DateTime), CAST(N'2025-01-07T00:00:00.000' AS DateTime), N'Overdue', CAST(46800.00 AS Decimal(10, 2)), CAST(36000.00 AS Decimal(10, 2)), 0, CAST(N'2025-01-05T15:09:49.480' AS DateTime), 5, CAST(N'2025-01-05T15:09:49.480' AS DateTime), 5, 2, 2, 52)
INSERT [dbo].[BorrowRecords] ([RecordID], [UserID], [BookID], [BorrowDate], [DueReturnDate], [Status], [FineAmount], [BorrowPrice], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [Quantity], [NumberOfDays], [PaymentID]) VALUES (28, 5, 2, CAST(N'2025-01-05T00:00:00.000' AS DateTime), CAST(N'2025-01-08T00:00:00.000' AS DateTime), N'Overdue', CAST(26000.00 AS Decimal(10, 2)), CAST(60000.00 AS Decimal(10, 2)), 0, CAST(N'2025-01-05T15:09:49.590' AS DateTime), 5, CAST(N'2025-01-05T15:09:49.590' AS DateTime), 5, 3, 3, 52)
INSERT [dbo].[BorrowRecords] ([RecordID], [UserID], [BookID], [BorrowDate], [DueReturnDate], [Status], [FineAmount], [BorrowPrice], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [Quantity], [NumberOfDays], [PaymentID]) VALUES (29, 5, 1, CAST(N'2025-01-05T00:00:00.000' AS DateTime), CAST(N'2025-01-09T00:00:00.000' AS DateTime), N'Returned', CAST(0.00 AS Decimal(10, 2)), CAST(30000.00 AS Decimal(10, 2)), 0, CAST(N'2025-01-05T15:09:49.687' AS DateTime), 5, CAST(N'2025-01-05T15:09:49.687' AS DateTime), 5, 2, 4, 52)
INSERT [dbo].[BorrowRecords] ([RecordID], [UserID], [BookID], [BorrowDate], [DueReturnDate], [Status], [FineAmount], [BorrowPrice], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [Quantity], [NumberOfDays], [PaymentID]) VALUES (30, 4, 14, CAST(N'2025-01-05T00:00:00.000' AS DateTime), CAST(N'2025-01-29T00:00:00.000' AS DateTime), N'Returned', CAST(0.00 AS Decimal(10, 2)), CAST(25000.00 AS Decimal(10, 2)), 0, CAST(N'2025-01-05T18:07:29.230' AS DateTime), 4, CAST(N'2025-01-05T18:07:29.230' AS DateTime), 4, 1, 24, 53)
INSERT [dbo].[BorrowRecords] ([RecordID], [UserID], [BookID], [BorrowDate], [DueReturnDate], [Status], [FineAmount], [BorrowPrice], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [Quantity], [NumberOfDays], [PaymentID]) VALUES (31, 4, 13, CAST(N'2025-01-05T00:00:00.000' AS DateTime), CAST(N'2025-01-06T00:00:00.000' AS DateTime), N'Overdue', CAST(135000.00 AS Decimal(10, 2)), CAST(30000.00 AS Decimal(10, 2)), 0, CAST(N'2025-01-05T18:07:29.283' AS DateTime), 4, CAST(N'2025-01-05T18:07:29.283' AS DateTime), 4, 1, 1, 53)
INSERT [dbo].[BorrowRecords] ([RecordID], [UserID], [BookID], [BorrowDate], [DueReturnDate], [Status], [FineAmount], [BorrowPrice], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [Quantity], [NumberOfDays], [PaymentID]) VALUES (32, 4, 12, CAST(N'2025-01-05T00:00:00.000' AS DateTime), CAST(N'2025-01-06T00:00:00.000' AS DateTime), N'Overdue', CAST(157500.00 AS Decimal(10, 2)), CAST(35000.00 AS Decimal(10, 2)), 0, CAST(N'2025-01-05T18:07:29.353' AS DateTime), 4, CAST(N'2025-01-05T18:07:29.353' AS DateTime), 4, 1, 1, 53)
INSERT [dbo].[BorrowRecords] ([RecordID], [UserID], [BookID], [BorrowDate], [DueReturnDate], [Status], [FineAmount], [BorrowPrice], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [Quantity], [NumberOfDays], [PaymentID]) VALUES (33, 4, 7, CAST(N'2025-01-05T00:00:00.000' AS DateTime), CAST(N'2025-01-06T00:00:00.000' AS DateTime), N'Overdue', CAST(97500.00 AS Decimal(10, 2)), CAST(25000.00 AS Decimal(10, 2)), 0, CAST(N'2025-01-05T18:07:29.430' AS DateTime), 4, CAST(N'2025-01-05T18:07:29.430' AS DateTime), 4, 1, 1, 53)
INSERT [dbo].[BorrowRecords] ([RecordID], [UserID], [BookID], [BorrowDate], [DueReturnDate], [Status], [FineAmount], [BorrowPrice], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [Quantity], [NumberOfDays], [PaymentID]) VALUES (34, 4, 3, CAST(N'2025-01-05T00:00:00.000' AS DateTime), CAST(N'2025-01-06T00:00:00.000' AS DateTime), N'Overdue', CAST(70200.00 AS Decimal(10, 2)), CAST(18000.00 AS Decimal(10, 2)), 0, CAST(N'2025-01-05T18:07:29.500' AS DateTime), 4, CAST(N'2025-01-05T18:07:29.500' AS DateTime), 4, 1, 1, 53)
INSERT [dbo].[BorrowRecords] ([RecordID], [UserID], [BookID], [BorrowDate], [DueReturnDate], [Status], [FineAmount], [BorrowPrice], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [Quantity], [NumberOfDays], [PaymentID]) VALUES (35, 4, 13, CAST(N'2025-01-05T00:00:00.000' AS DateTime), CAST(N'2025-01-06T00:00:00.000' AS DateTime), N'Returned', CAST(0.00 AS Decimal(10, 2)), CAST(30000.00 AS Decimal(10, 2)), 0, CAST(N'2025-01-05T18:09:46.263' AS DateTime), 4, CAST(N'2025-01-05T18:09:46.263' AS DateTime), 4, 1, 1, 54)
INSERT [dbo].[BorrowRecords] ([RecordID], [UserID], [BookID], [BorrowDate], [DueReturnDate], [Status], [FineAmount], [BorrowPrice], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [Quantity], [NumberOfDays], [PaymentID]) VALUES (36, 4, 14, CAST(N'2025-01-05T00:00:00.000' AS DateTime), CAST(N'2025-01-06T00:00:00.000' AS DateTime), N'Returned', CAST(0.00 AS Decimal(10, 2)), CAST(25000.00 AS Decimal(10, 2)), 0, CAST(N'2025-01-05T18:09:46.323' AS DateTime), 4, CAST(N'2025-01-05T18:09:46.323' AS DateTime), 4, 1, 1, 54)
INSERT [dbo].[BorrowRecords] ([RecordID], [UserID], [BookID], [BorrowDate], [DueReturnDate], [Status], [FineAmount], [BorrowPrice], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [Quantity], [NumberOfDays], [PaymentID]) VALUES (37, 4, 12, CAST(N'2025-01-05T00:00:00.000' AS DateTime), CAST(N'2025-01-06T00:00:00.000' AS DateTime), N'Returned', CAST(0.00 AS Decimal(10, 2)), CAST(35000.00 AS Decimal(10, 2)), 0, CAST(N'2025-01-05T18:09:46.367' AS DateTime), 4, CAST(N'2025-01-05T18:09:46.367' AS DateTime), 4, 1, 1, 54)
INSERT [dbo].[BorrowRecords] ([RecordID], [UserID], [BookID], [BorrowDate], [DueReturnDate], [Status], [FineAmount], [BorrowPrice], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [Quantity], [NumberOfDays], [PaymentID]) VALUES (38, 4, 6, CAST(N'2025-01-05T00:00:00.000' AS DateTime), CAST(N'2025-01-07T00:00:00.000' AS DateTime), N'Overdue', CAST(91000.00 AS Decimal(10, 2)), CAST(35000.00 AS Decimal(10, 2)), 0, CAST(N'2025-01-05T21:08:08.997' AS DateTime), 4, CAST(N'2025-01-05T21:08:08.997' AS DateTime), 4, 1, 2, 55)
INSERT [dbo].[BorrowRecords] ([RecordID], [UserID], [BookID], [BorrowDate], [DueReturnDate], [Status], [FineAmount], [BorrowPrice], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [Quantity], [NumberOfDays], [PaymentID]) VALUES (39, 4, 4, CAST(N'2025-01-05T00:00:00.000' AS DateTime), CAST(N'2025-01-08T00:00:00.000' AS DateTime), N'Overdue', CAST(28600.00 AS Decimal(10, 2)), CAST(22000.00 AS Decimal(10, 2)), 0, CAST(N'2025-01-05T21:08:09.053' AS DateTime), 4, CAST(N'2025-01-05T21:08:09.053' AS DateTime), 4, 1, 3, 55)
INSERT [dbo].[BorrowRecords] ([RecordID], [UserID], [BookID], [BorrowDate], [DueReturnDate], [Status], [FineAmount], [BorrowPrice], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [Quantity], [NumberOfDays], [PaymentID]) VALUES (40, 4, 3, CAST(N'2025-01-05T00:00:00.000' AS DateTime), CAST(N'2025-01-09T00:00:00.000' AS DateTime), N'Returned', CAST(0.00 AS Decimal(10, 2)), CAST(18000.00 AS Decimal(10, 2)), 0, CAST(N'2025-01-05T21:08:09.107' AS DateTime), 4, CAST(N'2025-01-05T21:08:09.107' AS DateTime), 4, 1, 4, 55)
INSERT [dbo].[BorrowRecords] ([RecordID], [UserID], [BookID], [BorrowDate], [DueReturnDate], [Status], [FineAmount], [BorrowPrice], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [Quantity], [NumberOfDays], [PaymentID]) VALUES (41, 4, 13, CAST(N'2025-01-05T00:00:00.000' AS DateTime), CAST(N'2025-01-06T00:00:00.000' AS DateTime), N'Returned', CAST(75000.00 AS Decimal(10, 2)), CAST(30000.00 AS Decimal(10, 2)), 0, CAST(N'2025-01-05T21:28:58.267' AS DateTime), 4, CAST(N'2025-01-05T21:28:58.267' AS DateTime), 4, 1, 1, 56)
INSERT [dbo].[BorrowRecords] ([RecordID], [UserID], [BookID], [BorrowDate], [DueReturnDate], [Status], [FineAmount], [BorrowPrice], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [Quantity], [NumberOfDays], [PaymentID]) VALUES (42, 4, 13, CAST(N'2025-01-05T00:00:00.000' AS DateTime), CAST(N'2025-01-06T00:00:00.000' AS DateTime), N'Returned', CAST(75000.00 AS Decimal(10, 2)), CAST(30000.00 AS Decimal(10, 2)), 0, CAST(N'2025-01-05T21:29:27.757' AS DateTime), 4, CAST(N'2025-01-05T21:29:27.757' AS DateTime), 4, 1, 1, 57)
INSERT [dbo].[BorrowRecords] ([RecordID], [UserID], [BookID], [BorrowDate], [DueReturnDate], [Status], [FineAmount], [BorrowPrice], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [Quantity], [NumberOfDays], [PaymentID]) VALUES (43, 4, 3, CAST(N'2025-01-05T00:00:00.000' AS DateTime), CAST(N'2025-01-07T00:00:00.000' AS DateTime), N'Returned', CAST(0.00 AS Decimal(10, 2)), CAST(18000.00 AS Decimal(10, 2)), 0, CAST(N'2025-01-05T21:36:42.453' AS DateTime), 4, CAST(N'2025-01-05T21:36:42.453' AS DateTime), 4, 1, 2, 58)
INSERT [dbo].[BorrowRecords] ([RecordID], [UserID], [BookID], [BorrowDate], [DueReturnDate], [Status], [FineAmount], [BorrowPrice], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [Quantity], [NumberOfDays], [PaymentID]) VALUES (44, 4, 2, CAST(N'2025-01-05T00:00:00.000' AS DateTime), CAST(N'2025-01-08T00:00:00.000' AS DateTime), N'Returned', CAST(60000.00 AS Decimal(10, 2)), CAST(20000.00 AS Decimal(10, 2)), 0, CAST(N'2025-01-05T21:36:42.523' AS DateTime), 4, CAST(N'2025-01-05T21:36:42.523' AS DateTime), 4, 1, 3, 58)
INSERT [dbo].[BorrowRecords] ([RecordID], [UserID], [BookID], [BorrowDate], [DueReturnDate], [Status], [FineAmount], [BorrowPrice], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [Quantity], [NumberOfDays], [PaymentID]) VALUES (45, 4, 1, CAST(N'2025-01-05T00:00:00.000' AS DateTime), CAST(N'2025-01-09T00:00:00.000' AS DateTime), N'Returned', CAST(0.00 AS Decimal(10, 2)), CAST(15000.00 AS Decimal(10, 2)), 0, CAST(N'2025-01-05T21:36:42.673' AS DateTime), 4, CAST(N'2025-01-05T21:36:42.673' AS DateTime), 4, 1, 4, 58)
INSERT [dbo].[BorrowRecords] ([RecordID], [UserID], [BookID], [BorrowDate], [DueReturnDate], [Status], [FineAmount], [BorrowPrice], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [Quantity], [NumberOfDays], [PaymentID]) VALUES (46, 4, 12, CAST(N'2025-01-05T00:00:00.000' AS DateTime), CAST(N'2025-01-03T00:00:00.000' AS DateTime), N'Overdue', CAST(157500.00 AS Decimal(10, 2)), CAST(35000.00 AS Decimal(10, 2)), 0, CAST(N'2025-01-05T23:18:27.910' AS DateTime), 4, CAST(N'2025-01-05T23:18:27.910' AS DateTime), 4, 1, 3, 59)
INSERT [dbo].[BorrowRecords] ([RecordID], [UserID], [BookID], [BorrowDate], [DueReturnDate], [Status], [FineAmount], [BorrowPrice], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [Quantity], [NumberOfDays], [PaymentID]) VALUES (47, 4, 11, CAST(N'2025-01-05T00:00:00.000' AS DateTime), CAST(N'2025-01-07T00:00:00.000' AS DateTime), N'Returned', CAST(80000.00 AS Decimal(10, 2)), CAST(32000.00 AS Decimal(10, 2)), 0, CAST(N'2025-01-05T23:18:28.000' AS DateTime), 4, CAST(N'2025-01-05T23:18:28.000' AS DateTime), 4, 1, 2, 59)
INSERT [dbo].[BorrowRecords] ([RecordID], [UserID], [BookID], [BorrowDate], [DueReturnDate], [Status], [FineAmount], [BorrowPrice], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [Quantity], [NumberOfDays], [PaymentID]) VALUES (48, 4, 20, CAST(N'2025-01-06T00:00:00.000' AS DateTime), CAST(N'2025-01-08T00:00:00.000' AS DateTime), N'Returned', CAST(60000.00 AS Decimal(10, 2)), CAST(20000.00 AS Decimal(10, 2)), 0, CAST(N'2025-01-06T23:35:52.590' AS DateTime), 4, CAST(N'2025-01-06T23:35:52.590' AS DateTime), 4, 1, 2, 60)
INSERT [dbo].[BorrowRecords] ([RecordID], [UserID], [BookID], [BorrowDate], [DueReturnDate], [Status], [FineAmount], [BorrowPrice], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [Quantity], [NumberOfDays], [PaymentID]) VALUES (49, 4, 18, CAST(N'2025-01-07T00:00:00.000' AS DateTime), CAST(N'2025-01-09T00:00:00.000' AS DateTime), N'Returned', CAST(0.00 AS Decimal(10, 2)), CAST(30000.00 AS Decimal(10, 2)), 0, CAST(N'2025-01-07T21:34:12.643' AS DateTime), 4, CAST(N'2025-01-07T21:34:12.643' AS DateTime), 4, 1, 2, 61)
INSERT [dbo].[BorrowRecords] ([RecordID], [UserID], [BookID], [BorrowDate], [DueReturnDate], [Status], [FineAmount], [BorrowPrice], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [Quantity], [NumberOfDays], [PaymentID]) VALUES (50, 4, 20, CAST(N'2025-01-07T00:00:00.000' AS DateTime), CAST(N'2025-01-08T00:00:00.000' AS DateTime), N'Overdue', CAST(30000.00 AS Decimal(10, 2)), CAST(20000.00 AS Decimal(10, 2)), 0, CAST(N'2025-01-07T22:01:59.833' AS DateTime), 4, CAST(N'2025-01-07T22:01:59.833' AS DateTime), 4, 1, 1, 62)
INSERT [dbo].[BorrowRecords] ([RecordID], [UserID], [BookID], [BorrowDate], [DueReturnDate], [Status], [FineAmount], [BorrowPrice], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [Quantity], [NumberOfDays], [PaymentID]) VALUES (51, 4, 23, CAST(N'2025-01-07T00:00:00.000' AS DateTime), CAST(N'2025-01-23T00:00:00.000' AS DateTime), N'Borrowed', CAST(0.00 AS Decimal(10, 2)), CAST(2098442.00 AS Decimal(10, 2)), 0, CAST(N'2025-01-07T22:07:28.593' AS DateTime), 4, CAST(N'2025-01-07T22:07:28.593' AS DateTime), 4, 1, 16, 63)
INSERT [dbo].[BorrowRecords] ([RecordID], [UserID], [BookID], [BorrowDate], [DueReturnDate], [Status], [FineAmount], [BorrowPrice], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [Quantity], [NumberOfDays], [PaymentID]) VALUES (52, 4, 23, CAST(N'2025-01-07T00:00:00.000' AS DateTime), CAST(N'2025-01-30T00:00:00.000' AS DateTime), N'Borrowed', CAST(0.00 AS Decimal(10, 2)), CAST(2098442.00 AS Decimal(10, 2)), 0, CAST(N'2025-01-07T22:08:16.703' AS DateTime), 4, CAST(N'2025-01-07T22:08:16.703' AS DateTime), 4, 1, 23, 64)
INSERT [dbo].[BorrowRecords] ([RecordID], [UserID], [BookID], [BorrowDate], [DueReturnDate], [Status], [FineAmount], [BorrowPrice], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [Quantity], [NumberOfDays], [PaymentID]) VALUES (53, 4, 13, CAST(N'2025-01-07T00:00:00.000' AS DateTime), CAST(N'2025-01-09T00:00:00.000' AS DateTime), N'Returned', CAST(75000.00 AS Decimal(10, 2)), CAST(30000.00 AS Decimal(10, 2)), 0, CAST(N'2025-01-07T22:19:43.037' AS DateTime), 4, CAST(N'2025-01-07T22:19:43.037' AS DateTime), 4, 1, 2, 65)
INSERT [dbo].[BorrowRecords] ([RecordID], [UserID], [BookID], [BorrowDate], [DueReturnDate], [Status], [FineAmount], [BorrowPrice], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [Quantity], [NumberOfDays], [PaymentID]) VALUES (54, 4, 23, CAST(N'2025-01-08T00:00:00.000' AS DateTime), CAST(N'2025-01-09T00:00:00.000' AS DateTime), N'Returned', CAST(0.00 AS Decimal(10, 2)), CAST(2098442.00 AS Decimal(10, 2)), 0, CAST(N'2025-01-08T17:42:58.697' AS DateTime), 4, CAST(N'2025-01-08T17:42:58.697' AS DateTime), 4, 1, 1, 66)
INSERT [dbo].[BorrowRecords] ([RecordID], [UserID], [BookID], [BorrowDate], [DueReturnDate], [Status], [FineAmount], [BorrowPrice], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [Quantity], [NumberOfDays], [PaymentID]) VALUES (55, 4, 20, CAST(N'2025-01-08T00:00:00.000' AS DateTime), CAST(N'2025-01-11T00:00:00.000' AS DateTime), N'Returned', CAST(60000.00 AS Decimal(10, 2)), CAST(20000.00 AS Decimal(10, 2)), 0, CAST(N'2025-01-08T17:42:58.750' AS DateTime), 4, CAST(N'2025-01-08T17:42:58.750' AS DateTime), 4, 1, 3, 66)
INSERT [dbo].[BorrowRecords] ([RecordID], [UserID], [BookID], [BorrowDate], [DueReturnDate], [Status], [FineAmount], [BorrowPrice], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [Quantity], [NumberOfDays], [PaymentID]) VALUES (56, 4, 18, CAST(N'2025-01-09T00:00:00.000' AS DateTime), CAST(N'2025-01-11T00:00:00.000' AS DateTime), N'Returned', CAST(0.00 AS Decimal(10, 2)), CAST(30000.00 AS Decimal(10, 2)), 0, CAST(N'2025-01-09T01:05:10.747' AS DateTime), 4, CAST(N'2025-01-09T01:05:10.747' AS DateTime), 4, 1, 2, 67)
INSERT [dbo].[BorrowRecords] ([RecordID], [UserID], [BookID], [BorrowDate], [DueReturnDate], [Status], [FineAmount], [BorrowPrice], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [Quantity], [NumberOfDays], [PaymentID]) VALUES (57, 4, 12, CAST(N'2025-01-09T00:00:00.000' AS DateTime), CAST(N'2025-01-17T00:00:00.000' AS DateTime), N'Returned', CAST(0.00 AS Decimal(10, 2)), CAST(35000.00 AS Decimal(10, 2)), 0, CAST(N'2025-01-09T01:22:35.940' AS DateTime), 4, CAST(N'2025-01-09T01:22:35.940' AS DateTime), 4, 1, 8, 68)
INSERT [dbo].[BorrowRecords] ([RecordID], [UserID], [BookID], [BorrowDate], [DueReturnDate], [Status], [FineAmount], [BorrowPrice], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [Quantity], [NumberOfDays], [PaymentID]) VALUES (58, 21, 10, CAST(N'2025-01-09T00:00:00.000' AS DateTime), CAST(N'2025-01-16T00:00:00.000' AS DateTime), N'Borrowed', CAST(0.00 AS Decimal(10, 2)), CAST(22000.00 AS Decimal(10, 2)), 0, CAST(N'2025-01-09T11:41:35.237' AS DateTime), 21, CAST(N'2025-01-09T11:41:35.237' AS DateTime), 21, 1, 7, 69)
INSERT [dbo].[BorrowRecords] ([RecordID], [UserID], [BookID], [BorrowDate], [DueReturnDate], [Status], [FineAmount], [BorrowPrice], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [Quantity], [NumberOfDays], [PaymentID]) VALUES (59, 21, 9, CAST(N'2025-01-09T00:00:00.000' AS DateTime), CAST(N'2025-01-11T00:00:00.000' AS DateTime), N'Borrowed', CAST(0.00 AS Decimal(10, 2)), CAST(40000.00 AS Decimal(10, 2)), 0, CAST(N'2025-01-09T11:41:35.297' AS DateTime), 21, CAST(N'2025-01-09T11:41:35.297' AS DateTime), 21, 1, 2, 69)
INSERT [dbo].[BorrowRecords] ([RecordID], [UserID], [BookID], [BorrowDate], [DueReturnDate], [Status], [FineAmount], [BorrowPrice], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [Quantity], [NumberOfDays], [PaymentID]) VALUES (60, 21, 20, CAST(N'2025-01-09T00:00:00.000' AS DateTime), CAST(N'2025-01-11T00:00:00.000' AS DateTime), N'Borrowed', CAST(0.00 AS Decimal(10, 2)), CAST(20000.00 AS Decimal(10, 2)), 0, CAST(N'2025-01-09T11:45:25.547' AS DateTime), 21, CAST(N'2025-01-09T11:45:25.547' AS DateTime), 21, 1, 2, 70)
INSERT [dbo].[BorrowRecords] ([RecordID], [UserID], [BookID], [BorrowDate], [DueReturnDate], [Status], [FineAmount], [BorrowPrice], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [Quantity], [NumberOfDays], [PaymentID]) VALUES (61, 21, 12, CAST(N'2025-01-09T00:00:00.000' AS DateTime), CAST(N'2025-01-10T00:00:00.000' AS DateTime), N'Returned', CAST(0.00 AS Decimal(10, 2)), CAST(35000.00 AS Decimal(10, 2)), 0, CAST(N'2025-01-09T11:57:33.383' AS DateTime), 21, CAST(N'2025-01-09T11:57:33.383' AS DateTime), 21, 1, 1, 71)
INSERT [dbo].[BorrowRecords] ([RecordID], [UserID], [BookID], [BorrowDate], [DueReturnDate], [Status], [FineAmount], [BorrowPrice], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [Quantity], [NumberOfDays], [PaymentID]) VALUES (62, 21, 12, CAST(N'2025-01-09T00:00:00.000' AS DateTime), CAST(N'2025-01-10T00:00:00.000' AS DateTime), N'Returned', CAST(0.00 AS Decimal(10, 2)), CAST(35000.00 AS Decimal(10, 2)), 0, CAST(N'2025-01-09T11:58:54.970' AS DateTime), 21, CAST(N'2025-01-09T11:58:54.970' AS DateTime), 21, 1, 1, 72)
INSERT [dbo].[BorrowRecords] ([RecordID], [UserID], [BookID], [BorrowDate], [DueReturnDate], [Status], [FineAmount], [BorrowPrice], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [Quantity], [NumberOfDays], [PaymentID]) VALUES (63, 21, 6, CAST(N'2025-01-09T00:00:00.000' AS DateTime), CAST(N'2025-01-11T00:00:00.000' AS DateTime), N'Borrowed', CAST(0.00 AS Decimal(10, 2)), CAST(35000.00 AS Decimal(10, 2)), 0, CAST(N'2025-01-09T12:04:53.373' AS DateTime), 21, CAST(N'2025-01-09T12:04:53.373' AS DateTime), 21, 1, 2, 73)
INSERT [dbo].[BorrowRecords] ([RecordID], [UserID], [BookID], [BorrowDate], [DueReturnDate], [Status], [FineAmount], [BorrowPrice], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [Quantity], [NumberOfDays], [PaymentID]) VALUES (64, 21, 20, CAST(N'2025-01-09T00:00:00.000' AS DateTime), CAST(N'2025-01-11T00:00:00.000' AS DateTime), N'Borrowed', CAST(0.00 AS Decimal(10, 2)), CAST(20000.00 AS Decimal(10, 2)), 0, CAST(N'2025-01-09T12:12:42.473' AS DateTime), 21, CAST(N'2025-01-09T12:12:42.473' AS DateTime), 21, 1, 2, 74)
INSERT [dbo].[BorrowRecords] ([RecordID], [UserID], [BookID], [BorrowDate], [DueReturnDate], [Status], [FineAmount], [BorrowPrice], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [Quantity], [NumberOfDays], [PaymentID]) VALUES (65, 4, 20, CAST(N'2025-01-09T00:00:00.000' AS DateTime), CAST(N'2025-01-10T00:00:00.000' AS DateTime), N'Borrowed', CAST(0.00 AS Decimal(10, 2)), CAST(20000.00 AS Decimal(10, 2)), 0, CAST(N'2025-01-09T12:18:58.840' AS DateTime), 4, CAST(N'2025-01-09T12:18:58.840' AS DateTime), 4, 1, 1, 75)
INSERT [dbo].[BorrowRecords] ([RecordID], [UserID], [BookID], [BorrowDate], [DueReturnDate], [Status], [FineAmount], [BorrowPrice], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [Quantity], [NumberOfDays], [PaymentID]) VALUES (66, 4, 19, CAST(N'2025-01-09T00:00:00.000' AS DateTime), CAST(N'2025-01-10T00:00:00.000' AS DateTime), N'Borrowed', CAST(0.00 AS Decimal(10, 2)), CAST(25000.00 AS Decimal(10, 2)), 0, CAST(N'2025-01-09T12:21:19.640' AS DateTime), 4, CAST(N'2025-01-09T12:21:19.640' AS DateTime), 4, 1, 1, 76)
INSERT [dbo].[BorrowRecords] ([RecordID], [UserID], [BookID], [BorrowDate], [DueReturnDate], [Status], [FineAmount], [BorrowPrice], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [Quantity], [NumberOfDays], [PaymentID]) VALUES (67, 4, 19, CAST(N'2025-01-09T00:00:00.000' AS DateTime), CAST(N'2025-01-10T00:00:00.000' AS DateTime), N'Borrowed', CAST(0.00 AS Decimal(10, 2)), CAST(25000.00 AS Decimal(10, 2)), 0, CAST(N'2025-01-09T12:26:08.597' AS DateTime), 4, CAST(N'2025-01-09T12:26:08.597' AS DateTime), 4, 1, 1, 77)
INSERT [dbo].[BorrowRecords] ([RecordID], [UserID], [BookID], [BorrowDate], [DueReturnDate], [Status], [FineAmount], [BorrowPrice], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [Quantity], [NumberOfDays], [PaymentID]) VALUES (68, 21, 19, CAST(N'2025-01-09T00:00:00.000' AS DateTime), CAST(N'2025-01-10T00:00:00.000' AS DateTime), N'Returned', CAST(0.00 AS Decimal(10, 2)), CAST(25000.00 AS Decimal(10, 2)), 0, CAST(N'2025-01-09T12:30:23.417' AS DateTime), 21, CAST(N'2025-01-09T12:30:23.420' AS DateTime), 21, 1, 1, 78)
INSERT [dbo].[BorrowRecords] ([RecordID], [UserID], [BookID], [BorrowDate], [DueReturnDate], [Status], [FineAmount], [BorrowPrice], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [Quantity], [NumberOfDays], [PaymentID]) VALUES (69, 21, 19, CAST(N'2025-01-09T00:00:00.000' AS DateTime), CAST(N'2025-01-25T00:00:00.000' AS DateTime), N'Borrowed', CAST(70000.00 AS Decimal(10, 2)), CAST(25000.00 AS Decimal(10, 2)), 0, CAST(N'2025-01-09T12:49:31.763' AS DateTime), 21, CAST(N'2025-01-09T12:49:31.763' AS DateTime), 21, 1, 16, 79)
INSERT [dbo].[BorrowRecords] ([RecordID], [UserID], [BookID], [BorrowDate], [DueReturnDate], [Status], [FineAmount], [BorrowPrice], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [Quantity], [NumberOfDays], [PaymentID]) VALUES (70, 21, 19, CAST(N'2025-01-09T00:00:00.000' AS DateTime), CAST(N'2025-01-10T00:00:00.000' AS DateTime), N'Borrowed', CAST(0.00 AS Decimal(10, 2)), CAST(25000.00 AS Decimal(10, 2)), 0, CAST(N'2025-01-09T13:41:50.657' AS DateTime), 21, CAST(N'2025-01-09T13:41:50.657' AS DateTime), 21, 1, 1, 80)
INSERT [dbo].[BorrowRecords] ([RecordID], [UserID], [BookID], [BorrowDate], [DueReturnDate], [Status], [FineAmount], [BorrowPrice], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [Quantity], [NumberOfDays], [PaymentID]) VALUES (71, 21, 18, CAST(N'2025-01-09T00:00:00.000' AS DateTime), CAST(N'2025-01-10T00:00:00.000' AS DateTime), N'Borrowed', CAST(0.00 AS Decimal(10, 2)), CAST(30000.00 AS Decimal(10, 2)), 0, CAST(N'2025-01-09T14:22:05.303' AS DateTime), 21, CAST(N'2025-01-09T14:22:05.303' AS DateTime), 21, 1, 1, 81)
SET IDENTITY_INSERT [dbo].[BorrowRecords] OFF
GO
SET IDENTITY_INSERT [dbo].[Categories] ON 

INSERT [dbo].[Categories] ([CategoryID], [CategoryName], [Description], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (1, N'Lịch sử', N'Những sách về lịch sử', 0, CAST(N'2024-12-29T20:11:00.857' AS DateTime), 1, CAST(N'2024-12-29T20:11:00.857' AS DateTime), NULL)
INSERT [dbo].[Categories] ([CategoryID], [CategoryName], [Description], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (2, N'Văn hóa', N'Những sách về văn hóa', 0, CAST(N'2024-12-29T20:11:00.857' AS DateTime), 1, CAST(N'2024-12-29T20:11:00.857' AS DateTime), NULL)
INSERT [dbo].[Categories] ([CategoryID], [CategoryName], [Description], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (3, N'CNTT', N'Những sách về công nghệ thông tin', 0, CAST(N'2024-12-29T20:11:00.857' AS DateTime), 1, CAST(N'2024-12-29T20:11:00.857' AS DateTime), NULL)
INSERT [dbo].[Categories] ([CategoryID], [CategoryName], [Description], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (4, N'Máy tính', N'Những sách về máy tính', 0, CAST(N'2024-12-29T20:11:00.857' AS DateTime), 1, CAST(N'2024-12-29T20:11:00.857' AS DateTime), NULL)
INSERT [dbo].[Categories] ([CategoryID], [CategoryName], [Description], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (5, N'Tâm lý học', N'Những sách về tâm lý học', 0, CAST(N'2024-12-29T20:11:00.857' AS DateTime), 1, CAST(N'2024-12-29T20:11:00.857' AS DateTime), NULL)
INSERT [dbo].[Categories] ([CategoryID], [CategoryName], [Description], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (6, N'Khoa học', N'Những sách về khoa học', 0, CAST(N'2024-12-29T20:11:00.857' AS DateTime), 1, CAST(N'2024-12-29T20:11:00.857' AS DateTime), NULL)
INSERT [dbo].[Categories] ([CategoryID], [CategoryName], [Description], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (7, N'Xã hội', N'Những sách về xã hội học', 0, CAST(N'2024-12-29T20:11:00.857' AS DateTime), 1, CAST(N'2024-12-29T20:11:00.857' AS DateTime), NULL)
INSERT [dbo].[Categories] ([CategoryID], [CategoryName], [Description], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (8, N'Category 1', N'Description 1', 1, CAST(N'2025-01-03T14:42:11.977' AS DateTime), 1, CAST(N'2025-01-03T14:42:11.977' AS DateTime), 1)
INSERT [dbo].[Categories] ([CategoryID], [CategoryName], [Description], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (9, N'Category 2', N'Description 2', 1, CAST(N'2025-01-03T14:42:11.977' AS DateTime), 1, CAST(N'2025-01-03T14:42:11.977' AS DateTime), 1)
INSERT [dbo].[Categories] ([CategoryID], [CategoryName], [Description], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (10, N'Category 3', N'Description 3', 1, CAST(N'2025-01-03T14:42:11.977' AS DateTime), 1, CAST(N'2025-01-03T14:42:11.977' AS DateTime), 1)
INSERT [dbo].[Categories] ([CategoryID], [CategoryName], [Description], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (11, N'Category 4', N'Description 4', 1, CAST(N'2025-01-03T14:42:11.977' AS DateTime), 1, CAST(N'2025-01-03T14:42:11.977' AS DateTime), 1)
INSERT [dbo].[Categories] ([CategoryID], [CategoryName], [Description], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (12, N'Category 5', N'Description 5', 1, CAST(N'2025-01-03T14:42:11.977' AS DateTime), 1, CAST(N'2025-01-03T14:42:11.977' AS DateTime), 1)
INSERT [dbo].[Categories] ([CategoryID], [CategoryName], [Description], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (13, N'Category 6', N'Description 6', 1, CAST(N'2025-01-03T14:42:11.977' AS DateTime), 1, CAST(N'2025-01-03T14:42:11.977' AS DateTime), 1)
INSERT [dbo].[Categories] ([CategoryID], [CategoryName], [Description], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (14, N'Category 7', N'Description 7', 1, CAST(N'2025-01-03T14:42:11.977' AS DateTime), 1, CAST(N'2025-01-03T14:42:11.977' AS DateTime), 1)
INSERT [dbo].[Categories] ([CategoryID], [CategoryName], [Description], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (15, N'Category 8', N'Description 8', 1, CAST(N'2025-01-03T14:42:11.977' AS DateTime), 1, CAST(N'2025-01-03T14:42:11.977' AS DateTime), 1)
INSERT [dbo].[Categories] ([CategoryID], [CategoryName], [Description], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (16, N'Category 9', N'Description 9', 1, CAST(N'2025-01-03T14:42:11.977' AS DateTime), 1, CAST(N'2025-01-03T14:42:11.977' AS DateTime), 1)
INSERT [dbo].[Categories] ([CategoryID], [CategoryName], [Description], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (17, N'Category 10', N'Description 10', 1, CAST(N'2025-01-03T14:42:11.977' AS DateTime), 1, CAST(N'2025-01-09T16:30:43.227' AS DateTime), 1)
INSERT [dbo].[Categories] ([CategoryID], [CategoryName], [Description], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (18, N'aaae', N'sách hài', 1, CAST(N'2025-01-07T10:21:37.653' AS DateTime), NULL, CAST(N'2025-01-07T10:40:29.690' AS DateTime), 1)
INSERT [dbo].[Categories] ([CategoryID], [CategoryName], [Description], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (19, N'aaa', N'1111', 1, CAST(N'2025-01-07T10:21:55.363' AS DateTime), NULL, CAST(N'2025-01-07T11:48:43.743' AS DateTime), 1)
INSERT [dbo].[Categories] ([CategoryID], [CategoryName], [Description], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (20, N'đáa', N'fdfsdfsdfsad', 1, CAST(N'2025-01-07T12:18:26.070' AS DateTime), NULL, CAST(N'2025-01-07T12:18:43.507' AS DateTime), 1)
SET IDENTITY_INSERT [dbo].[Categories] OFF
GO
SET IDENTITY_INSERT [dbo].[DamagedBooks] ON 

INSERT [dbo].[DamagedBooks] ([DamageID], [BookID], [ReportedBy], [DamageDate], [DamageDescription], [DamageSeverity], [Status], [RepairCost], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (1, 12, 4, CAST(N'2025-01-05T21:51:01.533' AS DateTime), N'Alo lỗi rồi nga', N'Low', N'Pending', NULL, 0, CAST(N'2025-01-05T21:53:21.387' AS DateTime), 4, CAST(N'2025-01-05T21:53:21.387' AS DateTime), 0)
INSERT [dbo].[DamagedBooks] ([DamageID], [BookID], [ReportedBy], [DamageDate], [DamageDescription], [DamageSeverity], [Status], [RepairCost], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (2, 20, 4, CAST(N'2025-01-05T21:55:02.823' AS DateTime), N'lỗi nà', N'Low', N'Pending', NULL, 0, CAST(N'2025-01-05T21:55:02.840' AS DateTime), 4, CAST(N'2025-01-05T21:55:02.840' AS DateTime), 0)
INSERT [dbo].[DamagedBooks] ([DamageID], [BookID], [ReportedBy], [DamageDate], [DamageDescription], [DamageSeverity], [Status], [RepairCost], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (3, 20, 4, CAST(N'2025-01-05T22:27:56.763' AS DateTime), N'1', N'Medium', N'Pending', NULL, 0, CAST(N'2025-01-05T22:27:56.780' AS DateTime), 4, CAST(N'2025-01-05T22:27:56.780' AS DateTime), 0)
INSERT [dbo].[DamagedBooks] ([DamageID], [BookID], [ReportedBy], [DamageDate], [DamageDescription], [DamageSeverity], [Status], [RepairCost], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (4, 11, 4, CAST(N'2025-01-05T23:19:09.860' AS DateTime), N'rách', N'Medium', N'Pending', NULL, 0, CAST(N'2025-01-05T23:19:09.877' AS DateTime), 4, CAST(N'2025-01-05T23:19:09.877' AS DateTime), 0)
INSERT [dbo].[DamagedBooks] ([DamageID], [BookID], [ReportedBy], [DamageDate], [DamageDescription], [DamageSeverity], [Status], [RepairCost], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (6, 20, 4, CAST(N'2025-01-07T19:56:44.777' AS DateTime), N'', N'Low', N'Pending', NULL, 0, CAST(N'2025-01-07T19:56:44.803' AS DateTime), 4, CAST(N'2025-01-07T19:56:44.803' AS DateTime), 0)
INSERT [dbo].[DamagedBooks] ([DamageID], [BookID], [ReportedBy], [DamageDate], [DamageDescription], [DamageSeverity], [Status], [RepairCost], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (7, 13, 4, CAST(N'2025-01-07T21:56:14.057' AS DateTime), N'', N'Low', N'Pending', NULL, 0, CAST(N'2025-01-07T21:56:14.080' AS DateTime), 4, CAST(N'2025-01-07T21:56:14.080' AS DateTime), 0)
INSERT [dbo].[DamagedBooks] ([DamageID], [BookID], [ReportedBy], [DamageDate], [DamageDescription], [DamageSeverity], [Status], [RepairCost], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (8, 13, 4, CAST(N'2025-01-07T22:20:46.697' AS DateTime), N'ng', N'High', N'Pending', NULL, 0, CAST(N'2025-01-07T22:20:46.720' AS DateTime), 4, CAST(N'2025-01-07T22:20:46.720' AS DateTime), 0)
INSERT [dbo].[DamagedBooks] ([DamageID], [BookID], [ReportedBy], [DamageDate], [DamageDescription], [DamageSeverity], [Status], [RepairCost], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (9, 20, 4, CAST(N'2025-01-08T17:43:54.940' AS DateTime), N'rach', N'Medium', N'Pending', NULL, 0, CAST(N'2025-01-08T17:43:54.960' AS DateTime), 4, CAST(N'2025-01-08T17:43:54.960' AS DateTime), 0)
INSERT [dbo].[DamagedBooks] ([DamageID], [BookID], [ReportedBy], [DamageDate], [DamageDescription], [DamageSeverity], [Status], [RepairCost], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (10, 13, 4, CAST(N'2025-01-09T01:08:38.717' AS DateTime), N'tt', N'Medium', N'Pending', NULL, 0, CAST(N'2025-01-09T01:08:38.740' AS DateTime), 4, CAST(N'2025-01-09T01:08:38.740' AS DateTime), 0)
INSERT [dbo].[DamagedBooks] ([DamageID], [BookID], [ReportedBy], [DamageDate], [DamageDescription], [DamageSeverity], [Status], [RepairCost], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (11, 19, 21, CAST(N'2025-01-09T12:50:47.557' AS DateTime), N'uu', N'Medium', N'Pending', NULL, 0, CAST(N'2025-01-09T12:50:47.580' AS DateTime), 21, CAST(N'2025-01-09T12:50:47.580' AS DateTime), 0)
INSERT [dbo].[DamagedBooks] ([DamageID], [BookID], [ReportedBy], [DamageDate], [DamageDescription], [DamageSeverity], [Status], [RepairCost], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (12, 13, 21, CAST(N'2025-01-09T13:05:30.243' AS DateTime), N'1', N'Low', N'Pending', NULL, 0, CAST(N'2025-01-09T13:05:30.270' AS DateTime), 21, CAST(N'2025-01-09T13:05:30.270' AS DateTime), 0)
INSERT [dbo].[DamagedBooks] ([DamageID], [BookID], [ReportedBy], [DamageDate], [DamageDescription], [DamageSeverity], [Status], [RepairCost], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (13, 20, 21, CAST(N'2025-01-09T13:07:43.520' AS DateTime), N'', N'Low', N'Pending', NULL, 0, CAST(N'2025-01-09T13:07:43.550' AS DateTime), 21, CAST(N'2025-01-09T13:07:43.550' AS DateTime), 0)
INSERT [dbo].[DamagedBooks] ([DamageID], [BookID], [ReportedBy], [DamageDate], [DamageDescription], [DamageSeverity], [Status], [RepairCost], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (14, 10, 21, CAST(N'2025-01-09T13:08:27.850' AS DateTime), N'ggg', N'Medium', N'Pending', NULL, 0, CAST(N'2025-01-09T13:08:27.860' AS DateTime), 21, CAST(N'2025-01-09T13:08:27.860' AS DateTime), 0)
SET IDENTITY_INSERT [dbo].[DamagedBooks] OFF
GO
SET IDENTITY_INSERT [dbo].[Payments] ON 

INSERT [dbo].[Payments] ([PaymentID], [UserID], [Amount], [PaymentDate], [PaymentMethod], [Description], [AmountGiven], [TotalOrderAmount], [ChangeAmount], [IsDeleted], [StudentID]) VALUES (1, 1, CAST(2765000.00 AS Decimal(15, 2)), CAST(N'2025-01-02T13:07:15.447' AS DateTime), N'Cash', N'Book Rental Payment', CAST(3000000.00 AS Decimal(15, 2)), CAST(2765000.00 AS Decimal(15, 2)), CAST(235000.00 AS Decimal(15, 2)), 0, 82)
INSERT [dbo].[Payments] ([PaymentID], [UserID], [Amount], [PaymentDate], [PaymentMethod], [Description], [AmountGiven], [TotalOrderAmount], [ChangeAmount], [IsDeleted], [StudentID]) VALUES (32, 4, CAST(20000.00 AS Decimal(15, 2)), CAST(N'2025-01-02T14:06:06.053' AS DateTime), N'Cash', N'Payment for book rental', CAST(30000.00 AS Decimal(15, 2)), CAST(20000.00 AS Decimal(15, 2)), CAST(10000.00 AS Decimal(15, 2)), 0, 82)
INSERT [dbo].[Payments] ([PaymentID], [UserID], [Amount], [PaymentDate], [PaymentMethod], [Description], [AmountGiven], [TotalOrderAmount], [ChangeAmount], [IsDeleted], [StudentID]) VALUES (33, 4, CAST(20000.00 AS Decimal(15, 2)), CAST(N'2025-01-02T14:29:43.630' AS DateTime), N'Cash', N'Payment for book rental', CAST(30000.00 AS Decimal(15, 2)), CAST(20000.00 AS Decimal(15, 2)), CAST(10000.00 AS Decimal(15, 2)), 0, 82)
INSERT [dbo].[Payments] ([PaymentID], [UserID], [Amount], [PaymentDate], [PaymentMethod], [Description], [AmountGiven], [TotalOrderAmount], [ChangeAmount], [IsDeleted], [StudentID]) VALUES (34, 4, CAST(45000.00 AS Decimal(15, 2)), CAST(N'2025-01-02T14:30:28.123' AS DateTime), N'Cash', N'Payment for book rental', CAST(50000.00 AS Decimal(15, 2)), CAST(45000.00 AS Decimal(15, 2)), CAST(5000.00 AS Decimal(15, 2)), 0, 82)
INSERT [dbo].[Payments] ([PaymentID], [UserID], [Amount], [PaymentDate], [PaymentMethod], [Description], [AmountGiven], [TotalOrderAmount], [ChangeAmount], [IsDeleted], [StudentID]) VALUES (35, 4, CAST(20000.00 AS Decimal(15, 2)), CAST(N'2025-01-02T17:56:09.913' AS DateTime), N'Cash', N'Payment for book rental', CAST(30000.00 AS Decimal(15, 2)), CAST(20000.00 AS Decimal(15, 2)), CAST(10000.00 AS Decimal(15, 2)), 0, 82)
INSERT [dbo].[Payments] ([PaymentID], [UserID], [Amount], [PaymentDate], [PaymentMethod], [Description], [AmountGiven], [TotalOrderAmount], [ChangeAmount], [IsDeleted], [StudentID]) VALUES (36, 4, CAST(50000.00 AS Decimal(15, 2)), CAST(N'2025-01-02T18:09:04.790' AS DateTime), N'Cash', N'Payment for book rental', CAST(60000.00 AS Decimal(15, 2)), CAST(50000.00 AS Decimal(15, 2)), CAST(10000.00 AS Decimal(15, 2)), 0, 79)
INSERT [dbo].[Payments] ([PaymentID], [UserID], [Amount], [PaymentDate], [PaymentMethod], [Description], [AmountGiven], [TotalOrderAmount], [ChangeAmount], [IsDeleted], [StudentID]) VALUES (37, 4, CAST(105000.00 AS Decimal(15, 2)), CAST(N'2025-01-02T18:17:17.693' AS DateTime), N'Cash', N'Payment for book rental', CAST(105000.00 AS Decimal(15, 2)), CAST(105000.00 AS Decimal(15, 2)), CAST(0.00 AS Decimal(15, 2)), 0, 82)
INSERT [dbo].[Payments] ([PaymentID], [UserID], [Amount], [PaymentDate], [PaymentMethod], [Description], [AmountGiven], [TotalOrderAmount], [ChangeAmount], [IsDeleted], [StudentID]) VALUES (38, 4, CAST(50000.00 AS Decimal(15, 2)), CAST(N'2025-01-02T18:19:10.810' AS DateTime), N'Cash', N'Payment for book rental', CAST(59999.00 AS Decimal(15, 2)), CAST(50000.00 AS Decimal(15, 2)), CAST(9999.00 AS Decimal(15, 2)), 0, 79)
INSERT [dbo].[Payments] ([PaymentID], [UserID], [Amount], [PaymentDate], [PaymentMethod], [Description], [AmountGiven], [TotalOrderAmount], [ChangeAmount], [IsDeleted], [StudentID]) VALUES (39, 4, CAST(45000.00 AS Decimal(15, 2)), CAST(N'2025-01-02T18:24:37.473' AS DateTime), N'Cash', N'Payment for book rental', CAST(50000.00 AS Decimal(15, 2)), CAST(45000.00 AS Decimal(15, 2)), CAST(5000.00 AS Decimal(15, 2)), 0, 82)
INSERT [dbo].[Payments] ([PaymentID], [UserID], [Amount], [PaymentDate], [PaymentMethod], [Description], [AmountGiven], [TotalOrderAmount], [ChangeAmount], [IsDeleted], [StudentID]) VALUES (40, 4, CAST(45000.00 AS Decimal(15, 2)), CAST(N'2025-01-02T18:25:29.043' AS DateTime), N'Cash', N'Payment for book rental', CAST(50000.00 AS Decimal(15, 2)), CAST(45000.00 AS Decimal(15, 2)), CAST(5000.00 AS Decimal(15, 2)), 0, 82)
INSERT [dbo].[Payments] ([PaymentID], [UserID], [Amount], [PaymentDate], [PaymentMethod], [Description], [AmountGiven], [TotalOrderAmount], [ChangeAmount], [IsDeleted], [StudentID]) VALUES (41, 4, CAST(45000.00 AS Decimal(15, 2)), CAST(N'2025-01-02T18:28:44.087' AS DateTime), N'Cash', N'Payment for book rental', CAST(50000.00 AS Decimal(15, 2)), CAST(45000.00 AS Decimal(15, 2)), CAST(5000.00 AS Decimal(15, 2)), 0, 82)
INSERT [dbo].[Payments] ([PaymentID], [UserID], [Amount], [PaymentDate], [PaymentMethod], [Description], [AmountGiven], [TotalOrderAmount], [ChangeAmount], [IsDeleted], [StudentID]) VALUES (42, 4, CAST(1634000.00 AS Decimal(15, 2)), CAST(N'2025-01-02T19:42:14.820' AS DateTime), N'Cash', N'Payment for book rental', CAST(2000000.00 AS Decimal(15, 2)), CAST(1634000.00 AS Decimal(15, 2)), CAST(366000.00 AS Decimal(15, 2)), 0, 82)
INSERT [dbo].[Payments] ([PaymentID], [UserID], [Amount], [PaymentDate], [PaymentMethod], [Description], [AmountGiven], [TotalOrderAmount], [ChangeAmount], [IsDeleted], [StudentID]) VALUES (43, 4, CAST(280000.00 AS Decimal(15, 2)), CAST(N'2025-01-02T19:45:38.937' AS DateTime), N'Cash', N'Payment for book rental', CAST(300000.00 AS Decimal(15, 2)), CAST(280000.00 AS Decimal(15, 2)), CAST(20000.00 AS Decimal(15, 2)), 0, 79)
INSERT [dbo].[Payments] ([PaymentID], [UserID], [Amount], [PaymentDate], [PaymentMethod], [Description], [AmountGiven], [TotalOrderAmount], [ChangeAmount], [IsDeleted], [StudentID]) VALUES (44, 4, CAST(485000.00 AS Decimal(15, 2)), CAST(N'2025-01-03T15:15:46.940' AS DateTime), N'Cash', N'Payment for book rental', CAST(500000.00 AS Decimal(15, 2)), CAST(485000.00 AS Decimal(15, 2)), CAST(15000.00 AS Decimal(15, 2)), 0, 82)
INSERT [dbo].[Payments] ([PaymentID], [UserID], [Amount], [PaymentDate], [PaymentMethod], [Description], [AmountGiven], [TotalOrderAmount], [ChangeAmount], [IsDeleted], [StudentID]) VALUES (46, 4, CAST(174000.00 AS Decimal(15, 2)), CAST(N'2025-01-05T14:25:58.163' AS DateTime), N'Cash', N'Payment for book rental', CAST(185000.00 AS Decimal(15, 2)), CAST(174000.00 AS Decimal(15, 2)), CAST(11000.00 AS Decimal(15, 2)), 0, 82)
INSERT [dbo].[Payments] ([PaymentID], [UserID], [Amount], [PaymentDate], [PaymentMethod], [Description], [AmountGiven], [TotalOrderAmount], [ChangeAmount], [IsDeleted], [StudentID]) VALUES (47, 4, CAST(120000.00 AS Decimal(15, 2)), CAST(N'2025-01-05T14:32:08.130' AS DateTime), N'Cash', N'Payment for book rental', CAST(120000.00 AS Decimal(15, 2)), CAST(120000.00 AS Decimal(15, 2)), CAST(0.00 AS Decimal(15, 2)), 0, 82)
INSERT [dbo].[Payments] ([PaymentID], [UserID], [Amount], [PaymentDate], [PaymentMethod], [Description], [AmountGiven], [TotalOrderAmount], [ChangeAmount], [IsDeleted], [StudentID]) VALUES (48, 4, CAST(105000.00 AS Decimal(15, 2)), CAST(N'2025-01-05T14:37:39.027' AS DateTime), N'Cash', N'Payment for book rental', CAST(105000.00 AS Decimal(15, 2)), CAST(105000.00 AS Decimal(15, 2)), CAST(0.00 AS Decimal(15, 2)), 0, 79)
INSERT [dbo].[Payments] ([PaymentID], [UserID], [Amount], [PaymentDate], [PaymentMethod], [Description], [AmountGiven], [TotalOrderAmount], [ChangeAmount], [IsDeleted], [StudentID]) VALUES (49, 4, CAST(125000.00 AS Decimal(15, 2)), CAST(N'2025-01-05T14:58:59.040' AS DateTime), N'Cash', N'Payment for book rental', CAST(234567.00 AS Decimal(15, 2)), CAST(125000.00 AS Decimal(15, 2)), CAST(109567.00 AS Decimal(15, 2)), 0, 82)
INSERT [dbo].[Payments] ([PaymentID], [UserID], [Amount], [PaymentDate], [PaymentMethod], [Description], [AmountGiven], [TotalOrderAmount], [ChangeAmount], [IsDeleted], [StudentID]) VALUES (50, 4, CAST(55000.00 AS Decimal(15, 2)), CAST(N'2025-01-05T15:05:48.090' AS DateTime), N'Cash', N'Payment for book rental', CAST(1234567.00 AS Decimal(15, 2)), CAST(55000.00 AS Decimal(15, 2)), CAST(1179567.00 AS Decimal(15, 2)), 0, 82)
INSERT [dbo].[Payments] ([PaymentID], [UserID], [Amount], [PaymentDate], [PaymentMethod], [Description], [AmountGiven], [TotalOrderAmount], [ChangeAmount], [IsDeleted], [StudentID]) VALUES (51, 4, CAST(30000.00 AS Decimal(15, 2)), CAST(N'2025-01-05T15:07:07.023' AS DateTime), N'Cash', N'Payment for book rental', CAST(111111.00 AS Decimal(15, 2)), CAST(30000.00 AS Decimal(15, 2)), CAST(81111.00 AS Decimal(15, 2)), 0, 82)
INSERT [dbo].[Payments] ([PaymentID], [UserID], [Amount], [PaymentDate], [PaymentMethod], [Description], [AmountGiven], [TotalOrderAmount], [ChangeAmount], [IsDeleted], [StudentID]) VALUES (52, 5, CAST(372000.00 AS Decimal(15, 2)), CAST(N'2025-01-05T15:09:49.270' AS DateTime), N'Cash', N'Payment for book rental', CAST(400000.00 AS Decimal(15, 2)), CAST(372000.00 AS Decimal(15, 2)), CAST(28000.00 AS Decimal(15, 2)), 0, 66)
INSERT [dbo].[Payments] ([PaymentID], [UserID], [Amount], [PaymentDate], [PaymentMethod], [Description], [AmountGiven], [TotalOrderAmount], [ChangeAmount], [IsDeleted], [StudentID]) VALUES (53, 4, CAST(708000.00 AS Decimal(15, 2)), CAST(N'2025-01-05T18:07:29.107' AS DateTime), N'Cash', N'Payment for book rental', CAST(1234567.00 AS Decimal(15, 2)), CAST(708000.00 AS Decimal(15, 2)), CAST(526567.00 AS Decimal(15, 2)), 0, 79)
INSERT [dbo].[Payments] ([PaymentID], [UserID], [Amount], [PaymentDate], [PaymentMethod], [Description], [AmountGiven], [TotalOrderAmount], [ChangeAmount], [IsDeleted], [StudentID]) VALUES (54, 4, CAST(90000.00 AS Decimal(15, 2)), CAST(N'2025-01-05T18:09:46.150' AS DateTime), N'Cash', N'Payment for book rental', CAST(111111.00 AS Decimal(15, 2)), CAST(90000.00 AS Decimal(15, 2)), CAST(21111.00 AS Decimal(15, 2)), 0, 79)
INSERT [dbo].[Payments] ([PaymentID], [UserID], [Amount], [PaymentDate], [PaymentMethod], [Description], [AmountGiven], [TotalOrderAmount], [ChangeAmount], [IsDeleted], [StudentID]) VALUES (55, 4, CAST(208000.00 AS Decimal(15, 2)), CAST(N'2025-01-05T21:08:08.880' AS DateTime), N'Cash', N'Payment for book rental', CAST(500000.00 AS Decimal(15, 2)), CAST(208000.00 AS Decimal(15, 2)), CAST(292000.00 AS Decimal(15, 2)), 0, 65)
INSERT [dbo].[Payments] ([PaymentID], [UserID], [Amount], [PaymentDate], [PaymentMethod], [Description], [AmountGiven], [TotalOrderAmount], [ChangeAmount], [IsDeleted], [StudentID]) VALUES (56, 4, CAST(30000.00 AS Decimal(15, 2)), CAST(N'2025-01-05T21:28:58.160' AS DateTime), N'Cash', N'Payment for book rental', CAST(44444.00 AS Decimal(15, 2)), CAST(30000.00 AS Decimal(15, 2)), CAST(14444.00 AS Decimal(15, 2)), 0, 65)
INSERT [dbo].[Payments] ([PaymentID], [UserID], [Amount], [PaymentDate], [PaymentMethod], [Description], [AmountGiven], [TotalOrderAmount], [ChangeAmount], [IsDeleted], [StudentID]) VALUES (57, 4, CAST(30000.00 AS Decimal(15, 2)), CAST(N'2025-01-05T21:29:27.677' AS DateTime), N'Cash', N'Payment for book rental', CAST(77777.00 AS Decimal(15, 2)), CAST(30000.00 AS Decimal(15, 2)), CAST(47777.00 AS Decimal(15, 2)), 0, 67)
INSERT [dbo].[Payments] ([PaymentID], [UserID], [Amount], [PaymentDate], [PaymentMethod], [Description], [AmountGiven], [TotalOrderAmount], [ChangeAmount], [IsDeleted], [StudentID]) VALUES (58, 4, CAST(156000.00 AS Decimal(15, 2)), CAST(N'2025-01-05T21:36:42.313' AS DateTime), N'Cash', N'Payment for book rental', CAST(400000.00 AS Decimal(15, 2)), CAST(156000.00 AS Decimal(15, 2)), CAST(244000.00 AS Decimal(15, 2)), 0, 64)
INSERT [dbo].[Payments] ([PaymentID], [UserID], [Amount], [PaymentDate], [PaymentMethod], [Description], [AmountGiven], [TotalOrderAmount], [ChangeAmount], [IsDeleted], [StudentID]) VALUES (59, 4, CAST(169000.00 AS Decimal(15, 2)), CAST(N'2025-01-05T23:18:27.770' AS DateTime), N'Cash', N'Payment for book rental', CAST(300000.00 AS Decimal(15, 2)), CAST(169000.00 AS Decimal(15, 2)), CAST(131000.00 AS Decimal(15, 2)), 0, 68)
INSERT [dbo].[Payments] ([PaymentID], [UserID], [Amount], [PaymentDate], [PaymentMethod], [Description], [AmountGiven], [TotalOrderAmount], [ChangeAmount], [IsDeleted], [StudentID]) VALUES (60, 4, CAST(88000.00 AS Decimal(15, 2)), CAST(N'2025-01-06T23:35:52.340' AS DateTime), N'Cash', N'Payment for book rental', CAST(100000.00 AS Decimal(15, 2)), CAST(88000.00 AS Decimal(15, 2)), CAST(12000.00 AS Decimal(15, 2)), 0, 74)
INSERT [dbo].[Payments] ([PaymentID], [UserID], [Amount], [PaymentDate], [PaymentMethod], [Description], [AmountGiven], [TotalOrderAmount], [ChangeAmount], [IsDeleted], [StudentID]) VALUES (61, 4, CAST(120000.00 AS Decimal(15, 2)), CAST(N'2025-01-07T21:34:12.510' AS DateTime), N'Cash', N'Payment for book rental', CAST(200000.00 AS Decimal(15, 2)), CAST(120000.00 AS Decimal(15, 2)), CAST(80000.00 AS Decimal(15, 2)), 0, 79)
INSERT [dbo].[Payments] ([PaymentID], [UserID], [Amount], [PaymentDate], [PaymentMethod], [Description], [AmountGiven], [TotalOrderAmount], [ChangeAmount], [IsDeleted], [StudentID]) VALUES (62, 4, CAST(68000.00 AS Decimal(15, 2)), CAST(N'2025-01-07T22:01:59.683' AS DateTime), N'Cash', N'Payment for book rental', CAST(68000.00 AS Decimal(15, 2)), CAST(68000.00 AS Decimal(15, 2)), CAST(0.00 AS Decimal(15, 2)), 0, 74)
INSERT [dbo].[Payments] ([PaymentID], [UserID], [Amount], [PaymentDate], [PaymentMethod], [Description], [AmountGiven], [TotalOrderAmount], [ChangeAmount], [IsDeleted], [StudentID]) VALUES (63, 4, CAST(35253826.00 AS Decimal(15, 2)), CAST(N'2025-01-07T22:07:28.460' AS DateTime), N'Cash', N'Payment for book rental', CAST(352353111.00 AS Decimal(15, 2)), CAST(35253826.00 AS Decimal(15, 2)), CAST(317099285.00 AS Decimal(15, 2)), 0, 67)
INSERT [dbo].[Payments] ([PaymentID], [UserID], [Amount], [PaymentDate], [PaymentMethod], [Description], [AmountGiven], [TotalOrderAmount], [ChangeAmount], [IsDeleted], [StudentID]) VALUES (64, 4, CAST(49942920.00 AS Decimal(15, 2)), CAST(N'2025-01-07T22:08:16.577' AS DateTime), N'Cash', N'Payment for book rental', CAST(111111111.00 AS Decimal(15, 2)), CAST(49942920.00 AS Decimal(15, 2)), CAST(61168191.00 AS Decimal(15, 2)), 0, 69)
INSERT [dbo].[Payments] ([PaymentID], [UserID], [Amount], [PaymentDate], [PaymentMethod], [Description], [AmountGiven], [TotalOrderAmount], [ChangeAmount], [IsDeleted], [StudentID]) VALUES (65, 4, CAST(120000.00 AS Decimal(15, 2)), CAST(N'2025-01-07T22:19:42.953' AS DateTime), N'Cash', N'Payment for book rental', CAST(200000.00 AS Decimal(15, 2)), CAST(120000.00 AS Decimal(15, 2)), CAST(80000.00 AS Decimal(15, 2)), 0, 72)
INSERT [dbo].[Payments] ([PaymentID], [UserID], [Amount], [PaymentDate], [PaymentMethod], [Description], [AmountGiven], [TotalOrderAmount], [ChangeAmount], [IsDeleted], [StudentID]) VALUES (66, 4, CAST(3885196.00 AS Decimal(15, 2)), CAST(N'2025-01-08T17:42:58.597' AS DateTime), N'Cash', N'Payment for book rental', CAST(4000000.00 AS Decimal(15, 2)), CAST(3885196.00 AS Decimal(15, 2)), CAST(114804.00 AS Decimal(15, 2)), 0, 79)
INSERT [dbo].[Payments] ([PaymentID], [UserID], [Amount], [PaymentDate], [PaymentMethod], [Description], [AmountGiven], [TotalOrderAmount], [ChangeAmount], [IsDeleted], [StudentID]) VALUES (67, 4, CAST(120000.00 AS Decimal(15, 2)), CAST(N'2025-01-09T01:05:10.627' AS DateTime), N'Cash', N'Payment for book rental', CAST(200000.00 AS Decimal(15, 2)), CAST(120000.00 AS Decimal(15, 2)), CAST(80000.00 AS Decimal(15, 2)), 0, 72)
INSERT [dbo].[Payments] ([PaymentID], [UserID], [Amount], [PaymentDate], [PaymentMethod], [Description], [AmountGiven], [TotalOrderAmount], [ChangeAmount], [IsDeleted], [StudentID]) VALUES (68, 4, CAST(348000.00 AS Decimal(15, 2)), CAST(N'2025-01-09T01:22:35.863' AS DateTime), N'Cash', N'Payment for book rental', CAST(400000.00 AS Decimal(15, 2)), CAST(348000.00 AS Decimal(15, 2)), CAST(52000.00 AS Decimal(15, 2)), 0, 79)
INSERT [dbo].[Payments] ([PaymentID], [UserID], [Amount], [PaymentDate], [PaymentMethod], [Description], [AmountGiven], [TotalOrderAmount], [ChangeAmount], [IsDeleted], [StudentID]) VALUES (69, 21, CAST(358000.00 AS Decimal(15, 2)), CAST(N'2025-01-09T11:41:35.113' AS DateTime), N'Cash', N'Payment for book rental', CAST(358000.00 AS Decimal(15, 2)), CAST(358000.00 AS Decimal(15, 2)), CAST(0.00 AS Decimal(15, 2)), 0, 72)
INSERT [dbo].[Payments] ([PaymentID], [UserID], [Amount], [PaymentDate], [PaymentMethod], [Description], [AmountGiven], [TotalOrderAmount], [ChangeAmount], [IsDeleted], [StudentID]) VALUES (70, 21, CAST(88000.00 AS Decimal(15, 2)), CAST(N'2025-01-09T11:45:25.463' AS DateTime), N'Cash', N'Payment for book rental', CAST(88000.00 AS Decimal(15, 2)), CAST(88000.00 AS Decimal(15, 2)), CAST(0.00 AS Decimal(15, 2)), 0, 79)
INSERT [dbo].[Payments] ([PaymentID], [UserID], [Amount], [PaymentDate], [PaymentMethod], [Description], [AmountGiven], [TotalOrderAmount], [ChangeAmount], [IsDeleted], [StudentID]) VALUES (71, 21, CAST(103000.00 AS Decimal(15, 2)), CAST(N'2025-01-09T11:57:33.293' AS DateTime), N'Cash', N'Payment for book rental', CAST(110000.00 AS Decimal(15, 2)), CAST(103000.00 AS Decimal(15, 2)), CAST(7000.00 AS Decimal(15, 2)), 0, 74)
INSERT [dbo].[Payments] ([PaymentID], [UserID], [Amount], [PaymentDate], [PaymentMethod], [Description], [AmountGiven], [TotalOrderAmount], [ChangeAmount], [IsDeleted], [StudentID]) VALUES (72, 21, CAST(103000.00 AS Decimal(15, 2)), CAST(N'2025-01-09T11:58:54.893' AS DateTime), N'Cash', N'Payment for book rental', CAST(103000.00 AS Decimal(15, 2)), CAST(103000.00 AS Decimal(15, 2)), CAST(0.00 AS Decimal(15, 2)), 0, 68)
INSERT [dbo].[Payments] ([PaymentID], [UserID], [Amount], [PaymentDate], [PaymentMethod], [Description], [AmountGiven], [TotalOrderAmount], [ChangeAmount], [IsDeleted], [StudentID]) VALUES (73, 21, CAST(138000.00 AS Decimal(15, 2)), CAST(N'2025-01-09T12:04:53.290' AS DateTime), N'Cash', N'Payment for book rental', CAST(138000.00 AS Decimal(15, 2)), CAST(138000.00 AS Decimal(15, 2)), CAST(0.00 AS Decimal(15, 2)), 0, 67)
INSERT [dbo].[Payments] ([PaymentID], [UserID], [Amount], [PaymentDate], [PaymentMethod], [Description], [AmountGiven], [TotalOrderAmount], [ChangeAmount], [IsDeleted], [StudentID]) VALUES (74, 21, CAST(88000.00 AS Decimal(15, 2)), CAST(N'2025-01-09T12:12:42.397' AS DateTime), N'Cash', N'Payment for book rental', CAST(88000.00 AS Decimal(15, 2)), CAST(88000.00 AS Decimal(15, 2)), CAST(0.00 AS Decimal(15, 2)), 0, 75)
INSERT [dbo].[Payments] ([PaymentID], [UserID], [Amount], [PaymentDate], [PaymentMethod], [Description], [AmountGiven], [TotalOrderAmount], [ChangeAmount], [IsDeleted], [StudentID]) VALUES (75, 4, CAST(68000.00 AS Decimal(15, 2)), CAST(N'2025-01-09T12:18:58.757' AS DateTime), N'Cash', N'Payment for book rental', CAST(68000.00 AS Decimal(15, 2)), CAST(68000.00 AS Decimal(15, 2)), CAST(0.00 AS Decimal(15, 2)), 0, 79)
INSERT [dbo].[Payments] ([PaymentID], [UserID], [Amount], [PaymentDate], [PaymentMethod], [Description], [AmountGiven], [TotalOrderAmount], [ChangeAmount], [IsDeleted], [StudentID]) VALUES (76, 4, CAST(81000.00 AS Decimal(15, 2)), CAST(N'2025-01-09T12:21:19.560' AS DateTime), N'Cash', N'Payment for book rental', CAST(81000.00 AS Decimal(15, 2)), CAST(81000.00 AS Decimal(15, 2)), CAST(0.00 AS Decimal(15, 2)), 0, 79)
INSERT [dbo].[Payments] ([PaymentID], [UserID], [Amount], [PaymentDate], [PaymentMethod], [Description], [AmountGiven], [TotalOrderAmount], [ChangeAmount], [IsDeleted], [StudentID]) VALUES (77, 4, CAST(81000.00 AS Decimal(15, 2)), CAST(N'2025-01-09T12:26:08.523' AS DateTime), N'Cash', N'Payment for book rental', CAST(333333.00 AS Decimal(15, 2)), CAST(81000.00 AS Decimal(15, 2)), CAST(252333.00 AS Decimal(15, 2)), 0, 79)
INSERT [dbo].[Payments] ([PaymentID], [UserID], [Amount], [PaymentDate], [PaymentMethod], [Description], [AmountGiven], [TotalOrderAmount], [ChangeAmount], [IsDeleted], [StudentID]) VALUES (78, 21, CAST(81000.00 AS Decimal(15, 2)), CAST(N'2025-01-09T12:30:23.330' AS DateTime), N'Cash', N'Payment for book rental', CAST(6666666666.00 AS Decimal(15, 2)), CAST(81000.00 AS Decimal(15, 2)), CAST(6666585666.00 AS Decimal(15, 2)), 0, 79)
INSERT [dbo].[Payments] ([PaymentID], [UserID], [Amount], [PaymentDate], [PaymentMethod], [Description], [AmountGiven], [TotalOrderAmount], [ChangeAmount], [IsDeleted], [StudentID]) VALUES (79, 21, CAST(456000.00 AS Decimal(15, 2)), CAST(N'2025-01-09T12:49:31.677' AS DateTime), N'Cash', N'Payment for book rental', CAST(500000.00 AS Decimal(15, 2)), CAST(456000.00 AS Decimal(15, 2)), CAST(44000.00 AS Decimal(15, 2)), 0, 67)
INSERT [dbo].[Payments] ([PaymentID], [UserID], [Amount], [PaymentDate], [PaymentMethod], [Description], [AmountGiven], [TotalOrderAmount], [ChangeAmount], [IsDeleted], [StudentID]) VALUES (80, 21, CAST(81000.00 AS Decimal(15, 2)), CAST(N'2025-01-09T13:41:50.500' AS DateTime), N'Cash', N'Payment for book rental', CAST(200000.00 AS Decimal(15, 2)), CAST(81000.00 AS Decimal(15, 2)), CAST(119000.00 AS Decimal(15, 2)), 0, 79)
INSERT [dbo].[Payments] ([PaymentID], [UserID], [Amount], [PaymentDate], [PaymentMethod], [Description], [AmountGiven], [TotalOrderAmount], [ChangeAmount], [IsDeleted], [StudentID]) VALUES (81, 21, CAST(90000.00 AS Decimal(15, 2)), CAST(N'2025-01-09T14:22:05.027' AS DateTime), N'Cash', N'Payment for book rental', CAST(100000.00 AS Decimal(15, 2)), CAST(90000.00 AS Decimal(15, 2)), CAST(10000.00 AS Decimal(15, 2)), 0, 79)
SET IDENTITY_INSERT [dbo].[Payments] OFF
GO
SET IDENTITY_INSERT [dbo].[Publishers] ON 

INSERT [dbo].[Publishers] ([PublisherID], [Name], [Address], [PhoneNumber], [Email], [Website], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (1, N'NXB Kim Đồng', N'Hà Nội, Việt Nam', N'0123456789', N'kimdong@gmail.com', NULL, 0, CAST(N'2024-12-29T20:10:46.137' AS DateTime), 1, CAST(N'2024-12-29T20:10:46.137' AS DateTime), NULL)
INSERT [dbo].[Publishers] ([PublisherID], [Name], [Address], [PhoneNumber], [Email], [Website], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (2, N'NXB Giáo Dục Việt Nam', N'Hà Nội, Việt Nam', N'0987654321', N'gdvn@gmail.com', NULL, 0, CAST(N'2024-12-29T20:10:46.137' AS DateTime), 1, CAST(N'2024-12-29T20:10:46.137' AS DateTime), NULL)
INSERT [dbo].[Publishers] ([PublisherID], [Name], [Address], [PhoneNumber], [Email], [Website], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (3, N'NXB Trẻ', N'Hồ Chí Minh, Việt Nam', N'0321654987', N'trepub@gmail.com', NULL, 0, CAST(N'2024-12-29T20:10:46.137' AS DateTime), 1, CAST(N'2024-12-29T20:10:46.137' AS DateTime), NULL)
INSERT [dbo].[Publishers] ([PublisherID], [Name], [Address], [PhoneNumber], [Email], [Website], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (4, N'NXB Văn Học', N'Hà Nội, Việt Nam', N'0912345678', N'vanhocpub@gmail.com', NULL, 0, CAST(N'2024-12-29T20:10:46.137' AS DateTime), 1, CAST(N'2024-12-29T20:10:46.137' AS DateTime), NULL)
INSERT [dbo].[Publishers] ([PublisherID], [Name], [Address], [PhoneNumber], [Email], [Website], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (5, N'NXB Chính Trị Quốc Gia', N'Hà Nội, Việt Nam', N'0909090909', N'chinhtri@gmail.com', NULL, 0, CAST(N'2024-12-29T20:10:46.137' AS DateTime), 1, CAST(N'2024-12-29T20:10:46.137' AS DateTime), NULL)
INSERT [dbo].[Publishers] ([PublisherID], [Name], [Address], [PhoneNumber], [Email], [Website], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (6, N'Publisher 1', N'Address 1', N'1234567890', N'publisher1@example.com', NULL, 1, CAST(N'2025-01-03T14:42:11.973' AS DateTime), 1, CAST(N'2025-01-07T11:48:36.080' AS DateTime), 1)
INSERT [dbo].[Publishers] ([PublisherID], [Name], [Address], [PhoneNumber], [Email], [Website], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (7, N'Publisher 2', N'Address 2', N'1234567891', N'publisher2@example.com', NULL, 1, CAST(N'2025-01-03T14:42:11.973' AS DateTime), 1, CAST(N'2025-01-03T14:42:11.973' AS DateTime), 1)
INSERT [dbo].[Publishers] ([PublisherID], [Name], [Address], [PhoneNumber], [Email], [Website], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (8, N'Publisher 3', N'Address 3', N'1234567892', N'publisher3@example.com', NULL, 1, CAST(N'2025-01-03T14:42:11.973' AS DateTime), 1, CAST(N'2025-01-03T14:42:11.973' AS DateTime), 1)
INSERT [dbo].[Publishers] ([PublisherID], [Name], [Address], [PhoneNumber], [Email], [Website], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (9, N'Publisher 4', N'Address 4', N'1234567893', N'publisher4@example.com', NULL, 1, CAST(N'2025-01-03T14:42:11.973' AS DateTime), 1, CAST(N'2025-01-03T14:42:11.973' AS DateTime), 1)
INSERT [dbo].[Publishers] ([PublisherID], [Name], [Address], [PhoneNumber], [Email], [Website], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (10, N'Publisher 5', N'Address 5', N'1234567894', N'publisher5@example.com', NULL, 1, CAST(N'2025-01-03T14:42:11.973' AS DateTime), 1, CAST(N'2025-01-03T14:42:11.973' AS DateTime), 1)
INSERT [dbo].[Publishers] ([PublisherID], [Name], [Address], [PhoneNumber], [Email], [Website], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (11, N'Publisher 6', N'Address 6', N'1234567895', N'publisher6@example.com', NULL, 1, CAST(N'2025-01-03T14:42:11.973' AS DateTime), 1, CAST(N'2025-01-03T14:42:11.973' AS DateTime), 1)
INSERT [dbo].[Publishers] ([PublisherID], [Name], [Address], [PhoneNumber], [Email], [Website], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (12, N'Publisher 7', N'Address 7', N'1234567896', N'publisher7@example.com', NULL, 1, CAST(N'2025-01-03T14:42:11.973' AS DateTime), 1, CAST(N'2025-01-03T14:42:11.973' AS DateTime), 1)
INSERT [dbo].[Publishers] ([PublisherID], [Name], [Address], [PhoneNumber], [Email], [Website], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (13, N'Publisher 8', N'Address 8', N'1234567897', N'publisher8@example.com', NULL, 1, CAST(N'2025-01-03T14:42:11.973' AS DateTime), 1, CAST(N'2025-01-03T14:42:11.973' AS DateTime), 1)
INSERT [dbo].[Publishers] ([PublisherID], [Name], [Address], [PhoneNumber], [Email], [Website], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (14, N'Publisher 9', N'Address 9', N'1234567898', N'publisher9@example.com', NULL, 1, CAST(N'2025-01-03T14:42:11.973' AS DateTime), 1, CAST(N'2025-01-03T14:42:11.973' AS DateTime), 1)
INSERT [dbo].[Publishers] ([PublisherID], [Name], [Address], [PhoneNumber], [Email], [Website], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (15, N'Publisher 10', N'Address 10', N'1234567899', N'publisher10@example.com', NULL, 1, CAST(N'2025-01-03T14:42:11.973' AS DateTime), 1, CAST(N'2025-01-03T14:42:11.973' AS DateTime), 1)
INSERT [dbo].[Publishers] ([PublisherID], [Name], [Address], [PhoneNumber], [Email], [Website], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (16, N'NXB Trẻ 12', N'tphcm', N'09123456789', N'chinhtri1@gmail.com', NULL, 1, CAST(N'2025-01-07T11:18:20.817' AS DateTime), NULL, CAST(N'2025-01-09T16:30:35.323' AS DateTime), 1)
INSERT [dbo].[Publishers] ([PublisherID], [Name], [Address], [PhoneNumber], [Email], [Website], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy]) VALUES (17, N'sdfsdf', N'gdfgd', N'09876543', N'dsfsdfs@gmail.com', NULL, 1, CAST(N'2025-01-07T12:15:04.683' AS DateTime), NULL, CAST(N'2025-01-07T12:15:37.600' AS DateTime), 1)
SET IDENTITY_INSERT [dbo].[Publishers] OFF
GO
SET IDENTITY_INSERT [dbo].[Students] ON 

INSERT [dbo].[Students] ([StudentID], [FullName], [DateOfBirth], [Gender], [Email], [PhoneNumber], [Address], [Avatar], [EnrollmentYear], [SchoolName], [UserID], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [TotalBooksRented], [LateReturnsCount], [DamagedBooksCount], [TotalOrders], [StudentCode], [GraduationYear]) VALUES (58, N'Nguyen Van A', CAST(N'2001-01-01' AS Date), N'Male', N'nguyenvana@example.com', N'0987654321', N'123 Le Loi, Hanoi', N'avaaa.png', 2020, N'Hanoi University', 2, 1, CAST(N'2024-12-24T19:02:35.027' AS DateTime), 2, CAST(N'2024-12-24T19:02:35.027' AS DateTime), 2, 0, 0, 0, 0, N'S12345', 2025)
INSERT [dbo].[Students] ([StudentID], [FullName], [DateOfBirth], [Gender], [Email], [PhoneNumber], [Address], [Avatar], [EnrollmentYear], [SchoolName], [UserID], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [TotalBooksRented], [LateReturnsCount], [DamagedBooksCount], [TotalOrders], [StudentCode], [GraduationYear]) VALUES (60, N'Le Van C', CAST(N'2000-03-03' AS Date), N'Male', N'levanc@example.com', N'0987654323', N'789 Nguyen Trai, Ho Chi Minh City', N'1735714817070.png', 2022, N'HCMC University', 1, 0, CAST(N'2024-12-24T19:02:35.027' AS DateTime), 2, CAST(N'2025-01-01T14:00:18.053' AS DateTime), 4, 0, 0, 0, 0, N'S12347', 2025)
INSERT [dbo].[Students] ([StudentID], [FullName], [DateOfBirth], [Gender], [Email], [PhoneNumber], [Address], [Avatar], [EnrollmentYear], [SchoolName], [UserID], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [TotalBooksRented], [LateReturnsCount], [DamagedBooksCount], [TotalOrders], [StudentCode], [GraduationYear]) VALUES (61, N'Pham Thi D', CAST(N'2001-04-04' AS Date), N'Female', N'phamthid@example.com', N'0987654324', N'321 Hai Ba Trung, Da Nang', N'1735714808524.png', 2020, N'Da Nang University', 3, 0, CAST(N'2024-12-24T19:02:35.027' AS DateTime), 2, CAST(N'2025-01-01T14:00:09.490' AS DateTime), 4, 0, 0, 0, 0, N'S12348', 2025)
INSERT [dbo].[Students] ([StudentID], [FullName], [DateOfBirth], [Gender], [Email], [PhoneNumber], [Address], [Avatar], [EnrollmentYear], [SchoolName], [UserID], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [TotalBooksRented], [LateReturnsCount], [DamagedBooksCount], [TotalOrders], [StudentCode], [GraduationYear]) VALUES (62, N'Hoang Van E', CAST(N'2002-05-05' AS Date), N'Male', N'hoangvane@example.com', N'0987654325', N'654 Nguyen Du, Hue', N'1735714793602.png', 2021, N'Hue University', 2, 0, CAST(N'2024-12-24T19:02:35.027' AS DateTime), 2, CAST(N'2025-01-01T13:59:54.590' AS DateTime), 4, 0, 0, 0, 0, N'S12349', 2025)
INSERT [dbo].[Students] ([StudentID], [FullName], [DateOfBirth], [Gender], [Email], [PhoneNumber], [Address], [Avatar], [EnrollmentYear], [SchoolName], [UserID], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [TotalBooksRented], [LateReturnsCount], [DamagedBooksCount], [TotalOrders], [StudentCode], [GraduationYear]) VALUES (63, N'Vo Thi F', CAST(N'2003-06-06' AS Date), N'Female', N'vothif@example.com', N'0987654326', N'987 Le Thanh Ton, HCMC', N'1735714783105.png', 2022, N'HCMC University', 3, 0, CAST(N'2024-12-24T19:02:35.027' AS DateTime), 3, CAST(N'2025-01-01T13:59:44.270' AS DateTime), 4, 0, 0, 0, 0, N'S12350', 2025)
INSERT [dbo].[Students] ([StudentID], [FullName], [DateOfBirth], [Gender], [Email], [PhoneNumber], [Address], [Avatar], [EnrollmentYear], [SchoolName], [UserID], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [TotalBooksRented], [LateReturnsCount], [DamagedBooksCount], [TotalOrders], [StudentCode], [GraduationYear]) VALUES (64, N'Nguyen Van G', CAST(N'2004-07-07' AS Date), N'Male', N'nguyenvang@example.com', N'0987654327', N'123 Pham Van Dong, Nha Trang', N'1735714774368.png', 2023, N'Nha Trang University', 2, 0, CAST(N'2024-12-24T19:02:35.027' AS DateTime), 3, CAST(N'2025-01-05T21:36:42.760' AS DateTime), 4, 3, 0, 0, 1, N'S12351', 2025)
INSERT [dbo].[Students] ([StudentID], [FullName], [DateOfBirth], [Gender], [Email], [PhoneNumber], [Address], [Avatar], [EnrollmentYear], [SchoolName], [UserID], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [TotalBooksRented], [LateReturnsCount], [DamagedBooksCount], [TotalOrders], [StudentCode], [GraduationYear]) VALUES (65, N'Tran Thi H', CAST(N'2001-08-08' AS Date), N'Female', N'tranthih@example.com', N'0987654328', N'456 Tran Phu, Hanoi', N'1735714763762.png', 2020, N'Hanoi University', 2, 0, CAST(N'2024-12-24T19:02:35.027' AS DateTime), 3, CAST(N'2025-01-09T01:08:38.790' AS DateTime), 4, 4, 0, 1, 2, N'S12352', 2025)
INSERT [dbo].[Students] ([StudentID], [FullName], [DateOfBirth], [Gender], [Email], [PhoneNumber], [Address], [Avatar], [EnrollmentYear], [SchoolName], [UserID], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [TotalBooksRented], [LateReturnsCount], [DamagedBooksCount], [TotalOrders], [StudentCode], [GraduationYear]) VALUES (66, N'Le Van I', CAST(N'2002-09-09' AS Date), N'Male', N'levani@example.com', N'0987654329', N'789 Kim Ma, Hanoi', N'1735714754608.png', 2021, N'Hanoi University', 3, 0, CAST(N'2024-12-24T19:02:35.027' AS DateTime), 3, CAST(N'2025-01-09T11:39:00.780' AS DateTime), 21, 0, 2, 0, 0, N'S12353', 2025)
INSERT [dbo].[Students] ([StudentID], [FullName], [DateOfBirth], [Gender], [Email], [PhoneNumber], [Address], [Avatar], [EnrollmentYear], [SchoolName], [UserID], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [TotalBooksRented], [LateReturnsCount], [DamagedBooksCount], [TotalOrders], [StudentCode], [GraduationYear]) VALUES (67, N'Pham Thi J', CAST(N'2003-10-10' AS Date), N'Female', N'phamthij@example.com', N'0987654330', N'321 Dien Bien Phu, Ho Chi Minh City', N'avaaa.png', 2022, N'HCMC University', 2, 0, CAST(N'2024-12-24T19:02:35.027' AS DateTime), 3, CAST(N'2025-01-09T12:50:47.607' AS DateTime), 21, 4, 0, 2, 4, N'S12354', 2025)
INSERT [dbo].[Students] ([StudentID], [FullName], [DateOfBirth], [Gender], [Email], [PhoneNumber], [Address], [Avatar], [EnrollmentYear], [SchoolName], [UserID], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [TotalBooksRented], [LateReturnsCount], [DamagedBooksCount], [TotalOrders], [StudentCode], [GraduationYear]) VALUES (68, N'Nguyen Van K', CAST(N'2004-11-11' AS Date), N'Male', N'nguyenvank@example.com', N'0987654331', N'654 Bach Dang, Da Nang', N'1735714743270.png', 2023, N'Da Nang University', 2, 0, CAST(N'2024-12-24T19:02:35.027' AS DateTime), 3, CAST(N'2025-01-09T11:58:55.013' AS DateTime), 21, 3, 1, 1, 2, N'S12355', 2025)
INSERT [dbo].[Students] ([StudentID], [FullName], [DateOfBirth], [Gender], [Email], [PhoneNumber], [Address], [Avatar], [EnrollmentYear], [SchoolName], [UserID], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [TotalBooksRented], [LateReturnsCount], [DamagedBooksCount], [TotalOrders], [StudentCode], [GraduationYear]) VALUES (69, N'Tran Thi L', CAST(N'2001-12-12' AS Date), N'Female', N'tranthil@example.com', N'0987654332', N'987 Nguyen Chi Thanh, Hue', N'1735714731200.png', 2020, N'Hue University', 3, 0, CAST(N'2024-12-24T19:02:35.027' AS DateTime), 2, CAST(N'2025-01-07T22:08:16.763' AS DateTime), 4, 1, 0, 0, 1, N'S12356', 2025)
INSERT [dbo].[Students] ([StudentID], [FullName], [DateOfBirth], [Gender], [Email], [PhoneNumber], [Address], [Avatar], [EnrollmentYear], [SchoolName], [UserID], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [TotalBooksRented], [LateReturnsCount], [DamagedBooksCount], [TotalOrders], [StudentCode], [GraduationYear]) VALUES (72, N'Hoang Van O', CAST(N'2004-03-15' AS Date), N'Male', N'hoangvano@example.com', N'0987654335', N'789 Tran Quoc Toan, Hanoi', N'1735714718913.png', 2023, N'Hanoi University', 1, 0, CAST(N'2024-12-24T19:02:35.027' AS DateTime), 2, CAST(N'2025-01-09T11:41:35.333' AS DateTime), 21, 4, 0, 1, 3, N'S12359', 2025)
INSERT [dbo].[Students] ([StudentID], [FullName], [DateOfBirth], [Gender], [Email], [PhoneNumber], [Address], [Avatar], [EnrollmentYear], [SchoolName], [UserID], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [TotalBooksRented], [LateReturnsCount], [DamagedBooksCount], [TotalOrders], [StudentCode], [GraduationYear]) VALUES (74, N'Nguyen Van Q', CAST(N'2002-05-17' AS Date), N'Male', N'nguyenvanq@example.com', N'0987654337', N'654 Tran Nhan Tong, HCMC', N'2.png', 2021, N'HCMC University', 2, 0, CAST(N'2024-12-24T19:02:35.027' AS DateTime), 2, CAST(N'2025-01-09T11:57:33.430' AS DateTime), 21, 3, 0, 1, 3, N'S12361', 2025)
INSERT [dbo].[Students] ([StudentID], [FullName], [DateOfBirth], [Gender], [Email], [PhoneNumber], [Address], [Avatar], [EnrollmentYear], [SchoolName], [UserID], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [TotalBooksRented], [LateReturnsCount], [DamagedBooksCount], [TotalOrders], [StudentCode], [GraduationYear]) VALUES (75, N'Tran Thi R', CAST(N'2003-06-18' AS Date), N'Male', N'tranthir@example.com', N'0987654338', N'987 Tran Hung Dao, Nha Trang', N'3.png', 2022, N'Nha Trang University', 1, 0, CAST(N'2024-12-24T19:02:35.027' AS DateTime), 2, CAST(N'2025-01-09T12:12:42.527' AS DateTime), 21, 1, 0, 0, 1, N'S12362', 2025)
INSERT [dbo].[Students] ([StudentID], [FullName], [DateOfBirth], [Gender], [Email], [PhoneNumber], [Address], [Avatar], [EnrollmentYear], [SchoolName], [UserID], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [TotalBooksRented], [LateReturnsCount], [DamagedBooksCount], [TotalOrders], [StudentCode], [GraduationYear]) VALUES (79, N'le van canh', CAST(N'2003-10-10' AS Date), N'Male', N'qrtyu@gmail.com', N'012345678', N'bac lieu', N'1735210945307.png', 2023, N'aptech', 1, 0, CAST(N'2024-12-26T17:51:41.567' AS DateTime), NULL, CAST(N'2025-01-09T14:22:05.510' AS DateTime), 21, 19, 2, 3, 12, N'ST12344', 2025)
INSERT [dbo].[Students] ([StudentID], [FullName], [DateOfBirth], [Gender], [Email], [PhoneNumber], [Address], [Avatar], [EnrollmentYear], [SchoolName], [UserID], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [TotalBooksRented], [LateReturnsCount], [DamagedBooksCount], [TotalOrders], [StudentCode], [GraduationYear]) VALUES (82, N'lê văn cảnhh', CAST(N'1993-09-09' AS Date), N'Male', N'asdfgh@gmail.com', N'0123456789', N'dsad đá', N'1735714703079.png', 2025, N'đâsdsd', 1, 0, CAST(N'2024-12-26T18:07:00.540' AS DateTime), NULL, CAST(N'2025-01-09T13:08:27.890' AS DateTime), 21, 0, 12, 66, 0, N'ST12345', 2023)
SET IDENTITY_INSERT [dbo].[Students] OFF
GO
SET IDENTITY_INSERT [dbo].[Users] ON 

INSERT [dbo].[Users] ([UserID], [FullName], [Username], [Password], [Email], [PhoneNumber], [Address], [UserRole], [Avatar], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [IsActive]) VALUES (1, N'Admin User', N'admin', N'password123', N'admin@example.com', N'1234567890', N'123 Main St', 2, N'1735714670635.png', 0, CAST(N'2024-12-24T15:39:51.830' AS DateTime), NULL, CAST(N'2024-12-24T15:39:51.830' AS DateTime), NULL, 1)
INSERT [dbo].[Users] ([UserID], [FullName], [Username], [Password], [Email], [PhoneNumber], [Address], [UserRole], [Avatar], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [IsActive]) VALUES (2, N'John Doe', N'johndoe', N'password123', N'john.doe@example.com', N'0987654321', N'456 Secondary St', 1, N'1735830832348.png', 0, CAST(N'2024-12-24T15:39:51.830' AS DateTime), NULL, CAST(N'2024-12-24T15:39:51.830' AS DateTime), NULL, 1)
INSERT [dbo].[Users] ([UserID], [FullName], [Username], [Password], [Email], [PhoneNumber], [Address], [UserRole], [Avatar], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [IsActive]) VALUES (3, N'Jane Smith', N'janesmith', N'password123', N'jane.smith@example.com', N'0123456789', N'789 Tertiary St', 1, N'1735714670635.png', 0, CAST(N'2024-12-24T15:39:51.830' AS DateTime), NULL, CAST(N'2024-12-24T15:39:51.830' AS DateTime), NULL, 0)
INSERT [dbo].[Users] ([UserID], [FullName], [Username], [Password], [Email], [PhoneNumber], [Address], [UserRole], [Avatar], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [IsActive]) VALUES (4, N'Le Van Canh', N'canh123', N'$2a$10$7fGwz7b/jxdK1Oa1dz65je69UgCz0p0Yjizi3NuKdyV46X.G.V0dC', N'aplevancanh1993@gmail.com', N'0123456788', N'tphcm', 2, N'1735714670635.png', 0, CAST(N'2025-01-01T10:19:35.343' AS DateTime), NULL, CAST(N'2025-01-01T10:19:35.343' AS DateTime), NULL, 1)
INSERT [dbo].[Users] ([UserID], [FullName], [Username], [Password], [Email], [PhoneNumber], [Address], [UserRole], [Avatar], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [IsActive]) VALUES (5, N'Le Van A', N'nhan vien', N'$2a$10$yVl9m2SwHxyGMi6Lm9edbuwqRghz66cwH2MVHzmes/2hL40uUfb4q', N'qqq@gmail.com', N'0987654321', N'tphcm', 1, N'1735830832348.png', 0, CAST(N'2025-01-02T22:11:08.197' AS DateTime), NULL, CAST(N'2025-01-02T22:11:08.197' AS DateTime), NULL, 1)
INSERT [dbo].[Users] ([UserID], [FullName], [Username], [Password], [Email], [PhoneNumber], [Address], [UserRole], [Avatar], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [IsActive]) VALUES (7, N'canh ne', N'', N'$2a$10$gPE1Kirqt0CU2u1k4r1tDuszsiFACoSIz7iz4tljymMjxnYkJx3N2', N'abc@gmail.com', N'0987654456', N'tphcm', 1, N'1735830832348.png', 0, CAST(N'2025-01-04T18:04:15.657' AS DateTime), NULL, CAST(N'2025-01-04T18:04:15.657' AS DateTime), NULL, 0)
INSERT [dbo].[Users] ([UserID], [FullName], [Username], [Password], [Email], [PhoneNumber], [Address], [UserRole], [Avatar], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [IsActive]) VALUES (12, N'canh ne nè', N'canh ne nè', N'$2a$10$9hCvH52kegR3BT58Ef3pOOzyUxCv.Ey2eJ9CAGrL9byh8hKQGSc/O', N'abcd@gmail.com', N'2345676545', N'tphcm', 1, N'1735830832348.png', 0, CAST(N'2025-01-04T18:15:58.120' AS DateTime), NULL, CAST(N'2025-01-04T18:15:58.120' AS DateTime), NULL, 0)
INSERT [dbo].[Users] ([UserID], [FullName], [Username], [Password], [Email], [PhoneNumber], [Address], [UserRole], [Avatar], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [IsActive]) VALUES (13, N'a', N'a', N'$2a$10$vXaLzkYtu4i1IMGGsLeujuFiSaXiN1RG.kJw3j0bbpo7lribWET5K', N'aaaa@gmail.com', N'5556677888', N'tphcm', 1, N'1735830832348.png', 0, CAST(N'2025-01-04T18:20:59.697' AS DateTime), NULL, CAST(N'2025-01-04T18:20:59.697' AS DateTime), NULL, 0)
INSERT [dbo].[Users] ([UserID], [FullName], [Username], [Password], [Email], [PhoneNumber], [Address], [UserRole], [Avatar], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [IsActive]) VALUES (14, N'canh canh canh', N'canh canh canh', N'$2a$10$Sq9eM10oNBAQsC9Ajs.zveIXdfWUcTWNWjkZ2SWwGJFEeCXPyRzs6', N'abaaa@gmail.com', N'3456765445', N'tphcm', 1, N'1735830832348.png', 0, CAST(N'2025-01-04T18:25:13.977' AS DateTime), NULL, CAST(N'2025-01-04T18:25:13.977' AS DateTime), NULL, 1)
INSERT [dbo].[Users] ([UserID], [FullName], [Username], [Password], [Email], [PhoneNumber], [Address], [UserRole], [Avatar], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [IsActive]) VALUES (18, N'Full Name Nga', N'abc@example.com', N'$2a$10$tjmpgjVlr42FsO3/fglqr..3K8tpyAOGt8seo7vZhJeqgNzjB5OHq', N'abc@example.com', N'tphcm', N'', 1, N'', 0, CAST(N'2025-01-05T11:19:01.773' AS DateTime), NULL, CAST(N'2025-01-05T11:19:01.773' AS DateTime), NULL, 0)
INSERT [dbo].[Users] ([UserID], [FullName], [Username], [Password], [Email], [PhoneNumber], [Address], [UserRole], [Avatar], [IsDeleted], [CreatedAt], [CreatedBy], [UpdatedAt], [UpdatedBy], [IsActive]) VALUES (21, N'a đê min nè', N'admin@gmail.com', N'$2a$10$3Ubbh48/MajeydqaWcXCM.vj00Ikz/49vGLw.oXB6VoXvX8ogm2JW', N'admin@gmail.com', N'0909090909', N'D5, Bình Thạnh', 2, N'1736394361174.png', 0, CAST(N'2025-01-09T10:42:33.753' AS DateTime), NULL, CAST(N'2025-01-09T10:46:04.670' AS DateTime), 0, 1)
SET IDENTITY_INSERT [dbo].[Users] OFF
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [UQ__BookCopi__BB96DE6F676801EF]    Script Date: 1/9/2025 5:10:41 PM ******/
ALTER TABLE [dbo].[BookCopies] ADD UNIQUE NONCLUSTERED 
(
	[UniqueCode] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [UQ__Books__447D36EA11CE67D9]    Script Date: 1/9/2025 5:10:41 PM ******/
ALTER TABLE [dbo].[Books] ADD UNIQUE NONCLUSTERED 
(
	[ISBN] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [UQ__Categori__8517B2E03332CBD8]    Script Date: 1/9/2025 5:10:41 PM ******/
ALTER TABLE [dbo].[Categories] ADD UNIQUE NONCLUSTERED 
(
	[CategoryName] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [UQ__Publishe__737584F63C71DB9E]    Script Date: 1/9/2025 5:10:41 PM ******/
ALTER TABLE [dbo].[Publishers] ADD UNIQUE NONCLUSTERED 
(
	[Name] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [UQ__Students__A9D1053463E2B512]    Script Date: 1/9/2025 5:10:41 PM ******/
ALTER TABLE [dbo].[Students] ADD UNIQUE NONCLUSTERED 
(
	[Email] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [UQ__Users__536C85E488289494]    Script Date: 1/9/2025 5:10:41 PM ******/
ALTER TABLE [dbo].[Users] ADD UNIQUE NONCLUSTERED 
(
	[Username] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
ALTER TABLE [dbo].[AuditLogs] ADD  DEFAULT (getdate()) FOR [ActionTime]
GO
ALTER TABLE [dbo].[Authors] ADD  DEFAULT ((0)) FOR [IsDeleted]
GO
ALTER TABLE [dbo].[Authors] ADD  DEFAULT (getdate()) FOR [CreatedAt]
GO
ALTER TABLE [dbo].[Authors] ADD  DEFAULT (getdate()) FOR [UpdatedAt]
GO
ALTER TABLE [dbo].[BookCopies] ADD  DEFAULT ('Available') FOR [Status]
GO
ALTER TABLE [dbo].[BookCopies] ADD  DEFAULT ((0)) FOR [IsDeleted]
GO
ALTER TABLE [dbo].[BookCopies] ADD  DEFAULT (getdate()) FOR [CreatedAt]
GO
ALTER TABLE [dbo].[BookCopies] ADD  DEFAULT (getdate()) FOR [UpdatedAt]
GO
ALTER TABLE [dbo].[BookReviews] ADD  DEFAULT (getdate()) FOR [ReviewDate]
GO
ALTER TABLE [dbo].[BookReviews] ADD  DEFAULT ((0)) FOR [IsDeleted]
GO
ALTER TABLE [dbo].[Books] ADD  DEFAULT ((0)) FOR [Quantity]
GO
ALTER TABLE [dbo].[Books] ADD  DEFAULT ((0)) FOR [StockQuantity]
GO
ALTER TABLE [dbo].[Books] ADD  DEFAULT ((0)) FOR [Price]
GO
ALTER TABLE [dbo].[Books] ADD  DEFAULT ((0)) FOR [RentalPrice]
GO
ALTER TABLE [dbo].[Books] ADD  DEFAULT ((0)) FOR [IsDeleted]
GO
ALTER TABLE [dbo].[Books] ADD  DEFAULT (getdate()) FOR [CreatedAt]
GO
ALTER TABLE [dbo].[Books] ADD  DEFAULT (getdate()) FOR [UpdatedAt]
GO
ALTER TABLE [dbo].[Books] ADD  DEFAULT ((0.00)) FOR [DepositPercentage]
GO
ALTER TABLE [dbo].[Books] ADD  DEFAULT ((1.00)) FOR [FineMultiplier]
GO
ALTER TABLE [dbo].[BorrowHistory] ADD  DEFAULT (getdate()) FOR [ActionDate]
GO
ALTER TABLE [dbo].[BorrowHistory] ADD  DEFAULT ((0)) FOR [IsDeleted]
GO
ALTER TABLE [dbo].[BorrowRecords] ADD  DEFAULT (getdate()) FOR [BorrowDate]
GO
ALTER TABLE [dbo].[BorrowRecords] ADD  DEFAULT ('Borrowed') FOR [Status]
GO
ALTER TABLE [dbo].[BorrowRecords] ADD  DEFAULT ((0)) FOR [FineAmount]
GO
ALTER TABLE [dbo].[BorrowRecords] ADD  DEFAULT ((0)) FOR [BorrowPrice]
GO
ALTER TABLE [dbo].[BorrowRecords] ADD  DEFAULT ((0)) FOR [IsDeleted]
GO
ALTER TABLE [dbo].[BorrowRecords] ADD  DEFAULT (getdate()) FOR [CreatedAt]
GO
ALTER TABLE [dbo].[BorrowRecords] ADD  DEFAULT (getdate()) FOR [UpdatedAt]
GO
ALTER TABLE [dbo].[BorrowRecords] ADD  DEFAULT ((1)) FOR [Quantity]
GO
ALTER TABLE [dbo].[BorrowRecords] ADD  DEFAULT ((1)) FOR [NumberOfDays]
GO
ALTER TABLE [dbo].[Categories] ADD  DEFAULT ((0)) FOR [IsDeleted]
GO
ALTER TABLE [dbo].[Categories] ADD  DEFAULT (getdate()) FOR [CreatedAt]
GO
ALTER TABLE [dbo].[Categories] ADD  DEFAULT (getdate()) FOR [UpdatedAt]
GO
ALTER TABLE [dbo].[DamagedBooks] ADD  DEFAULT (getdate()) FOR [DamageDate]
GO
ALTER TABLE [dbo].[DamagedBooks] ADD  DEFAULT ('Pending') FOR [Status]
GO
ALTER TABLE [dbo].[DamagedBooks] ADD  DEFAULT ((0)) FOR [RepairCost]
GO
ALTER TABLE [dbo].[DamagedBooks] ADD  DEFAULT ((0)) FOR [IsDeleted]
GO
ALTER TABLE [dbo].[DamagedBooks] ADD  DEFAULT (getdate()) FOR [CreatedAt]
GO
ALTER TABLE [dbo].[DamagedBooks] ADD  DEFAULT (getdate()) FOR [UpdatedAt]
GO
ALTER TABLE [dbo].[Notifications] ADD  DEFAULT (getdate()) FOR [SentDate]
GO
ALTER TABLE [dbo].[Notifications] ADD  DEFAULT ((0)) FOR [IsRead]
GO
ALTER TABLE [dbo].[Notifications] ADD  DEFAULT ((0)) FOR [IsDeleted]
GO
ALTER TABLE [dbo].[Payments] ADD  DEFAULT (getdate()) FOR [PaymentDate]
GO
ALTER TABLE [dbo].[Payments] ADD  DEFAULT ((0)) FOR [AmountGiven]
GO
ALTER TABLE [dbo].[Payments] ADD  DEFAULT ((0)) FOR [TotalOrderAmount]
GO
ALTER TABLE [dbo].[Payments] ADD  DEFAULT ((0)) FOR [ChangeAmount]
GO
ALTER TABLE [dbo].[Payments] ADD  DEFAULT ((0)) FOR [IsDeleted]
GO
ALTER TABLE [dbo].[Publishers] ADD  DEFAULT ((0)) FOR [IsDeleted]
GO
ALTER TABLE [dbo].[Publishers] ADD  DEFAULT (getdate()) FOR [CreatedAt]
GO
ALTER TABLE [dbo].[Publishers] ADD  DEFAULT (getdate()) FOR [UpdatedAt]
GO
ALTER TABLE [dbo].[RevenueStatistics] ADD  DEFAULT ((0)) FOR [TotalBooksSold]
GO
ALTER TABLE [dbo].[RevenueStatistics] ADD  DEFAULT ((0)) FOR [TotalRevenue]
GO
ALTER TABLE [dbo].[RevenueStatistics] ADD  DEFAULT ((0)) FOR [NetRevenue]
GO
ALTER TABLE [dbo].[RevenueStatistics] ADD  DEFAULT ((0)) FOR [IsDeleted]
GO
ALTER TABLE [dbo].[RevenueStatistics] ADD  DEFAULT (getdate()) FOR [CreatedAt]
GO
ALTER TABLE [dbo].[RevenueStatistics] ADD  DEFAULT (getdate()) FOR [UpdatedAt]
GO
ALTER TABLE [dbo].[Students] ADD  DEFAULT ((0)) FOR [IsDeleted]
GO
ALTER TABLE [dbo].[Students] ADD  DEFAULT (getdate()) FOR [CreatedAt]
GO
ALTER TABLE [dbo].[Students] ADD  DEFAULT (getdate()) FOR [UpdatedAt]
GO
ALTER TABLE [dbo].[Students] ADD  DEFAULT ((0)) FOR [TotalBooksRented]
GO
ALTER TABLE [dbo].[Students] ADD  DEFAULT ((0)) FOR [LateReturnsCount]
GO
ALTER TABLE [dbo].[Students] ADD  DEFAULT ((0)) FOR [DamagedBooksCount]
GO
ALTER TABLE [dbo].[Students] ADD  DEFAULT ((0)) FOR [TotalOrders]
GO
ALTER TABLE [dbo].[Users] ADD  DEFAULT ((0)) FOR [IsDeleted]
GO
ALTER TABLE [dbo].[Users] ADD  DEFAULT (getdate()) FOR [CreatedAt]
GO
ALTER TABLE [dbo].[Users] ADD  DEFAULT (getdate()) FOR [UpdatedAt]
GO
ALTER TABLE [dbo].[Users] ADD  DEFAULT ((0)) FOR [IsActive]
GO
ALTER TABLE [dbo].[AuditLogs]  WITH CHECK ADD FOREIGN KEY([UserID])
REFERENCES [dbo].[Users] ([UserID])
GO
ALTER TABLE [dbo].[BookCategories]  WITH CHECK ADD FOREIGN KEY([BookID])
REFERENCES [dbo].[Books] ([BookID])
GO
ALTER TABLE [dbo].[BookCategories]  WITH CHECK ADD FOREIGN KEY([CategoryID])
REFERENCES [dbo].[Categories] ([CategoryID])
GO
ALTER TABLE [dbo].[BookCopies]  WITH CHECK ADD FOREIGN KEY([BookID])
REFERENCES [dbo].[Books] ([BookID])
GO
ALTER TABLE [dbo].[BookReviews]  WITH CHECK ADD FOREIGN KEY([BookID])
REFERENCES [dbo].[Books] ([BookID])
GO
ALTER TABLE [dbo].[BookReviews]  WITH CHECK ADD FOREIGN KEY([UserID])
REFERENCES [dbo].[Users] ([UserID])
GO
ALTER TABLE [dbo].[Books]  WITH CHECK ADD FOREIGN KEY([AuthorID])
REFERENCES [dbo].[Authors] ([AuthorID])
GO
ALTER TABLE [dbo].[Books]  WITH CHECK ADD FOREIGN KEY([PublisherID])
REFERENCES [dbo].[Publishers] ([PublisherID])
GO
ALTER TABLE [dbo].[BorrowHistory]  WITH CHECK ADD FOREIGN KEY([RecordID])
REFERENCES [dbo].[BorrowRecords] ([RecordID])
GO
ALTER TABLE [dbo].[BorrowRecords]  WITH CHECK ADD FOREIGN KEY([BookID])
REFERENCES [dbo].[Books] ([BookID])
GO
ALTER TABLE [dbo].[BorrowRecords]  WITH CHECK ADD FOREIGN KEY([UserID])
REFERENCES [dbo].[Users] ([UserID])
GO
ALTER TABLE [dbo].[BorrowRecords]  WITH CHECK ADD  CONSTRAINT [FK_BorrowRecords_Payments] FOREIGN KEY([PaymentID])
REFERENCES [dbo].[Payments] ([PaymentID])
GO
ALTER TABLE [dbo].[BorrowRecords] CHECK CONSTRAINT [FK_BorrowRecords_Payments]
GO
ALTER TABLE [dbo].[DamagedBooks]  WITH CHECK ADD FOREIGN KEY([BookID])
REFERENCES [dbo].[Books] ([BookID])
GO
ALTER TABLE [dbo].[DamagedBooks]  WITH CHECK ADD FOREIGN KEY([ReportedBy])
REFERENCES [dbo].[Users] ([UserID])
GO
ALTER TABLE [dbo].[Notifications]  WITH CHECK ADD FOREIGN KEY([UserID])
REFERENCES [dbo].[Users] ([UserID])
GO
ALTER TABLE [dbo].[Payments]  WITH CHECK ADD FOREIGN KEY([UserID])
REFERENCES [dbo].[Users] ([UserID])
GO
ALTER TABLE [dbo].[Payments]  WITH CHECK ADD  CONSTRAINT [FK_Payments_Students] FOREIGN KEY([StudentID])
REFERENCES [dbo].[Students] ([StudentID])
GO
ALTER TABLE [dbo].[Payments] CHECK CONSTRAINT [FK_Payments_Students]
GO
ALTER TABLE [dbo].[RevenueStatistics]  WITH CHECK ADD FOREIGN KEY([BookID])
REFERENCES [dbo].[Books] ([BookID])
GO
ALTER TABLE [dbo].[RevenueStatistics]  WITH CHECK ADD FOREIGN KEY([PaymentID])
REFERENCES [dbo].[Payments] ([PaymentID])
GO
ALTER TABLE [dbo].[Students]  WITH CHECK ADD FOREIGN KEY([UserID])
REFERENCES [dbo].[Users] ([UserID])
GO
ALTER TABLE [dbo].[BookCopies]  WITH CHECK ADD CHECK  (([Status]='Lost' OR [Status]='Damaged' OR [Status]='Borrowed' OR [Status]='Available'))
GO
ALTER TABLE [dbo].[BookReviews]  WITH CHECK ADD CHECK  (([Rating]>=(1) AND [Rating]<=(5)))
GO
ALTER TABLE [dbo].[Books]  WITH CHECK ADD CHECK  (([PublishedYear]>=(1900) AND [PublishedYear]<=datepart(year,getdate())))
GO
ALTER TABLE [dbo].[BorrowHistory]  WITH CHECK ADD CHECK  (([Action]='Overdue' OR [Action]='Returned' OR [Action]='Borrowed'))
GO
ALTER TABLE [dbo].[BorrowRecords]  WITH CHECK ADD CHECK  (([Status]='Overdue' OR [Status]='Returned' OR [Status]='Borrowed'))
GO
ALTER TABLE [dbo].[DamagedBooks]  WITH CHECK ADD CHECK  (([DamageSeverity]='High' OR [DamageSeverity]='Medium' OR [DamageSeverity]='Low'))
GO
ALTER TABLE [dbo].[DamagedBooks]  WITH CHECK ADD CHECK  (([Status]='Discarded' OR [Status]='Replaced' OR [Status]='Under Repair' OR [Status]='Pending'))
GO
ALTER TABLE [dbo].[Payments]  WITH CHECK ADD CHECK  (([PaymentMethod]='Online' OR [PaymentMethod]='Card' OR [PaymentMethod]='Cash'))
GO
ALTER TABLE [dbo].[Students]  WITH CHECK ADD CHECK  (([EnrollmentYear]>=(2000) AND [EnrollmentYear]<=datepart(year,getdate())))
GO
ALTER TABLE [dbo].[Students]  WITH CHECK ADD CHECK  (([Gender]='Other' OR [Gender]='Female' OR [Gender]='Male'))
GO
ALTER TABLE [dbo].[Users]  WITH CHECK ADD CHECK  (([UserRole]=(2) OR [UserRole]=(1)))
GO
/****** Object:  StoredProcedure [dbo].[CheckBorrowStatus]    Script Date: 1/9/2025 5:10:41 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[CheckBorrowStatus]
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
/****** Object:  StoredProcedure [dbo].[DeleteAuthor]    Script Date: 1/9/2025 5:10:41 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[DeleteAuthor]
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
GO
/****** Object:  StoredProcedure [dbo].[DeleteBook]    Script Date: 1/9/2025 5:10:41 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[DeleteBook]
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
GO
/****** Object:  StoredProcedure [dbo].[DeleteBorrowRecord]    Script Date: 1/9/2025 5:10:41 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[DeleteBorrowRecord]
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
/****** Object:  StoredProcedure [dbo].[DeleteCategory]    Script Date: 1/9/2025 5:10:41 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[DeleteCategory]
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
GO
/****** Object:  StoredProcedure [dbo].[DeletePublisher]    Script Date: 1/9/2025 5:10:41 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[DeletePublisher]
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
GO
/****** Object:  StoredProcedure [dbo].[deleteStu]    Script Date: 1/9/2025 5:10:41 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE   PROCEDURE [dbo].[deleteStu]
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
/****** Object:  StoredProcedure [dbo].[InsertBorrowRecord]    Script Date: 1/9/2025 5:10:41 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[InsertBorrowRecord]
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
/****** Object:  StoredProcedure [dbo].[SelectAllBorrowRecords]    Script Date: 1/9/2025 5:10:41 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[SelectAllBorrowRecords]
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
/****** Object:  StoredProcedure [dbo].[SelectBorrowRecordByID]    Script Date: 1/9/2025 5:10:41 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[SelectBorrowRecordByID]
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
/****** Object:  StoredProcedure [dbo].[sp_CountPayments]    Script Date: 1/9/2025 5:10:41 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[sp_CountPayments]
AS
BEGIN
    SELECT COUNT(*) AS TotalPayments
    FROM Payments
    WHERE IsDeleted = 0;
END;
GO
/****** Object:  StoredProcedure [dbo].[sp_DeletePayment]    Script Date: 1/9/2025 5:10:41 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[sp_DeletePayment]
    @PaymentID INT
AS
BEGIN
    UPDATE Payments
    SET IsDeleted = 1
    WHERE PaymentID = @PaymentID;
END;
GO
/****** Object:  StoredProcedure [dbo].[sp_InsertPayment]    Script Date: 1/9/2025 5:10:41 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[sp_InsertPayment]
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
/****** Object:  StoredProcedure [dbo].[sp_SelectAllPayments]    Script Date: 1/9/2025 5:10:41 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[sp_SelectAllPayments]
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
/****** Object:  StoredProcedure [dbo].[sp_SelectPaymentByID]    Script Date: 1/9/2025 5:10:41 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[sp_SelectPaymentByID]
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
/****** Object:  StoredProcedure [dbo].[sp_UpdatePayment]    Script Date: 1/9/2025 5:10:41 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[sp_UpdatePayment]
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
/****** Object:  StoredProcedure [dbo].[UpdateAuthor]    Script Date: 1/9/2025 5:10:41 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[UpdateAuthor]
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
GO
/****** Object:  StoredProcedure [dbo].[UpdateBook]    Script Date: 1/9/2025 5:10:41 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[UpdateBook]
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
GO
/****** Object:  StoredProcedure [dbo].[UpdateBorrowRecord]    Script Date: 1/9/2025 5:10:41 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[UpdateBorrowRecord]
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
/****** Object:  StoredProcedure [dbo].[UpdateCategory]    Script Date: 1/9/2025 5:10:41 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[UpdateCategory]
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
GO
/****** Object:  StoredProcedure [dbo].[UpdateFineForOverdue]    Script Date: 1/9/2025 5:10:41 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[UpdateFineForOverdue]
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
/****** Object:  StoredProcedure [dbo].[UpdatePublisher]    Script Date: 1/9/2025 5:10:41 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[UpdatePublisher]
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
GO
/****** Object:  StoredProcedure [dbo].[updateStudent]    Script Date: 1/9/2025 5:10:41 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE   PROCEDURE [dbo].[updateStudent]
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
        UpdatedBy = @UserID,
        TotalBooksRented = @TotalBooksRented,
        LateReturnsCount = @LateReturnsCount,
        DamagedBooksCount = @DamagedBooksCount,
        TotalOrders = @TotalOrders,
        StudentCode = @StudentCode,
        UpdatedAt = GETDATE()
    WHERE StudentCode = @StudentCode;
END;
GO
USE [master]
GO
ALTER DATABASE [library] SET  READ_WRITE 
GO
