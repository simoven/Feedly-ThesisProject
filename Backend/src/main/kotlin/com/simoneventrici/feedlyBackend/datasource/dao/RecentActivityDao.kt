package com.simoneventrici.feedlyBackend.datasource.dao

import com.simoneventrici.feedlyBackend.model.RecentActivity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.PreparedStatementCreator
import org.springframework.jdbc.core.PreparedStatementSetter
import org.springframework.stereotype.Repository

@Repository
class RecentActivityDao(
    @Autowired private val jdbcTemplate: JdbcTemplate
) {

    private val getUserActivityQuery = "select * from recent_activity where \"user\"=?"

    fun getUserActivity(user: String): List<RecentActivity> {
        val list = mutableListOf<RecentActivity>()
        val creator = PreparedStatementCreator { it.prepareStatement(getUserActivityQuery) }
        val setter = PreparedStatementSetter { it.setString(1, user)}
        val query = jdbcTemplate.query(creator, setter) { rs ->
            while(rs.next()) {
                list.add(RecentActivity.fromResultSet(rs))
            }
            rs.close()
        }
        return list
    }
}