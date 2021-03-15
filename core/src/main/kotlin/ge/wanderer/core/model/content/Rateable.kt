package ge.wanderer.core.model.content

import ge.wanderer.core.model.rating.IVote

interface Rateable {
    fun giveVote(vote: IVote)
    fun rating(): Int
}