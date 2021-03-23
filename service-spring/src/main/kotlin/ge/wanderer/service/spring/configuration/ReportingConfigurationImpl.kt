package ge.wanderer.service.spring.configuration

import ge.wanderer.core.configuration.ReportingConfiguration
import ge.wanderer.core.model.content.ReportableContent
import ge.wanderer.core.model.map.IPin
import org.springframework.stereotype.Component

@Component
class ReportingConfigurationImpl: ReportingConfiguration {

    override fun shouldBeRemoved(reportableContent: ReportableContent): Boolean {
        return false
    }

    override fun shouldNotifyAdministration(reportableContent: ReportableContent): Boolean {
        return false
    }

    override fun shouldBeMarkedIrrelevant(pin: IPin): Boolean {
        return false
    }
}