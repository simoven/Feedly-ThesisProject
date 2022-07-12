package com.simoneventrici.feedlyBackend.controller

import com.simoneventrici.feedlyBackend.controller.exceptions.UnauthorizedException
import com.simoneventrici.feedlyBackend.model.Stock
import com.simoneventrici.feedlyBackend.model.StocksData
import com.simoneventrici.feedlyBackend.model.primitives.Ticker
import com.simoneventrici.feedlyBackend.service.StockService
import com.simoneventrici.feedlyBackend.service.UserService
import org.json.simple.JSONObject
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("stocks")
class StocksController(
    private val stockService: StockService,
    private val userService: UserService
) {
    @ExceptionHandler(UnauthorizedException::class)
    fun handleUnauthorized(e: UnauthorizedException): ResponseEntity<JSONObject> {
        return ResponseEntity(JSONObject().apply { put("msg", e.message) }, HttpStatus.FORBIDDEN)
    }

    @ExceptionHandler(IllegalStateException::class)
    fun handleIllegalState(e: IllegalStateException): ResponseEntity<JSONObject> {
        return ResponseEntity(JSONObject().apply { put("msg", e.message ?: "Invalid data provided") }, HttpStatus.BAD_REQUEST)
    }

    @GetMapping("all")
    fun getAllStocks(): List<Stock> {
        return stockService.allStocks
    }

    @GetMapping("userFavourites")
    fun getUserFavouriteStocks(@RequestHeader("Authorization") authToken: String): List<StocksData> {
        val user = userService.checkUserToken(authToken) ?: throw UnauthorizedException(msg = "Invalid token provided")
        return stockService.getUserFavouriteStocksData(user)
    }

    @PostMapping("addFavouriteStock")
    fun addUserFavouriteStock(
        @RequestHeader("Authorization") authToken: String,
        @RequestBody body: JSONObject
    ): ResponseEntity<JSONObject> {
        val user = userService.checkUserToken(authToken) ?: throw UnauthorizedException(msg = "Invalid token provided")
        val tickerObj = Ticker(body["ticker"] as String)
        if(!stockService.isSupportedStock(tickerObj)) throw  IllegalStateException("Invalid stock ticker provided")
        stockService.addUserFavouriteStock(user, tickerObj)
        return ResponseEntity(JSONObject().apply { put("msg", "Stock added succesfully") }, HttpStatus.OK)
    }

    @PostMapping("removeFavouriteStock")
    fun removeUserFavouriteStock(
        @RequestHeader("Authorization") authToken: String,
        @RequestBody body: JSONObject
    ): ResponseEntity<JSONObject> {
        val user = userService.checkUserToken(authToken) ?: throw UnauthorizedException(msg = "Invalid token provided")
        val tickerObj = Ticker(body["ticker"] as String)
        if(!stockService.isSupportedStock(tickerObj)) throw  IllegalStateException("Invalid stock ticker provided")
        stockService.removeUserFavouriteStock(user, tickerObj)
        return ResponseEntity(JSONObject().apply { put("msg", "Stock removed succesfully") }, HttpStatus.OK)
    }
}