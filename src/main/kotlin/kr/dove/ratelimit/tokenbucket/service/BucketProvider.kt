package kr.dove.ratelimit.tokenbucket.service

import io.github.bucket4j.Bandwidth
import io.github.bucket4j.Bucket
import io.github.bucket4j.BucketConfiguration
import io.github.bucket4j.Refill
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager
import kr.dove.ratelimit.tokenbucket.configurations.BucketConfigurationProperties
import org.springframework.stereotype.Service
import java.nio.charset.Charset

@Service
class BucketProvider(
    private val lettuceBasedProxyManager: LettuceBasedProxyManager<ByteArray>,
    private val bucketConfigurationProperties: BucketConfigurationProperties,
) {

    fun resolve(key: String): Bucket {
        //  get a bucket configuration(throttling) from database with key
        val properties = bucketConfigurationProperties.getProperties()

        val configuration = BucketConfiguration.builder()
            .addLimit(
                Bandwidth.classic(
                    properties.capacity,
                    Refill.intervally(
                        properties.refillTokens,
                        properties.refillInterval
                    )
                )
                    .withInitialTokens(properties.initialTokens)
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