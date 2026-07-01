package com.bankingsystem.dto;

import lombok.Data;

// Added: Admin Dashboard DTO (01-07-2026)
@Data
public class DashboardResponse {

    private long totalUsers;

    private long totalAccounts;

    private long activeAccounts;

    private long blockedAccounts;

    private long closedAccounts;

    private long totalTransactions;

}