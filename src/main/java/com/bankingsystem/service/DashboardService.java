package com.bankingsystem.service;

import org.springframework.stereotype.Service;

import com.bankingsystem.dto.DashboardResponse;
import com.bankingsystem.entity.AccountStatus;
import com.bankingsystem.repository.AccountRepository;
import com.bankingsystem.repository.TransactionRepository;
import com.bankingsystem.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    // Added: Admin Dashboard (01-07-2026)
    public DashboardResponse getDashboard() {

        DashboardResponse response = new DashboardResponse();

        response.setTotalUsers(userRepository.count());
        response.setTotalAccounts(accountRepository.count());
        response.setActiveAccounts(accountRepository.countByStatus(AccountStatus.ACTIVE));
        response.setBlockedAccounts(accountRepository.countByStatus(AccountStatus.BLOCKED));
        response.setClosedAccounts(accountRepository.countByStatus(AccountStatus.CLOSED));
        response.setTotalTransactions(transactionRepository.count());

        return response;
    }
}