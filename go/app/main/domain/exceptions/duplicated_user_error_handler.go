package exceptions

import (
    "github.com/gin-gonic/gin"
    "net/http"
)

type DuplicatedUserErrorHandler struct {
    StatusCode int
    Error      *DuplicatedUserError
    ErrorHandler
}

func NewDuplicatedUserErrorHandler() *DuplicatedUserErrorHandler {
    return &DuplicatedUserErrorHandler{StatusCode: http.StatusConflict, Error: &DuplicatedUserError{}}
}

func (handler *DuplicatedUserErrorHandler) Match(error error) bool {
    switch error.(type) {
    case *DuplicatedUserError:
        return true
    default:
        return false
    }
}

func (handler *DuplicatedUserErrorHandler) Response(ctx *gin.Context) {
    ctx.JSON(handler.StatusCode, gin.H{"error": DuplicatedUsername})
}
