package com.simoneventrici.feedlyBackend.controller

import com.simoneventrici.feedlyBackend.controller.exceptions.UnauthorizedException
import com.simoneventrici.feedlyBackend.model.Crypto
import com.simoneventrici.feedlyBackend.model.primitives.Ticker
import com.simoneventrici.feedlyBackend.service.CryptoService
import com.simoneventrici.feedlyBackend.service.UserService
import org.json.simple.JSONObject
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("crypto")
class CryptoController(
    val cryptoService: CryptoService,
    val userService: UserService
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
    fun getAllCryptos(): Collection<Crypto> {
        return cryptoService.getAllCryptos()
    }

    @GetMapping("userFavourites")
    fun getUserFavouriteCrypto(
        @RequestHeader("Authorization") authToken: String
    ): Collection<Crypto> {
        val user = userService.checkUserToken(authToken) ?: throw UnauthorizedException(msg = "Invalid token provided")
        return cryptoService.getUserFavoritesCrypto(user.username)
    }

    @PostMapping("addFavourite")
    fun addCryptoFavourite(
        @RequestHeader("Authorization") authToken: String,
        @RequestBody body: JSONObject
    ): ResponseEntity<JSONObject> {
        val user = userService.checkUserToken(authToken) ?: throw UnauthorizedException(msg = "Invalid token provided")
        val tickerObj = Ticker(body["ticker"] as String)
        if(!cryptoService.isCryptoSupported(tickerObj)) throw  IllegalStateException("Invalid crypto ticker provided")
        cryptoService.addCryptoFavourite(user.username, tickerObj)
        return ResponseEntity(JSONObject().apply { put("msg", "Crypto added succesfully") }, HttpStatus.OK)
    }

    @PostMapping("removeFavourite")
    fun removeCryptoFavourite(
        @RequestHeader("Authorization") authToken: String,
        @RequestBody ticker: String
    ) {
        val user = userService.checkUserToken(authToken) ?: throw UnauthorizedException(msg = "Invalid token provided")
        val tickerObj = Ticker(ticker)
        if(!cryptoService.isCryptoSupported(tickerObj)) throw  IllegalStateException("Invalid crypto ticker provided")
        cryptoService.removeCryptoFavourite(user.username, tickerObj)
    }

}