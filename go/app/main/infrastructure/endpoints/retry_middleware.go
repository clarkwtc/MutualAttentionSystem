package endpoints

import (
    "github.com/gin-gonic/gin"
    "mutualAttentionSystem/app/main/domain/exceptions"
    "net/http"
    "time"
)

func RetryMiddleware(retries int, delay time.Duration) gin.HandlerFunc {
    return func(ctx *gin.Context) {
        if ctx.Request.Method != http.MethodGet {
            return
        }

        innerDelay := delay
        for i := 0; i < retries; i++ {
            ctx.Next()

            if len(ctx.Errors) == 0 {
                return
            }

            if len(ctx.Errors) > 0 {
                err := ctx.Errors.Last().Err
                switch err.(type) {
                case exceptions.ICustomError:
                    return
                }
            }
            time.Sleep(innerDelay)
            innerDelay *= 2
        }

        ctx.Error(exceptions.NewServiceOverloadError("Max retry attempts reached"))
    }
}
