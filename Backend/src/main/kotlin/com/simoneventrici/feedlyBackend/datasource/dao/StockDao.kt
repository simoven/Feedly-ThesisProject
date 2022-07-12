package com.simoneventrici.feedlyBackend.datasource.dao

import com.simoneventrici.feedlyBackend.model.Stock
import com.simoneventrici.feedlyBackend.model.User
import com.simoneventrici.feedlyBackend.model.primitives.Ticker
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.PreparedStatementCreator
import org.springframework.jdbc.core.PreparedStatementSetter
import org.springframework.stereotype.Repository

@Repository
class StockDao(
    @Autowired val jdbcTemplate: JdbcTemplate
) {
    private val getAllQuery = "select * from stock"
    private val getUserStocksQuery = "select ticker from user_like_stock where \"user\"=?"
    private val deleteUserFavouriteStockQuery = "delete from user_like_stock where \"user\"=? and ticker=?"
    private val addUserFavouriteStockQuery = "insert into user_like_stock values(?,?) on conflict do nothing"

    fun getAll(): List<Stock> {
        val list = mutableListOf<Stock>()
        jdbcTemplate.query(getAllQuery) {
            list.add(Stock.fromResultSet(it))
        }
        return list
    }

    fun getUserFavouriteStocks(user: User): List<String> {
        val creator = PreparedStatementCreator { con -> con.prepareStatement(getUserStocksQuery) }
        val setter = PreparedStatementSetter { stm -> stm.setString(1, user.getUsername()) }
        val list = mutableListOf<String>()
        jdbcTemplate.query(creator, setter) { rs ->
            while(rs.next()) {
                list.add(rs.getString(1))
            }
            rs.close()
        }
        return list
    }

    fun addUserFavouriteStock(user: User, stock: Ticker) {
        jdbcTemplate.execute(addUserFavouriteStockQuery) {
            it.setString(1, user.getUsername())
            it.setString(2, stock.value.lowercase())
            it.execute()
        }
    }

    fun removeUserFavouriteStock(user: User, stock: Ticker) {
        jdbcTemplate.execute(deleteUserFavouriteStockQuery) {
            it.setString(1, user.getUsername())
            it.setString(2, stock.value.lowercase())
            it.execute()
        }
    }
}