package com.simoneventrici.feedlyBackend.model

data class TeamMatch(
  val homeTeamId: Int,
  val awayTeamId: Int,
  val homeTeamName: String,
  val awayTeamName: String,
  val leagueName: String,
  val leagueId: Int,
  val homeScore: Int,
  val awayScore: Int,
  val timestamp: Long,
)