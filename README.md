# Show Score Project

A full-stack web application built with Spring Boot for the backend and Angular for the frontend. The project integrates secure authentication using JWT.

## Technologies Used

*   **Backend**: Java, Spring Boot, Spring Security, Spring Data JPA
*   **Frontend**: Angular, TypeScript
*   **Database**: H2 (for development)
*   **Authentication**: JWT, OAuth2 (Google)
*   **Build**: Gradle (for the backend), NPM (for the frontend)

## Prerequisites

*   Java JDK 21 or higher
*   Node.js and npm
*   Git

## Installation and Setup

### 1. Clone the repository

```bash
git clone <REPOSITORY_URL>
cd <FOLDER_NAME>
````

### 2. Backend Configuration
The backend uses a .env file to manage sensitive environment variables.

Create a file named .env at the root of the project.
Copy the content below into your .env file and replace the values with your own keys, especially for Google OAuth2.

# Basic security credentials
SPRING_SECURITY_USER_NAME=user
SPRING_SECURITY_USER_PASSWORD=password

# Keys for Google OAuth2
GOOGLE_CLIENT_ID=YOUR_GOOGLE_CLIENT_ID
GOOGLE_CLIENT_SECRET=YOUR_GOOGLE_CLIENT_SECRET

# Secret key for JWT
JWT_SECRET=your_super_secret_jwt_to_generate


### 3. Running the Application
The project is configured so that the Spring Boot server also serves the Angular application.


Build the Angular Frontend: If you have made changes to the Angular source code (which should be in a subfolder, e.g., frontend/), you need to rebuild it to update the static files.


# Go to your Angular project folder
cd frontend/
npm install
npm run build
This command must be configured to place the build files in src/main/resources/static.


Run the Spring Boot Backend: You can run the application directly from your IntelliJ IDEA IDE by running the main ShowScoreApplication class, or by using Gradle:
./gradlew bootRun

### 4. Accessing the Application
Application: http://localhost:8080
H2 Console: http://localhost:8080/h2-console
JDBC URL: jdbc:h2:file:./showscore
User Name: sa
Password: (leave empty)