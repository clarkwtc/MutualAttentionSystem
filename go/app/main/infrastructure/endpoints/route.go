package endpoints

import (
    "github.com/gin-gonic/gin"
    "go.mongodb.org/mongo-driver/mongo"
    "mutualAttentionSystem/app/main/domain/errors"
    "mutualAttentionSystem/app/main/infrastructure/mongodb"
    "mutualAttentionSystem/app/main/infrastructure/repositories"
    "time"
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

func (router *Router) SetupErrorMiddleware() {
    errorMiddleware := ErrorMiddleware{}
    errorMiddleware.RegisterException(errors.NewDuplicatedUserErrorHandler())
    errorMiddleware.RegisterException(errors.NewNotExistUserErrorHandler())
    errorMiddleware.RegisterException(errors.NewServiceOverloadErrorHandler())
    errorMiddleware.RegisterException(errors.NewRequestLimitReachedErrorHandler())
    errorMiddleware.RegisterException(errors.NewPageSizeTooLargeErrorHandler())
    router.Engine.Use(errorMiddleware.Execute())
}

func (router *Router) SetupRetryMiddleware() {
    router.Engine.Use(RetryMiddleware(3, 200*time.Millisecond))
}

func (router *Router) SetupRateLimiterMiddleware() {
    middleware := NewRateLimiterMiddleware(5000, time.Second, 1)
    router.Engine.Use(middleware.Execute())
}
