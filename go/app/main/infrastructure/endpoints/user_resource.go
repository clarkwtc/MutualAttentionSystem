package endpoints

import (
    "github.com/gin-gonic/gin"
    "mutualAttentionSystem/app/main/application"
    "net/http"
)

type UserResource struct {
    registerUserUseCase *application.RegisterUserUseCase
}

func NewUserResource() *UserResource {
    return &UserResource{application.NewRegisterUserUseCase()}
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
    user := resource.registerUserUseCase.Execute(registerUserBody.Username)

    context.JSON(http.StatusCreated, user.ID)
}
