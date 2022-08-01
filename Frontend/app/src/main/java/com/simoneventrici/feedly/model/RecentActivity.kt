package com.simoneventrici.feedly.model

data class RecentActivity(
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
}