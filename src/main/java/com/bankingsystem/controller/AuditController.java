package com.bankingsystem.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bankingsystem.entity.AuditLog;
import com.bankingsystem.service.AuditService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AuditController {

    private final AuditService auditService;

    // Added: Get Audit Logs API (01-07-2026)
    @GetMapping("/audit")
    public List<AuditLog> getAllLogs() {

        return auditService.getAllLogs();
    }

}