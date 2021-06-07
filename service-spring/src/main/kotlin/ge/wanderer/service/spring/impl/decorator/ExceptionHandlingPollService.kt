package ge.wanderer.service.spring.impl.decorator

import ge.wanderer.service.protocol.data.CommentData
import ge.wanderer.service.protocol.data.DiscussionElementData
import ge.wanderer.service.protocol.interfaces.PollService
import ge.wanderer.service.protocol.request.*
import ge.wanderer.service.protocol.response.ServiceListingResponse
import ge.wanderer.service.protocol.response.ServiceResponse
import ge.wanderer.service.spring.impl.PollServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component

@Component
@Primary
class ExceptionHandlingPollService(
    @Autowired private val pollServiceImpl: PollServiceImpl
): PollService {
    override fun createPoll(request: CreatePollRequest): ServiceResponse<DiscussionElementData> =
        handle { pollServiceImpl.createPoll(request) }

    override fun updatePoll(request: UpdatePollRequest): ServiceResponse<DiscussionElementData> =
        handle { pollServiceImpl.updatePoll(request) }

    override fun addAnswer(request: AddAnswerToPollRequest): ServiceResponse<DiscussionElementData> =
        handle { pollServiceImpl.addAnswer(request) }

    override fun removeAnswer(request: RemovePollAnswerRequest): ServiceResponse<DiscussionElementData> =
        handle { pollServiceImpl.removeAnswer(request) }

    override fun selectAnswer(request: SelectPollAnswerRequest): ServiceResponse<DiscussionElementData> =
        handle { pollServiceImpl.selectAnswer(request) }

    override fun findById(id: Long, requestingUserId: Long?): ServiceResponse<DiscussionElementData> =
        handle { pollServiceImpl.findById(id, requestingUserId) }

    override fun activate(request: OperateOnContentRequest): ServiceResponse<DiscussionElementData> =
        handle { pollServiceImpl.activate(request) }

    override fun remove(request: OperateOnContentRequest): ServiceResponse<DiscussionElementData> =
        handle { pollServiceImpl.remove(request) }

    override fun addComment(request: AddCommentRequest): ServiceResponse<CommentData> =
        handle { pollServiceImpl.addComment(request) }

    override fun listComments(request: ListCommentsRequest): ServiceListingResponse<CommentData> =
        handleListing { pollServiceImpl.listComments(request) }
}