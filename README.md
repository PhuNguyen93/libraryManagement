# **Library Management System**

## **Overview**

This is a **Library Management System** developed in **Java Swing**. The application allows users to manage books, rentals, students, and more with a user-friendly interface. It also features **admin functionalities** for managing user accounts, borrowing history, and system settings.

## **Setup Instructions**

### **1. Clone the Project**

Clone the project repository from GitHub to your local machine:

git clone https://github.com/PhuNguyen93/libraryManagement.git

### 2. Configure the Database Connection
After cloning the project, you need to configure the database connection:

Navigate to the db.properties file (located in the root directory).

Edit the following details to match your machine configuration:

serverName: Name of your machine (e.g., DESKTOP-Q6HG8BL).

databaseName: The name of your database (e.g., library).

user: Your database username (e.g., sa).

password: Your database password (e.g., your_password).

![image](https://github.com/user-attachments/assets/b7edb52d-dfb9-46cd-a656-85bfdfb1861d)

### 3. Set Up the Database
Open SQL Server Management Studio (SSMS).

Connect to your server and run the newDb.sql script to create the necessary database and tables for the application.

### 4. Run the Application in Eclipse
Open the project in Eclipse IDE.

Navigate to the src/main/java/gui directory in Eclipse.

Open and run the Main.java file.

![image](https://github.com/user-attachments/assets/f70cbdc8-154a-44f2-b1ef-f34aa64770fe)

### 5. Login to the Application
Once the application starts, you will see the login screen (as shown below):

![image](https://github.com/user-attachments/assets/1c399585-1114-475e-9c1c-38d80b492285)

Use the default credentials to log in:

Username: admin@gmail.com

Password: 123456

After logging in, you can access the full functionalities of the application, including managing books, students, and rentals.

### Features

Book Management: Add, edit, view, and delete books.

Student Management: Manage student records and track borrowed books.

Admin Dashboard: View and manage system-wide data and settings.

### Technologies Used

Java Swing: For building the graphical user interface.

JDBC: For connecting and interacting with the SQL Server database.

SQL Server: For storing application data.

Feel free to customize and expand the instructions as needed. This README should provide a clear guide on how to set up and run the project.
