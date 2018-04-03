package com.procurement.budget.exception;

public enum ErrorType {

    EI_NOT_FOUND("00.01", "EI not found."),
    FS_NOT_FOUND("00.02", "FS not found."),
    INVALID_OWNER("00.03", "Invalid owner."),
    INVALID_CPV("00.04", "Invalid CPV."),
    INVALID_CURRENCY("00.05", "Invalid currency."),
    INVALID_AMOUNT("00.06", "Invalid amount."),
    INVALID_STATUS("00.07", "Financial source status invalid."),
    INVALID_PERIOD("01.01", "Invalid period."),
    INVALID_DATE("01.02", "Date does not match the period.");

    private final String code;
    private final String message;

    ErrorType(final String code, final String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }
}
