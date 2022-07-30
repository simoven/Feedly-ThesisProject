package com.simoneventrici.feedlyBackend.service

import com.simoneventrici.feedlyBackend.controller.dto.CredentialsDto
import com.simoneventrici.feedlyBackend.model.User
import com.simoneventrici.feedlyBackend.model.primitives.Email
import com.simoneventrici.feedlyBackend.model.primitives.Password
import com.simoneventrici.feedlyBackend.model.primitives.Username
import com.simoneventrici.feedlyBackend.datasource.dao.UserDao
import com.simoneventrici.feedlyBackend.security.Util
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService(
    @Autowired val userDao: UserDao
) {
    fun checkUserCredentials(credentials: CredentialsDto): Boolean {
        Username(credentials.username)
        Password(credentials.password)
        return userDao.checkCredentials(credentials)
    }

    fun getUserToken(username: String): String {
        var token = userDao.getUserToken(username)
        return token ?: userDao.saveUserToken(username = username, token = Util.generateNewToken())
    }

    fun tryUserRegistration(credentials: CredentialsDto): String {
        val user = User(
            username = Username(credentials.username),
            email = Email(credentials.email ?: ""),
            password = Password(credentials.password)
        )
        return userDao.tryRegistration(user)
    }

    fun checkUserToken(token: String): User? {
        var user: User? = null
        userDao.tryTokenLogin(token)?.let {
            user = User(
                username = Username(it.first),
                email = Email(it.second),
                password = Password("******", checkValidation = false)
            )
        }
        return user
    }

    fun changeUserPassword(token: String, oldPassword: Password, newPassword: Password): Boolean {
        return userDao.changeUserPassword(token, oldPassword, newPassword)
    }
}