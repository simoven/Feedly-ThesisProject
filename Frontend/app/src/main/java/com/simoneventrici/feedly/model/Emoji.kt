package com.simoneventrici.feedly.model

import com.simoneventrici.feedly.R
import java.lang.IllegalStateException

sealed class Emoji(val res_id: Int, val unicode_str: String) {
    class ThumbsUp : Emoji(res_id = R.drawable.emoji_1f44d, unicode_str = "1f44d")
    class WowFace : Emoji(res_id = R.drawable.emoji_1f62f, unicode_str = "1f62f")
    class LaughFace : Emoji(res_id = R.drawable.emoji_1f602, unicode_str = "1f602")
    class AngryFace : Emoji(res_id = R.drawable.emoji_1f621, unicode_str = "1f621")
    class SadFace : Emoji(res_id = R.drawable.emoji_1f625, unicode_str = "1f625")
    class Heart : Emoji(res_id = R.drawable.emoji_2764, unicode_str = "2764")

    object Loader {
        fun load(unicode: String): Emoji {
            return when(unicode) {
                "1f44d" -> ThumbsUp()
                "1f62f" -> WowFace()
                "1f602" -> LaughFace()
                "1f621" -> AngryFace()
                "1f625" -> SadFace()
                "2764" -> Heart()
                else -> throw IllegalStateException("Invalid emoji")
            }
        }
    }
}
