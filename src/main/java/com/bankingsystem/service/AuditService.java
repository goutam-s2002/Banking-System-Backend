package com.bankingsystem.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.bankingsystem.entity.AuditLog;
import com.bankingsystem.repository.AuditLogRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    // Added: Save Audit Log (01-07-2026)
    public void saveLog(String username, String action) {

        AuditLog log = new AuditLog();

        log.setUsername(username);
        log.setAction(action);
        log.setTimestamp(LocalDateTime.now());

        auditLogRepository.save(log);
    }

    // Added: Get All Audit Logs (01-07-2026)
    public List<AuditLog> getAllLogs() {

        return auditLogRepository.findAll();
    }

}