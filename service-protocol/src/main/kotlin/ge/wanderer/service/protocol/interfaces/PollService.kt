package ge.wanderer.service.protocol.interfaces

import ge.wanderer.service.protocol.data.DiscussionElementData
import ge.wanderer.service.protocol.interfaces.base.CommentableContentService
import ge.wanderer.service.protocol.interfaces.base.RateableContentService
import ge.wanderer.service.protocol.interfaces.base.UserContentService
import ge.wanderer.service.protocol.request.CreatePollRequest
import ge.wanderer.service.protocol.request.UpdatePollRequest
import ge.wanderer.service.protocol.response.ServiceResponse

interface PollService : UserContentService<DiscussionElementData>, RateableContentService, CommentableContentService {

    fun createPoll(request: CreatePollRequest): ServiceResponse<DiscussionElementData>
    fun updatePoll(request: UpdatePollRequest): ServiceResponse<DiscussionElementData>
}