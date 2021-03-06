package ge.wanderer.service.spring.impl

import ge.wanderer.common.enums.ReportReason
import ge.wanderer.core.command.Command
import ge.wanderer.core.command.comment.AddCommentCommand
import ge.wanderer.core.command.content.ActivateContentCommand
import ge.wanderer.core.command.content.RemoveContentCommand
import ge.wanderer.core.command.content.ReportContentCommand
import ge.wanderer.core.command.pin.CreatePinCommand
import ge.wanderer.core.command.pin.ReportIrrelevantPinCommand
import ge.wanderer.core.command.pin.UpdatePinCommand
import ge.wanderer.core.command.pin.VoteForPinCommand
import ge.wanderer.core.command.rating.RemoveVoteCommand
import ge.wanderer.core.configuration.ReportingConfiguration
import ge.wanderer.core.integration.file.AttachedFile
import ge.wanderer.core.integration.user.UserService
import ge.wanderer.core.model.map.IPin
import ge.wanderer.core.model.map.PinContent
import ge.wanderer.common.enums.VoteType
import ge.wanderer.common.listing.ListingParams
import ge.wanderer.core.integration.user.User
import ge.wanderer.persistence.repository.CommentRepository
import ge.wanderer.persistence.repository.PinRepository
import ge.wanderer.service.protocol.data.*
import ge.wanderer.service.protocol.interfaces.PinService
import ge.wanderer.service.protocol.request.*
import ge.wanderer.service.protocol.response.ServiceListingResponse
import ge.wanderer.service.protocol.response.ServiceResponse
import ge.wanderer.service.spring.CommentPreviewProvider
import ge.wanderer.service.spring.command.CommandProvider
import ge.wanderer.service.spring.data.*
import ge.wanderer.service.spring.logger
import ge.wanderer.service.spring.model.NoComment
import ge.wanderer.service.spring.model.NoPin
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class PinServiceImpl(
    @Autowired private val pinRepository: PinRepository,
    @Autowired private val userService: UserService,
    @Autowired private val commentPreviewProvider: CommentPreviewProvider,
    @Autowired private val commandProvider: CommandProvider,
    @Autowired private val reportingConfiguration: ReportingConfiguration,
    @Autowired private val commentRepository: CommentRepository
): PinService {

    override fun createPin(request: CreatePinRequest): ServiceResponse<PinData> {
        val user = userService.findUserById(request.userId)
        val content = PinContent(request.title, request.text, request.attachedFile?.let { AttachedFile(it.externalId, it.fileType) }?:let { null })
        val command = CreatePinCommand(request.onDate, user, request.type, content, request.location, request.routeCode)

        return response(
            commandProvider.decoratePersistentCommand(command, NoPin(), pinRepository, logger()), user
        )
    }

    override fun listForRoute(routeCode: String, listingParams: ListingParams): ServiceListingResponse<PinMapData> {
        val pins = pinRepository.listForRoute(routeCode, listingParams)
        return ServiceListingResponse(true, "Pins Fetched!", pins.size, listingParams.batchNumber, pins.map { it.mapData() })
    }

    override fun list(listingParams: ListingParams, requestingUserId: String?): ServiceListingResponse<PinData> {
        val pins = pinRepository.list(listingParams)
        return ServiceListingResponse(true, "Pins Fetched!", pins.size, listingParams.batchNumber, pins.map { it.dataWithCommentsPreview(getRequestingUser(requestingUserId, userService)) })
    }

    override fun reportIrrelevant(request: OperateOnContentRequest): ServiceResponse<PinData> {
        val user = userService.findUserById(request.userId)
        val pin = pinRepository.findById(request.contentId)
        val command = ReportIrrelevantPinCommand(user, request.date, pin, userService, reportingConfiguration)

        return response(
            commandProvider.decorateCommand(command, pin, logger()), user
        )
    }

    override fun updatePin(request: UpdatePinRequest): ServiceResponse<PinData> {
        val user = userService.findUserById(request.updaterId)
        val pin = pinRepository.findById(request.pinId)
        val command = UpdatePinCommand(pin, PinContent(request.newTitle, request.newText, request.newFile?.let { AttachedFile(it.externalId, it.fileType) }?:let { null }), user)

        return response(
            commandProvider.decorateCommand(command, pin, logger()), user
        )
    }

    override fun findById(id: Long, requestingUserId: String?): ServiceResponse<PinData> {
        val pin = pinRepository.findById(id)
        return ServiceResponse(true, "Pin fetched!", pin.dataWithCommentsPreview(getRequestingUser(requestingUserId, userService)))
    }

    override fun activate(request: OperateOnContentRequest): ServiceResponse<PinData> {
        val user = userService.findUserById(request.userId)
        val pin = pinRepository.findById(request.contentId)
        val command = ActivateContentCommand(user, pin,  request.date, userService)

        return response(
            commandProvider.decorateCommand(command, pin, logger()), user
        )
    }

    override fun remove(request: OperateOnContentRequest): ServiceResponse<PinData> {
        val user = userService.findUserById(request.userId)
        val pin = pinRepository.findById(request.contentId)
        val command = RemoveContentCommand(user, pin, request.date, userService)

        return response(
            commandProvider.decorateCommand(command, pin, logger()), user
        )
    }

    override fun giveUpVote(request: OperateOnContentRequest): ServiceResponse<RatingData> {
        val pin = pinRepository.findById(request.contentId)
        val user = userService.findUserById(request.userId)
        val command = VoteForPinCommand(VoteType.UP, user, request.date, pin, userService)

        return ratingResponse(
            commandProvider.decorateCommand(command, pin, logger())
        )
    }

    override fun giveDownVote(request: OperateOnContentRequest): ServiceResponse<RatingData> {
        val pin = pinRepository.findById(request.contentId)
        val user = userService.findUserById(request.userId)
        val command = VoteForPinCommand(VoteType.DOWN, user, request.date, pin, userService)

        return ratingResponse(
            commandProvider.decorateCommand(command, pin, logger())
        )
    }

    override fun removeVote(request: OperateOnContentRequest): ServiceResponse<RatingData> {
        val pin = pinRepository.findById(request.contentId)
        val user = userService.findUserById(request.userId)
        val command = RemoveVoteCommand(user, request.date, pin)

        return ratingResponse(
            commandProvider.decorateCommand(command, pin, logger())
        )
    }

    override fun addComment(request: AddCommentRequest): ServiceResponse<CommentData> {
        val pin = pinRepository.findById(request.contentId)
        val user = userService.findUserById(request.commenterId)
        val command = AddCommentCommand(request.commentContent, user, request.date, pin, userService)

        val result = commandProvider.decorateCommand(command, NoComment(), logger()).execute()
        return ServiceResponse(result.isSuccessful, result.message, result.returnedModel.data(user))
    }

    override fun listComments(request: ListCommentsRequest): ServiceListingResponse<CommentData> {
        val pin = pinRepository.findById(request.contentId)
        val requestingUser = getRequestingUser(request.requestingUserId, userService)
        val commentsData = commentRepository.listActiveFor(pin, request.listingParams)
            .map { it.data(requestingUser, commentPreviewProvider.getPreviewFor(it, requestingUser)) }

        return ServiceListingResponse(true, "Comments fetched!", commentsData.size, request.listingParams.batchNumber, commentsData)
    }

    override fun report(request: ReportContentRequest): ServiceResponse<ReportData> {
        check(request.reportReason != ReportReason.IRRELEVANT) { "Use reportIrrelevant() method for reporting a pin as irrelevant" }

        val pin = pinRepository.findById(request.contentId)
        val user = userService.findUserById(request.userId)

        val command = ReportContentCommand(user, request.date, request.reportReason, pin, userService, reportingConfiguration)
        val commandResult = commandProvider.decorateCommand(command, pin, logger()).execute()

        return noDataResponse(commandResult.isSuccessful, commandResult.message)
    }

    override fun listReportsForContent(contentId: Long): ServiceListingResponse<ReportData> {
        val pin = pinRepository.findById(contentId)
        val reports = pin.reports()
        return ServiceListingResponse(true, "Reports Retrieved!", reports.size, 1, reports.map { it.data() })
    }

    private fun IPin.dataWithCommentsPreview(user: User): PinData {
        return this.data(commentPreviewProvider.getPreviewFor(this, user), user)
    }

    private fun response(command: Command<IPin>, user: User): ServiceResponse<PinData> {
        val executionResult = command.execute()
        val data = executionResult.returnedModel.dataWithCommentsPreview(user)
        return ServiceResponse(executionResult.isSuccessful, executionResult.message, data)
    }
}