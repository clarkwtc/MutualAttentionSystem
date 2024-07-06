package errors

import (
    "github.com/gin-gonic/gin"
    "net/http"
)

type PageSizeTooLargeErrorHandler struct {
    StatusCode int
    Error      error
    ErrorHandler
}

func NewPageSizeTooLargeErrorHandler() *PageSizeTooLargeErrorHandler {
    return &PageSizeTooLargeErrorHandler{StatusCode: http.StatusTooManyRequests, Error: &PageSizeTooLargeError{}}
}

func (handler *PageSizeTooLargeErrorHandler) Match(error error) bool {
    switch error.(type) {
    case *PageSizeTooLargeError:
        return true
    default:
        return false
    }
}

func (handler *PageSizeTooLargeErrorHandler) Response(ctx *gin.Context) {
    ctx.JSON(handler.StatusCode, gin.H{"error": PageSizeTooLarge})
}
