package com.bankingsystem.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.bankingsystem.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByFromAccountIdOrToAccountId(Long fromId, Long toId);

    Page<Transaction> findByFromAccountIdOrToAccountId(
            Long fromId,
            Long toId,
            Pageable pageable
    );

    List<Transaction> findByFromAccountIdOrToAccountIdAndDateBetween(
            Long fromId,
            Long toId,
            Date from,
            Date to
    );

    // Added: Dashboard Total Transactions (01-07-2026)
    long count();

}