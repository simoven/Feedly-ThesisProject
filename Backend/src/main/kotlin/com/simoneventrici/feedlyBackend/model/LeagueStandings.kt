package com.simoneventrici.feedlyBackend.model

import com.fasterxml.jackson.annotation.JsonProperty

data class LeagueStandings(
    val rank: Int,
    @JsonProperty("team_id") val teamId: Int,
    @JsonProperty("team_name") val teamName: String,
    val points: Int,
    val played: Int,
    @JsonProperty("matches_won") val matchesWon: Int,
    @JsonProperty("matches_draw") val matchesDraw: Int,
    @JsonProperty("matches_lost") val matchesLost: Int,
    @JsonProperty("goal_scored") val goalScored: Int,
    @JsonProperty("goal_against") val goalAgainst: Int
) {
}