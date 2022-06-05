package com.simoneventrici.feedlyBackend.datasource.dao

import com.simoneventrici.feedlyBackend.model.Crypto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.PreparedStatementCreator
import org.springframework.jdbc.core.PreparedStatementSetter
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service

@Repository
class CryptoDao(
    @Autowired val jdbcTemplate: JdbcTemplate
): Dao<Crypto> {
    private val getAllQuery = "select * from crypto"
    private val saveQuery = "insert into crypto values(?,?,?,?) on conflict(\"ticker\") do nothing"
    private val removeQuery = "delete from crypto where ticker=?"
    private val getUserFavouritesCryptoQuery = "select * from user_like_crypto where \"user\"=?"
    private val addCryptoFavouriteQuery = "insert into user_like_crypto values(?,?) on conflict do nothing"
    private val removeCryptoFavouriteQuery = "delete from user_like_crypto where \"user\"=? and ticker=?"

    override fun getAll(): List<Crypto> {
        val cryptos = mutableListOf<Crypto>()
        jdbcTemplate.query(getAllQuery) {
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

    fun getUserFavouritesCrypto(username: String): Collection<String> {
        val creator = PreparedStatementCreator { it.prepareStatement(getUserFavouritesCryptoQuery) }
        val setter = PreparedStatementSetter { it.setString(1, username) }
        val list = mutableListOf<String>()
        jdbcTemplate.query(creator, setter) { rs ->
            while(rs.next()) {
                list.add(rs.getString("ticker"))
            }
            rs.close()
        }
        return list
    }

    fun addFavouriteCrypto(username: String, cryptoTicker: String) {
        jdbcTemplate.execute(addCryptoFavouriteQuery) {
            it.setString(1, username)
            it.setString(2, cryptoTicker)
            it.execute()
        }
    }

    fun removeFavouriteCrypto(username: String, cryptoTicker: String) {
        jdbcTemplate.execute(removeCryptoFavouriteQuery) {
            it.setString(1, username)
            it.setString(2, cryptoTicker)
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