package endpoints

import (
    "github.com/gin-gonic/gin"
    "mutualAttentionSystem/app/main/infrastructure/repositories"
)

func SetupRouter() *gin.Engine {
    r := gin.Default()

    userRepository := repositories.NewUserRepository()
    relationshipRepository := repositories.NewRelationshipRepository()
    userEndpoints := NewUserResource(userRepository, relationshipRepository)

    userRoutes := r.Group("/users")
    {
        userRoutes.POST("", userEndpoints.RegisterUser)
        userRoutes.GET("", userEndpoints.GetUser)
        userRoutes.POST("/:id/follow", userEndpoints.Follow)
        userRoutes.DELETE("/:id/unfollow", userEndpoints.UnFollow)
        userRoutes.GET("/:id/followings", userEndpoints.GetFollowingList)
        userRoutes.GET("/:id/fans", userEndpoints.GetFanList)
        userRoutes.GET("/:id/friends", userEndpoints.GetFriendList)
    }

    return r
}
