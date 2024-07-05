package exceptions

import (
    "github.com/gin-gonic/gin"
    "net/http"
)

type ServiceOverloadErrorHandler struct {
    StatusCode int
    Error      error
    ErrorHandler
}

func NewServiceOverloadErrorHandler() *ServiceOverloadErrorHandler {
    return &ServiceOverloadErrorHandler{StatusCode: http.StatusServiceUnavailable, Error: &ServiceOverloadError{}}
}

func (handler *ServiceOverloadErrorHandler) Match(error error) bool {
    switch error.(type) {
    case *ServiceOverloadError:
        return true
    default:
        return false
    }
}

func (handler *ServiceOverloadErrorHandler) Response(ctx *gin.Context) {
    err := ctx.Errors.Last().Err
    ctx.JSON(handler.StatusCode, gin.H{"error": err.Error()})
}
