package com.procurement.budget.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "ocds")
class OCDSProperties(
    var prefixes: Prefixes? = null
) {
    class Prefixes(
        var main: String? = null,
        var test: String? = null
    )
}
