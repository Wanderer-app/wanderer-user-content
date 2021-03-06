package ge.wanderer.service.spring.impl

import ge.wanderer.core.command.Command
import ge.wanderer.core.command.comment.AddCommentCommand
import ge.wanderer.core.command.content.ActivateContentCommand
import ge.wanderer.core.command.content.RemoveContentCommand
import ge.wanderer.core.command.discussion.*
import ge.wanderer.core.integration.user.UserService
import ge.wanderer.core.model.UpdateDiscussionElementData
import ge.wanderer.core.model.discussion.poll.IPoll
import ge.wanderer.core.integration.user.User
import ge.wanderer.persistence.repository.CommentRepository
import ge.wanderer.persistence.repository.PollRepository
import ge.wanderer.service.protocol.data.CommentData
import ge.wanderer.service.protocol.data.DiscussionElementData
import ge.wanderer.service.protocol.interfaces.PollService
import ge.wanderer.service.protocol.request.*
import ge.wanderer.service.protocol.response.ServiceListingResponse
import ge.wanderer.service.protocol.response.ServiceResponse
import ge.wanderer.service.spring.CommentPreviewProvider
import ge.wanderer.service.spring.command.CommandProvider
import ge.wanderer.service.spring.data.data
import ge.wanderer.service.spring.data.getRequestingUser
import ge.wanderer.service.spring.logger
import ge.wanderer.service.spring.model.NoComment
import ge.wanderer.service.spring.model.NoPoll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class PollServiceImpl(
    @Autowired private val userService: UserService,
    @Autowired private val commentPreviewProvider: CommentPreviewProvider,
    @Autowired private val commandProvider: CommandProvider,
    @Autowired private val pollRepository: PollRepository,
    @Autowired private val commentRepository: CommentRepository
): PollService {

    override fun createPoll(request: CreatePollRequest): ServiceResponse<DiscussionElementData> {
        val user = userService.findUserById(request.userId)
        val command = CreatePollCommand(request.onDate, user, request.text, request.routeCode, request.answers)

        return response (
            commandProvider.decoratePersistentCommand(command, NoPoll(), pollRepository, logger()), user
        )
    }

    override fun updatePoll(request: UpdatePollRequest): ServiceResponse<DiscussionElementData> {
        val user = userService.findUserById(request.updaterId)
        val poll = pollRepository.findById(request.pollId)
        val updateData = UpdateDiscussionElementData(request.newText, listOf())
        val command = UpdateDiscussionElementCommand(poll, updateData, user)

        return response(
            commandProvider.decorateCommand(command, poll, logger()), user
        )
    }

    override fun addAnswer(request: AddAnswerToPollRequest): ServiceResponse<DiscussionElementData> {
        val user = userService.findUserById(request.userId)
        val poll = pollRepository.findById(request.pollId)
        val command = AddPollAnswerCommand(poll, request.answerText, request.date, user)

        return response(
            commandProvider.decorateCommand(command, poll, logger()), user
        )
    }

    override fun removeAnswer(request: RemovePollAnswerRequest): ServiceResponse<DiscussionElementData> {
        val user = userService.findUserById(request.userId)
        val poll = pollRepository.findById(request.pollId)
        val command = RemovePollAnswerCommand(poll, user, request.answerId, request.date, userService)

        return response(
            commandProvider.decorateCommand(command, poll, logger()), user
        )
    }

    override fun selectAnswer(request: SelectPollAnswerRequest): ServiceResponse<DiscussionElementData> {
        val user = userService.findUserById(request.userId)
        val poll = pollRepository.findById(request.pollId)
        val command = SelectPollAnswerCommand(poll, request.answerId, user)

        return response(
            commandProvider.decorateCommand(command, poll, logger()), user
        )
    }

    override fun findById(id: Long, requestingUserId: String?): ServiceResponse<DiscussionElementData> =
        ServiceResponse(true, "Poll fetched", pollRepository.findById(id).dataWithCommentsPreview(getRequestingUser(requestingUserId, userService)))


    override fun activate(request: OperateOnContentRequest): ServiceResponse<DiscussionElementData> {
        val user = userService.findUserById(request.userId)
        val poll = pollRepository.findById(request.contentId)
        val command = ActivateContentCommand(user, poll, request.date, userService)

        return response(
            commandProvider.decorateCommand(command, poll, logger()), user
        )
    }

    override fun remove(request: OperateOnContentRequest): ServiceResponse<DiscussionElementData> {
        val user = userService.findUserById(request.userId)
        val poll = pollRepository.findById(request.contentId)
        val command = RemoveContentCommand(user, poll, request.date, userService)

        return response(
            commandProvider.decorateCommand(command, poll, logger()), user
        )
    }

    override fun addComment(request: AddCommentRequest): ServiceResponse<CommentData> {
        val poll = pollRepository.findById(request.contentId)
        val user = userService.findUserById(request.commenterId)
        val command = AddCommentCommand(request.commentContent, user, request.date, poll, userService)

        val result = commandProvider.decorateCommand(command, NoComment(), logger()).execute()
        return ServiceResponse(result.isSuccessful, result.message, result.returnedModel.data(user))
    }

    override fun listComments(request: ListCommentsRequest): ServiceListingResponse<CommentData> {
        val poll = pollRepository.findById(request.contentId)
        val user = getRequestingUser(request.requestingUserId, userService)
        val commentsData = commentRepository.listActiveFor(poll, request.listingParams)
            .map { it.data(user, commentPreviewProvider.getPreviewFor(it, user)) }

        return ServiceListingResponse(true, "Comments fetched!", commentsData.size, request.listingParams.batchNumber, commentsData)
    }

    private fun IPoll.dataWithCommentsPreview(user: User): DiscussionElementData {
        return this.data(commentPreviewProvider.getPreviewFor(this, user), user)
    }

    private fun response(command: Command<IPoll>, user: User): ServiceResponse<DiscussionElementData> {
        val executionResult = command.execute()
        val data = executionResult.returnedModel.dataWithCommentsPreview(user)
        return ServiceResponse(executionResult.isSuccessful, executionResult.message, data)
    }

}