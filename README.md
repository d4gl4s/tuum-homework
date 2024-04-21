# Tuum internship homework

## Table of Contents

- [How To Build and Run Application?](#how-to-build-and-run-application)
- [Important Choices](#important-choices-in-my-solution)
- [Estimating Transactions per Second](#estimating-transactions-per-second)
- [Scaling Applications Horizontally](#scaling-applications-horizontally)

## How to build and run application?


To build and run the application, follow these steps:

1. Open a terminal.

2. Navigate to the root directory of the project.

3. Run the following command to build the project using Gradle, skipping tests:
   
```code
./gradlew build --exclude-task test
```

4. After the build is successful, run the following command to start the application along with the PostgreSQL database and RabbitMQ using Docker Compose:
   
```
docker-compose up --build
```

5. Once the containers are up and running, you can access the application at `http://localhost:8080` or the RabbitMQ console at `http://localhost:15672` using `username=admin`  and `password=admin` .

6. To stop the application, press `Ctrl + C` in the terminal where Docker Compose is running, and then run:

```
docker-compose down
```
<br>

## Important Choices in My Solution

Here are some of the choices I made on my own command:

1. **Using BigDecimal:** I decided to use BigDecimal for amounts, since it is more precise than *double*.
2. **Not Using a Mapper:** Currently I am simply converting each response and request object by hand manually. This could be made better by using a mapper that does it for you. I decided to not implement it, since I was running short on time
3. **Not Creating a DTO for getTransactions:** I decided not to create a request object for the getTransactions() endpoint, since we are returning all the fields of the model anyway. This is not best practice and could be improved in the future, to allow for selection of returned fields (if we do not wish to return the ID for example).
4. **SQL Script to Create Database Tables:** I decided to use a good-old sql script to populate the database with tables. This could in theory be done with the combination of MyBatis and Hibernate, but I did not want to use hibernate in this homework.
5. **Using Enums:** I decided to use enums wherever possible, to make the code more maintainable moving forward.
6. **Dockerization:** I would have hoped to include the build process of the springboot application in the Dockerfile, but time was running short and I decided that building the .jar file manually before running containers, is good enough.
7. **Global Exception Handling:** I implemented global exception handling with custom exceptions that match our domain. This makes our endpoints more concise and easier to implement, since we do not have to catch the same exceptions in different endpoints and return appropriate responses. These custom exceptions are thrown in the service classes with meaningful messages, wherever necessary.
8. I split the business logic into two services, because that seemed more reasonable. Additionally, I made sure to only call corresponding mappers methods from services (for example avoided calling accountMapper methods from transactionService)

<br>

## Estimating Transactions per Second

I will try to estimate how many transactions can my application handle on per second on my local development machine.

<br>

## Scaling Applications Horizontally

To scale the application horizontally, I would consider the following:

* **Load balancing:** Distributing incoming requests across multiple instances of the application. 
* **Database sharding:** Partitioning data across multiple database instances to distribute the load. 
* **Message Queues:** Using RabbitMQ allows for decoupling of components, making it easier to scale independently.