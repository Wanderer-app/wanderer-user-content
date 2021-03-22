package ge.wanderer.service.protocol.interfaces.base

import ge.wanderer.service.protocol.data.RatingData
import ge.wanderer.service.protocol.request.OperateOnContentRequest
import ge.wanderer.service.protocol.response.ServiceResponse

interface RateableContentService {
    fun giveUpVote(request: OperateOnContentRequest): ServiceResponse<RatingData>
    fun giveDownVote(request: OperateOnContentRequest): ServiceResponse<RatingData>
    fun removeVote(request: OperateOnContentRequest): ServiceResponse<RatingData>
}