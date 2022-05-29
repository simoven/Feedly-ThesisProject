package com.simoneventrici.feedly.model.primitives

sealed class NewsCategory(val value: String) {
    class Business: NewsCategory("business")
    class General: NewsCategory("general")
    class Entertainment: NewsCategory("entertainment")
    class Health: NewsCategory("health")
    class Science: NewsCategory("science")
    class Sport: NewsCategory("sport")
    class Technology: NewsCategory("technology")

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
    }

}