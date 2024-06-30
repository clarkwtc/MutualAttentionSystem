package main

import (
    "github.com/gin-gonic/gin"
    "mutualAttentionSystem/app/main/infrastructure/endpoints"
    "mutualAttentionSystem/app/main/infrastructure/mongodb"
)

func main() {
    client := mongodb.Init()
    router := endpoints.Router{Engine: gin.Default(), MongoClient: client}
    router.SetupUserResource()
    err := router.Engine.Run(":8080")
    if err != nil {
        return
    }
}
