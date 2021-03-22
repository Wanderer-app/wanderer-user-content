package ge.wanderer.service.protocol.interfaces.base

import ge.wanderer.service.protocol.request.ModifyContentRequest
import ge.wanderer.service.protocol.response.ServiceResponse

interface UserContentService<T> {

    fun findById(id: Long): T
    fun activate(request: ModifyContentRequest): ServiceResponse<T>
    fun remove(request: ModifyContentRequest): ServiceResponse<T>
}