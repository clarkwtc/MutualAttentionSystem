package mock

import (
    "github.com/gin-gonic/gin"
    "mutualAttentionSystem/app/main/application"
    "mutualAttentionSystem/app/main/domain"
    "mutualAttentionSystem/app/main/infrastructure/endpoints"
)

type Router struct {
    UserRepository         domain.IUserRepository
    RelationshipRepository domain.IRelationshipRepository
}

func (mockRouter *Router) SetupRouter() *gin.Engine {
    r := gin.Default()

    userEndpoints := endpoints.UserResource{
        RegisterUserUseCase: &application.RegisterUserUseCase{UserRepository: mockRouter.UserRepository},
        GetUserUseCase: &application.GetUserUseCase{UserRepository: mockRouter.UserRepository,
            RelationshipRepository: mockRouter.RelationshipRepository},
    }

    userRoutes := r.Group("/users")
    {
        userRoutes.POST("", userEndpoints.RegisterUser)
        userRoutes.GET("", userEndpoints.GetUser)
        userRoutes.POST("/:id/follow")
        userRoutes.GET("/:id/followings")
        userRoutes.DELETE("/:id/unfollow")
        userRoutes.GET("/:id/fans")
        userRoutes.GET("/:id/friends")
    }

    return r
}
