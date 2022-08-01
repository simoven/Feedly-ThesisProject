package com.simoneventrici.feedly.repository

import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import com.simoneventrici.feedly.model.User
import com.simoneventrici.feedly.model.primitives.Email
import com.simoneventrici.feedly.model.primitives.Password
import com.simoneventrici.feedly.model.primitives.Username
import com.simoneventrici.feedly.remote.api.AuthAPI
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

data class Status(
    val code: Int,
    val token: String?,
    val errorMsg: String?
)

class AuthRepository(
    private val authAPI: AuthAPI
) {
    suspend fun doCredentialsLogin(username: Username, password: Password): Status {
        val body = JSONObject().apply {
            put("username", username.value)
            put("password", password.value)
        }
        val response = authAPI.doCredentialsLogin(body.toString().toRequestBody("application/json".toMediaTypeOrNull()))

        return if(response.isSuccessful) {
            val responseJson = JSONObject(response.body()?.string() ?: "{}")
            Status(code = 200, token = responseJson.toMap()["Authorization"], errorMsg = null)
        }
        else {
            val responseJson = JSONObject(response.errorBody()?.string() ?: "{}")
            Status(code = response.code(), errorMsg = responseJson.toMap()["msg"], token = null)
        }
    }

    suspend fun doTokenLogin(authToken: String): User? {
        val body = JSONObject().apply {
            put("Authorization", authToken)
        }
        val response = authAPI.doTokenLogin(body.toString().toRequestBody("application/json".toMediaTypeOrNull()))

        return if(response.isSuccessful) {
            val responseJson = JSONObject(response.body()?.string() ?: "{}")
            val username = (responseJson["user"] as JSONObject) ["username"] as String
            val email = (responseJson["user"] as JSONObject) ["email"] as String
            User(username, email)
        }
        else { null }
    }

    suspend fun doRegistration(username: Username, email: Email, password: Password): Status {
        val body = JSONObject().apply {
            put("username", username.value)
            put("password", password.value)
            put("email", email.value)
        }
        val response = authAPI.doRegistration(body.toString().toRequestBody("application/json".toMediaTypeOrNull()))

        return if(response.isSuccessful) {
            Status(code = 200, token = null, errorMsg = null)
        }
        else {
            val responseJson = JSONObject(response.errorBody()?.string() ?: "{}")
            Status(code = response.code(), errorMsg = responseJson.toMap()["msg"], token = null)
        }
    }

    suspend fun doUserPasswordChange(token: String, oldPassword: Password, newPassword: Password): Status {
        val body = JSONObject().apply {
            put("Authorization", token)
            put("old_password", oldPassword.value)
            put("new_password", newPassword.value)
        }
        val response = authAPI.doUserPasswordChange(body.toString().toRequestBody("application/json".toMediaTypeOrNull()))

        return if(response.isSuccessful) {
            Status(code = 200, token = null, errorMsg = null)
        }
        else {
            val responseJson = JSONObject(response.errorBody()?.string() ?: "{}")
            Status(code = response.code(), errorMsg = responseJson.toMap()["msg"], token = null)
        }
    }

    suspend fun doUserLogout(token: String) {
        val body = JSONObject().apply {
            put("Authorization", token)
        }
        authAPI.doUserLogout(body.toString().toRequestBody("application/json".toMediaTypeOrNull()))
    }

    suspend fun doGoogleLogin(tokenId: String): Status {
        val body = JSONObject().apply {
            put("google_token", tokenId)
        }
        val response = authAPI.doGoogleLogin(body.toString().toRequestBody("application/json".toMediaTypeOrNull()))
        return if(response.isSuccessful) {
            val responseJson = JSONObject(response.body()?.string() ?: "{}")
            Status(code = 200, token = responseJson.toMap()["Authorization"] ?: "", errorMsg = null)
        }
        else
            Status(code = response.code(), token = null, errorMsg = null)
    }

    suspend fun doPasswordReset(email: Email): Boolean {
        val body = JSONObject().apply {
            put("email", email.value)
        }
        val response = authAPI.resetPassword(body.toString().toRequestBody("application/json".toMediaTypeOrNull()))
        return response.isSuccessful
    }
}


private fun JSONObject.toMap(): Map<String, String> {
    return try {
        val map = mutableMapOf<String, String>()
        val keysItr: Iterator<String> = this.keys()
        while (keysItr.hasNext()) {
            val key = keysItr.next()
            val value = this.get(key) as String
            map[key] = value
        }
        map
    } catch (e: Exception) {
        emptyMap()
    }
}