package ge.wanderer.core.model.content

import ge.wanderer.core.model.rating.Vote

interface Rateable {
    fun giveVote(vote: Vote)
    fun rating(): Int
}