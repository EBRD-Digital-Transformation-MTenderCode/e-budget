package com.procurement.budget.exception

enum class ErrorType constructor(val code: String, val message: String) {
    EI_NOT_FOUND("00.01", "EI not found."),
    FS_NOT_FOUND("00.02", "FS not found."),
    INVALID_OWNER("00.03", "Invalid owner."),
    INVALID_TOKEN("00.04", "Invalid token."),
    INVALID_CPV("00.05", "Invalid CPV."),
    INVALID_CURRENCY("00.06", "Invalid currency."),
    INVALID_AMOUNT("00.07", "Invalid amount."),
    INVALID_STATUS("00.08", "Financial source status invalid."),
    INVALID_SOURCE_ENTITY("00.09", "FS source entity invalid."),
    INVALID_BUDGET_ID("00.10", "Budget id must not be empty."),
    INVALID_BUYER_ID("00.11", "Buyer id must not be empty."),
    INVALID_PERIOD("01.01", "Invalid period."),
    INVALID_DATE("01.02", "Date does not match the period."),
    PARAM_ERROR("01.03", "Should not be empty for this type of operation");
}
