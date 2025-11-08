# N-th Minimal Number Finder

A Spring Boot REST API service that finds the N-th minimum number in an Excel (.xlsx) file using an efficient QuickSelect algorithm.

## 🚀 Features

- **REST API** with Swagger/OpenAPI documentation

- **Excel file processing** (.xlsx format)

- **QuickSelect algorithm** - O(n) average time complexity

- **Docker containerization** for easy deployment

- **Input validation** and comprehensive error handling


## 📋 API Endpoints

### GET `/find-nth-min`

Finds the N-th minimum number in an Excel file.

**Request Body:**

```json
{
  "path": "/path/to/your/file.xlsx",
  "n": 5
}
```

**Response:**
```json
{
  "n": 5,
  "result": 42,
  "totalNumbers": 100
}
```
**Error Responses:**

- `400 Bad Request` - Invalid input parameters or file errors

- `500 Internal Server Error` - Unexpected server errors


## 🛠️ Technologies Used

- **Java 21** - Programming language

- **Spring Boot 3** - Application framework

- **Apache POI** - Excel file processing

- **SpringDoc OpenAPI** - API documentation

- **Docker** - Containerization

- **Maven** - Build tool


## 📁 Project Structure

```text

src/
├── main/
│   ├── java/ru/jorodsi/nthminimalnumber/
│   │   ├── controller/
│   │   │   └── NthMinController.java
│   │   ├── service/
│   │   │   ├── ExcelService.java
│   │   │   └── QuickSelectService.java
│   │   ├── model/
│   │   │   └── FindNumberRequest.java
│   │   └── NthMinimalNumberApplication.java
│   └── resources/
│       └── application.properties
└── test/
    └── java/ru/jordosi/nthminimalNumber/
        ├── controller/
        └── service/
```

## 🏃‍♂️ Quick Start

### Prerequisites

- Java 21 or higher

- Maven 3.6+

- Docker (optional)


### Running with Maven

1. **Clone and build the project:**

    ```bash
    git clone https://github.com/Jordosi/nth-minimal-number.git
    cd nth-minimal-number
    mvn clean package
   ```
2. **Run the application:**
    ```bash
    java -jar target/nth-min-finder-1.0.0.jar
    ```
3. **Access the application:**
    
    - API: `http://localhost:8080/find-nth-min`
        
    - Swagger UI: `http://localhost:8080/swagger-ui/index.html`
        
    - OpenAPI Spec: `http://localhost:8080/v3/api-docs`
        

### Running with Docker

1. **Build the Docker image:**
    
    ```bash
    docker build -t nth-minimal-number .
    ```
2. **Run the container:**
    ```bash
    
    docker run -d -p 8080:8080 --name nth-min-service nth-minimal-number    
    ```
## 🐳 Docker Configuration

### Dockerfile
```dockerfile
FROM maven:3.9-eclipse-temurin-21 AS builder  
WORKDIR /app  
COPY . .  
RUN mvn clean package  
  
#launch stage  
FROM eclipse-temurin:21  
WORKDIR /app  
COPY --from=builder /app/target/*.jar app.jar  
  
EXPOSE 8080  
ENTRYPOINT ["java", "-jar", "app.jar"]
```
## 📊 Algorithm

The service uses the **QuickSelect algorithm** to find the N-th smallest element:

- **Average Time Complexity**: O(n)

- **Worst Case**: O(n²)

- **Memory Efficiency**: O(1) additional space (in-place)


This is more efficient than full sorting (O(n log n)) for selection problems.

## 📝 Excel File Format

The service expects Excel files (.xlsx) with the following structure:

- **First column** should contain integer values

- **First sheet** will be processed

- Empty cells and non-numeric values are automatically skipped

- Supports files with up to 1,000,000 rows


**Example Excel structure:**

```text

| A  |
|----|
| 42 |
| 15 |
| 78 |
| 23 |
| 56 |
```
## 🧪 Testing

### Running Tests

```bash
# Run all tests
mvn test

# Run with test coverage report
mvn test jacoco:report

# Run specific test class
mvn test -Dtest=ExcelServiceTest
```
### Test Coverage

The project includes comprehensive tests for:

- ✅ Controller layer (REST API)

- ✅ Service layer (business logic)

- ✅ Excel file processing

- ✅ QuickSelect algorithm

- ✅ Error handling scenarios

- ✅ Integration tests

## 🆘 Support

For support and questions:

1. Check the API documentation at `/swagger-ui.html`
2. Create an issue in the project repository