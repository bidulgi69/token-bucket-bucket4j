package kr.dove.ratelimit.tokenbucket.configurations

import io.github.bucket4j.distributed.ExpirationAfterWriteStrategy
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager
import io.lettuce.core.RedisClient
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Duration

@ConfigurationProperties(prefix = "spring.data.redis")
@Configuration
class RedisConfiguration {
    lateinit var host: String
    lateinit var port: String

    @Bean
    fun redisClient(): RedisClient {
        return RedisClient.create("redis://$host:$port/0?timeout=1m")
    }

    @Bean
    fun lettuceBasedProxyManager(
        @Qualifier("redisClient") redisClient: RedisClient
    ): LettuceBasedProxyManager<ByteArray> {
        return LettuceBasedProxyManager.builderFor(redisClient)
            .withExpirationStrategy(
                ExpirationAfterWriteStrategy.fixedTimeToLive(
                    Duration.ofMinutes(5L)
                )
            )
            .build()
    }
}