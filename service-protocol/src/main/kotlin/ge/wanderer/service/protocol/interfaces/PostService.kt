package ge.wanderer.service.protocol.interfaces

import ge.wanderer.service.protocol.data.DiscussionElementData
import ge.wanderer.service.protocol.interfaces.base.CommentableContentService
import ge.wanderer.service.protocol.interfaces.base.RateableContentService
import ge.wanderer.service.protocol.interfaces.base.ReportableContentService
import ge.wanderer.service.protocol.interfaces.base.UserContentService
import ge.wanderer.service.protocol.request.CreatePostRequest
import ge.wanderer.service.protocol.request.UpdatePostRequest
import ge.wanderer.service.protocol.response.ServiceResponse

interface PostService : UserContentService<DiscussionElementData>, RateableContentService, CommentableContentService, ReportableContentService {

    fun createPost(request: CreatePostRequest): ServiceResponse<DiscussionElementData>
    fun updatePost(request: UpdatePostRequest): ServiceResponse<DiscussionElementData>
}