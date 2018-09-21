package com.procurement.budget

import com.procurement.budget.config.ApplicationConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackageClasses = [ApplicationConfig::class])
class BudgetApplication

fun main(args: Array<String>) {
    runApplication<BudgetApplication>(*args)
}
