package ge.wanderer.service.protocol.interfaces.base

import ge.wanderer.service.protocol.request.GiveVoteRequest
import ge.wanderer.service.protocol.data.RatingData
import ge.wanderer.service.protocol.response.ServiceResponse

interface RateableContentService {
    fun giveUpVote(request: GiveVoteRequest): ServiceResponse<RatingData>
    fun giveDownVote(request: GiveVoteRequest): ServiceResponse<RatingData>
}