package endpoints

import (
    "github.com/gin-gonic/gin"
    "go.mongodb.org/mongo-driver/mongo"
    "mutualAttentionSystem/app/main/infrastructure/mongodb"
    "mutualAttentionSystem/app/main/infrastructure/repositories"
)

type Router struct {
    Engine      *gin.Engine
    MongoClient *mongo.Client
}

func (router *Router) SetupUserResource() {
    userRepository := repositories.NewUserRepository(mongodb.NewUserRepository(router.MongoClient))
    relationshipRepository := repositories.NewRelationshipRepository(mongodb.NewRelationshipRepository(router.MongoClient))
    userEndpoints := NewUserResource(userRepository, relationshipRepository)
    userRoutes := router.Engine.Group("/users")
    {
        userRoutes.POST("", userEndpoints.RegisterUser)
        userRoutes.GET("", userEndpoints.GetUser)
        userRoutes.POST("/:id/follow", userEndpoints.Follow)
        userRoutes.DELETE("/:id/unfollow", userEndpoints.UnFollow)
        userRoutes.GET("/:id/followings", userEndpoints.GetFollowingList)
        userRoutes.GET("/:id/fans", userEndpoints.GetFanList)
        userRoutes.GET("/:id/friends", userEndpoints.GetFriendList)
    }
}
