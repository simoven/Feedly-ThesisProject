package com.simoneventrici.feedlyBackend.controller

import com.simoneventrici.feedlyBackend.controller.exceptions.UnauthorizedException
import com.simoneventrici.feedlyBackend.model.LeagueStandings
import com.simoneventrici.feedlyBackend.model.SoccerTeam
import com.simoneventrici.feedlyBackend.model.TeamMatch
import com.simoneventrici.feedlyBackend.service.SoccerService
import com.simoneventrici.feedlyBackend.service.UserService
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

    @GetMapping("matchesByTeamId")
    fun getMatchesByTeamId(
        @RequestParam("teamId") teamId: Int
    ): Collection<TeamMatch> {
        return soccerService.getMatchesByTeamId(teamId)
    }

    @GetMapping("standingsByLeagueId")
    fun getStandingsByLeagueId(
        @RequestParam("leagueId") leagueId: Int
    ): Collection<LeagueStandings> {
        return soccerService.getStandingsByLeagueId(leagueId)
    }

    @PostMapping("addFavouriteTeam")
    fun addFavouriteTeam(
        @RequestHeader("Authorization") token: String,
        @RequestBody body: JSONObject
    ) {
        val user = userService.checkUserToken(token) ?: throw UnauthorizedException(msg = "Invalid token provided")
        val teamId = body["teamId"] as Int? ?: throw IllegalStateException("Team id not provided")

        soccerService.addUserFavouriteTeam(user, teamId)
    }

    @PostMapping("removeFavouriteTeam")
    fun removeFavouriteTeam(
        @RequestHeader("Authorization") token: String,
        @RequestBody body: JSONObject
    ) {
        val user = userService.checkUserToken(token) ?: throw UnauthorizedException(msg = "Invalid token provided")
        val teamId = body["teamId"] as Int? ?: throw IllegalStateException("Team id not provided")

        soccerService.removeUserFavouriteTeam(user, teamId)
    }

    @GetMapping("userFavouriteTeams")
    fun getUserFavouriteTeams(
        @RequestHeader("Authorization") token: String
    ): List<SoccerTeam> {
        val user = userService.checkUserToken(token) ?: throw UnauthorizedException(msg = "Invalid token provided")
        return soccerService.getUserFavouriteTeam(user)
    }

}