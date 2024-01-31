package fish.focus.uvms.commons;

import fish.focus.uvms.commons.cache.HavCache;
import org.junit.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class HavCacheTest {
    @Test
    public void testHavCache() {
        HavCache<String, String> testCache = new HavCache<>(k -> k == null ? null : k + k, Duration.ofSeconds(5L));
        assertThat(testCache.get("abc", "def"), is("abcabc"));
        assertThat(testCache.get(null, "def"), is("def"));
    }

    @Test
    public void testHavCacheTimeOut() {
        HavCache<String, LocalDateTime> testCache = new HavCache<>(k -> LocalDateTime.now(), Duration.ofSeconds(5L));
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime fetchedTime = testCache.get("abc", LocalDateTime.MIN);
        assertThat(fetchedTime.toInstant(ZoneOffset.UTC).getEpochSecond() - startTime.toInstant(ZoneOffset.UTC).getEpochSecond() , is(0L));

        pause(2);
        startTime = LocalDateTime.now();
        fetchedTime = testCache.get("abc", LocalDateTime.MIN);
        assertThat(startTime.toInstant(ZoneOffset.UTC).getEpochSecond() - fetchedTime.toInstant(ZoneOffset.UTC).getEpochSecond(), is(2L));

        pause(4);
        startTime = LocalDateTime.now();
        fetchedTime = testCache.get("abc", LocalDateTime.MIN);
        assertThat(fetchedTime.toInstant(ZoneOffset.UTC).getEpochSecond() - startTime.toInstant(ZoneOffset.UTC).getEpochSecond(), is(0L));
    }

    private void pause(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            System.out.println("Interrupted error:" + e.getMessage());
        }
    }
}
