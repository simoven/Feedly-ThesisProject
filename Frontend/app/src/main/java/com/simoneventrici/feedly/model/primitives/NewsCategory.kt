package com.simoneventrici.feedly.model.primitives

import com.simoneventrici.feedly.R

sealed class NewsCategory(val value: String) {
    class Business: NewsCategory("business")
    class General: NewsCategory("general")
    class Entertainment: NewsCategory("entertainment")
    class Health: NewsCategory("health")
    class Science: NewsCategory("science")
    class Sport: NewsCategory("sport")
    class Technology: NewsCategory("technology")

    fun getNameId(): Int {
        return when(this) {
            is Business -> R.string.business
            is General -> R.string.general
            is Entertainment -> R.string.entertainment
            is Health -> R.string.health
            is Science -> R.string.science
            is Sport -> R.string.sport
            is Technology -> R.string.technology
        }
    }

    companion object {
        fun parse(str: String): NewsCategory {
            return when(str) {
                "business" -> Business()
                "general" -> General()
                "entertainment" -> Entertainment()
                "health" -> Health()
                "science" -> Science()
                "sport" -> Sport()
                "technology" -> Technology()
                else -> throw IllegalStateException("Invalid category")
            }
        }

        fun getAll() : List<NewsCategory> {
            return listOf(General(), Entertainment(), Business(), Health(), Science(), Sport(), Technology())
        }
    }

}