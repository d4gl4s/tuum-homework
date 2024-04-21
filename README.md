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

4. After the build is successful, run the following command to start the application along with the PostgreSQL database using Docker Compose:
   
```
docker-compose up --build
```

5. Once the containers are up and running, you can access the application at `http://localhost:8080`.

6. To stop the application, press `Ctrl + C` in the terminal where Docker Compose is running, and then run:

```
docker-compose down
```
<br>

## Important Choices in My Solution

## Estimating Transactions per Second

I will try to estimate how many transactions can my application handle on per second on my local development machine.

## Scaling Applications Horizontally

To scale the application horizontally, I would consider the following:

* **Load balancing:** Distributing incoming requests across multiple instances of the application. 
* **Database sharding:** Partitioning data across multiple database instances to distribute the load. 
* **Message Queues:** Using RabbitMQ allows for decoupling of components, making it easier to scale independently.