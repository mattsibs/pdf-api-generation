# PDF API

A Spring Boot REST API for generating PDF documents from CV data. This project provides endpoints to submit CV information and receive a generated PDF in response.

## Features
- Accepts CV data via REST API
- Generates PDF documents from submitted data
- Supports personal details, education, experience, and skills sections

## Technologies Used
- Java 17+
- Spring Boot
- Maven
- PDF generation library (add details if used)

## Getting Started

### Prerequisites
- Java 17 or higher
- Maven

### Installation
1. Clone the repository:
   ```bash
   git clone <repo-url>
   cd pdf-api
   ```
2. Build the project:
   ```bash
   ./mvnw clean install
   ```
3. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```

The API will be available at `http://localhost:8080` by default.

## API Endpoints

### Generate CV PDF
- **POST** `/cv/generate`
- **Request Body:**
  ```json
  {
    "personalDetails": { ... },
    "education": [ ... ],
    "experience": [ ... ],
    "skills": [ ... ]
  }
  ```
- **Response:** PDF file

## Example Usage
Use `curl` or Postman to send a POST request to `/cv/generate` with the required JSON body. The response will be a PDF file.

## Testing
Run tests with:
```bash
./mvnw test
```

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

## License
This project is licensed under the MIT License.
