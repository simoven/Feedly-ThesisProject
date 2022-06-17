package com.simoneventrici.feedlyBackend.datasource.api

import com.simoneventrici.feedlyBackend.datasource.dto.soccer.LeagueStandingsDto
import com.simoneventrici.feedlyBackend.datasource.dto.soccer.MatchFixturesDto
import com.simoneventrici.feedlyBackend.util.Properties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange

@Component
class FootballAPI(
    @Autowired private val restTemplate: RestTemplate,
    @Autowired private val appProperties: Properties
) {

    private val BASE_URL = "https://api-football-v1.p.rapidapi.com/v3"

    fun getMatchesByTeamId(teamId: Int, seasonYear: Int, lastMatches: Int = 5): ResponseEntity<MatchFixturesDto?> {
        val url = "$BASE_URL/fixtures?team=$teamId&season=$seasonYear&last=$lastMatches"
        val headers = HttpHeaders().apply { set("X-RapidAPI-Key", appProperties.footballApiKey) }
        return restTemplate.exchange<MatchFixturesDto?>(url, HttpMethod.GET, HttpEntity<String>(headers))
    }

    fun getStandingsByLeagueId(leagueId: Int, seasonYear: Int): ResponseEntity<LeagueStandingsDto?> {
        val url = "$BASE_URL/standings?season=$seasonYear&league=$leagueId"
        println(url)
        val headers = HttpHeaders().apply { set("X-RapidAPI-Key", appProperties.footballApiKey) }
        return restTemplate.exchange<LeagueStandingsDto?>(url, HttpMethod.GET, HttpEntity<String>(headers))
    }

}