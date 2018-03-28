package com.procurement.budget.exception;

public enum ErrorType {

    DATA_NOT_FOUND("00.01", "Data not found."),
    INVALID_OWNER("00.02", "Invalid owner."),
    INVALID_CPV("00.03", "Invalid CPV."),
    INVALID_CURRENCY("00.04", "Invalid currency."),
    INVALID_AMOUNT("00.05", "Invalid amount."),
    INVALID_STATUS("00.06", "Financial source status invalid."),
    INVALID_PERIOD("01.02", "Invalid period."),
    INVALID_DATE("01.03", "Date does not match the period.");

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
