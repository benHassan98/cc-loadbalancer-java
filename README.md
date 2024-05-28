# Build Your Own Load Balancer

https://codingchallenges.fyi/challenges/challenge-load-balancer

## Description

This is a java Spring implementation of a basic load balancer with round robin algorithms

## Usage

To start the server:
1. you need maven and java to be installed
2. you need a backend server with '/' and '/health' endpoints that respond with 200 status code
3. navigate to the project root directory
4. run the following command
```bash
mvn package -DskipTests
```
5. run the following command
```bash
java -Dservers=(comma separated values of backend urls) -jar (jar file of prev step)
```
