package fish.focus.uvms.commons.cache;

public interface HavCacheLoader<K, V> {
    public V load(K key);
}

