package com.simoneventrici.feedlyBackend.model

import java.sql.ResultSet

// i parametri sono general purpose, nel caso di:
// newsReaction : param1 -> newsId, param2 -> reaction, param3 -> newsImageUrl
class RecentActivity(
    val id: Int,
    val user: String,
    val activityType: ActivityType,
    val activityParam1: String,
    val activityParam2: String?,
    val activityParam3: String?,
    val date: String
) {
    sealed class ActivityType(val isAdding: Boolean, val type: String) {
        class NewsReaction(isAdding: Boolean): ActivityType(isAdding, if(isAdding) "added_reaction" else "removed_reaction")

        companion object {
            fun parse(value: String): ActivityType {
                return when(value) {
                    "added_reaction" -> NewsReaction(true)
                    "removed_reaction" -> NewsReaction(false)
                    else -> throw IllegalStateException("Invalid activity type")
                }
            }
        }
    }

    companion object {
        fun fromResultSet(rs: ResultSet): RecentActivity {
            return RecentActivity(
                id = rs.getInt(1),
                user = rs.getString(2),
                activityType = ActivityType.parse(rs.getString(3)),
                activityParam1 = rs.getString(4),
                activityParam2 = rs.getString(5),
                activityParam3 = rs.getString(6),
                date = rs.getString(7)
            )
        }
    }
}