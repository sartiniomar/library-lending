package com.sartiniomar.library.loan.infrastructure.persistence.model;

import com.sartiniomar.library.loan.domain.loan.LoanStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "loan")
@Data
public class LoanEntity {

  @Id
  private UUID id;
  private UUID patronId;
  private UUID bookInstanceId;
  private LoanStatus status;
  private Instant reservedAt;
  private Instant lentAt;
  private Instant dueAt;
  private Instant returnedAt;

  public LoanEntity() {}

  public LoanEntity(
      UUID id,
      UUID bookInstanceId,
      UUID patronId,
      LoanStatus status,
      Instant reservedAt,
      Instant lentAt,
      Instant dueAt,
      Instant returnedAt) {
    this.id = id;
    this.bookInstanceId = bookInstanceId;
    this.patronId = patronId;
    this.status = status;
    this.reservedAt = reservedAt;
    this.lentAt = lentAt;
    this.dueAt = dueAt;
    this.returnedAt = returnedAt;
  }
}