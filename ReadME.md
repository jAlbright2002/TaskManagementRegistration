
# Registration Service

## Getting Started

These instructions will give you a copy of the project up and running on
your local machine for development and testing purposes. 

### Prerequisites

Requirements for the software and other tools to build, test and push 
- [Java](https://www.oracle.com/java/technologies/downloads/#jdk21-windows)
	- Download Java 21
	- Run installer, keep all defaults
	- Set JAVA_HOME environment variable - [Follow answer by Taras Melnyk](https://stackoverflow.com/questions/11161248/setting-java-home)
- [Docker Desktop](https://www.docker.com/products/docker-desktop/)
	- [Windows | Docker Docs](https://docs.docker.com/desktop/setup/install/windows-install/#install-docker-desktop-on-windows) follow this guide to install Docker on Windows

## Running the Application

[Download](https://github.com/jAlbright2002/TaskManagementRegistration.git) and open the project in an IDE or locate the root directory within your terminal

Run the following command, this will pull and run the docker images for the project, RabbitMQ and Mongo

	docker-compose up

The project will now be running and you can access its endpoints ->

[Register](http://localhost:8081/register)
[Login](http://localhost:8081/login)

Both are POST requests and must contain a body

Example JSON can be found [here](https://github.com/jAlbright2002/TaskManagementRegistration/blob/master/src/main/resources/ExampleJSON)

It is recommended to use an API testing tool such as [Postman](https://www.postman.com/downloads/) or [Talend API Extension](https://chromewebstore.google.com/detail/talend-api-tester-free-ed/aejoelaoggembcahagimdiliamlcdmfm) 
(Note: Must use a Chromium based browser to use this extension)

## Project Architecture
The **Task Management Registration Service** was made to create and login users to access the Task Management operations. It is written in Java and built on SpringBoot. It uses RabbitMQ (message queues) to communicate between the other services and stores user information into MongoDB. The users password is encrypted with BCrypt and once logged in, receives a token which is used to verify the user in the Task Management Service.


## Running the Tests
[Download](https://github.com/jAlbright2002/TaskManagementRegistration.git) and open the project in an IDE or locate the root directory within your terminal and run the following command

	.\mvnw verify

This will test all unit and integration tests

## Authors
  - **James Albright** - *Project Owner* -
  - **Billie Thompson** - *Provided README Template* -
    [PurpleBooth](https://github.com/PurpleBooth)
