package test

import (
    "encoding/json"
    "fmt"
    "github.com/gin-gonic/gin"
    "github.com/stretchr/testify/assert"
    "mutualAttentionSystem/app/main/domain"
    "mutualAttentionSystem/app/main/infrastructure/endpoints"
    "mutualAttentionSystem/app/main/infrastructure/endpoints/dto"
    "mutualAttentionSystem/app/main/infrastructure/repositories"
    "mutualAttentionSystem/app/test/mock"
    "net/http/httptest"
    "net/url"
    "strings"
    "testing"
)

var router *gin.Engine = nil
var system = domain.NewMutualAttentionSysyem()

func init() {
    system.AddUser("sk22")
    system.AddUser("cp200")

    userRepository := repositories.NewUserRepository()
    for _, user := range system.Users {
        userRepository.Register(user)
    }

    mockRouter := mock.Router{UserRepository: userRepository,
        RelationshipRepository: repositories.NewRelationshipRepository()}

    router = mockRouter.SetupRouter()
}

func TestRegisterUser(t *testing.T) {
    // Given
    body, _ := json.Marshal(endpoints.RegisterUserBody{Username: "elk33"})

    // When
    request := httptest.NewRequest("POST", "/users", strings.NewReader(string(body)))
    response := httptest.NewRecorder()
    router.ServeHTTP(response, request)

    // Then
    assert.Equal(t, 201, response.Code)
    assert.NotEmpty(t, response.Body.String())
}

func TestGetUser(t *testing.T) {
    // Given
    user := system.Users[0]

    // When
    params := url.Values{}
    params.Add("id", user.ID.String())

    // 將查詢參數附加到基礎 URL
    fullUrl := fmt.Sprintf("%s?%s", "/users", params.Encode())
    request := httptest.NewRequest("GET", fullUrl, nil)
    response := httptest.NewRecorder()
    router.ServeHTTP(response, request)

    // Then
    var getUserDTO dto.GetUserDTO
    err := json.NewDecoder(response.Body).Decode(&getUserDTO)
    if err != nil {
        return
    }

    assert.Equal(t, 201, response.Code)
    assert.Equal(t, user.ID.String(), getUserDTO.Id)
    assert.Equal(t, user.Username, getUserDTO.Username)
    assert.Equal(t, 0, getUserDTO.Followings)
    assert.Equal(t, 0, getUserDTO.Friends)
    assert.Equal(t, 0, getUserDTO.Fans)
}
