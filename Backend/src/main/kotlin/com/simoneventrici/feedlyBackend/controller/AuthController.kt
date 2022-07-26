package com.simoneventrici.feedlyBackend.controller

import com.simoneventrici.feedlyBackend.controller.dto.CredentialsDto
import com.simoneventrici.feedlyBackend.service.UserService
import com.simoneventrici.feedlyBackend.util.Protocol
import org.json.simple.JSONObject
import org.postgresql.util.PSQLException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class AuthController(
    val userService: UserService
) {

    @ExceptionHandler(IllegalStateException::class)
    fun handleInvalidData(e: IllegalStateException): ResponseEntity<JSONObject> {
        return ResponseEntity(JSONObject().apply { put("msg", e.message ?: "Invalid data provided") }, HttpStatus.FORBIDDEN)
    }

    @ExceptionHandler(PSQLException::class)
    fun handlePsqlException(): ResponseEntity<JSONObject> {
        return ResponseEntity(JSONObject().apply { put("msg", "Internal server error") }, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @PostMapping("login")
    fun doCredentialsLogin(@RequestBody credentials: CredentialsDto): ResponseEntity<JSONObject> {
        return if(userService.checkUserCredentials(credentials)) {
            val token = userService.getUserToken(credentials.username)
            ResponseEntity(JSONObject().apply { put("Authorization", token) }, HttpStatus.OK)
        }
        else
            ResponseEntity(JSONObject().apply { put("msg", "Wrong combination of username or password") }, HttpStatus.UNAUTHORIZED)
    }

    @PostMapping("tokenLogin")
    fun doTokenLogin(@RequestBody obj: JSONObject): ResponseEntity<JSONObject> {
        val token = obj["Authorization"] as String? ?: throw IllegalStateException("No Authorization token provided")
        userService.checkUserToken(token)?.let {
            val respObj = JSONObject().apply {
                put("username", it.username.value)
                put("email", it.email.value)
            }
            return ResponseEntity(JSONObject().apply { put("user", respObj) }, HttpStatus.OK)
        }
        return ResponseEntity(JSONObject().apply { put("msg", "Invalid token provided for authentication") }, HttpStatus.UNAUTHORIZED)
    }

    @PostMapping("register")
    fun doRegistration(@RequestBody credentials: CredentialsDto): ResponseEntity<JSONObject> {
        if (credentials.email == null) throw IllegalStateException("No email provided")

        val response = userService.tryUserRegistration(credentials)
        return ResponseEntity(
            JSONObject().apply { put("msg", response) },
            when(response) {
                Protocol.REGISTRATION_OK -> HttpStatus.OK
                Protocol.USERNAME_ALREADY_EXISTS -> HttpStatus.CONFLICT
                Protocol.EMAIL_ALREADY_EXISTS -> HttpStatus.CONFLICT
                else -> HttpStatus.FORBIDDEN
            }
        )
    }
}