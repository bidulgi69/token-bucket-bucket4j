package kr.dove.ratelimit.tokenbucket.controller

import kr.dove.ratelimit.tokenbucket.service.BucketProvider
import org.springframework.http.HttpStatusCode
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class HelloController(
    private val bucketProvider: BucketProvider,
) {

    @GetMapping(
        value = ["/hello"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun hello(
        @RequestParam(name = "bucket", required = false, defaultValue = "key") key: String
    ): Mono<ResponseEntity<String>> {
        return Mono.defer {
            val bucket = bucketProvider.resolve(key)
            if (bucket.tryConsume(1L)) {
                Mono.just(
                    ResponseEntity.ok("Hello World!")
                )
            } else {
                Mono.just(
                    ResponseEntity.status(HttpStatusCode.valueOf(429))
                        .build()
                )
            }
        }
    }
}