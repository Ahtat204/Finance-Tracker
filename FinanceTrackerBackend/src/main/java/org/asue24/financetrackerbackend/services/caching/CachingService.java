package org.asue24.financetrackerbackend.services.caching;

public interface CachingService<T> {
    void  put(String key, T value);
    T get(String key);
    void Evict(String key);
}