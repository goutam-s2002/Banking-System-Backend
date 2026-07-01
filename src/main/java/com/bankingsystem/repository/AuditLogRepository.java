package com.bankingsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bankingsystem.entity.AuditLog;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
	

}
