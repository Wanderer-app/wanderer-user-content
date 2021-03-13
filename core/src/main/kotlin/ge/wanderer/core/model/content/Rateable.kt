package ge.wanderer.core.model.content

interface Rateable {
    fun giveVote(vote: Vote)
    fun rating(): Int
}