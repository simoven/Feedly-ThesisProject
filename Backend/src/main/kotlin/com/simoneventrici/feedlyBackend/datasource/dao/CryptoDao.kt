package com.simoneventrici.feedlyBackend.datasource.dao

import com.simoneventrici.feedlyBackend.model.Crypto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service

@Repository
class CryptoDao(
    @Autowired val jdbcTemplate: JdbcTemplate
): Dao<Crypto> {
    private val getAllQUery = "select * from crypto"
    private val saveQuery = "insert into crypto values(?,?,?,?)"
    private val removeQuery = "delete from crypto where ticker=?"

    override fun getAll(): List<Crypto> {
        val cryptos = mutableListOf<Crypto>()
        jdbcTemplate.query(getAllQUery) {
            cryptos.add(Crypto.fromResultSet(it))
        }
        return cryptos
    }

    override fun save(elem: Crypto) {
        jdbcTemplate.execute(saveQuery) {
            it.setString(1, elem.ticker)
            it.setString(2, elem.name)
            it.setString(3, elem.statsId)
            it.setInt(4, elem.graphicId)
            it.execute()
        }
    }

    fun remove(elem: Crypto) {
        jdbcTemplate.execute(removeQuery) {
            it.setString(1, elem.ticker)
            it.execute()
        }
    }
}