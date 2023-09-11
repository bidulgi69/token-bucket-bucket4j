package kr.dove.ratelimit.tokenbucket.service

import io.github.bucket4j.Bandwidth
import io.github.bucket4j.Bucket
import io.github.bucket4j.BucketConfiguration
import io.github.bucket4j.Refill
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager
import org.springframework.stereotype.Service
import java.nio.charset.Charset
import java.time.Duration

@Service
class BucketProvider(
    private val lettuceBasedProxyManager: LettuceBasedProxyManager<ByteArray>,
) {

    fun resolve(key: String): Bucket {
        //  get bucket configuration(throttling) from database with key
        val configuration = BucketConfiguration.builder()
            .addLimit(
                Bandwidth.classic(
                    3L,
                    Refill.intervally(3L, Duration.ofMinutes(3L))
                )
                    .withInitialTokens(3L)
            )
            .build()

        //  get bucket from redis
        return lettuceBasedProxyManager.builder()
            .build(
                key.toByteArray(Charset.defaultCharset()),
                configuration
            )
    }
}