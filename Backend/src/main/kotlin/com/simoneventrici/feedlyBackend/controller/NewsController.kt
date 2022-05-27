package com.simoneventrici.feedlyBackend.controller

import com.simoneventrici.feedlyBackend.controller.exceptions.UnauthorizedException
import com.simoneventrici.feedlyBackend.datasource.dto.news.NewsAndReactionsDto
import com.simoneventrici.feedlyBackend.datasource.dto.news.ReactionsDto
import com.simoneventrici.feedlyBackend.model.News
import com.simoneventrici.feedlyBackend.model.primitives.Emoji
import com.simoneventrici.feedlyBackend.service.NewsService
import com.simoneventrici.feedlyBackend.service.UserService
import org.json.simple.JSONObject
import org.postgresql.util.PSQLException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.UnknownHostException

@RestController
@RequestMapping("news")
class NewsController(
    @Autowired private val newsService: NewsService,
    private val userService: UserService
){

    @ExceptionHandler(IllegalStateException::class)
    fun handleIllegalState(e: IllegalStateException): ResponseEntity<JSONObject> {
        return ResponseEntity(JSONObject().apply { put("msg", e.message ?: "Invalid arguments provided") }, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(UnknownHostException::class)
    fun handleConnectionError(e: UnknownHostException): ResponseEntity<JSONObject> {
        return ResponseEntity(JSONObject().apply { put("msg", "Internal server error") }, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(PSQLException::class)
    fun handlePsqlException(e: PSQLException): ResponseEntity<JSONObject> {
        // è stata lanciata l'eccezione che viola il foreign key constraint, ovvero il news id è sbagliato
        if(e.message?.contains("foreign", ignoreCase = true) == true)
            return ResponseEntity(JSONObject().apply { put("msg", "Invalid news id") }, HttpStatus.BAD_REQUEST)

        // altrimenti è server error
        return ResponseEntity(JSONObject().apply { put("msg", "Internal server error") }, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @GetMapping("newsByCategory")
    fun getNewsByCategory(
        @RequestParam category: String,
        @RequestParam language: String,
        @RequestHeader("Authorization") token: String
    ): Collection<NewsAndReactionsDto> {
        val categoryObj = News.Category.parse(category)
        if(language != "en" && language != "it") throw IllegalStateException("Provided language is not supported")

        val user = userService.checkUserToken(token) ?: throw UnauthorizedException(msg = "Invalid token provided")

        return newsService.getAllCurrentNewsByCategory(categoryObj, language, user)
    }

    @PostMapping("addReaction")
    fun addReactionToNews(
        @RequestBody body: JSONObject,
        @RequestHeader("Authorization") token: String
    ): JSONObject {
        val user = userService.checkUserToken(token) ?: throw UnauthorizedException(msg = "Invalid token provided")
        val newsId = body["newsId"] as Int? ?: throw IllegalStateException("News id not provided")
        val emojiStr = body["emoji"] as String? ?: throw IllegalStateException("Reaction not provided")
        // controllo che l'emoji sia valida
        val emoji = Emoji.Loader.load(emojiStr)

        // restituisco le reazioni aggiornate
        val allReactions =  newsService.addReactionToNews(newsId, user, emoji)
        return JSONObject().apply {
            put("news_id", newsId)
            put("news_reactions", allReactions.newsReactions)
            put("user_reactions", allReactions.userReaction)
        }
    }

    @GetMapping("newsByKeyword")
    fun getNewsByKeyword(
        @RequestParam keyword: String
    ): Collection<News> {
      return newsService.getAllCurrentNewsByKeyword(keyword)
    }
}