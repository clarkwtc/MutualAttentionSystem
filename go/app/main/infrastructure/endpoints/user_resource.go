package endpoints

import (
    "github.com/gin-gonic/gin"
    "mutualAttentionSystem/app/main/application"
    "mutualAttentionSystem/app/main/domain"
    "mutualAttentionSystem/app/main/infrastructure/endpoints/dto"
    "net/http"
)

type UserResource struct {
    RegisterUserUseCase *application.RegisterUserUseCase
    GetUserUseCase      *application.GetUserUseCase
    FollowUserUseCase   *application.FollowUserUsecase
    UnFollowUserUseCase *application.UnFollowUserUseCase
}

func NewUserResource(userRepository domain.IUserRepository, relationshipRepository domain.IRelationshipRepository) *UserResource {
    return &UserResource{
        &application.RegisterUserUseCase{UserRepository: userRepository},
        &application.GetUserUseCase{UserRepository: userRepository, RelationshipRepository: relationshipRepository},
        &application.FollowUserUsecase{UserRepository: userRepository, RelationshipRepository: relationshipRepository},
        &application.UnFollowUserUseCase{UserRepository: userRepository, RelationshipRepository: relationshipRepository},
    }
}

type RegisterUserBody struct {
    Username string `json:"username"`
}

func (resource *UserResource) RegisterUser(context *gin.Context) {
    var registerUserBody RegisterUserBody
    err := context.ShouldBindBodyWithJSON(&registerUserBody)
    if err != nil {
        context.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
        return
    }
    user := resource.RegisterUserUseCase.Execute(registerUserBody.Username)

    context.JSON(http.StatusCreated, user.ID)
}

type GetUserQuery struct {
    Id string `form:"id"`
}

func (resource *UserResource) GetUser(context *gin.Context) {
    var getUserQuery GetUserQuery

    err := context.ShouldBindQuery(&getUserQuery)
    if err != nil {
        context.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
        return
    }
    user := resource.GetUserUseCase.Execute(getUserQuery.Id)

    userDTO := dto.ToGetUserDTO(user)
    context.JSON(http.StatusCreated, userDTO)
}

type UserUri struct {
    Id string `uri:"id"`
}

type FollowBody struct {
    FollowingId string `json:"followingId"`
}

func (resource *UserResource) Follow(context *gin.Context) {
    var userUri UserUri

    err := context.ShouldBindUri(&userUri)
    if err != nil {
        context.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
        return
    }

    var followBody FollowBody

    err = context.ShouldBindBodyWithJSON(&followBody)
    if err != nil {
        context.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
        return
    }

    resource.FollowUserUseCase.Execute(userUri.Id, followBody.FollowingId)

    context.JSON(http.StatusOK, nil)
}

func (resource *UserResource) UnFollow(context *gin.Context) {
    var userUri UserUri

    err := context.ShouldBindUri(&userUri)
    if err != nil {
        context.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
        return
    }

    var followBody FollowBody

    err = context.ShouldBindBodyWithJSON(&followBody)
    if err != nil {
        context.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
        return
    }

    resource.UnFollowUserUseCase.Execute(userUri.Id, followBody.FollowingId)

    context.JSON(http.StatusOK, nil)
}
