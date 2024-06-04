package org.app.infrastructure.utils;


import org.app.domain.exceptions.RequestLimitReachedException;

import java.util.function.Supplier;

public class RateLimiter {
    private final int capacity;
    private int tokens;
    private long lastRefillTime;
    private final long refillInterval;
    private final int refillAmount;

    public RateLimiter(int capacity, long refillInterval, int refillAmount) {
        this.capacity = capacity;
        this.tokens = capacity;
        this.refillInterval = refillInterval;
        this.refillAmount = refillAmount;
        this.lastRefillTime = System.currentTimeMillis();
    }

    public synchronized boolean tryAcquire() {
        refillTokens();
        if (tokens > 0) {
            tokens--;
            return true;
        }
        return false;
    }

    public <T> T execute(Supplier<T> action) {
        if(tryAcquire()){
            return action.get();
        }else{
            throw new RequestLimitReachedException();
        }
    }

    private synchronized void refillTokens() {
        long now = System.currentTimeMillis();
        long pastTime = now - lastRefillTime;
        int addTokens = (int) (pastTime / refillInterval) * refillAmount;
        this.tokens = Math.min(capacity, tokens + addTokens);
        this.lastRefillTime = now;
    }
}
