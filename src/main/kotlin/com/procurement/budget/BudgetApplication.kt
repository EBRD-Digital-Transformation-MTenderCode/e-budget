package com.procurement.budget

import com.procurement.budget.config.ApplicationConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient

@SpringBootApplication(scanBasePackageClasses = [ApplicationConfig::class])
@EnableEurekaClient
class BudgetApplication

fun main(args: Array<String>) {
    runApplication<BudgetApplication>(*args)
}
