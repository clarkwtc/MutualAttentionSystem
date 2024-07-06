package exceptions

import (
    "github.com/gin-gonic/gin"
    "net/http"
)

type RequestLimitReachedErrorHandler struct {
    StatusCode int
    Error      error
    ErrorHandler
}

func NewRequestLimitReachedErrorHandler() *RequestLimitReachedErrorHandler {
    return &RequestLimitReachedErrorHandler{StatusCode: http.StatusTooManyRequests, Error: &RequestLimitReachedError{}}
}

func (handler *RequestLimitReachedErrorHandler) Match(error error) bool {
    switch error.(type) {
    case *RequestLimitReachedError:
        return true
    default:
        return false
    }
}

func (handler *RequestLimitReachedErrorHandler) Response(ctx *gin.Context) {
    ctx.JSON(handler.StatusCode, gin.H{"error": RequestTooMany})
}
