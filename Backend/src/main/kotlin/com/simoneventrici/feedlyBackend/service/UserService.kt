package com.simoneventrici.feedlyBackend.service

import com.simoneventrici.feedlyBackend.controller.dto.CredentialsDto
import com.simoneventrici.feedlyBackend.model.User
import com.simoneventrici.feedlyBackend.model.primitives.Email
import com.simoneventrici.feedlyBackend.model.primitives.Password
import com.simoneventrici.feedlyBackend.model.primitives.Username
import com.simoneventrici.feedlyBackend.datasource.dao.UserDao
import com.simoneventrici.feedlyBackend.security.Util
import com.simoneventrici.feedlyBackend.util.EmailSender
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService(
    @Autowired val userDao: UserDao,
    val mailSender: EmailSender
) {
    fun checkUserCredentials(credentials: CredentialsDto): Boolean {
        Username(credentials.username)
        Password(credentials.password)
        return userDao.checkCredentials(credentials)
    }

    fun getUserToken(username: String): String {
        val token = userDao.getUserToken(username)
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
        val isGoogleAccount = userDao.isGoogleAccount(token)
        userDao.tryTokenLogin(token)?.let {
            user = User(
                username = Username(it.first, checkValidation = !isGoogleAccount),
                email = Email(it.second),
                password = Password("******", checkValidation = false)
            )
        }
        return user
    }

    fun changeUserPassword(token: String, oldPassword: Password, newPassword: Password): Boolean {
        return userDao.changeUserPassword(token, oldPassword, newPassword)
    }

    fun doGoogleLogin(googleEmail: String): String {
        userDao.checkGoogleLogin(googleEmail)
        val username = googleEmail.split("@")[0]
        val token = userDao.getUserToken(username)
        return token ?: userDao.saveUserToken(username = username, token = Util.generateNewToken())
    }

    fun doLogout(user: User) {
        userDao.logoutUser(user)
    }

    fun resetAndChangePassword(email: Email) {
        val newPassword = Util.generateNewPassword()
        if(userDao.changeUserPasswordByEmail(email, Password(newPassword)))
            mailSender.sendPasswordRecoveryEmail(email.value, newPassword)
    }
}