package com.simoneventrici.feedlyBackend.model.primitives

sealed class Emoji(val unicode_str: String) {
    class ThumbsUp : Emoji(unicode_str = "1f44d")
    class WowFace : Emoji(unicode_str = "1f62f")
    class LaughFace : Emoji(unicode_str = "1f602")
    class AngryFace : Emoji(unicode_str = "1f621")
    class SadFace : Emoji(unicode_str = "1f625")
    class Heart : Emoji(unicode_str = "2764")

    object Loader {
        fun load(unicode: String): Emoji {
            return when(unicode) {
                "1f44d" -> ThumbsUp()
                "1f62f" -> WowFace()
                "1f621" -> AngryFace()
                "1f625" -> SadFace()
                "2764" -> Heart()
                "1f602" -> LaughFace()
                else -> throw IllegalStateException("Invalid emoji code")
            }
        }
    }
}