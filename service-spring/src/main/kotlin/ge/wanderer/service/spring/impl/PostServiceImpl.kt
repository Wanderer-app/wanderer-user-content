package ge.wanderer.service.spring.impl

import ge.wanderer.core.command.Command
import ge.wanderer.core.command.comment.AddCommentCommand
import ge.wanderer.core.command.content.ActivateContentCommand
import ge.wanderer.core.command.content.RemoveContentCommand
import ge.wanderer.core.command.content.ReportContentCommand
import ge.wanderer.core.command.discussion.CreatePostCommand
import ge.wanderer.core.command.discussion.UpdateDiscussionElementCommand
import ge.wanderer.core.command.rating.GiveOnePointCommand
import ge.wanderer.core.command.rating.RemoveVoteCommand
import ge.wanderer.core.configuration.ReportingConfiguration
import ge.wanderer.core.data.file.AttachedFile
import ge.wanderer.core.integration.user.UserService
import ge.wanderer.core.model.UpdateDiscussionElementData
import ge.wanderer.core.model.discussion.post.IPost
import ge.wanderer.core.model.rating.VoteType
import ge.wanderer.core.model.report.Report
import ge.wanderer.persistence.listing.ListingParams
import ge.wanderer.persistence.repository.CommentRepository
import ge.wanderer.persistence.repository.PostRepository
import ge.wanderer.service.protocol.data.CommentData
import ge.wanderer.service.protocol.data.DiscussionElementData
import ge.wanderer.service.protocol.data.RatingData
import ge.wanderer.service.protocol.interfaces.PostService
import ge.wanderer.service.protocol.request.*
import ge.wanderer.service.protocol.response.ServiceListingResponse
import ge.wanderer.service.protocol.response.ServiceResponse
import ge.wanderer.service.spring.CommentPreviewProvider
import ge.wanderer.service.spring.command.CommandProvider
import ge.wanderer.service.spring.data.data
import ge.wanderer.service.spring.data.noDataResponse
import ge.wanderer.service.spring.data.ratingResponse
import ge.wanderer.service.spring.model.NoComment
import ge.wanderer.service.spring.model.NoPost
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class PostServiceImpl(
    @Autowired private val userService: UserService,
    @Autowired private val commentPreviewProvider: CommentPreviewProvider,
    @Autowired private val commandProvider: CommandProvider,
    @Autowired private val postRepository: PostRepository,
    @Autowired private val commentRepository: CommentRepository,
    @Autowired private val reportingConfiguration: ReportingConfiguration
): PostService {

    override fun createPost(request: CreatePostRequest): ServiceResponse<DiscussionElementData> {
        val user = userService.findUserById(request.userId)
        val command = CreatePostCommand(request.onDate, user, request.routeCode, request.text, request.attachedFiles.map { AttachedFile() })

        return response(
            commandProvider.decoratePersistentCommand(command, NoPost(), postRepository)
        )
    }

    override fun updatePost(request: UpdatePostRequest): ServiceResponse<DiscussionElementData> {
        val post = postRepository.findById(request.postId)
        val user = userService.findUserById(request.updaterId)
        val updateData = UpdateDiscussionElementData(request.newText, request.files)
        val command = UpdateDiscussionElementCommand(post, updateData, user)

        return response(
            commandProvider.decorateCommand(command, post)
        )
    }

    override fun findById(id: Long): ServiceResponse<DiscussionElementData> {
        val post = postRepository.findById(id)
        return ServiceResponse(true, "Post fetched", post.dataWithCommentsPreview())
    }

    override fun activate(request: OperateOnContentRequest): ServiceResponse<DiscussionElementData> {
        val user = userService.findUserById(request.userId)
        val post = postRepository.findById(request.contentId)
        val command = ActivateContentCommand(user, post, request.date, userService)

        return response(
            commandProvider.decorateCommand(command, post)
        )
    }

    override fun remove(request: OperateOnContentRequest): ServiceResponse<DiscussionElementData> {
        val user = userService.findUserById(request.userId)
        val post = postRepository.findById(request.contentId)
        val command = RemoveContentCommand(user, post, request.date, userService)

        return response(
            commandProvider.decorateCommand(command, post)
        )
    }

    override fun giveUpVote(request: OperateOnContentRequest): ServiceResponse<RatingData> {
        val post = postRepository.findById(request.contentId)
        val user = userService.findUserById(request.userId)
        val command = GiveOnePointCommand(VoteType.UP, user, request.date, post, userService)

        return ratingResponse(
            commandProvider.decorateCommand(command, post)
        )
    }

    override fun giveDownVote(request: OperateOnContentRequest): ServiceResponse<RatingData> {
        val post = postRepository.findById(request.contentId)
        val user = userService.findUserById(request.userId)
        val command = GiveOnePointCommand(VoteType.DOWN, user, request.date, post, userService)

        return ratingResponse(
            commandProvider.decorateCommand(command, post)
        )
    }

    override fun removeVote(request: OperateOnContentRequest): ServiceResponse<RatingData> {
        val post = postRepository.findById(request.contentId)
        val user = userService.findUserById(request.userId)
        val command = RemoveVoteCommand(user, request.date, post)

        return ratingResponse(
            commandProvider.decorateCommand(command, post)
        )
    }

    override fun addComment(request: AddCommentRequest): ServiceResponse<CommentData> {
        val post = postRepository.findById(request.contentId)
        val user = userService.findUserById(request.commenterId)
        val command = AddCommentCommand(request.commentContent, user, request.date, post, userService)

        val result = commandProvider.decorateCommand(command, NoComment()).execute()
        return ServiceResponse(result.isSuccessful, result.message, result.returnedModel.data())    }

    override fun listComments(contentId: Long, listingParams: ListingParams): ServiceListingResponse<CommentData> {
        val post = postRepository.findById(contentId)
        val comments = commentRepository.listActiveFor(post, listingParams)
        return ServiceListingResponse(true, "Comments Retrieved!", comments.size, listingParams.batchNumber, comments.map { it.data() })
    }

    override fun report(request: ReportContentRequest): ServiceResponse<Report> {
        val post = postRepository.findById(request.contentId)
        val user = userService.findUserById(request.userId)

        val commandResult = ReportContentCommand(user, request.date, request.reportReason, post, userService, reportingConfiguration)
            .execute()

        return noDataResponse(commandResult.isSuccessful, commandResult.message)
    }

    override fun listReportsForContent(contentId: Long): ServiceListingResponse<Report> {
        val post = postRepository.findById(contentId)
        val reports = post.reports()
        return ServiceListingResponse(true, "Reports Retrieved!", reports.size, 1, reports.toList())
    }

    private fun IPost.dataWithCommentsPreview(): DiscussionElementData {
        return this.data(commentPreviewProvider.getPreviewFor(this))
    }

    private fun response(command: Command<IPost>): ServiceResponse<DiscussionElementData> {
        val executionResult = command.execute()
        val data = executionResult.returnedModel.dataWithCommentsPreview()
        return ServiceResponse(executionResult.isSuccessful, executionResult.message, data)
    }
}