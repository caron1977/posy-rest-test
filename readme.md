# How to call rest services using a shell 

## prerequisites for windows

curl installed, https://sourceforge.net/projects/getgnuwin32/  [GnuWin32]  
jq installed,  https://stedolan.github.io/jq/ [jq]  

## POST find-exam-registrations

### using localhost
```
curl -H "Content-Type: application/json" -X POST -d @json-find-registrations-course.json http://localhost:8080/posy-rest/rest/posy-rest-service/find-exam-registrations | jq -r

curl -H "Content-Type: application/json" -X POST -d @json-find-registrations-faculty.json http://localhost:8080/posy-rest/rest/posy-rest-service/find-exam-registrations | jq -r "." > d:\tmp\testcurl.json
```

### using java-test
```
curl -u <user>:<password>  -H "Content-Type: application/json" -X POST -d @json-find-registrations-schedule.json https://java-test.fhws.de/posy-rest/rest/posy-rest-service/find-exam-registrations | jq -r .
```

## POST record-many-grades

### using localhost
```
curl -H "Content-Type: application/json" -X POST -d @json-many-grades.json http://localhost:8080/posy-rest/rest/posy-rest-service/record-many-grades | jq -r
```

### using java-test.fhws.de
```
curl -u <user>:<password> -H "Content-Type: application/json" -X POST -d @json-many-grades.json  https://java-test.fhws.de/posy-rest/rest/posy-rest-service/record-many-grades | jq -r
```
