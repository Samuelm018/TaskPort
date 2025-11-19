# Startup Website

This is a minimal Spring Boot + MySQL project that implements a client/admin project submission system.

- Backend: Spring Boot with JDBC (JdbcTemplate) and simple server-session authentication (HttpSession).
- Frontend: static HTML/CSS/JS served from `src/main/resources/static`.
- Database: MySQL (use provided credentials in `application.properties`).

Important: This project intentionally does NOT create the `admin` table. Please create `company.admin` manually with your admin credentials.

Run (Windows PowerShell):

```powershell
# Build
mvn -f "D:/Placement/Resume Projects/Startup website/pom.xml" clean package
# Run
mvn -f "D:/Placement/Resume Projects/Startup website/pom.xml" spring-boot:run
```

DB schema is in `src/main/resources/db/schema.sql` — run it against the `company` database.

Default DB config in `application.properties` points to `jdbc:mysql://localhost:3306/company` with user `root` and password `Sam996525`.

Admin account (seeded automatically on first startup):

- Gmail: `samuelm99729.work@gmail.com`
- Password: `Sam996525`

The application will create the `admin` table (if it does not exist) and insert this admin using a bcrypt-hashed password at startup via a DataInitializer component. The app uses server sessions (cookies) for authentication — no tokens required.
