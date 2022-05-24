package com.simoneventrici.feedlyBackend.datasource.dao

import com.simoneventrici.feedlyBackend.model.News
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class NewsDao(
    @Autowired val jdbcTemplate: JdbcTemplate
): Dao<News> {

    private val getAllQuery = "select * from news"
    private val saveQuery = "insert into news values(default,?,?,?,?,?,?,?,?,?,?,now(),?) on conflict do nothing "
    private val removeQuery = "delete from news where id=?"

    override fun getAll(): List<News> {
        val news = mutableListOf<News>()
        jdbcTemplate.query(getAllQuery) {
            news.add(News.fromResultSet(it))
        }
        return news
    }

    override fun save(elem: News) {
        var id: Int = 0
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
        jdbcTemplate.query("select id from news order by id desc limit 1") {
            id = it.getInt(1)
        }
        println(id)
        elem.setId(id)
    }

    fun remove(elem: News) {
        jdbcTemplate.execute(removeQuery) {
            it.setInt(1, elem.getId())
            it.execute()
        }
    }

}