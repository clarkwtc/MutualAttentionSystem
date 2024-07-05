package endpoints

import (
    "github.com/gin-gonic/gin"
    "mutualAttentionSystem/app/main/domain/exceptions"
)

type ErrorMiddleware struct {
    exceptions.ErrorHandler
}

func (handler *ErrorMiddleware) RegisterException(exceptionHandler exceptions.IErrorHandler) {
    exceptionHandler.SetNext(handler.Concrete)
    handler.Concrete = exceptionHandler
}

func (handler *ErrorMiddleware) ErrorMiddleware() gin.HandlerFunc {
    return func(ctx *gin.Context) {
        ctx.Next()

        if len(ctx.Errors) > 0 {
            err := ctx.Errors.Last().Err
            handler.Handle(ctx, err)
        }
    }
}
