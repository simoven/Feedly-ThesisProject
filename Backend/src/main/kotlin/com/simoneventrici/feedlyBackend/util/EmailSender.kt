package com.simoneventrici.feedlyBackend.util

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Component

@Component
class EmailSender {

    @Autowired
    lateinit var javaMailSender: JavaMailSender

    fun getPasswordRecoveryText(pass: String): String {
        return "This email has been sent to help you recover your password. This is your temporary password: $pass \nUse it to access in your account"
    }

    fun sendPasswordRecoveryEmail(to: String, password: String) {
        val msg = SimpleMailMessage()
        msg.setTo(to)
        msg.setSubject("Feedly password recovery")
        msg.setText(getPasswordRecoveryText(password))
        javaMailSender.send(msg)
    }
}