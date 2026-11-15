# College Student Portal

A web-based College Student Portal developed using Java, Spring Boot, MySQL, HTML, CSS, and JavaScript. It provides students with easy access to their academic information and gives administrators tools to manage student records.

## Features

### Student Dashboard
- Student login using Roll Number or Registration Number and Date of Birth.
- View student profile and academic details.
- View total fees, paid fees, and pending fees.
- View semester-wise marks.
- View assignments and their due dates.

### Admin Dashboard
- Secure admin login.
- Add, view, update, and delete student records.
- Manage student fee records.
- Add, update, and delete marks.
- Add, view, update, and delete assignments.

## Technologies Used

| Technology | Purpose |
|---|---|
| Java 17 | Backend programming |
| Spring Boot | Backend framework |
| Spring Data JPA | Database operations |
| MySQL | Database |
| HTML5 | Webpage structure |
| CSS3 | Styling and layout |
| JavaScript | Frontend functionality |
| Maven | Project and dependency management |
| Git & GitHub | Version control |

## Project Structure

```text
college-portal/
│
├── portal/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/portal/
│   │   │   │   ├── AdminController.java
│   │   │   │   ├── StudentController.java
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   ├── Student.java
│   │   │   │   ├── Fee.java
│   │   │   │   ├── Mark.java
│   │   │   │   ├── Assignment.java
│   │   │   │   └── Repositories
│   │   │   │
│   │   │   └── resources/static/
│   │   │       ├── index.html
│   │   │       ├── admin.html
│   │   │       ├── style.css
│   │   │       └── script.js
│   │   │
│   │   └── test/
│   ├── pom.xml
│   ├── mvnw
│   └── mvnw.cmd
│
├── .gitignore
└── README.md
```

## Installation and Setup

### 1. Clone the repository

```bash
git clone https://github.com/Samayra2217/college-portal.git
```

### 2. Open the project

Open the cloned project folder in VS Code.

### 3. Configure MySQL

- Install and start MySQL.
- Create a database named `college_portal`.
- Configure your database connection in `portal/src/main/resources/application.properties`.

Keep database credentials private. Do not upload your `application.properties` file.

### 4. Configure Admin Login

Set the following environment variables before starting the application:

- `ADMIN_USERNAME`
- `ADMIN_PASSWORD`

### 5. Run the application

Open the terminal inside the `portal` folder and execute:

**Windows:**
```powershell
.\mvnw.cmd spring-boot:run
```

### 6. Open the application

Visit:

```text
http://localhost:8080/
```

## Project Purpose

This project was developed as a practical learning project to understand Java backend development, Spring Boot, database connectivity, REST APIs, frontend integration, and CRUD operations.

## Author

**Samayra Garg**

GitHub: [Samayra2217](https://github.com/Samayra2217)
