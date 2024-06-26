package mock

import (
    "github.com/gin-gonic/gin"
    "mutualAttentionSystem/app/main/domain"
    "mutualAttentionSystem/app/main/infrastructure/endpoints"
)

type Router struct {
    UserRepository         domain.IUserRepository
    RelationshipRepository domain.IRelationshipRepository
}

func (mockRouter *Router) SetupRouter() *gin.Engine {
    r := gin.Default()

    userRepository := mockRouter.UserRepository
    relationshipRepository := mockRouter.RelationshipRepository
    userEndpoints := endpoints.NewUserResource(userRepository, relationshipRepository)

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
