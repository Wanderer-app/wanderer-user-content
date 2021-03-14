package ge.wanderer.core.model.rating

import ge.wanderer.core.model.content.UserAddedContent

interface IVote: UserAddedContent {
    fun weight(): Int
}