package ge.wanderer.service.protocol.interfaces.base

import ge.wanderer.service.protocol.request.OperateOnContentRequest
import ge.wanderer.service.protocol.response.ServiceResponse

interface UserContentService<T> {

    fun findById(id: Long, userId: Long): ServiceResponse<T>
    fun activate(request: OperateOnContentRequest): ServiceResponse<T>
    fun remove(request: OperateOnContentRequest): ServiceResponse<T>
}