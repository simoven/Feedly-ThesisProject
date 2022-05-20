package com.simoneventrici.feedlyBackend.persistence.dao

import com.simoneventrici.feedlyBackend.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.stereotype.Repository

@Repository
class UserDao (
    @Autowired val jdbcTemplate: JdbcTemplate
): Dao<User> {

    private val getAllQuery = "select * from users"
    private val saveUserQuery = "insert into users values (?,?,?)"
    private val deleteUserQuery = "delete from users where username=?"

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
            it.setString(3, BCrypt.hashpw(elem.getPassword(), "12"))
            it.execute()
        }
    }

    fun remove(elem: User) {
        jdbcTemplate.execute(deleteUserQuery) {
            it.setString(1, elem.getUsername())
            it.execute()
        }
    }
}