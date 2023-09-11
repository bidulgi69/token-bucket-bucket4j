package kr.dove.ratelimit.tokenbucket

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TokenBucketApplication

fun main(args: Array<String>) {
	runApplication<TokenBucketApplication>(*args)
}
