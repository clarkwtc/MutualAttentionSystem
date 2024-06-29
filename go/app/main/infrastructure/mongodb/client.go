package mongodb

import (
    "context"
    "go.mongodb.org/mongo-driver/mongo"
    "go.mongodb.org/mongo-driver/mongo/options"
    "log"
)

var connectionString = "mongodb://localhost:27017"

func Init() *mongo.Client {
    uri := options.Client().ApplyURI(connectionString)

    client, err := mongo.Connect(context.Background(), uri)
    if err != nil {
        log.Fatal(err)
    }

    err = client.Ping(context.Background(), nil)
    if err != nil {
        log.Fatal("Could not connect to MongoDB", err)
    }

    log.Println("Connected to MongoDB")
    return client
}
