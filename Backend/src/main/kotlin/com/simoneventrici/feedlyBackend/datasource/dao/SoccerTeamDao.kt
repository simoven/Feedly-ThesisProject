package com.simoneventrici.feedlyBackend.datasource.dao

import com.simoneventrici.feedlyBackend.model.SoccerTeam
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service

@Repository
class SoccerTeamDao(
    @Autowired val jdbcTemplate: JdbcTemplate
): Dao<SoccerTeam> {

    private val getAllQuery = "select * from soccer_team"
    private val saveQuery = "insert into soccer_team values(?,?)"

    override fun getAll(): List<SoccerTeam> {
        val list = mutableListOf<SoccerTeam>()
        jdbcTemplate.query(getAllQuery) {
            list.add(SoccerTeam.fromResultSet(it))
        }
        return list
    }

    override fun save(elem: SoccerTeam) {
       jdbcTemplate.execute(saveQuery) {
           it.setInt(1, elem.teamId)
           it.setString(2, elem.name)
           it.execute()
       }
    }
}