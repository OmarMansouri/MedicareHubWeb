package medicare.back.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import medicare.back.models.DiagnosticSession;

public interface DiagnosticSessionRepository extends JpaRepository<DiagnosticSession, Long> {} 
