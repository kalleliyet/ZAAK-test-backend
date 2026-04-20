package ZAAK.backend.ZAAK_Test.Redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisSensorService {

    private final StringRedisTemplate redisTemplate;

    private static final long BUCKET_SIZE_MS = 5 * 60 * 1000; // 5 minutes

    public void processReading(String machineId, String sensorId, double value) {
        log.info("machineId:{}, sensorId:{}, value:{}", machineId, sensorId, value);
        long now = System.currentTimeMillis();

        // 🔹 1. Store current value
        redisTemplate.opsForValue().set(getCurrentKey(machineId, sensorId), String.valueOf(value));

        // 🔹 2. Determine current bucket
        long bucketStart = getBucketStart(now);

        String minKey = getMinKey(machineId, sensorId, bucketStart);
        String maxKey = getMaxKey(machineId, sensorId, bucketStart);

        // 🔹 3. Update MIN (O(1))
        String currentMinStr = redisTemplate.opsForValue().get(minKey);
        if (currentMinStr == null) {
            redisTemplate.opsForValue().set(minKey, String.valueOf(value));
        } else {
            double currentMin = Double.parseDouble(currentMinStr);
            if (value < currentMin) {
                redisTemplate.opsForValue().set(minKey, String.valueOf(value));
            }
        }

        // 🔹 4. Update MAX (O(1))
        String currentMaxStr = redisTemplate.opsForValue().get(maxKey);
        if (currentMaxStr == null) {
            redisTemplate.opsForValue().set(maxKey, String.valueOf(value));
        } else {
            double currentMax = Double.parseDouble(currentMaxStr);
            if (value > currentMax) {
                redisTemplate.opsForValue().set(maxKey, String.valueOf(value));
            }
        }

        // 🔹 5. Set expiration (avoid memory leaks)
        redisTemplate.expire(minKey, 10, TimeUnit.MINUTES);
        redisTemplate.expire(maxKey, 10, TimeUnit.MINUTES);

        // 🔹 6. Check previous bucket and log if needed
        checkAndLogPreviousBucket(machineId, sensorId, bucketStart);
    }

    // 🔥 Logs the result of the previous 5-min bucket
    private void checkAndLogPreviousBucket(String machineId, String sensorId, long currentBucketStart) {

        long previousBucketStart = currentBucketStart - BUCKET_SIZE_MS;

        String minKey = getMinKey(machineId, sensorId, previousBucketStart);
        String maxKey = getMaxKey(machineId, sensorId, previousBucketStart);

        String minStr = redisTemplate.opsForValue().get(minKey);
        String maxStr = redisTemplate.opsForValue().get(maxKey);

        if (minStr != null && maxStr != null) {

            log.info("📊 [5-MIN METRIC] Machine={} Sensor={} WindowStart={} → MIN={} MAX={}",
                    machineId,
                    sensorId,
                    previousBucketStart,
                    minStr,
                    maxStr
            );

            // Optional: delete after logging (if you don't need them anymore)
            redisTemplate.delete(minKey);
            redisTemplate.delete(maxKey);
        }
    }

    // 🔹 Compute bucket start (round down to nearest 5 min)
    private long getBucketStart(long timestamp) {
        return (timestamp / BUCKET_SIZE_MS) * BUCKET_SIZE_MS;
    }

    // 🔹 Redis Keys

    private String getCurrentKey(String machineId, String sensorId) {
        return "machine:" + machineId + ":sensor:" + sensorId + ":current";
    }

    private String getMinKey(String machineId, String sensorId, long bucketStart) {
        return "machine:" + machineId + ":sensor:" + sensorId + ":bucket:" + bucketStart + ":min";
    }

    private String getMaxKey(String machineId, String sensorId, long bucketStart) {
        return "machine:" + machineId + ":sensor:" + sensorId + ":bucket:" + bucketStart + ":max";
    }
}