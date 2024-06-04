# Getting-started

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

## System designing diagram
![alt text](images/MututalAttentionSystemDesign.png)

## Table schema
User 
key | Type 
--- | --- 
id | string 
username | string
createdAt | date

Relationship 
key | Type 
--- | --- 
id | string 
followingId | string
fanId | string
isFriend | boolean
createdAt | date
