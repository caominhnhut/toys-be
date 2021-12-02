## Installation
- Install the docker to the machine
- Go to toys-be/document folder to start the PostgresSQL database
```bash
docker-compose -f .\docker-compose.postgres.yml up -d
```
- Open termimal/cmd type the below command to see if the database works
```
docker ps
```
- Build project with below command or you can create a Configuration Maven in IDE
```
mvn clean package
```
- Right click on ToysBeApplication to run the project.
- Refer to resources/api to see the open-api file.