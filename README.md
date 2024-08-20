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
cd Weather-App\src\main\resources
```
#### 2. Launch ```Setup.bat```
```bash
Setup.bat
```
#### 3. Open ```application.properties```****

#### 4. Please provide your username and password, API key, JWT secket, duration of session in the designated fields.

#### 5. Choose ```create``` or ```create-drop``` in the field ```spring.jpa.hibernate.ddl-auto```.

#### 6. Open pgAdmin and create database with name ```BlogPlatform```

#### 7. Execute the Maven command to clean the project and then build it
```bash
mvn clean install
```
#### 8. run the application using the following Java command:
```bash
java -jar \target\Weather-0.0.1-SNAPSHOT.jar
```
