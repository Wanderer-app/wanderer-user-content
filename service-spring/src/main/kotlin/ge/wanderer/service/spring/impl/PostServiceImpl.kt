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
import ge.wanderer.core.integration.file.AttachedFile
import ge.wanderer.core.integration.user.UserService
import ge.wanderer.core.model.UpdateDiscussionElementData
import ge.wanderer.core.model.discussion.post.IPost
import ge.wanderer.common.enums.VoteType
import ge.wanderer.core.integration.user.User
import ge.wanderer.persistence.repository.CommentRepository
import ge.wanderer.persistence.repository.PostRepository
import ge.wanderer.service.protocol.data.CommentData
import ge.wanderer.service.protocol.data.DiscussionElementData
import ge.wanderer.service.protocol.data.RatingData
import ge.wanderer.service.protocol.data.ReportData
import ge.wanderer.service.protocol.interfaces.PostService
import ge.wanderer.service.protocol.request.*
import ge.wanderer.service.protocol.response.ServiceListingResponse
import ge.wanderer.service.protocol.response.ServiceResponse
import ge.wanderer.service.spring.CommentPreviewProvider
import ge.wanderer.service.spring.command.CommandProvider
import ge.wanderer.service.spring.data.data
import ge.wanderer.service.spring.data.getRequestingUser
import ge.wanderer.service.spring.data.noDataResponse
import ge.wanderer.service.spring.data.ratingResponse
import ge.wanderer.service.spring.logger
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
        val command = CreatePostCommand(request.onDate, user, request.routeCode, request.text, request.attachedFiles.map { AttachedFile(it.externalId, it.fileType) })

        return response(
            commandProvider.decoratePersistentCommand(command, NoPost(), postRepository, logger()), user
        )
    }

    override fun updatePost(request: UpdatePostRequest): ServiceResponse<DiscussionElementData> {
        val post = postRepository.findById(request.postId)
        val user = userService.findUserById(request.updaterId)
        val updateData = UpdateDiscussionElementData(request.newText, request.files.map { AttachedFile(it.externalId, it.fileType) })
        val command = UpdateDiscussionElementCommand(post, updateData, user)

        return response(
            commandProvider.decorateCommand(command, post, logger()), user
        )
    }

    override fun findById(id: Long, requestingUserId: Long?): ServiceResponse<DiscussionElementData> {
        val post = postRepository.findById(id)
        return ServiceResponse(true, "Post fetched", post.dataWithCommentsPreview(getRequestingUser(requestingUserId, userService)))
    }

    override fun activate(request: OperateOnContentRequest): ServiceResponse<DiscussionElementData> {
        val user = userService.findUserById(request.userId)
        val post = postRepository.findById(request.contentId)
        val command = ActivateContentCommand(user, post, request.date, userService)

        return response(
            commandProvider.decorateCommand(command, post, logger()), user
        )
    }

    override fun remove(request: OperateOnContentRequest): ServiceResponse<DiscussionElementData> {
        val user = userService.findUserById(request.userId)
        val post = postRepository.findById(request.contentId)
        val command = RemoveContentCommand(user, post, request.date, userService)

        return response(
            commandProvider.decorateCommand(command, post, logger()), user
        )
    }

    override fun giveUpVote(request: OperateOnContentRequest): ServiceResponse<RatingData> {
        val post = postRepository.findById(request.contentId)
        val user = userService.findUserById(request.userId)
        val command = GiveOnePointCommand(VoteType.UP, user, request.date, post, userService)

        return ratingResponse(
            commandProvider.decorateCommand(command, post, logger())
        )
    }

    override fun giveDownVote(request: OperateOnContentRequest): ServiceResponse<RatingData> {
        val post = postRepository.findById(request.contentId)
        val user = userService.findUserById(request.userId)
        val command = GiveOnePointCommand(VoteType.DOWN, user, request.date, post, userService)

        return ratingResponse(
            commandProvider.decorateCommand(command, post, logger())
        )
    }

    override fun removeVote(request: OperateOnContentRequest): ServiceResponse<RatingData> {
        val post = postRepository.findById(request.contentId)
        val user = userService.findUserById(request.userId)
        val command = RemoveVoteCommand(user, request.date, post)

        return ratingResponse(
            commandProvider.decorateCommand(command, post, logger())
        )
    }

    override fun addComment(request: AddCommentRequest): ServiceResponse<CommentData> {
        val post = postRepository.findById(request.contentId)
        val user = userService.findUserById(request.commenterId)
        val command = AddCommentCommand(request.commentContent, user, request.date, post, userService)

        val result = commandProvider.decorateCommand(command, NoComment(), logger()).execute()
        return ServiceResponse(result.isSuccessful, result.message, result.returnedModel.data(user))    }

    override fun listComments(request: ListCommentsRequest): ServiceListingResponse<CommentData> {
        val post = postRepository.findById(request.contentId)
        val user = getRequestingUser(request.requestingUserId, userService)
        val comments = commentRepository.listActiveFor(post, request.listingParams)
        return ServiceListingResponse(true, "Comments Retrieved!", comments.size, request.listingParams.batchNumber, comments.map { it.data(user) })
    }

    override fun report(request: ReportContentRequest): ServiceResponse<ReportData> {
        val post = postRepository.findById(request.contentId)
        val user = userService.findUserById(request.userId)

        val command = ReportContentCommand(user, request.date, request.reportReason, post, userService, reportingConfiguration)
        val commandResult = commandProvider.decorateCommand(command, post, logger()).execute()

        return noDataResponse(commandResult.isSuccessful, commandResult.message)
    }

    override fun listReportsForContent(contentId: Long): ServiceListingResponse<ReportData> {
        val post = postRepository.findById(contentId)
        val reports = post.reports()
        return ServiceListingResponse(true, "Reports Retrieved!", reports.size, 1, reports.map { it.data() })
    }

    private fun IPost.dataWithCommentsPreview(user: User): DiscussionElementData {
        return this.data(commentPreviewProvider.getPreviewFor(this, user), user)
    }

    private fun response(command: Command<IPost>, user: User): ServiceResponse<DiscussionElementData> {
        val executionResult = command.execute()
        val data = executionResult.returnedModel.dataWithCommentsPreview(user)
        return ServiceResponse(executionResult.isSuccessful, executionResult.message, data)
    }
}