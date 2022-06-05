package com.simoneventrici.feedlyBackend.controller

import com.simoneventrici.feedlyBackend.controller.exceptions.UnauthorizedException
import com.simoneventrici.feedlyBackend.datasource.dao.RecentActivityDao
import com.simoneventrici.feedlyBackend.model.RecentActivity
import com.simoneventrici.feedlyBackend.service.UserService
import org.json.simple.JSONObject
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("activity")
class ActivityController(
    private val userService: UserService,
    private val activityDao: RecentActivityDao
) {
    @ExceptionHandler(UnauthorizedException::class)
    fun handleUnauthorized(e: UnauthorizedException): ResponseEntity<JSONObject> {
        return ResponseEntity(JSONObject().apply { put("msg", e.message) }, HttpStatus.FORBIDDEN)
    }

    @GetMapping("getUserRecentActivity")
    fun getUserRecentActivity(
        @RequestHeader("Authorization") authToken: String
    ): Collection<RecentActivity> {
        val user = userService.checkUserToken(authToken) ?: throw UnauthorizedException(msg = "Invalid token provided")

        return activityDao.getUserActivity(user.getUsername()).sortedByDescending { it.date }
    }
}