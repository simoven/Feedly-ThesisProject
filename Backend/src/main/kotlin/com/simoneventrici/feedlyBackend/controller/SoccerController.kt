package com.simoneventrici.feedlyBackend.controller

import com.simoneventrici.feedlyBackend.controller.exceptions.UnauthorizedException
import com.simoneventrici.feedlyBackend.model.LeagueStandings
import com.simoneventrici.feedlyBackend.model.SoccerLeague
import com.simoneventrici.feedlyBackend.model.SoccerTeam
import com.simoneventrici.feedlyBackend.model.TeamMatch
import com.simoneventrici.feedlyBackend.service.SoccerService
import com.simoneventrici.feedlyBackend.service.UserService
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.postgresql.util.PSQLException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("soccer")
class SoccerController(
    val soccerService: SoccerService,
    val userService: UserService
) {

    @ExceptionHandler(IllegalStateException::class)
    fun handleIllegalState(e: IllegalStateException): ResponseEntity<JSONObject> {
        return ResponseEntity(JSONObject().apply { put("msg", e.message ?: "Invalid arguments provided") }, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(PSQLException::class)
    fun handlePsqlException(e: PSQLException): ResponseEntity<JSONObject> {
        if(e.message?.contains("foreign", ignoreCase = true) == true)
            return ResponseEntity(JSONObject().apply { put("msg", "Invalid soccer team/league provided") }, HttpStatus.BAD_REQUEST)

        return ResponseEntity(JSONObject().apply { put("msg", "Internal server error") }, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(UnauthorizedException::class)
    fun handleUnauthorized(e: UnauthorizedException): ResponseEntity<JSONObject> {
        return ResponseEntity(JSONObject().apply { put("msg", e.message) }, HttpStatus.FORBIDDEN)
    }

    @GetMapping("userFavouriteTeamsMatches")
    fun getUserFavouritesTeamsMatches(
        @RequestHeader("Authorization") token: String
    ): Collection<TeamMatch> {
        val user = userService.checkUserToken(token) ?: throw UnauthorizedException(msg = "Invalid token provided")

        return soccerService.getUserFavouriteTeamsMatches(user)
    }

    @GetMapping("standingsByLeagueId")
    fun getStandingsByLeagueId(
        @RequestParam("leagueId") leagueId: Int
    ): Collection<LeagueStandings> {
        return soccerService.getStandingsByLeagueId(leagueId)
    }

    @PostMapping("setFavouriteTeams")
    fun setFavouriteTeams(
        @RequestHeader("Authorization") token: String,
        @RequestBody body: JSONObject
    ) {
        val user = userService.checkUserToken(token) ?: throw UnauthorizedException(msg = "Invalid token provided")
        val teamIds = body["teamIds"] as ArrayList<*>? ?: throw IllegalStateException("Team ids not provided")
        val idList = mutableListOf<Int>()
        teamIds.forEach {
            kotlin.runCatching {
                idList.add(it as Int)
            }.onFailure {
                throw IllegalStateException("Invalid value provided as team id")
            }
        }

        soccerService.setUserFavouriteTeam(user, idList)
    }

    @GetMapping("userFavouriteTeams")
    fun getUserFavouriteTeams(
        @RequestHeader("Authorization") token: String
    ): List<SoccerTeam> {
        val user = userService.checkUserToken(token) ?: throw UnauthorizedException(msg = "Invalid token provided")
        return soccerService.getUserFavouriteTeam(user)
    }

    @GetMapping("allTeams")
    fun getAllTeams(): List<SoccerTeam> {
        return soccerService.allTeams
    }

    @GetMapping("allSoccerLeagues")
    fun getAllSoccerLeagues(): List<SoccerLeague> {
        return soccerService.allLeagues
    }

}