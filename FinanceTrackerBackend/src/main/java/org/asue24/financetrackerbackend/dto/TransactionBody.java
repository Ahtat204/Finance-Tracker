package org.asue24.financetrackerbackend.dto;

import org.asue24.financetrackerbackend.entities.Transaction;

import java.util.Optional;

public record TransactionBody(Transaction transaction, int senderId, Optional<Integer> receiverId){}
