package ge.wanderer.service.protocol.interfaces

import ge.wanderer.service.protocol.data.DiscussionElementData
import ge.wanderer.service.protocol.interfaces.base.CommentableContentService
import ge.wanderer.service.protocol.interfaces.base.UserContentService
import ge.wanderer.service.protocol.request.*
import ge.wanderer.service.protocol.response.ServiceResponse

interface PollService : UserContentService<DiscussionElementData>, CommentableContentService {

    fun createPoll(request: CreatePollRequest): ServiceResponse<DiscussionElementData>
    fun updatePoll(request: UpdatePollRequest): ServiceResponse<DiscussionElementData>
    fun addAnswer(request: AddAnswerToPollRequest): ServiceResponse<DiscussionElementData>
    fun removeAnswer(request: RemovePollAnswerRequest): ServiceResponse<DiscussionElementData>
    fun selectAnswer(request: SelectPollAnswerRequest): ServiceResponse<DiscussionElementData>
}