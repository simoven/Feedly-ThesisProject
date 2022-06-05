package com.simoneventrici.feedlyBackend.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.simoneventrici.feedlyBackend.model.primitives.Email
import com.simoneventrici.feedlyBackend.model.primitives.Password
import com.simoneventrici.feedlyBackend.model.primitives.Username
import java.sql.ResultSet

data class User(
    @JsonIgnore val username: Username,
    @JsonIgnore val email: Email,
    @JsonIgnore private var password: Password
) {

    companion object {
        fun fromResultSet(rs: ResultSet): User {
            return User(
                username = Username(rs.getString("username")),
                email = Email(rs.getString("e-mail")),
                password = Password("********", checkValidation = false)
            )
        }
    }

    fun getUsername(): String = username.value
    fun getEmail(): String = email.value
    fun getPassword(): String {
        // read-once password
        val realPassword = password.value
        password = Password("********", checkValidation = false)
        return realPassword
    }
}