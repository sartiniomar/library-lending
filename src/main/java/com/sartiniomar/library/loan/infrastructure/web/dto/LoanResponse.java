package com.sartiniomar.library.loan.infrastructure.web.dto;

import com.sartiniomar.library.loan.domain.loan.LoanStatus;
import java.util.UUID;

public record LoanResponse(
    UUID id,
    UUID patronId,
    UUID bookInstanceId,
    LoanStatus status,
    String reservedAt,
    String lentAt,
    String dueAt,
    String returnedAt
) {
}
