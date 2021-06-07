package ge.wanderer.service.spring.impl.decorator

import ge.wanderer.service.protocol.data.CommentData
import ge.wanderer.service.protocol.data.DiscussionElementData
import ge.wanderer.service.protocol.data.RatingData
import ge.wanderer.service.protocol.data.ReportData
import ge.wanderer.service.protocol.interfaces.PostService
import ge.wanderer.service.protocol.request.*
import ge.wanderer.service.protocol.response.ServiceListingResponse
import ge.wanderer.service.protocol.response.ServiceResponse
import ge.wanderer.service.spring.impl.PostServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component

@Component
@Primary
class ExceptionHandlingPostService(
    @Autowired private val postServiceImpl: PostServiceImpl
): PostService {
    override fun createPost(request: CreatePostRequest): ServiceResponse<DiscussionElementData> =
        handle { postServiceImpl.createPost(request) }

    override fun updatePost(request: UpdatePostRequest): ServiceResponse<DiscussionElementData> =
        handle { postServiceImpl.updatePost(request) }

    override fun findById(id: Long, requestingUserId: Long?): ServiceResponse<DiscussionElementData> =
        handle { postServiceImpl.findById(id, requestingUserId) }

    override fun activate(request: OperateOnContentRequest): ServiceResponse<DiscussionElementData> =
        handle { postServiceImpl.activate(request) }

    override fun remove(request: OperateOnContentRequest): ServiceResponse<DiscussionElementData> =
        handle { postServiceImpl.remove(request) }

    override fun giveUpVote(request: OperateOnContentRequest): ServiceResponse<RatingData> =
        handle { postServiceImpl.giveUpVote(request) }

    override fun giveDownVote(request: OperateOnContentRequest): ServiceResponse<RatingData> =
        handle { postServiceImpl.giveDownVote(request) }

    override fun removeVote(request: OperateOnContentRequest): ServiceResponse<RatingData> =
        handle { postServiceImpl.removeVote(request) }

    override fun addComment(request: AddCommentRequest): ServiceResponse<CommentData> =
        handle { postServiceImpl.addComment(request) }

    override fun listComments(request: ListCommentsRequest): ServiceListingResponse<CommentData> =
        handleListing { postServiceImpl.listComments(request) }

    override fun report(request: ReportContentRequest): ServiceResponse<ReportData> =
        handle { postServiceImpl.report(request) }

    override fun listReportsForContent(contentId: Long): ServiceListingResponse<ReportData> =
        handleListing { postServiceImpl.listReportsForContent(contentId) }
}