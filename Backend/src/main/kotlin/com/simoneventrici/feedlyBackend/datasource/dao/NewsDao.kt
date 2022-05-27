package com.simoneventrici.feedlyBackend.datasource.dao

import com.simoneventrici.feedlyBackend.datasource.dto.news.NewsAndReactionsDto
import com.simoneventrici.feedlyBackend.datasource.dto.news.ReactionsDto
import com.simoneventrici.feedlyBackend.model.News
import com.simoneventrici.feedlyBackend.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.PreparedStatementCreator
import org.springframework.jdbc.core.PreparedStatementSetter
import org.springframework.stereotype.Repository

@Repository
class NewsDao(
    @Autowired val jdbcTemplate: JdbcTemplate
): Dao<News> {

    private val getAllQuery = "select * from news"
    private val saveQuery = "insert into news values(default,?,?,?,?,?,?,?,?,?,?,now(),?) on conflict do nothing "
    private val removeQuery = "delete from news where id=?"
    private val allNewsReactionsQuery = "select * from user_react_news where news=?"
    private val addReactionQuery = "insert into user_react_news values(?, ?, ?) on conflict (\"user\", news) do update set reaction = EXCLUDED.reaction"

    override fun getAll(): List<News> {
        val news = mutableListOf<News>()
        jdbcTemplate.query(getAllQuery) {
            news.add(News.fromResultSet(it))
        }
        return news
    }

    override fun save(elem: News) {
        var id: Int = -1
        jdbcTemplate.execute(saveQuery) {
            it.setString(1, elem.author)
            it.setString(2, elem.title)
            it.setString(3, elem.description)
            it.setString(4, elem.newsUrl)
            it.setString(5, elem.imageUrl)
            it.setString(6, elem.sourceName)
            it.setString(7, elem.sourceId)
            it.setString(8, elem.keyword)
            it.setString(9, elem.category?.value)
            it.setString(10, elem.publishedDate)
            it.setString(11, elem.language)
            it.execute()
        }
        // se la news è già presente nel db, prendo il suo id, altrimenti prendo l'id dell'ultima news inserita
        val creator = PreparedStatementCreator { it.prepareStatement("select id from news where news_url=?")}
        val setter = PreparedStatementSetter { it.setString(1, elem.newsUrl) }
        val query = jdbcTemplate.query(creator, setter) {
            if(it.next()) id = it.getInt((1))
            it.close()
        }
        if(id == -1) {
            jdbcTemplate.query("select id from news order by id desc limit 1") {
                id = it.getInt(1)
            }
        }
        elem.setId(id)
    }

    fun remove(elem: News) {
        jdbcTemplate.execute(removeQuery) {
            it.setInt(1, elem.getId())
            it.execute()
        }
    }

    fun getNewsReactions(newsId: Int, user: User): ReactionsDto {
        var userReaction: String? = null
        val reactionMap = mutableMapOf<String, Int>()
        val creator = PreparedStatementCreator { it.prepareStatement(allNewsReactionsQuery) }
        val setter = PreparedStatementSetter { it.setInt(1, newsId) }
        jdbcTemplate.query(creator, setter) { rs ->
            while(rs.next()) {
                val reaction = rs.getString("reaction")
                val username = rs.getString("user")
                if(username == user.getUsername())
                    userReaction = reaction

                reactionMap[reaction] = reactionMap.getOrElse(reaction) { 0 } + 1
            }
            rs.close()
        }
        return ReactionsDto(newsReactions = reactionMap, userReaction = userReaction)
    }

    // la insert lancia un'eccezione se non esiste il newsId. L'eccezione viene catturata poi nel controller
    fun addReactionToNews(newsId: Int, username: String, emoji: String) {
        jdbcTemplate.execute(addReactionQuery) {
            it.setString(1, username)
            it.setInt(2, newsId)
            it.setString(3, emoji)
            it.execute()
        }
    }
}