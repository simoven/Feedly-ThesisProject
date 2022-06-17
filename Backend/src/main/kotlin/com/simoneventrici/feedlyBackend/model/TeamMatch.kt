package com.simoneventrici.feedlyBackend.model

import com.fasterxml.jackson.annotation.JsonProperty

data class TeamMatch(
  @JsonProperty("home_team_id") val homeTeamId: Int,
  @JsonProperty("away_team_id") val awayTeamId: Int,
  @JsonProperty("home_team_name") val homeTeamName: String,
  @JsonProperty("away_team_name") val awayTeamName: String,
  @JsonProperty("league_name") val leagueName: String,
  @JsonProperty("league_id") val leagueId: Int,
  @JsonProperty("home_score") val homeScore: Int,
  @JsonProperty("away_score") val awayScore: Int,
  val timestamp: Long,
)