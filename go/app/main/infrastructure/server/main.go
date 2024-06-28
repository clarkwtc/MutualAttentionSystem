package main

import "mutualAttentionSystem/app/main/infrastructure/endpoints"

func main() {
    route := endpoints.SetupRouter()
    err := route.Run(":8080")
    if err != nil {
        return
    }
}
