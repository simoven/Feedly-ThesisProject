package com.simoneventrici.feedly.remote.dto

import com.simoneventrici.feedly.model.RecentActivity

data class RecentActivityDto(
    val activityParam1: String,
    val activityParam2: String?,
    val activityParam3: String?,
    val activityType: ActivityType,
    val date: String,
    val id: Int,
    val user: String
) {
    fun toRecentActivity(): RecentActivity {
        return RecentActivity(
            id = id,
            user = user,
            date = date,
            activityType = RecentActivity.ActivityType.parse(this.activityType.type),
            activityParam1 = activityParam1,
            activityParam2 = activityParam2,
            activityParam3 = activityParam3
        )
    }
}