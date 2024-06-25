# SFS Image Management

## Description

SFS Image Management is a web application for managing image uploads and retrieval using the Imgur API. It provides functionalities for user registration, authentication, and image management.It hasposted image upload data to kafka. For Consumer demo KafkaConsumer also implemented it is consuming the same data what kafkaproducer posted. Usually we can consume data from other topics based on requirement. 

## Table of Contents

- [Description](#description)
- [Features](#features)
- [Scenarios Implemented](#scenarios-implemented)
- [Installation](#installation)
- [Usage](#usage)
- [Configuration](#configuration)
- [API Endpoints](#api-endpoints)
- [Contributing](#contributing)
- [CI/CD Pipeline](#ci/cd-pipeline)
- [License](#license)
- [Swagger Documentation](#swagger-documentation)
- [Performance Optimization](#performance-optimization)
- [Actuator](#actuator)
- [TestCases](#test-cases)

## Features

- User registration and authentication
- Image upload to Imgur,Image delete from Imgur
- Retrieve and display user images
- OAUTH2-based security

### Scenarios Implemented

1. **User Registration **
   - Users can register with a username and password.
   
2. **Image Upload and Management and Authentication**
   - Users can upload images, which are stored on Imgur and post to kafkatopic.
   - Uploaded images are linked to the user's profile.
   - Users can retrieve and display their images.
   - Users can delete images, ensuring only authenticated and authorized actions are performed.

3. **Secure Endpoints**
   - Secure endpoints require a valid Access token.
   - Implemented security measures to protect user data and image management functionalities at DELETE image .

4. **Kafka Integration**
   - Image upload events are published to a Kafka topic for further processing.
   - A Kafka consumer listens to these events and processes them asynchronously, ensuring the main application remains responsive.

5. **Asynchronous Processing**
   - Asynchronous processing for image uploads to Kafka, improving the responsiveness of the application.

## Installation

1. Clone the repository:
    ```sh
    git clone https://github.com/HarithaMahanthi/sfs-image-management.git
    ```
2. Navigate to the project directory:
    ```sh
    cd sfs-image-management
    ```
3. Install the required dependencies:
    ```sh
    mvn install
    ```
4. Set up the database and run the migrations (if any):
    ```sh
   # For H2 in-memory database, no additional setup is required. The database will be created and initialized automatically on application startup.
    ```
5. Start the application:
    ```sh
    mvn spring-boot:run
    ```

## Usage

1. Register a new user via the `/register` endpoint.
2. Authenticate using the `/login` endpoint.
3. Use the token to access protected endpoints for deleting images.

## Configuration

`application.properties` file should exists with required properties under `src/main/resources` 
* Please have pom.xml


## API Endpoints

### User Endpoints

- `POST /sfs/users/register` - Register a new user
- `POST /sfs/users/login` - Authenticate a user and get a JWT token
- `GET /sfs/users/{username}` - Retrieve user profile

### Image Endpoints

- `POST /sfs/images/upload` - Upload an image 
- `GET /sfs/images/{username}` - Get images for a user 
- `DELETE /sfs/images/{imageid}?{username}&{password}` - Delete particular image on a user (protected, requires access token)
- `POST /kafka/images/upload` - Upload an image to kafka
- 

## Contributing

1. Fork the repository.
2. Create a new branch (`git checkout -b feature-branch`).
3. Make your changes.
4. Commit your changes (`git commit -am 'Add some feature'`).
5. Push to the branch (`git push origin feature-branch`).
6. Create a new Pull Request.

## CI/CD Pipeline

This project uses GitHub Actions for continuous integration and deployment. The CI/CD pipeline is configured to build, test, and package the application whenever code is pushed to the repository or a pull request is Merged.

## Swagger Documentation

This project uses Swagger for API documentation. After starting the application, you can access the Swagger UI at: [link] http://localhost:8080/swagger-ui.html

You can also access the OpenAPI documentation in JSON format at: [link] http://localhost:8080/v3/api-docs

### Explanation

1. **Swagger Configuration Section**: Added configuration for `springdoc-openapi` in the `pom.xml` and `config` file.
2. **Swagger Documentation Section**: Additionally, you can view the API documentation directly by uploading the `src/main/resources/SFS-Image-mgmt-Swagger.yml` Swagger file to Swagger Editor `editor.swagger.io`.

## Performance Optimization

To optimize the API for handling 100K RPM (requests per minute), the  HikariCP configuration is used in `application.properties`.

This configuration helps in managing the database connection pool efficiently to handle a high number of requests per minute.

### Explanation

1. **HikariCP Configuration Section**: Added the necessary HikariCP configuration properties for handling 100K RPM.

## Actuator

Add the configurations to your `application.properties` file and `pom.xml`.

For a complete list of available endpoints and their details, refer to the Spring Boot Actuator documentation [link] https://docs.spring.io/spring-boot/reference/actuator/index.html

## Testing
This project uses JUnit 5 for unit testing. Below are some of the test cases implemented:

1. **UserControllerTest**: Tests for user registration, login, and retrieving user profiles.
2. **testRegisterUser**: Verifies that a user can be registered successfully.
3. **testLoginUser_Success**: Verifies that a user can log in successfully with correct credentials.
4. **testLoginUser_Failure**: Verifies that login fails with incorrect credentials.
5. **testGetUserProfile_Success**: Verifies that a user profile can be retrieved successfully.
6. **testGetUserProfile_Failure**: Verifies that retrieving a non-existent user profile returns a 404 status.

## Contact
For any queries regarding the project, please contact:

Name: Haritha Mahanthi
Email: haritha.goodvibes4u@gmail.com
GitHub: [link] https://github.com/HarithaMahanthi/sfs-image-management.git




