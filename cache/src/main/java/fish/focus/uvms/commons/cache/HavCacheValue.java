package fish.focus.uvms.commons.cache;

import java.time.LocalDateTime;

public class HavCacheValue<V> {
    private V value;
    private LocalDateTime writeTime;

    public HavCacheValue(V v) {
        value = v;
        writeTime = LocalDateTime.now();
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public LocalDateTime getWriteTime() {
        return writeTime;
    }

    public void setWriteTime(LocalDateTime writeTime) {
        this.writeTime = writeTime;
    }
}
