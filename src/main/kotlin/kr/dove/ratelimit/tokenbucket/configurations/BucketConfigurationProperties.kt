package kr.dove.ratelimit.tokenbucket.configurations

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import java.time.Duration

@Configuration
class BucketConfigurationProperties(
    @Value("\${bucket.master.capacity}") val masterCapacity: Long,
    @Value("\${bucket.master.initialTokens}") val masterInitialTokens: Long,
    @Value("\${bucket.master.refillTokens}") val masterRefillTokens: Long,
    @Value("\${bucket.master.refillDurationSeconds}") val masterRefillDurationSeconds: Long,
    @Value("\${bucket.slave.capacity}") val slaveCapacity: Long,
    @Value("\${bucket.slave.initialTokens}") val slaveInitialTokens: Long,
    @Value("\${bucket.slave.refillTokens}") val slaveRefillTokens: Long,
    @Value("\${bucket.slave.refillDurationSeconds}") val slaveRefillDurationSeconds: Long,
    @Value("\${spring.config.activate.on-profile:master}") val profile: String
) {
    
    fun getProperties(): BucketProperties =
        when (profile.lowercase()) {
            "slave" -> BucketProperties(
                slaveCapacity,
                slaveInitialTokens,
                slaveRefillTokens,
                Duration.ofSeconds(slaveRefillDurationSeconds)
            ) 
            else -> BucketProperties(
                masterCapacity,
                masterInitialTokens,
                masterRefillTokens,
                Duration.ofSeconds(masterRefillDurationSeconds)
            )
        }
}

data class BucketProperties(
    val capacity: Long,
    val initialTokens: Long,
    val refillTokens: Long,
    val refillInterval: Duration
)