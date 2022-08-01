package com.simoneventrici.feedlyBackend.util

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Component

@Component
class Properties {

    @Value("\${api.news.key}")
    lateinit var newsApiKey: String

    @Value("\${api.football.key}")
    lateinit var footballApiKey: String

    @Value("\${api.finance.key}")
    lateinit var financeApiKey: String

    @Value("\${google.client_id}")
    lateinit var googleClientId: String
}