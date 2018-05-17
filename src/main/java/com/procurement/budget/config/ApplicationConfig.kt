package com.procurement.budget.config

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(WebConfig::class, DaoConfiguration::class, ServiceConfig::class, JsonConfig::class)
class ApplicationConfig

