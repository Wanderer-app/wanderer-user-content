package ge.wanderer.service.spring.impl

import ge.wanderer.core.command.Command
import ge.wanderer.core.command.comment.AddCommentCommand
import ge.wanderer.core.command.comment.UpdateCommentCommand
import ge.wanderer.core.command.content.ActivateContentCommand
import ge.wanderer.core.command.content.RemoveContentCommand
import ge.wanderer.core.command.content.ReportContentCommand
import ge.wanderer.core.command.rating.GiveOnePointCommand
import ge.wanderer.core.command.rating.RemoveVoteCommand
import ge.wanderer.core.configuration.ReportingConfiguration
import ge.wanderer.core.integration.user.UserService
import ge.wanderer.core.model.UpdateCommentData
import ge.wanderer.core.model.comment.IComment
import ge.wanderer.core.model.rating.VoteType
import ge.wanderer.core.model.report.Report
import ge.wanderer.core.repository.CommentRepository
import ge.wanderer.service.protocol.data.CommentData
import ge.wanderer.service.protocol.data.RatingData
import ge.wanderer.service.protocol.interfaces.CommentService
import ge.wanderer.service.protocol.request.AddCommentRequest
import ge.wanderer.service.protocol.request.OperateOnContentRequest
import ge.wanderer.service.protocol.request.ReportContentRequest
import ge.wanderer.service.protocol.request.UpdateCommentRequest
import ge.wanderer.common.listing.ListingParams
import ge.wanderer.service.protocol.response.ServiceListingResponse
import ge.wanderer.service.protocol.response.ServiceResponse
import ge.wanderer.service.spring.CommentPreviewProvider
import ge.wanderer.service.spring.command.CommandProvider
import ge.wanderer.service.spring.data.data
import ge.wanderer.service.spring.data.ratingResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CommentServiceImpl(
    @Autowired private val commentRepository: CommentRepository,
    @Autowired private val userService: UserService,
    @Autowired private val commandProvider: CommandProvider,
    @Autowired private val commentPreviewProvider: CommentPreviewProvider,
    @Autowired private val reportingConfiguration: ReportingConfiguration
): CommentService {

    override fun updateComment(request: UpdateCommentRequest): ServiceResponse<CommentData> {
        val comment = commentRepository.findById(request.commentId)
        val user = userService.findUserById(request.updaterId)
        val command = UpdateCommentCommand(comment, UpdateCommentData(request.text), user)

        return response(
            commandProvider.decorateCommand(command, comment)
        )
    }

    override fun findById(id: Long): ServiceResponse<CommentData> {
        val comment = commentRepository.findById(id)
        return ServiceResponse(true, "Successfully retrieved comment", comment.dataWithRepliesPreview())
    }

    override fun activate(request: OperateOnContentRequest): ServiceResponse<CommentData> {
        val comment = commentRepository.findById(request.contentId)
        val user = userService.findUserById(request.userId)
        val command = ActivateContentCommand(user, comment, request.date, userService)

        return response(
            commandProvider.decorateCommand(command, comment)
        )
    }

    override fun remove(request: OperateOnContentRequest): ServiceResponse<CommentData> {
        val comment = commentRepository.findById(request.contentId)
        val user = userService.findUserById(request.userId)
        val command = RemoveContentCommand(user, comment, request.date, userService)

        return response(
            commandProvider.decorateCommand(command, comment)
        )
    }

    override fun giveUpVote(request: OperateOnContentRequest): ServiceResponse<RatingData> {
        val comment = commentRepository.findById(request.contentId)
        val user = userService.findUserById(request.userId)
        val command = GiveOnePointCommand(VoteType.UP, user, request.date,comment, userService)

        return ratingResponse(
            commandProvider.decorateCommand(command, comment)
        )
    }

    override fun giveDownVote(request: OperateOnContentRequest): ServiceResponse<RatingData> {
        val comment = commentRepository.findById(request.contentId)
        val user = userService.findUserById(request.userId)
        val command = GiveOnePointCommand(VoteType.DOWN, user, request.date, comment, userService)

        return ratingResponse(
            commandProvider.decorateCommand(command, comment)
        )
    }

    override fun removeVote(request: OperateOnContentRequest): ServiceResponse<RatingData> {
        val comment = commentRepository.findById(request.contentId)
        val user = userService.findUserById(request.userId)
        val command = RemoveVoteCommand(user, request.date, comment)

        return ratingResponse(
            commandProvider.decorateCommand(command, comment)
        )
    }

    override fun addComment(request: AddCommentRequest): ServiceResponse<CommentData> {
        val comment = commentRepository.findById(request.contentId)
        val user = userService.findUserById(request.commenterId)
        val command = AddCommentCommand(request.commentContent, user, request.date, comment)

        return response(
            commandProvider.decorateCommand(command, comment)
        )

    }

    override fun listComments(contentId: Long, listingParams: ListingParams): ServiceListingResponse<CommentData> {
        val comment = commentRepository.findById(contentId)
        val replies = comment.comments()
        return ServiceListingResponse(true, "Replies Retrieved!", replies.size, 1, replies.map { it.data() })
    }

    override fun report(request: ReportContentRequest): ServiceResponse<Report> {
        val comment = commentRepository.findById(request.contentId)
        val user = userService.findUserById(request.userId)

        val commandResult = ReportContentCommand(user, request.date, request.reportReason, comment, userService, reportingConfiguration)
            .execute()

        return ServiceResponse(commandResult.isSuccessful, commandResult.message, null)
    }

    override fun listReportsForContent(contentId: Long): ServiceListingResponse<Report> {
        val comment = commentRepository.findById(contentId)
        val reports = comment.reports()
        return ServiceListingResponse(true, "Reports Retrieved!", reports.size, 1, reports.toList())
    }

    private fun response(command: Command<IComment>): ServiceResponse<CommentData> {
        val executionResult = command.execute()
        val data = executionResult.returnedModel.dataWithRepliesPreview()
        return ServiceResponse(executionResult.isSuccessful, executionResult.message, data)
    }

    private fun IComment.dataWithRepliesPreview() =
       this.data(commentPreviewProvider.getPreviewFor(this))


}