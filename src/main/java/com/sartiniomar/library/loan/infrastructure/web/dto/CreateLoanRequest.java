package com.sartiniomar.library.loan.infrastructure.web.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record CreateLoanRequest(
    @NotNull(message = "bookInstanceId is required")
    UUID bookInstanceId,
    @NotNull(message = "patronId is required")
    UUID patronId
) {
  public UUID getPatronId() {
    return this.patronId;
  }

  public UUID getBookInstanceId() {
    return this.bookInstanceId;
  }
}
