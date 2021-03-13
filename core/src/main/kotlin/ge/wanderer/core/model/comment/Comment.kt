package ge.wanderer.core.model.comment

import ge.wanderer.core.model.content.PublicContent
import ge.wanderer.core.model.user.User

interface Comment: PublicContent {
    fun text(): String
}