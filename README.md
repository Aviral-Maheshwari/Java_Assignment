# Bajaj Webhook Assignment - Java Spring Boot

A Spring Boot application that auto-runs on startup to generate webhooks, retrieve questions, solve them with SQL, and submit solutions via JWT-authenticated endpoints.

## 🚀 Features

- **Auto-runs on startup** using `CommandLineRunner` — no manual API calls needed
- **Webhook generation** from `POST https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA`
- **JWT authentication** for secure solution submission
- **SQL solution loading** from `solution.sql` file
- **Auto-submission** with proper authorization headers
- **Java 15 + Spring Boot 2.7.14** compatibility with Maven build system
- **No external dependencies** for PDF parsing or remote file fetching

## 📂 Project Structure

```
bajaj-webhook/
├── pom.xml
├── README.md
├── solution.sql
├── src/main/resources/
│   └── application.properties
└── src/main/java/com/example/bajaj_webhook/
    ├── BajajWebhookApplication.java
    ├── model/
    │   ├── GenerateWebhookResponse.java
    │   └── FinalSubmissionRequest.java
    └── service/
        ├── WebhookService.java
        └── QuestionFetcher.java
```

## ⚙️ Technologies Used

| Component | Version |
|-----------|---------|
| Java | 15 |
| Spring Boot | 2.7.14 |
| Maven | 3.9.9 |
| HTTP Client | RestTemplate |
| JSON Serialization | Jackson |

## 📝 Configuration

Edit `application.properties` before building:

```properties
# Webhook Generation Endpoint
bfhl.generateWebhookUrl=https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA

# User Credentials
bfhl.name=John Doe
bfhl.regNo=REG12327
bfhl.email=john@example.com

# Solution File
bfhl.solutionFile=solution.sql

# Logging
logging.level.root=INFO
```

Replace the name, registration number, and email with your actual details.

## 🧠 Question 1: Odd Last Digit Solution

The SQL query in `solution.sql` solves the assigned problem:

```sql
WITH emp_paid AS (
  SELECT DISTINCT e.emp_id, e.first_name, e.last_name, e.dob, e.department
  FROM employee e
  JOIN payments p ON p.emp_id = e.emp_id
  WHERE p.amount > 70000
),
emp_age AS (
  SELECT ep.*,
         FLOOR(DATEDIFF(CURRENT_DATE, ep.dob) / 365.25) AS age
  FROM emp_paid ep
),
ranked AS (
  SELECT ea.*,
         ROW_NUMBER() OVER (PARTITION BY ea.department ORDER BY ea.last_name, ea.first_name) AS rn
  FROM emp_age ea
)
SELECT d.department_name AS DEPARTMENT_NAME,
       ROUND(AVG(r.age), 2) AS AVERAGE_AGE,
       GROUP_CONCAT(
         CASE WHEN r.rn <= 10 THEN CONCAT(r.first_name, ' ', r.last_name) END
         ORDER BY r.rn SEPARATOR ', '
       ) AS EMPLOYEE_LIST
FROM ranked r
JOIN department d ON d.department_id = r.department
GROUP BY d.department_id, d.department_name
ORDER BY d.department_id DESC;
```

## ▶️ Build & Run

### 1. Build JAR File

```bash
mvn clean package -DskipTests
```

This creates `target/bajaj-webhook-1.0.0.jar`.

### 2. Run Application

```bash
java -jar target/bajaj-webhook-1.0.0.jar
```

### Execution Flow

1. Generates webhook from the specified endpoint
2. Displays webhook URL and masked access token
3. Loads SQL query from `solution.sql`
4. Submits solution via POST request with JWT authorization
5. Prints success/failure status to console

## 📤 Submission Checklist

- ✔ Public GitHub repository with complete source code
- ✔ `solution.sql` file with final query
- ✔ Executable JAR file
- ✔ Webhook generation and JWT handling
- ✔ Auto-submit functionality (no manual endpoints required)
- ✔ Fully compatible with specified tech stack

## 📋 What to Submit

1. **GitHub repository link** (with all source files)
2. **JAR file download link** (raw GitHub release or direct link)
3. **Assignment form submission** (with the above links)

## 💡 How It Works

The application starts and automatically:
- Calls the webhook generation endpoint with your credentials
- Receives a webhook URL and JWT access token
- Loads your SQL solution from the `solution.sql` file
- Posts the solution back to the webhook URL with proper authorization
- Logs the result to console

No manual API calls or HTTP client setup required — everything runs automatically on startup.

## 🛠️ Troubleshooting

**JAR won't build?**
- Ensure Maven 3.9.9+ is installed: `mvn --version`
- Check Java 15 is set: `java -version`

**Connection timeout?**
- Verify network connectivity to `bfhldevapigw.healthrx.co.in`
- Check if the endpoint is accessible from your environment

**SQL file not found?**
- Ensure `solution.sql` is in the root directory
- Verify the path in `application.properties` is correct

## 📬 Support

For additional help:
- Check console output for detailed error messages
- Review `application.properties` configuration
- Verify GitHub repository structure matches the project layout

---

**Ready to submit?** Package the JAR, push to GitHub, and share the links with your assignment form.
