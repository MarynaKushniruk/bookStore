
# 📚 Online Book Store project
## Description

Book store is a service which allows register customers and buy books. This API supports authentication, registration, CRUD (Create, Read, Update,Delete) operations with entity of database.




## Project Technology Stack: 💻🔧📚

* Programming Language : ***Java 17***
* Framework: Spring Boot 3.1.4
* Spring Security and JWT: For authentication and security
* Docker 4.24.0
* Logging System: Log4j2
* Testing: JUnit
* Database: MySQL 8.0.21
* Liquibase: For database version management
* Lombok: For boilerplate code generation
* MapStruct: For mapper generation
* Testcontainers: For integration tests with the MySQL test database



# Endpoints available for used api
## Available for non authenticated users:
- POST: /api/auth/register - register a new user,
- POST: /api/auth/login - authenticate in system.
## Available for users with role USER
- GET: /api/books - get list of all books,
- GET: /api/books/{id} - get a book by a book id,
- GET: /api/categories - get list of all categories,
- GET: /api/categories/{id} - get a category by a category id,
- GET: /api/categories/{id}/books - get list of books by category id,
- GET: /api/cart - get books from shopping cart,
- POST: /api/cart - add a book to shopping cart,
- PUT: /api/cart/cart-items/{cartItemId} - update book quantity in shopping cart,
- DELETE: /api/cart/cart-items/{cartItemId} - delete a book from shopping cart,
- GET: /api/orders - get list of all orders by user
- POST: /api/orders - create a new order,
- GET: /api/orders/{orderId}/items - get list of all order items by order id,
- GET: /api/orders/{orderId}/items/{id} - get a order item by order item id.
## Available for users with role ADMIN
- POST: /api/books/ - create a new book,
- PUT: /api/books/{id} - update data about book,
- DELETE: /api/books/{id} - delete a book,
- POST: /api/categories - create a new category,
- PUT: /api/categories/{id} - update info about category,
- DELETE: /api/categories/{id} - delete a category,
- PUT: /api/orders/{orderId} - update a status of user`s order

# Note on use the api
Please note that when using the api endpoints with methods POST, PUT required JSON body. Security builds on
technology jackson web token (JWT) with using Bearer token.

# Instructions:
- To run the book store API on a server, you will need to first run Docker.
- After running api database in first time will create all necessary tables in database
- To register an Administrator in the system, you will need to use the endpoint "POST: /api/auth/register" just like a usual user.
  Once the Administrator user is registered, you can add a record in the table user_roles with the following query:
  "INSERT INTO user_roles (user_id, role_id) values(ID_Administrator, 2);" where "ID_Administrator" is the ID of the Administrator from the table "users". This will assign the "ADMIN" role to the newly registered user.
- All credentials for connect to database you can set in file ".env"

# User Registration : 📌 

1. Use the POST method:
```code
/api/auth/register
```
Example : 
```json
{
"email": "User1@example.com",
"password": "securePassword444",
"repeatPassword": "securePassword444",
"firstName": "Maria",
"lastName": "Smith",
"shippingAddress": "44 Main St, City, Country "
}
```
The expected response will include the user's identifier and other user-related information.
![alt text](https://github.com/MarynaKushniruk/bookStore/blob/mastertest/Registration.png)
2. User Authentication: 📌 
* Use the POST method:
```code 
/api/auth/login
```
Example :
```json
{
"email": "User1@example.com",
"password": "securePassword444"
}
```
***The expected response will contain an access token that needs to be used for further requests.***
![alt text](https://github.com/MarynaKushniruk/bookStore/blob/mastertest/Login.png)

For a better understanding, you can check out this short tutorial on how to register a new user, log in and make requests to endpoints as a User :
- https://www.loom.com/share/bccce0c8797d4b0bb274df11dbbbb74c?sid=20434c71-02b4-4738-a1d6-330afdeaeecc





