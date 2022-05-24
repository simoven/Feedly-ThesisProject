package com.simoneventrici.feedlyBackend.controller

import com.simoneventrici.feedlyBackend.model.News
import com.simoneventrici.feedlyBackend.service.NewsService
import org.json.simple.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("news")
class NewsController(
    @Autowired private val newsService: NewsService
){

    @ExceptionHandler(IllegalStateException::class)
    fun handleIllegalState(e: IllegalStateException): ResponseEntity<JSONObject> {
        return ResponseEntity(JSONObject().apply { put("msg", e.message ?: "Invalid arguments provided") }, HttpStatus.FORBIDDEN)
    }

    @GetMapping("newsByCategory")
    fun getNewsByCategory(
        @RequestParam category: String,
        @RequestParam language: String
    ): Collection<News> {
        val categoryObj = News.Category.parse(category)
        if(language != "en" && language != "it") throw IllegalStateException("Provided language is not supported")

        return newsService.getCurrentNewsByCategory(categoryObj, language)
    }

    @GetMapping("newsByKeyword")
    fun getNewsByKeyword(
        @RequestParam keyword: String
    ): Collection<News> {
        return newsService.getCurrentNewsByKeyword(keyword)
    }
}