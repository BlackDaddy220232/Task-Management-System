# Task-Management-System
## What is it?
The Task Management System is a RESTful web service built using the Spring Framework. It provides users with the ability to manage their tasks, appoint executors. 
## Sonar Cloud
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=BlackDaddy220232_Task-Management-System&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=BlackDaddy220232_Task-Management-System)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=BlackDaddy220232_Task-Management-System&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=BlackDaddy220232_Task-Management-System)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=BlackDaddy220232_Task-Management-System&metric=bugs)](https://sonarcloud.io/summary/new_code?id=BlackDaddy220232_Task-Management-System)

This project is integrated with the Sonor Cloud platform, which provides advanced code analysis and quality assurance tools to help us deliver high-quality, maintainable code.

## API Reference
### Postman
To interact with the "Weather Application" application's RESTful API endpoints, you can use the Postman request by this [link](https://www.postman.com/material-saganist-75818563/workspace/task-management-system). Tap ```create a fork``` and use Postman for doing request.

```http
POST /auth/signup
```

| Body | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `username` | `string` | **Required**. Nickname of user |
| `password` | `int` | **Required**. Password of user |
| `country` | `string` | **Required**. Country of user |

#### Edit Password

```http
PATCH /auth/editPassword
```

| Body | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `password` | `int` | **Required**. New password |

#### Sign In

```http
POST /auth/signin
```

| Body | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `username` | `string` | **Required**. Nickname of user |
| `password` | `string` | **Required**. Password of user |

#### Create a new task for the user.
```http
POST /user/task
```
| Body | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `title` | `string` | **Required**. Title of task |
| `definition` | `string` | **Required**. Definition of task |
| `priority` | `Priority` | **Required**. Priority of task (LOW, MEDIUM, HIGH, CRITICAL) |
| `status` | `Status` | **Required**. Status of task (TO_DO, IN_PROGRESS, COMPLETED, CANCELLED) |

#### Appoint an employee to a task.
```http
POST /user/task/employee
```
| Param | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `taskId` | `Long` | **Required**. Id of task |
| `employeeId` | `Long` | **Required**. Id of employee |

#### Retrieve a list of all employees.
```http
GET /user/allEmployees
```
#### Retrieve all tasks for the current user.
```http
GET /user/tasks
```
#### Retrieve tasks by user ID with optional filters.
```http
GET /user/{id}/tasks
```
| Param | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `offset` | `Integer` | **Required**. Number of page |
| `limit` | `Integer` | **Required**. Number of tasks on the page |
| `status` | `Status` | **Not Required**. Filter task of status (TO_DO, IN_PROGRESS, COMPLETED, CANCELLED) |
| `priority` | `Priority` | **Not Required**. Filter task of priority (LOW, MEDIUM, HIGH, CRITICAL) |
| Path Variable | Type     | Description                |
| `id` | `Long` | **Required**. Id of user |

#### Creates a comment on a task.
```http
POST /user/task/{id}/comment
```
| Path Variable | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `id` | `Long` | **Required**. Id of user |
| Body | Type     | Description                |
| `content` | `String` | **Required**. Content of the comment |

#### Changes the status of a task.
```http
PATCH /user/task/{id}/status
```
| Path Variable | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `id` | `Long` | **Required**. Id of user |
| Body | Type     | Description                |
| `status` | `Status` | **Required**. New status of task (TO_DO, IN_PROGRESS, COMPLETED, CANCELLED) |

#### Edits an existing task.
```http
PATCH /user/task/{id}
```
| Path Variable | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `id` | `Long` | **Required**. Id of user |
| Body | Type     | Description                |
| `title` | `string` | **Not Required**. Title of task |
| `definition` | `string` | **Not Required**. Definition of task |
| `priority` | `Priority` | **Not Required**. Priority of task (LOW, MEDIUM, HIGH, CRITICAL) |
| `status` | `Status` | **Not Required**. Status of task (TO_DO, IN_PROGRESS, COMPLETED, CANCELLED) |

#### Deletes a task.

```http
DELETE /user/task/{id}
```
| Path Variable | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `id` | `Long` | **Required**. Id of user |

#### Get a list of all tasks
```http
GET /task/all
```
## Prerequisites

- Java Development Kit (JDK) 17 or later
- Apache Maven 3.9.6

## Installation

#### 1. Clone the repository:
    
```bash
  git clone https://github.com/BlackDaddy220232/Task-Management-System.git
```

#### 2. Navigate into the cloned directory
```bash
cd Task-Management-System\
```
#### 3. Open ``.env```****

#### 4. Please provide your username and password.

#### 5.Navigate to this folder

```
cd src\main\resources
```
#### 6. Open ```application.properties```****

#### 7. Please provide JWT secket, duration of session in the designated fields.

#### 8. Choose ```create``` or ```update``` in the field ```spring.jpa.hibernate.ddl-auto```.

### For Docker:

#### 1. Execute the Maven command to clean the project and then build it
```bash
mvn clean install -DskipTests
```
#### 2. Run the application in the Docker by this command:
```bash
docker-compose up --build
```
### For manual launch
#### 1. Open pgAdmin and create database with name ```BlogPlatform```
#### 2. Execute the Maven command to clean the project and then build it
```bash
mvn clean install
```
#### 3. run the application using the following Java command:
```bash
java -jar \target\taskmanagementsystem-0.0.1-SNAPSHOT.jar
```
## Technologies and Frameworks
The Weather Application is built using the following technologies and frameworks:

* Spring Framework
   
* Spring Security
   
* JWT (JSON Web Tokens)
  
* Unit Testing
* Logging and Aspect-Oriented Programming
* Caching
* Spring Boot

## Configuration

You can customize the application's behavior by modifying the `application.properties` file located in the `src/main/resources` directory. In this file, you can configure settings such as the time_of_session, and secret of JWT token, and type of creating database (update, create, create-drop).

## Tests

The service layer of the "Weather Application" application has **100% unit test coverage**. This means that every method and code path in the service layer is thoroughly tested, ensuring the correctness of the application's core functionality.

## Security
This application implements several security measures to protect the weather API integration:

### Authentication and Authorization
The application uses JSON Web Tokens (JWT) for authentication and authorization. When a user logs in, the server generates a signed JWT token, sends it back to the client, and the client stores the token in a cookie. For all subsequent requests, the client must include the token from the cookie in the Authorization header.

### Secure Cookies
In addition to JWT tokens, the application also uses cookies to manage user sessions. The session cookies are marked as HttpOnly and Secure, ensuring they can only be accessed by the server and transmitted over HTTPS.
## Contributing

Contributions are welcome! If you find any issues or want to add new features, please submit a pull request.

## License

This project is licensed under the MIT License. See the `LICENSE` file for more information.

## Author

This application was developed by Alexander Mynzul.
