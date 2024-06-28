package server

import (
    "github.com/gin-gonic/gin"
    "mutualAttentionSystem/app/main/infrastructure/endpoints"
)

func SetupRouter() *gin.Engine {
    r := gin.Default()

    userEndpoints := endpoints.NewUserResource()

    userRoutes := r.Group("/users")
    {
        userRoutes.POST("", userEndpoints.RegisterUser)
        userRoutes.GET("/")
        userRoutes.POST("/:id/follow")
        userRoutes.GET("/:id/followings")
        userRoutes.DELETE("/:id/unfollow")
        userRoutes.GET("/:id/fans")
        userRoutes.GET("/:id/friends")
    }

    return r
}
