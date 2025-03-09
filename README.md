# Blogwaves 📝🚀  

## Description  
**Blogwaves** is a backend-only application built using **Spring Boot** and **RESTful APIs** for managing blog-related operations. It is designed to be scalable, efficient, and cloud-ready. The project integrates **MySQL** with **Hibernate/JPA** for seamless database interaction and is deployed on **AWS Elastic Beanstalk**, with its database configured using **Amazon RDS**. API documentation is available via **Swagger (OpenAPI)**.  

## Features  
- 🌍 **RESTful APIs** for blog management  
- 🗄️ **Database Integration** using PostgreSQL/MySQL with Hibernate/JPA  
- 📜 **API Documentation** with Swagger (OpenAPI)  
- ☁️ **Deployed on AWS Elastic Beanstalk**  
- 🛢️ **Amazon RDS Configuration** for database storage  
- 🔑 **JWT Authentication** for secure access  

## Tech Stack  
- **Spring Boot**  
- **Spring Data JPA (Hibernate)**  
- **MySQL**  
- **Swagger (OpenAPI)**  
- **AWS Elastic Beanstalk**  
- **Amazon RDS**  
- **JWT Authentication**  

## Setup Instructions  

### Prerequisites  
- Java 17+  
- Maven  
- MySQL  
- AWS Account (for deployment)  

### Steps to Run Locally  
1. Clone the repository:  
   ```sh
   git clone https://github.com/yourusername/blogwaves.git
   cd blogwaves
   ```  
2. Configure the database in `application.properties`:  
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/blogwaves  
   spring.datasource.username=your_db_user  
   spring.datasource.password=your_db_password  
   ```  
3. Run the application:  
   ```sh
   mvn spring-boot:run
   ```  

### Deployment on AWS  
1. Package the application:  
   ```sh
   mvn clean package  
   ```  
2. Deploy the generated `.jar` file to **AWS Elastic Beanstalk**.  
3. Configure **Amazon RDS** as the database source.  
4. Access the APIs via the AWS-hosted endpoint.  

## API Documentation  
Swagger UI is available at:  
```
http://localhost:8080/swagger-ui.html
```
(Replace `localhost` with your AWS domain after deployment)  

## License  
This project is open-source and available under the MIT License.  
