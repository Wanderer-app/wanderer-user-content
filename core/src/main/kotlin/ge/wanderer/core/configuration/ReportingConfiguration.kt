package ge.wanderer.core.configuration

import ge.wanderer.core.model.content.ReportableContent
import ge.wanderer.core.model.map.IPin

interface ReportingConfiguration {

    fun shouldBeRemoved(reportableContent: ReportableContent): Boolean
    fun shouldNotifyAdministration(reportableContent: ReportableContent): Boolean
    fun shouldBeMarkedIrrelevant(pin: IPin): Boolean

}