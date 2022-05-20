package com.simoneventrici.feedlyBackend.controller

import com.simoneventrici.feedlyBackend.model.Crypto
import com.simoneventrici.feedlyBackend.model.SoccerTeam
import com.simoneventrici.feedlyBackend.model.primitives.News
import com.simoneventrici.feedlyBackend.persistence.dao.CryptoDao
import com.simoneventrici.feedlyBackend.persistence.dao.NewsDao
import com.simoneventrici.feedlyBackend.service.SoccerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

@RestController
@RequestMapping("/auth")
class AuthController(
    @Autowired val newsDao: NewsDao,
    @Autowired val soccerService: SoccerService
) {

    @GetMapping("test")
    fun doTest(): List<SoccerTeam> {
        /*val news = News(
            -1,
            "test",
            "test",
            "test",
            "testUrl4",
            "testImage",
            "my pc",
            "my-pc",
            "2022-05-18T09:23:00Z",
            null,
            News.Category.General()
        )*/
        return soccerService.allTeams
    }
}