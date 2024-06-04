package org.app.infrastructure.utils;

import org.app.domain.exceptions.CustomException;
import org.app.domain.exceptions.ServiceOverloadException;

import java.util.function.Supplier;

public class RetryUtil {
    public static <T> T retry(Supplier<T> action, int maxRetries, long delay) {
        int attempt = 0;
        while (attempt < maxRetries) {
            try {
               return action.get();
            }catch (CustomException e) {
                throw e;
            }catch (Exception e){
                attempt++;
                if (attempt >= maxRetries) {
                    throw e;
                }
                try {
                    Thread.sleep(delay *=2);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    throw new ServiceOverloadException("Thread was interrupted during retry", ex);
                }
            }
        }
        throw new ServiceOverloadException("Max retry attempts reached");
    }
}
