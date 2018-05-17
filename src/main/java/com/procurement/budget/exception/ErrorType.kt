package com.procurement.notice.exception

enum class ErrorType constructor(val code: String, val message: String) {
    EI_NOT_FOUND("00.01", "EI not found."),
    FS_NOT_FOUND("00.02", "FS not found."),
    INVALID_OWNER("00.03", "Invalid owner."),
    INVALID_CPV("00.04", "Invalid CPV."),
    INVALID_CURRENCY("00.05", "Invalid currency."),
    INVALID_AMOUNT("00.06", "Invalid amount."),
    INVALID_STATUS("00.07", "Financial source status invalid."),
    INVALID_PERIOD("01.01", "Invalid period."),
    INVALID_DATE("01.02", "Date does not match the period.");
}
