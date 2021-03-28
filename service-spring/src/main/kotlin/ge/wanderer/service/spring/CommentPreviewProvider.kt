package ge.wanderer.service.spring

import ge.wanderer.common.enums.UserContentType
import ge.wanderer.core.model.content.CommentableContent
import ge.wanderer.service.protocol.data.CommentData
import ge.wanderer.service.spring.data.data
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.PropertySource
import org.springframework.stereotype.Component

@Component
@PropertySource("classpath:user-content.properties")
class CommentPreviewProvider(
    @Autowired private val commentPreviewProperties: Map<UserContentType, Int>
) {
    fun getPreviewFor(content: CommentableContent): List<CommentData> =
        content.comments()
            .asSequence()
            .sortedByDescending { it.rating() }
            .map { it.data() }
            .take(commentPreviewProperties[content.contentType()] ?: error("Comments preview not available for ${content.contentType()}"))
            .toList()
}