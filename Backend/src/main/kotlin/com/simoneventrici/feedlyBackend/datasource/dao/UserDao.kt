package com.simoneventrici.feedlyBackend.datasource.dao

import com.simoneventrici.feedlyBackend.controller.dto.CredentialsDto
import com.simoneventrici.feedlyBackend.model.User
import com.simoneventrici.feedlyBackend.util.Protocol
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.PreparedStatementCreator
import org.springframework.jdbc.core.PreparedStatementSetter
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.stereotype.Repository

@Repository
class UserDao (
    @Autowired val jdbcTemplate: JdbcTemplate
): Dao<User> {

    private val getAllQuery = "select * from users"
    private val saveUserQuery = "insert into users values (?,?,?, null)"
    private val deleteUserQuery = "delete from users where username=?"
    private val checkUserCredentialsQuery = "select password from users where username=?"
    private val getUserTokenQuery = "select access_token from users where username=?"
    private val saveTokenQuery = "update users set access_token=? where username=?"
    private val getUserByTokenQuery = "select * from users where access_token is not null and access_token=?"

    override fun getAll(): List<User> {
        val list = mutableListOf<User>()
        jdbcTemplate.query(getAllQuery) { rs ->
            list.add(User.fromResultSet(rs))
        }
        return list
    }

    override fun save(elem: User) {
        jdbcTemplate.execute(saveUserQuery) {
            it.setString(1, elem.getUsername())
            it.setString(2, elem.getEmail())
            it.setString(3, BCrypt.hashpw(elem.getPassword(), BCrypt.gensalt(12)))
            it.execute()
        }
    }

    fun remove(elem: User) {
        jdbcTemplate.execute(deleteUserQuery) {
            it.setString(1, elem.getUsername())
            it.execute()
        }
    }

    fun checkCredentials(cred: CredentialsDto): Boolean {
        val creator = PreparedStatementCreator { conn -> conn.prepareStatement(checkUserCredentialsQuery) }
        val setter = PreparedStatementSetter {
            it.setString(1, cred.username)
        }
        var validLogin = false
        jdbcTemplate.query(creator, setter) {
            if(it.next()) {
                val hashPw = it.getString(1)
                validLogin = BCrypt.checkpw(cred.password, hashPw)
            }
            it.close()
        }
        return validLogin
    }

    fun getUserToken(username: String): String? {
        val creator = PreparedStatementCreator { conn -> conn.prepareStatement(getUserTokenQuery) }
        val setter = PreparedStatementSetter { it.setString(1, username) }
        var token : String? = null
        jdbcTemplate.query(creator, setter) {
            if(it.next())
                token = it.getString(1)
            it.close()
        }
        return token
    }

    fun saveUserToken(username: String, token: String): String {
        jdbcTemplate.execute(saveTokenQuery) {
            it.setString(1, token)
            it.setString(2, username)
            it.execute()
        }
        return token
    }

    fun tryRegistration(user: User): String {
        kotlin.runCatching {
            save(user)
        }.onFailure {
            if(it.message?.contains("username", ignoreCase = true) == true)
                return Protocol.USERNAME_ALREADY_EXISTS
            if(it.message?.contains("mail", ignoreCase = true) == true)
                return Protocol.EMAIL_ALREADY_EXISTS
        }.onSuccess {
            return Protocol.REGISTRATION_OK
        }
        return Protocol.REGISTRATION_ERROR
    }

    fun tryTokenLogin(token: String): Pair<String, String>? {
        val creator = PreparedStatementCreator { conn -> conn.prepareStatement(getUserByTokenQuery)}
        val setter = PreparedStatementSetter { ps -> ps.setString(1, token) }
        var pair: Pair<String, String>? = null
        val query = jdbcTemplate.query(creator, setter) { rs ->
            if(rs.next()) {
                pair = Pair(rs.getString("username"), rs.getString("e-mail"))
            }
            rs.close()
        }
        return pair
    }
}