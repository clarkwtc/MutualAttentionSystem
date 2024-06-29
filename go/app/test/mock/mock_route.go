package mock

import (
    "mutualAttentionSystem/app/main/domain"
    "mutualAttentionSystem/app/main/infrastructure/endpoints"
)

type Router struct {
    UserRepository         domain.IUserRepository
    RelationshipRepository domain.IRelationshipRepository
    endpoints.Router
}

func (mockRouter *Router) SetupMockUserResource() {
    userRepository := mockRouter.UserRepository
    relationshipRepository := mockRouter.RelationshipRepository
    userEndpoints := endpoints.NewUserResource(userRepository, relationshipRepository)

    userRoutes := mockRouter.Engine.Group("/users")
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
