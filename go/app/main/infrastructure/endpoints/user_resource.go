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
}

func NewUserResource(userRepository domain.IUserRepository, relationshipRepository domain.IRelationshipRepository) *UserResource {
    return &UserResource{
        &application.RegisterUserUseCase{UserRepository: userRepository},
        &application.GetUserUseCase{UserRepository: userRepository, RelationshipRepository: relationshipRepository},
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

func (resource *UserResource) GetUser(context *gin.Context) {
    id := context.Query("id")
    user := resource.GetUserUseCase.Execute(id)

    userDTO := dto.ToGetUserDTO(user)
    context.JSON(http.StatusCreated, userDTO)
}
