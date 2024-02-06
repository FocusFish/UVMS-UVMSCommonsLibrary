package fish.focus.uvms.commons.cache;

import javax.enterprise.context.ApplicationScoped;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;

@ApplicationScoped
public class HavCache<K, V> {
    private HashMap<K, HavCacheValue<V>> internalCache;
    private Duration durationUntilExpired;
    private HavCacheLoader<K, V> cacheLoader;

    public HavCache(HavCacheLoader<K, V> cacheLoader, Duration durationUntilExpired) {
        internalCache = new HashMap<>();
        this.cacheLoader = cacheLoader;
        this.durationUntilExpired = durationUntilExpired;
    }

    public V get(K key, V defaultValue) {
        HavCacheValue<V> cacheWriteTimeValuePair = internalCache.get(key);
        if (cacheWriteTimeValuePair == null || cacheWriteTimeValuePair.getWriteTime().plus(durationUntilExpired).isBefore(LocalDateTime.now())) {
            V value = cacheLoader.load(key);
            if (cacheWriteTimeValuePair != null) {
                internalCache.remove(key);
            }
            if (value == null) {
                return defaultValue;
            } else {
                internalCache.put(key, new HavCacheValue<>(value));
                return value;
            }
        }
        return cacheWriteTimeValuePair.getValue();
    }
}
