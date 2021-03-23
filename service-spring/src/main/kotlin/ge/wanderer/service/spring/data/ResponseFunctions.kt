package ge.wanderer.service.spring.data

import ge.wanderer.core.command.Command
import ge.wanderer.core.model.content.RateableContent
import ge.wanderer.service.protocol.data.RatingData
import ge.wanderer.service.protocol.response.ServiceResponse

fun ratingResponse(command: Command<RateableContent>): ServiceResponse<RatingData> {
    val executionResult = command.execute()
    val data = executionResult.returnedModel.ratingData()
    return ServiceResponse(executionResult.isSuccessful, executionResult.message, data)
}