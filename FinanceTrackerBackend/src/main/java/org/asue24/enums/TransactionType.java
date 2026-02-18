package org.asue24.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.NUMBER)
public enum TransactionType {
    INCOME,
    EXPENSE,
    TRANSFER,
}
