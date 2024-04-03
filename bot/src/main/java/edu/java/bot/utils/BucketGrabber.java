package edu.java.bot.utils;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.annotation.PostConstruct;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BucketGrabber {
    private Cache<String, Bucket> cache;

    @Value("${bucket.refill-size}")
    private long refillSize;

    @Value("${bucket.refill-interval}")
    private long refillInterval;

    @Value("${bucket.capacity}")
    private long capacity;

    @Value("${bucket-cache.max-size}")
    private long maxSize;

    @Value("${bucket-cache.expiration-time}")
    private long expirationTime;

    @PostConstruct
    void initCache() {
        cache = Caffeine.newBuilder()
                .maximumSize(maxSize)
                .expireAfterAccess(expirationTime, TimeUnit.MINUTES)
                .build();
    }

    public Bucket grabBucket(String ipAddress) {
        Bucket bucket = cache.getIfPresent(ipAddress);
        if (bucket == null) {
            Refill refill = Refill.intervally(refillSize, Duration.ofMinutes(refillInterval));
            Bandwidth limit = Bandwidth.classic(capacity, refill);
            bucket = Bucket.builder()
                    .addLimit(limit)
                    .build();
            cache.put(ipAddress, bucket);
        }
        return bucket;
    }
}
