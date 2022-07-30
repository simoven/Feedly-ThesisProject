package com.simoneventrici.feedly.presentation.authentication

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simoneventrici.feedly.R
import com.simoneventrici.feedly.commons.ValidationException
import com.simoneventrici.feedly.model.User
import com.simoneventrici.feedly.model.primitives.Email
import com.simoneventrici.feedly.model.primitives.Password
import com.simoneventrici.feedly.model.primitives.Username
import com.simoneventrici.feedly.persistence.DataStorePreferences
import com.simoneventrici.feedly.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    val authRepository: AuthRepository,
    val preferences: DataStorePreferences
): ViewModel() {

    val userToken = mutableStateOf("")
    val userState: MutableState<User?> = mutableStateOf(null)
    // isAuthenticating serve per non far uscire la welcome screen se non sono sicuro se l'utente sia loggato o meno
    val isAuthenticating = mutableStateOf(false)

    val showConfirmDialog = mutableStateOf(false)

    // sono gli id dei messaggi di errore nelle risorse
    val usernameErrorCode = mutableStateOf(-1)
    val passwordErrorCode = mutableStateOf(-1)
    val emailErrorCode = mutableStateOf(-1)

    val genericErrorCode = mutableStateOf(-1)

    init {
        isAuthenticating.value = true
        observeTokenChanges()
    }

    private fun observeTokenChanges() {
        // ad ogni cambiamento del token, tento il login oppure no
        preferences.tokensFlow.onEach { token ->
            // se il token è diverso da quello attualmente salvato, oppure è vuoto
            if(token != userToken.value || token == "") {
                userToken.value = token ?: ""
                isAuthenticating.value = false

                if (userToken.value.isNotBlank())
                    doTokenTogin()
                else
                    userState.value = null
            }
        }.launchIn(viewModelScope)
    }

    private fun doTokenTogin() {
        viewModelScope.launch {
            isAuthenticating.value = true
            userState.value = authRepository.doTokenLogin(userToken.value)
            isAuthenticating.value = false
        }
    }

    private fun validateUsername(username: String): Username? {
        return try {
            Username(username)
        } catch(e: ValidationException) {
            usernameErrorCode.value = e.errorMsgCode
            null
        }
    }

    private fun validateEmail(email: String): Email? {
        return try {
            Email(email)
        } catch(e: ValidationException) {
            emailErrorCode.value = e.errorMsgCode
            null
        }
    }

    private fun validatePassword(password: String): Password? {
        return try {
            Password(password)
        } catch(e: ValidationException) {
            passwordErrorCode.value = e.errorMsgCode
            null
        }
    }

    fun doCredentialsLogin(username: String, password: String) {
        clearErrorMessages()
        val usernameObj: Username? = validateUsername(username)
        val passwordObj: Password? = validatePassword(password)

        if(usernameObj != null && passwordObj != null) {
            viewModelScope.launch {
                val resp = authRepository.doCredentialsLogin(usernameObj, passwordObj)
                if(resp.code == 200)
                    preferences.saveToken(resp.token ?: "")
                else if(resp.code == 401)
                    genericErrorCode.value = R.string.wrong_username_password
                else
                    genericErrorCode.value = R.string.login_error
            }
        }
    }

    fun doSignup(username: String, email: String, password: String) {
        clearErrorMessages()
        val usernameObj: Username? = validateUsername(username)
        val emailObj: Email? = validateEmail(email)
        val passwordObj: Password? = validatePassword(password)

        if(usernameObj != null && emailObj != null && passwordObj != null) {
            viewModelScope.launch {
                val resp = authRepository.doRegistration(usernameObj, emailObj, passwordObj)
                if(resp.code == 200) {
                    showConfirmDialog.value = true
                    delay(2000)
                    showConfirmDialog.value = false
                }
                else if(resp.code == 409)
                    genericErrorCode.value = R.string.username_email_already_exists
                else
                    genericErrorCode.value = R.string.signup_error
            }
        }
    }

    fun changePassword(oldPassword: String, newPassword: String, confirmPassword: String) {
        clearErrorMessages()

        val oldPassObj = validatePassword(oldPassword)
        if(oldPassObj == null) {
            genericErrorCode.value = R.string.invalid_old_password
            return
        }

        val newPassObj = validatePassword(newPassword)
        if(newPassObj == null) {
            genericErrorCode.value = R.string.invalid_new_password
            return
        }

        val confirmPassObj = validatePassword(confirmPassword)
        if(confirmPassObj == null) {
            genericErrorCode.value = R.string.invalid_confirm_password
            return
        }

        if(newPassword != confirmPassword) {
            genericErrorCode.value = R.string.new_confirm_password_dont_match
            return
        }

        viewModelScope.launch {
            val resp = authRepository.doUserPasswordChange(userToken.value, oldPassObj, newPassObj)
            if(resp.code == 200) {
                showConfirmDialog.value = true
                delay(2000)
                showConfirmDialog.value = false
            }
            else if(resp.code == 400)
                genericErrorCode.value = R.string.old_password_dont_match
            else
                genericErrorCode.value = R.string.error_password_change

        }
    }

    fun doLogout() {
        viewModelScope.launch {
            preferences.saveToken("")
        }
    }

    fun clearErrorMessages() {
        usernameErrorCode.value = -1
        passwordErrorCode.value = -1
        emailErrorCode.value = -1
        genericErrorCode.value = -1
    }

}