package org.asue24.financetrackerbackend.dto;

import org.asue24.financetrackerbackend.entities.Transaction;

public record TransactionBody(Transaction transaction, int senderId, int receiverId){}
