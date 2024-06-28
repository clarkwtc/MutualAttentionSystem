package test

import (
    "encoding/json"
    "github.com/gin-gonic/gin"
    "github.com/stretchr/testify/assert"
    "mutualAttentionSystem/app/main/domain"
    "mutualAttentionSystem/app/main/infrastructure/endpoints"
    "mutualAttentionSystem/app/main/infrastructure/server"
    "net/http/httptest"
    "strings"
    "testing"
)

var system = domain.NewMutualAttentionSysyem()

func getRouter() *gin.Engine {
    return server.SetupRouter()
}

func init() {
    system.AddUser("sk22")
    system.AddUser("cp200")
}

func TestRegisterUser(t *testing.T) {
    // Given
    router := getRouter()

    // When

    body, _ := json.Marshal(endpoints.RegisterUserBody{Username: "elk33"})
    request := httptest.NewRequest("POST", "/users", strings.NewReader(string(body)))

    response := httptest.NewRecorder()
    router.ServeHTTP(response, request)

    // Then
    assert.Equal(t, 201, response.Code)
    assert.NotEmpty(t, response.Body.String())
}
