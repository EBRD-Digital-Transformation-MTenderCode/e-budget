package com.procurement.budget.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.procurement.budget.service")
public class ServiceConfig {
}
