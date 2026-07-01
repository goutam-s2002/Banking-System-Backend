package com.bankingsystem.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bankingsystem.dto.DashboardResponse;
import com.bankingsystem.service.DashboardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    // Added: Admin Dashboard API (01-07-2026)
    @GetMapping("/dashboard")
    public DashboardResponse getDashboard() {

        return dashboardService.getDashboard();
    }
}