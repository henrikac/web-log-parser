# WebLogParser

A simple Spring Boot web application for experimenting with **file uploads**, **form validation**, and **template rendering** using Thymeleaf.
The application is designed to work together with a separate [log-parser](https://github.com/henrikac/log-parser) project, which provides the actual log parsing logic.

This project is primarily intended as a learning exercise in Java and Spring Boot.

## Features
- Upload `.log` files through a web interface
- Basic validation (required file, only `.log` extension)
- Temporary storage of uploaded files in the system temp directory
- Automatic cleanup after processing
- Display of uploaded log content in a result view
- Error handling and user feedback via flash messages
- Logging with SLF4J for both success and failure cases
- Future goal: integrate the [log-parser](https://github.com/henrikac/log-parser) library for advanced filtering and parsing

## Technology Stack
- Java 21+
- Spring Boot
- Thymeleaf
- Maven
- SLF4J / Logback

## Usage
Start the application with Maven:

```bash
./mvnw spring-boot:run
```

Then open http://localhost:8080 in your browser.

## License
This project is licensed under the MIT License.
