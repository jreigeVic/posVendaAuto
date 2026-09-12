package com.soat.posvendaauto.auditoria;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LogAuditoriaRepository extends JpaRepository<LogAuditoria, UUID> {
}
