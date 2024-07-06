package endpoints

import (
    "github.com/gin-gonic/gin"
    "mutualAttentionSystem/app/main/domain/exceptions"
    "net/http"
    "sync"
    "time"
)

type RateLimiterMiddleware struct {
    capcacity       int
    tokens          int
    lastRefillTime  time.Time
    refiillInterval time.Duration
    refillAmount    int
    mutex           sync.Mutex
}

func NewRateLimiterMiddleware(capcacity int, refiillInterval time.Duration, refillAmount int) *RateLimiterMiddleware {
    return &RateLimiterMiddleware{capcacity: capcacity, tokens: capcacity, lastRefillTime: time.Now(),
        refiillInterval: refiillInterval, refillAmount: refillAmount}
}

func (handler *RateLimiterMiddleware) Execute() gin.HandlerFunc {
    return func(ctx *gin.Context) {
        if ctx.Request.Method == http.MethodGet {
            return
        }
        handler.mutex.Lock()
        defer handler.mutex.Unlock()

        handler.refillToken()
        if handler.tokens > 0 {
            handler.tokens -= 1
            ctx.Next()
        } else {
            ctx.Error(&exceptions.RequestLimitReachedError{})
        }
    }
}

func (handler *RateLimiterMiddleware) refillToken() {
    currentTime := time.Now()
    pastTime := currentTime.Sub(handler.lastRefillTime)
    addTokens := int(pastTime/handler.refiillInterval) * handler.refillAmount
    handler.tokens = min(handler.tokens+addTokens, handler.capcacity)
    handler.lastRefillTime = currentTime
}
