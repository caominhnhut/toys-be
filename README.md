## Installation Project
- Install the docker to the machine
- Go to toys-be/document folder to start the PostgresSQL database
```bash
docker-compose -f .\docker-compose.postgres.yml up -d
```
- Go to toys-be/document folder to start the mongodb
```bash
docker-compose -f .\docker-compose.mongo.yml up --build -d
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
## Installation Kafka
- Go to toys-be/document folder to start the Kafka
```bash
docker-compose -f .\docker-compose.kafka.yml up -d
```
- Create a topic "kafka-toyapp-chat" in kafka
```bash
kafka-topics.sh --create --partitions 1 --replication-factor 1 --topic kafka-toyapp-chat --bootstrap-server localhost:9093
```
- Verify the topic "kafka-toyapp-chat" after creating
```bash
kafka-topics.sh --list  --bootstrap-server localhost:9093
```
- Delete a specific topic "kafka-toyapp-chat" if needed
```bash
 kafka-topics.sh --bootstrap-server localhost:9093 --delete --topic kafka-toyapp-chat
```
- List out message that is received by customer
```bash
 kafka-console-consumer.sh --bootstrap-server localhost:9093 --topic kafka-toyapp-chat
```