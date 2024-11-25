# IMDb Movies API Integration

This project is a containerized application that integrates the IMDb Movies API, providing access to film data through a PHP backend and a Java Spring application. The application consists of three primary components:

- **PHP API Service**: Uses the IMDb API to fetch movie details and search results. (Using [php-imdb-api](https://github.com/hmerritt/php-imdb-api))
- **Java Spring Application**: Interacts with the PHP backend to display movie data and handle user requests.
- **PHP UI**: A user interface that allows users to search for movies and view their details.

## Project Overview

This application consists of the following major parts:

- **PHP IMDb API**: A PHP backend that makes requests to the IMDb API and processes the responses. It offers endpoints for fetching film data and searching for movies.
- **Java Spring Application**: A Spring Boot app that consumes the movie data from the PHP API, processes it, and exposes it to the UI.
- **PHP UI**: The frontend of the application, which allows users to interact with the system, search for movies, and view movie details in a responsive user interface.

## Features

1. **Search for Movies**: Search movies by title and retrieve detailed information such as the movie's plot, genres, and release year.
2. **Film Details**: Fetch detailed data about individual movies, including information like length, tech specs, and more.
3. **Responsive UI**: The user interface is designed to be intuitive and user-friendly, providing an easy way to interact with the movie data.

## Architecture

- **PHP Backend**: The PHP component acts as the intermediary between the Java Spring application and the IMDb API. It makes API calls to IMDb and processes the data.
- **Java Spring Application**: The Java Spring app is responsible for business logic and communicates with the PHP backend to request movie data.
- **PHP UI**: The UI, built with PHP, provides the frontend interface for users to interact with the app.

## Technologies Used

- **PHP**: Backend logic and interaction with the IMDb API.
- **Java Spring Boot**: Main application framework for handling API requests and serving data to the UI.
- **IMDb API**: Third-party API for fetching movie data.
- **Docker**: Containerized application, allowing the entire stack to run in isolated environments.

## Dockerization

This app is fully containerized using Docker, making it easy to deploy and run in any environment. The Docker setup includes three containers:

1. **PHP IMDb API**: The container responsible for handling the backend API requests and interacting with IMDb.
2. **Java Spring App**: The container running the Spring Boot application that communicates with the PHP backend and processes user input.
3. **PHP UI**: The container providing the user interface, allowing users to search for and view movie details.

## Setup and Running

### Prerequisites
Make sure you have [Docker](https://www.docker.com/get-started) installed on your system.

### Running the Application

1. Clone the repository:

   ```bash
   git clone [https://github.com/Nes-Afriat-New/Imdb-fun.git]
```
2. Setup the project of https://github.com/hmerritt/php-imdb-api using their readme,** and add to the php-imdb-api dir the api.php and the Docker File in the main Dir**
