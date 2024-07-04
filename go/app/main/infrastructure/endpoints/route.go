package endpoints

import (
    "github.com/gin-gonic/gin"
    "go.mongodb.org/mongo-driver/mongo"
    "mutualAttentionSystem/app/main/domain/exceptions"
    "mutualAttentionSystem/app/main/infrastructure/mongodb"
    "mutualAttentionSystem/app/main/infrastructure/repositories"
)

type Router struct {
    Engine      *gin.Engine
    MongoClient *mongo.Client
    exceptions.ErrorHandler
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

func (router *Router) SetupErrorHandler() {
    router.registerException(exceptions.NewDuplicatedUserErrorHandler())
    router.registerException(exceptions.NewNotExistUserErrorHandler())

    router.Engine.Use(router.errorHandler())
}

func (router *Router) registerException(exceptionHandler exceptions.IErrorHandler) {
    exceptionHandler.SetNext(router.ErrorHandler.Concrete)
    router.ErrorHandler.Concrete = exceptionHandler
}

func (router *Router) errorHandler() gin.HandlerFunc {
    return func(ctx *gin.Context) {
        ctx.Next()

        if len(ctx.Errors) > 0 {
            err := ctx.Errors.Last().Err
            router.Handle(ctx, err)
        }
    }
}
