package com.simoneventrici.feedlyBackend.controller

import com.simoneventrici.feedlyBackend.model.primitives.Username
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController {

    @GetMapping("login")
    fun doLogin() {
        val user = Username("abcd")


    }
}