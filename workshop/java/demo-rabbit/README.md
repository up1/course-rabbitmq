# Demo with RabbitMQ

Start server
```
$mvnw spring-boot:run
```

Send message to queue = `q.hello`
```
$curl http://localhost:8080/demo/test1
```