package ge.wanderer.core.model.discussion.post

import ge.wanderer.core.model.content.RateableContent
import ge.wanderer.core.model.content.ReportableContent
import ge.wanderer.core.model.discussion.DiscussionElement

interface IPost: DiscussionElement, RateableContent, ReportableContent {
}