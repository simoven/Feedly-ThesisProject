package com.simoneventrici.feedlyBackend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.web.client.RestTemplate


@SpringBootApplication(exclude = [SecurityAutoConfiguration::class])
class FeedlyBackendApplication {

	@Bean
	fun restTemplate(restTemplateBuilder: RestTemplateBuilder): RestTemplate = restTemplateBuilder.build()
}

fun main(args: Array<String>) {
	runApplication<FeedlyBackendApplication>(*args)
}
