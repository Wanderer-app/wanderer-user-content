package ge.wanderer.service.spring.impl

import ge.wanderer.common.listing.ListingParams
import ge.wanderer.core.command.Command
import ge.wanderer.core.command.comment.AddCommentCommand
import ge.wanderer.core.command.content.ActivateContentCommand
import ge.wanderer.core.command.content.RemoveContentCommand
import ge.wanderer.core.command.content.ReportContentCommand
import ge.wanderer.core.command.pin.CreatePinCommand
import ge.wanderer.core.command.pin.ReportIrrelevantPinCommand
import ge.wanderer.core.command.pin.UpdatePinCommand
import ge.wanderer.core.command.pin.VoteForPinCommand
import ge.wanderer.core.command.rating.GiveOnePointCommand
import ge.wanderer.core.command.rating.RemoveVoteCommand
import ge.wanderer.core.configuration.ReportingConfiguration
import ge.wanderer.core.integration.user.UserService
import ge.wanderer.core.model.map.IPin
import ge.wanderer.core.model.rating.VoteType
import ge.wanderer.core.model.report.Report
import ge.wanderer.core.model.report.ReportReason
import ge.wanderer.core.repository.CommentRepository
import ge.wanderer.core.repository.PinRepository
import ge.wanderer.service.protocol.data.CommentData
import ge.wanderer.service.protocol.data.PinData
import ge.wanderer.service.protocol.data.PinMapData
import ge.wanderer.service.protocol.data.RatingData
import ge.wanderer.service.protocol.interfaces.PinService
import ge.wanderer.service.protocol.request.*
import ge.wanderer.service.protocol.response.ServiceListingResponse
import ge.wanderer.service.protocol.response.ServiceResponse
import ge.wanderer.service.spring.CommentPreviewProvider
import ge.wanderer.service.spring.command.CommandProvider
import ge.wanderer.service.spring.data.data
import ge.wanderer.service.spring.data.mapData
import ge.wanderer.service.spring.data.noDataResponse
import ge.wanderer.service.spring.data.ratingResponse
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
        val command = CreatePinCommand(request.onDate, user, request.type, request.content, request.location, request.routeCode)

        return response(
            commandProvider.decoratePersistentCommand(command, NoPin(), pinRepository)
        )
    }

    override fun listForRoute(routeCode: String, listingParams: ListingParams): ServiceListingResponse<PinMapData> {
        val pins = pinRepository.listForRoute(routeCode, listingParams)
        return ServiceListingResponse(true, "Pins Fetched!", pins.size, listingParams.batchNumber, pins.map { it.mapData() })
    }

    override fun list(listingParams: ListingParams): ServiceListingResponse<PinData> {
        val pins = pinRepository.list(listingParams)
        return ServiceListingResponse(true, "Pins Fetched!", pins.size, listingParams.batchNumber, pins.map { it.dataWithCommentsPreview() })
    }

    override fun reportIrrelevant(request: OperateOnContentRequest): ServiceResponse<PinData> {
        val user = userService.findUserById(request.userId)
        val pin = pinRepository.findById(request.contentId)
        val command = ReportIrrelevantPinCommand(user, request.date, pin, userService, reportingConfiguration)

        return response(
            commandProvider.decorateCommand(command, pin)
        )
    }

    override fun updatePin(request: UpdatePinRequest): ServiceResponse<PinData> {
        val user = userService.findUserById(request.updaterId)
        val pin = pinRepository.findById(request.pinId)
        val command = UpdatePinCommand(pin, request.newContent, user)

        return response(
            commandProvider.decorateCommand(command, pin)
        )
    }

    override fun findById(id: Long): ServiceResponse<PinData> {
        val pin = pinRepository.findById(id)
        return ServiceResponse(true, "Pin fetched!", pin.dataWithCommentsPreview())
    }

    override fun activate(request: OperateOnContentRequest): ServiceResponse<PinData> {
        val user = userService.findUserById(request.userId)
        val pin = pinRepository.findById(request.contentId)
        val command = ActivateContentCommand(user, pin,  request.date, userService)

        return response(
            commandProvider.decorateCommand(command, pin)
        )
    }

    override fun remove(request: OperateOnContentRequest): ServiceResponse<PinData> {
        val user = userService.findUserById(request.userId)
        val pin = pinRepository.findById(request.contentId)
        val command = RemoveContentCommand(user, pin, request.date, userService)

        return response(
            commandProvider.decorateCommand(command, pin)
        )
    }

    override fun giveUpVote(request: OperateOnContentRequest): ServiceResponse<RatingData> {
        val pin = pinRepository.findById(request.contentId)
        val user = userService.findUserById(request.userId)
        val command = VoteForPinCommand(VoteType.UP, user, request.date, pin, userService)

        return ratingResponse(
            commandProvider.decorateCommand(command, pin)
        )
    }

    override fun giveDownVote(request: OperateOnContentRequest): ServiceResponse<RatingData> {
        val pin = pinRepository.findById(request.contentId)
        val user = userService.findUserById(request.userId)
        val command = VoteForPinCommand(VoteType.DOWN, user, request.date, pin, userService)

        return ratingResponse(
            commandProvider.decorateCommand(command, pin)
        )
    }

    override fun removeVote(request: OperateOnContentRequest): ServiceResponse<RatingData> {
        val pin = pinRepository.findById(request.contentId)
        val user = userService.findUserById(request.userId)
        val command = RemoveVoteCommand(user, request.date, pin)

        return ratingResponse(
            commandProvider.decorateCommand(command, pin)
        )
    }

    override fun addComment(request: AddCommentRequest): ServiceResponse<CommentData> {
        val pin = pinRepository.findById(request.contentId)
        val user = userService.findUserById(request.commenterId)
        val command = AddCommentCommand(request.commentContent, user, request.date, pin, userService)

        val result = commandProvider.decorateCommand(command, pin).execute()
        return ServiceResponse(result.isSuccessful, result.message, result.returnedModel.comments().last().data())
    }

    override fun listComments(contentId: Long, listingParams: ListingParams): ServiceListingResponse<CommentData> {
        val pin = pinRepository.findById(contentId)
        val commentsData = commentRepository.listActiveFor(pin, listingParams)
            .map { it.data(commentPreviewProvider.getPreviewFor(it)) }

        return ServiceListingResponse(true, "Comments fetched!", commentsData.size, listingParams.batchNumber, commentsData)
    }

    override fun report(request: ReportContentRequest): ServiceResponse<Report> {
        check(request.reportReason != ReportReason.IRRELEVANT) { "Use reportIrrelevant() method for reporting a pin as irrelevant" }

        val pin = pinRepository.findById(request.contentId)
        val user = userService.findUserById(request.userId)

        val commandResult = ReportContentCommand(user, request.date, request.reportReason, pin, userService, reportingConfiguration)
            .execute()

        return noDataResponse(commandResult.isSuccessful, commandResult.message)
    }

    override fun listReportsForContent(contentId: Long): ServiceListingResponse<Report> {
        val pin = pinRepository.findById(contentId)
        val reports = pin.reports()
        return ServiceListingResponse(true, "Reports Retrieved!", reports.size, 1, reports.toList())
    }

    private fun IPin.dataWithCommentsPreview(): PinData {
        return this.data(commentPreviewProvider.getPreviewFor(this))
    }

    private fun response(command: Command<IPin>): ServiceResponse<PinData> {
        val executionResult = command.execute()
        val data = executionResult.returnedModel.dataWithCommentsPreview()
        return ServiceResponse(executionResult.isSuccessful, executionResult.message, data)
    }
}