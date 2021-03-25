package ge.wanderer.service.spring.data

import ge.wanderer.core.model.content.RateableContent
import ge.wanderer.core.model.discussion.DiscussionElement
import ge.wanderer.service.protocol.data.RatingData

fun RateableContent.ratingData() =
    RatingData(this.rating())

fun DiscussionElement.ratingData(): RatingData? =
    if (this is RateableContent) {
        RatingData(this.rating())
    } else {
        null
    }