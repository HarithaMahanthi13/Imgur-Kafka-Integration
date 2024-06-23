# SFS Image Management

## Description

SFS Image Management is a web application for managing image uploads and retrieval using the Imgur API. It provides functionalities for user registration, authentication, and image management.

## Table of Contents

- [Description](#description)
- [Features](#features)
- [Installation](#installation)
- [Usage](#usage)
- [Configuration](#configuration)
- [API Endpoints](#api-endpoints)
- [Contributing](#contributing)
- [License](#license)

## Features

- User registration and authentication
- Image upload to Imgur,Image delete from Imgur
- Retrieve and display user images
- OAUTH2-based security

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
    # Add your database setup commands here
    ```
5. Start the application:
    ```sh
    mvn spring-boot:run
    ```

## Usage

1. Register a new user via the `/register` endpoint.
2. Authenticate using the `/login` endpoint to get a JWT token.
3. Use the token to access protected endpoints for uploading and retrieving images.

## Configuration

Create an `application.properties` file in `src/main/resources` and add the following configurations:

```properties
# Application properties
spring.datasource.url=jdbc:mysql://localhost:3306/sfs_image_management
spring.datasource.username=root
spring.datasource.password=root

# Imgur API
imgur.client.id=YOUR_IMGUR_CLIENT_ID
imgur.client.secret=YOUR_IMGUR_CLIENT_SECRET

## API Endpoints

### User Endpoints

- `POST /sfs/users/register` - Register a new user
- `POST /sfs/users/login` - Authenticate a user and get a JWT token
- `GET /sfs/users/{username}` - Retrieve user profile

### Image Endpoints

- `POST /sfs/images/upload` - Upload an image (protected)
- `GET /sfs/images/{username}` - Get images for a user (protected)

## Contributing

1. Fork the repository.
2. Create a new branch (`git checkout -b feature-branch`).
3. Make your changes.
4. Commit your changes (`git commit -am 'Add some feature'`).
5. Push to the branch (`git push origin feature-branch`).
6. Create a new Pull Request.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.



