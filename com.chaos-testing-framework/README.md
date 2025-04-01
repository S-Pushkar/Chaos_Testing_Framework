# Chaos Testing Framework

## Sample Docker Run Command for testing the fault injection

```bash
docker run --name mongo --memory=2048m --cpu-quota=20000 --cpu-period=1000 -p 27018:27017 -d mongo
```

## Sample URL for testing the fault injection

```
http://localhost:8080/api/fault-test/DECREASE_RAM?containerName=mongo
```