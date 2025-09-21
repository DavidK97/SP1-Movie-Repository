# SP1 - Movie Repository
- This project is a group project made for the 3rd semester of the Datamatiker education.
- The projects goal is to fetch information about Movies, Actors, Directors and Genres from The Movie DataBase API (TMDB) and store it locally in a SQL-database using Hibernate/JPA, from where we can retrieve and work with the data using JQPL and Streams 

## Architecture
The architecture consists of:

TMDB API ↔ Main ↔ Service Layer ↔ DAO Layer ↔ Database

- Service Layer: Handles business logic, DTO ↔ Entity conversion, and communicates with the DAO-layer.  
- DAO Layer: Responsible for database operations using Hibernate/JPA.  
- Database: stores Movies, Actors, Directors, and Genres.  

## Tools/Technologies/Dependencies
- Java 17
- Hibernate / JPA : ORM-tool used for managing entities 
- PostgreSQL : Database
- Jackson : Converting Json, fetched from TMDB API, to DTO's 
- Lombok : Used to reduce boilerplate code in the Entity-classes
- JUnit 5 : Used for testing
- Hamcrest : Used for testing
- Docker : For running PostgreSQL in a container

## Running the project
To run the project you will need to apply for a free TMDB API-key and add it to your environment-variables: https://www.themoviedb.org/subscribe?language=da-DK

