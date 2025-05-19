# belejki.scheduler

**belejki.scheduler** belejki.scheduler is a Spring Boot MVC application that serves as monitor of the user reminders. It set their expiration flags (month, week, today), and sends reminder mail to the users for the event.

⚠️ Note: This application depends on the belejki.rest backend API. Make sure the backend is running and accessible before launching the frontend.

## ✨ Features

- Fetches all user events(reminders) checks if they expire: today, after a week or after month. Set the flags for these 3 options. Daily.
- Fetches all flagged expiresToday events and sends user email to remind him for the event. Daily.
- Fetches all flagged expiresAfterWeek events and sends user email to remind him for the event. Daily.
- Fetches all flagged expiresAfterMonth events and sends user email to remind him for the event. Daily.


## ⚙️ Installation & Setup

### Requirements

- Java 17+
- Maven
- IntelliJ IDEA (recommended)

### 📥 Clone the Repository

bash
git clone https://github.com/Valsinev/belejki.mvc.git


📧 Mail Configuration Requirements

To enable automated email sending (e.g., for reminder notifications), the scheduler application requires access to an SMTP-enabled email account. This account is used by the system to send emails.
✅ What You Need

You can use any email account that supports SMTP (e.g., Gmail, Outlook, Yahoo). For Gmail, some additional setup is required.
📌 Required Environment Variables

Set the following environment variables in your .env file or deployment configuration:


### ⚙️ Environment Variables
Before running the application, set the following environment variables:
| Variable                   | Description                                                     | Default            |
| -------------------------- | --------------------------------------------------------------- | ------------------ |
| `SERVER_PORT`              | (Optional) Port to run the app on                               | `8080`             |
| `BACKEND_API_URL`          | Backend rest URL used in email confirmation links               | `http://localhost:8080` |
| `AUTH_USERNAME`            | Registration in the backend(belejki.rest) must have ROLE_ADMIN authority  | —                  |
| `AUTH_PASSWORD`            | The password registered in the backend(belejki.rest)            | —                  |
| `SPRING_MAIL_USERNAME`     | The email address that will send the messages.                  | —                  |
| `SPRING_MAIL_PASSWORD`           | The SMTP or App Password for the email account.          | —                  |
| `SPRING_MAIL_HOST`           | The SMTP server (e.g., smtp.gmail.com for Gmail).              | —                  |
| `SPRING_MAIL_PORT`           | (Optional) SMTP port (587 for TLS, 465 for SSL).             | —                  |


🛡️ Gmail Setup Instructions

If you're using a Gmail account, follow these steps:

    Enable 2-Step Verification on your Google account: https://myaccount.google.com/security

    Go to App Passwords

    Generate an App Password (e.g., ndhd pdbi pixr xoxj)

    Use this App Password as your SPRING_MAIL_PASSWORD

Example:

SPRING_MAIL_USERNAME=youraccount@gmail.com
SPRING_MAIL_PASSWORD=ndhd pdbi pixr xoxj
SPRING_MAIL_HOST=smtp.gmail.com

⚠️ Notes

    Do not use your regular Gmail password — use an App Password.

    Prefer a dedicated email account for production usage.

    For improved reliability, scalability, and analytics, consider third-party SMTP providers like:

        SendGrid

        Mailgun

        Amazon SES


💡 In IntelliJ, go to Run > Edit Configurations > Environment Variables to set these.


### 🚀 Running the Application
✅ In IntelliJ (Recommended)

1. Open the project in IntelliJ IDEA.

2. Set environment variables (see above).

3. Find the main class: Application.java.

4. Right-click it and choose Run.

The API will start and be available at: http://localhost:8080/

🧪 From Terminal (Alternative)

./mvnw spring-boot:run

Or package and run the JAR:

./mvnw package
java -jar target/scheduler-0.0.1-SNAPSHOT.jar

Make sure environment variables are exported before running:

...



## 🛠️ Technologies Used

- Java 17

- Spring Boot

- Maven
- 
- Spring Mail

## 🔗 Related Projects

- [belejki.rest](https://github.com/Valsinev/belejki.rest.git) – REST API backend for this frontend.
