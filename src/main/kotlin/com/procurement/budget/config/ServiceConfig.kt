package com.procurement.budget.config

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(basePackages = ["com.procurement.budget.service"])
@EnableConfigurationProperties(value = [OCDSProperties::class])
class ServiceConfig
