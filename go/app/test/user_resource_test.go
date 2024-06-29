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
var userRepository = repositories.NewUserRepository()
var relationshipRepository = repositories.NewRelationshipRepository()

func init() {
    system.AddUser("sk22")
    system.AddUser("cp200")

    for _, user := range system.Users {
        userRepository.Save(user)
    }

    mockRouter := mock.Router{UserRepository: userRepository,
        RelationshipRepository: relationshipRepository}

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

func TestFollowUser(t *testing.T) {
    // Given
    user := system.Users[0]
    following := system.Users[1]

    // When
    body, _ := json.Marshal(endpoints.FollowBody{FollowingId: following.ID.String()})

    uri := fmt.Sprintf("/users/%s/follow", user.ID.String())
    request := httptest.NewRequest("POST", uri, strings.NewReader(string(body)))
    response := httptest.NewRecorder()
    router.ServeHTTP(response, request)

    // Then
    relationships := relationshipRepository.Find(following.ID, user.ID)
    assert.Equal(t, 200, response.Code)
    assert.Equal(t, 1, len(relationships))
    assert.Equal(t, false, relationships[0].IsFriend)
}

func TestUnFollowUser(t *testing.T) {
    // Given
    user := system.Users[0]
    following := system.Users[1]
    user.Follow(following)
    following.Follow(user)
    relationshipRepository.Save(user.Relationships)
    assert.Equal(t, 2, len(relationshipRepository.FindUserId(user.ID)))
    assert.Equal(t, 2, len(relationshipRepository.FindUserId(following.ID)))
    assert.Equal(t, true, user.IsFriend(following))

    // When
    body, _ := json.Marshal(endpoints.FollowBody{FollowingId: following.ID.String()})

    uri := fmt.Sprintf("/users/%s/unfollow", user.ID.String())
    request := httptest.NewRequest("DELETE", uri, strings.NewReader(string(body)))
    response := httptest.NewRecorder()
    router.ServeHTTP(response, request)

    // Then
    user = userRepository.Find(user.ID)
    assert.Equal(t, 200, response.Code)
    assert.Equal(t, 1, len(relationshipRepository.FindUserId(user.ID)))
    assert.Equal(t, 1, len(relationshipRepository.FindUserId(following.ID)))
    assert.Equal(t, false, user.IsFriend(following))
}
