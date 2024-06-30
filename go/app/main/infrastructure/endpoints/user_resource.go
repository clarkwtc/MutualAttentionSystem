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

func (resource *UserResource) RegisterUser(ctx *gin.Context) {
    var registerUserBody RegisterUserBody
    err := ctx.ShouldBindBodyWithJSON(&registerUserBody)
    if err != nil {
        ctx.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
        return
    }
    user := resource.RegisterUserUseCase.Execute(registerUserBody.Username)

    ctx.JSON(http.StatusCreated, user.ID)
}

type GetUserQuery struct {
    Id string `form:"id"`
}

func (resource *UserResource) GetUser(ctx *gin.Context) {
    var getUserQuery GetUserQuery

    err := ctx.ShouldBindQuery(&getUserQuery)
    if err != nil {
        ctx.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
        return
    }
    user := resource.GetUserUseCase.Execute(getUserQuery.Id)

    getUserDTO := dto.ToGetUserDTO(user)
    ctx.JSON(http.StatusCreated, getUserDTO)
}

type UserUri struct {
    Id string `uri:"id"`
}

type FollowBody struct {
    FollowingId string `json:"followingId"`
}

func (resource *UserResource) Follow(ctx *gin.Context) {
    var userUri UserUri

    err := ctx.ShouldBindUri(&userUri)
    if err != nil {
        ctx.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
        return
    }

    var followBody FollowBody

    err = ctx.ShouldBindBodyWithJSON(&followBody)
    if err != nil {
        ctx.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
        return
    }

    resource.FollowUserUseCase.Execute(userUri.Id, followBody.FollowingId)

    ctx.JSON(http.StatusOK, nil)
}

func (resource *UserResource) UnFollow(ctx *gin.Context) {
    var userUri UserUri

    err := ctx.ShouldBindUri(&userUri)
    if err != nil {
        ctx.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
        return
    }

    var followBody FollowBody

    err = ctx.ShouldBindBodyWithJSON(&followBody)
    if err != nil {
        ctx.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
        return
    }

    resource.UnFollowUserUseCase.Execute(userUri.Id, followBody.FollowingId)

    ctx.JSON(http.StatusOK, nil)
}

func (resource *UserResource) GetFollowingList(ctx *gin.Context) {
    var userUri UserUri
    err := ctx.ShouldBindUri(&userUri)
    if err != nil {
        ctx.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
        return
    }

    user := resource.GetUserUseCase.Execute(userUri.Id)

    getFollowingListDTO := dto.ToGetFollowingListDTO(user.GetFollowings())
    ctx.JSON(http.StatusOK, getFollowingListDTO)
}
